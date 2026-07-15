package com.vmfg.scm.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.vmfg.scm.entity.*;
import com.vmfg.scm.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.request.InitiateProcessRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.impl.StageManagementService;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.scm.dao.impl.MaterialInwardDAO;
import com.vmfg.scm.dao.interfaces.IGrnDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.services.interfaces.IGrnService;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class GrnService implements IGrnService {
	private static final Logger logger = LoggerFactory.getLogger(GrnService.class);

	@Autowired
	private IGrnDAO iGrnDAO;

	@Autowired
	private MaterialInwardDAO materialInwardDAO;

	@Autowired
	private StageManagementService stageManagementService;
	
	@Autowired
	private CommonNotifyMethod commonNotifyMethod;
	
	@Autowired
	private IndentUploadDAO indentUploadDAO;
	
	@Autowired
	private IPoDAO iPoDao;
	
	@Autowired
	private IndentGroupDAO indentGroupDAO;
	
	@Autowired
	private StageManagementDAO stageManagementDAO;

	@Override
	public ResponseAsList getGrnHdrDetails(GetGrnHdrRequest getGrnHdrRequest) {
		ResponseAsList returnList = new ResponseAsList();
		List<GrnHdrEntity> mainList = new ArrayList<>();

		try {
			mainList = iGrnDAO.getGrnHdrDetails(getGrnHdrRequest.getProjectId(), getGrnHdrRequest.getTenantId() , getGrnHdrRequest.getPoId(),
					getGrnHdrRequest.getFromDate(), getGrnHdrRequest.getToDate());
			if (mainList.size() > 0) {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getGrnHdrDetails service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPoCostType(GetPoCostTypeReq getPoCostTypeReq){
		ResponseAsList returnList = new ResponseAsList();
		List<PoCostTypeEntity> poCostTypeEntityList = new ArrayList<>();
		try{
			poCostTypeEntityList = iGrnDAO.getPoCostType(getPoCostTypeReq.getIsActive(), getPoCostTypeReq.getTenantId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if(poCostTypeEntityList.size() > 0){
			returnList.setResponseData(poCostTypeEntityList);
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.success);
		}
		else{
			returnList.setResponseData(poCostTypeEntityList);
			returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnList.setResponseMessage(ResponseMessageMap.noRecord);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getGrnDtlwithMaterialInwardDtl(GrnDtlRequest grnDtlRequest) {
		ResponseAsList returnList = new ResponseAsList();
		List<GrnDtlEntity> mainList = new ArrayList<>();

		try {
			mainList = iGrnDAO.getGrnDtlwithMaterialInwardDtl(grnDtlRequest.getGrnHdrId(), grnDtlRequest.getTenantId());
			if (mainList.size() > 0) {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getGrnDtlwithMaterialInwardDtl service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage insertQtyInspReq(InsertQtyInspRequest insertQtyInspReq) {
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		try {
			int totalReqinsert = 0;
			int totalReworkInsert=0;
			List<SelectedPODtlsReq> selectedPODtls = insertQtyInspReq.getSelectedPODtls();
			String poCode = iGrnDAO.getPoCodeByPoId(insertQtyInspReq.getPoId());
			if (selectedPODtls.size() > 0) {

				for (int i = 0; i < selectedPODtls.size(); i++) {
				//	String qty = iGrnDAO.getreceivedqtyBypoDtlId(selectedPODtls.get(i).get[i]);
					String indentDtlId = iGrnDAO.getindentDtlBypoDtlId(selectedPODtls.get(i).getPoDtlId());
					String poHdrId = iGrnDAO.getPmHdrIdByPOId(insertQtyInspReq.getPoId());
					int reqQtyInsertCount = 0,reworkInsertCount=0;
					
					BigDecimal reqQty=new BigDecimal(selectedPODtls.get(i).getQtyInspectReqCount());
					BigDecimal reworkQty=new BigDecimal(selectedPODtls.get(i).getReworkQty());
					
					String miDtlId = iGrnDAO.getMIIndentDtlBypoDtlId(selectedPODtls.get(i).getPoDtlId(), indentDtlId,insertQtyInspReq.getTenantId());
					if (!poHdrId.equalsIgnoreCase("")) {
						// insert Req_qty 
						if(reqQty != null && reqQty.compareTo(BigDecimal.ZERO) > 0) {
							reqQtyInsertCount = iGrnDAO.insertQualityInspecRequest(insertQtyInspReq.getPoId(), poCode,
									selectedPODtls.get(i).getPoDtlId(), selectedPODtls.get(i).getQtyInspectReqCount(), "0", insertQtyInspReq.getTenantId(), indentDtlId, poHdrId, "PO",
									miDtlId,"0",insertQtyInspReq.getEmpId());
							
							if(reqQtyInsertCount>0) {
								totalReqinsert = totalReqinsert + 1;
								// Update QI_REQ last updated datetime in indent_Dtl table
								String qiReqLastUpdatedDate=indentGroupDAO.getLastUpdatedDateTime(indentDtlId, insertQtyInspReq.getTenantId(), "QI_REQUEST");
								if(!qiReqLastUpdatedDate.equalsIgnoreCase("")){
									indentGroupDAO.updateLastUpdatedDateTime(indentDtlId, "QI_REQUESTED_DATETIME", qiReqLastUpdatedDate, insertQtyInspReq.getTenantId());
								}else {
					            	indentGroupDAO.reUpdatedDateTimeIndentDtl(indentDtlId, "QI_REQUESTED_DATETIME", insertQtyInspReq.getTenantId());
					            }
							}
							
							
							List<String> messageList = new ArrayList<>();
							List<String> otherEmp = new ArrayList<>();
							String projCode = indentUploadDAO.getProjectCodeByProjId(insertQtyInspReq.getPmHdrId(), insertQtyInspReq.getTenantId());
							String productCode = iPoDao.getProdDescByIndentDtlId(indentDtlId);
							messageList.add(poCode);
							messageList.add( projCode);
							messageList.add(productCode);
							String qHdrId = iGrnDAO.getQualityHdrId( insertQtyInspReq.getPmHdrId(),insertQtyInspReq.getTenantId());
							List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC068", "1",
									insertQtyInspReq.getTenantId());
							String approvingDesc= currSeqDocLifeCycleMstList.get(0).getApprDesi();
							logger.info("approvingDesc "+approvingDesc);
							commonNotifyMethod.InvokeNotificationMethod(1, 23, "", 
									insertQtyInspReq.getTenantId(), messageList,
									otherEmp, "1","6", qHdrId, null);
							commonNotifyMethod.InvokeApprovalDesigMethod("6",
									"DC068", Integer.toString(reqQtyInsertCount), insertQtyInspReq.getPmHdrId(), insertQtyInspReq.getTenantId(),
									"", approvingDesc,insertQtyInspReq.getEnquiryId(),poCode +"-"+productCode );
						}else {
							totalReqinsert = totalReqinsert + 1;
						}
						
						// insert Rework_qty 
						if(reworkQty != null && reworkQty.compareTo(BigDecimal.ZERO) > 0) {
							reworkInsertCount = iGrnDAO.insertQualityInspecRequest(insertQtyInspReq.getPoId(), poCode,
									selectedPODtls.get(i).getPoDtlId(), selectedPODtls.get(i).getReworkQty(), "0", insertQtyInspReq.getTenantId(), indentDtlId, poHdrId, "PO",
									miDtlId,"1",insertQtyInspReq.getEmpId());
							if(reworkInsertCount>0) {
							totalReworkInsert = totalReworkInsert + 1;
							
								// Update QI_REQ last updated datetime in indent_Dtl table
								String qiReqLastUpdatedDate=indentGroupDAO.getLastUpdatedDateTime(indentDtlId, insertQtyInspReq.getTenantId(), "QI_REQUEST");
								if(!qiReqLastUpdatedDate.equalsIgnoreCase("")){
									indentGroupDAO.updateLastUpdatedDateTime(indentDtlId, "QI_REQUESTED_DATETIME", qiReqLastUpdatedDate, insertQtyInspReq.getTenantId());
								}else {
					            	indentGroupDAO.reUpdatedDateTimeIndentDtl(indentDtlId, "QI_REQUESTED_DATETIME", insertQtyInspReq.getTenantId());
					            }
							}
							List<String> messageList = new ArrayList<>();
							List<String> otherEmp = new ArrayList<>();
							String projCode = indentUploadDAO.getProjectCodeByProjId(insertQtyInspReq.getPmHdrId(), insertQtyInspReq.getTenantId());
							String productCode = iPoDao.getProdDescByIndentDtlId(indentDtlId);
							messageList.add(poCode);
							messageList.add( projCode);
							messageList.add( productCode);
							String qHdrId = iGrnDAO.getQualityHdrId( insertQtyInspReq.getPmHdrId(),insertQtyInspReq.getTenantId());
							List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC068", "1",
									insertQtyInspReq.getTenantId());
							String approvingDesc= currSeqDocLifeCycleMstList.get(0).getApprDesi();
							logger.info("approvingDesc "+approvingDesc);
						
							commonNotifyMethod.InvokeNotificationMethod(1, 23, "", 
									insertQtyInspReq.getTenantId(), messageList,
									otherEmp, "1","6", qHdrId, null);
							commonNotifyMethod.InvokeApprovalDesigMethod("6",
									"DC068", Integer.toString(reworkInsertCount), insertQtyInspReq.getPmHdrId(), insertQtyInspReq.getTenantId(),
									"", approvingDesc,insertQtyInspReq.getEnquiryId(),poCode+"-"+productCode );
						}else {
							totalReworkInsert = totalReworkInsert + 1;
						}
						
						InitiateProcessRequest initiateReq = new InitiateProcessRequest();
						initiateReq.setDeptCode("");
						initiateReq.setDueDate(CommonMethod.getCurrentDate());
						initiateReq.setEmpID(insertQtyInspReq.getEmpId());
						initiateReq.setRefId(poHdrId);
						initiateReq.setTenantId(insertQtyInspReq.getTenantId());
						initiateReq.setStartDate(CommonMethod.getCurrentDate());
						initiateReq.setPmId(insertQtyInspReq.getPmId());
						stageManagementService.initiateProcess(initiateReq);
						
					}
				}
				logger.info("commonNotifyMethod in GRN service");
				if (totalReqinsert == selectedPODtls.size()&& totalReworkInsert==selectedPODtls.size()) {
					
					
					returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMsg.setResponseDataMessage(ResponseMessageMap.success);
					returnMsg.setResponseMessage(ResponseMessageMap.successUpdated);
				} else {
					returnMsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					returnMsg.setResponseDataMessage(ResponseMessageMap.partialSucess);
					returnMsg.setResponseMessage(ResponseMessageMap.partialSucess);
				}

			}
		} catch (Exception e) {
			logger.error("insertQtyInspReq error " + e);
		}
		return returnMsg;
	}

	@Override
	public ResponseAsMessage insertMIQtyReq(InsertQtyInspRequest insertQtyInspReq) {
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		try {
			int totalinsert = 0;
			List<SelectedPODtlsReq> selectedPODtls = insertQtyInspReq.getSelectedPODtls();
			String poCode = iGrnDAO.getPoCodeByPoId(insertQtyInspReq.getPoId());
			if (selectedPODtls.size() > 0) {

				for (int i = 0; i < selectedPODtls.size(); i++) {
				//	String qty = grnDAO.getqtyByMIDtlId(selectedPODtls[i]);
					List<MaterialInwardDtlEntity> miDtllist = materialInwardDAO
							.getMaterialInwardDtlListByMiDtlId(selectedPODtls.get(i).getMiDtlId(), insertQtyInspReq.getTenantId());
					String poHdrId = iGrnDAO.getPmHdrIdByPOId(insertQtyInspReq.getPoId());
					int insertCount = 0;
					if (!poHdrId.equalsIgnoreCase("")) {
						insertCount = iGrnDAO.insertQualityInspecRequest(insertQtyInspReq.getPoId(), poCode,
								miDtllist.get(0).getPoDtlId(), selectedPODtls.get(i).getQtyInspectReqCount(), "0", insertQtyInspReq.getTenantId(),
								miDtllist.get(0).getIndentDtlId(), poHdrId, "MI", miDtllist.get(0).getMiDtlId(),"0",insertQtyInspReq.getEmpId());

					}
					if(insertCount>0) {
					totalinsert = totalinsert + 1;
						
						// Update QI_REQ last updated datetime in indent_Dtl table
						String qiReqLastUpdatedDate=indentGroupDAO.getLastUpdatedDateTime(miDtllist.get(0).getIndentDtlId(), insertQtyInspReq.getTenantId(), "QI_REQUEST");
						if(!qiReqLastUpdatedDate.equalsIgnoreCase("")){
							indentGroupDAO.updateLastUpdatedDateTime(miDtllist.get(0).getIndentDtlId(), "QI_REQUESTED_DATETIME", qiReqLastUpdatedDate, insertQtyInspReq.getTenantId());
						}else {
			            	indentGroupDAO.reUpdatedDateTimeIndentDtl(miDtllist.get(0).getIndentDtlId(), "QI_REQUESTED_DATETIME", insertQtyInspReq.getTenantId());
			            }
					}
					List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
					String NotifyEmp=iGrnDAO.getNotifyEmp(insertQtyInspReq.getTenantId());
					if(!NotifyEmp.isEmpty()) {
						String empArr[]=NotifyEmp.split(",");
						for(int emp=0;emp<empArr.length;emp++) {
							otherEmp.add(empArr[emp]);
						}
					}
					
					String pmHdrId=iGrnDAO.getpmHdrId(insertQtyInspReq.getPoId());
					String enquiryId=iGrnDAO.getenquiryId(pmHdrId);
					String projCode = indentUploadDAO.getProjectCodeByProjId(pmHdrId, insertQtyInspReq.getTenantId());
					String productCode = iPoDao.getProdDescByIndentDtlId(miDtllist.get(0).getIndentDtlId());
					messageList.add(poCode);
					messageList.add(projCode );
					messageList.add(productCode );
					String qHdrId = iGrnDAO.getQualityHdrId( pmHdrId,insertQtyInspReq.getTenantId());
					List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC068", "1",
							insertQtyInspReq.getTenantId());
					String approvingDesc= currSeqDocLifeCycleMstList.get(0).getApprDesi();
					logger.info("approvingDesc "+approvingDesc);
					
					commonNotifyMethod.InvokeNotificationMethod(1, 23, "", 
							insertQtyInspReq.getTenantId(), messageList,
							otherEmp, "1","6", qHdrId, null);
					commonNotifyMethod.InvokeApprovalDesigMethod("6",
							"DC068",Integer.toString(insertCount), pmHdrId, insertQtyInspReq.getTenantId(),
							"", approvingDesc,enquiryId,poCode+"-"+productCode );
				}
				if (totalinsert == selectedPODtls.size()) {
					//
					
//					//---
//					commonNotifyMethod.InvokeNotificationMethod(1, 24, "", 
//							insertQtyInspReq.getTenantId(), messageList,
//							otherEmp, "",insertQtyInspReq.getPmId(), insertQtyInspReq.getMstId(), null);
//					commonNotifyMethod.InvokeApprovalDesigMethod(insertQtyInspReq.getPmId(),
//							"", insertQtyInspReq.getPoId(), pmHdrId, insertQtyInspReq.getTenantId(),
//							"", "DS04,DS11",enquiryId ,poCode);
					returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMsg.setResponseDataMessage(ResponseMessageMap.success);
					returnMsg.setResponseMessage(ResponseMessageMap.successUpdated);
				} else {
					returnMsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					returnMsg.setResponseDataMessage(ResponseMessageMap.partialSucess);
					returnMsg.setResponseMessage(ResponseMessageMap.partialSucess);
				}
			}
		} catch (Exception e) {
			logger.error("insertMIQtyReq error " + e);
		}
		return returnMsg;
	}

	@Override
	public ResponseAsMessage grnCancel(GrnCancelReq grnCancelReq){
		ResponseAsMessage rsmg =new ResponseAsMessage();
		String update = "";
		try{
			if(!grnCancelReq.getGrnDtlEntityList().isEmpty()){
				for(GrnDtlEntity grnDtl : grnCancelReq.getGrnDtlEntityList()){
					boolean isAvailable = iGrnDAO.checkCountInInvProdDtl(grnDtl.getProductId(), grnCancelReq.getInventoryLoc(), grnCancelReq.getTenantId(), grnDtl.getGrnReceivedQty());
					if(!isAvailable){
						rsmg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						rsmg.setResponseDataMessage(ResponseMessageMap.failTodeleteMsg);
						rsmg.setResponseMessage(ResponseMessageMap.failTodeleteMsg);
						return rsmg;
					}
				}
			}
			update = iGrnDAO.grnCancel(grnCancelReq);
			if(update.equals("Success")){
				for(GrnDtlEntity grnDtl : grnCancelReq.getGrnDtlEntityList()){
					iGrnDAO.removeQtyFromInvPrdDtl(grnDtl, grnCancelReq.getInventoryLoc(), grnCancelReq.getTenantId());
				}
			}
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		if(update.equalsIgnoreCase("success")){
			rsmg.setResponseCode(ResponseMessageMap.responseCodeOk);
			rsmg.setResponseDataMessage(ResponseMessageMap.success);
			rsmg.setResponseMessage(ResponseMessageMap.successUpdated);
		}
		else {
			rsmg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			rsmg.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
			rsmg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
		}
		return rsmg;
	}

	@Override
	public ResponseAsMessage insertGrnHdrAndDtl(GrnHdrInsertRequest grnHdrAndDtl) {
//		List<ProductDtlsEntity> prodDtls=new ArrayList<ProductDtlsEntity>();
		ResponseAsMessage rmsg = new ResponseAsMessage();
		int grnDtlRes = 0, increaseReceivedQty = 0;
		String invLocation = grnHdrAndDtl.getInvLocation();
		String miId = "0"; String UpdatedDateTime = ""; String colName = "GRN_COMPLETED_DATETIME";
		String miDtlId = "0"; String type = "GRN";

		try {
			// INSERT OF GRN_HDR
			EnquiryCodeWithId grnHdrRes = iGrnDAO.insertGrnHdr(grnHdrAndDtl.getCreatedBy(), grnHdrAndDtl.getGrnDate(),
					miId, grnHdrAndDtl.getTenantId(), invLocation,grnHdrAndDtl.getPoId());

			// INSERT OF GRN_DTL
			if (grnHdrRes.getId() > 0) {
				for (GrnDtlInsertReq grnDtlObj : grnHdrAndDtl.getGrnDtlList()) {
					
				String productId =
				grnDtlObj.getProductId() == null ?  
				   iGrnDAO.getProductIdByProductCode(miDtlId,"",grnDtlObj.getTenantId()) : grnDtlObj.getProductId() ;
				
				iGrnDAO.updateDcDtlBin(grnDtlObj.getDcDtlId(), grnDtlObj.getBin(), grnDtlObj.getProductId());
					grnDtlRes = iGrnDAO.insertGrnDtl(grnHdrRes.getId(), miDtlId, grnDtlObj.getRecivedQty(),
							grnDtlObj.getTenantId(), grnDtlObj.getPoDtlId(), grnDtlObj.getIndentDtlId(),
							grnDtlObj.getPmHdrId(), grnDtlObj.getProductCode(), invLocation, grnDtlObj.getCreatedBy(),
							grnHdrRes.getEnquiryCode(),productId);
					// InCreasing dcDtl receivedQty
					if(grnDtlRes > 0) {
					increaseReceivedQty = iGrnDAO.updateDcDtlReceivedQty(grnDtlObj.getDcDtlId(),
							grnDtlObj.getRecivedQty());
					
					}
					
//					if(grnDtlRes > 0 && !grnDtlObj.getIndentDtlId().equalsIgnoreCase("")) 
					if(grnDtlRes > 0 && grnDtlObj.getIndentDtlId() != null && !grnDtlObj.getIndentDtlId().trim().isEmpty()) {
					    UpdatedDateTime = indentGroupDAO.getLastUpdatedDateTime(grnDtlObj.getIndentDtlId(), grnDtlObj.getTenantId(), type);
				            if(!UpdatedDateTime.equalsIgnoreCase("")) {
					           indentGroupDAO.updateLastUpdatedDateTime(grnDtlObj.getIndentDtlId(), colName, UpdatedDateTime, grnDtlObj.getTenantId());
				            }else {
				            	indentGroupDAO.reUpdatedDateTimeIndentDtl(grnDtlObj.getIndentDtlId(), colName, grnDtlObj.getTenantId());
				            }
					}
				}

			}
			if (grnHdrRes.getId() > 0 && grnDtlRes > 0 && increaseReceivedQty > 0) {

				rmsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				rmsg.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				rmsg.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}

		} catch (Exception ex) {
			logger.error("insertGrnHdrAndDtl Method Exception --->" + ex);

		}
		return rmsg;
	}

}
