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
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.request.ManagementProjRequest;
import com.vmfg.mis.request.ScmMisRequest;
import com.vmfg.mis.services.interfaces.IScmMisService;

@Controller
@RequestMapping("/")
public class ScmMisController {
	private static final Logger logger = LoggerFactory.getLogger(ScmMisController.class);

	@Autowired
	IScmMisService iScmMisService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSCMWidgetDtl")
	public ResponseEntity<ResponseAsList> getSCMWidgetDtl(@RequestBody ScmMisRequest scmMisReq) {

		logger.debug("getSCMWidgetDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iScmMisService.getSCMWidgetDtl(scmMisReq);

		} catch (Exception ex) {
			logger.error("getSCMWidgetDtl  method  exception" + ex);
		}
		logger.debug("getSCMWidgetDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentToPO")
	public ResponseEntity<ResponseAsList> getIndentToPO(@RequestBody ScmMisRequest scmMisReq) {

		logger.debug("getIndentToPO   method Start");
		ResponseAsList resp = null;
		try {

			resp = iScmMisService.getIndentToPO(scmMisReq);

		} catch (Exception ex) {
			logger.error("getIndentToPO  method  exception" + ex);
		}
		logger.debug("getIndentToPO   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getcostnegotiated")
	public ResponseEntity<ResponseAsMessage> getcostnegotiated(@RequestBody ScmMisRequest scmMisReq) {

		logger.debug("getcostnegotiated   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iScmMisService.getcostnegotiated(scmMisReq);
		} catch (Exception ex) {
			logger.error("getcostnegotiated  method  exception" + ex);
		}
		logger.debug("getcostnegotiated   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getInventoryValue")
	public ResponseEntity<ResponseAsMessage> getInventoryValue(@RequestBody ScmMisRequest scmMisReq) {

		logger.debug("getInventoryValue   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iScmMisService.getInventoryValue(scmMisReq);
		} catch (Exception ex) {
			logger.error("getInventoryValue  method  exception" + ex);
		}
		logger.debug("getInventoryValue   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getInventoryAgeing")
	public ResponseEntity<ResponseAsMessage> getInventoryAgeing(@RequestBody ScmMisRequest scmMisReq) {

		logger.debug("getInventoryAgeing   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iScmMisService.getInventoryAgeing(scmMisReq);
		} catch (Exception ex) {
			logger.error("getInventoryAgeing  method  exception" + ex);
		}
		logger.debug("getInventoryAgeing   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getScmEmployeeIndentDtls")
	public ResponseEntity<ResponseAsList> getScmEmployeeIndentDtls(@RequestBody ScmMisRequest scmMisReq) {

		logger.debug("getScmEmployeeIndentDtls   method Start");
		ResponseAsList resp = null;
		try {
			resp = iScmMisService.getScmEmployeeIndentDtls(scmMisReq);
		} catch (Exception ex) {
			logger.error("getScmEmployeeIndentDtls  method  exception" + ex);
		}
		logger.debug("getScmEmployeeIndentDtls   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorPaymentCount")
	public ResponseEntity<ResponseAsList> getVendorPaymentCount(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getVendorPaymentCount  method Start");
		ResponseAsList resp = null;
		try {
			resp = iScmMisService.getVendorPaymentCount(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getVendorPaymentCount  method  exception" + ex);
		}
		logger.debug("getVendorPaymentCount  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorDetailView")
	public ResponseEntity<ResponseAsList> getVendorDetailHdrView(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getVendorDetailView  method Start");
		ResponseAsList resp = null;
		try {
			resp = iScmMisService.getVendorDetailView(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getVendorDetailView  method  exception" + ex);
		}
		logger.debug("getVendorDetailView  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorDtlDrillDown")
	public ResponseEntity<ResponseAsList> getVendorDtlDrillDown(@RequestBody ManagementProjRequest manageProjCnt ){
	
		logger.info("getVendorDtlDrillDown  method Start");
		ResponseAsList resp = null;
		try {
			resp = iScmMisService.getVendorDtlDrillDown(manageProjCnt);
		} catch (Exception ex) {
			logger.error("getVendorDtlDrillDown  method  exception" + ex);
		}
		logger.debug("getVendorDtlDrillDown  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	} 
}
