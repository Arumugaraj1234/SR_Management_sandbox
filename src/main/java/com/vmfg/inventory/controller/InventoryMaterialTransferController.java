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
import com.vmfg.inventory.request.AvailableProductsForTransferRequest;
import com.vmfg.inventory.request.AvailableQtyRequest;
import com.vmfg.inventory.request.BinDropDownRequest;
import com.vmfg.inventory.request.BinsForProductsRequest;
import com.vmfg.inventory.request.InsertMaterialTransferBatchRequest;
import com.vmfg.inventory.request.ProductDropDownRequest;
import com.vmfg.inventory.request.getQtyAvailLocRequest;
import com.vmfg.inventory.servisec.interfaces.IInventoryMaterialTransferService;
import com.vmfg.scm.request.PMInvReq;
import com.vmfg.scm.request.ProjectDtlRequest;

@Controller
@RequestMapping("/")
public class InventoryMaterialTransferController {
	
	@Autowired
	private IInventoryMaterialTransferService iInventoryMaterialService;
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryMaterialTransferController.class);
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("retrieveinventoryMaterial")
	public ResponseEntity<ResponseAsList> retrieveinventoryMaterial(@RequestBody ProjectDtlRequest projectdtlreq) {
		
		logger.info("retrieveInvntoryMaterial Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryMaterialService.retrieveinventoryMaterial(projectdtlreq);
		}catch(Exception e) {
			logger.error("retrieveInvntoryJournal Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("gettProjectdropdown")
	public ResponseEntity<ResponseAsList> getProjectdropdown(@RequestBody TenantRequest tenanttreq) {
		
		logger.info("getProjectdropdown Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryMaterialService.getProjectdropdown(tenanttreq);
			logger.info("getProjectdropdown Controller  method End");
		}catch(Exception e) {
			logger.error("getProjectdropdown Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjdtlOrdById")
	public ResponseEntity<ResponseAsList> getProjdtlOrdById(@RequestBody TenantRequest tenanttreq) {
		
		logger.info("getProjdtlOrdById Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryMaterialService.getProjdtlOrdById(tenanttreq);
			logger.info("getProjdtlOrdById Controller  method End");
		}catch(Exception e) {
			logger.error("getProjdtlOrdById Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProductdropdown")
	public ResponseEntity<ResponseAsList> getProductdropdown(@RequestBody ProductDropDownRequest productdtldropdowntreq) {
		
		logger.info("getProductdropdown Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryMaterialService.getProductdropdown(productdtldropdowntreq);
			
		}catch(Exception e) {
			logger.error("getProductdropdown Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAvailableProductsForTransfer")
	public ResponseEntity<ResponseAsList> getAvailableProductsForTransfer(@RequestBody AvailableProductsForTransferRequest availableProductsForTransferReq) {

		logger.info("getAvailableProductsForTransfer Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryMaterialService.getAvailableProductsForTransfer(availableProductsForTransferReq);
		}catch(Exception e) {
			logger.error("getAvailableProductsForTransfer Controller  method  exception:" + e);
		}

		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);

	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getBins")
	public ResponseEntity<ResponseAsList> getBins(@RequestBody BinDropDownRequest binDropDownRequest) {
	    
	    logger.info("getBins Controller method Start");
	    ResponseAsList resp = new ResponseAsList();
	    try {
	        resp = iInventoryMaterialService.getBins(binDropDownRequest);
	    } catch (Exception e) {
	        logger.error("getBins Controller method exception: " + e);
	    }

	    return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getBinsForProducts")
	public ResponseEntity<ResponseAsList> getBinsForProducts(@RequestBody BinsForProductsRequest binsForProductsRequest) {

	    logger.info("getBinsForProducts Controller method Start");
	    ResponseAsList resp = new ResponseAsList();
	    try {
	        resp = iInventoryMaterialService.getBinsForProducts(binsForProductsRequest);
	    } catch (Exception e) {
	        logger.error("getBinsForProducts Controller method exception: " + e);
	    }

	    return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getLocationdropdown")
	public ResponseEntity<ResponseAsList> getLocationdropdown(@RequestBody TenantRequest tenanttreq) {
		
		logger.info("getLocationdropdown Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryMaterialService.getLocationdropdown(tenanttreq);
		}catch(Exception e) {
			logger.error("getLocationdropdown Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQtyAvailableLocations")
	public ResponseEntity<ResponseAsList> getQtyAvailableLocations(@RequestBody getQtyAvailLocRequest getQtyAvailLocReq) {
		
		logger.info("getQtyAvailableLocations Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iInventoryMaterialService.getQtyAvailableLocations(getQtyAvailLocReq);
		}catch(Exception e) {
			logger.error("getQtyAvailableLocations Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAvailableQty")
	public ResponseEntity<String> getAvailableQty(@RequestBody AvailableQtyRequest availableqtyreq) {
		
		logger.info("getAvailableQty Controller  method Start");
		String resp = "";
		try {
			resp=iInventoryMaterialService.getAvailableQty(availableqtyreq);
		}catch(Exception e) {
			logger.error("getAvailableQty Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<String>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertMaterialTransfer")
	public ResponseEntity<ResponseAsMessage> insertMaterialTransfer(@RequestBody InsertMaterialTransferBatchRequest insertmaterialreq) {
		
		logger.info("getAvailableQty Controller  method Start");
		ResponseAsMessage resp = new ResponseAsMessage();
		try {
			resp=iInventoryMaterialService.insertMaterialTransfer(insertmaterialreq);
		}catch(Exception e) {
			logger.error("getAvailableQty Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
		
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPmInv")
	public ResponseEntity<ResponseAsList> getPmInv(@RequestBody PMInvReq invReq) {
		logger.debug("getPmInv method Start");
		ResponseAsList list=null;
		try {

			list = iInventoryMaterialService.getPmInv(invReq);

		} catch (Exception ex) {
			logger.error("getPmInv method  exception" + ex);
		}
		logger.debug("getPmInv method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPmInvDtl")
	public ResponseEntity<ResponseAsList> getPmInvDtl(@RequestBody PMInvReq invReq) {
		logger.debug("getPmInvDtl method Start");
		ResponseAsList list=null;
		try {

			list = iInventoryMaterialService.getPmInvDtl(invReq);

		} catch (Exception ex) {
			logger.error("getPmInvDtl method  exception" + ex);
		}
		logger.debug("getPmInvDtl method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

}
