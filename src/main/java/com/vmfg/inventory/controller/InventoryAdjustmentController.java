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

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.inventory.request.InsertAdjustmentRequest;
import com.vmfg.inventory.servisec.interfaces.IInventoryAdjustmentService;
import com.vmfg.scm.request.ProjectDtlRequest;


@Controller
@RequestMapping("/")
public class InventoryAdjustmentController {
	@Autowired
	private IInventoryAdjustmentService iInventoryAdjustmentService;
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryMaterialTransferController.class);
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveinventoryAdjustment")
	public ResponseEntity<ResponseAsList> retrieveinventoryAdjustment(@RequestBody ProjectDtlRequest projectdtlreq) {
		
		logger.info("retrieveinventoryAdjustment Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryAdjustmentService.retrieveinventoryAdjustment(projectdtlreq);
		}catch(Exception e) {
			logger.error("retrieveinventoryAdjustment Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getadjustmettypedropdown")
	public ResponseEntity<ResponseAsList> getadjustmettypedropdown(@RequestBody TenantRequest tenanttreq) {
		
		logger.info("getadjustmettypedropdown Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryAdjustmentService.getadjustmettypedropdown(tenanttreq);
		}catch(Exception e) {
			logger.error("getadjustmettypedropdown Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertAdjustment")
	public ResponseEntity<ResponseAsMessage> insertAdjustment(@RequestBody InsertAdjustmentRequest insertadjustreq) {
		
		logger.info("getAvailableQty Controller  method Start");
		ResponseAsMessage resp = new ResponseAsMessage();
		try {
			resp=iInventoryAdjustmentService.insertAdjustment(insertadjustreq);
		}catch(Exception e) {
			logger.error("getAvailableQty Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
		
	}

}
