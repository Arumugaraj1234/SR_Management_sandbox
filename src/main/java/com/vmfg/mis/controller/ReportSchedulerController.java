package com.vmfg.mis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.services.interfaces.iReportSchedulerService;
@Controller
@RequestMapping("/")
public class ReportSchedulerController {
	private static final Logger logger = LoggerFactory.getLogger(ReportSchedulerController.class);
	@Autowired 
	iReportSchedulerService ireportSchedulerService;
	
//	@Scheduled(cron = "0 5 0 * * *")
	@CrossOrigin(maxAge = 3600)
	@GetMapping("insertReportDtls")
	public ResponseEntity<ResponseAsMessage> insertReportDtls() {
		logger.info("insertReportDtls method Start");
		ResponseAsMessage list = null;
		try {
			list = ireportSchedulerService.insertReportDtls();
		} catch (Exception ex) {
			logger.error("insertReportDtls  method  exception" + ex);
		}
		logger.debug("insertReportDtls method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
}
