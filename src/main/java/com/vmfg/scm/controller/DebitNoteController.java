package com.vmfg.scm.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.vmfg.finance.request.RetrievePraRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.scm.request.DebitNoteHdrAndDtlRequest;
import com.vmfg.scm.services.interfaces.IDebitNoteService;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class DebitNoteController {
	private static final Logger logger = LoggerFactory.getLogger(DebitNoteController.class);

	@Autowired
	private IDebitNoteService iDebitNoteService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertDebitNoteHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> insertDebitNoteHdrAndDtl(@RequestBody DebitNoteHdrAndDtlRequest debitNoteRequest) {
		logger.debug("insertDebitNoteHdrAndDtl   method Start");
		ResponseAsMessage respMsg= new ResponseAsMessage();
		try {

			respMsg = iDebitNoteService.insertDebitNoteHdrAndDtl(debitNoteRequest);

		} catch (Exception ex) {
			logger.error("insertDebitNoteHdrAndDtl  method  exception" + ex);
		}
		logger.debug("insertDebitNoteHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateDebitNoteHdr")
	public ResponseEntity<ResponseAsMessage> updateDebitNoteHdr(@RequestBody DebitNoteHdrAndDtlRequest debitNoteRequest) {
		logger.debug("updateDebitNoteHdr   method Start");
		ResponseAsMessage respMsg=new ResponseAsMessage();
		try {

			respMsg = iDebitNoteService.updateDebitNoteHdr(debitNoteRequest);

		} catch (Exception ex) {
			logger.error("updateDebitNoteHdr  method  exception" + ex);
		}
		logger.debug("updateDebitNoteHdr   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveDebitNote")
	public ResponseEntity<ResponseAsList> retrieveDebitNote(@RequestBody RetrievePraRequest RetrievePraReq) {
		logger.info("retrieveDebitNote   method Start");
		ResponseAsList resp = null;
		try {
			resp = iDebitNoteService.retrieveDebitNote(RetrievePraReq);
		} catch (Exception ex) {
			logger.error("retrieveDebitNote  method  exception" + ex);
		}
		logger.info("retrieveDebitNote   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertDebitNoteFile")
	public ResponseEntity<ResponseAsMessage> insertDebitNoteFileByDnID(@RequestParam("insertDocRequest") String insertDocReq, @RequestParam("file") MultipartFile file) {
		logger.debug("insertDebitNoteFile  method Start");
		ResponseAsMessage list = null;
		try {
			JSONObject jsonObj = new JSONObject(insertDocReq);
			list = iDebitNoteService.insertDebitNoteFileByDnID(jsonObj, file);
		} catch (Exception e) {
			logger.debug("insertDebitNoteFile methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
}
