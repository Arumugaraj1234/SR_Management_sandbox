package com.vmfg.general.controller;

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

import com.vmfg.general.request.GenerateProjCodeReq;
import com.vmfg.general.request.GetUpdateProcessDtlRequest;
import com.vmfg.general.request.GetprocessEnbleStatusRequest;
import com.vmfg.general.request.GetstageprocessDtlRequest;
import com.vmfg.general.request.InitiateProcessRequest;
import com.vmfg.general.request.UpdateDueDateRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.services.interfaces.IStageManagementService;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;

@Controller
@RequestMapping("/")
public class StageManagementController {
	private static final Logger logger = LoggerFactory.getLogger(StageManagementController.class);

	@Autowired
	IStageManagementService iStageManagementService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getstageprocessDtl")
	public ResponseEntity<ResponseAsList> getstageprocessDtl(
			@RequestBody GetstageprocessDtlRequest getstageprocessDtlReq) {
		logger.debug("getstageprocessDtl  method Start");
		ResponseAsList list = null;
		try {
			list = iStageManagementService.getstageprocessDtl(getstageprocessDtlReq);
		} catch (Exception e) {
			logger.debug("getstageprocessDtl methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getprocessEnbleStatus")
	public ResponseEntity<ResponseAsList> getprocessEnbleStatus(
			@RequestBody GetprocessEnbleStatusRequest getprocessEnbleStatusReq) {
		logger.debug("getprocessEnbleStatus  method Start");
		ResponseAsList list = null;
		try {
			list = iStageManagementService.getprocessEnbleStatus(getprocessEnbleStatusReq);
		} catch (Exception e) {
			logger.debug("getprocessEnbleStatus methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getUpdateProcessDtl")
	public ResponseEntity<ResponseAsMessage> getUpdateProcessDtl(
			@RequestBody GetUpdateProcessDtlRequest getUpdateProcessDtlRequest) {
		logger.debug("getUpdateProcessDtl  method Start");
		ResponseAsMessage list = null;
		try {
			list = iStageManagementService.getUpdateProcessDtl(getUpdateProcessDtlRequest);
		} catch (Exception e) {
			logger.debug("getUpdateProcessDtl methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDefaultComponentName")
	public ResponseEntity<ResponseAsList> getDefaultComponentName(@RequestBody GetstageprocessDtlRequest req) {
		logger.debug("getDefaultComponentName  method Start");
		ResponseAsList list = null;
		try {
			list = iStageManagementService.getDefaultComponentName(req);
		} catch (Exception e) {
			logger.debug("getDefaultComponentName methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("initiateProcessFromOtherStage")
	public ResponseEntity<ResponseAsMessage> initiateProcess(@RequestBody InitiateProcessRequest initiateProcessReq) {
		logger.debug("initiateProcess  method Start");
		ResponseAsMessage list = null;
		try {
			list = iStageManagementService.initiateProcess(initiateProcessReq);
		} catch (Exception e) {
			logger.debug("initiateProcess methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("GenerateAndUpdateProjectCode")
	public ResponseEntity<ResponseAsMessage> GenerateAndUpdateProjectCode(@RequestBody GenerateProjCodeReq generateProjCodeReq) {
		logger.debug("GenerateAndUpdateProjectCode  method Start");
		ResponseAsMessage list = null;
		try {
			list = iStageManagementService.GenerateAndUpdateProjectCode(generateProjCodeReq);
		} catch (Exception e) {
			logger.debug("GenerateAndUpdateProjectCode methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("UpdateProjectDueDate")
	public ResponseEntity<ResponseAsMessage> UpdateProjectDueDate(@RequestBody UpdateDueDateRequest updateDueDateReq) {
		logger.debug("UpdateProjectDueDate  method Start");
		ResponseAsMessage list = null;
		try {
			list = iStageManagementService.UpdateProjectDueDate(updateDueDateReq);
		} catch (Exception e) {
			logger.debug("UpdateProjectDueDate methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjectDueDates")
	public ResponseEntity<ResponseAsList> getProjectDueDates(
			@RequestBody PmHdrIdAndTenantIdRequest pmHdrIdAndTenantIdReq) {
		logger.debug("getProjectDueDates  method Start");
		ResponseAsList list = null;
		try {
			list = iStageManagementService.getProjectDueDates(pmHdrIdAndTenantIdReq);
		} catch (Exception e) {
			logger.debug("getProjectDueDates methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
}
