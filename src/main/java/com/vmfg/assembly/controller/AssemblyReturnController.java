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

import com.vmfg.assembly.request.InsertMrHdrAndDtlReq;
import com.vmfg.assembly.request.MaterialReqDtlRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.assembly.request.MaterialReturnAcceptRequest;
import com.vmfg.assembly.services.interfaces.IAssemblyReturnService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

@Controller
@RequestMapping("/")
public class AssemblyReturnController {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyReturnController.class);
	@Autowired
	private IAssemblyReturnService iAssemblyReturnService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertMRHAndMRD")
	public ResponseEntity<ResponseAsMessage> insertMRHAndMRD(@RequestBody InsertMrHdrAndDtlReq insertMsDtls) {
		logger.debug("insertMrHdrAndDtl   method Start");
		ResponseAsMessage respMsg = null;

		try {

			respMsg = iAssemblyReturnService.insertMRHAndMRD(insertMsDtls);

		} catch (Exception ex) {
			logger.error("insertMrHdrAndDtl  method  exception" + ex);
		}
		logger.debug("insertMrHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("mrHdrRetrieve")
	public ResponseEntity<ResponseAsList> mrHdrRetrieve(@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("mrHdrRetrieve   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyReturnService.mrHdrRetrieve(materialHdrReq);

		} catch (Exception ex) {
			logger.error("mrHdrRetrieve  method  exception" + ex);
		}
		logger.debug("mrHdrRetrieve   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveMreturnDtlByHdr")
	public ResponseEntity<ResponseAsList> retrieveMreturnDtlByHdr(@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("retrieveMreturnDtlByHdr   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyReturnService.retrieveMreturnDtlByHdr(materialHdrReq);

		} catch (Exception ex) {
			logger.error("retrieveMreturnDtlByHdr  method  exception" + ex);
		}
		logger.debug("retrieveMreturnDtlByHdr   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveApprovedGroupReturnsByProject")
	public ResponseEntity<ResponseAsList> retrieveApprovedGroupReturnsByProject(
			@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("retrieveApprovedGroupReturnsByProject   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyReturnService.retrieveApprovedGroupReturnsByProject(materialHdrReq);

		} catch (Exception ex) {
			logger.error("retrieveApprovedGroupReturnsByProject  method  exception" + ex);
		}
		logger.debug("retrieveApprovedGroupReturnsByProject   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("ApproveMreturnDtls")
	public ResponseEntity<ResponseAsMessage> ApproveMreturnDtls(@RequestBody MaterialReqDtlRequest materialDtlReq) {
		logger.debug("ApproveMreturnDtls method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iAssemblyReturnService.ApproveMreturnDtls(materialDtlReq);

		} catch (Exception ex) {
			logger.error("ApproveMreturnDtls  method  exception" + ex);
		}
		logger.debug("ApproveMreturnDtls method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("cancelMaterialReturnHdr")
	public ResponseEntity<ResponseAsMessage> cancelMaterialReturnHdr(
			@RequestBody MaterialReqHdrRequest materialReqHdr) {
		logger.debug("cancelMaterialReturnHdr   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iAssemblyReturnService.cancelMaterialReturnHdr(materialReqHdr);

		} catch (Exception ex) {
			logger.error("cancelMaterialReturnHdr  method  exception" + ex);
		}
		logger.debug("cancelMaterialReturnHdr   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("materialReturnAccept")
	public ResponseEntity<ResponseAsMessage> materialReturnAccept(
			@RequestBody MaterialReturnAcceptRequest materialAccept) {
		logger.debug("materialReturnAccept   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iAssemblyReturnService.materialReturnAccept(materialAccept);

		} catch (Exception ex) {
			logger.error("materialReturnAccept  method  exception" + ex);
		}
		logger.debug("materialReturnAccept   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

}
