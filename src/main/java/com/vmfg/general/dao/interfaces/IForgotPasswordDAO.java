package com.vmfg.general.dao.interfaces;

import java.util.List;

import com.vmfg.util.entity.MessageLogEntity;
import com.vmfg.util.entity.MessageTemplateEntity;

public interface IForgotPasswordDAO {

	String getEmailIdByUserName(String userName, String tenantId);

	int insertInMessageLog(String msgTempId, String msgSub, String sentFilePath, String msgTo, String tenantId);

	MessageTemplateEntity getMsgTemplateDtls(String msgTemplateId, String tenantId);

	int updateOtp(String otp, String userName, String expiryTime);

	int verifyOtp(String userName, String tenantId, String otp);

	String getOtpExpiryTime(String tenantId, String userName);

	int checkUserName(String userName, String tenantId);

	int resetPassword(String userName, String encPassword);

	List<MessageLogEntity> getMsgLogDtl(String msgTempId, String tenantId);

	int updateSentStatus(String msgLogId);

}
