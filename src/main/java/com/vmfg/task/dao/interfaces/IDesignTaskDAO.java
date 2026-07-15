package com.vmfg.task.dao.interfaces;

import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.task.entity.GetAllTaskCategorytcEntity;
import com.vmfg.task.entity.GetAllTaskTypeEntity;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;
import com.vmfg.task.entity.GetTaskHdrAndDtlIdEntity;
import com.vmfg.task.entity.GetTaskHdrByEmpIdEntity;
import com.vmfg.task.entity.TaskCategoryMstEntity;
import com.vmfg.task.entity.TaskEntryDtlEntity;
import com.vmfg.task.entity.TaskEntryHdrEntity;
import com.vmfg.task.entity.TaskEntryRemarksEntity;
import com.vmfg.task.entity.TaskTemplateDtlEntity;
import com.vmfg.task.entity.TaskTypeMstEntity;
import com.vmfg.task.entity.TemplateHdrNameEntity;
import com.vmfg.task.request.GetTaskCategoryByPmHdrIdRequest;
import com.vmfg.task.request.GetTaskHdrByEmpIdReq;
import com.vmfg.task.request.GetTaskPercentFlagRequest;
import com.vmfg.task.request.GetTaskRecordedRequest;
import com.vmfg.task.request.GetTaskRemarksByIDRequest;
import com.vmfg.task.request.TaskReassignRequest;
import com.vmfg.task.response.GetRemarksByIdResponse;
import com.vmfg.task.response.GetTaskTemplateHdrResponse;

public interface IDesignTaskDAO {

	String getEmpDesinationCode(String empId, String tenantId);

	String getEmpDepartmentCode(String empId, String tenantId);

	List<GetTaskTemplateHdrResponse> getTaskNameByDept(String deptCode, String tenantId, String isActive);

	List<TaskTypeMstEntity> getTaskTypeDtl(String deptCode, String isActive, String tenantId);

	List<TaskCategoryMstEntity> getCategoryMst(String tenantId, String isActive, String typeCode);
	
	List<GetAllTaskCategorytcEntity> getCategoryMstAll(String tenantId, String typeCode);
	
	List<GetAllTaskTypeEntity> getAllTaskType(String tenantId, String deptCode);

	List<TaskEntryHdrEntity> getTaskEntryHdr(String typeCode, String category, String slaveId, String tenantId);

	List<TaskEntryHdrEntity> getTaskEntryHdrByDependentId(String typeCode, String category, String slaveId,
			String tenantId, String dependentId);

	List<TaskEntryDtlEntity> getTaskEntryDtl(String ttHdrId);

	int updateTaskEntryDtl(String teHdrId, String ttDtlId, String activityName, String plannedStartDate, String dueDate,
			String plannedCompletedDAte, String completedDate, String isCompleted, String teDtlId, String assignTo,
			String updatedBy);

	int updateTaskEntryHdr(String teHdrId, String empid);

	int taskEntryCompleteSts(String tedtlId);

	int updateTaskDtlStatus(String seq, String status, String teDtlId, String tenantId, String isCompleted,
			String completedDate, String statusDesc, String remarks, String empId, String completePtg);

	String projectEnquiryIdByprojId(String projId, String tenantId);

	List<GetTaskEntryDtlEntity> getTaskEntryDtlEntity(String ttCode, String tcCode, String masterId, String tenantid,
			String dependentId, String department);

	List<TaskTemplateDtlEntity> tasktemplateDtl(String ttHdrId, String tenantId);

	List<TemplateHdrNameEntity> taskhdrName(String deptCode, String typeCode, String catCode, String tenantId);

	List<GetRemarksByIdResponse> getTemplateHdrName(GetTaskRemarksByIDRequest getTaskRemarksByIDRequest);

	String getStatusByDesc(String statusCode, String tenantId);

	List<DocumentStatusMstEntity> getNextSeqandStatus(int currentSeq, String docType, String tenantId);

	List<TaskEntryHdrEntity> getTaskEntryHdrByDtlId(String tenantId, String dependentId);

	int getCounthdrId(String teHdrId, String tenantId);

	int getCounthdrIdIsCompleted(String teHdrId, String tenantId, String completed);

