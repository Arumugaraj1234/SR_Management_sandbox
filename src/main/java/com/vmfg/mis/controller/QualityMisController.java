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

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.request.DrilldownDtlReq;
import com.vmfg.mis.request.QualityWidgetDtlReq;
import com.vmfg.mis.request.QulyProjCntRequest;
import com.vmfg.mis.request.TeamMemberLoadReq;
import com.vmfg.mis.services.interfaces.IQualityMisService;

@Controller
@RequestMapping("/")
public class QualityMisController {

	@Autowired
	IQualityMisService iqualityMisService;
	
	private static final Logger logger = LoggerFactory.getLogger(QualityMisController.class);
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQualityProjCnt")
	public ResponseEntity<ResponseAsMessage> getQualityProjCnt(@RequestBody QulyProjCntRequest qlyProjCnt ){
	
		logger.info("getQualityProjCnt  method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iqualityMisService.getQualityProjCnt(qlyProjCnt);
		} catch (Exception ex) {
			logger.error("getQualityProjCnt  method  exception" + ex);
		}
		logger.debug("getQualityProjCnt  method end");
		return new ResponseEntity<ResponseAsMessage>(resp,HttpStatus.OK);
	}
	

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQualityWidgetDtl")
	public ResponseEntity<ResponseAsList> getQualityWidgetDtl(@RequestBody QualityWidgetDtlReq widgetDtl) {
		
		logger.info("getQualityWidgetDtl Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iqualityMisService.QualityWidgetDtlResp(widgetDtl);
		}catch(Exception e) {
			logger.error("getQualityWidgetDtl Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDrilldownDtl")
	public ResponseEntity<ResponseAsList> getDrilldownDtl(@RequestBody DrilldownDtlReq drillDownDtl) {
		
		logger.info("getDrilldownDtl Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iqualityMisService.getDrilldownDtlResp(drillDownDtl);
		}catch(Exception e) {
			logger.error("getDrilldownDtl Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSupplierRating")
	public ResponseEntity<ResponseAsList> getSupplierRating(@RequestBody QualityWidgetDtlReq widgetDtl) {
		
		logger.info("getSupplierRating Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iqualityMisService.SupplierRatingResp(widgetDtl);
		}catch(Exception e) {
			logger.error("getSupplierRating Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTeamMemberLoad")
	public ResponseEntity<ResponseAsList> getTeamMemberLoad(@RequestBody TeamMemberLoadReq teamLoad) {
		
		logger.info("getTeamMemberLoad Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iqualityMisService.TeamMemberLoadResp(teamLoad);
		}catch(Exception e) {
			logger.error("getTeamMemberLoad Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getVendorByCatAndType")
	public ResponseEntity<ResponseAsList> getVendorByCatAndType(@RequestBody TenantRequest tenantId) {
		
		logger.info("getVendorByCatAndType Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iqualityMisService.getVendorByCatAndType(tenantId);
		}catch(Exception e) {
			logger.error("getVendorByCatAndType Controller  method  exception:" + e);
		}
		
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
}
