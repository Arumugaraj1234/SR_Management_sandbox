package com.vmfg.export.controller;

import java.util.List;

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

import com.vmfg.assembly.request.MaterialIssueRequest;
import com.vmfg.export.request.DcRequestHdrRequest;
import com.vmfg.export.request.DeliveryChallanRequest;
import com.vmfg.export.request.DtlIdandTenantIdRequest;
import com.vmfg.export.response.ResponseAsList;
import com.vmfg.export.services.interfaces.IDeliveryChallanReportService;
import com.vmfg.general.response.ResponseAsMessage;

@Controller
@RequestMapping("/")
public class DeliveryChallanReportController {
	private static final Logger logger = LoggerFactory.getLogger(DeliveryChallanReportController.class);

	@Autowired
	private IDeliveryChallanReportService iDeliveryChallanReportService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDeliveryChallanReportPdf")
	public ResponseEntity<ResponseAsList> getDeliveryChallanReportPdf(@RequestBody DeliveryChallanRequest deliveryReq) {
		logger.info("getDeliveryChallanReportPdf   method Start");
		ResponseAsList resp = null;
		try {
			resp = iDeliveryChallanReportService.getDeliveryChallanReportPdf(deliveryReq);
		} catch (Exception ex) {
			logger.error("getDeliveryChallanReportPdf  method  exception" + ex);
		}
		logger.info("getDeliveryChallanReportPdf   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertdcreqhdrdtl")
	public ResponseEntity<ResponseAsMessage> getinsertqcreq(@RequestBody DcRequestHdrRequest deliveryReq) {
		logger.info("getinsertqcreq   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iDeliveryChallanReportService.getinsertqcreq(deliveryReq);
		} catch (Exception ex) {
			logger.error("getinsertqcreq  method  exception" + ex);
		}
		logger.info("getinsertqcreq   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getdcreqhdrdtldtl")
	public ResponseEntity<ResponseAsList> getdcreqhdrdtldtl(@RequestBody MaterialIssueRequest deliveryReq) {
		logger.info("getDeliveryChallanReportPdf   method Start");
		ResponseAsList resp = null;
		try {
			resp = iDeliveryChallanReportService.getdcreqhdrdtldtl(deliveryReq);
		} catch (Exception ex) {
			logger.error("getdcreqhdrdtldtl  method  exception" + ex);
		}
		logger.info("getdcreqhdrdtldtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateDcqty")
	public ResponseEntity<ResponseAsMessage> updateDcqty(@RequestBody List<DtlIdandTenantIdRequest>  deliveryReq) {
		logger.info("getDeliveryChallanReportPdf   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iDeliveryChallanReportService.updateDcqty(deliveryReq);
		} catch (Exception ex) {
			logger.error("getdcreqhdrdtldtl  method  exception" + ex);
		}
		logger.info("getdcreqhdrdtldtl   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
		
	}
	


}
