package com.vmfg.general.services.impl;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.email.Email;
import com.vmfg.export.dao.impl.ProjectReportTrackerDAO;
import com.vmfg.general.dao.interfaces.IForgotPasswordDAO;
import com.vmfg.general.request.ResetPasswordRequest;
import com.vmfg.general.request.getOtpRequest;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.interfaces.IForgotPasswordService;
import com.vmfg.security.config.WebSecurityConfig;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.entity.MessageLogEntity;
import com.vmfg.util.entity.MessageTemplateEntity;

@Service
public class ForgotPasswordService implements IForgotPasswordService {
	private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);

	@Autowired
	private IForgotPasswordDAO iForgotPasswordDAO;
	
	@Autowired
	ProjectReportTrackerDAO projectReportTrackerDAO;

		
	@Override
	public ResponseAsMessage generateOtp(getOtpRequest getOtpReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		MessageTemplateEntity msgTemplate = new MessageTemplateEntity();
		List<MessageLogEntity> messageLogList=new ArrayList<MessageLogEntity>();
		logger.info("generateOtp service start");
		Boolean mailSentStatus=false;
		try {
			int checkUserName=iForgotPasswordDAO.checkUserName(getOtpReq.getUserName(), getOtpReq.getTenantId());
			if(checkUserName==1) {
				String userEmail=iForgotPasswordDAO.getEmailIdByUserName(getOtpReq.getUserName(),getOtpReq.getTenantId());
				if(userEmail.equalsIgnoreCase("NA")) {
					
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					returnMessage.setResponseDataMessage(ResponseMessageMap.contactAdmin);
				
				}else {
					 String otp = generateOTP(6); 
					 
					 String validTime=projectReportTrackerDAO.getPropValueByTenant(getOtpReq.getTenantId(), "OTP_VALID_TIME");
					 
					 LocalDateTime currentDateTime = LocalDateTime.now();
					 LocalDateTime expiryDateTime = currentDateTime.plusMinutes(Integer.parseInt(validTime));
					 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");
					 String formattedExpDateTime = expiryDateTime.format(formatter);
					 
					 msgTemplate=iForgotPasswordDAO.getMsgTemplateDtls("1",getOtpReq.getTenantId());
					 
					 String contents = new String(Files.readAllBytes(Paths.get(msgTemplate.getMsgBodyFilePath())));
					 String otpFilePath=projectReportTrackerDAO.getPropValueByTenant(getOtpReq.getTenantId(), "OTP_MAIL_TEMPLATE");
					 
					 File UPLOADED_FOLDER = new File(otpFilePath);
						if (!UPLOADED_FOLDER.exists()) {
							UPLOADED_FOLDER.mkdirs();
						}
						
						String path = UPLOADED_FOLDER + File.separator + "OTP_MAIL"
								+ CommonMethod.getCurrentdateformat() + CommonMethod.getCurrentTimeformat() + ".html";
						File newmodfile = new File(path);
						FileWriter writer = new FileWriter(newmodfile);
						String userName = getOtpReq.getUserName().substring(0, 1).toUpperCase() + getOtpReq.getUserName().substring(1);
						
						contents = contents.replace("replacement1", userName);
						contents = contents.replace("replacement2", String.valueOf(otp));
						contents = contents.replace("replacement3", String.valueOf(formattedExpDateTime));
						writer.write(contents);
						writer.close();
					 
					    int msgLog=iForgotPasswordDAO.insertInMessageLog("1", msgTemplate.getMsgSub(), path, userEmail, getOtpReq.getTenantId());
					    messageLogList=iForgotPasswordDAO.getMsgLogDtl("1",getOtpReq.getTenantId());
					  
					    if(msgLog==1) {
					    	for(MessageLogEntity messageLog : messageLogList ) {
					    		Email emailObj = new Email();
					    		mailSentStatus=emailObj.sendMail(messageLog, msgTemplate);
					    		if(mailSentStatus==true) {
					    			iForgotPasswordDAO.updateSentStatus(messageLog.getMsgLogId());
					    			
					    			// update OTP and ExpiryTime in user_loin
									iForgotPasswordDAO.updateOtp(otp, getOtpReq.getUserName(),String.valueOf(expiryDateTime));
									returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
									returnMessage.setResponseDataMessage(ResponseMessageMap.successMsg);
					    		}else {
					    			logger.error("email send status is failed");
					    			returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
									returnMessage.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
					    		}
					    	}
					    }else{
					    	logger.error("Error in message log insert");
					    }
				}
			}else {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnMessage.setResponseDataMessage(ResponseMessageMap.contactAdmin);
			}
		}catch (Exception e) {
			logger.error("generateOtp service Exception" + e);
		}
		logger.info("generateOtp  service end");
		return returnMessage;
	}
	
		public static String generateOTP(int length) {  
			String numbers = "0123456789";
	        Random random = new Random();
	        StringBuilder otp = new StringBuilder(length);

	        for (int i = 0; i < length; i++) {
	            otp.append(numbers.charAt(random.nextInt(numbers.length())));
	        }

	        // Ensure the OTP is exactly 6 digits 
	        while (otp.length() < length) {
	            otp.insert(0, '0');
	        }

	        return otp.toString();
	    }  

	@Override
	public ResponseAsMessage verifyOtp(getOtpRequest getOtpReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		logger.info("verifyOtp service start");
		try {
			getOtpReq.setTenantId("bgrn");
			String otpExpiryTime=iForgotPasswordDAO.getOtpExpiryTime(getOtpReq.getTenantId(),getOtpReq.getUserName());
			if(!otpExpiryTime.equalsIgnoreCase("NA")) {
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		        LocalDateTime expiryTime = LocalDateTime.parse(otpExpiryTime, formatter);
		        LocalDateTime currentTime = LocalDateTime.now();
		       
				if(currentTime.isBefore(expiryTime)) {
					int validateOtp=iForgotPasswordDAO.verifyOtp(getOtpReq.getUserName(),getOtpReq.getTenantId(),getOtpReq.getOtp());
						if(validateOtp==1) {
							returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
							returnMessage.setResponseDataMessage(ResponseMessageMap.successMsg);
						}else {
							returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
							returnMessage.setResponseDataMessage(ResponseMessageMap.wrongOtp);
						}
					}else {
						returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						returnMessage.setResponseDataMessage(ResponseMessageMap.reqNewOtp);	
					}
				}
		}catch (Exception e) {
			logger.error("verifyOtp service Exception" + e);
		}
		logger.info("verifyOtp  service end");
		return returnMessage;
	}

	@Override
	public ResponseAsMessage resetPassword(ResetPasswordRequest ResetPasswordReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		WebSecurityConfig ws = new WebSecurityConfig();
		logger.info("resetPassword service start");
		try {
			String encPassword = ws.passwordEncoder().encode(ResetPasswordReq.getPassword());
			int resetPwd=iForgotPasswordDAO.resetPassword(ResetPasswordReq.getUserName(),encPassword);
			
			if(resetPwd==1) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage(ResponseMessageMap.successUpdated);
			}else{
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);	
			}
		}catch (Exception e) {
			logger.error("resetPassword service Exception" + e);
		}
		logger.info("resetPassword  service end");
		return returnMessage;
	}
	
}
