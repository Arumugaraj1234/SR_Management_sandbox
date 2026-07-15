package com.vmfg.quality.controller;

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
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.quality.entity.QualityInspectionHdrEntity;
import com.vmfg.quality.request.QiCaDtlsRequest;
import com.vmfg.quality.request.QiStatusRequest;
import com.vmfg.quality.request.UpdateQiCaDtlsRequest;
import com.vmfg.quality.request.UpdateVendorRatingRequest;
import com.vmfg.quality.request.getQIDtlsRequest;
import com.vmfg.quality.services.interfaces.IQualityInspectionService;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;

@Controller
@RequestMapping("/")
public class QualityInspectionController {
	private static final Logger logger = LoggerFactory.getLogger(QualityInspectionController.class);

	@Autowired
	IQualityInspectionService iQualityInspectionService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getInspHdrAndDtl")
	public ResponseEntity<ResponseAsList> getInspHdrAndDtl(@RequestBody getQIDtlsRequest getQIDtlsReq) {
		logger.info("getInspHdrAndDtl getInspHdrAndDtl  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityInspectionService.getInspHdrAndDtl(getQIDtlsReq);
		} catch (Exception ex) {
			logger.error("getInspHdrAndDtl Controller  method  exception:" + ex);
		}
		logger.info("getInspHdrAndDtl Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQIStatusDtls")
	public ResponseEntity<ResponseAsList> getQIStatusDtls(@RequestBody QiStatusRequest qiStatusReq) {
		logger.info("getQIStatusDtls Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityInspectionService.getQIStatusDtls(qiStatusReq);
		} catch (Exception ex) {
			logger.error("getQIStatusDtls Controller  method  exception:" + ex);
		}
		logger.info("getQIStatusDtls Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertInspHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> insertInspHdrAndDtl(@RequestBody QualityInspectionHdrEntity qiHdrDtlReq) {
		logger.info("insertInspHdrAndDtl Controller  method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iQualityInspectionService.insertInspHdrAndDtl(qiHdrDtlReq);
		} catch (Exception ex) {
			logger.error("insertInspHdrAndDtl Controller  method  exception:" + ex);
		}
		logger.info("insertInspHdrAndDtl Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateQISeqAndStatus")
	public ResponseEntity<ResponseAsMessage> updateQISeqAndStatus(@RequestBody UpdateSeqAndStatusRequest UpdateHdrReq) {
		logger.debug("updateQISeqAndStatus  method Start");
	    logger.debug("Received request: " + UpdateHdrReq);

		ResponseAsMessage list = null;
		try {
			list = iQualityInspectionService.updateQISeqAndStatus(UpdateHdrReq);
		} catch (Exception e) {
			logger.debug("updateQISeqAndStatus methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateQiCaSeqAndStatus")
	public ResponseEntity<ResponseAsMessage> updateQiCaSeqAndStatus(@RequestBody UpdateSeqAndStatusRequest UpdateHdrReq) {
		logger.debug("updateQiCaSeqAndStatus  method Start");
		ResponseAsMessage list = null;
		try {
			list = iQualityInspectionService.updateQiCaSeqAndStatus(UpdateHdrReq);
		} catch (Exception e) {
			logger.debug("updateQiCaSeqAndStatus methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQiCaDtlsByPmHdrId")
	public ResponseEntity<ResponseAsList> getQiCaDtlsByPmHdrId(@RequestBody PmHdrIdAndTenantIdRequest request) {
		logger.info("getQiCaDtlsByPmHdrId  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityInspectionService.getQiCaDtlsByPmHdrId(request);
		} catch (Exception ex) {
			logger.error("getQiCaDtlsByPmHdrId Controller  method  exception:" + ex);
		}
		logger.info("getQiCaDtlsByPmHdrId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQiCaDtlsByQiCaDtlId")
	public ResponseEntity<ResponseAsList> getQiCaDtlsByQiCaDtlId(@RequestBody QiCaDtlsRequest qiCaDtlsReq) {
		logger.info("getQiCaDtlsByQiCaDtlId controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityInspectionService.getQiCaDtlsByQiCaDtlId(qiCaDtlsReq);
		} catch (Exception ex) {
			logger.error("getQiCaDtlsByQiCaDtlId Controller  method  exception:" + ex);
		}
		logger.info("getQiCaDtlsByQiCaDtlId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateQiCaDtls")
	public ResponseEntity<ResponseAsMessage> updateQiCaDtls(@RequestBody UpdateQiCaDtlsRequest updateQiCaDtlsReq) {
		logger.info("updateQiCaDtls Controller  method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iQualityInspectionService.updateQiCaDtls(updateQiCaDtlsReq);
		} catch (Exception ex) {
			logger.error("updateQiCaDtls Controller  method  exception:" + ex);
		}
		logger.info("updateQiCaDtls Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateVendorRating")
	public ResponseEntity<ResponseAsMessage> updateVendorRating(@RequestBody UpdateVendorRatingRequest UpdateVendorRatingRequest) {
		logger.info("updateVendorRating Controller  method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iQualityInspectionService.updateVendorRating(UpdateVendorRatingRequest);
		} catch (Exception ex) {
			logger.error("updateVendorRating Controller  method  exception:" + ex);
		}
		logger.info("updateVendorRating Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
}