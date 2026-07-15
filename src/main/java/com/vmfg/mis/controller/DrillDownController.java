package com.vmfg.mis.controller;

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
import com.vmfg.mis.request.DelayedIndentRequest;
import com.vmfg.mis.request.DrillDownRequest;
import com.vmfg.mis.services.interfaces.IDrillDownService;

@Controller
@RequestMapping("/")
public class DrillDownController {
	
	@Autowired
	IDrillDownService iDrillDownService;
	
	private static final Logger logger = LoggerFactory.getLogger(DrillDownController.class);

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentByNotAvailablePO")
	public ResponseEntity<ResponseAsList> getIndentByNotAvailablePO(@RequestBody DrillDownRequest drillDownRequest){
	
		logger.info("getIndentByNotAvailablePO  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iDrillDownService.getIndentByNotAvailablePO(drillDownRequest);
		} catch (Exception ex) {
			logger.error("getIndentByNotAvailablePO  method  exception" + ex);
		}
		logger.debug("getIndentByNotAvailablePO  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentByInventoryStock")
	public ResponseEntity<ResponseAsList> getIndentByInventoryStock(@RequestBody DrillDownRequest drillDownRequest){
	
		logger.info("getIndentByInventoryStock  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iDrillDownService.getIndentByInventoryStock(drillDownRequest);
		} catch (Exception ex) {
			logger.error("getIndentByInventoryStock  method  exception" + ex);
		}
		logger.debug("getIndentByInventoryStock  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDelayedIndent")
	public ResponseEntity<ResponseAsList> getDelayedIndent(@RequestBody DrillDownRequest delayedIndentRequest){
	
		logger.info("getDelayedIndent  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iDrillDownService.getDelayedIndent(delayedIndentRequest);
		} catch (Exception ex) {
			logger.error("getDelayedIndent  method  exception" + ex);
		}
		logger.debug("getDelayedIndent  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPoInitalValue")
	public ResponseEntity<ResponseAsList> getPoInitalValue(@RequestBody DelayedIndentRequest getPoInitalValueRequest){
	
		logger.info("getPoInitalValue  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iDrillDownService.getPoInitalValue(getPoInitalValueRequest);
		} catch (Exception ex) {
			logger.error("getPoInitalValue  method  exception" + ex);
		}
		logger.debug("getPoInitalValue  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getInventoryValueDrill")
	public ResponseEntity<ResponseAsList> getInventoryValueDrill(@RequestBody DelayedIndentRequest getgetInventoryValueRequest){
	
		logger.info("getInventoryValueDrill  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iDrillDownService.getInventoryValueDrill(getgetInventoryValueRequest);
		} catch (Exception ex) {
			logger.error("getInventoryValueDrill  method  exception" + ex);
		}
		logger.debug("getInventoryValueDrill  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getNumberOfPoDrill")
	public ResponseEntity<ResponseAsList> getNumberOfPoDrill(@RequestBody DelayedIndentRequest getgetInventoryValueRequest){
	
		logger.info("getNumberOfPoDrill  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp = iDrillDownService.getNumberOfPoDrill(getgetInventoryValueRequest);
		} catch (Exception ex) {
			logger.error("getNumberOfPoDrill  method  exception" + ex);
		}
		logger.debug("getNumberOfPoDrill  method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
}