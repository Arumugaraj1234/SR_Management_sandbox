package com.vmfg.task.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.ProjectWbsInitiationMst;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.sales.services.impl.EnquiryService;
import com.vmfg.task.dao.interfaces.IDesignTaskDAO;
import com.vmfg.task.entity.GetAllTaskCategorytcEntity;
import com.vmfg.task.entity.GetAllTaskTypeEntity;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;
import com.vmfg.task.entity.GetTaskHdrByEmpIdEntity;
import com.vmfg.task.entity.InsertTaskTemplateEntity;
import com.vmfg.task.entity.TaskCategoryMstEntity;
import com.vmfg.task.entity.TaskEntryDtlEntity;
import com.vmfg.task.entity.TaskEntryHdrEntity;
import com.vmfg.task.entity.TaskEntryRemarksEntity;
import com.vmfg.task.entity.TaskTemplateDtlEntity;
import com.vmfg.task.entity.TaskTypeMstEntity;
import com.vmfg.task.entity.TemplateHdrNameEntity;
import com.vmfg.task.request.DeleteTaskHdrAndDtlReq;
import com.vmfg.task.request.DeleteTemplateHdrAndDtlReq;
import com.vmfg.task.request.GetAllTaskTypeRequest;
import com.vmfg.task.request.GetTaskCategoryByPmHdrIdRequest;
import com.vmfg.task.request.GetTaskEntryDtlByDeptDtlIdRequest;
import com.vmfg.task.request.GetTaskEntryDtlBySlaveIdRequest;
import com.vmfg.task.request.GetTaskHdrByEmpIdReq;
import com.vmfg.task.request.GetTaskPercentFlagRequest;
import com.vmfg.task.request.GetTaskRecordedRequest;
import com.vmfg.task.request.GetTaskRemarksByIDRequest;
import com.vmfg.task.request.GetTemplateHdrNameRequest;
import com.vmfg.task.request.GettemplateDtlRequest;
import com.vmfg.task.request.TaskCategoryMasterReq;
import com.vmfg.task.request.TaskDtlByempIdRequest;
import com.vmfg.task.request.TaskDtlBytypeCodeRequest;
import com.vmfg.task.request.TaskReassignRequest;
import com.vmfg.task.request.TaskTypeMasterReq;
import com.vmfg.task.request.UpdateTaskDtlPtgRequest;
import com.vmfg.task.request.UpdateTaskDtlSeqRequest;
import com.vmfg.task.request.UpdateTaskEntryDtlRequest;
import com.vmfg.task.response.GetRemarksByIdResponse;
import com.vmfg.task.response.GetTaskTemplateHdrResponse;
import com.vmfg.task.services.interfaces.IDesignTaskServices;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class DesignTaskServices implements IDesignTaskServices {

	private static final Logger logger = LoggerFactory.getLogger(EnquiryService.class);

	@Autowired
	IDesignTaskDAO iDesignTaskDAO;

	@Autowired
	StageManagementDAO stageManagementDAO;

	@Autowired
	UploadManagementDAO uploadManagementDAO;

	@Autowired
	private IndentUploadDAO indentUploadDAO;
	
	@Autowired
	CommonNotifyMethod commonNotifyMethod;

	@Override
	public ResponseAsList getTaskTemplateHdr(TaskDtlByempIdRequest taskDtlByempIdReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			String empId = taskDtlByempIdReq.getEmpId();
			String tenantId = taskDtlByempIdReq.getTenantId();
			List<GetTaskTemplateHdrResponse> taskDtl = new ArrayList<GetTaskTemplateHdrResponse>();
			String deptCode = iDesignTaskDAO.getEmpDepartmentCode(empId, tenantId);
			if (!deptCode.equalsIgnoreCase("")) {

				taskDtl = iDesignTaskDAO.getTaskNameByDept(deptCode, tenantId, "1");
			}
			if (taskDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskDtl);
			}
		} catch (Exception ex) {
			logger.error("getTaskTemplateHdr Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getTaskTypeByEmp(TaskDtlByempIdRequest taskDtlByempIdReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			// String empId = taskDtlByempIdReq.getEmpId();
			String tenantId = taskDtlByempIdReq.getTenantId();
			List<TaskTypeMstEntity> taskTypeDtl = new ArrayList<TaskTypeMstEntity>();
			String departmentCode = taskDtlByempIdReq.getDepCode();
			List<ProjectWbsInitiationMst> pmIdList = new ArrayList<ProjectWbsInitiationMst>();
			if (departmentCode.equalsIgnoreCase("")) {
				pmIdList = stageManagementDAO.getPMFromPMID(taskDtlByempIdReq.getPmId(), tenantId);
				if (pmIdList.size() > 0) {
					if (!pmIdList.get(0).getDeptCode().equalsIgnoreCase("")) {
						taskTypeDtl = iDesignTaskDAO.getTaskTypeDtl(pmIdList.get(0).getDeptCode(),"1", tenantId);
					}
				}else {
					list.setResponseCode(ResponseMessageMap.noRecord);
					list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
					list.setResponseData(taskTypeDtl);
					return list;
				}
			} else {
				taskTypeDtl = iDesignTaskDAO.getTaskTypeDtl(departmentCode,"1", tenantId);
			}

			if (taskTypeDtl.size() > 0) {
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskTypeDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskTypeDtl);
			}
		}
		 catch (Exception ex) {
			logger.error("getTaskTypeByEmp Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getTaskCategoryByTypeCode(TaskDtlBytypeCodeRequest taskDtlBytypeCodeReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			String typeCode = taskDtlBytypeCodeReq.getTypeCode();
			String tenantId = taskDtlBytypeCodeReq.getTenantId();
			List<TaskCategoryMstEntity> taskCatDtl = new ArrayList<TaskCategoryMstEntity>();

			taskCatDtl = iDesignTaskDAO.getCategoryMst(tenantId, "1", typeCode);
			if (taskCatDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskCatDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskCatDtl);
			}
		} catch (Exception ex) {
			logger.error("getTaskCategoryByTypeCode Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getTaskEntryDtlByMstId(GetTaskEntryDtlBySlaveIdRequest taskEntryDtlBySlaveIdReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			String typeCode = taskEntryDtlBySlaveIdReq.getTypeCode();
			String tenantId = taskEntryDtlBySlaveIdReq.getTenantId();
			String categoryCode = taskEntryDtlBySlaveIdReq.getCategoryCode();
			String masterID = taskEntryDtlBySlaveIdReq.getMasterId();
			String empId = taskEntryDtlBySlaveIdReq.getEmpId();
			String desiCode = iDesignTaskDAO.getEmpDesinationCode(empId, tenantId);
			List<TaskEntryHdrEntity> taskEntryhdr = new ArrayList<TaskEntryHdrEntity>();

			taskEntryhdr = iDesignTaskDAO.getTaskEntryHdr(typeCode, categoryCode, masterID, tenantId);
			if (taskEntryhdr.size() > 0) {
				for (int i = 0; i < taskEntryhdr.size(); i++) {
					List<TaskEntryDtlEntity> taskDtl = iDesignTaskDAO.getTaskEntryDtl(taskEntryhdr.get(i).getTeHdrId());
					for (int j = 0; j < taskDtl.size(); j++) {
						int checkApp = stageManagementDAO.empApproveCheck(desiCode, "DC014",
								taskDtl.get(j).getApprovalSeq(), tenantId);
						if (checkApp > 0) {
							taskDtl.get(j).setIsApproval("True");
						} else {
							taskDtl.get(j).setIsApproval("False");
						}
					}
					taskEntryhdr.get(i).setTaskEntryDtl(taskDtl);
				}
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskEntryhdr);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskEntryhdr);
			}
		} catch (Exception ex) {
			logger.error("getTaskEntryDtlByMstId Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getTaskEntryDtlByDeptDtlId(GetTaskEntryDtlByDeptDtlIdRequest getTaskEntryDtlByDeptDtlIdReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			String typeCode = getTaskEntryDtlByDeptDtlIdReq.getTypeCode();
			String tenantId = getTaskEntryDtlByDeptDtlIdReq.getTenantId();
			String categoryCode = getTaskEntryDtlByDeptDtlIdReq.getCategoryCode();
			String masterId = getTaskEntryDtlByDeptDtlIdReq.getMasterId();
			String empId = getTaskEntryDtlByDeptDtlIdReq.getEmpId();
			String dependentId = getTaskEntryDtlByDeptDtlIdReq.getDependentDtlId();
			String pmId = getTaskEntryDtlByDeptDtlIdReq.getPmId();
			String desiCode = iDesignTaskDAO.getEmpDesinationCode(empId, tenantId);
			List<ProjectWbsInitiationMst> pmIdList= stageManagementDAO.getPMFromPMID(pmId, tenantId);
		//	String departmentCode = iDesignTaskDAO.getEmpDepartmentCode(empId, tenantId);
			// List<TaskEntryHdrEntity> taskEntryhdr = new ArrayList<TaskEntryHdrEntity>();
			List<GetTaskEntryDtlEntity> taskEntryDtlEntity = new ArrayList<GetTaskEntryDtlEntity>();
			if(pmIdList.size()>0) {	
				if(pmId.equalsIgnoreCase("2") || pmId.equalsIgnoreCase("4")) {
					taskEntryDtlEntity =iDesignTaskDAO.getTaskEntryDtlEntity(typeCode,
							categoryCode, masterId, tenantId, dependentId, pmIdList.get(0).getAssignedDept());
				}else {		
					taskEntryDtlEntity =iDesignTaskDAO.getTaskEntryDtlEntity(typeCode,
							categoryCode, masterId, tenantId, dependentId, pmIdList.get(0).getDeptCode());
				}
			// taskEntryhdr = iDesignTaskDAO.getTaskEntryHdrByDependentId(typeCode,
			// categoryCode, slaveId, tenantId,dependentId);
			if (taskEntryDtlEntity.size() > 0) {
				// for(int i =0;i<taskEntryhdr.size();i++) {
				// List<TaskEntryDtlEntity> taskDtl =
				// iDesignTaskDAO.getTaskEntryDtl(taskEntryhdr.get(i).getTeHdrId());
				for (int j = 0; j < taskEntryDtlEntity.size(); j++) {
					
					List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq(getTaskEntryDtlByDeptDtlIdReq.getDocTypeCode(),taskEntryDtlEntity.get(j).getApprovalSeq(), tenantId);
					List<DocumentStatusMstEntity> docLifeCycleMstList = iDesignTaskDAO.getNextSeqandStatus(
							Integer.parseInt(taskEntryDtlEntity.get(j).getApprovalSeq()),
							getTaskEntryDtlByDeptDtlIdReq.getDocTypeCode(), tenantId);
					if (docLifeCycleMstList.size() > 0) {
						int approveBtnEnable = indentUploadDAO.getApprovebtnEnableByCurr(desiCode, tenantId,
								getTaskEntryDtlByDeptDtlIdReq.getDocTypeCode(),
								currSeqDocLifeCycleMstList.get(0).getCurrSequence());
						int approveNextBtnEnable = indentUploadDAO.getApprovebtnEnableByCurr(desiCode, tenantId,
								getTaskEntryDtlByDeptDtlIdReq.getDocTypeCode(),
								docLifeCycleMstList.get(0).getCurrSequence());
						
						docLifeCycleMstList.get(0)
								.setNextSeqStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
										docLifeCycleMstList.get(0).getCurrSequence(), tenantId,
										getTaskEntryDtlByDeptDtlIdReq.getDocTypeCode()));
					
						docLifeCycleMstList.get(0)
								.setNextSeqStatusDesc(iDesignTaskDAO.getStatusByDesc(indentUploadDAO
										.getStatusCodebySeqAndDocType(docLifeCycleMstList.get(0).getCurrSequence(),
												tenantId, getTaskEntryDtlByDeptDtlIdReq.getDocTypeCode()),
										tenantId));
						docLifeCycleMstList.get(0).setIsEditable(currSeqDocLifeCycleMstList.get(0).getIsEditable());
						// cancel seq
						if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
							docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
							docLifeCycleMstList.get(0)
							.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
									currSeqDocLifeCycleMstList.get(0).getCancelSeq(),tenantId, getTaskEntryDtlByDeptDtlIdReq.getDocTypeCode()));

							docLifeCycleMstList.get(0).setCancelStatusDesc(iDesignTaskDAO.getStatusByDesc(
									indentUploadDAO.getStatusCodebySeqAndDocType(
											currSeqDocLifeCycleMstList.get(0).getCancelSeq(), tenantId,getTaskEntryDtlByDeptDtlIdReq.getDocTypeCode()),
									tenantId));
						}else {
							docLifeCycleMstList.get(0).setCancelSeq(null);
						}
//						docLifeCycleMstList.get(0).setIsEditable(currSeqDocLifeCycleMstList.get(0).getIsEditable());
						if (approveBtnEnable > 0) {
							taskEntryDtlEntity.get(j).setIsApproval("True");
							taskEntryDtlEntity.get(j).setDocStatusMst(docLifeCycleMstList);
						} else {
							taskEntryDtlEntity.get(j).setIsApproval("False");
							taskEntryDtlEntity.get(j).setDocStatusMst(docLifeCycleMstList);
						}
						if(approveNextBtnEnable>0) {
							taskEntryDtlEntity.get(j).setDocStatusMst(docLifeCycleMstList);
						}
					}
				}
			}else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskEntryDtlEntity);
			}
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskEntryDtlEntity);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskEntryDtlEntity);
			}
		} catch (Exception ex) {
			logger.error("getTaskEntryDtlByDeptDtlId Error  " + ex);
		}
		return list;
	}

	@Override
	@Transactional
	public ResponseAsMessage updateTaskEntryDtl(UpdateTaskEntryDtlRequest updateTaskEntryDtlReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			int updatestatus = 0;
			String empDept = uploadManagementDAO.getDepCodeByEmpId(updateTaskEntryDtlReq.getEmployeID(),
					updateTaskEntryDtlReq.getTenantId());
			String teHdrid = iDesignTaskDAO.getTeHdrId(empDept, updateTaskEntryDtlReq.getTaskTypeCode(),
					updateTaskEntryDtlReq.getTaskCategoryCode(), updateTaskEntryDtlReq.getTtHdrId(),
					updateTaskEntryDtlReq.getTenantId(), updateTaskEntryDtlReq.getMasterId());
			if (teHdrid.equalsIgnoreCase("")) {
				String dependentId = updateTaskEntryDtlReq.getDependentTeHdrId();

				updatestatus = iDesignTaskDAO.insertTaskEntryHdr(updateTaskEntryDtlReq.getTtHdrId(),
						updateTaskEntryDtlReq.getMasterId(), empDept, updateTaskEntryDtlReq.getTaskTypeCode(),
						updateTaskEntryDtlReq.getTaskCategoryCode(), dependentId, updateTaskEntryDtlReq.getEmployeID(),
						updateTaskEntryDtlReq.getTenantId(),updateTaskEntryDtlReq.getPmHdrId());
				teHdrid = Integer.toString(updatestatus);
			}
			String projCode = indentUploadDAO.getProjectCodeByProjId(updateTaskEntryDtlReq.getPmHdrId(), updateTaskEntryDtlReq.getTenantId());

			if (!teHdrid.equalsIgnoreCase("0")) {
				for (int i = 0; i < updateTaskEntryDtlReq.getTaskEntryDtl().size(); i++) {
					List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
					updatestatus = updateTaskDtltbl(updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getTeDtlId(),
							teHdrid, updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getTtDtlId(),
							updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getActivityName(),
							updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getPlannedStartDate(),
							updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getDueDate(),
							updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getTenantId(),
							updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getAssignTo(),
							updateTaskEntryDtlReq.getEmployeID(),
							updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getRequirementFrom(),
							updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getQty());
					messageList.add("Project "+projCode);
					if(updatestatus>0) {
						commonNotifyMethod.InvokeNotificationMethod(1, 21, updateTaskEntryDtlReq.getTaskEntryDtl().get(i).getAssignTo(), 
								updateTaskEntryDtlReq.getTenantId(), messageList,
								otherEmp, "","", updateTaskEntryDtlReq.getMasterId(), null);
					}
				}
			}
			if (updatestatus > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updateTaskEntryDtl Error" + ex);
		}
		return message;
	}

	public int updateTaskDtltbl(String teDtlId, String teHdrId, String ttdtlId, String activityName,
			String plannedStartDate, String dueDate, String tenantId, String assignTo, String updatedBy,
			String requirement, String qty) {
		int updateDtl = 0;
		try {
			List<DocumentStatusMstEntity> getstatus = stageManagementDAO.getFirstOrLastSeqDocDtl("DC014", "ASC",
					tenantId);
			if (teDtlId.equalsIgnoreCase("")) {
				if (getstatus.size() > 0) {
					updateDtl = iDesignTaskDAO.insertTaskEntryDtl(teHdrId, ttdtlId, activityName, plannedStartDate,
							dueDate, dueDate, null, getstatus.get(0).getCurrSequence(), getstatus.get(0).getDocStatus(),
							"0", tenantId, assignTo, updatedBy, requirement, qty);
				} else {
					logger.debug("updateTaskDtltbl getStatus is empty");
				}
			} else {
				int taskEntryCompleteSts = iDesignTaskDAO.taskEntryCompleteSts(teDtlId);
				if (taskEntryCompleteSts == 0) {
					updateDtl = iDesignTaskDAO.updateTaskEntryDtl(teHdrId, ttdtlId, activityName, plannedStartDate,
							dueDate, dueDate, null, "0", teDtlId, assignTo, updatedBy);
				}
			}

		} catch (Exception ex) {
			logger.error("updateTaskDtltbl Error " + ex);
		}
		return updateDtl;

	}

	@Override
	public ResponseAsMessage updateTaskDtlSeq(UpdateTaskDtlSeqRequest updateTaskDtlSeqreq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			int updatestatus = 0;
			String isComplete = "0";
			String completedDate = null;
			String CompletePtg = "COMPLETED_PTG";
			String isFlag = updateTaskDtlSeqreq.getIsFlag();
			String seq = "", Status = "";

			if (updateTaskDtlSeqreq.getSeq() == null) {

				List<DocumentStatusMstEntity> docLifeCycleMstList = uploadManagementDAO.getPrevSeqandStatus(
						Integer.parseInt(updateTaskDtlSeqreq.getSeq()), updateTaskDtlSeqreq.getDocTypeCode(),
						updateTaskDtlSeqreq.getTenantId());
				seq = docLifeCycleMstList.get(0).getCurrSequence();
				Status = docLifeCycleMstList.get(0).getDocStatus();

			} else {
				seq = updateTaskDtlSeqreq.getSeq();
				Status = updateTaskDtlSeqreq.getStatus();

			}
			String statCode = iDesignTaskDAO.getStatusByDesc(Status, updateTaskDtlSeqreq.getTenantId());
			
			if (updateTaskDtlSeqreq.getIsLastSeq().equalsIgnoreCase("1")) {
				isComplete = "1";
				completedDate = CommonMethod.getCurrentDate();
				CompletePtg = "100";
				updatestatus = iDesignTaskDAO.updateTaskDtlStatus(seq, Status, updateTaskDtlSeqreq.getDtlId(),
						updateTaskDtlSeqreq.getTenantId(), isComplete, completedDate, statCode,
						updateTaskDtlSeqreq.getRemarks(), updateTaskDtlSeqreq.getEmpId(), CompletePtg);
			}else {
				
				updatestatus = iDesignTaskDAO.updateTaskDtlStatusTbl(seq, Status, updateTaskDtlSeqreq.getDtlId(),
						updateTaskDtlSeqreq.getTenantId(), isComplete, completedDate, statCode,
						updateTaskDtlSeqreq.getRemarks(), updateTaskDtlSeqreq.getEmpId());
			}
			
			List<TaskEntryHdrEntity> taskEntryHdrList = iDesignTaskDAO
					.getTaskEntryHdrByDtlId(updateTaskDtlSeqreq.getTenantId(), updateTaskDtlSeqreq.getDtlId());
			
			String teDtlid = updateTaskDtlSeqreq.getDtlId();
			if (!teDtlid.equalsIgnoreCase("")) {
				
				// check if the seq is on hold
				if(seq.equalsIgnoreCase("5")) {
					for (int i = 0; i < taskEntryHdrList.size(); i++) {
						iDesignTaskDAO.updateSubTaskSeq(taskEntryHdrList.get(i).getTeHdrId(),updateTaskDtlSeqreq.getEmpId(),seq,Status);
					}
				}
				
				if (isFlag.equalsIgnoreCase("1") && updateTaskDtlSeqreq.getIsLastSeq().equalsIgnoreCase("1")) {
		
					for (int i = 0; i < taskEntryHdrList.size(); i++) {
						int getCounthdrId = iDesignTaskDAO.getCounthdrId(taskEntryHdrList.get(i).getTeHdrId(),
								taskEntryHdrList.get(i).getTenantId());
						if (getCounthdrId > 0) {
							message.setResponseCode(ResponseMessageMap.failToupdateCode);
							message.setResponseMessage(ResponseMessageMap.subTaskNotCompleted);
							message.setResponseDataMessage(ResponseMessageMap.subTaskNotCompleted);
							return message;
						}
					}
				}
				

				

				
				String projCode = indentUploadDAO.getProjectCodeByProjId(updateTaskDtlSeqreq.getPmHdrId(), updateTaskDtlSeqreq.getTenantId());

				if(updatestatus>0) {
					List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
					String assignedto=iDesignTaskDAO.getAssignedTo(updateTaskDtlSeqreq.getDtlId());
					String approvingDesc=commonNotifyMethod.getNxtAppDesc(updateTaskDtlSeqreq.getDocTypeCode(), updateTaskDtlSeqreq.getSeq(),updateTaskDtlSeqreq.getTenantId());
					String approvalDocDesc=commonNotifyMethod.getNxtAppDocDesc(updateTaskDtlSeqreq.getDocTypeCode(), updateTaskDtlSeqreq.getSeq(),updateTaskDtlSeqreq.getTenantId());
					messageList.add("Project "+projCode);
					messageList.add(approvalDocDesc);
					commonNotifyMethod.InvokeNotificationMethod(2, 21, assignedto, 
							updateTaskDtlSeqreq.getTenantId(), messageList,
							otherEmp, "",updateTaskDtlSeqreq.getPmId(), updateTaskDtlSeqreq.getMstId(), approvingDesc);
					commonNotifyMethod.InvokeApprovalDesigMethod(updateTaskDtlSeqreq.getPmId(),
							updateTaskDtlSeqreq.getDocTypeCode(), updateTaskDtlSeqreq.getDtlId(), updateTaskDtlSeqreq.getPmHdrId(), updateTaskDtlSeqreq.getTenantId(),
							"", approvingDesc,updateTaskDtlSeqreq.getEnquiryID() ,projCode);
				}
			}

			if (updatestatus > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseMessage(ResponseMessageMap.successUpdated);
				message.setResponseDataMessage(ResponseMessageMap.successUpdated);
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				message.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updateTaskDtlSeq " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage UpdateTaskFileDtl(JSONObject jsonObj, MultipartFile file) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			JSONArray tritem = jsonObj.getJSONArray("mstDtl");
			String tenantId = "";
			String teDtlId = "";
			String pmHdrId = "";
			String stageCode = "";
			String empId = "";
			String documentName = "";
			String remarks = "";
			String type = "";
			String refId = "";
			String uploadDocType = "";
			for (int i = 0; i < tritem.length(); i++) {
				JSONObject objects = tritem.getJSONObject(i);
				JSONArray keys = objects.names();
				for (int j = 0; j < keys.length(); ++j) {
					String key = keys.getString(j);
					String value = objects.getString(key);
					if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					} else if (key.equalsIgnoreCase("teDtlId")) {
						teDtlId = value;
					} else if (key.equalsIgnoreCase("pmHdrId")) {
						pmHdrId = value;
					} else if (key.equalsIgnoreCase("stageCode")) {
						stageCode = value;
					} else if (key.equalsIgnoreCase("empId")) {
						empId = value;
					} else if (key.equalsIgnoreCase("documentName")) {
						documentName = value;
					} else if (key.equalsIgnoreCase("remarks")) {
						remarks = value;
					} else if (key.equalsIgnoreCase("type")) {
						type = value;
					} else if (key.equalsIgnoreCase("refId")) {
						refId = value;
					} else if (key.equalsIgnoreCase("uploadDocType")) {
						uploadDocType = value;
					}
				}
			}

			String fileName = file.getOriginalFilename();
			logger.info("getfileName---> " + fileName);

			String equiryId = iDesignTaskDAO.projectEnquiryIdByprojId(pmHdrId, tenantId);
			int newDmId = 0;
			int insertFileDtls = 0;
			if (!equiryId.equalsIgnoreCase("")) {
				List<DocumentStatusMstEntity> getstatus = stageManagementDAO.getFirstOrLastSeqDocDtl("DC014", "DESC",
						tenantId);
				for (int i = 0; i < getstatus.size(); i++) {
					newDmId = uploadManagementDAO.insertDocumentDtls(equiryId, pmHdrId, documentName, teDtlId,
							stageCode, uploadDocType, 1, tenantId, remarks, getstatus.get(0).getCurrSequence(), "0","DC014");
					if (newDmId > 0) {
						insertFileDtls = uploadManagementDAO.insertNewFileDtl(file, tenantId, newDmId, uploadDocType,
								empId, 1, "DC014", type, refId);
						// int insertDocStatusDtl=uploadManagementDAO.insertDocAppStatusDtl(newDmId,
						// getstatus.get(0).getCurrSequence(), tenantId, list.get(0).getDocStatus(),
						// empId);

					}
				}
			}
			if (newDmId > 0 && insertFileDtls > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage(fileName);
				message.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseDataMessage("Failure");
				message.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("UpdateTaskFileDtl " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage UpdateTaskDtlPtg(UpdateTaskDtlPtgRequest updateTaskDtlPtgReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			int updatestatus = 0;
			String teDtlid = updateTaskDtlPtgReq.getTeDtlId();
			if (!teDtlid.equalsIgnoreCase("")) {
				updatestatus = iDesignTaskDAO.UpdateTaskDtlPtg(teDtlid, updateTaskDtlPtgReq.getPtgVal(),
						updateTaskDtlPtgReq.getEmployeeId(), updateTaskDtlPtgReq.getRemarks());
			}

			iDesignTaskDAO.updateAvgPercent(teDtlid, updateTaskDtlPtgReq.getTenantId(),updateTaskDtlPtgReq.getEmployeeId());

			if (updatestatus > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage("Success");
				message.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseDataMessage("Failure");
				message.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("UpdateTaskDtlPtg Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsList gettemplateDtl(GettemplateDtlRequest gettemplateDtlReq) {
		ResponseAsList list = new ResponseAsList();
		try {

			List<TaskTemplateDtlEntity> taskDtl = new ArrayList<TaskTemplateDtlEntity>();
			taskDtl = iDesignTaskDAO.tasktemplateDtl(gettemplateDtlReq.getTtHdrId(), gettemplateDtlReq.getTenantId());

			if (taskDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskDtl);
			}
		} catch (Exception ex) {
			logger.error("gettemplateDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList gettemplateHdrName(GetTemplateHdrNameRequest gettemplateHdrNamereq) {
		ResponseAsList list = new ResponseAsList();
		try {

			String department = uploadManagementDAO.getDepCodeByEmpId(gettemplateHdrNamereq.getEmpId(),
					gettemplateHdrNamereq.getTenantId());
			List<TemplateHdrNameEntity> taskDtl = new ArrayList<TemplateHdrNameEntity>();
			taskDtl = iDesignTaskDAO.taskhdrName(department, gettemplateHdrNamereq.getTypeCode(),
					gettemplateHdrNamereq.getCatCode(), gettemplateHdrNamereq.getTenantId());

			if (taskDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskDtl);
			}
		} catch (Exception ex) {
			logger.error("gettemplateHdrName Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList gettemplateHdrName(GetTaskRemarksByIDRequest getTaskRemarksByIDRequest) {
		ResponseAsList list = new ResponseAsList();
		try {

			List<GetRemarksByIdResponse> remarks = iDesignTaskDAO.getTemplateHdrName(getTaskRemarksByIDRequest);

			if (remarks.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(remarks);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(remarks);
			}

		} catch (Exception ex) {
			logger.error("gettemplateHdrName Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage TaskReassignFlag(TaskReassignRequest taskReassReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			String updatestatus = "";

			updatestatus = iDesignTaskDAO.TaskReassignFlag(taskReassReq);

			message.setResponseCode(ResponseMessageMap.responseCodeOk);
			message.setResponseDataMessage(ResponseMessageMap.success);
			message.setResponseMessage(updatestatus);

		} catch (Exception ex) {
			logger.error("TaskReassignFlag Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage TaskReassignForEmpId(TaskReassignRequest taskReassReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			int updatestatus = 0;
			updatestatus = iDesignTaskDAO.TaskReassignForEmpId(taskReassReq);
			if (updatestatus > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage(ResponseMessageMap.successUpdated);
				message.setResponseMessage(ResponseMessageMap.successUpdated);
			}

		} catch (Exception ex) {
			logger.error("TaskReassignFlag Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsList getTaskCategoryRecorded(GetTaskRecordedRequest getRecordedTask) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<TaskCategoryMstEntity> taskCatDtl = new ArrayList<TaskCategoryMstEntity>();

			taskCatDtl = iDesignTaskDAO.getTaskCategoryRecorded(getRecordedTask);

			if (taskCatDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskCatDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskCatDtl);
			}

		} catch (Exception ex) {
			logger.error("gettemplateHdrName Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getTaskDtlRemarksById(GetTaskRemarksByIDRequest getTaskRemarksByID) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<TaskEntryRemarksEntity> taskCatDtl = new ArrayList<TaskEntryRemarksEntity>();

			taskCatDtl = iDesignTaskDAO.getTaskDtlRemarksById(getTaskRemarksByID);

			if (taskCatDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskCatDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskCatDtl);
			}

		} catch (Exception ex) {
			logger.error("getTaskDtlRemarksById Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage TaskPercentageFlag(GetTaskPercentFlagRequest taskPercentFlag) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			String updatestatus = "";

			updatestatus = iDesignTaskDAO.TaskPercentageFlag(taskPercentFlag);
			message.setResponseCode(ResponseMessageMap.responseCodeOk);
			message.setResponseDataMessage(ResponseMessageMap.success);
			message.setResponseMessage(updatestatus);

		} catch (Exception ex) {
			logger.error("taskPercentFlag service Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage TaskPercentageUpdate(GetTaskPercentFlagRequest taskpercent) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			int updatestatus = 0;
			String teDtlid = taskpercent.getDtlId();

			updatestatus = iDesignTaskDAO.updateAvgPercent(teDtlid, taskpercent.getTenantId(),taskpercent.getEmployeeId());
			if (updatestatus > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage("Success");
				message.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseDataMessage("Failure");
				message.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("UpdateTaskDtlPtg Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsList getTaskHdrByEmpId(GetTaskHdrByEmpIdReq getTaskHdrByEmpIdReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<GetTaskHdrByEmpIdEntity> taskCatDtl = new ArrayList<GetTaskHdrByEmpIdEntity>();

			taskCatDtl = iDesignTaskDAO.getTaskHdrByEmpId(getTaskHdrByEmpIdReq);

			if (taskCatDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskCatDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskCatDtl);
			}

		} catch (Exception ex) {
			logger.error("getTaskHdrByEmpId Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage deleteTaskHdrAndDtl(DeleteTaskHdrAndDtlReq deleteTaskHdrAndDtlReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
		//	List<GetTaskHdrAndDtlIdEntity> dtlList = null;
			
	//		dtlList = iDesignTaskDAO.deleteTaskHdrAndDtl(deleteTaskHdrAndDtlReq.getTeHdrId(),
	//				deleteTaskHdrAndDtlReq.getTenantId());

		//	for (GetTaskHdrAndDtlIdEntity str : dtlList) {
			int deleteStatus=0;
			
			int subCountCheck=iDesignTaskDAO.CheckSubCount(deleteTaskHdrAndDtlReq.getTeDtlId(), deleteTaskHdrAndDtlReq.getTenantId());
			if(subCountCheck>0) {
				
					message.setResponseCode(ResponseMessageMap.failToupdateCode);
					message.setResponseDataMessage("Cannot delete parent task due to existing subtask.");
					message.setResponseMessage("Cannot delete parent task due to existing subtask.");
				
			}else {
				 deleteStatus=	iDesignTaskDAO.deleteTaskDtl(deleteTaskHdrAndDtlReq.getTeDtlId(), deleteTaskHdrAndDtlReq.getTenantId());
				 if (deleteStatus > 0) {
						message.setResponseCode(ResponseMessageMap.responseCodeOk);
						message.setResponseDataMessage(ResponseMessageMap.successMsg);
						message.setResponseMessage(ResponseMessageMap.successfulDeleted);
					} else {
						message.setResponseCode(ResponseMessageMap.failToupdateCode);
						message.setResponseDataMessage(ResponseMessageMap.failMsg);
						message.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
					}
			}
			
		//	}
		//	deleteStatus = iDesignTaskDAO.CommanDeleteTasHdrAndDtls(deleteTaskHdrAndDtlReq.getTeHdrId(),
	//				deleteTaskHdrAndDtlReq.getTenantId());

			

		} catch (Exception ex) {
			logger.error("deleteTaskHdrAndDtl Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsList getTaskCategoryByPmHdrId(GetTaskCategoryByPmHdrIdRequest getTaskCategoryByPmHdrIdReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<GetTaskHdrByEmpIdEntity> taskCatDtl = new ArrayList<GetTaskHdrByEmpIdEntity>();

			taskCatDtl = iDesignTaskDAO.getTaskCategoryByPmHdrId(getTaskCategoryByPmHdrIdReq);

			if (taskCatDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskCatDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskCatDtl);
			}

		} catch (Exception ex) {
			logger.error("gettemplateHdrName Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage insertTypeMst(TaskTypeMasterReq taskTypeMasterReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			String taskTypeCode = taskTypeMasterReq.getTaskTypeCode();
			String taskDesc = taskTypeMasterReq.getTaskDesc();
			String deptCode = taskTypeMasterReq.getDeptCode();
			String tenantId = taskTypeMasterReq.getTenantID();
			String isActive = taskTypeMasterReq.getIsActive();
			int taskInsert=0;
			if(taskTypeCode==null || taskTypeCode.isEmpty()) {
				//insert
				 taskInsert = iDesignTaskDAO.insertTaskTypeMaster(taskTypeCode,taskDesc,deptCode,tenantId,isActive);
			}else {
				//update
				 taskInsert = iDesignTaskDAO.updateTypeMaster(taskTypeCode,taskDesc,deptCode,tenantId,isActive);
			}
			 if (taskInsert > 0) {
					message.setResponseCode(ResponseMessageMap.responseCodeOk);
					message.setResponseDataMessage(ResponseMessageMap.successMsg);
					message.setResponseMessage(ResponseMessageMap.successCreated);
				} else {
					message.setResponseCode(ResponseMessageMap.failToupdateCode);
					message.setResponseDataMessage(ResponseMessageMap.failMsg);
					message.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
				}
			
					
		}catch(Exception ex) {
			logger.error("insertTypeMst Error  " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage insertTaskCategoryMst(TaskCategoryMasterReq taskCategoryMasterReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			
			 String taskCategoryCode =taskCategoryMasterReq.getTaskCategoryCode();
			 String taskCategoryDesc=taskCategoryMasterReq.getTaskCategoryDesc();
			 String taskTypeCode=taskCategoryMasterReq.getTaskTypeCode();
			 String tenantID=taskCategoryMasterReq.getTenantID();
			 String isActive=taskCategoryMasterReq.getIsActive(); 
			
			
			int taskCatInsert=0;
			if(taskCategoryCode==null || taskCategoryCode.isEmpty()) {
				//insert
				taskCatInsert = iDesignTaskDAO.insertTaskCategoryMst(taskCategoryCode,taskCategoryDesc,taskTypeCode,isActive,tenantID);
			}else {
				//update
				taskCatInsert = iDesignTaskDAO.updateTaskCategoryMst(taskCategoryCode,taskCategoryDesc,taskTypeCode,isActive,tenantID);
			}
			 if (taskCatInsert > 0) {
					message.setResponseCode(ResponseMessageMap.responseCodeOk);
					message.setResponseDataMessage(ResponseMessageMap.successMsg);
					message.setResponseMessage(ResponseMessageMap.successCreated);
				} else {
					message.setResponseCode(ResponseMessageMap.failToupdateCode);
					message.setResponseDataMessage(ResponseMessageMap.failMsg);
					message.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
				}
			
		}catch(Exception ex) {
			logger.error("insertTaskCategoryMst Error  " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsList getAllTaskType(GetAllTaskTypeRequest getAllTaskTypeReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			String deptCode = getAllTaskTypeReq.getDeptCode();
			String tenantId = getAllTaskTypeReq.getTenantId();
			List<GetAllTaskTypeEntity> taskCatDtl = new ArrayList<GetAllTaskTypeEntity>();

			taskCatDtl = iDesignTaskDAO.getAllTaskType(tenantId, deptCode);
			if (taskCatDtl.size() > 0) {

				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskCatDtl);
			} else {
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskCatDtl);
			}
		} catch (Exception ex) {
			logger.error("getAllTaskType Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getAllTaskCategoryByTypeCode(TaskDtlBytypeCodeRequest taskDtlBytypeCodeReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			String typeCode = taskDtlBytypeCodeReq.getTypeCode();
			String tenantId = taskDtlBytypeCodeReq.getTenantId();
			List<GetAllTaskCategorytcEntity> taskCatDtl = new ArrayList<GetAllTaskCategorytcEntity>();

			taskCatDtl = iDesignTaskDAO.getCategoryMstAll(tenantId, typeCode);
			if (taskCatDtl.size() > 0) {

				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(taskCatDtl);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(taskCatDtl);
			}
		} catch (Exception ex) {
			logger.error("getAllTaskCategoryByTypeCode Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage updateTaskTemplate(InsertTaskTemplateEntity insertTaskTemplateReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			
			 String ttHdrId =insertTaskTemplateReq.getTtHdrId();
		//	 String taskCategoryDesc=taskCategoryMasterReq.getTaskCategoryDesc();
		//	 String taskTypeCode=taskCategoryMasterReq.getTaskTypeCode();
		//	 String tenantID=taskCategoryMasterReq.getTenantID();
		//	 String isActive=taskCategoryMasterReq.getTenantID(); 
			
			
			int taskCatInsert=0;
			if(ttHdrId==null || ttHdrId.isEmpty()) {
				//insert
				taskCatInsert = iDesignTaskDAO.insertTaskTemplateHdr(insertTaskTemplateReq.getTtName(),insertTaskTemplateReq.getTtCreatedBy(),insertTaskTemplateReq.getTtCreatedOn(),insertTaskTemplateReq.getTtDepartmentCode(),insertTaskTemplateReq.getTaskTypeCode(),insertTaskTemplateReq.getTaskCategoryCode(),insertTaskTemplateReq.getIsActive(),CommonMethod.getCurrentDateTime(),insertTaskTemplateReq.getLastUpdatedBy(),insertTaskTemplateReq.getTenantId());
			}else {
			int	updateTaskCatInsert = iDesignTaskDAO.updateTaskTemplateHdr(insertTaskTemplateReq.getTtName(),insertTaskTemplateReq.getTtDepartmentCode(),insertTaskTemplateReq.getTaskTypeCode(),insertTaskTemplateReq.getTaskCategoryCode(),insertTaskTemplateReq.getIsActive(),CommonMethod.getCurrentDateTime(),insertTaskTemplateReq.getLastUpdatedBy(),insertTaskTemplateReq.getTtHdrId());
				if(updateTaskCatInsert>0) {
					taskCatInsert =Integer.parseInt(insertTaskTemplateReq.getTtHdrId()) ;
				}
			
			}
			

			
			 if (taskCatInsert > 0) {
				 for(int q= 0 ; q < insertTaskTemplateReq.getTaskTemplateDtlList().size();q++) {
						String ttDtlId = insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getTtDtlId();
					 
						if(ttDtlId==null || ttDtlId.isEmpty()) {
							//insert
							 iDesignTaskDAO.insertTaskTemplateDtl(Integer.toString(taskCatInsert),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getActivityName(),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getPlannedDurationDays(),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getIsActive(),CommonMethod.getCurrentDateTime(),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getLastUpdatedBy(),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getTenantId());
						}else {
						 iDesignTaskDAO.updateTaskTemplateDtl(insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getActivityName(),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getPlannedDurationDays(),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getIsActive(),CommonMethod.getCurrentDateTime(),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getLastUpdatedBy(),insertTaskTemplateReq.getTaskTemplateDtlList().get(q).getTtDtlId());
						
						}
						
					}
					message.setResponseCode(ResponseMessageMap.responseCodeOk);
					message.setResponseDataMessage(ResponseMessageMap.successMsg);
					message.setResponseMessage(ResponseMessageMap.successCreated);
				} else {
					message.setResponseCode(ResponseMessageMap.failToupdateCode);
					message.setResponseDataMessage(ResponseMessageMap.failMsg);
					message.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
				}
			
		}catch(Exception ex) {
			logger.error("updateTaskTemplate Error  " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage deleteTemplateHdrAndDtl(DeleteTemplateHdrAndDtlReq deletereq) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("deleteTemplateHdrAndDtl Service start ");
		int response = 0;
		int RemoveTtId = 0;
		try {
			String ttHdrId = deletereq.getTtHdrId();
			String ttDtlId = deletereq.getTtDtlId();
			String tenantId = deletereq.getTenantId();
		
			response = iDesignTaskDAO.deleteTemplateHdrAndDtl(ttDtlId, tenantId);
			RemoveTtId = iDesignTaskDAO.CheckTtTempDtl(ttHdrId, tenantId);
			if (response > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successfulDeleted);
			}else if (response > 0 && RemoveTtId > 0 ) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successfulDeleted);
			}else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failTodeleteMsg);
			}
			logger.info("deleteTemplateHdrAndDtl Service end ");
		} catch (Exception ex) {
			logger.error("deleteTemplateHdrAndDtl error " + ex);
		}
		return returnres;
	}
}
