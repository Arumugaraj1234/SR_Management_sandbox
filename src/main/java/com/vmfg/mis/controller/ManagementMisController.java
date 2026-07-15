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
import com.vmfg.mis.request.ManagementProjRequest;
import com.vmfg.mis.services.interfaces.IManagementMisService;


@Controller
@RequestMapping("/")
public class ManagementMisController {

	@Autowired
	IManagementMisService IManagementMisService;
	
	private static final Logger logger = LoggerFactory.getLogger(ManagementMisController.class);
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTotalProjCnt")
	public ResponseEntity<ResponseAsList> getTotalProjCnt(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getTotalProjCnt  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getTotalProjCnt(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getTotalProjCnt  method  exception" + ex);
		}
		logger.debug("getTotalProjCnt  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjConsumedValue")
	public ResponseEntity<ResponseAsList> getProjConsumedValue(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getProjConsumedValue  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getProjConsumedValue(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getProjConsumedValue  method  exception" + ex);
		}
		logger.debug("getProjConsumedValue  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
//	@PostMapping("getProjSpentDrillDown")
	public ResponseEntity<ResponseAsList> getProjSpentDrillDown(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getProjSpentDrillDown  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getProjSpentDrillDown(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getProjSpentDrillDown  method  exception" + ex);
		}
		logger.debug("getProjSpentDrillDown  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getOverAllProjSpentDrillDown")
	public ResponseEntity<ResponseAsList> getOverAllProjSpentDrillDown(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getOverAllProjSpentDrillDown  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getOverAllProjSpentDrillDown(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getOverAllProjSpentDrillDown  method  exception" + ex);
		}
		logger.debug("getOverAllProjSpentDrillDown  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
//	@PostMapping("getProjActualValDrillDown")
	public ResponseEntity<ResponseAsList> getProjActualValDrillDown(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getProjActualValDrillDown  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getProjActualValDrillDown(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getProjActualValDrillDown  method  exception" + ex);
		}
		logger.debug("getProjActualValDrillDown  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjSpentDetailByPmId")
	public ResponseEntity<ResponseAsList> getProjSpentDetailByPmId(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getProjSpentDetailByPmId  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getProjSpentDetailByPmId(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getProjSpentDetailByPmId  method  exception" + ex);
		}
		logger.debug("getProjSpentDetailByPmId  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjDetailsDrillDown")
	public ResponseEntity<ResponseAsList> getProjDetailsDrillDown(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getProjDetailsDrillDown  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getProjDetailsDrillDown(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getProjDetailsDrillDown  method  exception" + ex);
		}
		logger.debug("getProjDetailsDrillDown  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorDetailDrillDown")
	public ResponseEntity<ResponseAsList> getVendorDetailDrillDown(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getVendorDetailDrillDown  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getVendorDetailDrillDown(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getVendorDetailDrillDown  method  exception" + ex);
		}
		logger.debug("getVendorDetailDrillDown  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorDetailHdrView")
	public ResponseEntity<ResponseAsList> getVendorDetailHdrView(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getVendorDetailHdrView  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getVendorDetailHdrView(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getVendorDetailHdrView  method  exception" + ex);
		}
		logger.debug("getVendorDetailHdrView  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorPaymentDetails")
	public ResponseEntity<ResponseAsList> getVendorPaymentDetails(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getVendorPaymentDetails  method Start");
		ResponseAsList resp = null;
		try {
			resp = IManagementMisService.getVendorPaymentDetails(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getVendorPaymentDetails  method  exception" + ex);
		}
		logger.debug("getVendorPaymentDetails  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
}
