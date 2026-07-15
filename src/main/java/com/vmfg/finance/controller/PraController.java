package com.vmfg.finance.controller;

import com.vmfg.finance.request.*;
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

import com.vmfg.finance.services.interfaces.IPraService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

@Controller
@RequestMapping("/")
public class PraController {
	private static final Logger logger = LoggerFactory.getLogger(PraController.class);

	@Autowired
	private IPraService iPraService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("InsertPRA")
	public ResponseEntity<ResponseAsMessage> insertPRA(@RequestBody PraInsertRequest praInsertRequest) {
		logger.info("InsertPRA   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iPraService.insertPRA(praInsertRequest);
		} catch (Exception ex) {
			logger.error("insertPRA  method  exception" + ex);
		}
		logger.info("InsertPRA   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPraDtl")
	public ResponseEntity<ResponseAsList> getPraDtl(@RequestBody getPraDtlRequest getPraDtlReq) {
		logger.info("getPraDtl   method Start");
		ResponseAsList resp = null;
		try {
			resp = iPraService.getPraDtl(getPraDtlReq);
		} catch (Exception ex) {
			logger.error("getPraDtl  method  exception" + ex);
		}
		logger.info("getPraDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("praCancel")
	public ResponseEntity<ResponseAsMessage> praCancel(@RequestBody PraCancelRequest praCancelRequest) {
		logger.info("praCancel  method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iPraService.praCancel(praCancelRequest);
		} catch (Exception ex) {
			logger.error("praCancel  method  exception" + ex);
		}
		logger.info("praCancel  method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrievePRA")
	public ResponseEntity<ResponseAsList> retrievePRA(@RequestBody RetrievePraRequest RetrievePraReq) {
		logger.info("retrievePRA   method Start");
		ResponseAsList resp = null;
		try {
			resp = iPraService.retrievePRA(RetrievePraReq);
		} catch (Exception ex) {
			logger.error("retrievePRA  method  exception" + ex);
		}
		logger.info("retrievePRA   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("udpatePraHdrSeq")
	public ResponseEntity<ResponseAsMessage> udpatePraHdrSeq(@RequestBody UdpatePraHdrSeqRequest udpatePraHdrSeqReq) {
		logger.info("udpatePraHdrSeq   method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iPraService.udpatePraHdrSeq(udpatePraHdrSeqReq);
		} catch (Exception ex) {
			logger.error("udpatePraHdrSeq  method  exception" + ex);
		}
		logger.info("udpatePraHdrSeq   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
}
