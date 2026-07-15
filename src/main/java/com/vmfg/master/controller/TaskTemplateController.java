package com.vmfg.master.controller;

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

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.request.TaskCategoryDrpdwnRequest;
import com.vmfg.master.request.TaskTemplateInsertUpdateRequest;
import com.vmfg.master.request.TaskTemplateTypeRequest;
import com.vmfg.master.request.TaskTemplatedtlRequest;
import com.vmfg.master.request.taskTemplateHdrRequest;
import com.vmfg.master.services.interfaces.ITaskTemplateService;

@Controller
@RequestMapping("/")
public class TaskTemplateController {
	private static final Logger logger = LoggerFactory.getLogger(TaskTemplateController.class);

	@Autowired ITaskTemplateService iTaskTemplateService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskTypeTemplatedrpDwn")
	public  ResponseEntity<ResponseAsList> getTaskTypeTemplatedrpDwn (@RequestBody TaskTemplateTypeRequest req ){
		logger.info("getTaskTypeTemplatedrpDwn   method Start");
		ResponseAsList drpDown = new ResponseAsList();
		try {
			drpDown = iTaskTemplateService.getTaskTypeTemplatedrpDwn(req);
		} catch (Exception ex) {
			logger.error("getTaskTypeTemplatedrpDwn  method  exception" + ex);
		}
		logger.debug("getTaskTypeTemplatedrpDwn method end");
		return new ResponseEntity<ResponseAsList>(drpDown, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskTemplatedtl")
	public  ResponseEntity<ResponseAsList> getTaskTemplatedtl (@RequestBody TaskTemplatedtlRequest req ){
		logger.info("getTaskTemplatedtl   method Start");
		ResponseAsList list = new ResponseAsList();
		try {
			list = iTaskTemplateService.getTaskTemplatedtl(req);
		} catch (Exception ex) {
			logger.error("getTaskTemplatedtl  method  exception" + ex);
		}
		logger.debug("getTaskTemplatedtl method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskCategorydrpDwn")
	public  ResponseEntity<ResponseAsList> getTaskCategorydrpDwn (@RequestBody TaskCategoryDrpdwnRequest req ){
		logger.info("getTaskCategorydrpDwn   method Start");
		ResponseAsList drpDown = new ResponseAsList();
		try {
			drpDown = iTaskTemplateService.getTaskCategorydrpDwn(req);
		} catch (Exception ex) {
			logger.error("getTaskCategorydrpDwn  method  exception" + ex);
		}
		logger.debug("getTaskCategorydrpDwn method end");
		return new ResponseEntity<ResponseAsList>(drpDown, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertUpdateTemplate")
	public  ResponseEntity<ResponseAsMessage> insertUpdateTemplate (@RequestBody TaskTemplateInsertUpdateRequest Request ){
		logger.info("insertUpdateTemplate method Start");
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		try {

			returnMsg = iTaskTemplateService.insertUpdateTemplate(Request);

		} catch (Exception ex) {
			logger.error("insertUpdateTemplate method  exception" + ex);
		}
		logger.debug("insertUpdateTemplate method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertTemplateHdr")
	public  ResponseEntity<ResponseAsMessage> insertTemplateHdr (@RequestBody taskTemplateHdrRequest Request ){
		logger.info("insertTemplateHdr method Start");
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		try {

			returnMsg = iTaskTemplateService.insertTemplateHdr(Request);

		} catch (Exception ex) {
			logger.error("insertTemplateHdr method  exception" + ex);
		}
		logger.debug("insertTemplateHdr method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
}
