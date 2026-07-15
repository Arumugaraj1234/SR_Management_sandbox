package com.vmfg.scm.controller;

import java.util.List;

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
import com.vmfg.scm.entity.MaterialInwardPoDtl;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.MaterialInwardHdrRequest;
import com.vmfg.scm.services.interfaces.IMaterialInwardService;

@Controller
@RequestMapping("/")
public class MaterialInwardController {
	private static final Logger logger = LoggerFactory.getLogger(MaterialInwardController.class);
	@Autowired
	IMaterialInwardService iMaterialInwardService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getMaterialInwardHdrDtls")
	public ResponseEntity<ResponseAsList> getMaterialInwardHdrDtls(@RequestBody MaterialInwardHdrRequest materialInwardHdrReq) {
		logger.debug("getMaterialInwardHdrDtls   method Start");
		ResponseAsList list=null;
		try {

			list = iMaterialInwardService.getMaterialInwardHdrDtls(materialInwardHdrReq);

		} catch (Exception ex) {
			logger.error("getMaterialInwardHdrDtls  method  exception" + ex);
		}
		logger.debug("getMaterialInwardHdrDtls   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getMaterialInwardDtlList")
	public ResponseEntity<ResponseAsList> getMaterialInwardDtlList(@RequestBody HdrIdandTenantIdRequest hdrIdandTenantIdReq) {
		logger.debug("getMaterialInwardDtlList   method Start");
		ResponseAsList list=null;
		try {

			list = iMaterialInwardService.getMaterialInwardDtlList(hdrIdandTenantIdReq);

		} catch (Exception ex) {
			logger.error("getMaterialInwardDtlList  method  exception" + ex);
		}
		logger.debug("getMaterialInwardDtlList   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertMaterialInwardDtls")
	public ResponseEntity<ResponseAsMessage> insertMaterialInwardDtls(@RequestBody List<MaterialInwardPoDtl> materialInwardPoDtl) {
		logger.debug("insertMaterialInwardDtls   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iMaterialInwardService.insertMaterialInwardDtls(materialInwardPoDtl);

		} catch (Exception ex) {
			logger.error("insertMaterialInwardDtls  method  exception" + ex);
		}
		logger.debug("insertMaterialInwardDtls   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPoDtlsForMaterialInward")
	public ResponseEntity<ResponseAsList> getMaterialInwardDtl(@RequestBody HdrIdandTenantIdRequest hdrIdandTenantIdReq) {
		logger.debug("getPoDtlsForMaterialInward method Start");
		ResponseAsList list=null;
		try {

			list = iMaterialInwardService.getPoDtlsForMaterialInward(hdrIdandTenantIdReq);

		} catch (Exception ex) {
			logger.error("getPoDtlsForMaterialInward method  exception" + ex);
		}
		logger.debug("getPoDtlsForMaterialInward method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	
	
}
