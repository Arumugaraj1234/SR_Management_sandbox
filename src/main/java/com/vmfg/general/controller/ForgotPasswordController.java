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

import com.vmfg.general.request.ResetPasswordRequest;
import com.vmfg.general.request.getOtpRequest;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.services.interfaces.IForgotPasswordService;

@Controller
@RequestMapping("/")
public class ForgotPasswordController {
	private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

	@Autowired
	private IForgotPasswordService iForgotPasswordService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("generateOtp")
	public ResponseEntity<ResponseAsMessage> generateOtp(@RequestBody getOtpRequest getOtpReq) {
		logger.info("generateOtp  method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iForgotPasswordService.generateOtp(getOtpReq);

		} catch (Exception ex) {
			logger.error("generateOtp  method  exception" + ex);
		}
		logger.debug("generateOtp method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("verifyOtp")
	public ResponseEntity<ResponseAsMessage> verifyOtp(@RequestBody getOtpRequest getOtpReq) {
		logger.info("verifyOtp method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iForgotPasswordService.verifyOtp(getOtpReq);

		} catch (Exception ex) {
			logger.error("verifyOtp method  exception" + ex);
		}
		logger.debug("verifyOtp method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("resetPassword")
	public ResponseEntity<ResponseAsMessage> resetPassword(@RequestBody ResetPasswordRequest ResetPasswordReq) {
		logger.info("resetPassword method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iForgotPasswordService.resetPassword(ResetPasswordReq);

		} catch (Exception ex) {
			logger.error("resetPassword method  exception" + ex);
		}
		logger.debug("resetPassword method end");
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
}
