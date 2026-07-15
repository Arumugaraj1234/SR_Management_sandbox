
package com.vmfg.master.controller;

import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.entity.CustomerMstEntity;
import com.vmfg.master.request.CustomerComplaintCheck;
import com.vmfg.master.request.InsertVendorReq;
import com.vmfg.master.request.VendorAllDtlReq;
import com.vmfg.master.request.VendorApprDtlReq;
import com.vmfg.master.request.VendorInspRatingRequest;
import com.vmfg.master.services.interfaces.IVendorService;

@Controller
@RequestMapping("/")
public class VendorController {
	private static final Logger logger = LoggerFactory.getLogger(VendorController.class);
	@Autowired
	IVendorService iVendorService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getApprVendorDtls")
	public ResponseEntity<ResponseAsList> getApprVendorDtls(@RequestBody VendorApprDtlReq vendorApprDtlReq) {
		logger.info("getApprVendorDtls method Start");
		ResponseAsList list = null;
		try {

			list = iVendorService.getApprVendorDtls(vendorApprDtlReq);

		} catch (Exception ex) {
			logger.error("getApprVendorDtls  method  exception" + ex);
		}
		logger.debug("getApprVendorDtls method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertVendorDtls")
	public ResponseEntity<ResponseAsMessage> insertVendorDtls(@RequestBody InsertVendorReq vendorInsertDtlReq) {
		logger.info("insertVendorDtls method Start");
		ResponseAsMessage list = null;
		try {

			list = iVendorService.insertVendorDtls(vendorInsertDtlReq);

		} catch (Exception ex) {
			logger.error("insertVendorDtls  method  exception" + ex);
		}
		logger.debug("insertVendorDtls method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllVendorDtls")
	public ResponseEntity<ResponseAsList> getAllVendorDtls(@RequestBody VendorAllDtlReq vendorApprDtlReq) {
		logger.info("getAllVendorDtls method Start");
		ResponseAsList list = null;
		try {

			list = iVendorService.getAllVendorDtls(vendorApprDtlReq);

		} catch (Exception ex) {
			logger.error("getAllVendorDtls  method  exception" + ex);
		}
		logger.debug("getAllVendorDtls method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllCustomerDtl")
	public ResponseEntity<ResponseAsList> getAllCustomerDtl(@RequestBody TenantRequest tenantRequest) {
		logger.info("getAllCustomerDtl method Start");
		ResponseAsList list = null;
		try {

			list = iVendorService.getAllCustomerDtl(tenantRequest);

		} catch (Exception ex) {
			logger.error("getAllCustomerDtl  method  exception" + ex);
		}
		logger.debug("getAllCustomerDtl method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorCategory")
	public ResponseEntity<ResponseAsList> getVendorCategory(@RequestBody TenantRequest tenantRequest) {
		logger.info("getVendorCategory method Start");
		ResponseAsList list = null;
		try {

			list = iVendorService.getVendorCategory(tenantRequest);

		} catch (Exception ex) {
			logger.error("getVendorCategory  method  exception" + ex);
		}
		logger.debug("getVendorCategory method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateInspectionRaised")
	public ResponseEntity<ResponseAsMessage> updateInspectionRaised(@RequestBody VendorInspRatingRequest req) {
		logger.info("updateInspectionRaised method Start");
		ResponseAsMessage list = null;
		try {

			list = iVendorService.updateInspectionRaised(req);

		} catch (Exception ex) {
			logger.error("updateInspectionRaised  method  exception" + ex);
		}
		logger.debug("updateInspectionRaised method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorInspRatingDtls")
	public ResponseEntity<ResponseAsList> getVendorInspRatingDtls(@RequestBody VendorInspRatingRequest req) {
		logger.info("getVendorInspRatingDtls method Start");
		ResponseAsList list = null;
		try {

			list = iVendorService.getVendorInspRatingDtls(req);

		} catch (Exception ex) {
			logger.error("getVendorInspRatingDtls  method  exception" + ex);
		}
		logger.debug("getVendorInspRatingDtls method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("vendorDtlInsert")
	public ResponseEntity<ResponseAsMessage> vendorDtlInsert(@RequestParam("vendorRatingReq") String req, @RequestParam("file") MultipartFile file) {
		logger.info("vendorDtlInsert method Start");
		ResponseAsMessage list = null;
		try {
			JSONObject jsonObj = new JSONObject(req);
			list = iVendorService.vendorDtlInsert(jsonObj,file);

		} catch (Exception ex) {
			logger.error("vendorDtlInsert  method  exception" + ex);
		}
		logger.debug("vendorDtlInsert method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("customerComplaintCheck")
	public ResponseEntity<ResponseAsMessage> customerComplaintCheckupdate(@RequestBody CustomerComplaintCheck customerReq) {
		logger.info("vendorDtlInsert method Start");
		ResponseAsMessage list = null;
		try {
			list = iVendorService.customerComplaintCheck(customerReq);

		} catch (Exception ex) {
			logger.error("vendorDtlInsert  method  exception" + ex);
		}
		logger.debug("vendorDtlInsert method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("UpdateCustMstDtl")
	public ResponseEntity<ResponseAsMessage> updateCustomerdtl(@RequestBody CustomerMstEntity updateCus) {
		logger.info("CustomerMstDtlUpdate method Start");
		ResponseAsMessage list = null;
		try {
			list = iVendorService.updateCustomerdtl(updateCus);

		} catch (Exception ex) {
			logger.error("CustomerMstDtlUpdate  method  exception" + ex);
		}
		logger.debug("CustomerMstDtlUpdate method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
}
