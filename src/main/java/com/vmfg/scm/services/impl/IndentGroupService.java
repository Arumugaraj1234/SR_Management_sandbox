package com.vmfg.scm.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.design.request.IdAndTenantIdRequest;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.project.request.BudgetExcessSheetRequest;
import com.vmfg.project.service.interfaces.IBudgetExcessSheetService;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.dao.impl.GrnDAO;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.scm.dao.impl.PoDAO;
import com.vmfg.scm.dao.interfaces.IIndentGroupDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.entity.GetPoDtlsEntity;
import com.vmfg.scm.entity.IndentGroupDetailsEntity;
import com.vmfg.scm.entity.IndentGroupHdrAndDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpVenDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpVenEntity;
import com.vmfg.scm.entity.IndentGrpScpVenPtEntity;
import com.vmfg.scm.entity.IndentGrpScsStatusEntity;
import com.vmfg.scm.entity.PoCancelEntity;
import com.vmfg.scm.entity.PoDispatchDocEntity;
import com.vmfg.scm.entity.PoDtlEntity;
import com.vmfg.scm.entity.PoPaymentTermEntity;
import com.vmfg.scm.entity.ScpDtlsEntity;
import com.vmfg.scm.request.DeleteIndScpDtlIdRequest;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.IndentGrpDelRequest;
import com.vmfg.scm.request.IndentGrpDtlRequest;
import com.vmfg.scm.request.IndentInsertGrpRequest;
import com.vmfg.scm.request.IndentTemplateNameRequest;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.scm.request.getIndentQtyDtlRequest;
import com.vmfg.scm.services.interfaces.IIndentGroupService;
import com.vmfg.scm.services.interfaces.IPoServices;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class IndentGroupService implements IIndentGroupService {
	private static final Logger logger = LoggerFactory.getLogger(IndentGroupService.class);

	@Autowired
	IIndentGroupDAO iIndentGroupDAO;

	@Autowired
	IndentUploadDAO indentUploadDAO;

	@Autowired
	DesignTaskDAO designTaskDAO;

	@Autowired
	GrnDAO igrnDao;

	@Autowired
	StageManagementDAO stageManagementDAO;


	@Autowired
	private UploadManagementDAO uploadManagementDAO;

	@Autowired
	private IPoServices  ipoServices;

	@Autowired
	IBudgetExcessSheetService iBudgetExcessSheetService;

	@Autowired
	PoDAO poDAO;

	@Autowired
	ProjectDAO projectDAO;

	@Autowired
	CommonNotifyMethod commonNotifyMethod;

	@Autowired
	IndentGroupDAO indentGroupDAO;

	@Autowired
	private IPoDAO iPoDAO;

	@Override
	public ResponseAsList getIndentGroupDetails(IndentGrpDtlRequest indentGrpDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<IndentGroupDetailsEntity> list = new ArrayList<IndentGroupDetailsEntity>();
		try {
			String fromDate = indentGrpDtlReq.getFromDate();
			String toDate = indentGrpDtlReq.getToDate();
			String tenantId = indentGrpDtlReq.getTenantId();
			String indentId = indentGrpDtlReq.getIndentId();
			String processDoc = indentGrpDtlReq.getProcessCode();
			String partCount="";
			list = iIndentGroupDAO.getIndentGroupRetrieve(tenantId, fromDate, toDate, indentId,indentGrpDtlReq.getProjectId(),indentGrpDtlReq.getEmpId());
			if(list.size()>0) {
				// docGroupList only depends on tenantId/processDoc (constant for this whole request) - fetch once
				// instead of once per row. The doc-lifecycle/status lookups below repeat the same (currSeq, docGroup)
				// / docStatus combinations across many rows too - cache them per-request (read-only lookups,
				// never mutated after being fetched, so sharing the same cached instance across rows is safe).
				List<String> docGroupList = iIndentGroupDAO.getDistinctScsDocGroup("DC038", tenantId, processDoc);
				Map<String, List<DocumentStatusMstEntity>> nextSeqDocCache = new HashMap<>();
				Map<String, String> statusDescCache = new HashMap<>();
				for(int i=0;i<list.size();i++) {
					String vendorQualified=iIndentGroupDAO.getVenQualifiedByIgHdrId(list.get(i).getIgHdrId());
					String basicTotalCol="";
					if(!vendorQualified.equalsIgnoreCase("NA")) {
						if(vendorQualified.equalsIgnoreCase("L1")) {
							basicTotalCol="L1_FINAL_SUB_TOTAL";
						}else if(vendorQualified.equalsIgnoreCase("L2")) {
							basicTotalCol="L2_FINAL_SUB_TOTAL";
						}else  {
							basicTotalCol="L3_FINAL_SUB_TOTAL";
						}
						String venDtlBasicCost = iIndentGroupDAO.venDtlBasicCost(list.get(i).getIgHdrId(),basicTotalCol);
						list.get(i).setFinalCost(venDtlBasicCost);
						String venDtlBasicCostFx = indentGroupDAO.venDtlBasicCostFx(list.get(i).getIgHdrId(),basicTotalCol);
						list.get(i).setFinalCostFx(venDtlBasicCostFx);
					}else {
						list.get(i).setFinalCost("0");
					}
					int verFlag = iIndentGroupDAO.pjsversioncheck(list.get(i).getIgHdrId());
					list.get(i).setVersionCheck(Integer.toString(verFlag));
					String	docGroup = resolveScsDocGroup(list.get(i).getFinalCost(), docGroupList);

					List<ScpDtlsEntity>	mainList=iIndentGroupDAO.getScpDtlsByIgHdrId(list.get(i).getIgHdrId(),tenantId);
					String currSeq = "1";
					if(mainList.size()>0) {
						currSeq=mainList.get(0).getSeqNo();
					}
					String indentGroupSCSType = iIndentGroupDAO.indentGroupSCSType(list.get(i).getIgHdrId());
					list.get(i).setType(indentGroupSCSType);
					String seqGroupKey = currSeq + "|" + docGroup;
					List<DocumentStatusMstEntity> docLifeCycleMstList = nextSeqDocCache.get(seqGroupKey);
					if (docLifeCycleMstList == null) {
						docLifeCycleMstList = designTaskDAO.getNextSeqandStatusByDoc(Integer.parseInt(currSeq), "DC038",
								tenantId, docGroup, processDoc);
						nextSeqDocCache.put(seqGroupKey, docLifeCycleMstList);
					}
					if(docLifeCycleMstList.size()>0 && (list.get(i).getType()!=null && !list.get(i).getType().equalsIgnoreCase("Cash Voucher"))) {
						String docStatus = docLifeCycleMstList.get(0).getDocStatus();
						String nextStatusDesc = statusDescCache.get(docStatus);
						if (nextStatusDesc == null) {
							nextStatusDesc = designTaskDAO.getStatusByDesc(docStatus, tenantId);
							statusDescCache.put(docStatus, nextStatusDesc);
						}
						list.get(i).setNextStatus(nextStatusDesc);
					}
					if(list.get(i).getIsInventory().equalsIgnoreCase("1")) {
						list.get(i).setIsInventory("True");
						list.get(i).setPoStatus("NA");
						list.get(i).setScsStatus("NA");
						list.get(i).setNextStatus("");
					}else {
						list.get(i).setIsInventory("False");
						int count = iIndentGroupDAO.getIndentgrpScsCountByIgHdrId(list.get(i).getIgHdrId());
						if(count>0) {
							list.get(i).setScsStatus(iIndentGroupDAO.getScsStatusDesc(list.get(i).getIgHdrId()));
							String igScsId=iIndentGroupDAO.getScsIdByIgHdrId(list.get(i).getIgHdrId());
							list.get(i).setScsId(igScsId);
							int poDtlCount=iIndentGroupDAO.getPoCountByIgScsId(igScsId);
							if(poDtlCount>0) {
								list.get(i).setPoId(String.valueOf(poDtlCount));
								list.get(i).setPoStatus(iIndentGroupDAO.getPoStatusDesc(igScsId,tenantId));
							}else {
								list.get(i).setPoStatus("PO Not Created");
							}
						}else {
							list.get(i).setScsStatus("PJS Not Created");
							list.get(i).setNextStatus("Prepared");
							list.get(i).setPoStatus("PO Not Created");
						}
					}
					partCount=String.valueOf(iIndentGroupDAO.getIndentGrpDtlCountByIgHdrId(list.get(i).getIgHdrId()));
					list.get(i).setPartCount(partCount);
				}
			}

			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getIndentGroupDetails error---> " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage delIndentGrpDtl(IndentGrpDelRequest indentGrpDtlReq) {
		int del = 0;
		int lastCountCheck = 0;
		String indentId="";
		// logic to check on the comparative statement pending.
		ResponseAsMessage rm = new ResponseAsMessage();

		if (indentGrpDtlReq.getDelAll() == 0) {
			int checkCount = iIndentGroupDAO.getIndentgrpDtlCount(indentGrpDtlReq.getIgDtlId());
			if (checkCount == 0) {
				indentId = iIndentGroupDAO.getIndentIdByIgDtlId(indentGrpDtlReq.getIgDtlId());
//				indentCode =  iIndentGroupDAO.getIndentIdByIgDtlId(indentGrpDtlReq.getIgDtlId(), "2");
				lastCountCheck = iIndentGroupDAO.lastIndentGrpDtlCheck(indentGrpDtlReq.getIgDtlId());
				del = iIndentGroupDAO.delIndentGrpDtl(indentGrpDtlReq);
			}
		} else {
			int checkCount = iIndentGroupDAO.getIndentgrpScsCountByIgHdrId(indentGrpDtlReq.getIgDtlId());
			if (checkCount == 0) {
				indentId = iIndentGroupDAO.getIndentIdByIgHdrId(indentGrpDtlReq.getIgDtlId());
//					indentCode =  iIndentGroupDAO.getIndentIdByIgHdrId(indentGrpDtlReq.getIgDtlId(),"2");
				lastCountCheck =1;
				del = iIndentGroupDAO.delIndentGrp(indentGrpDtlReq);
			}
		}
		if (del > 0) {
			if(lastCountCheck==1 && !indentId.equalsIgnoreCase("")) {
				String indentGrpType = indentGroupDAO.getindentTypeCode(indentId);
				String seqStr = "6";
				if(indentGrpType.equalsIgnoreCase("IT001")) {
					seqStr=	iIndentGroupDAO.getTenantPropertyVal("INDENT_SCM_SEQ_PRODUCT", indentGrpDtlReq.getTenantId());
				}else if(indentGrpType.equalsIgnoreCase("IT002")) {
					seqStr=	iIndentGroupDAO.getTenantPropertyVal("INDENT_SCM_SEQ_SERVICE", indentGrpDtlReq.getTenantId());
				}
				if(seqStr == null ) {
					seqStr = "6";
				}
				indentUploadDAO.updateIndentHdrStatusAndSeq(indentId, seqStr, "DS070", indentGrpDtlReq.getEmpId(), indentGrpDtlReq.getTenantId());
//				if(!indentCode.equalsIgnoreCase("")) {
//					indentUploadDAO.updatenotification(indentCode, "DC038", indentGrpDtlReq.getTenantId());
//				}
			}
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.successfulDeleted);
		} else {
			rm.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			rm.setResponseMessage("PJS raised for this group");
			rm.setResponseDataMessage(ResponseMessageMap.deleteUnSuccessful);
		}

		return rm;
	}

	@Override
	public ResponseAsList getIndentGroupHdrAndDtl(IndentGrpDelRequest indentGrpDtlReq) {
		// TODO Auto-generated method stub
		ResponseAsList returnList = new ResponseAsList();
		List<IndentGroupHdrAndDtlEntity> list = new ArrayList<IndentGroupHdrAndDtlEntity>();
		try {
			list = iIndentGroupDAO.getIndentGroupHdrAndDtl(indentGrpDtlReq);
			int dmId=0;
			for(int i=0;i<list.size();i++) {
				dmId=indentUploadDAO.getDmIdByLatestVerion(list.get(i).getIndentDtlId(),indentGrpDtlReq.getTenantId());
				list.get(i).setDmId(String.valueOf(dmId));
				BigDecimal diffQty = BigDecimal.ZERO;
				diffQty=indentUploadDAO.getDiffernceQtyByIndent(list.get(i).getIndentDtlId(),indentGrpDtlReq.getTenantId());
				list.get(i).setDifferenceQty(String.valueOf(diffQty));
			}

			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getIndentGroupDetails error---> " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage checkTemplateName(IndentTemplateNameRequest indentTempName) {
		int resp = 0;

		ResponseAsMessage rm = new ResponseAsMessage();

		resp = iIndentGroupDAO.checkTemplateName(indentTempName);

		if (resp > 0) {
			rm.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			rm.setResponseMessage(ResponseMessageMap.responseAlreadyExists);
			rm.setResponseDataMessage(ResponseMessageMap.grpTemplatDup);
		} else {
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.success);
		}

		return rm;
	}

	@Override
	public ResponseAsMessage insertTempGrup(IndentInsertGrpRequest indentTempName) {
		int resp = 0, hdrId = 0;
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstLists = new ArrayList<DocumentStatusMstEntity>();

		ResponseAsMessage rm = new ResponseAsMessage();
        if(indentTempName.getIgHdrId() == null || indentTempName.getIgHdrId().isEmpty()) {
        	 hdrId = iIndentGroupDAO.insertTempGrup(indentTempName);
        }else {
        	hdrId = Integer.valueOf(indentTempName.getIgHdrId());
        }
	
        final int finalHdrId = hdrId;
		if (hdrId > 0 && indentTempName.getInsrtGrpDtl().size() > 0) {
			indentTempName.getInsrtGrpDtl().forEach(grpDtl -> {
				iIndentGroupDAO.insertTempGrpDtl(finalHdrId, grpDtl);
			});
		}

		String indentId = iIndentGroupDAO.getIndentIdByIndentDtlId(indentTempName.getInsrtGrpDtl().get(0).getIndentDtlId());
		int isInventoryCount=iPoDAO.getIsInventoryCount(indentId,indentTempName.getTenantId());
		int indentCloseCount = iPoDAO.getIndentCloseStatus(indentId,indentTempName.getTenantId());
		int checkCount=isInventoryCount-indentCloseCount;
		if(checkCount == 0) {
			String currSeq = iPoDAO.getTenantPropertyVal(indentTempName.getTenantId(), "INDENT_CLOSE_SEQ");
			currSeqDocLifeCycleMstLists = stageManagementDAO.getDocDtlcurrentSeq("DC018",
					currSeq, indentTempName.getTenantId());

			poDAO.updateIntentHdrSeqAndStatus("", currSeqDocLifeCycleMstLists.get(0).getCurrSequence(),
					currSeqDocLifeCycleMstLists.get(0).getDocStatus(), Integer.valueOf(indentId), "1");
		}

		if (resp > 0) {
			rm.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			rm.setResponseMessage(ResponseMessageMap.responseAlreadyExists);
			rm.setResponseDataMessage(ResponseMessageMap.grpTemplatDup);
		} else {
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.success);
		}

		return rm;
	}

	@Override
	public ResponseAsList getIndentQtyDtl(getIndentQtyDtlRequest getIndentQtyDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<IndentDtlTblEntity> indentdtl = new ArrayList<IndentDtlTblEntity>();
		try {
			indentdtl	=	indentUploadDAO.getIndentDtlsByIndentId(" ",getIndentQtyDtlReq.getIndentid(), getIndentQtyDtlReq.getTenantId());

//			for(int i =0;i<indentdtl.size();i++) {
			indentdtl.parallelStream().forEach(item -> {
				BigDecimal sumOfGrpQty=iIndentGroupDAO.getsumOfGrpQty(item.getIndentDtlId(), item.getTenantId());
				item.setQty((new BigDecimal(item.getQty()).subtract(sumOfGrpQty)).toString());
			});

			if (indentdtl.size() > 0) {
				returnList.setResponseData(indentdtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(indentdtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getIndentQtyDtl error---> " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getScpDtlsByIgHdrId(IdAndTenantIdRequest idAndTenantIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		ScpDtlsEntity list=new ScpDtlsEntity();
		List<ScpDtlsEntity> mainList = new ArrayList<ScpDtlsEntity>();
		PoCancelEntity poCancelEntity = new PoCancelEntity();
		List<PoPaymentTermEntity> poPaymentTerm = new ArrayList<>();
		List<IndentGrpScpDtlEntity> scpDtlList = new ArrayList<IndentGrpScpDtlEntity>();
		List<IndentGrpScpVenEntity> scpVendorList = new ArrayList<IndentGrpScpVenEntity>();
		List<IndentGrpScpVenDtlEntity> scpVendorDtlList = new ArrayList<IndentGrpScpVenDtlEntity>();
		List<IndentGrpScpVenPtEntity> scpvendorPtList = new ArrayList<IndentGrpScpVenPtEntity>();
		List<IndentGrpScsStatusEntity> scsStatusList=new ArrayList<IndentGrpScsStatusEntity>();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		int approveBtnEnable=0;
		String docGroup="",finalBasicTotal="";
//		String l1Country="",l2Country="",l3Country="";
		String l1CurrencyType="",l2CurrencyType="",l3CurrencyType="";
		String l1ExchangeRate="",l2ExchangeRate="",l3ExchangeRate="";
		String processDoc= idAndTenantIdReq.getProcessCode();
		try {
			mainList=iIndentGroupDAO.getScpDtlsByIgHdrId(idAndTenantIdReq.getHdrId(),idAndTenantIdReq.getTenantId());
			if(mainList.size()>0) {
				for(int i=0;i<mainList.size();i++) {

					scpDtlList=iIndentGroupDAO.getScpDtlList(mainList.get(i).getIgScpId());
					if (l1ExchangeRate.equalsIgnoreCase("") && l1CurrencyType.equalsIgnoreCase("") && scpDtlList != null && !scpDtlList.isEmpty() && scpDtlList.get(0).getL1CurrencyType() != null) {
						mainList.get(i).setL1CurrencyType(scpDtlList.get(0).getL1CurrencyType());
						mainList.get(i).setL1ExchangeRate(scpDtlList.get(0).getL1ExchangeRate());
						l1CurrencyType = scpDtlList.get(0).getL1CurrencyType();
					}
					if (l2ExchangeRate.equalsIgnoreCase("") && l2CurrencyType.equalsIgnoreCase("") && scpDtlList != null && !scpDtlList.isEmpty() && scpDtlList.get(0).getL2CurrencyType() != null) {
						mainList.get(i).setL2CurrencyType(scpDtlList.get(0).getL2CurrencyType());
						mainList.get(i).setL2ExchangeRate(scpDtlList.get(0).getL2ExchangeRate());
						l2CurrencyType = scpDtlList.get(0).getL2CurrencyType();
					}
					if (l3ExchangeRate.equalsIgnoreCase("") && l3CurrencyType.equalsIgnoreCase("") && scpDtlList != null && !scpDtlList.isEmpty() && scpDtlList.get(0).getL3CurrencyType() != null) {
						mainList.get(i).setL3CurrencyType(scpDtlList.get(0).getL3CurrencyType());
						mainList.get(i).setL3ExchangeRate(scpDtlList.get(0).getL3ExchangeRate());
						l3CurrencyType = scpDtlList.get(0).getL3CurrencyType();
					}
					for(int p =0;p<scpDtlList.size();p++) {
						int dmId = indentUploadDAO.getDmIdByLatestVerion(scpDtlList.get(p).getIndentDtlId(),
								idAndTenantIdReq.getTenantId());
						scpDtlList.get(p).setDmId(Integer.toString(dmId));
						if (dmId > 0) {
							String isPdf = iIndentGroupDAO.getIsPdfOrNot(String.valueOf(dmId));
	                        scpDtlList.get(p).setIsPdf(isPdf);
						}		
					}
					scpVendorList=iIndentGroupDAO.getScpVendorList(mainList.get(i).getIgScpId());
					if (scpVendorList.size() > 0) {
						if (scpVendorList.get(0).getL1VendorCode() != null) {
							scpVendorList.get(0).setL1VendorName(iIndentGroupDAO.getVendorNameByVendorCode(scpVendorList.get(0).getL1VendorCode()));
							scpVendorList.get(0).setL1VendorUniqueCode(iIndentGroupDAO.getVendorUniqueCodeByVendorCode(scpVendorList.get(0).getL1VendorCode()));
						}
						if (scpVendorList.get(0).getL2VendorCode() != null) {
							scpVendorList.get(0).setL2VendorName(iIndentGroupDAO.getVendorNameByVendorCode(scpVendorList.get(0).getL2VendorCode()));
							scpVendorList.get(0).setL2VendorUniqueCode(iIndentGroupDAO.getVendorUniqueCodeByVendorCode(scpVendorList.get(0).getL2VendorCode()));
						}
						if (scpVendorList.get(0).getL3VendorCode() != null) {
							scpVendorList.get(0).setL3VendorName(iIndentGroupDAO.getVendorNameByVendorCode(scpVendorList.get(0).getL3VendorCode()));
							scpVendorList.get(0).setL3VendorUniqueCode(iIndentGroupDAO.getVendorUniqueCodeByVendorCode(scpVendorList.get(0).getL3VendorCode()));
						}
					}
					scpVendorDtlList=iIndentGroupDAO.getScpVendorDtlList(mainList.get(i).getIgScpId());
					scpvendorPtList=iIndentGroupDAO.getScpvendorPtList(mainList.get(i).getIgScpId());
					String poId = iPoDAO.getPoIdByIgscsId(mainList.get(i).getIgScpId(), idAndTenantIdReq.getTenantId());
					String poCode = igrnDao.getPoCodeByPoId(poId);
					poPaymentTerm = iPoDAO.getPoPaymentTermList(poId);
					poCancelEntity.setPoId(poId);
					poCancelEntity.setPoCode(poCode);
					poCancelEntity.setPoPaymentTerm(poPaymentTerm);
					if (poPaymentTerm != null && poPaymentTerm.size() > 0) {
						for (PoPaymentTermEntity payment : poPaymentTerm) {
							String status = iPoDAO.getPaymentTermStatus(payment.getPotId());
							String[] cnt = (status != null) ? status.split("\\|") : null;
							payment.setIsCreated(cnt[0]);
							payment.setPraDate(cnt[1]);
							payment.setPraCode(cnt[2]);
							payment.setDocumentStatus(cnt[3]);
						}
					}
					scsStatusList=iIndentGroupDAO.getScsStatusList(mainList.get(i).getIgScpId());

					if (scpVendorDtlList.size() > 0) {
						if (mainList.get(i).getVendorQualified().equalsIgnoreCase("L1")) {
							finalBasicTotal = scpVendorDtlList.get(0).getL1ExtnFinalBasicTotal();
						} else if (mainList.get(i).getVendorQualified().equalsIgnoreCase("L2") && !scpVendorDtlList.isEmpty()) {
							finalBasicTotal = scpVendorDtlList.get(0).getL2ExtnFinalBasicTotal();
						} else {
							finalBasicTotal = scpVendorDtlList.get(0).getL3ExtnFinalBasicTotal();
						}
					}
					// get pjs doc_group
					docGroup = getScsDocGroup(finalBasicTotal,idAndTenantIdReq.getTenantId(),processDoc);


					String currSeq=mainList.get(i).getSeqNo();
					mainList.get(i).setSeqStatusDesc(designTaskDAO.getStatusByDesc(mainList.get(i).getSeqStatus(), idAndTenantIdReq.getTenantId()));

					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp("DC038", currSeq,
							idAndTenantIdReq.getTenantId(), docGroup, processDoc);
					docLifeCycleMstList = designTaskDAO.getNextSeqandStatusByDoc(Integer.parseInt(currSeq), "DC038",
							idAndTenantIdReq.getTenantId(), docGroup, processDoc);

//					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC038",currSeq, idAndTenantIdReq.getTenantId());
//					docLifeCycleMstList = designTaskDAO.getNextSeqandStatus(Integer.parseInt(currSeq), "DC038", idAndTenantIdReq.getTenantId());

					if(docLifeCycleMstList != null && docLifeCycleMstList.size()>0) {

						String designCode = uploadManagementDAO.getDesigCodeByEmpId(idAndTenantIdReq.getEmpId(),
								idAndTenantIdReq.getTenantId());

						approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode,docGroup , processDoc,
								idAndTenantIdReq.getTenantId(), "DC038", docLifeCycleMstList.get(0).getCurrSequence());

						if (approveBtnEnable == 1) {
							// curr seq
							docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO.getDocTypeDescByDocType(
									docLifeCycleMstList.get(0).getDocType(), idAndTenantIdReq.getTenantId()));
							docLifeCycleMstList.get(0)
									.setDocStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
													docLifeCycleMstList.get(0).getCurrSequence(),
													idAndTenantIdReq.getTenantId(), "DC038", docGroup,processDoc),
											idAndTenantIdReq.getTenantId()));// Current seq docStatus

							// Previous Seq
							docLifeCycleMstList.get(0).setPreviousSeq(currSeq);
							docLifeCycleMstList.get(0)
									.setPreviousSeqStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(currSeq,
											idAndTenantIdReq.getTenantId(), "DC038", docGroup,processDoc));

							docLifeCycleMstList.get(0)
									.setPreviousSeqStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(currSeq,
													idAndTenantIdReq.getTenantId(), "DC038", docGroup,processDoc),
											idAndTenantIdReq.getTenantId()));

							// cancel seq
							if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
								docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
								docLifeCycleMstList.get(0)
										.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
												idAndTenantIdReq.getTenantId(), "DC038", docGroup,processDoc));

								docLifeCycleMstList.get(0)
										.setCancelStatusDesc(designTaskDAO.getStatusByDesc(
												indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
														currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
														idAndTenantIdReq.getTenantId(), "DC038", docGroup,processDoc),
												idAndTenantIdReq.getTenantId()));

							}else {
								docLifeCycleMstList.get(0).setCancelSeq(null);
							}

							mainList.get(i).setDocLifeCycleMstList(docLifeCycleMstList);
							mainList.get(i).setIsEditable(docLifeCycleMstList.get(0).getIsEditable()!=null && docLifeCycleMstList.get(0).getIsEditable()!=""? docLifeCycleMstList.get(0).getIsEditable():"0");
						}else {
							mainList.get(i).setIsEditable("0");
						}

					}else {
						mainList.get(i).setIsEditable("0");
					}
					mainList.get(i).setScpDtlList(scpDtlList);
					mainList.get(i).setScpVendorList(scpVendorList);
					mainList.get(i).setScpVendorDtlList(scpVendorDtlList);
					mainList.get(i).setScpvendorPtList(scpvendorPtList);
					mainList.get(i).setPoCancel(poCancelEntity);
					mainList.get(i).setScsStatusList(scsStatusList);


				}
			}else {
				list.setIsEditable("1");
				mainList.add(list);
			}

			if (mainList.size() > 0) {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				list.setIsEditable("1");
				mainList.add(list);
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getScpDtlsByIgHdrId error---> " + ex);
		}
		return returnList;
	}

	// Same lookup as getScsDocGroup below, but reuses a docGroupList already fetched once for the whole
	// request instead of re-querying the DB - used by the per-row loop in getIndentGroupDetails.
	private String resolveScsDocGroup(String val, List<String> docGroupList) {
		String docGroup = "";
		try {
			BigDecimal value = new BigDecimal(val);
			if (docGroupList.size() > 0) {
				for (String docGrpVal : docGroupList) {
					if (value.compareTo(new BigDecimal(docGrpVal)) <= 0) {
						docGroup = docGrpVal;
						break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("resolveScsDocGroup Error" + ex);
		}
		return docGroup;
	}

	public String getScsDocGroup(String val,String tenantId, String processDoc) {
		String docGroup = "";
		try {
			BigDecimal value=new BigDecimal(val);
			List<String> docGroupList = iIndentGroupDAO.getDistinctScsDocGroup("DC038",tenantId, processDoc);
			if (docGroupList.size() > 0) {
				for (String docGrpVal : docGroupList) {
					if (value.compareTo(new BigDecimal(docGrpVal)) <= 0) {
						docGroup = docGrpVal;
						break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("getDocGroup Error" + ex);
		}
		return docGroup;
	}

	@Override
	public ResponseAsMessage insertScpDtlsByIgHdrId(List<ScpDtlsEntity> scpDtlsEntity) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstLists = new ArrayList<DocumentStatusMstEntity>();
		int updateInScp = 0,insertInScpDtl= 0,insertInVen= 0,insertInVenDtl= 0,insertInVenPt= 0;
		int newScpId=0;
		String scpID="",indentId="";
		String budgetCol="";
		String oldbudgetCol="",oldFinalBasicTotal="";

		try {
			String oldVendorQualified="";
			logger.info("insertScpDtlsByIgHdrId service start");
			if (scpDtlsEntity.size() > 0) {

				if(scpDtlsEntity.get(0).getIgScpId().equalsIgnoreCase("")) {
					indentId=projectDAO.getindentHdrId(scpDtlsEntity.get(0).getScpDtlList().get(0).getIndentDtlId());

					newScpId=iIndentGroupDAO.insertInScp(scpDtlsEntity.get(0).getIgHdrId(), scpDtlsEntity.get(0).getTechnicalCompassion(), scpDtlsEntity.get(0).getTechnicalRecommendation(), scpDtlsEntity.get(0).getVendorEvaluated(), scpDtlsEntity.get(0).getVendorShortListed(),
							scpDtlsEntity.get(0).getJustification(), scpDtlsEntity.get(0).getVendorQualified(), scpDtlsEntity.get(0).getCustomerApproval(), scpDtlsEntity.get(0).getCreatedDate(), scpDtlsEntity.get(0).getCreatedBy(), "1", "DS069", scpDtlsEntity.get(0).getTenantId(),indentId,scpDtlsEntity.get(0).getType());
					scpID=String.valueOf(newScpId);
					//Scp status table insert
					iIndentGroupDAO.insertInScpStatus(scpID, "1", "DS069", "created", scpDtlsEntity.get(0).getCreatedBy(), scpDtlsEntity.get(0).getTenantId());

				}else {
					scpID=scpDtlsEntity.get(0).getIgScpId();
					oldVendorQualified = iIndentGroupDAO.getVendorQualified(scpDtlsEntity.get(0).getIgScpId());
					if(!oldVendorQualified.equalsIgnoreCase("")) {
						if(oldVendorQualified.equalsIgnoreCase("L1")) {
							oldbudgetCol="L1_FINAL_SUB_TOTAL";
						}else if(oldVendorQualified.equalsIgnoreCase("L2")) {
							oldbudgetCol="L2_FINAL_SUB_TOTAL";
						}else {
							oldbudgetCol="L3_FINAL_SUB_TOTAL";
						}
					}

					oldFinalBasicTotal=indentGroupDAO.getScmBudgetValueByigscsId(scpID, oldbudgetCol);
					updateInScp=iIndentGroupDAO.updateInScp(scpDtlsEntity.get(0).getIgScpId(), scpDtlsEntity.get(0).getTechnicalCompassion(), scpDtlsEntity.get(0).getTechnicalRecommendation(), scpDtlsEntity.get(0).getVendorEvaluated(), scpDtlsEntity.get(0).getVendorShortListed(),
							scpDtlsEntity.get(0).getJustification(), scpDtlsEntity.get(0).getVendorQualified(), scpDtlsEntity.get(0).getCustomerApproval(),scpDtlsEntity.get(0).getCreatedBy(),scpDtlsEntity.get(0).getType());

				}

				if(newScpId>0 ||updateInScp==1 ) {
					// Scp dtl table insert
					if(scpDtlsEntity.get(0).getScpDtlList().size()>0) {
						for(int j=0;j<scpDtlsEntity.get(0).getScpDtlList().size();j++) {
							insertInScpDtl=iIndentGroupDAO.insertInScpDtl(scpDtlsEntity.get(0).getScpDtlList().get(j).getIgScpDItld(), scpID, scpDtlsEntity.get(0).getScpDtlList().get(j).getIgDtlId(), scpDtlsEntity.get(0).getL1CurrencyType(), scpDtlsEntity.get(0).getL1ExchangeRate(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL1UnitPrice(),
									scpDtlsEntity.get(0).getScpDtlList().get(j).getL1ExtendedPrice(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL1UnitPriceFx(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL1ExtendedPriceFx(), scpDtlsEntity.get(0).getL2CurrencyType(), scpDtlsEntity.get(0).getL2ExchangeRate(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL2UnitPrice(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL2ExtendedPrice(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL2UnitPriceFx(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL2ExtendedPriceFx(),
									scpDtlsEntity.get(0).getL3CurrencyType(), scpDtlsEntity.get(0).getL3ExchangeRate(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL3UnitPrice(),
									scpDtlsEntity.get(0).getScpDtlList().get(j).getL3ExtendedPrice(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL3UnitPriceFx(), scpDtlsEntity.get(0).getScpDtlList().get(j).getL3ExtendedPriceFx(), scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL1UnitPrice(),scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL1ExtendedPrice(), scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL1UnitPriceFx(), scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL1ExtendedPriceFx(),
									scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL2UnitPrice(),scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL2ExtendedPrice(), scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL2UnitPriceFx(), scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL2ExtendedPriceFx(), scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL3UnitPrice(),scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL3ExtendedPrice(), scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL3UnitPriceFx(),  scpDtlsEntity.get(0).getScpDtlList().get(j).getFinalL3ExtendedPriceFx(),
									scpDtlsEntity.get(0).getTenantId(),scpDtlsEntity.get(0).getScpDtlList().get(j).getIndentDtlId());
						}
					}
					// Scp vendor table insert
					if(scpDtlsEntity.get(0).getScpVendorList().size()>0) {
						for(int k=0;k<scpDtlsEntity.get(0).getScpVendorList().size();k++) {
							insertInVen=iIndentGroupDAO.insertInVen(scpDtlsEntity.get(0).getScpVendorList().get(k).getIgScpVid(),  scpID, scpDtlsEntity.get(0).getScpVendorList().get(k).getL1VendorCode(), scpDtlsEntity.get(0).getScpVendorList().get(k).getL2VendorCode(),
									scpDtlsEntity.get(0).getScpVendorList().get(k).getL3VendorCode(), scpDtlsEntity.get(0).getScpVendorList().get(k).getLevel(), scpDtlsEntity.get(0).getCreatedBy(),scpDtlsEntity.get(0).getScpVendorList().get(k).getL1Gst(),scpDtlsEntity.get(0).getScpVendorList().get(k).getL2Gst(),scpDtlsEntity.get(0).getScpVendorList().get(k).getL3Gst());
						}
					}

					// Scp vendor dtl table insert
					List<IndentGrpScpVenDtlEntity>vendorDtllist=new ArrayList<IndentGrpScpVenDtlEntity>(scpDtlsEntity.get(0).getScpVendorDtlList());
					if(vendorDtllist.size()>0) {
						for(int h=0;h<vendorDtllist.size();h++) {
							insertInVenDtl=iIndentGroupDAO.insertInVenDtl(vendorDtllist.get(h), scpDtlsEntity.get(0).getCreatedBy(),scpID);
						}
					}

					String scpIndentId	 = iIndentGroupDAO.getindentIdBygrpScd(scpID);
					String vendorQualified=iIndentGroupDAO.getVendorQualified(scpID);
//					if(!oldVendorQualified.equalsIgnoreCase(vendorQualified)) {

					if(vendorQualified.equalsIgnoreCase("L1")) {
						budgetCol="L1_FINAL_SUB_TOTAL";
					}else if(vendorQualified.equalsIgnoreCase("L2")) {
						budgetCol="L2_FINAL_SUB_TOTAL";
					}else {
						budgetCol="L3_FINAL_SUB_TOTAL";
					}

					indentUploadDAO.updateScmBudgetvalByoperation(oldFinalBasicTotal, scpIndentId, "-");
//						}
					String val =	indentGroupDAO.getScmBudgetValueByigscsId(scpID, budgetCol);
					indentUploadDAO.updateScmBudgetvalByoperation(val, scpIndentId, "+");

					// Scp vendor pt table insert
					List<IndentGrpScpVenPtEntity>vendorPtlist=new ArrayList<IndentGrpScpVenPtEntity>(scpDtlsEntity.get(0).getScpvendorPtList());
					int getPtCount=iIndentGroupDAO.getScsPtCount(scpID);
					if(getPtCount>0) {
						iIndentGroupDAO.deleteScsPt(scpID);
					}
					if(vendorPtlist.size()>0) {
						for(int c=0;c<scpDtlsEntity.get(0).getScpvendorPtList().size();c++) {
							insertInVenPt=iIndentGroupDAO.insertInVenPt(vendorPtlist.get(c), scpDtlsEntity.get(0).getTenantId(),scpID,scpDtlsEntity.get(0).getCreatedBy(),vendorPtlist.get(c).getRemarks());
						}
					}

				}

			}
             if(scpDtlsEntity.get(0).getType().equalsIgnoreCase("Cash Voucher")) {
            	 iIndentGroupDAO.updateInvStockYesOrNo("1",scpDtlsEntity.get(0).getIgHdrId()); 
				 indentId=projectDAO.getindentHdrId(scpDtlsEntity.get(0).getScpDtlList().get(0).getIndentDtlId());
            	 int isInventoryCount = iPoDAO.getIsInventoryCount(indentId,
            			 scpDtlsEntity.get(0).getTenantId());
 				int indentCloseCount = iPoDAO.getIndentCloseStatus(indentId,
 						scpDtlsEntity.get(0).getTenantId());
 				int checkCount = isInventoryCount - indentCloseCount;
 				if (checkCount == 0) {
 					String currSeq = iPoDAO.getTenantPropertyVal(scpDtlsEntity.get(0).getTenantId(), "INDENT_CLOSE_SEQ");
 					currSeqDocLifeCycleMstLists = stageManagementDAO.getDocDtlcurrentSeq("DC018", currSeq,
 							scpDtlsEntity.get(0).getTenantId());

 					intentDetailsUpdate(indentId, currSeqDocLifeCycleMstLists.get(0).getCurrSequence(),
 							currSeqDocLifeCycleMstLists.get(0).getDocStatus(), scpDtlsEntity.get(0).getTenantId(),
 							scpDtlsEntity.get(0).getPmId(), scpDtlsEntity.get(0).getMstId(), "1");

 					iPoDAO.updateindentClose(indentId);
 				}
             }else {
            	 iIndentGroupDAO.updateInvStockYesOrNo("0",scpDtlsEntity.get(0).getIgHdrId());
				 indentId=projectDAO.getindentHdrId(scpDtlsEntity.get(0).getScpDtlList().get(0).getIndentDtlId());

				 int pendingCount = iIndentGroupDAO.getPendingUngroupedItemCount(indentId, scpDtlsEntity.get(0).getTenantId());
				 if (pendingCount == 0) {
	            	 String currSeq = iPoDAO.getTenantPropertyVal(scpDtlsEntity.get(0).getTenantId(), "INDENT_PERV_SEQ");
					currSeqDocLifeCycleMstLists = stageManagementDAO.getDocDtlcurrentSeq("DC018", currSeq,
							scpDtlsEntity.get(0).getTenantId());

					intentDetailsUpdate(indentId, currSeqDocLifeCycleMstLists.get(0).getCurrSequence(),
							currSeqDocLifeCycleMstLists.get(0).getDocStatus(), scpDtlsEntity.get(0).getTenantId(),
							scpDtlsEntity.get(0).getPmId(), scpDtlsEntity.get(0).getMstId(), "0");
				 }
             }
			if (insertInScpDtl == 1 && insertInVen == 1 && insertInVenDtl == 1 && insertInVenPt == 1) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {

				iIndentGroupDAO.deleteScpId(scpID, "indent_grp_scs_dtl");
				iIndentGroupDAO.deleteScpId(scpID, "indent_grp_scs_status");
				iIndentGroupDAO.deleteScpId(scpID, "indent_grp_scs_ven_pt");
				iIndentGroupDAO.deleteScpId(scpID, "indent_grp_scs_ven_dtl");
				iIndentGroupDAO.deleteScpId(scpID, "indent_grp_scs_ven");
				iIndentGroupDAO.deleteScpId(scpID, "indent_grp_scs");
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
			logger.info("insertScpDtlsByIgHdrId service end");
		} catch (Exception ex) {
			logger.error("insertScpDtlsByIgHdrId Method Exception --->" + ex);

		}
		return returnMessage;
	}


	private int intentDetailsUpdate(String indentId, String seq, String status, String tenantId, String pmId,
			String masterId, String isCompleted) {
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		int updateIndentHdr = 0;
		int getIntentId = Integer.valueOf(indentId);

		if (getIntentId > 0) {

			updateIndentHdr = iPoDAO.updateIntentHdrSeqAndStatus(indentId, seq, status, getIntentId, isCompleted);
			if (updateIndentHdr == 1) {
				otherEmpId = iPoDAO.getEmpListByIndentId(String.valueOf(getIntentId));
				messageList.add(indentUploadDAO.getIndentCodeByIndentId(String.valueOf(getIntentId)));
				commonNotifyMethod.InvokeNotificationMethod(2, 45, null, tenantId, messageList, otherEmpId, "0", pmId,
						masterId, null);
			}
		}
		return updateIndentHdr;
	}

	@Override
	public ResponseAsMessage updateScpSeqAndStatus(UpdateSeqAndStatusRequest updateHdrReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<IndentGrpScpVenDtlEntity> scpVendorDtlList = new ArrayList<IndentGrpScpVenDtlEntity>();
		int  isCompletedCount=0,budgetExcessInsert=0;
		BigDecimal scmBudgetValue=BigDecimal.ZERO;
		BigDecimal indentTargetValue=BigDecimal.ZERO;
		BigDecimal budgetExcessValue=BigDecimal.ZERO;
		String indentId="",finalBasicTotal=""; String processDoc = updateHdrReq.getProcessCode();
		try {
			logger.info("updateScpSeqAndStatus service start");
			String docType = "DC038";
			String scsId=updateHdrReq.getHdrId();
//			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq(docType,updateHdrReq.getCurrentseq(), updateHdrReq.getTenantId());

			String vendorQualified=iIndentGroupDAO.getVendorQualified(scsId);
			scpVendorDtlList=iIndentGroupDAO.getScpVendorDtlList(scsId);
			String vendorCode = "";
			if(vendorQualified.equalsIgnoreCase("L1")) {
				finalBasicTotal=scpVendorDtlList.get(0).getL1ExtnFinalBasicTotal();
				vendorCode=	"L1_VENDOR_CODE";
			}else if(vendorQualified.equalsIgnoreCase("L2")) {
				finalBasicTotal=scpVendorDtlList.get(0).getL2ExtnFinalBasicTotal();
				vendorCode=	"L2_VENDOR_CODE";
			}else {
				finalBasicTotal=scpVendorDtlList.get(0).getL3ExtnFinalBasicTotal();
				vendorCode=	"L3_VENDOR_CODE";
			}

			// get scs doc_group
			String docGroup = getScsDocGroup(finalBasicTotal,updateHdrReq.getTenantId(), processDoc);

			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp(docType,
					updateHdrReq.getCurrentseq(), updateHdrReq.getTenantId(), docGroup,processDoc);
			String scsBudgetExcessSeq="", budgetExcessSeq ="";
            if(processDoc.equalsIgnoreCase("5")) {
            	 scsBudgetExcessSeq=iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", updateHdrReq.getTenantId());
    			 budgetExcessSeq=iIndentGroupDAO.getTenantPropertyVal("BUDGET_EXCESS", updateHdrReq.getTenantId());
            }else {
            	 scsBudgetExcessSeq=iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", updateHdrReq.getTenantId());
    			 budgetExcessSeq=iIndentGroupDAO.getTenantPropertyVal("CAPEX_BUDGET_EXCESS", updateHdrReq.getTenantId());
            }
			
			indentId=poDAO.getIndentId(scsId);
			//	String vendorQualified=iIndentGroupDAO.getVendorQualified(scsId);
			// check for SCS - budget excess
			if(updateHdrReq.getCurrentseq().equals(scsBudgetExcessSeq)) {

//				if(vendorQualified.equalsIgnoreCase("L1")) {
//					budgetCol="L1_EXTN_FINAL_BASIC_TOTAL";
//				}else if(vendorQualified.equalsIgnoreCase("L2")) {
//					budgetCol="L2_EXTN_FINAL_BASIC_TOTAL";
//				}else {
//					budgetCol="L3_EXTN_FINAL_BASIC_TOTAL";
//				}
				scmBudgetValue = new BigDecimal(indentGroupDAO.getindentScmVal(indentId));
				// update scmBudget Value in indent_hdr
				//	indentUploadDAO.updateScmBudget(String.valueOf(scmBudgetValue), indentId);

				String costFlowType = indentUploadDAO.getCostFlowTypeByIndentId(indentId);
				boolean isBudgetExceeded;
				if ("NEW".equalsIgnoreCase(costFlowType)) {
					// NEW-flow: TARGET_VALUE is always 0 (see project_budget_target_cost_removal),
					// so check the real remaining budget at the station instead: what's allocated
					// to the station minus what's already committed there (approved POs, plus other
					// SCS's that already crossed this same "Project Approved" step but have no PO yet).
					BigDecimal remainingStationBudget = computeStationBudgetSnapshot(indentId, scsBudgetExcessSeq).remaining;
					// Mirror the legacy formula's own resolution mechanism (indentTargetValue =
					// TARGET_VALUE + getBudgetExcessValue, below) but scoped per-indent, not into the
					// station's shared Allocated Value pool - once a Budget Excess is approved for
					// THIS indent, its real shortfall amount (ACTUAL_EXCESS, not the legacy EXCESS
					// column which is always the full quote here) is added to this one comparison
					// only, so it doesn't inflate budget for other indents sharing the same station.
					BigDecimal approvedExcessForThisIndent = new BigDecimal(
							iIndentGroupDAO.getApprovedActualExcessByIndentId(indentId));
					BigDecimal effectiveRemaining = remainingStationBudget.add(approvedExcessForThisIndent);
					isBudgetExceeded = effectiveRemaining.compareTo(scmBudgetValue) < 0;
				} else {
					indentTargetValue = new BigDecimal(iIndentGroupDAO.getIndentTargetValue(indentId));
					budgetExcessValue = new BigDecimal(iIndentGroupDAO.getBudgetExcessValue(indentId));
					indentTargetValue = indentTargetValue.add(budgetExcessValue);
					isBudgetExceeded = indentTargetValue.compareTo(scmBudgetValue) < 0;
				}

				if (isBudgetExceeded) {
					if ("NEW".equalsIgnoreCase(costFlowType)) {
						// NEW-flow: never auto-create a Budget Excess Sheet entry from this click.
						// If a Budget Excess was already raised for this SCS, it's still short even
						// counting any approved amount (effectiveRemaining above already accounts for
						// that) - so it's pending approval, not something the PM needs to act on again.
						// Tell them that instead of re-prompting "Allocate to Station"/"Raise Budget
						// Excess", which would be misleading once they've already raised one.
						if (iIndentGroupDAO.getBudgetExcessDtlCount(scsId) > 0) {
							returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
							returnMessage.setResponseMessage(ResponseMessageMap.approveBudgetExcess);
							return returnMessage;
						}
						// No Budget Excess raised yet - block and tell the PM which explicit action to
						// take instead - either "Allocate to Station" (if Sales Budget is available
						// elsewhere in the project) or "Raise Budget Excess" (new PM-triggered action,
						// see raiseBudgetExcess() below) - both are now deliberate PM choices rather
						// than one of them happening automatically. Legacy flow (costFlowType is
						// never "NEW" there) is untouched and still auto-creates below.
						String projectId = indentUploadDAO.getProjectIdByIndentId(indentId);
						String mstId = projectDAO.getmstIdByPmHdrId(projectId, updateHdrReq.getTenantId());
						BigDecimal unallocatedProjectBudget = new BigDecimal(
								projectDAO.getUnallocatedSalesBudgetTotalByMstId(mstId, updateHdrReq.getTenantId()));
						returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
						returnMessage.setResponseMessage(unallocatedProjectBudget.compareTo(BigDecimal.ZERO) > 0
								? ResponseMessageMap.allocateBudgetFromSalesValue
								: ResponseMessageMap.raiseBudgetExcessRequired);
						return returnMessage;
					}
					int budgetExcessEntry=iIndentGroupDAO.getBudgetExcessDtlCount(scsId);
					if(budgetExcessEntry==0) {
						BudgetExcessSheetRequest budgetExcessSheetReq = new BudgetExcessSheetRequest();
						budgetExcessSheetReq.setIndentId(indentId);
						budgetExcessSheetReq.setTenantID(updateHdrReq.getTenantId());
						budgetExcessSheetReq.setUpdatedBy(updateHdrReq.getEmpId());
						budgetExcessSheetReq.setVendor(iIndentGroupDAO.getVendorCodeByScsId(scsId,vendorCode));
						budgetExcessSheetReq.setIgScsId(updateHdrReq.getHdrId());
						budgetExcessSheetReq.setMasterId(updateHdrReq.getMstId());
						budgetExcessSheetReq.setPmId(updateHdrReq.getPmId());
						budgetExcessSheetReq.setProjectId(updateHdrReq.getPmHdrId());
						budgetExcessSheetReq.setScsFinalCost(updateHdrReq.getScsFinalCost());
						budgetExcessSheetReq.setProcessDoc(updateHdrReq.getProcessCode().equalsIgnoreCase("5") ? "3" : "8");
						iBudgetExcessSheetService.insertBudgetExcessSheetDtl(budgetExcessSheetReq);
						budgetExcessInsert=1;
					}
				}
			}

			// check for budget excess
			if(updateHdrReq.getCurrentseq().equals(budgetExcessSeq)) {
				int getCount=iIndentGroupDAO.getBudgetExcessDtlCount(scsId);
				if(getCount>0) {
					isCompletedCount=iIndentGroupDAO.getBudgetExcessIsCompleted(scsId);
				}
				if(isCompletedCount>0 || getCount == 0 ) {
					returnMessage=updateScsDtls(scsId,updateHdrReq.getCurrentseq(),currSeqDocLifeCycleMstList.get(0).getDocStatus(),updateHdrReq.getRemarks(), updateHdrReq.getEmpId(),
							updateHdrReq.getTenantId(),currSeqDocLifeCycleMstList.get(0).getLastSeq(),
							updateHdrReq.getPmId(),updateHdrReq.getMstId(),
							updateHdrReq.getDocTypeCode(),updateHdrReq.getPmHdrId(),updateHdrReq.getEnquiryId(),docGroup,processDoc);
					if(budgetExcessInsert==1) {
						returnMessage.setResponseDataMessage(ResponseMessageMap.budgetExcessReported);
					}
					return returnMessage;
				}else {
					if(budgetExcessInsert==1) {
						returnMessage.setResponseDataMessage(ResponseMessageMap.budgetExcessReported);
					}
					returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
					returnMessage.setResponseMessage(ResponseMessageMap.approveBudgetExcess);
					return returnMessage;
				}
			}else {
				returnMessage=updateScsDtls(scsId,updateHdrReq.getCurrentseq(),currSeqDocLifeCycleMstList.get(0).getDocStatus(),updateHdrReq.getRemarks(), updateHdrReq.getEmpId(),
						updateHdrReq.getTenantId(),currSeqDocLifeCycleMstList.get(0).getLastSeq(),
						updateHdrReq.getPmId(),updateHdrReq.getMstId(),updateHdrReq.getDocTypeCode(),
						updateHdrReq.getPmHdrId(),updateHdrReq.getEnquiryId(),docGroup,processDoc);
				if(budgetExcessInsert==1) {
					returnMessage.setResponseDataMessage(ResponseMessageMap.budgetExcessReported);
				}
				return returnMessage;
			}
		} catch (Exception ex) {
			logger.error("updateScpSeqAndStatus error " + ex);
		}
		logger.info("updateScpSeqAndStatus service end");
		return returnMessage;
	}

	// Holds the pieces of the station-budget check together so callers that only need the
	// remaining-budget gate (updateScpSeqAndStatus) and callers that also need to snapshot
	// stationAllocated/actualSpentSoFar onto a new Budget Excess row (raiseBudgetExcess) can
	// share one set of DAO calls instead of computing the same sums twice.
	private static class StationBudgetSnapshot {
		final BigDecimal stationAllocated;
		final BigDecimal actualSpentSoFar;
		final BigDecimal remaining;

		StationBudgetSnapshot(BigDecimal stationAllocated, BigDecimal actualSpentSoFar) {
			this.stationAllocated = stationAllocated;
			this.actualSpentSoFar = actualSpentSoFar;
			this.remaining = stationAllocated.subtract(actualSpentSoFar);
		}
	}

	private StationBudgetSnapshot computeStationBudgetSnapshot(String indentId, String scsBudgetExcessSeq) {
		String pkaId = indentUploadDAO.getPkaIdByIndentId(indentId);
		BigDecimal stationAllocated = new BigDecimal(projectDAO.getAllocatedValSum(pkaId));
		BigDecimal approvedPoTotal = new BigDecimal(poDAO.getApprovedPoTotalByPkaId(pkaId));
		BigDecimal otherCommittedPjs = new BigDecimal(
				indentGroupDAO.getOtherCommittedScsTotalByPkaId(pkaId, indentId, scsBudgetExcessSeq));
		BigDecimal actualSpentSoFar = approvedPoTotal.add(otherCommittedPjs);
		return new StationBudgetSnapshot(stationAllocated, actualSpentSoFar);
	}

	@Override
	public ResponseAsMessage raiseBudgetExcess(UpdateSeqAndStatusRequest updateHdrReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<IndentGrpScpVenDtlEntity> scpVendorDtlList = new ArrayList<IndentGrpScpVenDtlEntity>();
		try {
			String scsId = updateHdrReq.getHdrId();
			String processDoc = updateHdrReq.getProcessCode();
			String indentId = poDAO.getIndentId(scsId);
			String costFlowType = indentUploadDAO.getCostFlowTypeByIndentId(indentId);

			if (!"NEW".equalsIgnoreCase(costFlowType)) {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseMessage(ResponseMessageMap.actionNotAllowed);
				return returnMessage;
			}

			BigDecimal scmBudgetValue = new BigDecimal(indentGroupDAO.getindentScmVal(indentId));
			String scsBudgetExcessSeq = processDoc.equalsIgnoreCase("5")
					? iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", updateHdrReq.getTenantId())
					: iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", updateHdrReq.getTenantId());
			StationBudgetSnapshot stationBudgetSnapshot = computeStationBudgetSnapshot(indentId, scsBudgetExcessSeq);
			boolean isBudgetExceeded = stationBudgetSnapshot.remaining.compareTo(scmBudgetValue) < 0;
			if (!isBudgetExceeded) {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseMessage(ResponseMessageMap.actionNotAllowed);
				return returnMessage;
			}

			int budgetExcessEntry = iIndentGroupDAO.getBudgetExcessDtlCount(scsId);
			if (budgetExcessEntry != 0) {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseMessage(ResponseMessageMap.budgetExcessAlreadyRaised);
				return returnMessage;
			}

			String vendorQualified = iIndentGroupDAO.getVendorQualified(scsId);
			scpVendorDtlList = iIndentGroupDAO.getScpVendorDtlList(scsId);
			String vendorCode = "";
			if (vendorQualified.equalsIgnoreCase("L1")) {
				vendorCode = "L1_VENDOR_CODE";
			} else if (vendorQualified.equalsIgnoreCase("L2")) {
				vendorCode = "L2_VENDOR_CODE";
			} else {
				vendorCode = "L3_VENDOR_CODE";
			}

			BudgetExcessSheetRequest budgetExcessSheetReq = new BudgetExcessSheetRequest();
			budgetExcessSheetReq.setIndentId(indentId);
			budgetExcessSheetReq.setTenantID(updateHdrReq.getTenantId());
			budgetExcessSheetReq.setUpdatedBy(updateHdrReq.getEmpId());
			budgetExcessSheetReq.setVendor(iIndentGroupDAO.getVendorCodeByScsId(scsId, vendorCode));
			budgetExcessSheetReq.setIgScsId(updateHdrReq.getHdrId());
			budgetExcessSheetReq.setMasterId(updateHdrReq.getMstId());
			budgetExcessSheetReq.setPmId(updateHdrReq.getPmId());
			budgetExcessSheetReq.setProjectId(updateHdrReq.getPmHdrId());
			budgetExcessSheetReq.setScsFinalCost(updateHdrReq.getScsFinalCost());
			budgetExcessSheetReq.setProcessDoc(processDoc.equalsIgnoreCase("5") ? "3" : "8");
			budgetExcessSheetReq.setAllocatedValue(stationBudgetSnapshot.stationAllocated.toString());
			budgetExcessSheetReq.setActualSpentSoFar(stationBudgetSnapshot.actualSpentSoFar.toString());
			iBudgetExcessSheetService.insertBudgetExcessSheetDtl(budgetExcessSheetReq);

			returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnMessage.setResponseMessage(ResponseMessageMap.successCreated);
		} catch (Exception ex) {
			logger.error("raiseBudgetExcess error " + ex);
		}
		return returnMessage;
	}

	private ResponseAsMessage updateScsDtls(String scsId, String currentseq, String docStatus,
											String remarks,String empId, String tenantId,String lastSeq,String pmId,
											String masterId,String docType,String pmHdrId,String enquiryId,String docGroup, String processDoc) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		GetPoDtlsEntity poList= new GetPoDtlsEntity();
		try {
			int updateIdentHdr = iIndentGroupDAO.updateScpSeqAndStatus(scsId,currentseq, docStatus,empId);

			if (updateIdentHdr == 1) {

				iIndentGroupDAO.insertInScpStatus(scsId, currentseq, docStatus, remarks,empId, tenantId);
//				 String pjsLastUpdatedDate = iIndentGroupDAO.getLastUpdatedDateTime(indentDtlId, tenantId, "PJS");
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				String IndentCode=iIndentGroupDAO.getindentCode(scsId);
				String type = iIndentGroupDAO.getScsGrpType(scsId);
				String approveDesig ="";
				if(type.equalsIgnoreCase("0")) {
					approveDesig=commonNotifyMethod.getNxtAppDescByDocGroup("DC038", currentseq,docGroup,tenantId,processDoc);
				}
				messageList.add(IndentCode);
				commonNotifyMethod.InvokeNotificationMethod(2, 27, null,
						tenantId, messageList, otherEmp, "", pmId, "", approveDesig);
				commonNotifyMethod.InvokeApprovalDesigMethod(pmId, "DC038", scsId,
						pmHdrId, tenantId, "", approveDesig, enquiryId,IndentCode);
				if(lastSeq !=null && lastSeq.equalsIgnoreCase("1")) {
					iIndentGroupDAO.updateScpApproved(scsId,"1");

					// Update PJS last updated datetime in indent_Dtl table
					List<IndentDtlTblEntity> list = new ArrayList<IndentDtlTblEntity>();
					list= iIndentGroupDAO.getIndentDtlsByScsId(scsId, tenantId);
					if(list.size()>0) {
						for(int i=0;i<list.size();i++) {
							String scsLastUpdatedDate=indentGroupDAO.getLastUpdatedDateTime(list.get(i).getIndentDtlId(), tenantId, "PJS");
							if(!scsLastUpdatedDate.equalsIgnoreCase("")){
								indentGroupDAO.updateLastUpdatedDateTime(list.get(i).getIndentDtlId(), "PJS_COMPLETED_DATETIME", scsLastUpdatedDate,tenantId);
							}else {
				            	indentGroupDAO.reUpdatedDateTimeIndentDtl(list.get(i).getIndentDtlId(), "PJS_COMPLETED_DATETIME", tenantId);
				            }
						}
					}


					// check if cancelled PO created before...
					int cancelledPo = iIndentGroupDAO.checkForCancelledPo(scsId, tenantId);
//					if(cancelledPo==1){
//						String poId = iPoDAO.getPoIdByIgscsId(scsId, tenantId);
//					}else {


						// check if PO already created
						int poDtlCount = iIndentGroupDAO.getPoCountByIgScsId(scsId);

						if (poDtlCount == 0) {
							poList = getPoDtlsByScsId(scsId, tenantId, empId, pmHdrId, pmId, masterId);
							returnMessage = ipoServices.insertPoHdrDtl(poList);
							return returnMessage;
						} else {
							returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
							returnMessage.setResponseDataMessage(ResponseMessageMap.successMsg);
							returnMessage.setResponseMessage(ResponseMessageMap.poAlreadyCreated);
							return returnMessage;
						}
//					}

				}
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage(ResponseMessageMap.successMsg);
				returnMessage.setResponseMessage(ResponseMessageMap.approved);
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage(ResponseMessageMap.failMsg);
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		}catch (Exception e) {
			logger.error("updateScsDtls error " + e);
		}

		return returnMessage;
	}

	@Override
	public GetPoDtlsEntity 	getPoDtlsByScsId(String igScpId,String tenantId,String empId, String pmHdrId, String pmId, String masterId) {
		GetPoDtlsEntity list = new GetPoDtlsEntity();
		List<IndentGrpScpVenEntity> scpVendorList = new ArrayList<IndentGrpScpVenEntity>();
//		List<IndentDtlTblEntity> indentDtllist = new ArrayList<IndentDtlTblEntity>();
		List<IndentGrpScpVenDtlEntity> scpVendorDtlList = new ArrayList<IndentGrpScpVenDtlEntity>();
		List<IndentGrpScpDtlEntity> scpDtlList =new ArrayList<IndentGrpScpDtlEntity>();
		List<IndentGrpScpVenPtEntity> scpvendorPtList=new ArrayList<IndentGrpScpVenPtEntity>();
		List<PoDtlEntity> poDtlList = new ArrayList<PoDtlEntity>();
		List<PoPaymentTermEntity> poPaymentTermList = new ArrayList<PoPaymentTermEntity>();
		PoDispatchDocEntity poDispatchDoc = new PoDispatchDocEntity();
		List<PoDispatchDocEntity> poDispatchDocList = new ArrayList<PoDispatchDocEntity>();
		String level="";
		try {
			String vendorQualified=iIndentGroupDAO.getVendorQualified(igScpId);
			scpDtlList=iIndentGroupDAO.getScpDtlList(igScpId);
			scpVendorList=iIndentGroupDAO.getScpVendorList(igScpId);
			scpVendorDtlList=iIndentGroupDAO.getScpVendorDtlList(igScpId);
			String indentId=poDAO.getIndentId(igScpId);

			list.setIgScpId(igScpId);
			list.setIndentID(indentId);
			list.setRevision(iIndentGroupDAO.getPoRevisionNoByIgScsId(igScpId));
			list.setRevisionDate(CommonMethod.getCurrentDate());
			list.setTenantId(tenantId);
			list.setEmpId(empId);
			list.setPmId(pmId);
			list.setProjectId(pmHdrId);
			list.setMasterId(masterId);

			if(scpVendorDtlList.size()>0) {
				if(vendorQualified.equalsIgnoreCase("L1")) {
					list.setBasicTotal(scpVendorDtlList.get(0).getL1ExtnFinalBasicTotal());
					list.setBasicTotalFx(scpVendorDtlList.get(0).getL1ExtnFinalBasicTotalFx());
					list.setTotalValue(scpVendorDtlList.get(0).getL1FinalTotalCost());
					list.setTotalValueFx(scpVendorDtlList.get(0).getL1FinalTotalCostFx());
					list.setGst(scpVendorDtlList.get(0).getL1FinalGSTValue());
					list.setGstFx(scpVendorDtlList.get(0).getL1FinalGSTValueFx());
					list.setPF(scpVendorDtlList.get(0).getL1FinalPF());
					list.setPFFX(scpVendorDtlList.get(0).getL1FinalPFFx());
					list.setRefDate(scpVendorDtlList.get(0).getL1FinalQuotedDate());
					list.setDeliveryDate(scpVendorDtlList.get(0).getL1DeliveryDate());
					list.setWarrenty(scpVendorDtlList.get(0).getL1Warrenty());
					list.setLiqDamages(scpVendorDtlList.get(0).getL1Ld());
					list.setRefNo(scpVendorDtlList.get(0).getL1FinalQuotedRef());
					list.setTransportCharges(scpVendorDtlList.get(0).getL1FinalTransportCharges());
					list.setTransportChargesFx(scpVendorDtlList.get(0).getL1FinalTransportChargesFx());
					list.setIntialunitPrice(scpVendorDtlList.get(0).getL1SubTotal());
//					list.setIntialunitPrice(scpVendorDtlList.get(0).getL1UnitFinalBasicTotalFx());
					list.setIntialExtendedPrice(scpVendorDtlList.get(0).getL1FinalSubTotal());
					if(scpVendorList.size()>0) {
						list.setVendorCode(scpVendorList.get(0).getL1VendorCode());
						list.setPoGst(scpVendorList.get(0).getL1Gst());
					}
					level="1";

				}else if(vendorQualified.equalsIgnoreCase("L2")) {
					list.setBasicTotal(scpVendorDtlList.get(0).getL2ExtnFinalBasicTotal());
					list.setBasicTotalFx(scpVendorDtlList.get(0).getL2ExtnFinalBasicTotalFx());
					list.setTotalValue(scpVendorDtlList.get(0).getL2FinalTotalCost());
					list.setTotalValueFx(scpVendorDtlList.get(0).getL2FinalTotalCostFx());
					list.setGst(scpVendorDtlList.get(0).getL2FinalGSTValue());
					list.setGstFx(scpVendorDtlList.get(0).getL2FinalGSTValueFx());
					list.setPF(scpVendorDtlList.get(0).getL2FinalPF());
					list.setRefDate(scpVendorDtlList.get(0).getL2FinalQuotedDate());
					list.setDeliveryDate(scpVendorDtlList.get(0).getL2DeliveryDate());
					list.setWarrenty(scpVendorDtlList.get(0).getL2Warrenty());
					list.setLiqDamages(scpVendorDtlList.get(0).getL2Ld());
					list.setRefNo(scpVendorDtlList.get(0).getL2FinalQuotedRef());
					list.setTransportCharges(scpVendorDtlList.get(0).getL2FinalTransportCharges());
					list.setTransportChargesFx(scpVendorDtlList.get(0).getL2FinalTransportChargesFx());
					list.setIntialunitPrice(scpVendorDtlList.get(0).getL2SubTotal());
//					list.setIntialunitPrice(scpVendorDtlList.get(0).getL2UnitFinalBasicTotalFx());
					list.setIntialExtendedPrice(scpVendorDtlList.get(0).getL2FinalSubTotal());
					if(scpVendorList.size()>0) {
						list.setVendorCode(scpVendorList.get(0).getL2VendorCode());
						list.setPoGst(scpVendorList.get(0).getL2Gst());
					}
					level="2";
				}else {
					list.setBasicTotal(scpVendorDtlList.get(0).getL3ExtnFinalBasicTotal());
					list.setBasicTotalFx(scpVendorDtlList.get(0).getL3ExtnFinalBasicTotalFx());
					list.setTotalValue(scpVendorDtlList.get(0).getL3FinalTotalCost());
					list.setTotalValueFx(scpVendorDtlList.get(0).getL3FinalTotalCostFx());
					list.setGst(scpVendorDtlList.get(0).getL3FinalGSTValue());
					list.setGstFx(scpVendorDtlList.get(0).getL3FinalGSTValueFx());
					list.setPF(scpVendorDtlList.get(0).getL3FinalPF());
					list.setRefDate(scpVendorDtlList.get(0).getL3FinalQuotedDate());
					list.setDeliveryDate(scpVendorDtlList.get(0).getL3DeliveryDate());
					list.setWarrenty(scpVendorDtlList.get(0).getL3Warrenty());
					list.setLiqDamages(scpVendorDtlList.get(0).getL3Ld());
					list.setRefNo(scpVendorDtlList.get(0).getL3FinalQuotedRef());
					list.setTransportCharges(scpVendorDtlList.get(0).getL3FinalTransportCharges());
					list.setTransportChargesFx(scpVendorDtlList.get(0).getL3FinalTransportChargesFx());
					list.setIntialunitPrice(scpVendorDtlList.get(0).getL3SubTotal());
//					list.setIntialunitPrice(scpVendorDtlList.get(0).getL3UnitFinalBasicTotalFx());
					list.setIntialExtendedPrice(scpVendorDtlList.get(0).getL3FinalSubTotal());
					if(scpVendorList.size()>0) {
						list.setVendorCode(scpVendorList.get(0).getL3VendorCode());
						list.setPoGst(scpVendorList.get(0).getL3Gst());
					}
					level="3";
				}
			}

			if(scpDtlList.size()>0) {
				for(int i=0;i<scpDtlList.size();i++) {
					PoDtlEntity poDtl = new PoDtlEntity();
					poDtl.setIndentDtlId(scpDtlList.get(i).getIndentDtlId());
					List<IndentDtlTblEntity> indentDtllist = indentUploadDAO.getIndentDtlsByIndentDtlId(scpDtlList.get(i).getIndentDtlId(), tenantId);
					poDtl.setQty(scpDtlList.get(i).getQty());
					poDtl.setUomCode(indentDtllist.get(0).getUnit());
					poDtl.setMaterialDesc(scpDtlList.get(i).getProdDesc());

					if(vendorQualified.equalsIgnoreCase("L1")) {
						//FX for foreign amount and currency type
						poDtl.setCurrencyType(scpDtlList.get(i).getL1CurrencyType());
						poDtl.setUnitRateFx(scpDtlList.get(i).getFinalL1UnitPriceFx());
						poDtl.setTotalValueFx(scpDtlList.get(i).getFinalL1ExtendedPriceFx());
						poDtl.setTotalValue(scpDtlList.get(i).getFinalL1ExtendedPrice());
						poDtl.setUnitRate(scpDtlList.get(i).getFinalL1UnitPrice());
						if(scpVendorList.size()>0) {
							poDtl.setPoGst(scpVendorList.get(0).getL1Gst());
						}
					}else if(vendorQualified.equalsIgnoreCase("L2")) {
						poDtl.setCurrencyType(scpDtlList.get(i).getL2CurrencyType());
						poDtl.setUnitRateFx(scpDtlList.get(i).getFinalL2UnitPriceFx());
						poDtl.setTotalValueFx(scpDtlList.get(i).getFinalL2ExtendedPriceFx());
						poDtl.setTotalValue(scpDtlList.get(i).getFinalL2ExtendedPrice());
						poDtl.setUnitRate(scpDtlList.get(i).getFinalL2UnitPrice());
						if(scpVendorList.size()>0) {
							poDtl.setPoGst(scpVendorList.get(0).getL2Gst());
						}
					}else {
						poDtl.setCurrencyType(scpDtlList.get(i).getL3CurrencyType());
						poDtl.setUnitRateFx(scpDtlList.get(i).getFinalL3UnitPriceFx());
						poDtl.setTotalValueFx(scpDtlList.get(i).getFinalL3ExtendedPriceFx());
						poDtl.setTotalValue(scpDtlList.get(i).getFinalL3ExtendedPrice());
						poDtl.setUnitRate(scpDtlList.get(i).getFinalL3UnitPrice());
						if(scpVendorList.size()>0) {
							poDtl.setPoGst(scpVendorList.get(0).getL3Gst());
						}
					}
					poDtlList.add(poDtl);
				}
			}

			scpvendorPtList =iIndentGroupDAO.getScpPtListForLevel(igScpId,level);
			if(scpvendorPtList.size()>0) {
				for(int j=0;j<scpvendorPtList.size();j++) {
					PoPaymentTermEntity poPaymentTerm = new PoPaymentTermEntity();
					poPaymentTerm.setPercentage(scpvendorPtList.get(j).getPercentage());
					poPaymentTerm.setTerm(scpvendorPtList.get(j).getTerm());
					poPaymentTermList.add(poPaymentTerm);
				}
			}

			poDispatchDoc.setInvoiceNo("0");
			poDispatchDocList.add(poDispatchDoc);
			list.setPoDtl(poDtlList);
			list.setPoDispatchDoc(poDispatchDocList);
			list.setPoPaymentTerm(poPaymentTermList);

		} catch (Exception ex) {
			logger.error("getPoDtlsByScsId Method Exception --->" + ex);

		}
		return list;

	}

	@Override
	public ResponseAsMessage deleteIndScpDtlId(DeleteIndScpDtlIdRequest deleteIndScpDtlIdreq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();

		try {
			int poCheck = iIndentGroupDAO.poScsCheck(deleteIndScpDtlIdreq.getIndScpId());
			if(poCheck>0) {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage(ResponseMessageMap.poCreateForScs);
				returnMessage.setResponseMessage(ResponseMessageMap.failTodeleteMsg);
			}else {
				String oldbudgetCol ="";
				String oldVendorQualified = iIndentGroupDAO.getVendorQualified(deleteIndScpDtlIdreq.getIndScpId());
				String indentGrpType = indentGroupDAO.getindentTypeCode(deleteIndScpDtlIdreq.getIndentId());
				String seqStr = "6";
				if(indentGrpType.equalsIgnoreCase("IT001")) {
					seqStr=	iIndentGroupDAO.getTenantPropertyVal("INDENT_SCM_SEQ_PRODUCT", deleteIndScpDtlIdreq.getTenantId());
				}else if(indentGrpType.equalsIgnoreCase("IT002")) {
					seqStr=	iIndentGroupDAO.getTenantPropertyVal("INDENT_SCM_SEQ_SERVICE", deleteIndScpDtlIdreq.getTenantId());
				}
				if(seqStr == null ) {
					seqStr = "6";
				}
				if(!oldVendorQualified.equalsIgnoreCase("")) {
					if(oldVendorQualified.equalsIgnoreCase("L1")) {
						oldbudgetCol="L1_FINAL_SUB_TOTAL";
					}else if(oldVendorQualified.equalsIgnoreCase("L2")) {
						oldbudgetCol="L2_FINAL_SUB_TOTAL";
					}else {
						oldbudgetCol="L3_FINAL_SUB_TOTAL";
					}
				}
				//	String scpIndentId	 = iIndentGroupDAO.getindentIdBygrpScd(deleteIndScpDtlIdreq.getIndScpId());
				if(!oldbudgetCol.equalsIgnoreCase("")) {

					//	String scpIndentId	 = iIndentGroupDAO.getindentIdBygrpScd(deleteIndScpDtlIdreq.getIndScpId());
					String	oldFinalBasicTotal=indentGroupDAO.getScmBudgetValueByigscsId(deleteIndScpDtlIdreq.getIndScpId(), oldbudgetCol);
					indentUploadDAO.updateScmBudgetvalByoperation(oldFinalBasicTotal, deleteIndScpDtlIdreq.getIndentId(), "-");
				}
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				String IndentCode=iIndentGroupDAO.getindentCode(deleteIndScpDtlIdreq.getIndScpId());
				messageList.add(IndentCode);
				iIndentGroupDAO.deleteScpId(deleteIndScpDtlIdreq.getIndScpId(), "indent_grp_scs_dtl");
				iIndentGroupDAO.deleteScpId(deleteIndScpDtlIdreq.getIndScpId(), "indent_grp_scs_status");
				iIndentGroupDAO.deleteScpId(deleteIndScpDtlIdreq.getIndScpId(), "indent_grp_scs_ven_pt");
				iIndentGroupDAO.deleteScpId(deleteIndScpDtlIdreq.getIndScpId(), "indent_grp_scs_ven_dtl");
				iIndentGroupDAO.deleteScpId(deleteIndScpDtlIdreq.getIndScpId(), "indent_grp_scs_ven");
//			    iIndentGroupDAO.deletePoScsId(deleteIndScpDtlIdreq.getIndScpId(), deleteIndScpDtlIdreq.getTenantId());
				iIndentGroupDAO.deleteScpId(deleteIndScpDtlIdreq.getIndScpId(), "indent_grp_scs");

				//	indentUploadDAO.updateIndentHdrStatusAndSeq(deleteIndScpDtlIdreq.getIndentId(), seqStr, "DS070", deleteIndScpDtlIdreq.getEmpId());


				commonNotifyMethod.InvokeNotificationMethod(1	,60, deleteIndScpDtlIdreq.getEmpId(), deleteIndScpDtlIdreq.getTenantId(), messageList,
						otherEmp, "1", "5", deleteIndScpDtlIdreq.getMstId(), null);

				indentUploadDAO.updatenotification(IndentCode, "DC038", deleteIndScpDtlIdreq.getTenantId());

				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage(ResponseMessageMap.successfulDeleted);
				returnMessage.setResponseMessage(ResponseMessageMap.successfulDeleted);
			}

		} catch (Exception ex) {
			logger.error("deleteIndScpDtlId error " + ex);
		}
		return returnMessage;
	}
	// yet to finish
	@Override
	public ResponseAsList getIndentGroupDtlsForSCS(HdrIdandTenantIdRequest hdrIdandTenantIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<IndentGroupDetailsEntity> list = new ArrayList<IndentGroupDetailsEntity>();
		try {
			String tenantId = hdrIdandTenantIdReq.getTenantId();
			String pmHdrId = hdrIdandTenantIdReq.getHdrId();
			String processDoc = hdrIdandTenantIdReq.getProcessCode();
			String partCount="";
			list = iIndentGroupDAO.getIndentGroupDtlsForSCS(pmHdrId,tenantId);
			if(list.size()>0) {
				// Same fix as getIndentGroupDetails above: docGroupList only depends on tenantId/processDoc
				// (constant for this request) - fetch once. The doc-lifecycle/status lookups repeat the same
				// (currSeq, docGroup) / docStatus combinations across many rows too - cache them per-request
				// (read-only lookups, never mutated after being fetched).
				List<String> docGroupList = iIndentGroupDAO.getDistinctScsDocGroup("DC038", tenantId, processDoc);
				Map<String, List<DocumentStatusMstEntity>> nextSeqDocCache = new HashMap<>();
				Map<String, String> statusDescCache = new HashMap<>();
				for(int i=0;i<list.size();i++) {
					list.get(i).setSNumber(String.valueOf(i+1));
					int verFlag = iIndentGroupDAO.pjsversioncheck(list.get(i).getIgHdrId());
					list.get(i).setVersionCheck(Integer.toString(verFlag));
					String vendorQualified=iIndentGroupDAO.getVendorQualified(list.get(i).getScsId());
					String basicTotalCol="";
					if(vendorQualified.equalsIgnoreCase("L1")) {
						basicTotalCol="L1_FINAL_SUB_TOTAL";
					}else if(vendorQualified.equalsIgnoreCase("L2")) {
						basicTotalCol="L2_FINAL_SUB_TOTAL";
					}else {
						basicTotalCol="L3_FINAL_SUB_TOTAL";
					}
					String venDtlBasicCost = iIndentGroupDAO.venDtlBasicCost(list.get(i).getIgHdrId(),basicTotalCol);

					list.get(i).setFinalCost(venDtlBasicCost);
					String venDtlBasicCostFx = indentGroupDAO.venDtlBasicCostFx(list.get(i).getIgHdrId(),basicTotalCol);
					list.get(i).setFinalCostFx(venDtlBasicCostFx);

					String	docGroup = resolveScsDocGroup(list.get(i).getFinalCost(), docGroupList);

					List<ScpDtlsEntity>	mainList=iIndentGroupDAO.getScpDtlsByIgHdrId(list.get(i).getIgHdrId(),tenantId);
					String currSeq = "1";
					if(mainList.size()>0) {
						currSeq=mainList.get(0).getSeqNo();
					}
					String seqGroupKey = currSeq + "|" + docGroup;
					List<DocumentStatusMstEntity> docLifeCycleMstList = nextSeqDocCache.get(seqGroupKey);
					if (docLifeCycleMstList == null) {
						docLifeCycleMstList = designTaskDAO.getNextSeqandStatusByDoc(Integer.parseInt(currSeq), "DC038",
								tenantId, docGroup,processDoc);
						nextSeqDocCache.put(seqGroupKey, docLifeCycleMstList);
					}
					if(docLifeCycleMstList.size()>0) {
						String docStatus = docLifeCycleMstList.get(0).getDocStatus();
						String nextStatusDesc = statusDescCache.get(docStatus);
						if (nextStatusDesc == null) {
							nextStatusDesc = designTaskDAO.getStatusByDesc(docStatus, tenantId);
							statusDescCache.put(docStatus, nextStatusDesc);
						}
						list.get(i).setNextStatus(nextStatusDesc);
					}
					if(list.get(i).getIsInventory().equalsIgnoreCase("1")) {
						list.get(i).setIsInventory("True");
						list.get(i).setPoStatus("NA");
						list.get(i).setScsStatus("NA");
					}else {
						list.get(i).setIsInventory("False");
						int count = iIndentGroupDAO.getIndentgrpScsCountByIgHdrId(list.get(i).getIgHdrId());
						if(count>0) {
							list.get(i).setScsStatus(iIndentGroupDAO.getScsStatusDesc(list.get(i).getIgHdrId()));
							int poDtlCount=iIndentGroupDAO.getPoCountByIgScsId(list.get(i).getScsId());
							if(poDtlCount>0) {
								list.get(i).setPoId(String.valueOf(poDtlCount));
								list.get(i).setPoStatus(iIndentGroupDAO.getPoStatusDesc(list.get(i).getScsId(),tenantId));
							}else {
								list.get(i).setPoStatus("PO Not Created");
							}
						}else {
							list.get(i).setScsStatus("PJS Not Created");
							list.get(i).setPoStatus("PO Not Created");
						}
					}
					partCount=String.valueOf(iIndentGroupDAO.getIndentGrpDtlCountByIgHdrId(list.get(i).getIgHdrId()));
					list.get(i).setPartCount(partCount);
				}
			}

			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getIndentGroupDetails error---> " + ex);
		}
		return returnList;
	}

}