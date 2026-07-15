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
import com.vmfg.master.request.TaskTypeRequest;
import com.vmfg.master.request.insertUpdateTaskTypeRequest;
import com.vmfg.master.services.interfaces.ITaskTypeService;

@Controller
@RequestMapping("/")
public class TaskTypeController {
	private static final Logger logger = LoggerFactory.getLogger(TaskTypeController.class);
    
	@Autowired ITaskTypeService iTaskTypeService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTasktypeDtls")
	public  ResponseEntity<ResponseAsList> getTasktypeDtls (@RequestBody TaskTypeRequest taskType ){
		logger.info("getTasktypeDtls method Start");
		ResponseAsList list = new ResponseAsList();
		try {
			list = iTaskTypeService.getTasktypeDtls(taskType);
		} catch (Exception ex) {
			logger.error("getTasktypeDtls method  exception" + ex);
		}
		logger.error("getTasktypeDtls method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertUpdateTaskType")
	public ResponseEntity<ResponseAsMessage> insertUpdateTaskType(@RequestBody insertUpdateTaskTypeRequest insertDtlreq) {
		logger.info("insertUpdateTaskType method Start");
		ResponseAsMessage list = new ResponseAsMessage();
		try {
			list = iTaskTypeService.insertUpdateTaskType(insertDtlreq);
	   	} catch (Exception ex) {
			logger.error("insertUpdateTaskType  method  exception" + ex);
		}
		logger.debug("insertUpdateTaskType method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

}
