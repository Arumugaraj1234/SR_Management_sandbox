package com.vmfg.general.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.entity.GetIndentDtlEntity;
import com.vmfg.design.request.TenantEmpRequest;
import com.vmfg.design.request.UpdateHdrRequest;
import com.vmfg.design.services.impl.IndentUploadService;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.dao.interfaces.INotificationGeneralDAO;
import com.vmfg.general.entity.ApprovalDesignationEntity;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.NotificationDtlsResponse;
import com.vmfg.general.entity.NotificationEventGroup;
import com.vmfg.general.entity.NotificationReqEntity;
import com.vmfg.general.request.DirectApprovalRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.interfaces.INotificationGeneralService;
import com.vmfg.project.dao.interfaces.IBudgetExcessSheetDAO;
import com.vmfg.project.service.impl.BudgetExcessSheetService;
import com.vmfg.quality.dao.interfaces.IQualityInspectionDAO;
import com.vmfg.quality.services.impl.QualityInspectionService;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.scm.services.impl.IndentGroupService;
import com.vmfg.scm.services.impl.PoService;
import com.vmfg.task.dao.impl.DesignTaskDAO;

@Service
public class NotificationGeneralService implements INotificationGeneralService {
	private static final Logger logger = LoggerFactory.getLogger(NotificationGeneralService.class);

	@Autowired
	INotificationGeneralDAO iNotificationGeneralDAO;

    @Autowired
    IndentUploadDAO indentUploadDAO;
    
    @Autowired
    DesignTaskDAO DesignTaskDAO;
    
    @Autowired
    StageManagementDAO StageManagementDAO;
    
    @Autowired
    UploadManagementDAO uploadManagementDAO;
    
    @Autowired
    IndentGroupDAO iIndentGroupDAO;
    
    @Autowired
    IBudgetExcessSheetDAO iBudgetExcessSheetDAO;
    
    @Autowired
    IQualityInspectionDAO iQualityInspectionDAO;
    
    @Autowired
    IndentUploadService indentUploadService;
    
    @Autowired
    IndentGroupService indentGroupService;
    
    @Autowired
    BudgetExcessSheetService budgetExcessSheetService;
    
    @Autowired
    PoService poService;
    
    @Autowired
    QualityInspectionService qualityInspectionService;
    
    @Autowired
    IPoDAO iPoDAO;
    
