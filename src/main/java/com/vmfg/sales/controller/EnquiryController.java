package com.vmfg.sales.controller;

import org.json.JSONArray;
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

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.entity.ProjectCreationStatusEntity;
import com.vmfg.sales.request.CustomerMstRequest;
import com.vmfg.sales.request.EnqEnablementRequest;
import com.vmfg.sales.request.GetsalebudgetextDtlBySbDtlIdRequest;
import com.vmfg.sales.request.UpdateEnquiryDtlRequest;
import com.vmfg.sales.request.UpdatesalesBudgetHdrRequest;
import com.vmfg.sales.services.interfaces.IEnquiryService;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;

@Controller
@RequestMapping("/")
public class EnquiryController {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryController.class);

	@Autowired
	IEnquiryService iEnquiryService;



	@CrossOrigin(maxAge = 3600)
	@PostMapping("UpdateEnquiryDtl")
	public ResponseEntity<ResponseAsMessage> UpdateEnquiryDtl(@RequestBody UpdateEnquiryDtlRequest updateEnquiryDtlReq) {
		logger.info("UpdateEnquiryDtl   method Start");
		ResponseAsMessage departmentInfoEntity=null;
		try {

			departmentInfoEntity = iEnquiryService.UpdateEnquiryDtl(updateEnquiryDtlReq);

		} catch (Exception ex) {
			logger.error("DepartmentAndEmployeeInfoController  method  exception" + ex);
		}
		logger.debug("DepartmentAndEmployeeInfoController   method end");
		return new ResponseEntity<ResponseAsMessage>(departmentInfoEntity, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteSaleEnqContact")
	public ResponseEntity<ResponseAsMessage>deleteSaleEnqContact(@RequestBody HdrIdandTenantIdRequest HdrIdandTenantIdReq) {
		logger.info("deleteSaleEnqContact   method Start");
		ResponseAsMessage departmentInfoEntity=null;
		try {

			departmentInfoEntity = iEnquiryService.deleteSaleEnqContact(HdrIdandTenantIdReq);

		} catch (Exception ex) {
			logger.error("deleteSaleEnqContact method  exception" + ex);
		}
		logger.debug("deleteSaleEnqContact method end");
		return new ResponseEntity<ResponseAsMessage>(departmentInfoEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("uploadBudgetSheetfile")
	public ResponseEntity<ResponseAsList> uploadBudgetSheetfile(@RequestParam("UploadFileReq") String uploadFileReq,
			@RequestParam("file") MultipartFile file) {
		logger.info("uploadBudgetSheetfile Controller  method Start");
		ResponseAsList list = null;
		try {
			JSONObject jsonObj = new JSONObject(uploadFileReq);
			JSONArray getArray = jsonObj.getJSONArray("reqObj");
			list = iEnquiryService.uploadBudgetSheetfile(getArray, file);
		} catch (Exception ex) {
			logger.error("uploadBudgetSheetfile Controller  method  exception:" + ex);
		}
		logger.info("uploadBudgetSheetfile Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjectCreationStatus")
	public ResponseEntity<ResponseAsMessage> getProjectCreationStatus(@RequestBody ProjectCreationStatusEntity projectCreationStatus) {
		logger.info("getProjectCreationStatus Controller  method Start");
		ResponseAsMessage list = null;
		try {

			list = iEnquiryService.getProjectCreationStatus(projectCreationStatus);
		} catch (Exception ex) {
			logger.error("getProjectCreationStatus Controller  method  exception:" + ex);
		}
		logger.info("getProjectCreationStatus Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getsalesBudgetHdrDtl")
	public ResponseEntity<ResponseAsList> getsalesBudgetHdrDtl(@RequestBody ProjectCreationStatusEntity projectCreationStatus) {
		logger.info("getsalesBudgetHdrDtl Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iEnquiryService.getsalesBudgetHdrDtl(projectCreationStatus);
		} catch (Exception ex) {
			logger.error("getsalesBudgetHdrDtl Controller  method  exception:" + ex);
		}
		logger.info("getsalesBudgetHdrDtl Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updatesalesBudgetHdr")
	public ResponseEntity<ResponseAsMessage> updatesalesBudgetHdr(@RequestBody UpdatesalesBudgetHdrRequest updatesalesBudgetHdrReq) {
		logger.info("updatesalesBudgetHdr Controller  method Start");
		ResponseAsMessage list = null;
		try {

			list = iEnquiryService.updatesalesBudgetHdr(updatesalesBudgetHdrReq);
		} catch (Exception ex) {
			logger.error("updatesalesBudgetHdr Controller  method  exception:" + ex);
		}
		logger.info("updatesalesBudgetHdr Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getsalebudgetextDtlBySbDtlId")
	public ResponseEntity<ResponseAsList> getsalebudgetextDtlBySbDtlId(@RequestBody GetsalebudgetextDtlBySbDtlIdRequest getsalebudgetextDtlBySbDtlIdReq) {
		logger.info("getsalebudgetextDtlBySbDtlId Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iEnquiryService.getsalebudgetextDtlBySbDtlId(getsalebudgetextDtlBySbDtlIdReq);
		} catch (Exception ex) {
			logger.error("getsalebudgetextDtlBySbDtlId Controller  method  exception:" + ex);
		}
		logger.info("getsalebudgetextDtlBySbDtlId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEnqEnablement")
	public ResponseEntity<ResponseAsMessage> getEnqEnablement(@RequestBody EnqEnablementRequest enqEnablementRequest) {
		logger.info("getEnqEnablement Controller  method Start");
		ResponseAsMessage list = null;
		try {

			list = iEnquiryService.getEnqEnablement(enqEnablementRequest);
		} catch (Exception ex) {
			logger.error("getEnqEnablement Controller  method  exception:" + ex);
		}
		logger.info("getEnqEnablement Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getCustomermst")
	public ResponseEntity<ResponseAsList> getCustomerMst(@RequestBody CustomerMstRequest customerMstReq ) {
		logger.info("getCustomermst Controller  method Start");
		ResponseAsList list = null;
		try {

			list = iEnquiryService.getCustomerMst(customerMstReq);
		} catch (Exception ex) {
			logger.error("getCustomermst Controller  method  exception:" + ex);
		}
		logger.info("getCustomermst Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
}
