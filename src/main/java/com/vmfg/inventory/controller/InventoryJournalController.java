package com.vmfg.inventory.controller;

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
import com.vmfg.inventory.request.InventoryJournalRequest;
import com.vmfg.inventory.request.InventoryTenantRequest;
import com.vmfg.inventory.servisec.interfaces.IInventoryJournalService;

@Controller
@RequestMapping("/")
public class InventoryJournalController {
	
	@Autowired
	private IInventoryJournalService iInventoryJournalService;
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryJournalController.class);
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveinventoryJournal")
	public ResponseEntity<ResponseAsList> retrieveinventoryJournal(@RequestBody InventoryJournalRequest projectdtlreq) {
		
		logger.info("retrieveInvntoryJournal Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryJournalService.retrieveinventoryJournal(projectdtlreq);
		}catch(Exception e) {
			logger.error("retrieveInvntoryJournal Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getInvLocationForInward")
	public ResponseEntity<ResponseAsList> getInvLocationForInward(@RequestBody InventoryTenantRequest tenantReq) {
		
		logger.info("getInvLocationForInward Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryJournalService.getInvLocationForInward(tenantReq);
		}catch(Exception e) {
			logger.error("getInvLocationForInward Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}

}