	@Override
	public ResponseAsList getNotificationDetails(TenantEmpRequest tenantEmpRequest) {
		ResponseAsList resp = new ResponseAsList();
		List<NotificationReqEntity> NotificationReqList = null;
		List<NotificationDtlsResponse> Notify=new ArrayList<NotificationDtlsResponse>();
		NotificationDtlsResponse ObjNotify=new NotificationDtlsResponse();
		
		try {
			String TENANT_ID = tenantEmpRequest.getTenantID();
			String empId=tenantEmpRequest.getEmpId();
			NotificationReqList=iNotificationGeneralDAO.getNotifyDtlList(TENANT_ID,empId);
			
			 Map<String, List<NotificationReqEntity>> groupedByEvent = NotificationReqList.stream()
		                .collect(Collectors.groupingBy(NotificationReqEntity::getEvent));

		        List<NotificationEventGroup> result = groupedByEvent.entrySet().stream()
		                .map(entry -> new NotificationEventGroup(entry.getKey(), entry.getValue()))
		                .collect(Collectors.toList());
		        int countVal=iNotificationGeneralDAO.getNotifyCount(TENANT_ID,empId);
		        ObjNotify.setCount(countVal);
		        ObjNotify.setList(result);
		        Notify.add(ObjNotify);
			if (result.size() > 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.success);
				resp.setResponseData(Notify);
			} else {
				resp.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				resp.setResponseMessage(ResponseMessageMap.noRecord);
				resp.setResponseData(Notify);
			}
			
		}catch(Exception ex) {
			logger.error("getNotificationDetails error "+ex);
		}
		return resp;
	}

	@Override
	public ResponseAsMessage updateNotificationDtls(TenantEmpRequest tenantEmpRequest) {
		logger.info("updateNotificationDtls  method start");
		ResponseAsMessage returnnMsg = null;
		try {
			String TENANT_ID = tenantEmpRequest.getTenantID();
			String EMP_ID = tenantEmpRequest.getEmpId();
			if ((!TENANT_ID.isEmpty()) &&  !EMP_ID.isEmpty()) {
			returnnMsg=iNotificationGeneralDAO.updateNotificationDetails(TENANT_ID,EMP_ID);
			}
			
		}catch(Exception ex) {
			logger.info("updateNotificationDtls  method Error "+ex);
		}
		return returnnMsg;
		
	}

	@Override
	public ResponseAsList getApprovalDesigDtls(TenantEmpRequest tenantEmpRequest) {
		ResponseAsList resp = new ResponseAsList();
		List<ApprovalDesignationEntity> resList=new ArrayList<>();

		try {
			String TENANT_ID = tenantEmpRequest.getTenantID();
			String EMP_ID = tenantEmpRequest.getEmpId();
			String empDesc=iNotificationGeneralDAO.getEmpDesinationCode(TENANT_ID,EMP_ID);
			resList=iNotificationGeneralDAO.getApprovalList(TENANT_ID,empDesc);
			
			List<ApprovalDesignationEntity> filterList = resList.stream()
					   .filter(item -> !item.getPmId().isEmpty())
					   .collect(Collectors.toList());
			resList = resList.stream()
					.filter(item -> item.getPmId().isEmpty())
					.collect(Collectors.toList());
			
			if(filterList.size() > 0) {
				for(ApprovalDesignationEntity obj : filterList) {
		 			
					String tableName = iNotificationGeneralDAO.getTableNameforNotification("bgrn", obj.getPmId());
					if(!tableName.equalsIgnoreCase("NA")) {
//						if(obj.getProjectId().isEmpty()) {
//							resList.add(obj);
//						}else {
//							int rec = iNotificationGeneralDAO.getNotificationRec(tableName, obj.getProjectId(), obj.getPmId(),EMP_ID );
//							if(rec > 0) {
//								resList.add(obj);
//							}
//						}
						
//					}
					
					BigDecimal allocatedValue = BigDecimal.ZERO;
					int approveBtnEnable = 0; String availableValue = ""; String targetValue = "";
					String docGroup = ""; int currentSeq = 0; String qIHdrId = ""; String poId = "";
					ApprovalDesignationEntity resentity = new ApprovalDesignationEntity();
					List<GetIndentDtlEntity> lst = new ArrayList<GetIndentDtlEntity>();
					GetIndentDtlEntity getList = new GetIndentDtlEntity();
					List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
//					DocumentStatusMstEntity docList = new DocumentStatusMstEntity();
					List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
//					
					if(obj.getDocTypeCode().equalsIgnoreCase("DC018")) {

						 currentSeq = indentUploadDAO.getCurrentSeqByIndentId(obj.getRefId(),
									TENANT_ID);
						 if(currentSeq>0) {
						 allocatedValue = indentUploadDAO.getindentBudgetval(obj.getRefId());
						 availableValue = indentUploadDAO.getAvailableValue(obj.getRefId());
						 targetValue = indentUploadDAO.getTargetValue(obj.getRefId());
						 getList.setCostFlowType(indentUploadDAO.getCostFlowTypeByIndentId(obj.getRefId()));

						 docGroup = indentUploadDAO.getIndentTypeCodeByIndentId(obj.getRefId(),
								TENANT_ID);
						 }
					}else if(obj.getDocTypeCode().equalsIgnoreCase("DC038")) {
						
						currentSeq = indentUploadDAO.getCurIndentGrpSeq(obj.getRefId());
						if(currentSeq>0) {
						String vendorQualified= iIndentGroupDAO.getVendorQualified(obj.getRefId());
						
						String basicTotalCol=""; 
						if(!vendorQualified.equalsIgnoreCase("NA")) {
							if(vendorQualified.equalsIgnoreCase("L1")) {
								basicTotalCol="L1_FINAL_SUB_TOTAL";
							}else if(vendorQualified.equalsIgnoreCase("L2")) {
								basicTotalCol="L2_FINAL_SUB_TOTAL";
							}else  {
								basicTotalCol="L3_FINAL_SUB_TOTAL";
							}
							String getIgHdrId= iIndentGroupDAO.getIgHdrId(obj.getRefId(),TENANT_ID);
							String indentId = iIndentGroupDAO.getIndentIdByIgHdrId(getIgHdrId);
							 targetValue = indentUploadDAO.getTargetValue(indentId);
							 allocatedValue = new BigDecimal(iIndentGroupDAO.venDtlBasicCost(getIgHdrId,basicTotalCol));
							getList.setAllocatedValue(allocatedValue.toString());
							getList.setTargetValue(targetValue);
							getList.setCostFlowType(indentUploadDAO.getCostFlowTypeByIndentId(indentId));
						}else {
							getList.setAllocatedValue("0");
							getList.setTargetValue("0");
						}
						      if(obj.getIsInternal().equalsIgnoreCase("1")) {
							     docGroup = getScsDocGroup(getList.getAllocatedValue(),TENANT_ID, obj.getDocTypeCode(),"8");
						      }else {
							     docGroup = getScsDocGroup(getList.getAllocatedValue(),TENANT_ID, obj.getDocTypeCode(),"5");
						      }	
						}
					}else if(obj.getDocTypeCode().equalsIgnoreCase("DC039")) {

						currentSeq = iBudgetExcessSheetDAO.getCurrSeqForBudgetExcess(obj.getRefId());
						if(currentSeq>0) {
					    String excess = iBudgetExcessSheetDAO.getExcessValue(obj.getRefId());
					    if(obj.getIsInternal().equalsIgnoreCase("1")) {
					    	docGroup = getDocGroup(excess,TENANT_ID,"8");
					      }else {
					    	  docGroup = getDocGroup(excess,TENANT_ID,"3");
					      }	
						targetValue = iBudgetExcessSheetDAO.getTargetValue(obj.getRefId());
						allocatedValue = new BigDecimal(iBudgetExcessSheetDAO.getActualValue(obj.getRefId()));
						}
 
					}else if(obj.getDocTypeCode().equalsIgnoreCase("DC044")) {

						 currentSeq = indentUploadDAO.getCurrentSeqByPoId(obj.getRefId(), TENANT_ID);
						 if(currentSeq>0) {
						 int getIntentId = iPoDAO.retriveIndentId(obj.getRefId());
						 docGroup = indentUploadDAO.getIndentTypeCodeByIndentId(String.valueOf(getIntentId),
								TENANT_ID);
						 } 
					}
					else if(obj.getDocTypeCode().equalsIgnoreCase("DC068")) {

						currentSeq = Integer.valueOf(iQualityInspectionDAO.getCurrSeqForQInspection(obj.getRefId(),"1"));
						if(currentSeq>0) {
							if(obj.getProjectId() != null) {
							    poId = iQualityInspectionDAO.getPoId(obj.getProjectId(), obj.getRefId(), TENANT_ID);	
							}
							qIHdrId = iQualityInspectionDAO.getCurrSeqForQInspection(obj.getRefId(),"2");
							if(qIHdrId.equalsIgnoreCase("0")) {
								qIHdrId = null;
							}
						}
					}
		
						if(currentSeq>0 &&  obj.getDocTypeCode().equalsIgnoreCase("DC018") ||
								currentSeq>0 && obj.getDocTypeCode().equalsIgnoreCase("DC039")) {
							docLifeCycleMstList = DesignTaskDAO.getNextSeqandStatusByDoc(currentSeq, obj.getDocTypeCode(), TENANT_ID,docGroup);	
					
					     	currSeqDocLifeCycleMstList = StageManagementDAO.getDocDtlcurrentSeqByDocGrp(obj.getDocTypeCode(),
								String.valueOf(currentSeq), TENANT_ID,docGroup);
						
						if (docLifeCycleMstList.size() > 0 ) {

							String designCode = uploadManagementDAO.getDesigCodeByEmpId(EMP_ID,
									TENANT_ID);

							approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, docGroup,
									TENANT_ID, obj.getDocTypeCode(), docLifeCycleMstList.get(0).getCurrSequence());
							if (approveBtnEnable == 1) {
								// curr seq
								docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO
										.getDocTypeDescByDocType(docLifeCycleMstList.get(0).getDocType(), TENANT_ID));
								docLifeCycleMstList.get(0)
										.setDocStatusDesc(DesignTaskDAO.getStatusByDesc(
												indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
														docLifeCycleMstList.get(0).getCurrSequence(), TENANT_ID, obj.getDocTypeCode(), docGroup),
												TENANT_ID));// Current seq docStatus

								// Previous Seq
								docLifeCycleMstList.get(0).setPreviousSeq(String.valueOf(currentSeq));
								docLifeCycleMstList.get(0).setPreviousSeqStatusCode(indentUploadDAO
										.getStatusCodebySeqAndDocTypeByDocGrp(String.valueOf(currentSeq), TENANT_ID, obj.getDocTypeCode(), docGroup));

								docLifeCycleMstList.get(0).setPreviousSeqStatusDesc(
										DesignTaskDAO.getStatusByDesc(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												String.valueOf(currentSeq), TENANT_ID, obj.getDocTypeCode(), docGroup), TENANT_ID));

								if (docLifeCycleMstList.get(0).getNextSeq() != null
										&& !docLifeCycleMstList.get(0).getNextSeq().equalsIgnoreCase("")) {
									docLifeCycleMstList.get(0)
											.setNextSeqStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
													docLifeCycleMstList.get(0).getNextSeq(), TENANT_ID, obj.getDocTypeCode(), docGroup));

									docLifeCycleMstList.get(0).setNextSeqStatusDesc(DesignTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
													String.valueOf(docLifeCycleMstList.get(0).getNextSeq()), TENANT_ID, obj.getDocTypeCode(), docGroup),
											TENANT_ID));
								}

								// cancel seq
								if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
									docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
									docLifeCycleMstList.get(0)
											.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
													currSeqDocLifeCycleMstList.get(0).getCancelSeq(), TENANT_ID, obj.getDocTypeCode(), docGroup));

									docLifeCycleMstList.get(0).setCancelStatusDesc(DesignTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
													currSeqDocLifeCycleMstList.get(0).getCancelSeq(), TENANT_ID, obj.getDocTypeCode(), docGroup),
											TENANT_ID));
								}

								getList.setDocLifeCycleMstList(docLifeCycleMstList);
							}
						
						}
						
						if(obj.getProjectId().isEmpty()) {
						    getList.setApproveBtnEnable(approveBtnEnable);
//						    getList.setPoId(null);
							getList.setAllocatedValue(allocatedValue.toString());
							getList.setAvilablevalue(availableValue);
							getList.setTargetValue(targetValue);
							getList.setSeq(String.valueOf(currentSeq));
							lst.add(getList);
							resentity.setIndentDtl(lst);
							resentity.setDnAppId(obj.getDnAppId());
							resentity.setDnDtlId(obj.getDnDtlId());
							resentity.setRefId(obj.getRefId());
	                        resentity.setRefCode(obj.getRefCode());
	                        resentity.setDocTypeCode(obj.getDocTypeCode());
	                        resentity.setDocTypeDesc(obj.getDocTypeDesc());
	                        resentity.setPmId(obj.getPmId());
	                        resentity.setProjectCode(obj.getProjectCode());
	                        resentity.setProjectId(obj.getProjectId());
	                        resentity.setProjectName(obj.getProjectName());
	                        resentity.setCustomerName(obj.getCustomerName());
	                        resentity.setInsertedDate(obj.getInsertedDate());
	                        resentity.setIsInternal(obj.getIsInternal());
	                        resentity.setMstId("0");
	                        resentity.setEnquiryId(obj.getEnquiryId());
	                        resentity.setEnquiryCode(obj.getEnquiryCode());
							resList.add(resentity);	
//							resList.add(obj);
						}else {
							int rec = iNotificationGeneralDAO.getNotificationRec(tableName, obj.getProjectId(), obj.getPmId(),EMP_ID, "1");
							if(rec > 0) {
								    getList.setApproveBtnEnable(approveBtnEnable);
									getList.setAllocatedValue(allocatedValue.toString());
									getList.setAvilablevalue(availableValue);
									getList.setTargetValue(targetValue);
									getList.setSeq(String.valueOf(currentSeq));
									getList.setPoId(poId);
									lst.add(getList);
									resentity.setIndentDtl(lst);
									resentity.setDnAppId(obj.getDnAppId());
									resentity.setDnDtlId(obj.getDnDtlId());
									resentity.setRefId(obj.getRefId());
			                        resentity.setRefCode(obj.getRefCode());
			                        resentity.setDocTypeCode(obj.getDocTypeCode());
			                        resentity.setDocTypeDesc(obj.getDocTypeDesc());
			                        resentity.setPmId(obj.getPmId());
			                        resentity.setProjectCode(obj.getProjectCode());
			                        resentity.setProjectId(obj.getProjectId());
			                        resentity.setProjectName(obj.getProjectName());
			                        resentity.setMstId(String.valueOf(iNotificationGeneralDAO.getNotificationRec(tableName, obj.getProjectId(), obj.getPmId(),EMP_ID, "2" )));
			                        resentity.setCustomerName(obj.getCustomerName());
			                        resentity.setInsertedDate(obj.getInsertedDate());
			                        resentity.setEnquiryId(obj.getEnquiryId());
			                        resentity.setEnquiryCode(obj.getEnquiryCode());
			                        resentity.setIsInternal(obj.getIsInternal());
									resList.add(resentity);
//								resList.add(obj);
							}
						}		
					}else if(currentSeq>0 &&  obj.getDocTypeCode().equalsIgnoreCase("DC038")) {
						String processDoc ="";
						if(obj.getIsInternal().equalsIgnoreCase("1")) {
							processDoc = "8";
						}else{
							processDoc = "5";
						}
						docLifeCycleMstList = DesignTaskDAO.getNextSeqandStatusByDoc(currentSeq, obj.getDocTypeCode(), TENANT_ID,docGroup,processDoc);	
						
				     	currSeqDocLifeCycleMstList = StageManagementDAO.getDocDtlcurrentSeqByDocGrp(obj.getDocTypeCode(),
							String.valueOf(currentSeq), TENANT_ID,docGroup,processDoc);
					
					if (docLifeCycleMstList.size() > 0 ) {

						String designCode = uploadManagementDAO.getDesigCodeByEmpId(EMP_ID,
								TENANT_ID);

						approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, docGroup,processDoc,
								TENANT_ID, obj.getDocTypeCode(), docLifeCycleMstList.get(0).getCurrSequence());
						if (approveBtnEnable == 1) {
							// curr seq
							docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO
									.getDocTypeDescByDocType(docLifeCycleMstList.get(0).getDocType(), TENANT_ID));
							docLifeCycleMstList.get(0)
									.setDocStatusDesc(DesignTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
													docLifeCycleMstList.get(0).getCurrSequence(), TENANT_ID, obj.getDocTypeCode(), docGroup,processDoc),
											TENANT_ID));// Current seq docStatus

							// Previous Seq
							docLifeCycleMstList.get(0).setPreviousSeq(String.valueOf(currentSeq));
							docLifeCycleMstList.get(0).setPreviousSeqStatusCode(indentUploadDAO
									.getStatusCodebySeqAndDocTypeByDocGrp(String.valueOf(currentSeq), TENANT_ID, obj.getDocTypeCode(), docGroup,processDoc));

							docLifeCycleMstList.get(0).setPreviousSeqStatusDesc(
									DesignTaskDAO.getStatusByDesc(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
											String.valueOf(currentSeq), TENANT_ID, obj.getDocTypeCode(), docGroup,processDoc), TENANT_ID));

							if (docLifeCycleMstList.get(0).getNextSeq() != null
									&& !docLifeCycleMstList.get(0).getNextSeq().equalsIgnoreCase("")) {
								docLifeCycleMstList.get(0)
										.setNextSeqStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												docLifeCycleMstList.get(0).getNextSeq(), TENANT_ID, obj.getDocTypeCode(), docGroup,processDoc));

								docLifeCycleMstList.get(0).setNextSeqStatusDesc(DesignTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												String.valueOf(docLifeCycleMstList.get(0).getNextSeq()), TENANT_ID, obj.getDocTypeCode(), docGroup,processDoc),
										TENANT_ID));
							}

							// cancel seq
							if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
								docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
								docLifeCycleMstList.get(0)
										.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(), TENANT_ID, obj.getDocTypeCode(), docGroup,processDoc));

								docLifeCycleMstList.get(0).setCancelStatusDesc(DesignTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(), TENANT_ID, obj.getDocTypeCode(), docGroup,processDoc),
										TENANT_ID));
							}

							getList.setDocLifeCycleMstList(docLifeCycleMstList);
						}
					
					}
					
					if(obj.getProjectId().isEmpty()) {
					    getList.setApproveBtnEnable(approveBtnEnable);
//					    getList.setPoId(null);
						getList.setAllocatedValue(allocatedValue.toString());
						getList.setAvilablevalue(availableValue);
						getList.setTargetValue(targetValue);
						getList.setSeq(String.valueOf(currentSeq));
						lst.add(getList);
						resentity.setIndentDtl(lst);
						resentity.setDnAppId(obj.getDnAppId());
						resentity.setDnDtlId(obj.getDnDtlId());
						resentity.setRefId(obj.getRefId());
                        resentity.setRefCode(obj.getRefCode());
                        resentity.setDocTypeCode(obj.getDocTypeCode());
                        resentity.setDocTypeDesc(obj.getDocTypeDesc());
                        resentity.setPmId(obj.getPmId());
                        resentity.setProjectCode(obj.getProjectCode());
                        resentity.setProjectId(obj.getProjectId());
                        resentity.setProjectName(obj.getProjectName());
                        resentity.setCustomerName(obj.getCustomerName());
                        resentity.setInsertedDate(obj.getInsertedDate());
                        resentity.setIsInternal(obj.getIsInternal());
                        resentity.setMstId("0");
                        resentity.setEnquiryId(obj.getEnquiryId());
                        resentity.setEnquiryCode(obj.getEnquiryCode());
						resList.add(resentity);	
//						resList.add(obj);
					}else {
						int rec = iNotificationGeneralDAO.getNotificationRec(tableName, obj.getProjectId(), obj.getPmId(),EMP_ID, "1");
						if(rec > 0) {
							    getList.setApproveBtnEnable(approveBtnEnable);
								getList.setAllocatedValue(allocatedValue.toString());
								getList.setAvilablevalue(availableValue);
								getList.setTargetValue(targetValue);
								getList.setSeq(String.valueOf(currentSeq));
								getList.setPoId(poId);
								lst.add(getList);
								resentity.setIndentDtl(lst);
								resentity.setDnAppId(obj.getDnAppId());
								resentity.setDnDtlId(obj.getDnDtlId());
								resentity.setRefId(obj.getRefId());
		                        resentity.setRefCode(obj.getRefCode());
		                        resentity.setDocTypeCode(obj.getDocTypeCode());
		                        resentity.setDocTypeDesc(obj.getDocTypeDesc());
		                        resentity.setPmId(obj.getPmId());
		                        resentity.setProjectCode(obj.getProjectCode());
		                        resentity.setProjectId(obj.getProjectId());
		                        resentity.setProjectName(obj.getProjectName());
		                        resentity.setMstId(String.valueOf(iNotificationGeneralDAO.getNotificationRec(tableName, obj.getProjectId(), obj.getPmId(),EMP_ID, "2" )));
		                        resentity.setCustomerName(obj.getCustomerName());
		                        resentity.setInsertedDate(obj.getInsertedDate());
		                        resentity.setEnquiryId(obj.getEnquiryId());
		                        resentity.setEnquiryCode(obj.getEnquiryCode());
		                        resentity.setIsInternal(obj.getIsInternal());
								resList.add(resentity);
//							resList.add(obj);
						}
					}				
					}else if(currentSeq>0 && obj.getDocTypeCode().equalsIgnoreCase("DC044") || currentSeq>0 && obj.getDocTypeCode().equalsIgnoreCase("DC068")) {
						 currSeqDocLifeCycleMstList = StageManagementDAO.getDocDtlcurrentSeq(obj.getDocTypeCode(), String.valueOf(currentSeq), TENANT_ID);
							
							docLifeCycleMstList= DesignTaskDAO.getNextSeqandStatus(currentSeq,obj.getDocTypeCode(), TENANT_ID);
							if(docLifeCycleMstList.size()>0) {
								
								String designCode = uploadManagementDAO.getDesigCodeByEmpId(EMP_ID, TENANT_ID);
		
								approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, "default", TENANT_ID, obj.getDocTypeCode(), docLifeCycleMstList.get(0).getCurrSequence());
								
								if (approveBtnEnable == 1) {
									// curr seq
									docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO.getDocTypeDescByDocType(docLifeCycleMstList.get(0).getDocType(), TENANT_ID));
									docLifeCycleMstList.get(0)
									.setDocStatusDesc(DesignTaskDAO.getStatusByDesc(indentUploadDAO.getStatusCodebySeqAndDocType(
										docLifeCycleMstList.get(0).getCurrSequence(), TENANT_ID, obj.getDocTypeCode()), TENANT_ID));// Current seq docStatus
	
									// Previous Seq
									docLifeCycleMstList.get(0).setPreviousSeq(currSeqDocLifeCycleMstList.get(0).getCurrSequence());
									docLifeCycleMstList.get(0).setPreviousSeqStatusCode(indentUploadDAO
											.getStatusCodebySeqAndDocType(currSeqDocLifeCycleMstList.get(0).getCurrSequence(),  TENANT_ID, obj.getDocTypeCode()));
		
									docLifeCycleMstList.get(0).setPreviousSeqStatusDesc(
											DesignTaskDAO.getStatusByDesc(currSeqDocLifeCycleMstList.get(0).getDocStatus(), TENANT_ID));
								
									// cancel seq
									if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
										docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
										docLifeCycleMstList.get(0)
										.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(), TENANT_ID, obj.getDocTypeCode()));
		
										docLifeCycleMstList.get(0).setCancelStatusDesc(DesignTaskDAO.getStatusByDesc(
												indentUploadDAO.getStatusCodebySeqAndDocType(
														currSeqDocLifeCycleMstList.get(0).getCancelSeq(), TENANT_ID, obj.getDocTypeCode()), TENANT_ID));
									}
									
									getList.setDocLifeCycleMstList(docLifeCycleMstList);
								}
							}
							if(obj.getProjectId().isEmpty()) {
								getList.setSeq(String.valueOf(currentSeq));
								getList.setAllocatedValue("0");
								getList.setTargetValue("0");
								getList.setPoId(null);
								lst.add(getList);
								resentity.setIndentDtl(lst);
								resentity.setDnAppId(obj.getDnAppId());
								resentity.setDnDtlId(obj.getDnDtlId());
								resentity.setRefId(obj.getRefId() + " - " +qIHdrId);
		                        resentity.setRefCode(obj.getRefCode());
		                        resentity.setDocTypeCode(obj.getDocTypeCode());
		                        resentity.setDocTypeDesc(obj.getDocTypeDesc());
		                        resentity.setPmId(obj.getPmId());
		                        resentity.setProjectCode(obj.getProjectCode());
		                        resentity.setProjectId(obj.getProjectId());
		                        resentity.setProjectName(obj.getProjectName());
		                        resentity.setCustomerName(obj.getCustomerName());
		                        resentity.setInsertedDate(obj.getInsertedDate());
		                        resentity.setEnquiryId(obj.getEnquiryId());
		                        resentity.setEnquiryCode(obj.getEnquiryCode());
		                        resentity.setMstId("0");
		                        resentity.setIsInternal(obj.getIsInternal());
								resList.add(resentity);
//								resList.add(obj);
							}else {
								int rec = iNotificationGeneralDAO.getNotificationRec(tableName, obj.getProjectId(), obj.getPmId(),EMP_ID, "1" );
								if(rec > 0) {
									getList.setSeq(String.valueOf(currentSeq));
									getList.setAllocatedValue("0");
									getList.setTargetValue("0");
									getList.setPoId(poId);
									lst.add(getList);
									resentity.setIndentDtl(lst);
									resentity.setDnAppId(obj.getDnAppId());
									resentity.setDnDtlId(obj.getDnDtlId());
									resentity.setRefId(obj.getRefId() + " - " +qIHdrId);
			                        resentity.setRefCode(obj.getRefCode());
			                        resentity.setDocTypeCode(obj.getDocTypeCode());
			                        resentity.setDocTypeDesc(obj.getDocTypeDesc());
			                        resentity.setPmId(obj.getPmId());
			                        resentity.setProjectCode(obj.getProjectCode());
			                        resentity.setProjectId(obj.getProjectId());
			                        resentity.setProjectName(obj.getProjectName());
			                        resentity.setCustomerName(obj.getCustomerName());
			                        resentity.setInsertedDate(obj.getInsertedDate());
			                        resentity.setEnquiryId(obj.getEnquiryId());
			                        resentity.setEnquiryCode(obj.getEnquiryCode());
			                        resentity.setIsInternal(obj.getIsInternal());
			                        resentity.setMstId(String.valueOf(iNotificationGeneralDAO.getNotificationRec(tableName, obj.getProjectId(), obj.getPmId(),EMP_ID, "2")));
									resList.add(resentity);
//									resList.add(obj);
								}
							}
					}else {
						if(obj.getProjectId().isEmpty()) {
							resList.add(obj);
						}else {
							int rec = iNotificationGeneralDAO.getNotificationRec(tableName, obj.getProjectId(), obj.getPmId(),EMP_ID, "1" );
 				        	if(rec > 0) {
								resList.add(obj);
							}
						}
					}	
					}
				}	
			}

			if (resList.size() > 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.success);
				resp.setResponseData(resList);
			} else {
				resp.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				resp.setResponseMessage(ResponseMessageMap.noRecord);
				resp.setResponseData(resList);
			}
		}catch(Exception ex) {
			logger.info("getApprovalDesigDtls  method Error "+ex);

		}
		return resp;
	}
	
	private String getDocGroup(String excess, String tENANT_ID, String processCode) {
		String docGroup = "";
		try {
			BigDecimal exces=new BigDecimal(excess);
			List<String> docGroupList = iBudgetExcessSheetDAO.getDistinctDocGroup("DC039",tENANT_ID,processCode);
			if (docGroupList.size() > 0) {
				for (String docGrpVal : docGroupList) {
					if (exces.compareTo(new BigDecimal(docGrpVal)) <= 0) {
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

	public String getScsDocGroup(String val,String tenantId, String docType, String processDoc) {
		String docGroup = "";
		try {
			BigDecimal value=new BigDecimal(val);
			List<String> docGroupList = iIndentGroupDAO.getDistinctScsDocGroup(docType,tenantId,processDoc);
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
	public ResponseAsMessage getDirectApprovalDesignNotify(DirectApprovalRequest directApprovalReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();

		try {
			if(directApprovalReq.getDocType().equalsIgnoreCase("DC018")) {
				UpdateHdrRequest updateHdrReq= new UpdateHdrRequest();
				updateHdrReq.setCurrentseq(directApprovalReq.getCurrentseq());
				updateHdrReq.setDocType(directApprovalReq.getDocType());
				updateHdrReq.setEmpId(directApprovalReq.getEmpId());
				updateHdrReq.setIndentId(directApprovalReq.getHdrId());
				updateHdrReq.setMasterId(directApprovalReq.getMstId());
				updateHdrReq.setPmId(directApprovalReq.getPmId());
				updateHdrReq.setRemarks(directApprovalReq.getRemarks());
				updateHdrReq.setTenantId(directApprovalReq.getTenantId());
				
				returnMessage=indentUploadService.updateIndentHdrStatus(updateHdrReq);
				
			}else if(directApprovalReq.getDocType().equalsIgnoreCase("DC038")) {
				
				UpdateSeqAndStatusRequest updateHdrReq = new UpdateSeqAndStatusRequest();
				updateHdrReq.setCurrentseq(directApprovalReq.getCurrentseq());
				updateHdrReq.setDocTypeCode(directApprovalReq.getDocType());
				updateHdrReq.setEmpId(directApprovalReq.getEmpId());
				updateHdrReq.setHdrId(directApprovalReq.getHdrId());
				updateHdrReq.setMstId(directApprovalReq.getMstId());
				updateHdrReq.setPmId(directApprovalReq.getPmId());
				updateHdrReq.setRemarks(directApprovalReq.getRemarks());
				updateHdrReq.setEnquiryId(directApprovalReq.getEnquiryId());
				updateHdrReq.setPmHdrId(directApprovalReq.getPmHdrId());
				updateHdrReq.setTenantId(directApprovalReq.getTenantId());
				updateHdrReq.setDocGroup(directApprovalReq.getDocGroup());
				updateHdrReq.setProcessCode(directApprovalReq.getPmId());
				returnMessage = indentGroupService.updateScpSeqAndStatus(updateHdrReq);
				
			}else if (directApprovalReq.getDocType().equalsIgnoreCase("DC039")) {
				
				UpdateSeqAndStatusRequest updateHdrReq = new UpdateSeqAndStatusRequest();
				updateHdrReq.setCurrentseq(directApprovalReq.getCurrentseq());
				updateHdrReq.setDocTypeCode(directApprovalReq.getDocType());
				updateHdrReq.setEmpId(directApprovalReq.getEmpId());
				updateHdrReq.setHdrId(directApprovalReq.getHdrId());
				updateHdrReq.setMstId(directApprovalReq.getMstId());
				updateHdrReq.setPmId(directApprovalReq.getPmId());
				updateHdrReq.setRemarks(directApprovalReq.getRemarks());	
				updateHdrReq.setTenantId(directApprovalReq.getTenantId());
				updateHdrReq.setEnquiryId(directApprovalReq.getEnquiryId());
				updateHdrReq.setPmHdrId(directApprovalReq.getPmHdrId());
				updateHdrReq.setDocGroup(directApprovalReq.getDocGroup());
				
				returnMessage = budgetExcessSheetService.updateBudgetSheetExcessSeqAndStatus(updateHdrReq);
				
			}else if (directApprovalReq.getDocType().equalsIgnoreCase("DC044")) {
				UpdateSeqAndStatusRequest updateHdrReq = new UpdateSeqAndStatusRequest();
				updateHdrReq.setCurrentseq(directApprovalReq.getCurrentseq());
				updateHdrReq.setDocTypeCode(directApprovalReq.getDocType());
				updateHdrReq.setEmpId(directApprovalReq.getEmpId());
				updateHdrReq.setHdrId(directApprovalReq.getHdrId());
				updateHdrReq.setMstId(directApprovalReq.getMstId());
				updateHdrReq.setPmId(directApprovalReq.getPmId());
				updateHdrReq.setRemarks(directApprovalReq.getRemarks());
				updateHdrReq.setTenantId(directApprovalReq.getTenantId());
				updateHdrReq.setEnquiryId(directApprovalReq.getEnquiryId());
				updateHdrReq.setPmHdrId(directApprovalReq.getPmHdrId());
				updateHdrReq.setDocGroup(directApprovalReq.getDocGroup());
				
				returnMessage = poService.updatePoSeqAndStatus(updateHdrReq);
				
			}else if(directApprovalReq.getDocType().equalsIgnoreCase("DC068")){
				
				UpdateSeqAndStatusRequest updateHdrReq = new UpdateSeqAndStatusRequest(); 
				String qHdrId = directApprovalReq.getHdrId();
				String[] parts = qHdrId.split(" - ");
				qHdrId = parts[1];
				updateHdrReq.setCurrentseq(directApprovalReq.getCurrentseq());
				updateHdrReq.setDocTypeCode(directApprovalReq.getDocType());
				updateHdrReq.setEmpId(directApprovalReq.getEmpId());
				updateHdrReq.setHdrId(qHdrId);
				updateHdrReq.setMstId(directApprovalReq.getMstId());
				updateHdrReq.setPmId(directApprovalReq.getPmId());
				updateHdrReq.setRemarks(directApprovalReq.getRemarks());
				updateHdrReq.setTenantId(directApprovalReq.getTenantId());
				updateHdrReq.setEnquiryId(directApprovalReq.getEnquiryId());
				updateHdrReq.setPmHdrId(directApprovalReq.getPmHdrId());
				updateHdrReq.setDocGroup(directApprovalReq.getDocGroup());
				updateHdrReq.setPoId(directApprovalReq.getPoId());
				
				returnMessage = qualityInspectionService.updateQISeqAndStatus(updateHdrReq);
			}
		}catch(Exception ex) {
			logger.error("getDirectApprovalDesignNotify Error" + ex);	
		}
		return returnMessage;
	}
		
}
