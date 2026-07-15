package com.vmfg.scm.controller;

import com.vmfg.scm.request.*;
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
import com.vmfg.scm.services.interfaces.IGrnService;

@Controller
@RequestMapping(("/"))
public class GrnController {
	private static final Logger logger = LoggerFactory.getLogger(GrnController.class);

	@Autowired
	private IGrnService iGrnService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getGrnHdrDetails")
	public ResponseEntity<ResponseAsList> getGrnHdrDetails(@RequestBody GetGrnHdrRequest getGrnHdrRequest) {
		logger.info("getGrnHdrDetails Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iGrnService.getGrnHdrDetails(getGrnHdrRequest);
		} catch (Exception ex) {
			logger.error("getGrnHdrDetails  method  exception:" + ex);
		}
		logger.info("getGrnHdrDetails  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPoCostType")
	public ResponseEntity<ResponseAsList> getPoCostType(@RequestBody GetPoCostTypeReq getPoCostTypeReq) {
		logger.info("getPoCostType Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iGrnService.getPoCostType(getPoCostTypeReq);
		} catch (Exception ex) {
			logger.error("getPoCostType  method  exception:" + ex);
		}
		logger.info("getPoCostType  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getGrnDtlwithMaterialInwardDtl")
	public ResponseEntity<ResponseAsList> getGrnDtlwithMaterialInwardDtl(@RequestBody GrnDtlRequest grnDtlRequest) {
		logger.info("getGrnDtlwithMaterialInwardDtl   method Start");
		ResponseAsList list = null;
		try {
			list = iGrnService.getGrnDtlwithMaterialInwardDtl(grnDtlRequest);
		} catch (Exception ex) {
			logger.error("getGrnDtlwithMaterialInwardDtl  method  exception:" + ex);
		}
		logger.info("getGrnDtlwithMaterialInwardDtl  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertGrnHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> insertGrnHdrAndDtl(@RequestBody GrnHdrInsertRequest grnHdrAndDtl) {
		logger.debug("insertPoDtlsEntity   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iGrnService.insertGrnHdrAndDtl(grnHdrAndDtl);

		} catch (Exception ex) {
			logger.error("insertGrnHdrAndDtl  method  exception" + ex);
		}
		logger.debug("insertGrnHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertQtyInspReq")
	public ResponseEntity<ResponseAsMessage> insertQtyInspReq(@RequestBody InsertQtyInspRequest insertQtyInspReq) {
		logger.debug("insertQtyInspReq   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iGrnService.insertQtyInspReq(insertQtyInspReq);

		} catch (Exception ex) {
			logger.error("insertQtyInspReq  method  exception" + ex);
		}
		logger.debug("insertQtyInspReq   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertMIQtyReq")
	public ResponseEntity<ResponseAsMessage> insertMIQtyReq(@RequestBody InsertQtyInspRequest insertQtyInspReq) {
		logger.debug("insertMIQtyReq   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iGrnService.insertMIQtyReq(insertQtyInspReq);

		} catch (Exception ex) {
			logger.error("insertMIQtyReq  method  exception" + ex);
		}
		logger.debug("insertMIQtyReq   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("grnCancel")
	public ResponseEntity<ResponseAsMessage> grnCancel(@RequestBody GrnCancelReq grnCancelReq) {
		logger.debug("grnCancel method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iGrnService.grnCancel(grnCancelReq);

		} catch (Exception ex) {
			logger.error("grnCancel  method  exception" + ex);
		}
		logger.debug("grnCancel  method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

}
