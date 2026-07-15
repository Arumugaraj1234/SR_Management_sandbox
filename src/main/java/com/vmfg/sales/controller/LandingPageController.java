package com.vmfg.sales.controller;

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
import com.vmfg.sales.request.GetEnqDtlbyDateRequest;
import com.vmfg.sales.request.GetEnqDtlbySlaveIdRequest;
import com.vmfg.sales.services.interfaces.ILandingPageService;

@Controller
@RequestMapping("/")
public class LandingPageController {
	private static final Logger logger = LoggerFactory.getLogger(LandingPageController.class);

	@Autowired
	ILandingPageService iLandingPageService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEnqDtlbyDate")
	public ResponseEntity<ResponseAsList> getEnqDtlbyDate(@RequestBody GetEnqDtlbyDateRequest getEnqDtlbyDateRequest ) {
		logger.debug("getEnqDtlbyDate  method Start");
	ResponseAsList list = null;
		try {
			list = iLandingPageService.getEnqDtlbyDate(getEnqDtlbyDateRequest);
		} catch (Exception e) {
			logger.debug("getEnqDtlbyDate methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}@CrossOrigin(maxAge = 3600)
	@PostMapping("getEnqDtlbySlaveId")
	public ResponseEntity<ResponseAsList> getEnqDtlbySlaveId(@RequestBody GetEnqDtlbySlaveIdRequest getEnqDtlbySlaveIdReq ) {
		logger.debug("getEnqDtlbySlaveId  method Start");
	ResponseAsList list = null;
		try {
			list = iLandingPageService.getEnqDtlbySlaveId(getEnqDtlbySlaveIdReq);
		} catch (Exception e) {
			logger.debug("getEnqDtlbySlaveId methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
}
