package com.vmfg.sales.controller;

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
import com.vmfg.sales.request.ApprovedBtnRequest;
import com.vmfg.sales.request.ChangeRequest;
import com.vmfg.sales.request.GetApprovedDocRequest;
import com.vmfg.sales.request.GetVersionRequest;
import com.vmfg.sales.request.getFileConfigDtlRequest;
import com.vmfg.sales.services.interfaces.IUploadManagementService;

@Controller
@RequestMapping("/")
public class UploadManagementController {
	private static final Logger logger = LoggerFactory.getLogger(UploadManagementController.class);

	@Autowired
	IUploadManagementService iUploadManagementService;
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getApprovedDocDtl")
	public ResponseEntity<ResponseAsList> getApprovedDocDtl(@RequestBody GetApprovedDocRequest getApprovedDocReq ) {
		logger.debug("getApprovedDocDtl  method Start");
	ResponseAsList list = null;
		try {
			list = iUploadManagementService.getApprovedDocDtl(getApprovedDocReq);
		} catch (Exception e) {
			logger.debug("getApprovedDocDtl methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVersionDtls")
	public ResponseEntity<ResponseAsList> getVersionDtls(@RequestBody GetVersionRequest GetVersionReq ) {
		logger.debug("getVersionDtls  method Start");
	ResponseAsList list = null;
		try {
			list = iUploadManagementService.getVersionDtls(GetVersionReq);
		} catch (Exception e) {
			logger.debug("getVersionDtls methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("addDocument")
	public ResponseEntity<ResponseAsMessage> addDocument(@RequestParam("addDocument") String obj,@RequestParam("file") MultipartFile file ) {
		logger.debug("addDocument  method Start");
	ResponseAsMessage list = null;
		try {
			JSONObject jsonObj = new JSONObject(obj);
			list = iUploadManagementService.addDocument(jsonObj,file);
		} catch (Exception e) {
			logger.debug("addDocument methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getFileUploadConfigDtl")
	public ResponseEntity<ResponseAsList> getFileUploadConfigDtl(@RequestBody getFileConfigDtlRequest getFileConfigDtlReq ) {
		logger.debug("getFileUploadConfigDtl  method Start");
	ResponseAsList list = null;
		try {
			list = iUploadManagementService.getFileUploadConfigDtl(getFileConfigDtlReq);
		} catch (Exception e) {
			logger.debug("getFileUploadConfigDtl methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("sumbitApprovedDoc")
	public ResponseEntity<ResponseAsMessage> sumbitApprovedDoc(@RequestBody ApprovedBtnRequest ApprovedBtnReq) {
		logger.debug("sumbitApprovedDoc  method Start");
	ResponseAsMessage list = null;
		try {
			list = iUploadManagementService.sumbitApprovedDoc(ApprovedBtnReq);
		} catch (Exception e) {
			logger.debug("sumbitApprovedDoc methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getChangeRequestInfo")
	public ResponseEntity<ResponseAsList> getChangeRequestInfo(@RequestBody ChangeRequest getChangeReq ) {
		logger.debug("getChangeRequestInfo  method Start");
	ResponseAsList list = null;
		try {
			list = iUploadManagementService.getChangeRequestInfo(getChangeReq);
		} catch (Exception e) {
			logger.debug("getChangeRequestInfo methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

}