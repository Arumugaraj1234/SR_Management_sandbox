package com.vmfg.general.controller;

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

import com.vmfg.design.request.TenantEmpRequest;
import com.vmfg.general.request.DirectApprovalRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.services.interfaces.INotificationGeneralService;

@Controller
@RequestMapping("/")
public class NotificationGeneralController {
	private static final Logger logger = LoggerFactory.getLogger(NotificationGeneralController.class);
	
	@Autowired
	INotificationGeneralService iNotificationGeneralService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getNotificationDetails")
	public ResponseEntity<ResponseAsList> getNotificationDetails(@RequestBody TenantEmpRequest TenantEmpRequest) {
		logger.info("getNotificationDetails   method Start");
		ResponseAsList getNotificationDtls=null;
		try {

			getNotificationDtls = iNotificationGeneralService.getNotificationDetails(TenantEmpRequest);

		} catch (Exception ex) {
			logger.error("getNotificationDetails  method  exception" + ex);
		}
		logger.debug("getNotificationDetails   method end");
		return new ResponseEntity<ResponseAsList>(getNotificationDtls, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getApprovalDesigDtls")
	public ResponseEntity<ResponseAsList> getApprovalDesigDtls(@RequestBody TenantEmpRequest TenantEmpRequest) {
		logger.info("getApprovalDesigDtls   method Start");
		ResponseAsList ApprovalDtls=null;
		try {
			ApprovalDtls = iNotificationGeneralService.getApprovalDesigDtls(TenantEmpRequest);
		} catch (Exception ex) {
			logger.error("getApprovalDesigDtls  method  exception" + ex);
		}
		logger.debug("getApprovalDesigDtls   method end");
		return new ResponseEntity<ResponseAsList>(ApprovalDtls, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateNotificationDtls")
	public ResponseEntity<ResponseAsMessage> updateNotificationDtls(@RequestBody  TenantEmpRequest TenantEmpRequest) {
		logger.info("updateNotificationDtls   method Start");
		ResponseAsMessage returnMsg = null;
		try {
			returnMsg = iNotificationGeneralService.updateNotificationDtls(TenantEmpRequest);
			logger.info("updateNotificationDtls method End");
		} catch (Exception ex) {
			logger.error("updateNotificationDtls Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDirectApprovalDesignNotify")
	public ResponseEntity<ResponseAsMessage> getDirectApprovalDesignNotify(@RequestBody  DirectApprovalRequest directApprovalReq) {
		logger.info("getDirectApprovalDesign method Start");
		ResponseAsMessage returnMsg = null;
		try {
			returnMsg = iNotificationGeneralService.getDirectApprovalDesignNotify(directApprovalReq);
			logger.info("getDirectApprovalDesignNotify method End");
		} catch (Exception ex) {
			logger.error("getDirectApprovalDesignNotify Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
}
