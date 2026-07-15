package com.vmfg.export.controller;

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

import com.vmfg.export.request.IdAndTenantIdRequest;
import com.vmfg.export.request.ProjectTrackerReportRequest;
import com.vmfg.export.services.interfaces.IProjectTrackerReportService;
import com.vmfg.finance.request.getPraDtlRequest;
import com.vmfg.export.response.ResponseAsList;

@Controller
@RequestMapping("/")
public class ProjectTrackerReportController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectTrackerReportController.class);

	@Autowired
	private IProjectTrackerReportService iProjectTrackerReportService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjectTrackerReportPDF")
	public ResponseEntity<ResponseAsList> getProjectTrackerReportPDF(@RequestBody ProjectTrackerReportRequest designReq) {
		logger.info("getProjectTrackerReportPDF   method Start");
		ResponseAsList resp = null;
		try {
			resp = iProjectTrackerReportService.getProjectTrackerReportPDF(designReq);
		} catch (Exception ex) {
			logger.error("getProjectTrackerReportPDF  method  exception" + ex);
		}
		logger.info("getProjectTrackerReportPDF   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
		
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPoDtlsByPoIdReportPDF")
	public ResponseEntity<ResponseAsList> getPoDtlsByPoIdReportPDF(@RequestBody IdAndTenantIdRequest IdAndTenantIdReq) {
		logger.debug("getPoDtlsByPoIdReportPDF   method Start");
		ResponseAsList list=null;
		try {
			list = iProjectTrackerReportService.getPoDtlsByPoIdReportPDF(IdAndTenantIdReq);
		} catch (Exception ex) {
			logger.error("getPoDtlsByPoIdReportPDF  method  exception" + ex);
		}
		logger.info("getPoDtlsByPoIdReportPDF   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

@CrossOrigin(maxAge = 3600)
@PostMapping("getPraReportByPraId")
public ResponseEntity<ResponseAsList>  getPraReportByPraId(@RequestBody getPraDtlRequest PraIdAndTenantIdReq) {
	logger.debug("getPraReportByPraId method Start");
	ResponseAsList list=null;
	try {
		list = iProjectTrackerReportService.getPraReportByPraId(PraIdAndTenantIdReq);
	} catch (Exception ex) {
		logger.error("getPraReportByPraId method exception" + ex);
     }
	logger.info("getPraReportByPraId method end");
	return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);

}
}
