package com.vmfg.task.controller;

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
import com.vmfg.task.entity.GetAllCountByEmpIdEntity;
import com.vmfg.task.request.GetAllCountByEmpId;
import com.vmfg.task.request.GetReqManHdrDtlRequest;
import com.vmfg.task.request.GetRequestCategoryRequest;
import com.vmfg.task.request.GetStatusRemarksDtlRequest;
import com.vmfg.task.request.InsertRMRemarksRequest;
import com.vmfg.task.request.ReqManagementHdr;
import com.vmfg.task.request.UpdateReqStatusRequest;
import com.vmfg.task.services.interfaces.IRequestManagementServices;

@Controller
@RequestMapping("/")
public class RequestManagementController {
	private static final Logger logger = LoggerFactory.getLogger(RequestManagementController.class);
	@Autowired
	private IRequestManagementServices iRequestManagementServices;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertReqManagementHdr")
	public ResponseEntity<ResponseAsMessage> insertOrUpdateSalesBudgetSheetHdrAndDtl(
			@RequestBody ReqManagementHdr reqManagementHdr) {
		logger.info("insertReqManagementHdr   method Start");
		ResponseAsMessage returnMsg = null;
		try {
			returnMsg = iRequestManagementServices.insertReqManagementHdr(reqManagementHdr);
			logger.info("insertReqManagementHdr method End");

		} catch (Exception ex) {
			logger.error("insertReqManagementHdr Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getStatusRemarksDtl")
	public ResponseEntity<ResponseAsList> getStatusRemarksDtl(
			@RequestBody GetStatusRemarksDtlRequest GetStatusRemarks) {
		logger.debug("getStatusRemarksDtl   method Start");
		ResponseAsList response = null;
		try {

			response = iRequestManagementServices.getStatusRemarksDtl(GetStatusRemarks);

		} catch (Exception ex) {
			logger.error("getStatusRemarksDtl  method  exception" + ex);
		}
		logger.debug("getStatusRemarksDtl   method end");
		return new ResponseEntity<ResponseAsList>(response, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertRMRemarks")
	public ResponseEntity<ResponseAsMessage> insertRMRemarks(@RequestBody InsertRMRemarksRequest insertRMRemarks) {
		logger.info("insertRMRemarks   method Start");
		ResponseAsMessage returnMsg = null;
		try {
			returnMsg = iRequestManagementServices.insertRMRemarks(insertRMRemarks);
			logger.info("insertRMRemarks method End");

		} catch (Exception ex) {
			logger.error("insertRMRemarks Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertReqStatus")
	public ResponseEntity<ResponseAsMessage> insertReqStatus(@RequestBody UpdateReqStatusRequest updateReqStatus) {
		logger.info("insertReqStatus   method Start");
		ResponseAsMessage returnMsg = null;
		try {
			returnMsg = iRequestManagementServices.insertReqStatus(updateReqStatus);
			logger.info("insertReqStatus method End");

		} catch (Exception ex) {
			logger.error("insertReqStatus Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getRequestCategory")
	public ResponseEntity<ResponseAsList> getRequestCategory(@RequestBody GetRequestCategoryRequest requestCategory) {
		logger.debug("getStatusRemarksDtl   method Start");
		ResponseAsList response = null;
		try {

			response = iRequestManagementServices.getRequestCategory(requestCategory);

		} catch (Exception ex) {
			logger.error("getRequestCategory  method  exception" + ex);
		}
		logger.debug("getRequestCategory   method end");
		return new ResponseEntity<ResponseAsList>(response, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getReqManHdrDtl")
	public ResponseEntity<ResponseAsList> getReqManHdrDtl(@RequestBody GetReqManHdrDtlRequest getReqManHdr) {
		logger.debug("getReqManHdrDtl   method Start");
		ResponseAsList response = null;
		try {

			response = iRequestManagementServices.getReqManHdrDtl(getReqManHdr);

		} catch (Exception ex) {
			logger.error("getReqManHdrDtl  method  exception" + ex);
		}
		logger.debug("getReqManHdrDtl   method end");
		return new ResponseEntity<ResponseAsList>(response, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getReqManHdrAndStatusAndRemarks")
	public ResponseEntity<ResponseAsList> getReqManHdrAndStatusAndRemarks(
			@RequestBody GetStatusRemarksDtlRequest getStatusRemarksDtlRequest) {
		logger.debug("getReqManHdrAndStatusAndRemarks   method Start");
		ResponseAsList response = null;
		try {

			response = iRequestManagementServices.getReqManHdrAndStatusAndRemarks(getStatusRemarksDtlRequest);

		} catch (Exception ex) {
			logger.error("getReqManHdrAndStatusAndRemarks  method  exception" + ex);
		}
		logger.debug("getReqManHdrAndStatusAndRemarks   method end");
		return new ResponseEntity<ResponseAsList>(response, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllCounts")
	public ResponseEntity<GetAllCountByEmpIdEntity> getAllCounts(@RequestBody GetAllCountByEmpId getAllCountByEmpId) {
		logger.debug("getAllCounts   method Start");
		GetAllCountByEmpIdEntity response = null;
		try {

			response = iRequestManagementServices.getAllCounts(getAllCountByEmpId);

		} catch (Exception ex) {
			logger.error("getAllCounts  method  exception" + ex);
		}
		logger.debug("getAllCounts   method end");
		return new ResponseEntity<GetAllCountByEmpIdEntity>(response, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getRequestedToDtl")
	public ResponseEntity<ResponseAsList> getRequestedToDtl(@RequestBody GetAllCountByEmpId getAllByEmpId) {
		logger.debug("getRequestedToDtl   method Start");
		ResponseAsList response = null;
		try {

			response = iRequestManagementServices.getRequestedToDtl(getAllByEmpId);

		} catch (Exception ex) {
			logger.error("getRequestedToDtl  method  exception" + ex);
		}
		logger.debug("getRequestedToDtl   method end");
		return new ResponseEntity<ResponseAsList>(response, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getRequestedByDtl")
	public ResponseEntity<ResponseAsList> getRequestedByDtl(@RequestBody GetAllCountByEmpId getAllByEmpId) {
		logger.debug("getRequestedByDtl   method Start");
		ResponseAsList response = null;
		try {

			response = iRequestManagementServices.getRequestedByDtl(getAllByEmpId);

		} catch (Exception ex) {
			logger.error("getRequestedByDtl  method  exception" + ex);
		}
		logger.debug("getRequestedByDtl   method end");
		return new ResponseEntity<ResponseAsList>(response, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getRequestedByDtlWithIsComplete")
	public ResponseEntity<ResponseAsList> getRequestedByDtlWithIsComplete(
			@RequestBody GetAllCountByEmpId getAllByEmpId) {
		logger.debug("getRequestedByDtlWithIsComplete   method Start");
		ResponseAsList response = null;
		try {

			response = iRequestManagementServices.getRequestedByDtlWithIsComplete(getAllByEmpId);

		} catch (Exception ex) {
			logger.error("getRequestedByDtlWithIsComplete  method  exception" + ex);
		}
		logger.debug("getRequestedByDtlWithIsComplete   method end");
		return new ResponseEntity<ResponseAsList>(response, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getRequestToWithAllDepartment")
	public ResponseEntity<ResponseAsList> getRequestToWithAllDepartment(@RequestBody GetAllCountByEmpId getAllByEmpId) {
		logger.debug("getRequestToWithAllDepartment   method Start");
		ResponseAsList response = null;
		try {

			response = iRequestManagementServices.getRequestToWithAllDepartment(getAllByEmpId);

		} catch (Exception ex) {
			logger.error("getRequestToWithAllDepartment  method  exception" + ex);
		}
		logger.debug("getRequestToWithAllDepartment   method end");
		return new ResponseEntity<ResponseAsList>(response, HttpStatus.OK);
	}

}
