package com.vmfg.finance.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.dao.interfaces.IIndentUploadDAO;
import com.vmfg.finance.dao.interfaces.IPraDAO;
import com.vmfg.finance.entity.GetPraDtlEntity;
import com.vmfg.finance.entity.GrnDtlsEntity;
import com.vmfg.finance.entity.PraDtlListEntity;
import com.vmfg.finance.entity.PraDtlsHistoryEntity;
import com.vmfg.finance.entity.PraStatusEntity;
import com.vmfg.finance.request.PraCancelRequest;
import com.vmfg.finance.request.PraInsertRequest;
import com.vmfg.finance.request.RetrievePraRequest;
import com.vmfg.finance.request.UdpatePraHdrSeqRequest;
import com.vmfg.finance.request.getPraDtlRequest;
import com.vmfg.finance.services.interfaces.IPraService;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.sales.dao.impl.EnquiryDAO;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.scm.dao.impl.PoDAO;
import com.vmfg.scm.entity.PoPaymentTermEntity;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.task.dao.interfaces.IDesignTaskDAO;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class PraService implements IPraService {
	private static final Logger logger = LoggerFactory.getLogger(PraService.class);
	@Autowired
	IPraDAO iPraDAO;
	
	@Autowired
	PoDAO poDao;

	@Autowired
	private UploadManagementDAO uploadManagementDAO;

	@Autowired
	DesignTaskDAO designTaskDAO;
	
	@Autowired
	StageManagementDAO stageManagementDAO;
	
	@Autowired
	IDesignTaskDAO iDesignTaskDAO;
	
	@Autowired
	IndentUploadDAO indentUploadDAO;
	
	@Autowired
	EnquiryDAO enquiryDao;
	
	@Autowired
	CommonNotifyMethod commonNotifyMethod;
	
	@Autowired
	private IIndentUploadDAO iIndentUploadDAO;
	
	@Autowired
	private IndentGroupDAO indentGroupDAO;

	@Override
	public ResponseAsMessage insertPRA(PraInsertRequest praInsertRequest) {
		ResponseAsMessage returnList = new ResponseAsMessage();
		String seq = "1";

		try {
			if(praInsertRequest.getPraId() != null && !praInsertRequest.getPraId().equalsIgnoreCase("")) {
				//update
				int updatePraHdr = iPraDAO.updatePraHdr(praInsertRequest.getInvoiceNumber(), praInsertRequest.getInvoiceDate(),praInsertRequest.getTransportValue(),praInsertRequest.getPfValue(),praInsertRequest.getInsuranceValue(),praInsertRequest.getOtherValue(),praInsertRequest.getRemarks(),praInsertRequest.getTds(),praInsertRequest.getAmountPayable(),praInsertRequest.getRetention(),praInsertRequest.getLd(),praInsertRequest.getOthers(), praInsertRequest.getPraId());
				List<String> indentDtlIds = iPraDAO.getIndentDtlId(praInsertRequest.getPraId(), praInsertRequest.getTenantId());
				String UpdatedDateTime = "";
				String type = "PRA";
				String colName = "PRA_COMPLETED_DATETIME";
				for (String indentDtl : indentDtlIds) {
					if (indentDtl != null && !indentDtl.equalsIgnoreCase("")) {
						String count = iPraDAO.getCountAsCompleted(indentDtl, praInsertRequest.getTenantId());
						if (count.equalsIgnoreCase("0")) {
							UpdatedDateTime = indentGroupDAO.getLastUpdatedDateTime(indentDtl, praInsertRequest.getTenantId(), type);
							if (!UpdatedDateTime.equalsIgnoreCase("") && !UpdatedDateTime.equalsIgnoreCase(null)) {
								indentGroupDAO.updateLastUpdatedDateTime(indentDtl, colName, UpdatedDateTime, praInsertRequest.getTenantId());
							} else {
								indentGroupDAO.reUpdatedDateTimeIndentDtl(indentDtl, colName, praInsertRequest.getTenantId());
							}
						}
					}
				} 
				  if(updatePraHdr>0) {
//					  iPraDAO.insertPraStatusDtl(praInsertRequest.getPraId(), seq, praInsertRequest.getStatusCode(), praInsertRequest.getTenantId(), praInsertRequest.getRemarks(), praInsertRequest.getEmpId());
					  returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
					  returnList.setResponseMessage(ResponseMessageMap.successUpdated);
				  }else {
					  returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
						returnList.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				  }
				
			}else {
				//insert
				if (praInsertRequest.getTenantId() == null || praInsertRequest.getTenantId().trim().isEmpty()) {
					returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					returnList.setResponseMessage("Session expired or invalid. Please log out and log back in, then try again.");
					return returnList;
				}
				int praHdrId =0;
				String lastPraCode = iPraDAO.getLastPraCode();
				String praCode = CommonMethod.genNewPraCode(lastPraCode);
			
				List<PoPaymentTermEntity> paymentTerm =	poDao.getPoPaymentTermListByPotId(praInsertRequest.getPotId());
				if(paymentTerm.size()>0) {
					List<DocumentStatusMstEntity> liststageManagement=		enquiryDao.getfirstSeqByDocType("DC077", praInsertRequest.getTenantId());
			//		List<ProcessConfigEntity> liststageManagement =		stageManagementDAO.getprocessDtlcurrentSeq(praInsertRequest.getPmId(), "1", praInsertRequest.getTenantId());
				if(liststageManagement.size()>0) {
					praInsertRequest.setStatus(liststageManagement.get(0).getCurrSequence() );
					praInsertRequest.setStatusCode(liststageManagement.get(0).getDocStatus());
					//BigDecimal invoiceVal = new BigDecimal(paymentTerm.get(0).getPercentage()).multiply(new BigDecimal(praInsertRequest.getOrderValue())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);	
				//praInsertRequest.setInvoiceValue(invoiceVal.toString());
					praInsertRequest.setPercentage(paymentTerm.get(0).getPercentage());
				praInsertRequest.setPaymentTerms(paymentTerm.get(0).getTerm());

				BigDecimal gst = new BigDecimal(praInsertRequest.getGst());
				BigDecimal iGst = new BigDecimal(praInsertRequest.getIgst());
				BigDecimal pf = new BigDecimal(praInsertRequest.getPfValue());
				BigDecimal transport = new BigDecimal(praInsertRequest.getTransportValue());
				BigDecimal insurance = new BigDecimal(praInsertRequest.getInsuranceValue());
				BigDecimal other = new BigDecimal(praInsertRequest.getOtherValue());
				BigDecimal invoice = new BigDecimal(praInsertRequest.getInvoiceValue());
				BigDecimal total = gst.add(pf).add(transport).add(insurance).add(other).add(invoice).add(iGst);
				String amountPayable= total.toPlainString();
				praInsertRequest.setAmountPayable(amountPayable);

				 praHdrId = iPraDAO.InsertPraHdr(praInsertRequest,praCode , praInsertRequest.getIsLast());
				 List<String> messageList = new ArrayList<>();
				 messageList.add(praCode);
				 
				 String nextApprDesig = iIndentUploadDAO.getIndentNxtAppDesc("DC077", "1", "default",praInsertRequest.getTenantId());
				 int mstId =iPraDAO.getMstId(praInsertRequest.getPmHdrId());
				 int projectCode =iPraDAO.getProjId(praInsertRequest.getPmHdrId());
				 messageList.add(String.valueOf(projectCode));
				 messageList.add(praInsertRequest.getEmpId());
				 messageList.add(praInsertRequest.getPoCode());

//					Add PRA status entry after 'PRA' created
					iPraDAO.insertPraStatusDtl(String.valueOf(praHdrId), seq, praInsertRequest.getStatusCode(), praInsertRequest.getTenantId(), "Created", praInsertRequest.getEmpId());
				commonNotifyMethod.InvokeNotificationMethod(1, 55, null,praInsertRequest.getTenantId() , messageList,new ArrayList<>() , "1", praInsertRequest.getProcessCode(), String.valueOf(praHdrId), nextApprDesig);
				commonNotifyMethod.InvokeApprovalDesigMethod(praInsertRequest.getProcessCode(),
						"DC077",  String.valueOf(praHdrId) , praInsertRequest.getPmHdrId(), praInsertRequest.getTenantId(),
						"", nextApprDesig ,praInsertRequest.getEnquiryId(),praCode);


				if(praInsertRequest.getGrnHdrId() != null && !praInsertRequest.getGrnHdrId().equalsIgnoreCase("")) {
				String grnDtlIds =iPraDAO.grnDtlList(praInsertRequest.getGrnHdrId(),praInsertRequest.getTenantId());
					if(grnDtlIds != null) {
						String[] dtlArr = grnDtlIds.split(",");
			        	for(int i=0;i<dtlArr.length;i++) {
				      	int PraDtl = iPraDAO.InsertPraDtl(dtlArr[i],praHdrId);
				      	String dtlId = String.valueOf(PraDtl);
				      	String praId = String.valueOf(praHdrId);
				        String indentDtl = iPraDAO.getDtlId(praId, dtlId);
					    String UpdatedDateTime = "";
					    String type = "PRA_REQUEST";
					    String colName = "PRA_REQUESTED_DATETIME";
					          if(!indentDtl.equalsIgnoreCase("") && indentDtl != null ) {
						      UpdatedDateTime = indentGroupDAO.getLastUpdatedDateTime(indentDtl, praInsertRequest.getTenantId(), type);
						            if(!UpdatedDateTime.equalsIgnoreCase("") && !UpdatedDateTime.equalsIgnoreCase(null)) {
							        indentGroupDAO.updateLastUpdatedDateTime(indentDtl, colName, UpdatedDateTime, praInsertRequest.getTenantId());
						            }else {
						            	indentGroupDAO.updateLastUpdatedDateTime(indentDtl, colName, CommonMethod.getCurrentDateTime(), praInsertRequest.getTenantId());
						            }
					          }
				        }
					}
				}
				}
			}
				if(praHdrId>0) {
					String paymentPending = iPraDAO.getPotPendingAmnt(praInsertRequest.getPotId());
					BigDecimal pending = new BigDecimal(paymentPending);
					BigDecimal invoiceAmnt = new BigDecimal(praInsertRequest.getInvoiceValue());
					pending = pending.subtract(invoiceAmnt);

					String pendAmt = pending.toString();

					iPraDAO.updatePotPendingAmnt(praInsertRequest.getPotId(),pendAmt);

					if(praInsertRequest.getIsLast().equalsIgnoreCase("1")){
					String paymentPendingAfterUpdt = iPraDAO.getPotPendingAmnt(praInsertRequest.getPotId());
						BigDecimal pendingAftrUpdt = new BigDecimal(paymentPendingAfterUpdt);
						if (pendingAftrUpdt.compareTo(BigDecimal.ZERO) > 0) {
							iPraDAO.updatePotPendingAmnt(praInsertRequest.getPotId(), BigDecimal.ZERO.toString());
							// |---> GST value in the pending amount will be removed as GST is handled separately in the PRA...
						}
					}


				}


				if(praHdrId>0) {
					returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.successInserted);
				}else {
					  returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
						returnList.setResponseMessage(ResponseMessageMap.failToCreateMsg);
				}
			}
			
		} catch (Exception ex) {
			logger.error("PraService Service Method Error " + ex);
			returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
			returnList.setResponseMessage("PRA creation failed: " + ex.getMessage());

		}
		return returnList;
	}

	@Override
	public ResponseAsMessage praCancel(PraCancelRequest praCancelRequest){
		ResponseAsMessage returnList = new ResponseAsMessage();
		String tenantid = praCancelRequest.getTenantId();

		try{
			String praId = praCancelRequest.getPraId();
//			List<String> praIds = iPraDAO.getPraIdsByPoId(praCancelRequest.getPoId(), tenantid);
//			for(String praId : praIds){
			iPraDAO.praCancel(praId, tenantid);
//			}
			returnList.setResponseMessage("PRA cancellation completed successfully.");
			returnList.setResponseCode("200");
		} catch (Exception e) {
			returnList.setResponseMessage("PRA cancellation failed: " + e.getMessage());
			returnList.setResponseCode("500");
			throw new RuntimeException(e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPraDtl(getPraDtlRequest getPraDtlReq) {
			ResponseAsList returnL = new ResponseAsList();
		try {
			List<GetPraDtlEntity> returnList = null;
			List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<>();
			List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<>();

			int approveBtnEnable = 0;
			String docGroup = "default";
			String employeeId = getPraDtlReq.getEmpId();
			
			returnList = iPraDAO.getPraDtlList(getPraDtlReq.getPraId(),getPraDtlReq.getTenantId());

				for(int i =0;i<returnList.size();i++) {
				List<PraDtlListEntity> praDtl = iPraDAO.getpraSubList(returnList.get(i).getPraId(), returnList.get(i).getTenantId());
				List<GrnDtlsEntity>grnlist=iPraDAO.getgrnlist(returnList.get(i).getGrnHdrId(),returnList.get(i).getTenantId());
				List<PraDtlsHistoryEntity> praList=iPraDAO.getprahistory(getPraDtlReq.getPraId(),getPraDtlReq.getTenantId());
				List<PraStatusEntity> praStatusEntities = iPraDAO.getPraStatusList(returnList.get(i).getPraId());

				//Retrieval of Po Cost Type Description in the PRA detail view
				String pocostTypeDesc = iPraDAO.getPoCostTypeDesc(getPraDtlReq.getPraId(), getPraDtlReq.getTenantId());
				returnList.get(i).setPoCostType(pocostTypeDesc);

				returnList.get(i).setPraStatusList(praStatusEntities);
				returnList.get(i).setGetPraDtl(praList);
				returnList.get(i).setGrnDtl(grnlist);
				returnList.get(i).setDtl(praDtl);

				//Document Life cycle Mst logic
					String currSeq=returnList.get(i).getStatus();
//					mainList.get(i).setSeqStatusDesc(designTaskDAO.getStatusByDesc(mainList.get(i).getSeqStatus(), idAndTenantIdReq.getTenantId()));

					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp("DC077", currSeq,
							getPraDtlReq.getTenantId(), docGroup);
					docLifeCycleMstList = designTaskDAO.getNextSeqandStatusByDoc(Integer.parseInt(currSeq), "DC077",
							getPraDtlReq.getTenantId(), docGroup);
					if(docLifeCycleMstList != null && docLifeCycleMstList.size()>0) {

						String designCode = uploadManagementDAO.getDesigCodeByEmpId(employeeId,
								getPraDtlReq.getTenantId());

						approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, docGroup ,
								getPraDtlReq.getTenantId(), "DC077", docLifeCycleMstList.get(0).getCurrSequence());

						if (approveBtnEnable == 1) {
							// curr seq
							docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO.getDocTypeDescByDocType(
									docLifeCycleMstList.get(0).getDocType(), getPraDtlReq.getTenantId()));
							docLifeCycleMstList.get(0)
									.setDocStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
													docLifeCycleMstList.get(0).getCurrSequence(),
													getPraDtlReq.getTenantId(), "DC077", docGroup),
											getPraDtlReq.getTenantId()));// Current seq docStatus

							// Previous Seq
							docLifeCycleMstList.get(0).setPreviousSeq(currSeq);
							docLifeCycleMstList.get(0)
									.setPreviousSeqStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(currSeq,
												getPraDtlReq.getTenantId(), "DC077", docGroup));

							docLifeCycleMstList.get(0)
									.setPreviousSeqStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(currSeq,
													getPraDtlReq.getTenantId(), "DC077", docGroup),
											getPraDtlReq.getTenantId()));

							// cancel seq
							if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
								docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
								docLifeCycleMstList.get(0)
										.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
												getPraDtlReq.getTenantId(), "DC077", docGroup));

								docLifeCycleMstList.get(0)
										.setCancelStatusDesc(designTaskDAO.getStatusByDesc(
												indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
														currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
														getPraDtlReq.getTenantId(), "DC077", docGroup),
												getPraDtlReq.getTenantId()));

							}else {
								docLifeCycleMstList.get(0).setCancelSeq(null);
							}

							returnList.get(i).setDocStatusMst(docLifeCycleMstList);
							returnList.get(i).setIsEditable(docLifeCycleMstList.get(0).getIsEditable()!=null && docLifeCycleMstList.get(0).getIsEditable()!=""? docLifeCycleMstList.get(0).getIsEditable():"0");
						}else {
							returnList.get(i).setIsEditable("0");
						}

					}else {
						returnList.get(i).setIsEditable("0");
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
			logger.error("error in getFinanceDtl service " + ex.getMessage());
		}

		return returnL;
	}

	@Override
	public ResponseAsList retrievePRA(RetrievePraRequest retrievePraReq) {
		ResponseAsList returnL = new ResponseAsList();
		try {
			List<GetPraDtlEntity> returnList = null;
			String poId = "";
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
			returnList = iPraDAO.getPraHdrListByPmHdrId(pmHdrId, poId, retrievePraReq.getTenantId());

				for(int i =0;i<returnList.size();i++) {
				List<PraDtlListEntity> praDtl = iPraDAO.getpraSubList(returnList.get(i).getPraId(), returnList.get(i).getTenantId());	
				returnList.get(i).setDtl(praDtl);
				returnList.get(i).setVerCheck(iPraDAO.getVerCheckByPraId(returnList.get(i).getPraId(),returnList.get(i).getTenantId()));
				
				List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq(retrievePraReq.getDocTypeCode(),returnList.get(i).getStatus(), returnList.get(i).getTenantId());
				List<DocumentStatusMstEntity> docLifeCycleMstList = iDesignTaskDAO.getNextSeqandStatus(
						Integer.parseInt(returnList.get(i).getStatus()),
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
//					docLifeCycleMstList.get(0).setIsEditable(currSeqDocLifeCycleMstList.get(0).getIsEditable());
					if (approveBtnEnable > 0) {
						returnList.get(i).setIsApproval("True");
					//	taskEntryDtlEntity.get(j).setDocStatusMst(docLifeCycleMstList);
					} else {
						returnList.get(i).setIsApproval("False");
			//			taskEntryDtlEntity.get(j).setDocStatusMst(docLifeCycleMstList);
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
			logger.error("error in retrievePRA service " + ex.getMessage());
		}

		return returnL;
	}

	@Override
	public ResponseAsMessage udpatePraHdrSeq(UdpatePraHdrSeqRequest updatePraHdrSeq) {
		ResponseAsMessage returnList = new ResponseAsMessage();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();

		try {
			String isLast  = "0";
			if(updatePraHdrSeq.getIslast() != null && !updatePraHdrSeq.getIslast().equalsIgnoreCase("")) {
					isLast = updatePraHdrSeq.getIslast();
			}
			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp("DC077",
					updatePraHdrSeq.getSeq(), updatePraHdrSeq.getTenantId(), "default");
				int updatePraHdr = iPraDAO.updatePraHdrSeq(updatePraHdrSeq.getSeq(),updatePraHdrSeq.getSeqDesc(),currSeqDocLifeCycleMstList.get(0).getLastSeq(),CommonMethod.getCurrentDateTime(),updatePraHdrSeq.getPraId());
				 List<String> messageList = new ArrayList<>();
				 messageList.add(updatePraHdrSeq.getPraCode());
//				 messageList.add(projCode);
				 
				 String nextApprDesig = iIndentUploadDAO.getIndentNxtAppDesc("DC077", updatePraHdrSeq.getSeq(), "default",updatePraHdrSeq.getTenantId());
				 int mstId =iPraDAO.getMstId(updatePraHdrSeq.getPmHdrId());
				 
				 int projectCode =iPraDAO.getProjId(updatePraHdrSeq.getPmHdrId());
				 messageList.add(updatePraHdrSeq.getPoNo());
				 messageList.add(String.valueOf(projectCode));

				  if(updatePraHdr>0) {
//					  if(currSeqDocLifeCycleMstList.get(0).getLastSeq().equals("0")){
//						  iPraDAO.insertPraStatusDtl(updatePraHdrSeq.getPraId(), updatePraHdrSeq.getSeq(), updatePraHdrSeq.getSeqDesc(), updatePraHdrSeq.getTenantId(), updatePraHdrSeq.getRemarks(), updatePraHdrSeq.getEmpId());
//					  }
					  iPraDAO.insertPraStatusDtl(updatePraHdrSeq.getPraId(), updatePraHdrSeq.getSeq(), updatePraHdrSeq.getSeqDesc(), updatePraHdrSeq.getTenantId(), updatePraHdrSeq.getRemarks(), updatePraHdrSeq.getEmpId());
//					  if(updatePraHdrSeq.getIsPrevStage().equals("1")){
//						  commonNotifyMethod.InvokeNotificationMethod(1, 56, null,updatePraHdrSeq.getTenantId() , messageList,new ArrayList<>() , "1", updatePraHdrSeq.getProcessCode(), String.valueOf(mstId), nextApprDesig);
//						  commonNotifyMethod.InvokeApprovalDesigMethod(updatePraHdrSeq.getProcessCode(),
//								  "DC077",  String.valueOf(mstId) , updatePraHdrSeq.getPmHdrId(), updatePraHdrSeq.getTenantId(),
//								  "", nextApprDesig ,updatePraHdrSeq.getEnqId(),updatePraHdrSeq.getPraCode());
//					  }
					  commonNotifyMethod.InvokeNotificationMethod(1, 56, null,updatePraHdrSeq.getTenantId() , messageList,new ArrayList<>() , "1", updatePraHdrSeq.getProcessCode(), String.valueOf(mstId), nextApprDesig);
//						commonNotifyMethod.InvokeApprovalDesigMethod(updatePraHdrSeq.getProcessCode(),
//								"DC077",  String.valueOf(mstId) , updatePraHdrSeq.getPmHdrId(), updatePraHdrSeq.getTenantId(),
//								"", nextApprDesig ,updatePraHdrSeq.getEnqId(),updatePraHdrSeq.getPraCode());
					  commonNotifyMethod.InvokeApprovalDesigMethod(updatePraHdrSeq.getProcessCode(),
							  "DC077", updatePraHdrSeq.getPraId(), updatePraHdrSeq.getPmHdrId(), updatePraHdrSeq.getTenantId(),
							  "", nextApprDesig ,updatePraHdrSeq.getEnqId(),updatePraHdrSeq.getPraCode());

					  returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successUpdated);

						String lastSeqFlag = currSeqDocLifeCycleMstList.get(0).getLastSeq();
						if (lastSeqFlag != null && lastSeqFlag.equalsIgnoreCase("1")) {
							try {
								List<String> praIndentDtlIds = iPraDAO.getIndentDtlId(updatePraHdrSeq.getPraId(), updatePraHdrSeq.getTenantId());
								for (String praIndentDtlId : praIndentDtlIds) {
									if (praIndentDtlId != null && !praIndentDtlId.isEmpty()) {
										indentGroupDAO.updateLastUpdatedDateTime(praIndentDtlId, "PRA_COMPLETED_DATETIME", CommonMethod.getCurrentDateTime(), updatePraHdrSeq.getTenantId());
									}
								}
							} catch (Exception praCompleteEx) {
								logger.error("PraService udpatePraHdrSeq - PRA_COMPLETED_DATETIME update failed: " + praCompleteEx);
							}
							try {
								String indentId = poDao.getIndentIdByPraId(updatePraHdrSeq.getPraId(), updatePraHdrSeq.getTenantId());
								if (indentId != null && !indentId.isEmpty()) {
									int uncoveredItems = poDao.getUncoveredNonInventoryCount(indentId, updatePraHdrSeq.getTenantId());
									int incompletePras = poDao.getUncompletedPraCountByIndentId(indentId, updatePraHdrSeq.getTenantId());
									if (uncoveredItems == 0 && incompletePras == 0) {
										String closeSeq = poDao.getTenantPropertyVal(updatePraHdrSeq.getTenantId(), "INDENT_CLOSE_SEQ");
										List<DocumentStatusMstEntity> closeSeqList = stageManagementDAO.getDocDtlcurrentSeq("DC018", closeSeq, updatePraHdrSeq.getTenantId());
										if (closeSeqList != null && closeSeqList.size() > 0) {
											poDao.updateIntentHdrSeqAndStatus("", closeSeqList.get(0).getCurrSequence(),
													closeSeqList.get(0).getDocStatus(), Integer.parseInt(indentId), "1");
											poDao.updateindentClose(indentId);
										}
									}
								}
							} catch (Exception indentCloseEx) {
								logger.error("PraService udpatePraHdrSeq - indent close failed: " + indentCloseEx);
							}
						}
				  }else {
					  returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
						returnList.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				  }


		} catch (Exception ex) {
			logger.error("PraService Service Method Error " + ex);
		}
		return returnList;
	}

}
