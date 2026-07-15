package com.vmfg.task.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.sales.controller.EnquiryController;
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
import com.vmfg.task.services.interfaces.IDesignTaskServices;

@Controller
@RequestMapping("/")
public class DesignTaskController {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryController.class);

	@Autowired
	IDesignTaskServices iDesignTaskServices;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskTemplateHdr")
	public ResponseEntity<ResponseAsList> getTaskTemplateHdr(@RequestBody TaskDtlByempIdRequest taskDtlByempIdReq) {
		logger.debug("getTaskTemplateHdr   method Start");
		ResponseAsList taskTemplateHdr = null;
		try {

			taskTemplateHdr = iDesignTaskServices.getTaskTemplateHdr(taskDtlByempIdReq);

		} catch (Exception ex) {
			logger.error("getTaskTemplateHdr  method  exception" + ex);
		}
		logger.debug("getTaskTemplateHdr   method end");
		return new ResponseEntity<ResponseAsList>(taskTemplateHdr, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskTypeByEmp")
	public ResponseEntity<ResponseAsList> getTaskTypeByEmp(@RequestBody TaskDtlByempIdRequest taskDtlByempIdReq) {
		logger.debug("getTaskTypeByEmp   method Start");
		ResponseAsList taskTypeByEmp = null;
		try {

			taskTypeByEmp = iDesignTaskServices.getTaskTypeByEmp(taskDtlByempIdReq);

		} catch (Exception ex) {
			logger.error("getTaskTypeByEmp  method  exception" + ex);
		}
		logger.debug("getTaskTemplateHdr   method end");
		return new ResponseEntity<ResponseAsList>(taskTypeByEmp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllTaskType")
	public ResponseEntity<ResponseAsList> getAllTaskType(@RequestBody GetAllTaskTypeRequest getAllTaskTypeReq) {
		logger.debug("getAllTaskType   method Start");
		ResponseAsList allTaskType = null;
		try {

			allTaskType = iDesignTaskServices.getAllTaskType(getAllTaskTypeReq);

		} catch (Exception ex) {
			logger.error("getAllTaskType  method  exception" + ex);
		}
		logger.debug("getAllTaskType   method end");
		return new ResponseEntity<ResponseAsList>(allTaskType, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskCategoryByTypeCode")
	public ResponseEntity<ResponseAsList> getTaskCategoryByTypeCode(
			@RequestBody TaskDtlBytypeCodeRequest taskDtlBytypeCodeReq) {
		logger.debug("getTaskCategoryByTypeCode   method Start");
		ResponseAsList taskCategoryByTypeCode = null;
		try {

			taskCategoryByTypeCode = iDesignTaskServices.getTaskCategoryByTypeCode(taskDtlBytypeCodeReq);

		} catch (Exception ex) {
			logger.error("getTaskCategoryByTypeCode  method  exception" + ex);
		}
		logger.debug("getTaskCategoryByTypeCode   method end");
		return new ResponseEntity<ResponseAsList>(taskCategoryByTypeCode, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllTaskCategoryByTypeCode")
	public ResponseEntity<ResponseAsList> getAllTaskCategoryByTypeCode(
			@RequestBody TaskDtlBytypeCodeRequest taskDtlBytypeCodeReq) {
		logger.debug("getAllTaskCategoryByTypeCode   method Start");
		ResponseAsList taskCategoryByTypeCode = null;
		try {

			taskCategoryByTypeCode = iDesignTaskServices.getAllTaskCategoryByTypeCode(taskDtlBytypeCodeReq);

		} catch (Exception ex) {
			logger.error("getAllTaskCategoryByTypeCode  method  exception" + ex);
		}
		logger.debug("getAllTaskCategoryByTypeCode   method end");
		return new ResponseEntity<ResponseAsList>(taskCategoryByTypeCode, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskEntryDtlByMstId")
	public ResponseEntity<ResponseAsList> getTaskEntryDtlByMstId(
			@RequestBody GetTaskEntryDtlBySlaveIdRequest taskEntryDtlBySlaveIdReq) {
		logger.debug("getTaskEntryDtlByMstId   method Start");
		ResponseAsList taskEntryDtlBySlaveId = null;
		try {

			taskEntryDtlBySlaveId = iDesignTaskServices.getTaskEntryDtlByMstId(taskEntryDtlBySlaveIdReq);

		} catch (Exception ex) {
			logger.error("getTaskEntryDtlByMstId  method  exception" + ex);
		}
		logger.debug("getTaskEntryDtlByMstId   method end");
		return new ResponseEntity<ResponseAsList>(taskEntryDtlBySlaveId, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskEntryDtlByDeptDtlId")
	public ResponseEntity<ResponseAsList> getTaskEntryDtlByDeptDtlId(
			@RequestBody GetTaskEntryDtlByDeptDtlIdRequest taskEntryDtlByDeptDtlIdReq) {
		logger.debug("getTaskEntryDtlByDeptDtlId   method Start");
		ResponseAsList taskEntryDtlByDeptDtlId = null;
		try {
			
			taskEntryDtlByDeptDtlId = iDesignTaskServices.getTaskEntryDtlByDeptDtlId(taskEntryDtlByDeptDtlIdReq);

		} catch (Exception ex) {
			logger.error("getTaskEntryDtlByDeptDtlId  method  exception" + ex);
		}
		logger.debug("getTaskEntryDtlByDeptDtlId   method end");
		return new ResponseEntity<ResponseAsList>(taskEntryDtlByDeptDtlId, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateTaskEntryDtl")
	public ResponseEntity<ResponseAsMessage> updateTaskEntryDtl(
			@RequestBody UpdateTaskEntryDtlRequest updateTaskEntryDtlReq) {
		logger.debug("updateTaskEntryDtl   method Start");
		ResponseAsMessage departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDesignTaskServices.updateTaskEntryDtl(updateTaskEntryDtlReq);

		} catch (Exception ex) {
			logger.error("updateTaskEntryDtl  method  exception" + ex);
		}
		logger.debug("updateTaskEntryDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("UpdateTaskDtlSeq")
	public ResponseEntity<ResponseAsMessage> UpdateTaskDtlSeq(
			@RequestBody UpdateTaskDtlSeqRequest UpdateTaskDtlSeqreq) {
		logger.debug("UpdateTaskDtlSeq   method Start");
		ResponseAsMessage updateTaskDtl = null;
		try {

			updateTaskDtl = iDesignTaskServices.updateTaskDtlSeq(UpdateTaskDtlSeqreq);

		} catch (Exception ex) {
			logger.error("UpdateTaskDtlSeq  method  exception" + ex);
		}
		logger.debug("UpdateTaskDtlSeq   method end");
		return new ResponseEntity<ResponseAsMessage>(updateTaskDtl, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("UpdateTaskFileDtl")
	public ResponseEntity<ResponseAsMessage> UpdateTaskFileDtl(@RequestParam("maininfo") String maininfo,
			@RequestParam("file") MultipartFile file) {
		logger.debug("UpdateTaskFileDtl   method Start");
		ResponseAsMessage departmentInfoEntity = null;
		try {
			JSONObject jsonObj = new JSONObject(maininfo);

			departmentInfoEntity = iDesignTaskServices.UpdateTaskFileDtl(jsonObj, file);

		} catch (Exception ex) {
			logger.error("UpdateTaskFileDtl  method  exception" + ex);
		}
		logger.debug("UpdateTaskFileDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("UpdateTaskDtlPtg")
	public ResponseEntity<ResponseAsMessage> UpdateTaskDtlPtg(
			@RequestBody UpdateTaskDtlPtgRequest updateTaskDtlPtgReq) {
		logger.debug("UpdateTaskDtlPtg   method Start");
		ResponseAsMessage departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDesignTaskServices.UpdateTaskDtlPtg(updateTaskDtlPtgReq);

		} catch (Exception ex) {
			logger.error("UpdateTaskDtlPtg  method  exception" + ex);
		}
		logger.debug("UpdateTaskDtlPtg   method end");
		return new ResponseEntity<ResponseAsMessage>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("gettemplateDtl")
	public ResponseEntity<ResponseAsList> gettemplateDtl(@RequestBody GettemplateDtlRequest gettemplateDtlReq) {
		logger.debug("gettemplateDtl   method Start");
		ResponseAsList departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDesignTaskServices.gettemplateDtl(gettemplateDtlReq);

		} catch (Exception ex) {
			logger.error("gettemplateDtl  method  exception" + ex);
		}
		logger.debug("gettemplateDtl   method end");
		return new ResponseEntity<ResponseAsList>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("gettemplateHdrName")
	public ResponseEntity<ResponseAsList> gettemplateHdrName(
			@RequestBody GetTemplateHdrNameRequest getTemplateHdrNameReq) {
		logger.debug("gettemplateHdrName   method Start");
		ResponseAsList departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDesignTaskServices.gettemplateHdrName(getTemplateHdrNameReq);

		} catch (Exception ex) {
			logger.error("gettemplateHdrName  method  exception" + ex);
		}
		logger.debug("gettemplateHdrName   method end");
		return new ResponseEntity<ResponseAsList>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskRemarksById")
	public ResponseEntity<ResponseAsList> getTaskRemarksById(
			@RequestBody GetTaskRemarksByIDRequest getTaskRemarksByIDRequest) {
		logger.debug("getTaskRemarksById   method Start");
		ResponseAsList taskResp = null;
		try {

			taskResp = iDesignTaskServices.gettemplateHdrName(getTaskRemarksByIDRequest);

		} catch (Exception ex) {
			logger.error("getTaskRemarksById  method  exception" + ex);
		}
		logger.debug("getTaskRemarksById   method end");
		return new ResponseEntity<ResponseAsList>(taskResp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("TaskReassignFlag")
	public ResponseEntity<ResponseAsMessage> TaskReassignFlag(@RequestBody TaskReassignRequest taskReassReq) {
		logger.debug("TaskReassignFlag   method Start");
		ResponseAsMessage departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDesignTaskServices.TaskReassignFlag(taskReassReq);

		} catch (Exception ex) {
			logger.error("TaskReassignFlag  method  exception" + ex);
		}
		logger.debug("TaskReassignFlag   method end");
		return new ResponseEntity<ResponseAsMessage>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("TaskReassignForEmpId")
	public ResponseEntity<ResponseAsMessage> TaskReassignForEmpId(@RequestBody TaskReassignRequest taskReassReq) {
		logger.debug("TaskReassignForEmpId   method Start");
		ResponseAsMessage departmentInfoEntity = null;
		try {

			departmentInfoEntity = iDesignTaskServices.TaskReassignForEmpId(taskReassReq);

		} catch (Exception ex) {
			logger.error("TaskReassignForEmpId  method  exception" + ex);
		}
		logger.debug("TaskReassignForEmpId   method end");
		return new ResponseEntity<ResponseAsMessage>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskCategoryRecorded")
	public ResponseEntity<ResponseAsList> getTaskCategoryRecorded(@RequestBody GetTaskRecordedRequest getRecordedTask) {
		logger.debug("getTaskCategoryRecorded   method Start");
		ResponseAsList taskResp = null;
		try {

			taskResp = iDesignTaskServices.getTaskCategoryRecorded(getRecordedTask);

		} catch (Exception ex) {
			logger.error("getTaskRemarksById  method  exception" + ex);
		}
		logger.debug("getTaskRemarksById   method end");
		return new ResponseEntity<ResponseAsList>(taskResp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskDtlRemarksById")
	public ResponseEntity<ResponseAsList> getTaskDtlRemarksById(
			@RequestBody GetTaskRemarksByIDRequest getTaskRemarksByID) {
		logger.debug("getTaskRemarksById   method Start");
		ResponseAsList taskResp = null;
		try {

			taskResp = iDesignTaskServices.getTaskDtlRemarksById(getTaskRemarksByID);

		} catch (Exception ex) {
			logger.error("getTaskRemarksById  method  exception" + ex);
		}
		logger.debug("getTaskRemarksById   method end");
		return new ResponseEntity<ResponseAsList>(taskResp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("TaskPercentageFlag")
	public ResponseEntity<ResponseAsMessage> TaskPercentageFlag(
			@RequestBody GetTaskPercentFlagRequest taskPercentFlag) {
		logger.debug("TaskPercentageFlag   method Start");
		ResponseAsMessage TaskPercentageFlag = null;
		try {

			TaskPercentageFlag = iDesignTaskServices.TaskPercentageFlag(taskPercentFlag);

		} catch (Exception ex) {
			logger.error("TaskPercentageFlag  method  exception" + ex);
		}
		logger.debug("TaskPercentageFlag   method end");
		return new ResponseEntity<ResponseAsMessage>(TaskPercentageFlag, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("TaskPercentageUpdate")
	public ResponseEntity<ResponseAsMessage> TaskPercentageUpdate(
			@RequestBody GetTaskPercentFlagRequest taskpercent) {
		logger.info("TaskPercentageUpdate   method Start");
		ResponseAsMessage TaskPercentageUpd = null;
		try {

			TaskPercentageUpd = iDesignTaskServices.TaskPercentageUpdate(taskpercent);

		} catch (Exception ex) {
			logger.error("TaskPercentageUpdate  method  exception" + ex);
		}
		logger.info("TaskPercentageUpdate   method end");
		return new ResponseEntity<ResponseAsMessage>(TaskPercentageUpd, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskHdrByEmpId")
	public ResponseEntity<ResponseAsList> getTaskHdrByEmpId(
			@RequestBody GetTaskHdrByEmpIdReq getTaskHdrByEmpIdReq) {
		logger.debug("getTaskHdrByEmpId   method Start");
		ResponseAsList taskResp = null;
		try {

			taskResp = iDesignTaskServices.getTaskHdrByEmpId(getTaskHdrByEmpIdReq);

		} catch (Exception ex) {
			logger.error("getTaskHdrByEmpId  method  exception" + ex);
		}
		logger.debug("getTaskHdrByEmpId   method end");
		return new ResponseEntity<ResponseAsList>(taskResp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskCategoryByPmHdrId")
	public ResponseEntity<ResponseAsList> getTaskCategoryByPmHdrId(
			@RequestBody GetTaskCategoryByPmHdrIdRequest getTaskCatByPmHdrIdReq) {
		logger.debug("getTaskCategoryByPmHdrId   method Start");
		ResponseAsList taskResp = null;
		try {

			taskResp = iDesignTaskServices.getTaskCategoryByPmHdrId(getTaskCatByPmHdrIdReq);

		} catch (Exception ex) {
			logger.error("getTaskCategoryByPmHdrId  method  exception" + ex);
		}
		logger.debug("getTaskCategoryByPmHdrId   method end");
		return new ResponseEntity<ResponseAsList>(taskResp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteTaskHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> deleteTaskHdrAndDtl(
			@RequestBody DeleteTaskHdrAndDtlReq deleteTaskHdrAndDtlReq) {
		logger.info("deleteTaskHdrAndDtl   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iDesignTaskServices.deleteTaskHdrAndDtl(deleteTaskHdrAndDtlReq);

		} catch (Exception ex) {
			logger.error("deleteTaskHdrAndDtl  method  exception" + ex);
		}
		logger.info("deleteTaskHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertTaskTypeMst")
	public ResponseEntity<ResponseAsMessage> insertTypeMst(
			@RequestBody TaskTypeMasterReq taskTypeMasterReq) {
		logger.info("insertTypeMst   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iDesignTaskServices.insertTypeMst(taskTypeMasterReq);
		} catch (Exception ex) {
			logger.error("insertTypeMst  method  exception" + ex);
		}
		logger.info("insertTypeMst   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertTaskCategoryMst")
	public ResponseEntity<ResponseAsMessage> insertTaskCategoryMst(
			@RequestBody TaskCategoryMasterReq taskCategoryMasterReq) {
		logger.info("insertTaskCategoryMst   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iDesignTaskServices.insertTaskCategoryMst(taskCategoryMasterReq);
		} catch (Exception ex) {
			logger.error("insertTaskCategoryMst  method  exception" + ex);
		}
		logger.info("insertTaskCategoryMst   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateTaskTemplate")
	public ResponseEntity<ResponseAsMessage> updateTaskTemplate(
			@RequestBody InsertTaskTemplateEntity insertTaskTemplateReq) {
		logger.info("updateTaskTemplate   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iDesignTaskServices.updateTaskTemplate(insertTaskTemplateReq);
		} catch (Exception ex) {
			logger.error("updateTaskTemplate  method  exception" + ex);
		}
		logger.info("updateTaskTemplate   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteTemplateHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> deleteTemplateHdrAndDtl(
			@RequestBody DeleteTemplateHdrAndDtlReq deletereq) {

		logger.debug("deleteTemplateHdrAndDtl method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iDesignTaskServices.deleteTemplateHdrAndDtl(deletereq);

		} catch (Exception ex) {
			logger.error("deleteTemplateHdrAndDtl method exception" + ex);
		}
		logger.debug("deleteTemplateHdrAndDtl method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	
}
