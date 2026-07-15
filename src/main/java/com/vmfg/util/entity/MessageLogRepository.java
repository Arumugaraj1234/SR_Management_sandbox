package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class MessageLogRepository implements RowMapper<MessageLogEntity>{
	
	private static final Logger logger = LoggerFactory.getLogger(MessageLogRepository.class);

	@Override
	public MessageLogEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		MessageLogEntity m = new MessageLogEntity();
		try{
			m.setLastUpdatedDatetime(row.getString("LAST_UPDATED_DATETIME"));			
//			m.setMsgLogDate(row.getString("MSG_LOG_DATE"));
			m.setMsgLogDatetime(row.getString("MSG_LOG_DATETIME"));
			m.setMsgLogId(row.getString("MSG_LOG_ID"));
			m.setMsgSentStatus(row.getString("MSG_SENT_STATUS"));
			m.setMsgTempId(row.getString("MSG_TEMP_ID"));
			m.setMsgTo(row.getString("MSG_TO"));
			m.setSendMsgFilePath(row.getString("SENT_MSG_FILE_PATH"));
			m.setMsgSubject(row.getString("MSG_SUBJECT"));
			m.setTenantId(row.getString("TENANT_ID"));
			m.setMsgCc(row.getString("MSG_CC"));
			m.setAttachmentPath(row.getString("ATTACHMENT_PATH"));
			m.setIsAttachmentAvailable(row.getString("IS_ATTACHMENT_AVAILABLE"));
			
		}catch(Exception ex) {
			logger.error("MessageLogRepository"+ex);
		}
		return m;
	}

}
