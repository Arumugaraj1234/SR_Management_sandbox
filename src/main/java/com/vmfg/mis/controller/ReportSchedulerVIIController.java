package com.vmfg.mis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.services.interfaces.iReportSchedulerVIIService;
@Controller
@RequestMapping("/")
public class ReportSchedulerVIIController {
	private static final Logger logger = LoggerFactory.getLogger(ReportSchedulerVIIController.class);
	@Autowired 
	iReportSchedulerVIIService ireportSchedulerService;
	
//	@Scheduled(cron = "0 * */2 * * *")
	@CrossOrigin(maxAge = 3600)
	@GetMapping("populateMaterialReports")
	public ResponseEntity<ResponseAsMessage> populateMaterialReports() {
		logger.info("populateMaterialReports method Start");
		ResponseAsMessage list = null;
		try {
			list = ireportSchedulerService.populateMaterialReports();
		} catch (Exception ex) {
			logger.error("populateMaterialReports  method  exception" + ex);
		}
		logger.debug("populateMaterialReports method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	
//	@Scheduled(cron = "* 5 0 * * *")
	@CrossOrigin(maxAge = 3600)
	@GetMapping("NotifyScmForVendor")
	public ResponseEntity<ResponseAsMessage> NotifyScmForVendor() {
		logger.info("NotifyScmForVendor method Start");
		ResponseAsMessage list = null;
		try {
			list = ireportSchedulerService.NotifyScmForVendor();
		} catch (Exception ex) {
			logger.error("NotifyScmForVendor  method  exception" + ex);
		}
		logger.info("NotifyScmForVendor method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
}
