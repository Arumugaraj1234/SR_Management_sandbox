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
import com.vmfg.master.request.TaskCategoryInsertUpdateRequest;
import com.vmfg.master.request.TaskCategoryRequest;
import com.vmfg.master.request.TenantIdRequest;
import com.vmfg.master.services.interfaces.ITaskCategoryService;

@Controller
@RequestMapping("/")
public class TaskCategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskCategoryController.class);
	
	@Autowired
	private ITaskCategoryService iTaskCategoryservice;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskTypeDropDownIsActive")
	public  ResponseEntity<ResponseAsList> getTaskTypeDropDownIsActive (@RequestBody TenantIdRequest tenantIdRequest ){
		logger.info("getTaskTypeDropDownIsActive method Start");
		ResponseAsList responseList = new ResponseAsList();
		try {

			responseList = iTaskCategoryservice.getTaskTypeDropDownIsActive(tenantIdRequest);

		} catch (Exception ex) {
			logger.error("getTaskTypeDropDownIsActive method  exception" + ex);
		}
		logger.debug("getTaskTypeDropDownIsActive method end");
		return new ResponseEntity<ResponseAsList>(responseList, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskCategory")
	public  ResponseEntity<ResponseAsList> getTaskCategory (@RequestBody TaskCategoryRequest taskCategoryrequest ){
		logger.info("getTaskCategory method Start");
		ResponseAsList responseList = new ResponseAsList();
		try {

			responseList = iTaskCategoryservice.getTaskCategory(taskCategoryrequest);

		} catch (Exception ex) {
			logger.error("getTaskCategory method  exception" + ex);
		}
		logger.debug("getTaskCategory method end");
		return new ResponseEntity<ResponseAsList>(responseList, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertandupdatetaskCat")
	public  ResponseEntity<ResponseAsMessage> insertandUpdateTaskCat (@RequestBody TaskCategoryInsertUpdateRequest taskCategoryInsertUpdateRequest ){
		logger.info("getTaskCategory method Start");
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		try {

			returnMsg = iTaskCategoryservice.insertandUpdateTaskCat(taskCategoryInsertUpdateRequest);

		} catch (Exception ex) {
			logger.error("getTaskCategory method  exception" + ex);
		}
		logger.debug("getTaskCategory method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}

}
