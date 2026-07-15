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

import com.vmfg.assembly.request.InsertMsHdrAndDtlRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.assembly.services.interfaces.IAssemblyStagingService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

@Controller
@RequestMapping("/")
public class AssemblyStagingContoller {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyStagingContoller.class);

	@Autowired
	private IAssemblyStagingService iAssemblyStagingService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("msHdrRetrieve")
	public ResponseEntity<ResponseAsList> msHdrRetrieve(@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("msHdrRetrieve   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyStagingService.msHdrRetrieve(materialHdrReq);

		} catch (Exception ex) {
			logger.error("msHdrRetrieve  method  exception" + ex);
		}
		logger.debug("msHdrRetrieve   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveMSDtlByHdr")
	public ResponseEntity<ResponseAsList> retrieveMSDtlByHdr(@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("retrieveMSDtlByHdr   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyStagingService.retrieveMSDtlByHdr(materialHdrReq);

		} catch (Exception ex) {
			logger.error("retrieveMSDtlByHdr  method  exception" + ex);
		}
		logger.debug("retrieveMSDtlByHdr   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertMsHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> insertMsHdrAndDtl(@RequestBody InsertMsHdrAndDtlRequest insertMsDtls) {
		logger.debug("insertMsHdrAndDtl   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iAssemblyStagingService.insertMsHdrAndDtl(insertMsDtls);

		} catch (Exception ex) {
			logger.error("insertMsHdrAndDtl  method  exception" + ex);
		}
		logger.debug("insertMsHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("cancelMsHdrReq")
	public ResponseEntity<ResponseAsMessage> cancelMsHdrReq(@RequestBody MaterialReqHdrRequest materialReqHdr) {
		logger.debug("cancelMsHdrReq   method Start");
		ResponseAsMessage respMsg = null;
		try {

			respMsg = iAssemblyStagingService.cancelMsHdrReq(materialReqHdr);

		} catch (Exception ex) {
			logger.error("cancelMsHdrReq  method  exception" + ex);
		}
		logger.debug("cancelMsHdrReq   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveForMS")
	public ResponseEntity<ResponseAsList> retrieveForMS(@RequestBody MaterialReqHdrRequest materialHdrReq) {
		logger.debug("retrieveForMS   method Start");
		ResponseAsList list = null;
		try {

			list = iAssemblyStagingService.retrieveForMS(materialHdrReq);

		} catch (Exception ex) {
			logger.error("retrieveForMS  method  exception" + ex);
		}
		logger.debug("retrieveForMS   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
}
