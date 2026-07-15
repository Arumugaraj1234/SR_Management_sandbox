package com.vmfg.timesheet.controller;

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
import com.vmfg.timesheet.request.DeleteActivityforEmpIdRequest;
import com.vmfg.timesheet.request.DeleteTimeSheetTaskDtlRequest;
import com.vmfg.timesheet.request.GetAllTimeSheetTaskForHdrRequest;
import com.vmfg.timesheet.request.GetEmpIDinTimesheetRequest;
import com.vmfg.timesheet.request.GetTimeDtlByHdrRequest;
import com.vmfg.timesheet.request.GetTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.GetTimeSheetDtlRequest;
import com.vmfg.timesheet.request.GetTimeSheetTaskForHdrandDtlRequset;
import com.vmfg.timesheet.request.InsertIndividualTimeSheetTaskDtl;
import com.vmfg.timesheet.request.InsertTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.InsertTimeSheetRequest;
import com.vmfg.timesheet.request.InsertTimeSheetTaskRequest;
import com.vmfg.timesheet.request.TaskEntryHdrAndDtl;
import com.vmfg.timesheet.request.TimeSheetRemainingLogRequest;
import com.vmfg.timesheet.request.TimeSheetRequests;
import com.vmfg.timesheet.request.UpdateTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.UpdateTimeSheetTaskHdrGroupName;
import com.vmfg.timesheet.request.UpdateTimeSheetTaskRequest;
import com.vmfg.timesheet.services.interfaces.ITimeSheetServices;

@Controller
@RequestMapping("/")
public class TimeSheetController {
	private static final Logger logger = LoggerFactory.getLogger(TimeSheetController.class);