	String TaskReassignFlag(TaskReassignRequest taskReassReq);

	int TaskReassignForEmpId(TaskReassignRequest taskReassReq);

	int UpdateTaskDtlPtg(String teDtlid, String ptgVal, String employeeId, String remarks);

	List<TaskCategoryMstEntity> getTaskCategoryRecorded(GetTaskRecordedRequest getRecordedTask);
	
	List<GetTaskHdrByEmpIdEntity> getTaskCategoryByPmHdrId(GetTaskCategoryByPmHdrIdRequest getTaskCategoryByPmHdrIdReq);
	
	

	List<TaskEntryRemarksEntity> getTaskDtlRemarksById(GetTaskRemarksByIDRequest getTaskRemarksByID);

	String TaskPercentageFlag(GetTaskPercentFlagRequest taskPercentFlag);

	String getTeHdrId(String dept, String typeCode, String typeCat, String ttHdrId, String tenantId, String masterId);

	int insertTaskEntryDtl(String teHdrId, String ttDtlId, String activityName, String plannedStartDate, String dueDate,
			String plannedCompletedDAte, String completedDate, String approvalSeq, String ApprovalStatus,
			String isCompleted, String tenantId, String assigned, String updatedBy, String requirment, String qty);

	int insertTaskEntryHdr(String ttHdrId, String masterId, String departmentCode, String taskTypeCode,
			String taskCategoryCode, String dependentTeHdrId, String empId, String tenantId, String pmHdrId);

	int updateAvgPercent(String teDtlId, String tenantId,String empId);

	List<GetTaskHdrByEmpIdEntity> getTaskHdrByEmpId(GetTaskHdrByEmpIdReq getTaskHdrByEmpIdReq);

	List<GetTaskHdrAndDtlIdEntity> deleteTaskHdrAndDtl(String teHdrId, String tenantId);

	void relaventDeleteTaskHdrAndDtl(String str, String tenantId);

	int CommanDeleteTasHdrAndDtls(String teHdrId, String tenantId);

	List<DocumentStatusMstEntity> getNextSeqandStatusByDoc(int currentSeq, String docType, String tenantId,
			String docGrp);
	
	int deleteTaskDtl(String teDtlId, String tenantId);

	String getAssignedTo(String dtlId);

	int updateSubTaskSeq(String teHdrId, String empId, String seq, String status);

	int CheckSubCount(String teDtlId, String tenantId);

	int insertTaskTypeMaster(String taskTypeCode, String taskDesc, String deptCode, String tenantId, String isActive);

	int updateTypeMaster(String taskTypeCode, String taskDesc, String deptCode, String tenantId, String isActive);

	int insertTaskCategoryMst(String taskCategoryCode, String taskCategoryDesc, String taskTypeCode, String isActive,
			String tenantID);

	int updateTaskCategoryMst(String taskCategoryCode, String taskCategoryDesc, String taskTypeCode, String isActive,
			String tenantID);
	
	int insertTaskTemplateHdr(String ttName,String ttCreatedBy,String createdOn,String ttDepartmentCode,String taskTypeCode,String taskCategoryCode,String isActive,String lastUpdatedDateTime,String lastUpdatedBy,String tenantId);

	int updateTaskTemplateHdr(String ttName,String ttDepartmentCode,String taskTypeCode,String taskCategoryCode,String isActive,String lastUpdatedDateTime,String lastUpdatedBy,String ttHdrId);

	int insertTaskTemplateDtl(String ttHdrId,String activityName,String plannedDurationDays,String isActive ,String lastUpdatedDateTime,String lastUpdatedBy,String tenantId);
	
	int updateTaskTemplateDtl(String activityName,String plannedDurationDays,String isActive ,String lastUpdatedDateTime,String lastUpdatedBy,String ttDtlId);

	int deleteTemplateHdrAndDtl(String ttDtlId, String tenantId);

	int CheckTtTempDtl(String ttHdrId, String tenantId);

	int updateTaskDtlStatusTbl(String seq, String status, String teDtlId, String tenantId, String isCompleted,
			String completedDate, String statusDesc, String remarks, String empId);
	
}
