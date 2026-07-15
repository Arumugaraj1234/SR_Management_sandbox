package com.vmfg.general.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.dao.interfaces.IForgotPasswordDAO;
import com.vmfg.general.rowmapper.MessageTemplateRepository;
import com.vmfg.general.services.impl.ForgotPasswordService;
import com.vmfg.util.entity.MessageLogEntity;
import com.vmfg.util.entity.MessageLogRepository;
import com.vmfg.util.entity.MessageTemplateEntity;

@Transactional
@Repository
public class ForgotPasswordDAO implements IForgotPasswordDAO{
	private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String getEmailIdByUserName(String userName, String tenantId) {
		String mailId = "";
		try {
			String qry = "SELECT \r\n" + 
					"    COALESCE(NULLIF(emp.EMAIL_ID, ''), 'NA') AS EMAIL_ID\r\n" + 
					"FROM\r\n" + 
					"    user_login ul inner join employee_mst emp on ul.EMPLOYEE_ID=emp.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    userName = ? and ul.TENANT_ID= ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,userName,tenantId);
			mailId = resultMap.get("EMAIL_ID").toString();
		} catch (Exception ex) {
			logger.error("getEmailIdByUserName Error" + ex);
		}
		return mailId;
	}

	@Override
	public int insertInMessageLog(String msgTempId, String msgSub, String sentFilePath,String msgTo, String tenantId) {
		int insertStatus = 0;
		try {

			String qry = "INSERT INTO message_log (MSG_TEMP_ID, MSG_SUBJECT, SENT_MSG_FILE_PATH, MSG_TO, TENANT_ID) VALUES (?, ?, ?, ? , ?);";
			insertStatus = this.jdbcTemplate.update(qry, msgTempId, msgSub, sentFilePath, msgTo,tenantId);

		} catch (Exception ex) {
			logger.error("insertInMessageLog Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public int checkUserName(String userName, String tenantId) {
		int count = 0;
		try {
			String qry = "select count(*) as VALUE from user_login where userName= ? and TENANT_ID= ? AND IS_ACTIVE=1;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,userName,tenantId);
			count = Integer.parseInt(resultMap.get("VALUE").toString());

		} catch (Exception ex) {
			logger.error("checkUserName Error" + ex);
		}
		return count;
	}
	@Override
	public MessageTemplateEntity getMsgTemplateDtls(String msgTemplateId, String tenantId) {
		List<MessageTemplateEntity> list=new ArrayList<MessageTemplateEntity>();
		try {
			String qry = "SELECT * FROM message_template WHERE MSG_TEMP_ID = ? AND TENANT_ID = ?";
			list = this.jdbcTemplate.query(qry, new Object[]{2, "bgrn"}, new MessageTemplateRepository());

		}catch (Exception ex) {
			logger.error("getMsgTemplateDtls Error" + ex);
		}
		return list.get(0);
	}
	
	@Override
	public int updateOtp(String otp,String userName,String expiryTime) {
		int updateStatus = 0;
		try {

			String qry = "UPDATE user_login SET OTP=?, EXPIRY_DATETIME=? WHERE userName=?";
			updateStatus = this.jdbcTemplate.update(qry, otp, expiryTime, userName);

		} catch (Exception ex) {
			logger.error("updateOtp Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int verifyOtp(String userName, String tenantId, String otp) {
		int updateStatus = 0;
		try {

			String qry = "select count(*) as VALUE from user_login where userName= ? and OTP= ? and TENANT_ID= ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,userName,otp,tenantId);
			updateStatus = Integer.parseInt(resultMap.get("VALUE").toString());

		} catch (Exception ex) {
			logger.error("verifyOtp Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public String getOtpExpiryTime(String tenantId, String userName) {
		String time = "";
		try {
			String qry = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN EXPIRY_DATETIME IS NOT NULL THEN EXPIRY_DATETIME\r\n" + 
					"        ELSE 'NA'\r\n" + 
					"    END as EXPIRY_DATE \r\n" + 
					"FROM\r\n" + 
					"    user_login\r\n" + 
					"WHERE\r\n" + 
					"    userName = ? \r\n" + 
					"        AND TENANT_ID = ? ;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,userName,tenantId);
			time = resultMap.get("EXPIRY_DATE").toString();
		} catch (Exception ex) {
			logger.error("getOtpExpiryTime DAO Error" + ex);
		}
		return time;
	}

	@Override
	public int resetPassword(String userName, String encPassword) {
		int updateStatus = 0;
		try {

			String qry = "UPDATE user_login SET userPassword=? WHERE userName=?";
			updateStatus = this.jdbcTemplate.update(qry, encPassword,userName);

		} catch (Exception ex) {
			logger.error("updateOtp Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public int updateSentStatus(String msgLogId) {
		int updateStatus = 0;
		try {

			String qry = "update message_log set MSG_SENT_STATUS  = '1' where MSG_LOG_ID = ?";
			updateStatus = this.jdbcTemplate.update(qry, msgLogId);
		} catch (Exception ex) {
			logger.error("updateSentStatus Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public List<MessageLogEntity> getMsgLogDtl(String msgTempId, String tenantId) {
		List<MessageLogEntity> list=new ArrayList<MessageLogEntity>();
		try {
			
			String qry="select * from message_log where MSG_TEMP_ID='"+msgTempId+"' and MSG_SENT_STATUS='0'; ";
			list=this.jdbcTemplate.query(qry,new MessageLogRepository());
			
		}catch (Exception ex) {
			logger.error("getMsgLogDtl Error" + ex);
		}
		return list;
	}
}
