package com.vmfg.project.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.dao.interfaces.IBudgetExcessSheetDAO;
import com.vmfg.project.entity.BudgetExcessSheetEntity;
import com.vmfg.project.entity.GetIndentBudgetDtlsEntity;
import com.vmfg.project.entity.RetriveBudgetExcessStatusDtlEntity;
import com.vmfg.project.entity.SalesCategoryDtlEntity;
import com.vmfg.project.request.BudgetExcessSheetRequest;
import com.vmfg.project.request.BudgetExcessStatusDtlReq;
import com.vmfg.project.request.IndentBudgetDtlReq;
import com.vmfg.project.request.updateBudgetExcessSheetRequest;
import com.vmfg.project.response.BudgetExcessBasedIndentHdrDtl;
import com.vmfg.project.service.interfaces.IBudgetExcessSheetService;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class BudgetExcessSheetService implements IBudgetExcessSheetService {
	private static final Logger logger = LoggerFactory.getLogger(BudgetExcessSheetService.class);
	@Autowired
	IBudgetExcessSheetDAO iBudgetExcessSheetDAO;
	@Autowired
	private DesignTaskDAO designTaskDAO;
	@Autowired
	private IndentUploadDAO indentUploadDAO;
	@Autowired
	private StageManagementDAO stageManagementDAO;
	@Autowired
	private UploadManagementDAO uploadManagementDAO;
	
	@Autowired
	private CommonNotifyMethod commonNotifyMethod;

	@Override
	public ResponseAsMessage insertBudgetExcessSheetDtl(BudgetExcessSheetRequest budgetExcessSheetRequest) {
		logger.debug("insertBudgetExcessSheetDtl   method Start");
		ResponseAsMessage rmsg = new ResponseAsMessage();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		int insertBudgetStatusDtl = 0;
		try {
			String seqNo = "1";
			// getIndentHdrDetail
			BudgetExcessBasedIndentHdrDtl hdrDtl = iBudgetExcessSheetDAO
					.getIndentHdrDetail(budgetExcessSheetRequest.getIndentId());
			// getDocCurrentSeq
			List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = iBudgetExcessSheetDAO
					.getDocCurrentSeqDtl("DC039", "1", budgetExcessSheetRequest.getTenantID(),
							budgetExcessSheetRequest.getProcessDoc());
			DocumentStatusMstEntity listObj = currSeqDocLifeCycleMstList.get(0);
			BigDecimal actualCost = new BigDecimal(hdrDtl.getScmBudAllocatedValue());
			BigDecimal targetCost = new BigDecimal(hdrDtl.getTargetValue());
			BigDecimal excessValue = actualCost.subtract(targetCost);
			String excess = String.valueOf(excessValue);
			String reason = null;
			String rootCase = null;
			String action = null;
			String responsible = null;
			BigDecimal scsActualCost= BigDecimal.ZERO;
			// insertBudgetSheetDtl
			String bugtExCheck = iBudgetExcessSheetDAO.checkIndentExcessCount(hdrDtl.getIndentId(), budgetExcessSheetRequest.getTenantID());
			if(!bugtExCheck.equalsIgnoreCase("0") ){
				scsActualCost = new BigDecimal(budgetExcessSheetRequest.getScsFinalCost()); 
			}else {
				scsActualCost = new BigDecimal (hdrDtl.getScmBudAllocatedValue()).subtract(targetCost);
			}
			int beHdrId = iBudgetExcessSheetDAO.insertBudgetExcessSheetDtl(hdrDtl.getIndentId(), hdrDtl.getPmHdrId(),
					hdrDtl.getTargetValue(), hdrDtl.getScmBudAllocatedValue(), excess,
					budgetExcessSheetRequest.getVendor(), reason, rootCase, action, responsible, seqNo,
					listObj.getDocStatus(), budgetExcessSheetRequest.getUpdatedBy(),
					budgetExcessSheetRequest.getTenantID(), hdrDtl.getDskId(), budgetExcessSheetRequest.getIgScsId(),String.valueOf(scsActualCost));
			// insertBudgetSheetStatus
			String remarks = "New Budget Excess";
			if (beHdrId > 0) {
				insertBudgetStatusDtl = iBudgetExcessSheetDAO.insertBudgetExcessStatus(beHdrId, seqNo,
						listObj.getDocStatus(), remarks, budgetExcessSheetRequest.getUpdatedBy(),
						budgetExcessSheetRequest.getTenantID());
				String indentCode=indentUploadDAO.getIndentCodeByIndentId(budgetExcessSheetRequest.getIndentId());
				messageList.add(indentCode);
				
				// get docGroup based on excess value
				String docGrp = getDocGroup(excess,budgetExcessSheetRequest.getTenantID(),budgetExcessSheetRequest.getProcessDoc());
				String nextApprDesig = indentUploadDAO.getIndentNxtAppDesc("DC039", "1",docGrp,budgetExcessSheetRequest.getTenantID());
				
				commonNotifyMethod.InvokeNotificationMethod(1,43, null, budgetExcessSheetRequest.getTenantID(), messageList, otherEmpId, "1", budgetExcessSheetRequest.getPmId(), budgetExcessSheetRequest.getMasterId(), nextApprDesig);
				commonNotifyMethod.InvokeApprovalDesigMethod(budgetExcessSheetRequest.getPmId(), "DC039",String.valueOf(beHdrId),budgetExcessSheetRequest.getProjectId(), budgetExcessSheetRequest.getTenantID(),"" ,nextApprDesig, indentUploadDAO.getEnqIdByProjectId(budgetExcessSheetRequest.getProjectId()),indentCode);
		
			}
			if (beHdrId > 0 && insertBudgetStatusDtl > 0) {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				rmsg.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				rmsg.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
		} catch (Exception ex) {
			logger.error("insertBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("insertBudgetExcessSheetDtl   method end");
		return rmsg;
	}

	@Override
	public ResponseAsMessage updateBudgetExcessSheetDtl(updateBudgetExcessSheetRequest updateudget) {
		logger.debug("updateBudgetExcessSheetDtl   method Start");
		ResponseAsMessage rmsg = new ResponseAsMessage();
		try {
			int qResp = iBudgetExcessSheetDAO.updateBudgetExcessSheetDtl(updateudget);

			if (qResp > 0) {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				rmsg.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				rmsg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		} catch (Exception ex) {
			logger.error("updateBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("updateBudgetExcessSheetDtl   method end");
		return rmsg;
	}

	@Override
	public List<BudgetExcessSheetEntity> retriveBudgetExcessSheetDtl(BudgetExcessStatusDtlReq statusDtlReq) {
		logger.debug("retriveBudgetExcessSheetDtl   method Start");
		List<BudgetExcessSheetEntity> list = new ArrayList<>();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<>();
		List<BudgetExcessSheetEntity> newTotalList = new ArrayList<>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String docGroup = "";
			list = iBudgetExcessSheetDAO.retriveBudgetExcessSheetDtl(statusDtlReq);
			int serialNumber = 1;
			for (BudgetExcessSheetEntity listObj : list) {
				listObj.setSerialNumber(String.valueOf(serialNumber++));
				listObj.setAssemblyValue(iBudgetExcessSheetDAO.getAssemblyDes(statusDtlReq.getTenantId(),listObj.getIndentId()));
				listObj.setSubAssemblyValue(iBudgetExcessSheetDAO.getSubAssemblyDes(statusDtlReq.getTenantId(),listObj.getIndentId()));
				listObj.setBudgetCostlat(iBudgetExcessSheetDAO.getBudgetCodeByIndentId(listObj.getIndentId()));
				listObj.setVerCheck(iBudgetExcessSheetDAO.getVerChechForBudgetExcessByBeHdrId(listObj.getBeHdrId(),listObj.getTenantId()));
				// get docGroup based on excess value
				docGroup = getDocGroup(listObj.getExcess(),statusDtlReq.getTenantId(),statusDtlReq.getProcessCode());

				int approveBtnEnable = 0;
				// DocList Start
				String currSeq = listObj.getSequenceNo();
				currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp("DC039", currSeq,
						statusDtlReq.getTenantId(), docGroup, statusDtlReq.getProcessCode());
				docLifeCycleMstList = designTaskDAO.getNextSeqandStatusByDoc(Integer.parseInt(currSeq), "DC039",
						statusDtlReq.getTenantId(), docGroup, statusDtlReq.getProcessCode());
				String nextSeqTypeCode = "NA";
				if(docLifeCycleMstList.size() > 0) {
					nextSeqTypeCode = designTaskDAO.getStatusByDesc(
							indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
									docLifeCycleMstList.get(0).getCurrSequence(),
									statusDtlReq.getTenantId(), "DC039", docGroup,statusDtlReq.getProcessCode()),
							statusDtlReq.getTenantId());
				}
				
				if (nextSeqTypeCode.isEmpty()) {
				    nextSeqTypeCode = "NA";
				}
				
				String nextSeq= docLifeCycleMstList.size() > 0 ? docLifeCycleMstList.get(0).getNextSeq() : "NA";
				
				listObj.setNextSeq(nextSeq);
				listObj.setNextSeqDesc(nextSeqTypeCode);
				
				if (docLifeCycleMstList.size() > 0) {

					String designCode = uploadManagementDAO.getDesigCodeByEmpId(statusDtlReq.getEmpId(),
							statusDtlReq.getTenantId());

					approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, docGroup,statusDtlReq.getProcessCode(),
							statusDtlReq.getTenantId(), "DC039", docLifeCycleMstList.get(0).getCurrSequence());
					if (approveBtnEnable == 1) {
						// curr seq
						docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO.getDocTypeDescByDocType(
								docLifeCycleMstList.get(0).getDocType(), statusDtlReq.getTenantId()));
						docLifeCycleMstList.get(0)
								.setDocStatusDesc(designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												docLifeCycleMstList.get(0).getCurrSequence(),
												statusDtlReq.getTenantId(), "DC039", docGroup,statusDtlReq.getProcessCode()),
										statusDtlReq.getTenantId()));// Current seq docStatus

						// Previous Seq
						docLifeCycleMstList.get(0).setPreviousSeq(currSeq);
						docLifeCycleMstList.get(0)
								.setPreviousSeqStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(currSeq,
										statusDtlReq.getTenantId(), "DC039", docGroup,statusDtlReq.getProcessCode()));

						docLifeCycleMstList.get(0)
								.setPreviousSeqStatusDesc(designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(currSeq,
												statusDtlReq.getTenantId(), "DC039", docGroup,statusDtlReq.getProcessCode()),
										statusDtlReq.getTenantId()));
						// cancel seq
						if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
							docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
							docLifeCycleMstList.get(0)
									.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
											currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
											statusDtlReq.getTenantId(), "DC039", docGroup,statusDtlReq.getProcessCode()));

							docLifeCycleMstList.get(0)
									.setCancelStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
													currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
													statusDtlReq.getTenantId(), "DC039", docGroup,statusDtlReq.getProcessCode()),
											statusDtlReq.getTenantId()));
						}
						listObj.setDocumentStatusMstList(docLifeCycleMstList);
					}
				}
				// DocList End//
				// BudgetSheet StatusList
				List<RetriveBudgetExcessStatusDtlEntity> statusList = iBudgetExcessSheetDAO
						.getBudegetStatusDtl(listObj.getBeHdrId(),statusDtlReq.getTenantId());
				listObj.setBudgetExcessStatusDtlList(statusList);

				newTotalList.add(listObj);

			}

			list.clear();
			list.addAll(newTotalList);
		} catch (Exception ex) {
			logger.error("retriveBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("retriveBudgetExcessSheetDtl   method end");
		return list;
	}

	@Override
	public List<SalesCategoryDtlEntity> getIndentBudgetDtl(IndentBudgetDtlReq indentBudgetDtl) {
		logger.debug("getIndentBudgetDtl   method Start");
		List<SalesCategoryDtlEntity> salesCategoryDtlList = new ArrayList<>();
		List<GetIndentBudgetDtlsEntity> list = new ArrayList<>();
		List<SalesCategoryDtlEntity> catagoryObj = new ArrayList<>();
		try {

			salesCategoryDtlList = iBudgetExcessSheetDAO.getSalesCatagoryDtl(indentBudgetDtl);

			for (SalesCategoryDtlEntity str : salesCategoryDtlList) {
				BigDecimal budegtValue = new BigDecimal(str.getBudgetValue());
				BigDecimal allocattedValue = new BigDecimal(str.getScmBudgetAllocated());
				BigDecimal excessValue = allocattedValue.subtract(budegtValue);
				String excess = String.valueOf(excessValue);
				str.setExcessBudgetValue(excess);
				list = iBudgetExcessSheetDAO.getIndentBudgetDtl(str.getSbcCode(), indentBudgetDtl.getPmHdrId());

				str.setList(list);
				catagoryObj.add(str);

			}
		} catch (Exception ex) {
			logger.error("getIndentBudgetDtl  method  exception" + ex);
		}
		logger.debug("getIndentBudgetDtl   method end");
		return catagoryObj;
	}

	@Override
	public ResponseAsMessage updateBudgetSheetExcessSeqAndStatus(UpdateSeqAndStatusRequest updateDtls) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {
			logger.debug("updateBudgetSheetExcessSeqAndStatus method Start");
			int insertBudegtSheetDtlStatusDtl = 0;
			String docType = "DC039";
			String budegtSheetDtlId = updateDtls.getHdrId();
			String excessValue = iBudgetExcessSheetDAO.getBudgetExcessValueByHdrId(budegtSheetDtlId);

			String docGrp = getDocGroup(excessValue,updateDtls.getTenantId(),updateDtls.getProcessCode());
			// Take Last Sequence from documentLifeCycle
			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp(docType,
					updateDtls.getCurrentseq(), updateDtls.getTenantId(), docGrp,updateDtls.getProcessCode());
			
				
				iBudgetExcessSheetDAO.UpdateIndentAndScsStatus(updateDtls.getCurrentseq(), updateDtls.getHdrId(), updateDtls.getTenantId(), updateDtls.getEmpId(), docGrp,updateDtls.getProcessCode());	
				
				// Update BudgetExcessDtl
						iBudgetExcessSheetDAO.updateBudgetSheetExcessSeqAndStatus(budegtSheetDtlId,
						updateDtls.getCurrentseq(), currSeqDocLifeCycleMstList.get(0).getDocStatus(),
						updateDtls.getTenantId(),updateDtls.getEmpId());
				
				// BudgetExcessDtlStatus
				insertBudegtSheetDtlStatusDtl = iBudgetExcessSheetDAO.insertBudgetSheetExcessStatusStatusDtl(
						budegtSheetDtlId, updateDtls.getCurrentseq(), currSeqDocLifeCycleMstList.get(0).getDocStatus(),
						updateDtls.getTenantId(), updateDtls.getRemarks(), updateDtls.getEmpId());

				String indentId=iBudgetExcessSheetDAO.getIndentIdByBehdrID(updateDtls.getHdrId());
				messageList.add(indentUploadDAO.getIndentCodeByIndentId(indentId));
				
				String nextApprDesig = commonNotifyMethod.getNxtAppDescByDocGroup("DC039", updateDtls.getCurrentseq(), updateDtls.getDocGroup(),updateDtls.getTenantId(),updateDtls.getProcessCode());
				if (nextApprDesig.equalsIgnoreCase("")) {
					nextApprDesig=null;
				}
				commonNotifyMethod.InvokeNotificationMethod(1,44, "", updateDtls.getTenantId(), messageList, otherEmpId, "1", updateDtls.getPmId(), null, nextApprDesig);
				
				if (currSeqDocLifeCycleMstList.get(0).getLastSeq()!=null && currSeqDocLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
					iBudgetExcessSheetDAO.updateBudgetSheetExcessApproved(budegtSheetDtlId);
				}
					commonNotifyMethod.InvokeApprovalDesigMethod(updateDtls.getPmId(), "DC039",updateDtls.getHdrId(),updateDtls.getPmHdrId(), updateDtls.getTenantId(),"" ,nextApprDesig, indentUploadDAO.getEnqIdByProjectId(updateDtls.getPmHdrId()),indentUploadDAO.getIndentCodeByIndentId(indentId));
				

				if (insertBudegtSheetDtlStatusDtl > 0) {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage("Success");
					returnMessage.setResponseMessage(ResponseMessageMap.approved);
				} else {

				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updateBudgetSheetExcessSeqAndStatus error " + ex);
		}
		logger.debug("updateBudgetSheetExcessSeqAndStatus method end");
		return returnMessage;
	}

	public String getDocGroup(String excessValue, String tenantId, String processCode) {
		String docGroup = "";
		try {
			BigDecimal excess=new BigDecimal(excessValue);
			List<String> docGroupList = iBudgetExcessSheetDAO.getDistinctDocGroup("DC039",tenantId,processCode);
			if (docGroupList.size() > 0) {
				for (String docGrpVal : docGroupList) {
					if (excess.compareTo(new BigDecimal(docGrpVal)) <= 0) {
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

}
