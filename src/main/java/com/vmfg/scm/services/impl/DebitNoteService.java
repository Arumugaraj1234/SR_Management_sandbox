package com.vmfg.scm.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.finance.dao.interfaces.IPraDAO;
import com.vmfg.finance.entity.GrnDtlsEntity;
import com.vmfg.finance.entity.PraStatusEntity;
import com.vmfg.finance.services.interfaces.IPraService;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.dao.interfaces.IGrnDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.entity.*;
import com.vmfg.scm.request.DebitNoteDtlRequest;
import com.vmfg.scm.services.interfaces.IPoServices;
import com.vmfg.tally.service.PayableSyncService;
import com.vmfg.tally.service.PraUpdateService;
import com.vmfg.tally.service.ReceivableSyncService;
import com.vmfg.tally.service.SalesBudgetSheetUpdation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.finance.request.RetrievePraRequest;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.scm.request.DebitNoteHdrAndDtlRequest;
import com.vmfg.scm.services.interfaces.IDebitNoteService;
import com.vmfg.task.dao.interfaces.IDesignTaskDAO;
import com.vmfg.util.CommonMethod;
import com.vmfg.scm.dao.interfaces.IDebitNoteDAO;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DebitNoteService implements IDebitNoteService {
	private static final Logger logger = LoggerFactory.getLogger(DebitNoteService.class);
	
	@Autowired
	private IDebitNoteDAO IDebitNoteDAO;

	@Autowired
	private IGrnDAO iGrnDAO;

	@Autowired
	private IPoDAO iPoDAO;


	@Autowired
	private UploadManagementDAO uploadManagementDAO;

	
	@Autowired
	IDesignTaskDAO iDesignTaskDAO;

	@Autowired
	PayableSyncService payableSyncService;

	@Autowired
	ReceivableSyncService receivableSyncService;

	@Autowired
	SalesBudgetSheetUpdation salesBudgetSheetUpdation;

	@Autowired
	PraUpdateService praUpdateService;
	
	@Autowired
	IndentUploadDAO indentUploadDAO;
	
	@Autowired
	StageManagementDAO stageManagementDAO;
	
	@Override
	public ResponseAsMessage insertDebitNoteHdrAndDtl(DebitNoteHdrAndDtlRequest debitNoteRequest) {
		ResponseAsMessage message = new ResponseAsMessage();
		int val = 0;
		try {
				int dnrId = 0;
				if(debitNoteRequest.getDnrId()==null){
					dnrId = IDebitNoteDAO.insertDebitNoteReason(debitNoteRequest.getDnReason(), debitNoteRequest.getTenantId());
					debitNoteRequest.setDnrId(String.valueOf(dnrId));
				}
                String lastDebitCode = IDebitNoteDAO.getLastDebitCode();
                String debitCode = CommonMethod.genNewDebitNoteCode(lastDebitCode);
				val = IDebitNoteDAO.insertDebitNoteHdrAndDtl(debitNoteRequest, debitCode);

				for(DebitNoteDtlRequest debitNoteDtlRequest : debitNoteRequest.getDebitNoteDtl()){
					String poQty = "";
					if(debitNoteDtlRequest.getPoDtlId()!=null){
						poQty = iPoDAO.getPoDtQty(debitNoteDtlRequest.getPoDtlId());
					}
					int updateQty = IDebitNoteDAO.updateIndentQtyByPoDtlQty(debitNoteDtlRequest, poQty); // update qty in the indent with po dtl qty to revert it...
					if(updateQty==0){
						message.setResponseCode(ResponseMessageMap.failToupdateCode);
						message.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
						message.setResponseMessage(Integer.toString(val));
						return message;
					}
				}

			if (val > 0 ) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage(ResponseMessageMap.successCreated);
				message.setResponseMessage(Integer.toString(val));
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
				message.setResponseMessage(Integer.toString(val));
			}

		} catch (Exception ex) {
			logger.error("insertDebitNoteHdrAndDtl Service Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage updateDebitNoteHdr(DebitNoteHdrAndDtlRequest debitNoteRequest) {
		ResponseAsMessage returnList = new ResponseAsMessage();
		
		try {			
				int updatePraHdr = IDebitNoteDAO.updateDebitNoteHdr(debitNoteRequest.getSeq(),debitNoteRequest.getSeqStatus(),debitNoteRequest.getIsLast(),CommonMethod.getCurrentDateTime(),debitNoteRequest.getEmpId(),debitNoteRequest.getDnId());

				  if(updatePraHdr>0) {
					  IDebitNoteDAO.insertDebitNoteStatusDtl(debitNoteRequest.getDnId(), debitNoteRequest.getSeq(), debitNoteRequest.getSeqStatus(), debitNoteRequest.getTenantId(), debitNoteRequest.getRemarks(), debitNoteRequest.getEmpId());
					    returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successUpdated);
				  }else {
					  returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
					  returnList.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				  }		
		} catch (Exception ex) {
			logger.error("updateDebitNoteHdr Service Method Error " + ex);
		}
		
		return returnList;
	}

	@Override
	public ResponseAsList retrieveDebitNote(RetrievePraRequest retrievePraReq) {
		ResponseAsList returnL = new ResponseAsList();
		try {
			List<GetDebitNoteEntity> returnList = null;
			List<PoDtlEntity> poDtl = null;
			List<IndentDtlTblEntity> indentDtlList = new ArrayList<IndentDtlTblEntity>();
			String poId = "";
			int dmId = 0;

//			payableSyncService.syncPayablesFromJson();
//			praUpdateService.updatePraCompletionStatus();
//			receivableSyncService.syncReceivablesFromJson();
//			salesBudgetSheetUpdation.updateReceivedBalance();
			if(retrievePraReq.getPoId().equalsIgnoreCase("getAll")) {
				poId = "%%";
			}else {
				poId = retrievePraReq.getPoId();
			}
			String pmHdrId = "";
			if(retrievePraReq.getPmHdrId().equalsIgnoreCase("getAll")) {
				pmHdrId = "%%";
			}else {
				pmHdrId = retrievePraReq.getPmHdrId();
			}
			String desiCode = iDesignTaskDAO.getEmpDesinationCode(retrievePraReq.getEmpId(), retrievePraReq.getTenantId());
			returnList = IDebitNoteDAO.getDebitNoteHdrListByPmHdrId(pmHdrId, poId, retrievePraReq.getTenantId());


			for(int i =0;i<returnList.size();i++) {
				List<DebitNoteDtlListEntity> DebitNoteDtl = IDebitNoteDAO.getDebitNoteSubList(returnList.get(i).getDnId());
				returnList.get(i).setDebitNoteDtl(DebitNoteDtl);
				List<DebitNoteStatusEntity> debitNoteStatus = IDebitNoteDAO.getDebitNoteStatusList(returnList.get(i).getDnId());
				returnList.get(i).setDebitNoteStatus(debitNoteStatus);
			// Extract PO DTL IDs from debit note
			Set<String> dnPoDtlIds = returnList.get(i).getDebitNoteDtl().stream()
					.map(DebitNoteDtlListEntity::getPoDtlId)
					.collect(Collectors.toSet());

			// Get full PO DTLs
			List<PoDtlEntity> allPoDtl = iPoDAO.getPoDtlList(returnList.get(i).getPoId());

			// Filter to keep only those in debit note
			List<PoDtlEntity> filteredPoDtl = allPoDtl.stream()
					.filter(po -> dnPoDtlIds.contains(po.getPoDtlId()))
					.collect(Collectors.toList());
				List<String> filteredPoDtlIds = filteredPoDtl.stream()
						.map(PoDtlEntity::getPoDtlId)
						.collect(Collectors.toList());

				List<GrnDtlsEntity> grnDtlsEntities = iGrnDAO.getgrnlistForDebitNote(filteredPoDtlIds, retrievePraReq.getTenantId());
				returnList.get(i).setGrnDtlsEntity(grnDtlsEntities);

//					for (int j = 0; j < poDtl.size(); j++) {
					for (PoDtlEntity podtlItem : filteredPoDtl) {
						podtlItem.setQtyInspectReqCount(
								Integer.toString(iPoDAO.qtyInspectReqCount(podtlItem.getPoDtlId())));
						indentDtlList = indentUploadDAO.getIndentDtlsByIndentDtlId(podtlItem.getIndentDtlId(),
								retrievePraReq.getTenantId());
						for (int k = 0; k < indentDtlList.size(); k++) {
							int indentDmId = indentUploadDAO.getDmIdByLatestVerion(indentDtlList.get(k).getIndentDtlId(),
									retrievePraReq.getTenantId());
							indentDtlList.get(k).setDmId(indentDmId);
						}
						String productCode = iPoDAO.getProdCodeByIndentDtlId(podtlItem.getIndentDtlId());

//						String qcRequestedQty=iPoDAO.getQcRequestyQty(poDtl.get(j).getPoDtlId());
						BigDecimal qcRequestedQty = BigDecimal.ZERO;

						// get pending qty
//						String pendingQty = poService.getQcPendingQty(podtlItem.getPoDtlId(), podtlItem.getQty());

//						podtlItem.setPendingQty(String.valueOf(pendingQty));
						podtlItem.setProductCode(productCode);
						podtlItem.setIndentDtlList(indentDtlList);
						podtlItem.setQcRequestedQty(String.valueOf(qcRequestedQty));

					}
					returnList.get(i).setPoDtlEntity(filteredPoDtl);

//					dmId = indentUploadDAO.getDmIdByLatestVerion(returnList.get(i).getDnId(),
//							retrievePraReq.getTenantId());
					dmId = IDebitNoteDAO.getDmIdByLatestVerionForDebit(returnList.get(i).getDnId(), "DC083", retrievePraReq.getTenantId());
					returnList.get(i).setDmId(String.valueOf(dmId));

//				returnList.get(i).setVerCheck(iPraDAO.getVerCheckByPraId(returnList.get(i).getPraId(),returnList.get(i).getTenantId()));
				List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq(retrievePraReq.getDocTypeCode(),returnList.get(i).getSeqno(), returnList.get(i).getTenantId());
				List<DocumentStatusMstEntity> docLifeCycleMstList = iDesignTaskDAO.getNextSeqandStatus(
						Integer.parseInt(returnList.get(i).getSeqno()),
						retrievePraReq.getDocTypeCode(), retrievePraReq.getTenantId());
				if (docLifeCycleMstList.size() > 0) {
					int approveBtnEnable = indentUploadDAO.getApprovebtnEnableByCurr(desiCode, returnList.get(i).getTenantId(),
							retrievePraReq.getDocTypeCode(),
							currSeqDocLifeCycleMstList.get(0).getCurrSequence());
					int approveNextBtnEnable = indentUploadDAO.getApprovebtnEnableByCurr(desiCode, returnList.get(i).getTenantId(),
							retrievePraReq.getDocTypeCode(),
							docLifeCycleMstList.get(0).getCurrSequence());
					
					docLifeCycleMstList.get(0)
							.setNextSeqStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
									docLifeCycleMstList.get(0).getCurrSequence(), returnList.get(i).getTenantId(),
									retrievePraReq.getDocTypeCode()));
				
					docLifeCycleMstList.get(0)
							.setNextSeqStatusDesc(iDesignTaskDAO.getStatusByDesc(indentUploadDAO
									.getStatusCodebySeqAndDocType(docLifeCycleMstList.get(0).getCurrSequence(),
											returnList.get(i).getTenantId(), retrievePraReq.getDocTypeCode()),
									returnList.get(i).getTenantId()));
					// cancel seq
					if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
						docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
						docLifeCycleMstList.get(0)
						.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
								currSeqDocLifeCycleMstList.get(0).getCancelSeq(),returnList.get(i).getTenantId(), retrievePraReq.getDocTypeCode()));

						docLifeCycleMstList.get(0).setCancelStatusDesc(iDesignTaskDAO.getStatusByDesc(
								indentUploadDAO.getStatusCodebySeqAndDocType(
										currSeqDocLifeCycleMstList.get(0).getCancelSeq(), returnList.get(i).getTenantId(),retrievePraReq.getDocTypeCode()),
								returnList.get(i).getTenantId()));
					}else {
						docLifeCycleMstList.get(0).setCancelSeq(null);
					}
					if (approveBtnEnable > 0) {
						returnList.get(i).setIsApproval("True");
					} else {
						returnList.get(i).setIsApproval("False");
					}
					if(approveNextBtnEnable>0) {
						returnList.get(i).setDocStatusMst(docLifeCycleMstList);
					}
				}
			}
				
			if (returnList.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(returnList);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("error in retrieveDebitNote service " + ex.getMessage());
		}

		return returnL;
	}

	@Override
	public ResponseAsMessage insertDebitNoteFileByDnID(JSONObject jsonObj, MultipartFile file) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> list = new ArrayList<DocumentStatusMstEntity>();
		int newDmId = 0;
		try {
			JSONArray iluoArray = jsonObj.getJSONArray("reqObj");
			String enquiryId = "",docType="",uploadDocType="";
			String tenantId = "";
			String type = "";
			String empId = "";
			String refId = "";
			String projectId = "";
			String stageCode = "";
			String remarks = "";
			String debitHdrId = "";
			String indentDtlId = "";
//			String documentType = "DC018";
//			String uploadDocType = "FC015";

//			String documentName="";
			for (int l = 0; l < iluoArray.length(); l++) {
				JSONObject iluoobjects = iluoArray.getJSONObject(l);
				JSONArray iluobodykeys = iluoobjects.names();
				for (int k = 0; k < iluobodykeys.length(); ++k) {
					String key = iluobodykeys.getString(k);
					String value = iluoobjects.getString(key);
					if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					} else if (key.equalsIgnoreCase("empId")) {
						empId = value;
					} else if (key.equalsIgnoreCase("type")) {
						type = value;
					} else if (key.equalsIgnoreCase("stageCode")) {
						stageCode = value;
					} else if (key.equalsIgnoreCase("enquiryId")) {
						enquiryId = value;
					} else if (key.equalsIgnoreCase("projectId")) {
						projectId = value;
					} else if (key.equalsIgnoreCase("indentHdrId")) {
						debitHdrId = value;
					} else if (key.equalsIgnoreCase("indentDtlId")) {
						indentDtlId = value;
					}else if (key.equalsIgnoreCase("docType")) {
						docType = value;
					} else if (key.equalsIgnoreCase("uploadDocType")) {
						uploadDocType = value;
					}

				}
			}

			String getfileName = file.getOriginalFilename();
			String fileName = getfileName.substring(0, getfileName.lastIndexOf('.'));

			String productCode = fileName;
			if (indentDtlId.equalsIgnoreCase("")) {
//				refId = iIndentUploadDAO.getIndentDtlIdByProductCode(productCode, tenantId, indentHdrId);
			} else {
				refId = indentDtlId;
			}
			if (!refId.equalsIgnoreCase("0")) {

				int version = 0;
				int checkCount = uploadManagementDAO.getCountByComb(tenantId, uploadDocType, debitHdrId, stageCode);
				if (checkCount > 0) {
					version = uploadManagementDAO.getLatestVersionbycomb(tenantId, uploadDocType, debitHdrId, stageCode);
					version = version + 1;
				} else {
					version = 1;
				}
				list = uploadManagementDAO.getSeqAndStatusByDocTypeCode(docType, tenantId);
				if (list.size() > 0) {
					newDmId = uploadManagementDAO.insertDocumentDtls(enquiryId, projectId, productCode, debitHdrId,
							stageCode, uploadDocType, version, tenantId, remarks, list.get(0).getCurrSequence(), "0",
							docType);
				}

				if (newDmId > 0) {
					int insertFileDtls = uploadManagementDAO.insertNewFileDtl(file, tenantId, newDmId, uploadDocType,
							empId, version, docType, type, debitHdrId);
					if (insertFileDtls == 1) {
						returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnMessage.setResponseDataMessage("Successfully Uploaded");
						returnMessage.setResponseMessage(ResponseMessageMap.successUpload);
					} else {

						returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						returnMessage.setResponseDataMessage(getfileName);
						returnMessage.setResponseMessage(ResponseMessageMap.noRecord);
					}
				}

			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Uploaded file doesn't match with the product updated.");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		} catch (Exception ex) {
			logger.error("insertIndentFileByIndentID error " + ex);
		}
		return returnMessage;
	}
}
