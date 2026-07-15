package com.vmfg.mis.controller;

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
import com.vmfg.mis.services.interfaces.IAssemblyMisService;
import com.vmfg.project.request.DesignWidgetDtlReq;

@Controller
@RequestMapping("/")
public class AssemblyMisController {
	
	@Autowired
	IAssemblyMisService iAssemblyMisService;
	
	private static final Logger logger = LoggerFactory.getLogger(AssemblyMisController.class);

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAssyMisWidgetDtl")
	public ResponseEntity<ResponseAsList> getAssyMisWidgetDtl(@RequestBody DesignWidgetDtlReq designWidgetDtl ){
	
		logger.info("getAssyMisWidgetDtl  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iAssemblyMisService.getAssyMisWidgetDtl(designWidgetDtl);
		} catch (Exception ex) {
			logger.error("getAssyMisWidgetDtl  method  exception" + ex);
		}
		logger.debug("getAssyMisWidgetDtl  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskCompTime")
	public ResponseEntity<ResponseAsList> getTaskCompTime(@RequestBody DesignWidgetDtlReq designWidgetDtl ){
	
		logger.info("getTaskCompTime  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iAssemblyMisService.getTaskCompTimeResp(designWidgetDtl);
		} catch (Exception ex) {
			logger.error("getTaskCompTime  method  exception" + ex);
		}
		logger.debug("getTaskCompTime  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPojCompDtl")
	public ResponseEntity<ResponseAsList> getPojCompDtl(@RequestBody DesignWidgetDtlReq designWidgetDtl ){
	
		logger.info("getPojCompDtl  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iAssemblyMisService.getPojCompDtl(designWidgetDtl);
		} catch (Exception ex) {
			logger.error("getPojCompDtl  method  exception" + ex);
		}
		logger.debug("getPojCompDtl  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAssyTaskReport")
	public ResponseEntity<ResponseAsList> getAssyTaskReport(@RequestBody DesignWidgetDtlReq designWidgetDtl ){
	
		logger.info("getAssyTaskReport  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iAssemblyMisService.getAssyTaskReport(designWidgetDtl);
		} catch (Exception ex) {
			logger.error("getAssyTaskReport  method  exception" + ex);
		}
		logger.debug("getAssyTaskReport  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAssyDtlTaskReport")
	public ResponseEntity<ResponseAsList> getAssyDtlTaskReport(@RequestBody DesignWidgetDtlReq designWidgetDtl ){
	
		logger.info("getAssyDtlTaskReport  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iAssemblyMisService.getAssyDtlTaskReportResp(designWidgetDtl);
		} catch (Exception ex) {
			logger.error("getAssyDtlTaskReport  method  exception" + ex);
		}
		logger.debug("getAssyDtlTaskReport  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjectProgressDtls")
	public ResponseEntity<ResponseAsList> getProjectProgressDtls(@RequestBody DesignWidgetDtlReq designWidgetDtl ){
	
		logger.info("getProjectProgressDtls  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iAssemblyMisService.getProjectProgressDtls(designWidgetDtl);
		} catch (Exception ex) {
			logger.error("getProjectProgressDtls  method  exception" + ex);
		}
		logger.debug("getProjectProgressDtls  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
}