	@Autowired
	ITimeSheetServices iTimeSheetServices;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEmpIDinTimesheet")
	public ResponseEntity<ResponseAsList> getEmpIDinTimesheet(
			@RequestBody GetEmpIDinTimesheetRequest getEmpIDinTimesheetRequest) {
		logger.debug("getTaskTemplateHdr   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.getEmpIDinTimesheet(getEmpIDinTimesheetRequest);

		} catch (Exception ex) {
			logger.error("getEmpIDinTimesheet  method  exception" + ex);
		}
		logger.debug("getEmpIDinTimesheet   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTimeSheetDtl")
	public ResponseEntity<ResponseAsList> getTimeSheetDtl(@RequestBody GetTimeSheetDtlRequest getTimeSheetDtl) {
		logger.debug("getTimeSheetDtl   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.getTimeSheetDtl(getTimeSheetDtl);

		} catch (Exception ex) {
			logger.error("getTimeSheetDtl  method  exception" + ex);
		}
		logger.debug("getTimeSheetDtl   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertTimeSheetDtl")
	public ResponseEntity<ResponseAsMessage> insertTimeSheetDtl(
			@RequestBody InsertTimeSheetRequest insertTimeSheetDtl) {

		logger.debug("insertTimeSheetDtl   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.insertTimeSheetDtl(insertTimeSheetDtl);

		} catch (Exception ex) {
			logger.error("insertTimeSheetDtl  method  exception" + ex);
		}
		logger.debug("insertTimeSheetDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTimeDtlByHdr")
	public ResponseEntity<ResponseAsList> getTimeDtlByHdr(@RequestBody GetTimeDtlByHdrRequest getTimeDtlByHdr) {
		logger.debug("getTimeDtlByHdr   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.getTimeDtlByHdr(getTimeDtlByHdr);

		} catch (Exception ex) {
			logger.error("getTimeDtlByHdr  method  exception" + ex);
		}
		logger.debug("getTimeDtlByHdr   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteActivityforEmpId")
	public ResponseEntity<ResponseAsMessage> deleteActivityforEmpId(
			@RequestBody DeleteActivityforEmpIdRequest deletereq) {

		logger.debug("deleteActivityforEmpId   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.deleteActivityforEmpId(deletereq);

		} catch (Exception ex) {
			logger.error("deleteActivityforEmpId  method  exception" + ex);
		}
		logger.debug("deleteActivityforEmpId   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertTimeSheetTask")
	public ResponseEntity<ResponseAsMessage> insertTimeSheetTask(
			@RequestBody InsertTimeSheetTaskRequest insertTimeSheetTask) {

		logger.debug("insertTimeSheetTask   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.insertTimeSheetTask(insertTimeSheetTask);

		} catch (Exception ex) {
			logger.error("insertTimeSheetTask  method  exception" + ex);
		}
		logger.debug("insertTimeSheetTask   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateTimeSheetTask")
	public ResponseEntity<ResponseAsMessage> updateTimeSheetTask(
			@RequestBody UpdateTimeSheetTaskRequest updateTimeSheetTask) {

		logger.debug("updateTimeSheetTask   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.updateTimeSheetTask(updateTimeSheetTask);

		} catch (Exception ex) {
			logger.error("updateTimeSheetTask  method  exception" + ex);
		}
		logger.debug("updateTimeSheetTask   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteTimeSheetTaskDtl")
	public ResponseEntity<ResponseAsMessage> deleteTimeSheetTaskDtl(
			@RequestBody DeleteTimeSheetTaskDtlRequest deletereq) {

		logger.debug("deleteTimeSheetTaskDtl   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.deleteTimeSheetTaskDtl(deletereq);

		} catch (Exception ex) {
			logger.error("deleteTimeSheetTaskDtl  method  exception" + ex);
		}
		logger.debug("deleteTimeSheetTaskDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateTimeSheetTaskHdrGroupName")
	public ResponseEntity<ResponseAsMessage> updateTimeSheetTaskHdrGroupName(
			@RequestBody UpdateTimeSheetTaskHdrGroupName updateTimeSheetTask) {

		logger.debug("updateTimeSheetTaskHdrGroupName   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.updateTimeSheetTaskHdrGroupName(updateTimeSheetTask);

		} catch (Exception ex) {
			logger.error("updateTimeSheetTaskHdrGroupName  method  exception" + ex);
		}
		logger.debug("updateTimeSheetTaskHdrGroupName   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllTimeSheetTaskForHdr")
	public ResponseEntity<ResponseAsList> getAllTimeSheetTaskForHdr(
			@RequestBody GetAllTimeSheetTaskForHdrRequest getTimeDtlByHdr) {
		logger.debug("getAllTimeSheetTaskForHdr   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.getAllTimeSheetTaskForHdr(getTimeDtlByHdr);

		} catch (Exception ex) {
			logger.error("getAllTimeSheetTaskForHdr  method  exception" + ex);
		}
		logger.debug("getAllTimeSheetTaskForHdr   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTimeSheetTaskForHdrandDtl")
	public ResponseEntity<ResponseAsList> getTimeSheetTaskForHdrandDtl(
			@RequestBody GetTimeSheetTaskForHdrandDtlRequset getTimeDtlByHdr) {
		logger.debug("getTimeSheetTaskForHdrandDtl   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.getTimeSheetTaskForHdrandDtl(getTimeDtlByHdr);

		} catch (Exception ex) {
			logger.error("getTimeSheetTaskForHdrandDtl  method  exception" + ex);
		}
		logger.debug("getTimeSheetTaskForHdrandDtl   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertTimeSheetCategory")
	public ResponseEntity<ResponseAsMessage> insertTimeSheetCategory(
			@RequestBody InsertTimeSheetCategoryRequest insertTimeSheetCategory) {

		logger.debug("insertTimeSheetCategory   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.insertTimeSheetCategory(insertTimeSheetCategory);

		} catch (Exception ex) {
			logger.error("insertTimeSheetCategory  method  exception" + ex);
		}
		logger.debug("insertTimeSheetCategory   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateTimeSheetCategory")
	public ResponseEntity<ResponseAsMessage> updateTimeSheetCategory(
			@RequestBody UpdateTimeSheetCategoryRequest updateTimeSheetCategory) {

		logger.debug("updateTimeSheetCategory   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.updateTimeSheetCategory(updateTimeSheetCategory);

		} catch (Exception ex) {
			logger.error("updateTimeSheetCategory  method  exception" + ex);
		}
		logger.debug("updateTimeSheetCategory   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTimeSheetCategory")
	public ResponseEntity<ResponseAsList> getTimeSheetCategory(
			@RequestBody GetTimeSheetCategoryRequest getTimeSheetCategory) {
		logger.debug("getTimeSheetCategory   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.getTimeSheetCategory(getTimeSheetCategory);

		} catch (Exception ex) {
			logger.error("getTimeSheetCategory  method  exception" + ex);
		}
		logger.debug("getTimeSheetCategory   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("timeSheetReport")
	public ResponseEntity<ResponseAsList> timeSheetReport(@RequestBody TimeSheetRequests timeSheetReport) {
		logger.debug("timeSheetReport   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.timeSheetReport(timeSheetReport);

		} catch (Exception ex) {
			logger.error("timeSheetReport  method  exception" + ex);
		}
		logger.debug("timeSheetReport   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("timeSheetMonthDtl")
	public ResponseEntity<ResponseAsList> timeSheetMonthDtl(@RequestBody TimeSheetRequests timeSheetReport) {
		logger.debug("timeSheetMonthDtl   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.timeSheetMonthDtl(timeSheetReport);

		} catch (Exception ex) {
			logger.error("timeSheetMonthDtl  method  exception" + ex);
		}
		logger.debug("timeSheetMonthDtl   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("timeSheetDeptDtl")
	public ResponseEntity<ResponseAsList> timeSheetDeptDtl(@RequestBody TimeSheetRequests timeSheetReport) {
		logger.debug("timeSheetDeptDtl   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.timeSheetDeptDtl(timeSheetReport);

		} catch (Exception ex) {
			logger.error("timeSheetDeptDtl  method  exception" + ex);
		}
		logger.debug("timeSheetDeptDtl   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("taskEntryHdrAndDtl")
	public ResponseEntity<ResponseAsList> taskEntryHdrAndDtl(@RequestBody TaskEntryHdrAndDtl timeSheetReport) {
		logger.debug("taskEntryHdrAndDtl   method Start");
		ResponseAsList timeSheet = null;
		try {

			timeSheet = iTimeSheetServices.taskEntryHdrAndDtl(timeSheetReport);

		} catch (Exception ex) {
			logger.error("taskEntryHdrAndDtl  method  exception" + ex);
		}
		logger.debug("taskEntryHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsList>(timeSheet, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertIndividualTimeSheetTaskDtl")
	public ResponseEntity<ResponseAsMessage> insertIndividualTimeSheetTaskDtl(
			@RequestBody InsertIndividualTimeSheetTaskDtl insertTimeSheet) {

		logger.debug("insertIndividualTimeSheetTaskDtl   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iTimeSheetServices.insertIndividualTimeSheetTaskDtl(insertTimeSheet);

		} catch (Exception ex) {
			logger.error("insertIndividualTimeSheetTaskDtl  method  exception" + ex);
		}
		logger.debug("insertIndividualTimeSheetTaskDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("timeSheetReportCategory")
	public ResponseEntity<ResponseAsList> timeSheetReportCategory(@RequestBody TimeSheetRequests timeSheetReportCategory) {
		logger.debug("timeSheetReportCategory   method Start");
		ResponseAsList timeSheetCategory = null;
		try {

			timeSheetCategory = iTimeSheetServices.timeSheetReportCategory(timeSheetReportCategory);

		} catch (Exception ex) {
			logger.error("timeSheetReportCategory  method  exception" + ex);
		}
		logger.debug("timeSheetReportCategory   method end");
		return new ResponseEntity<ResponseAsList>(timeSheetCategory, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("timelogRemaining")
	public ResponseEntity<ResponseAsMessage> timelogremaining(@RequestBody TimeSheetRemainingLogRequest timelogReq) {
		logger.debug("timelogremaining   method Start");
		ResponseAsMessage timelogremaining = null;
		try {

			timelogremaining = iTimeSheetServices.timelogremaining(timelogReq);

		} catch (Exception ex) {
			logger.error("timelogremaining  method  exception" + ex);
		}
		logger.debug("timelogremaining   method end");
		return new ResponseEntity<ResponseAsMessage>(timelogremaining, HttpStatus.OK);
	}
}
