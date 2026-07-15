package com.vmfg.assembly.controller;

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

import com.vmfg.assembly.request.InsertMaterialIssueRequest;
import com.vmfg.assembly.request.MaterialIssueHdrRequest;
import com.vmfg.assembly.services.interfaces.IAssemblyIssueService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

@Controller
@RequestMapping("/")
public class AssemblyIssueController {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyIssueController.class);
	@Autowired
	private IAssemblyIssueService iAssemblyIssueService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertMaterialIssueHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> insertMaterialIssueHdrAndDtl(
			@RequestBody InsertMaterialIssueRequest insertMrDtlsEntity) {
		logger.debug("insertMaterialIssueHdrAndDtl   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iAssemblyIssueService.insertMaterialIssueHdrAndDtl(insertMrDtlsEntity);

		} catch (Exception ex) {
			logger.error("insertMaterialIssueHdrAndDtl  method  exception" + ex);
		}
		logger.debug("insertMaterialIssueHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getMaterialIssueHdr")
	public ResponseEntity<ResponseAsList> getMaterialIssueHdr(@RequestBody MaterialIssueHdrRequest materialHdrReq) {
		logger.debug("getMaterialIssueHdr   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyIssueService.getMaterialIssueHdr(materialHdrReq);

		} catch (Exception ex) {
			logger.error("getMaterialReqHdr  method  exception" + ex);
		}
		logger.debug("getMaterialIssueHdr   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getMaterialIssueDtl")
	public ResponseEntity<ResponseAsList> getMaterialIssueDtl(@RequestBody MaterialIssueHdrRequest materialHdrReq) {
		logger.debug("getMaterialIssueDtl   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyIssueService.getMaterialIssueDtl(materialHdrReq);

		} catch (Exception ex) {
			logger.error("getMaterialIssueDtl  method  exception" + ex);
		}
		logger.debug("getMaterialIssueDtl   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("retriveFromIssueStock")
	public ResponseEntity<ResponseAsList> retriveFromIssueStock(@RequestBody MaterialIssueHdrRequest retriveFromStock) {
		logger.debug("retriveFromIssueStock   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyIssueService.retriveFromIssueStock(retriveFromStock);

		} catch (Exception ex) {
			logger.error("retriveFromIssueStock  method  exception" + ex);
		}
		logger.debug("retriveFromIssueStock   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

}
