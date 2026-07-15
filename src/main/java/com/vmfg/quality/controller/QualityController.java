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
import com.vmfg.quality.request.GetConfigNameReq;
import com.vmfg.quality.request.GetInspTypeReq;
import com.vmfg.quality.request.GetQtyDtlRequest;
import com.vmfg.quality.request.GetQtyInspectionHdrRequest;
import com.vmfg.quality.request.GetqtyInspecDocDtlRequest;
import com.vmfg.quality.request.RetieveQCInspectionHdrReq;
import com.vmfg.quality.request.RetrieveQualitInspectionReq;
import com.vmfg.quality.services.interfaces.IQualityServices;

@Controller
@RequestMapping("/")
public class QualityController {
	private static final Logger logger = LoggerFactory.getLogger(QualityController.class);

	@Autowired
	IQualityServices iQualityServices;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQtyDtl")
	public ResponseEntity<ResponseAsList> getQtyDtl(@RequestBody GetQtyDtlRequest getQtyDtlReq) {
		logger.info("getQtyDtl Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityServices.getQtyDtl(getQtyDtlReq);
		} catch (Exception ex) {
			logger.error("getQtyDtl Controller  method  exception:" + ex);
		}
		logger.info("getQtyDtl Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveQualitInspectionReq")
	public ResponseEntity<ResponseAsList> retrieveQualitInspectionReq(
			@RequestBody RetrieveQualitInspectionReq retrieveQualitInspectionReq) {
		logger.info("retrieveQualitInspectionReq Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityServices.retrieveQualitInspectionReq(retrieveQualitInspectionReq);
		} catch (Exception ex) {
			logger.error("retrieveQualitInspectionReq Controller  method  exception:" + ex);
		}
		logger.info("retrieveQualitInspectionReq Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("retieveQCInspectionHdr")
	public ResponseEntity<ResponseAsList> retieveQCInspectionHdr(
			@RequestBody RetieveQCInspectionHdrReq retieveQCInspection) {
		logger.info("retieveQCInspectionHdr Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityServices.retieveQCInspectionHdr(retieveQCInspection);
		} catch (Exception ex) {
			logger.error("retieveQCInspectionHdr Controller  method  exception:" + ex);
		}
		logger.info("retieveQCInspectionHdr Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getInspType")
	public ResponseEntity<ResponseAsList> getInspType(@RequestBody GetInspTypeReq inspecType) {
		logger.info("getInspType Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityServices.getInspType(inspecType);
		} catch (Exception ex) {
			logger.error("getInspType Controller  method  exception:" + ex);
		}
		logger.info("getInspType Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getConfigName")
	public ResponseEntity<ResponseAsList> getConfigName(@RequestBody GetConfigNameReq inspecType) {
		logger.info("getConfigName Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityServices.getConfigName(inspecType);
		} catch (Exception ex) {
			logger.error("getConfigName Controller  method  exception:" + ex);
		}
		logger.info("getConfigName Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQtyInspectionHdr")
	public ResponseEntity<ResponseAsList> getQtyInspectionHdr(@RequestBody GetQtyInspectionHdrRequest getQtyInspectionHdr) {
		logger.info("getQtyInspectionHdr Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityServices.getQtyInspectionHdr(getQtyInspectionHdr);
		} catch (Exception ex) {
			logger.error("getQtyInspectionHdr Controller  method  exception:" + ex);
		}
		logger.info("getQtyInspectionHdr Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getqtyInspecDocDtl")
	public ResponseEntity<ResponseAsList> getqtyInspecDocDtl(@RequestBody GetqtyInspecDocDtlRequest getqtyInspecDocDtlReq) {
		logger.info("getqtyInspecDocDtl Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iQualityServices.getqtyInspecDocDtl(getqtyInspecDocDtlReq);
		} catch (Exception ex) {
			logger.error("getqtyInspecDocDtl Controller  method  exception:" + ex);
		}
		logger.info("getqtyInspecDocDtl Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
}
