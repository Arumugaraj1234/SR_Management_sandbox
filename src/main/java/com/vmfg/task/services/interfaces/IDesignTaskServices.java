package com.vmfg.task.services.interfaces;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.task.entity.InsertTaskTemplateEntity;
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

public interface IDesignTaskServices {

	ResponseAsList getTaskTemplateHdr(TaskDtlByempIdRequest taskDtlByempIdReq);

	ResponseAsList getTaskTypeByEmp(TaskDtlByempIdRequest taskDtlByempIdReq);

	ResponseAsList getTaskCategoryByTypeCode(TaskDtlBytypeCodeRequest taskDtlBytypeCodeReq);
	
	ResponseAsList getAllTaskCategoryByTypeCode(TaskDtlBytypeCodeRequest taskDtlBytypeCodeReq);
	
	ResponseAsList getTaskEntryDtlByMstId(GetTaskEntryDtlBySlaveIdRequest taskEntryDtlBySlaveIdReq);

	ResponseAsList getTaskEntryDtlByDeptDtlId(GetTaskEntryDtlByDeptDtlIdRequest getTaskEntryDtlByDeptDtlIdReq);

	ResponseAsMessage updateTaskEntryDtl(UpdateTaskEntryDtlRequest updateTaskEntryDtlReq);

	ResponseAsMessage updateTaskDtlSeq(UpdateTaskDtlSeqRequest uspdateTaskDtlSeqreq);

	ResponseAsMessage UpdateTaskFileDtl(JSONObject jsonObj, MultipartFile file);

	ResponseAsMessage UpdateTaskDtlPtg(UpdateTaskDtlPtgRequest updateTaskDtlPtgReq);

	ResponseAsList gettemplateDtl(GettemplateDtlRequest gettemplateDtlReq);

	ResponseAsList gettemplateHdrName(GetTemplateHdrNameRequest gettemplateHdrNamereq);

	ResponseAsList gettemplateHdrName(GetTaskRemarksByIDRequest getTaskRemarksByIDRequest);

	ResponseAsMessage TaskReassignFlag(TaskReassignRequest taskReassReq);

	ResponseAsMessage TaskReassignForEmpId(TaskReassignRequest taskReassReq);

	ResponseAsList getTaskCategoryRecorded(GetTaskRecordedRequest getRecordedTask);

	ResponseAsList getTaskDtlRemarksById(GetTaskRemarksByIDRequest getTaskRemarksByID);

	ResponseAsMessage TaskPercentageFlag(GetTaskPercentFlagRequest taskPercentFlag);

	ResponseAsMessage TaskPercentageUpdate(GetTaskPercentFlagRequest taskpercent);

	ResponseAsList getTaskHdrByEmpId(GetTaskHdrByEmpIdReq getTaskHdrByEmpIdReq);

	ResponseAsMessage deleteTaskHdrAndDtl(DeleteTaskHdrAndDtlReq deleteTaskHdrAndDtlReq);
	
	ResponseAsList getTaskCategoryByPmHdrId(GetTaskCategoryByPmHdrIdRequest getTaskCategoryByPmHdrIdReq);

	ResponseAsMessage insertTypeMst(TaskTypeMasterReq taskTypeMasterReq);

	ResponseAsMessage insertTaskCategoryMst(TaskCategoryMasterReq taskCategoryMasterReq);
	
	ResponseAsMessage updateTaskTemplate(InsertTaskTemplateEntity insertTaskTemplateReq);
	
	ResponseAsList getAllTaskType(GetAllTaskTypeRequest getAllTaskTypeReq);

	ResponseAsMessage deleteTemplateHdrAndDtl(DeleteTemplateHdrAndDtlReq deletereq);
}
