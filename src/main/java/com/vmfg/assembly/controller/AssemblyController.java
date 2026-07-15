package com.vmfg.assembly.controller;

import io.swagger.annotations.SwaggerDefinition;
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

import com.vmfg.assembly.request.GetAssyDtlRequest;
import com.vmfg.assembly.request.InsertMrHdrAndDtlRequest;
import com.vmfg.assembly.request.IsStagingRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.assembly.request.RetriveFromStockRequest;
import com.vmfg.assembly.services.interfaces.IAssemblyService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

@Controller
@RequestMapping("/")
public class AssemblyController {

	private static final Logger logger = LoggerFactory.getLogger(AssemblyController.class);

	@Autowired
	private IAssemblyService iAssyService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAssyDtl")
	public ResponseEntity<ResponseAsList> getAssyDtl(@RequestBody GetAssyDtlRequest getAssyDtlReq) {
		logger.debug("getAssyDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iAssyService.getAssyDtl(getAssyDtlReq);

		} catch (Exception ex) {
			logger.error("getAssyDtl  method  exception" + ex);
		}
		logger.debug("getAssyDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getMaterialReqHdr")
	public ResponseEntity<ResponseAsList> getMaterialReqHdr(@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("getMaterialReqHdr   method Start");
		ResponseAsList list = null;
		try {

			list = iAssyService.getMaterialReqHdr(materialHdrReq);

		} catch (Exception ex) {
			logger.error("getMaterialReqHdr  method  exception" + ex);
		}
		logger.debug("getMaterialReqHdr   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getMaterialReqDtl")
	public ResponseEntity<ResponseAsList> getMaterialReqDtl(@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("getMaterialReqHdrAndDtl   method Start");
		ResponseAsList list = null;
		try {

			list = iAssyService.getMaterialReqDtl(materialHdrReq);

		} catch (Exception ex) {
			logger.error("getMaterialReqHdrAndDtl  method  exception" + ex);
		}
		logger.debug("getMaterialReqHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("cancelMiRequestHdr")
	public ResponseEntity<ResponseAsMessage> cancelMiRequestHdr(@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("cancelMiRequestHdr   method Start");
		ResponseAsMessage res = null;
		try {

			res = iAssyService.cancelMiRequestHdr(materialHdrReq);

		} catch (Exception ex) {
			logger.error("cancelMiRequestHdr  method  exception" + ex);
		}
		logger.debug("cancelMiRequestHdr   method end");
		return new ResponseEntity<ResponseAsMessage>(res, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("retriveFromStock")
	public ResponseEntity<ResponseAsList> retriveFromStock(@RequestBody RetriveFromStockRequest retriveFromStock) {
		logger.debug("retriveFromStock   method Start");
		ResponseAsList list = null;
		try {

			list = iAssyService.retriveFromStock(retriveFromStock);

		} catch (Exception ex) {
			logger.error("retriveFromStock  method  exception" + ex);
		}
		logger.debug("retriveFromStock   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertMrHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> insertMrHdrAndDtl(
			@RequestBody InsertMrHdrAndDtlRequest insertMrDtlsEntity) {
		logger.debug("insertMrHdrAndDtl   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iAssyService.insertMrHdrAndDtl(insertMrDtlsEntity);

		} catch (Exception ex) {
			logger.error("insertMrHdrAndDtl  method  exception" + ex);
		}
		logger.debug("insertMrHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("retriveAssyResp")
	public ResponseEntity<ResponseAsMessage> retriveAssyResp(@RequestBody MaterialReqHdrRequest assyMstRequest) {
		logger.info("retriveAssyResp   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iAssyService.retriveAssyResp(assyMstRequest);

		} catch (Exception ex) {
			logger.error("retriveAssyResp  method  exception" + ex);
		}
		logger.debug("retriveAssyResp   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("retriveIsStagingStatus")
	public ResponseEntity<ResponseAsMessage> retriveIsStagingStatus(@RequestBody IsStagingRequest isStagingReq) {
		logger.info("retriveIsStagingStatus method Start");
		ResponseAsMessage resp = new ResponseAsMessage();
		try {

			resp = iAssyService.retriveIsStagingStatus(isStagingReq);

		} catch (Exception ex) {
			logger.error("retriveIsStagingStatus method  exception" + ex);
		}
		logger.debug("retriveIsStagingStatus method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateIsStagingStatus")
	public ResponseEntity<ResponseAsMessage> updateIsStagingStatus(@RequestBody IsStagingRequest isStagingReq) {
		logger.info("updateIsStagingStatus method Start");
		ResponseAsMessage resp = new ResponseAsMessage();
		try {

			resp = iAssyService.updateIsStagingStatus(isStagingReq);

		} catch (Exception ex) {
			logger.error("updateIsStagingStatus method  exception" + ex);
		}
		logger.debug("updateIsStagingStatus method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
}
