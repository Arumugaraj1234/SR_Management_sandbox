package com.vmfg.master.controller;

import java.util.List;

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

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.request.ProjectInitiationUpdateRequest;
import com.vmfg.master.services.interfaces.IProjectInitiationMasterService;

@Controller
@RequestMapping("/")
public class ProjectInitiationMasterController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectInitiationMasterController.class);
	@Autowired
	IProjectInitiationMasterService iProjectInitiationMasterService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjectInitiationDtl")
	public ResponseEntity<ResponseAsList> getProjectInitiationDtl(@RequestBody TenantRequest TenantRequestReq) {
		logger.info("getProjectInitiationDtl method Start");
		ResponseAsList list = null;
		try {
			list = iProjectInitiationMasterService.getProjectInitiationDtl(TenantRequestReq);
		} catch (Exception ex) {
			logger.error("getProjectInitiationDtl  method  exception" + ex);
		}
		logger.debug("getProjectInitiationDtl method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateProjectIntiationMaster")
	public ResponseEntity<ResponseAsMessage> updateProjectIntiationMaster(@RequestBody List<ProjectInitiationUpdateRequest> ProjectInitiationUpdateReq) {
		logger.info("updateProjectIntiationMaster method Start");
		ResponseAsMessage msg = null;
		try {
			msg = iProjectInitiationMasterService.updateProjectIntiationMaster(ProjectInitiationUpdateReq);
		} catch (Exception ex) {
			logger.error("updateProjectIntiationMaster  method  exception" + ex);
		}
		logger.debug("updateProjectIntiationMaster method end");
		return new ResponseEntity<ResponseAsMessage>(msg, HttpStatus.OK);
	}
	
	
	
}
