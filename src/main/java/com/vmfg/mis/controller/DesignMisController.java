package com.vmfg.mis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.request.DesignMisRequest;
import com.vmfg.mis.request.DesignReportMisRequest;
import com.vmfg.mis.services.interfaces.IDesignMisService;
@Controller
@RequestMapping("/")
public class DesignMisController {

	private static final Logger logger = LoggerFactory.getLogger(DesignMisController.class);

	@Autowired
	IDesignMisService iDesignMisService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDesignWidgetDtl")
	public ResponseEntity<ResponseAsList> getDesignWidgetDtl(@RequestBody DesignMisRequest designMisReq) {
		logger.debug("getDesignWidgetDtl   method Start");
		ResponseAsList resp = null;
		try {
			resp = iDesignMisService.getDesignWidgetDtl(designMisReq);
		} catch (Exception ex) {
			logger.error("getDesignWidgetDtl  method  exception" + ex);
		}
		logger.debug("getDesignWidgetDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDesignWidgetDtlByCategory")
	public ResponseEntity<ResponseAsList> getDesignWidgetDtlByCategory(@RequestBody DesignMisRequest designMisReq) {
		logger.debug("getDesignWidgetDtlByCategory   method Start");
		ResponseAsList resp = null;
		try {
			resp = iDesignMisService.getDesignWidgetDtlByCategory(designMisReq);
		} catch (Exception ex) {
			logger.error("getDesignWidgetDtlByCategory  method  exception" + ex);
		}
		logger.debug("getDesignWidgetDtlByCategory   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPlannedProject")
	public ResponseEntity<ResponseAsList> getPlannedProject(@RequestBody DesignMisRequest designMisReq) {
		logger.debug("getPlannedProject  method Start");
		ResponseAsList resp = null;
		try {
			resp = iDesignMisService.getPlannedProject(designMisReq);
		} catch (Exception ex) {
			logger.error("getPlannedProject  method  exception" + ex);
		}
		logger.debug("getPlannedProject  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPlannedActivity")
	public ResponseEntity<ResponseAsList> getPlannedActivity(@RequestBody DesignMisRequest designMisReq) {
		logger.debug("getPlannedActivity  method Start");
		ResponseAsList resp = null;
		try {
			resp = iDesignMisService.getPlannedActivity(designMisReq);
		} catch (Exception ex) {
			logger.error("getPlannedActivity  method  exception" + ex);
		}
		logger.debug("getPlannedActivity  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskCompPerBymonth")
	public ResponseEntity<ResponseAsList> getTaskCompPerBymonth(@RequestBody DesignMisRequest designMisReq) {
		logger.debug("getTaskCompPer  method Start");
		ResponseAsList resp = null;
		try {
			resp = iDesignMisService.getTaskCompPerBymonth(designMisReq);
		} catch (Exception ex) {
			logger.error("getTaskCompPerBymonth  method  exception" + ex);
		}
		logger.debug("getTaskCompPerBymonth  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTaskCompPerByYear")
	public ResponseEntity<ResponseAsList> getTaskCompPerByYear(@RequestBody DesignReportMisRequest designMisReq) {
		logger.debug("getTaskCompPerByYear  method Start");
		ResponseAsList resp = null;
		try {
			resp = iDesignMisService.getTaskCompPerByYear(designMisReq);
		} catch (Exception ex) {
			logger.error("getTaskCompPerByYear  method  exception" + ex);
		}
		logger.debug("getTaskCompPerByYear  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTeamMemberEnableCheck")
	public ResponseEntity<ResponseAsMessage> getTeamMemberEnableCheck(@RequestBody DesignReportMisRequest designMisReq) {
		logger.debug("getTeamMemberEnableCheck  method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iDesignMisService.getTeamMemberEnableCheck(designMisReq);
		} catch (Exception ex) {
			logger.error("getTeamMemberEnableCheck  method  exception" + ex);
		}
		logger.debug("getTeamMemberEnableCheck  method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	
//	@Scheduled(cron = "0 10 0 * * *")
	@CrossOrigin(maxAge = 3600)
	@GetMapping("updateReportTaskMonth")
	public ResponseEntity<ResponseAsMessage> updateReportTaskMonth() {
		logger.debug("updateReportTaskMonth  method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iDesignMisService.updateReportTaskMonth();
		} catch (Exception ex) {
			logger.error("updateReportTaskMonth  method  exception" + ex);
		}
		logger.debug("updateReportTaskMonth  method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
//	@Scheduled(cron = "0 25 0 * * *")
	@CrossOrigin(maxAge = 3600)
	@GetMapping("updateReportTaskWeek")
	public ResponseEntity<ResponseAsMessage> updateReportTaskWeek() {
		logger.debug("updateReportTaskWeek  method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iDesignMisService.updateReportTaskWeek();
		} catch (Exception ex) {
			logger.error("updateReportTaskWeek  method  exception" + ex);
		}
		logger.debug("updateReportTaskWeek  method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
}
