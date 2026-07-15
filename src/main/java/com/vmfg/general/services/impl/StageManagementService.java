package com.vmfg.general.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.ChangeRequestDAO;
import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.DepartmentAndEmployeeDAO;
import com.vmfg.general.dao.interfaces.IStageManagementDAO;
import com.vmfg.general.entity.CancelSeqEntity;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.DocumentTypeMstEntity;
import com.vmfg.general.entity.EmployeeForDepartmentEntity;
import com.vmfg.general.entity.GetComponentDtls;
import com.vmfg.general.entity.GetstageprocessDtlEntity;
import com.vmfg.general.entity.ProcessConfigEntity;
import com.vmfg.general.entity.ProjectDueDateEntity;
import com.vmfg.general.entity.ProjectWbsInitiationMst;
import com.vmfg.general.entity.StatusDtlEntity;
import com.vmfg.general.request.GenerateProjCodeReq;
import com.vmfg.general.request.GetUpdateProcessDtlRequest;
import com.vmfg.general.request.GetprocessEnbleStatusRequest;
import com.vmfg.general.request.GetstageprocessDtlRequest;
import com.vmfg.general.request.InitiateProcessRequest;
import com.vmfg.general.request.UpdateDueDateRequest;
import com.vmfg.general.response.GetprocessEnbleStatusResponse;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.interfaces.IStageManagementService;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.sales.dao.interfaces.IEnquiryDAO;
import com.vmfg.sales.dao.interfaces.IUploadManagementDAO;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class StageManagementService implements IStageManagementService {
	private static final Logger logger = LoggerFactory.getLogger(StageManagementService.class);

	@Autowired
	IStageManagementDAO iStageManagementDAO;

	@Autowired
	IEnquiryDAO iEnquiryDAO;

	@Autowired
	IndentUploadDAO indentUploadDAO;

	@Autowired
	DepartmentAndEmployeeDAO departmentAndEmployeeDAO;
	
	@Autowired
	CommonNotifyMethod commonNotifyMethod;
	
	@Autowired
	ChangeRequestDAO changeRequestDAO;
	
	@Autowired
	IUploadManagementDAO iUploadManagementDAO;

	@Override
	public ResponseAsList getstageprocessDtl(GetstageprocessDtlRequest getstageprocessDtlRequest) {
		ResponseAsList list = new ResponseAsList();
		List<GetstageprocessDtlEntity> processConfig = new ArrayList<GetstageprocessDtlEntity>();
		List<GetstageprocessDtlEntity> processAllConfig = new ArrayList<GetstageprocessDtlEntity>();
		List<GetstageprocessDtlEntity> processNotAllConfig = new ArrayList<GetstageprocessDtlEntity>();
		try {
			String referenceId = getstageprocessDtlRequest.getReferenceId();
			String processDoc = getstageprocessDtlRequest.getProcessDoc();
			String tenantId = getstageprocessDtlRequest.getTenantId();
			String currentStage = "";
			String mstTable = "";
			String refTable = "";
			mstTable = iStageManagementDAO.getDistinctMstTableNameDtlId(processDoc, tenantId);

			if (!mstTable.equalsIgnoreCase("")) {
				String mstColumnId = iStageManagementDAO.getDistinctMstTableId(processDoc, tenantId);
				if (!mstColumnId.equalsIgnoreCase("")) {
					currentStage = iStageManagementDAO.getcurrentMstTblStage(referenceId, tenantId, mstTable,
							mstColumnId);

					if (!currentStage.equalsIgnoreCase("")) {
						int seq = iStageManagementDAO.getPreStgValCheck(processDoc, currentStage,tenantId );
						int visibleSeq = Integer.parseInt(currentStage);
						if (seq > 0) {
							processNotAllConfig = iStageManagementDAO.getVisibleAllprocessDtl(processDoc, "0",
									tenantId);
							if (processNotAllConfig.size() > 0) {
								for (int i = 0; i < processNotAllConfig.size(); i++) {
									if (processNotAllConfig.get(i).getSeq() >= Integer.parseInt(currentStage)) {
										if (processNotAllConfig.get(i).getPreviousStgEdit().equalsIgnoreCase("1")) {
											visibleSeq = processNotAllConfig.get(i).getSeq();
										} else {
											i = processNotAllConfig.size();
										}
									}
								}
							}
						}
						processConfig = iStageManagementDAO.getprocessDtlBySeq(processDoc, Integer.toString(visibleSeq),
								"0", tenantId);
						if (processConfig.size() > 0) {
							processConfig.get(processConfig.size() - 1).setIsdefault("True");

						}
					}
				}
			}

			processAllConfig = iStageManagementDAO.getVisibleAllprocessDtl(processDoc, "1", tenantId);
			processConfig.addAll(processAllConfig);
			for (int i = 0; i < processConfig.size(); i++) {
				List<DocumentTypeMstEntity> documentTypeMst = iStageManagementDAO.getDocTypeMstDtlByStage(
						processConfig.get(i).getStgCode(), processConfig.get(i).getProcessCode(), tenantId);
				String slaveId = "";

				if (documentTypeMst.size() > 0) {
					refTable = iStageManagementDAO.getRefTableNameByDocTyp(documentTypeMst.get(0).getDocTypeCode(),
							documentTypeMst.get(0).getTenantId());
					if (refTable != null && !refTable.equalsIgnoreCase("")) {
						String currSeq = iStageManagementDAO.getcurrentEnquiryStatus(referenceId,
								processConfig.get(i).getTenantId(), refTable);
						if (!currSeq.equalsIgnoreCase("")) {
							int isEditable = iStageManagementDAO.getisEditableStatus(currSeq,
									documentTypeMst.get(0).getDocTypeCode(), processConfig.get(i).getProcessCode(),tenantId);
							if (isEditable > 0) {
								processConfig.get(i).setIsEditable("1");
							} else {
								processConfig.get(i).setIsEditable("0");
							}
						}
					} else {
						processConfig.get(i).setIsEditable("1");
					}
					processConfig.get(i).setDocTypeCode(documentTypeMst.get(0).getDocTypeCode());
					if (documentTypeMst.get(0).getRefSlaveId() != null
							&& !documentTypeMst.get(0).getRefSlaveId().equalsIgnoreCase("")) {
						slaveId = iStageManagementDAO.getcurrentStageSeqMstTable(referenceId,
								documentTypeMst.get(0).getRefTableName(), documentTypeMst.get(0).getRefSlaveId());
					}
				}
				processConfig.get(i).setSlaveId(slaveId);
				processConfig.get(i).setMstId(referenceId);

			}
			list.setResponseCode(ResponseMessageMap.responseCodeOk);
			list.setResponseMessage(ResponseMessageMap.success);
			list.setResponseData(processConfig.stream().sorted(Comparator.comparing(GetstageprocessDtlEntity::getSeq))
					.distinct().collect(Collectors.toList()));
		} catch (Exception ex) {
			logger.error("getstageprocessDtl error " + ex);
		}

		return list;
	}

	@Override
	public ResponseAsList getprocessEnbleStatus(GetprocessEnbleStatusRequest getprocessEnbleStatusReq) {
		ResponseAsList list = new ResponseAsList();
		List<GetprocessEnbleStatusResponse> getprocessEnbleStatusRespList = new ArrayList<GetprocessEnbleStatusResponse>();
		GetprocessEnbleStatusResponse getprocessEnbleStatusResp = new GetprocessEnbleStatusResponse();

		try {
			String currentStatus = "";
			String slaveId = getprocessEnbleStatusReq.getSlaveId();
			String referenceDocType = getprocessEnbleStatusReq.getReferenceDocType();
			String tenantId = getprocessEnbleStatusReq.getTenantId();
			String empId = getprocessEnbleStatusReq.getEmpId();
		//	String referenceId = getprocessEnbleStatusReq.getReferenceId();
			List<DocumentTypeMstEntity> refTableName = iStageManagementDAO.getDocTypeMstByDoc(referenceDocType);
			List<DocumentStatusMstEntity> currSeqList = new ArrayList<DocumentStatusMstEntity>();
			CancelSeqEntity cancelSeq = new CancelSeqEntity();

			if (refTableName.get(0).getRefTableName() != null && !refTableName.get(0).getRefTableName().equalsIgnoreCase("")) {

				// if ( refTableName.get(0).getRefTableName() != null &&
				// !refTableName.get(0).getRefTableName().equalsIgnoreCase("")) {
				currentStatus = iStageManagementDAO.getcurrentMstTblStatus(slaveId, tenantId,
						refTableName.get(0).getRefTableName(), refTableName.get(0).getRefSlaveId());

				List<DocumentStatusMstEntity> nextSeqBatchList = new ArrayList<DocumentStatusMstEntity>();
				if (!currentStatus.equalsIgnoreCase("")) {
					List<DocumentStatusMstEntity> documentcurrentseq = iStageManagementDAO
							.getDocDtlcurrentSeq(referenceDocType, currentStatus, tenantId);
					if (documentcurrentseq.size() > 0) {
						currSeqList = iStageManagementDAO.getDocCurrentSeqDtl(referenceDocType,
								documentcurrentseq.get(0).getCurrSequence(), tenantId);
						if (currSeqList.size() > 0) {
							cancelSeq.setCurrStatusDesc(currSeqList.get(0).getDocStatusDesc());
						}
						String empDesignation = iStageManagementDAO.getEmpDesinationCode(empId, tenantId);
						if (!empDesignation.equalsIgnoreCase("")) {
							// int empApproveCheck = iStageManagementDAO.empApproveCheck(empDesignation,
							// referenceDocType,
							// documentcurrentseq.get(0).getCurrSequence(), tenantId);
							// if (empApproveCheck > 0) {

							nextSeqBatchList = iStageManagementDAO.getNextSeqbatchDtlByDesig(referenceDocType,
									documentcurrentseq.get(0).getNextSeq(), tenantId, empDesignation);
							// currSeqList.addAll(nextSeqBatchList);

							if (nextSeqBatchList.size() > 0) {
								if (nextSeqBatchList.get(0).getCancelSeq() != null
										&& !nextSeqBatchList.get(0).getCancelSeq().equalsIgnoreCase("")) {
									cancelSeq.setCancelSeq(nextSeqBatchList.get(0).getCancelSeq());
									List<DocumentStatusMstEntity> cancelcurrentseq = iStageManagementDAO
											.getDocDtlcurrentSeq(referenceDocType,
													nextSeqBatchList.get(0).getCancelSeq(), tenantId);
									if (cancelcurrentseq.size() > 0) {
										cancelSeq.setCancelDesc(cancelcurrentseq.get(0).getDocStatusDesc());
									}
								}
							}
							// }
						}
					}

				}
				List<StatusDtlEntity> getStatusDtl = iStageManagementDAO.getStatusDtl(referenceDocType, slaveId,
						tenantId, refTableName.get(0).getStatusTableName());
				getprocessEnbleStatusResp.setCancelseq(cancelSeq);
				getprocessEnbleStatusResp.setDocumentStatusMstlist(nextSeqBatchList);
				getprocessEnbleStatusResp.setStatusDtl(getStatusDtl);
				getprocessEnbleStatusRespList.add(getprocessEnbleStatusResp);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseData(getprocessEnbleStatusRespList);
				list.setResponseMessage(ResponseMessageMap.success);
			} else {
				getprocessEnbleStatusResp.setCancelseq(cancelSeq);
				getprocessEnbleStatusResp.setDocumentStatusMstlist(currSeqList);
				getprocessEnbleStatusRespList.add(getprocessEnbleStatusResp);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(getprocessEnbleStatusRespList);
				list.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getprocessEnbleStatus Error" + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage getUpdateProcessDtl(GetUpdateProcessDtlRequest getUpdateProcessDtlreq) {
		ResponseAsMessage resp = new ResponseAsMessage();
		int updateRecord = 0;
		try {
			String updateStatusCode = "";
			String referenceId =getUpdateProcessDtlreq.getReferenceId();
			String referenceDocType = getUpdateProcessDtlreq.getReferenceDoc();
			String tenantId = getUpdateProcessDtlreq.getTenantId();
			String updateseq = getUpdateProcessDtlreq.getUpdatedSeq();
			String lastseq = getUpdateProcessDtlreq.getLastSeq();
			String processCode = getUpdateProcessDtlreq.getProcessCode();
			String empId = getUpdateProcessDtlreq.getEmpId();
			String remarks = getUpdateProcessDtlreq.getRemarks();
			List<DocumentTypeMstEntity> documentTypeMst = iStageManagementDAO.getDocTypeMstDtl(referenceDocType,
					processCode);
			if (documentTypeMst.size() > 0) {
				String refTableName = documentTypeMst.get(0).getRefTableName();
				String statusTableName = documentTypeMst.get(0).getStatusTableName();
				String mstTableName = documentTypeMst.get(0).getMstTableName();

				if (!refTableName.equalsIgnoreCase("")) {
					updateStatusCode = iStageManagementDAO.getDocstsBydocseqAtype(referenceDocType, updateseq,
							tenantId);

					if (!updateStatusCode.equalsIgnoreCase("")) {
						// String currentStageSeq =
						// iStageManagementDAO.getcurrentEnquiryStage(referenceId, tenantId);
						// updateStageCode = iStageManagementDAO.getNextStgDtl(processCode,
						// currentStageSeq, tenantId);
						updateRecord = iStageManagementDAO.updateProcessDtlStsAndCode(referenceId, updateseq,
								updateStatusCode, refTableName, documentTypeMst.get(0).getRefSlaveId());
						if (!statusTableName.equalsIgnoreCase("")) {
							iStageManagementDAO.updateProcesStatusDtl(referenceId, updateseq, updateStatusCode,
									statusTableName, empId, referenceDocType, tenantId, remarks);

						}
					}
				}

				if (!mstTableName.equalsIgnoreCase("")) {
					String masterId = iStageManagementDAO.getMasterIdBySlaveId(referenceId, refTableName, tenantId,
							documentTypeMst.get(0).getRefSlaveId());
					if (!masterId.equalsIgnoreCase("")) {
						String currentStage = iStageManagementDAO.getcurrentEnquiryStageSeqMstTable(masterId, tenantId,
								mstTableName, documentTypeMst.get(0).getMstColumnName());
						List<ProcessConfigEntity> nextStageDtl = iStageManagementDAO
								.getNextprocessStaDtlBySeq(processCode, currentStage, "0", tenantId);
						iStageManagementDAO.updateProcessHdrStsAndCode(masterId, updateseq, updateStatusCode,
								mstTableName, documentTypeMst.get(0).getMstColumnName(),getUpdateProcessDtlreq.getEmpId());
                        int mstCompleted=iStageManagementDAO.getMstCompletedVal(referenceDocType, updateseq, tenantId);
                        if(mstCompleted==1) {
                        	iStageManagementDAO.updateMstTblIsCompleted(masterId, mstTableName, documentTypeMst.get(0).getMstColumnName());
                        }
						
						if (nextStageDtl.size() > 0) {
							String newstatusSeq = iStageManagementDAO.getProcessLifeCycleCurrSeq(processCode,
									nextStageDtl.get(0).getMasterDocStatus(), tenantId);
							if (!newstatusSeq.equalsIgnoreCase("")) {
								if (lastseq.equalsIgnoreCase("1")) {
									iStageManagementDAO.updateProcessHdrStgAndCode(masterId,
											nextStageDtl.get(0).getSeq(), nextStageDtl.get(0).getStgCode(),
											mstTableName, documentTypeMst.get(0).getMstColumnName(),getUpdateProcessDtlreq.getEmpId());
								}
							}
						}
					}
				}
			}
			if (updateRecord > 0) {
				String docStatDesc =changeRequestDAO.getStatusDescByStatusCode(updateStatusCode, tenantId);
				String enqCode =iStageManagementDAO.getprojectCode(getUpdateProcessDtlreq.getPmId(),referenceId);
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				if(getUpdateProcessDtlreq.getPmId().equalsIgnoreCase("1")) {
					messageList.add("Enquiry "+enqCode);
					messageList.add("Enquiry");
				}else {
					messageList.add("Project "+enqCode);
					messageList.add("Project");
				}
				messageList.add(docStatDesc);
				String nextApproveDesig=commonNotifyMethod.getNxtAppDesc(referenceDocType, updateseq,tenantId);
				String masterRefId =  iStageManagementDAO.getSaleEnqRefId(getUpdateProcessDtlreq.getReferenceId(),getUpdateProcessDtlreq.getTenantId());
				commonNotifyMethod.InvokeNotificationMethod(1, 8, null, tenantId, messageList, otherEmp, "1",getUpdateProcessDtlreq.getPmId(), masterRefId,null);
				commonNotifyMethod.InvokeApprovalDesigMethod(getUpdateProcessDtlreq.getPmId() , referenceDocType, masterRefId,null, getUpdateProcessDtlreq.getTenantId(),"", nextApproveDesig, referenceId,enqCode);
				
			
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.success);

			} else {
				resp.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				resp.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("GetUpdateProcessDtl Error " + ex);
		}
		return resp;

	}

	@Override
	public ResponseAsMessage initiateProcess(InitiateProcessRequest initiateProcessReq) {
		ResponseAsMessage resp = new ResponseAsMessage();
		
		try {
			int pmhdrIdFlag=0;
			List<ProjectWbsInitiationMst> PMdetails = null;
			if (initiateProcessReq.getDeptCode().isEmpty()) {
				String dept = iStageManagementDAO.getChildPMID(initiateProcessReq.getPmId(),
						initiateProcessReq.getTenantId());
				PMdetails = iStageManagementDAO.getPMFromPMID(dept, initiateProcessReq.getTenantId());
				pmhdrIdFlag=1;
			} else {
				PMdetails = iStageManagementDAO.getPMFromDept(initiateProcessReq);
			}
			final int getPmhdrIdFlag = pmhdrIdFlag;

			PMdetails.forEach(li -> {

				String checkValue = iStageManagementDAO.checkMasterInfo(li.getPmId(), initiateProcessReq.getRefId(),
						initiateProcessReq.getTenantId(),getPmhdrIdFlag);
				String mstId="";
				if (checkValue.equalsIgnoreCase("0")) {
					String Id = iStageManagementDAO.insertMasterInfo(li.getPmId(), initiateProcessReq.getRefId(),
							initiateProcessReq.getTenantId(), initiateProcessReq.getDueDate(),
							initiateProcessReq.getEmpID(), initiateProcessReq.getStartDate(),getPmhdrIdFlag);
					 mstId=Id;
//					updateStgstatusDtl(mstId, li.getPmId(), initiateProcessReq.getTenantId());
//					String empArr[] = li.getPrimaryDoc().split(",");
//					for (int m = 0; m < empArr.length; m++) {
//						List<EmployeeForDepartmentEntity> empList = departmentAndEmployeeDAO
//								.getEmployeeForDesignation(initiateProcessReq.getTenantId(), empArr[m]);
//						for (int p = 0; p < empList.size(); p++) {
//
//							iEnquiryDAO.insertProcessAssignDtl(mstId, empList.get(p).getEmployeeId(),
//									initiateProcessReq.getTenantId(), li.getPmId());
////							if(li.getPmId().equalsIgnoreCase("3")) {
////								List<String> messageList = new ArrayList<>();
////								List<String> otherEmp = new ArrayList<>();
////								String enqCode =iEnquiryDAO.getsaleEnquiryCode(initiateProcessReq.getRefId());
////								messageList.add("Enquiry "+enqCode);
////								commonNotifyMethod.InvokeNotificationMethod(8, 9, empList.get(p).getEmployeeId(), initiateProcessReq.getTenantId(), messageList, otherEmp,null,initiateProcessReq.getPmId(), initiateProcessReq.getRefId(),null);
////								
////							}else {
////								List<String> messageList = new ArrayList<>();
////								List<String> otherEmp = new ArrayList<>();
////								String projCode =indentUploadDAO.getProjectCodeByProjId(initiateProcessReq.getRefId(),initiateProcessReq.getTenantId());
////								messageList.add("Project "+projCode);
////								commonNotifyMethod.InvokeNotificationMethod(8, 9, empList.get(p).getEmployeeId(), initiateProcessReq.getTenantId(), messageList, otherEmp, null,initiateProcessReq.getPmId(), mstId,null);
////								
////							}
//						}
//						
//					}
//					if(li.getPmId().equalsIgnoreCase("3")) {
//						List<String> messageList = new ArrayList<>();
//						List<String> otherEmp = new ArrayList<>();
//						String enqCode =iEnquiryDAO.getsaleEnquiryCode(initiateProcessReq.getRefId());
//						messageList.add("Enquiry "+enqCode);
//						String stgCode = iUploadManagementDAO.getCurrStageByRefId(initiateProcessReq.getRefId());
//						String currDocTypeCode = iUploadManagementDAO.getDocTypeCodeBystgAndpmId(stgCode, initiateProcessReq.getPmId());
//						commonNotifyMethod.InvokeNotificationMethod(2, 9, null, initiateProcessReq.getTenantId(), messageList, otherEmp, "1",initiateProcessReq.getPmId(), initiateProcessReq.getRefId(),null);
//						commonNotifyMethod.InvokeApprovalDesigMethod(initiateProcessReq.getPmId() , currDocTypeCode, initiateProcessReq.getRefId(),"", initiateProcessReq.getTenantId(), null,"", initiateProcessReq.getRefId(),enqCode);
//						
//					}else {
//						List<String> messageList = new ArrayList<>();
//						List<String> otherEmp = new ArrayList<>();
//						String projCode =indentUploadDAO.getProjectCodeByProjId(initiateProcessReq.getRefId(),initiateProcessReq.getTenantId());
//						messageList.add("Project "+projCode);
//						commonNotifyMethod.InvokeNotificationMethod(2, 9, null, initiateProcessReq.getTenantId(), messageList, otherEmp, "1",initiateProcessReq.getPmId(), mstId,null);
//						
//					}
						
					
				} else {
					String tableName ="";
					if(li.getPmId().equalsIgnoreCase("5")){
						tableName = "scm_hdr";
						iStageManagementDAO.udpateDueDate(tableName,initiateProcessReq.getStartDate(), initiateProcessReq.getDueDate(), initiateProcessReq.getTenantId(), initiateProcessReq.getRefId());
						
					}else if(li.getPmId().equalsIgnoreCase("2")){
						tableName ="design_hdr";
						iStageManagementDAO.udpatestartAndEndDate(tableName,initiateProcessReq.getStartDate(), initiateProcessReq.getDueDate(), initiateProcessReq.getTenantId(), initiateProcessReq.getRefId());
						
					}else if(li.getPmId().equalsIgnoreCase("4")){
						tableName ="assy_hdr";
						iStageManagementDAO.udpatestartAndEndDate(tableName,initiateProcessReq.getStartDate(), initiateProcessReq.getDueDate(), initiateProcessReq.getTenantId(), initiateProcessReq.getRefId());
						
					}
				
					resp.setResponseDataMessage(checkValue);
				}
                 mstId = (!mstId.isEmpty()) ? mstId : checkValue;
				updateStgstatusDtl(mstId, li.getPmId(), initiateProcessReq.getTenantId());
				String empArr[] = li.getPrimaryDoc().split(",");
				for (int m = 0; m < empArr.length; m++) {
					List<EmployeeForDepartmentEntity> empList = departmentAndEmployeeDAO
							.getEmployeeForDesignation(initiateProcessReq.getTenantId(), empArr[m]);
					for (int p = 0; p < empList.size(); p++) {

						iEnquiryDAO.insertProcessAssignDtl(mstId, empList.get(p).getEmployeeId(),
								initiateProcessReq.getTenantId(), li.getPmId());
//						if(li.getPmId().equalsIgnoreCase("3")) {
//							List<String> messageList = new ArrayList<>();
//							List<String> otherEmp = new ArrayList<>();
//							String enqCode =iEnquiryDAO.getsaleEnquiryCode(initiateProcessReq.getRefId());
//							messageList.add("Enquiry "+enqCode);
//							commonNotifyMethod.InvokeNotificationMethod(8, 9, empList.get(p).getEmployeeId(), initiateProcessReq.getTenantId(), messageList, otherEmp,null,initiateProcessReq.getPmId(), initiateProcessReq.getRefId(),null);
//							
//						}else {
//							List<String> messageList = new ArrayList<>();
//							List<String> otherEmp = new ArrayList<>();
//							String projCode =indentUploadDAO.getProjectCodeByProjId(initiateProcessReq.getRefId(),initiateProcessReq.getTenantId());
//							messageList.add("Project "+projCode);
//							commonNotifyMethod.InvokeNotificationMethod(8, 9, empList.get(p).getEmployeeId(), initiateProcessReq.getTenantId(), messageList, otherEmp, null,initiateProcessReq.getPmId(), mstId,null);
//							
//						}
					}
					
				}
				if(li.getPmId().equalsIgnoreCase("3")) {
					List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
					String enqCode =iEnquiryDAO.getsaleEnquiryCode(initiateProcessReq.getRefId());
					messageList.add("Enquiry "+enqCode);
					String stgCode = iUploadManagementDAO.getCurrStageByRefId(initiateProcessReq.getRefId());
					String currDocTypeCode = iUploadManagementDAO.getDocTypeCodeBystgAndpmId(stgCode, initiateProcessReq.getPmId());
					commonNotifyMethod.InvokeNotificationMethod(2, 9, null, initiateProcessReq.getTenantId(), messageList, otherEmp, "1",initiateProcessReq.getPmId(), initiateProcessReq.getRefId(),null);
					commonNotifyMethod.InvokeApprovalDesigMethod(initiateProcessReq.getPmId() , currDocTypeCode, initiateProcessReq.getRefId(),"", initiateProcessReq.getTenantId(), null,"", initiateProcessReq.getRefId(),enqCode);
					
				}else {
					List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
					String projCode =indentUploadDAO.getProjectCodeByProjId(initiateProcessReq.getRefId(),initiateProcessReq.getTenantId());
					messageList.add("Project "+projCode);
					commonNotifyMethod.InvokeNotificationMethod(2, 9, null, initiateProcessReq.getTenantId(), messageList, otherEmp, "1",initiateProcessReq.getPmId(), mstId,null);
					
				}
				resp.setResponseDataMessage(mstId);
			});
			
			resp.setResponseCode(ResponseMessageMap.responseCodeOk);
			resp.setResponseMessage(ResponseMessageMap.success);

		} catch (Exception ex) {
			logger.error("initiateProcess Error " + ex);
		}
		return resp;
	}

	public int updateStgstatusDtl(String masterId, String pmId, String tenantId) {
		int insert = 0;
		try {
			List<DocumentTypeMstEntity> doctypeMst = iEnquiryDAO.getStgDocDtl(pmId, tenantId);
			for (int p = 0; p < doctypeMst.size(); p++) {
				if (doctypeMst.get(p).getRefTableName() != null
						&& !doctypeMst.get(p).getRefTableName().equalsIgnoreCase("")) {
					List<DocumentStatusMstEntity> DocStatusDtl = iEnquiryDAO
							.getfirstSeqBypmIdDocType(doctypeMst.get(p).getDocTypeCode(), pmId, tenantId);
					if (DocStatusDtl.size() > 0) {
						insert = iEnquiryDAO.insertStgDtl(masterId, DocStatusDtl.get(0).getDocStatus(),
								DocStatusDtl.get(0).getCurrSequence(), tenantId, doctypeMst.get(p).getRefTableName());
					}
				}
			}
		} catch (Exception ex) {
			logger.error("updateStgstatusDtl Error " + ex);
		}
		return insert;
	}

	@Override
	public ResponseAsMessage GenerateAndUpdateProjectCode(GenerateProjCodeReq generateProjCodeReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		String projCode = "";
		int updateSeqStatus = 0;
		try {
			logger.info("GenerateAndUpdateProjectCode service start  ");
			 int existingCodeCount =0;
			
			if (generateProjCodeReq.getProjectCode() != null &&  !generateProjCodeReq.getProjectCode().equalsIgnoreCase("")) {
				projCode = generateProjCodeReq.getProjectCode();	
			   existingCodeCount = iStageManagementDAO.getProjectCode(generateProjCodeReq.getProjectCode(), generateProjCodeReq.getTenantId());
	        
		        if (existingCodeCount == 0) {
				int updateStatus = iStageManagementDAO.GenerateAndUpdateProjectCode(generateProjCodeReq.getPmHdrId(),
						generateProjCodeReq.getTenantId(), generateProjCodeReq.getProjectCode());
	
					if(updateStatus==1) {
						String seqStatus = indentUploadDAO.getStatusCodebySeqAndDocType("2", generateProjCodeReq.getTenantId(),
								"DC024");
						updateSeqStatus = iStageManagementDAO.updateInitiationDate(CommonMethod.getCurrentDate(), seqStatus, "2",
								generateProjCodeReq.getPmHdrId());
						iStageManagementDAO.updateProjectDtl(seqStatus, "2", generateProjCodeReq.getPmHdrId());
						
						if (projCode != "" && projCode != null && updateSeqStatus == 1) {
							List<String> messageList = new ArrayList<>();
							List<String> otherEmp = new ArrayList<>();
							String projCodeByPmId =indentUploadDAO.getProjectCodeByProjId(generateProjCodeReq.getPmHdrId(),generateProjCodeReq.getTenantId());
							messageList.add("Project "+projCodeByPmId);
							commonNotifyMethod.InvokeNotificationMethod(2, 9, null, generateProjCodeReq.getTenantId(), messageList, otherEmp, "1",generateProjCodeReq.getPmId(), generateProjCodeReq.getPmHdrId(),null);
							
							
							returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
							returnMessage.setResponseDataMessage(projCode);
							returnMessage.setResponseMessage(ResponseMessageMap.successUpdated);
						} else {
							returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
							returnMessage.setResponseDataMessage("Failed to update");
							returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
						}
						logger.info("GenerateAndUpdateProjectCode service end");
			        }else {
			        	logger.info("Project Code is not updated in project_hdr");
			        	returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
						returnMessage.setResponseDataMessage("Failed to update");
						returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			        }
				}else {
					returnMessage.setResponseCode(ResponseMessageMap.responseAlreadyExists);
		            returnMessage.setResponseMessage(ResponseMessageMap.projectCodeDuplicate);
		            return returnMessage;
				}
			} else {
	        	logger.error("Project Code is empty");
	        	returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failed to update");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
	        }
		}catch (Exception ex) {
			logger.error("GenerateAndUpdateProjectCode Error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsMessage UpdateProjectDueDate(UpdateDueDateRequest updateDueDateReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		int insertStatus = 0;
		try {
			logger.info("UpdateProjectDueDate service start  ");
			insertStatus=iStageManagementDAO.updateProjectDueDate(updateDueDateReq.getDueDate(),updateDueDateReq.getEmpId(),
					updateDueDateReq.getPmHdrId(),updateDueDateReq.getReason(),updateDueDateReq.getTenantId());
//				   update duedate in project_hdr  
			iStageManagementDAO.updatePmDueDate(updateDueDateReq.getPmHdrId(),updateDueDateReq.getDueDate());
			
			if(insertStatus==1) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
			logger.info("UpdateProjectDueDate service end");

		} catch (Exception ex) {
			logger.error("UpdateProjectDueDate Error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getProjectDueDates(PmHdrIdAndTenantIdRequest pmHdrIdAndTenantIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<ProjectDueDateEntity> list = new ArrayList<ProjectDueDateEntity>();
		try {
			list = iStageManagementDAO.getProjectDueDates(pmHdrIdAndTenantIdReq.getPmHdrId(),pmHdrIdAndTenantIdReq.getTenantId());

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
			logger.error("getProjectDueDates error---> " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getDefaultComponentName(GetstageprocessDtlRequest req) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetComponentDtls> list = new ArrayList<GetComponentDtls>();
		try {

			String stgCode = iStageManagementDAO.getStageCodeForPmId(req.getProcessDoc(), req.getDocDesc(),req.getTenantId());
			
			if (!stgCode.equalsIgnoreCase("")) {
				list = iStageManagementDAO.getComponentNameForPmId(req.getProcessDoc(), stgCode,req.getTenantId());
			}

			returnList.setResponseData(list);
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.success);
			
		} catch (Exception ex) {
			logger.error("retriveAssyResp error " + ex.getMessage());

		}
		return returnList;
	}
}
