package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.util.entity.MessageTemplateEntity;

public class MessageTemplateRepository implements RowMapper<MessageTemplateEntity>{
	private static final Logger logger = LoggerFactory.getLogger(MessageTemplateRepository.class);
	@Override
	public MessageTemplateEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		MessageTemplateEntity m =  new MessageTemplateEntity();
		try {
						
			m.setIsActive(row.getString("IS_ACTIVE"));
			m.setLastUpdatedDatetime(row.getString("LAST_UPDATED_DATETIME"));
			m.setLastUpdatedUserId(row.getString("LAST_UPDATED_USER_ID"));
			m.setMecCode(row.getString("MEC_CODE"));
			m.setMetCode(row.getString("MET_CODE"));			
			m.setMsgBodyFilePath(row.getString("MSG_BODY_FILE_PATH"));
			m.setMsgEngineType(row.getString("MSG_ENGINE_TYPE"));
			m.setMsgFrom(row.getString("MSG_FROM"));
			m.setMsgFromHost(row.getString("MSG_FROM_HOST"));
			m.setMsgFromPassword(row.getString("MSG_FROM_PASSWORD"));
			m.setMsgFromPort(row.getString("MSG_FROM_PORT"));
			m.setMsgFromUsername(row.getString("MSG_FROM_USERNAME"));
			m.setMsgPriority(row.getString("MSG_PRIORITY"));
			m.setMsgSub(row.getString("MSG_SUB"));
			m.setMsgTempId(row.getString("MSG_TEMP_ID"));
			m.setMsgTo(row.getString("MSG_TO"));
			m.setTenantId(row.getString("TENANT_ID"));
			
			
			m.setMsgCc(row.getString("MSG_CC"));			
			m.setMsgBcc(row.getString("MSG_BCC"));
		}catch(Exception ex) {
			logger.error("MessageTemplateRepository"+ex);
		}
		return m;
	}

}
