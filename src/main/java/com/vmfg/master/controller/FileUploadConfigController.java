package com.vmfg.master.controller;

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
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.request.DocTypeMstRequest;
import com.vmfg.master.request.FileUploadConfigRequest;
import com.vmfg.master.request.InsertFileUploadConfigRequest;
import com.vmfg.master.services.interfaces.IFileUploadConfigService;

@Controller
@RequestMapping("/")
public class FileUploadConfigController{
	private static final Logger logger = LoggerFactory.getLogger(FileUploadConfigController.class);

	@Autowired IFileUploadConfigService iFileUploadConfigService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("docTypeMstDropDwn")
	public  ResponseEntity<ResponseAsList> docTypeMstDropDwn (@RequestBody DocTypeMstRequest req ){
		logger.info("docTypeMstDropDwn   method Start");
		ResponseAsList docTypeDrpDown = new ResponseAsList();
		try {

			docTypeDrpDown = iFileUploadConfigService.docTypeMstDropDwn(req);

		} catch (Exception ex) {
			logger.error("docTypeMstDropDwn  method  exception" + ex);
		}
		logger.debug("docTypeMstDropDwn   method end");
		return new ResponseEntity<ResponseAsList>(docTypeDrpDown, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getFileUploadConfig")
	public  ResponseEntity<ResponseAsList> getFileUploadConfig (@RequestBody FileUploadConfigRequest fileUpload ){
		logger.info("getFileUploadConfig method Start");
		ResponseAsList fileConfig = new ResponseAsList();
		try {

			fileConfig = iFileUploadConfigService.getFileUploadConfig(fileUpload);

		} catch (Exception ex) {
			logger.error("getFileUploadConfig method  exception" + ex);
		}
		logger.debug("getFileUploadConfig method end");
		return new ResponseEntity<ResponseAsList>(fileConfig, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertUpdateFileUploadConfig")
	public ResponseEntity<ResponseAsMessage> insertUpdateFileUploadConfig(@RequestBody InsertFileUploadConfigRequest insertDtlreq) {
		logger.info("insertUpdateFileUploadConfig method Start");
		ResponseAsMessage list = new ResponseAsMessage();
		try {
			list = iFileUploadConfigService.insertUpdateFileUploadConfig(insertDtlreq);
	   	} catch (Exception ex) {
			logger.error("insertUpdateFileUploadConfig  method  exception" + ex);
		}
		logger.debug("insertUpdateFileUploadConfig method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
}