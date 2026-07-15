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
import com.vmfg.mis.request.PMMileRequest;
import com.vmfg.mis.request.PMWidgetRequest;
import com.vmfg.mis.services.interfaces.IPMMisService;

@Controller
@RequestMapping("/")
public class PMMisController {
	private static final Logger logger = LoggerFactory.getLogger(PMMisController.class);

	@Autowired
	IPMMisService iPMMisService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPMWidgetDtl")
	public ResponseEntity<ResponseAsList> getPMWidgetDtl(@RequestBody PMWidgetRequest PMWidgetReq) {

		logger.debug("getPMWidgetDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iPMMisService.getPMWidgetDtl(PMWidgetReq);

		} catch (Exception ex) {
			logger.error("getPMWidgetDtl  method  exception" + ex);
		}
		logger.debug("getPMWidgetDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPMBySBCType") // bar chart to return plan vs target vs actual
	public ResponseEntity<ResponseAsList> getPMBySBCType(@RequestBody PMWidgetRequest PMWidgetReq) {

		logger.debug("getPMBySBCType   method Start");
		ResponseAsList resp = null;
		try {

			resp = iPMMisService.getPMBySBCType(PMWidgetReq);

		} catch (Exception ex) {
			logger.error("getPMBySBCType  method  exception" + ex);
		}
		logger.debug("getPMBySBCType   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPMMilestoneByMonthYr") // table view
	public ResponseEntity<ResponseAsList> getPMMilestoneByMonthYr(@RequestBody PMMileRequest PMMileReq) {

		logger.debug("getPMMilestoneByMonthYr   method Start");
		ResponseAsList resp = null;
		try {

			resp = iPMMisService.getPMMilestoneByMonthYr(PMMileReq);

		} catch (Exception ex) {
			logger.error("getPMMilestoneByMonthYr  method  exception" + ex);
		}
		logger.debug("getPMMilestoneByMonthYr   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPMReportTracker") // drill down pop up
	public ResponseEntity<ResponseAsList> getPMReportTracker(@RequestBody PMWidgetRequest PMWidgetReq) {

		logger.debug("getPMReportTracker   method Start");
		ResponseAsList resp = null;
		try {

			resp = iPMMisService.getPMReportTracker(PMWidgetReq);

		} catch (Exception ex) {
			logger.error("getPMReportTracker  method  exception" + ex);
		}
		logger.debug("getPMReportTracker   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPMWorkLoad") // work load table 
	public ResponseEntity<ResponseAsList> getPMWorkLoad(@RequestBody PMWidgetRequest PMWidgetReq) {

		logger.debug("getPMWorkLoad   method Start");
		ResponseAsList resp = null;
		try {

			resp = iPMMisService.getPMWorkLoad(PMWidgetReq);

		} catch (Exception ex) {
			logger.error("getPMWorkLoad  method  exception" + ex);
		}
		logger.debug("getPMWorkLoad   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	
}
