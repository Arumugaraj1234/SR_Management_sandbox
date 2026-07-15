package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class MessageTemplateRowMapper implements RowMapper<MessageTemplate> {
	private static final Logger logger = LoggerFactory.getLogger(MessageTemplateRowMapper.class);

	@Override
	public MessageTemplate mapRow(ResultSet row, int rowNum) throws SQLException {
		MessageTemplate mt = new MessageTemplate();
		try {
			mt.setMessageTempid(row.getString("MSG_TEMP_ID"));
			mt.setMessTo(row.getString("MSG_TO"));
			mt.setMsgBody(row.getString("MSG_BODY_FILE_PATH"));
			mt.setCc(row.getString("MSG_CC"));
		} catch (Exception ex) {
			logger.error("MessageTemplateRowMapper map row exception--->" + ex);
		}
		return mt;
	}

}
