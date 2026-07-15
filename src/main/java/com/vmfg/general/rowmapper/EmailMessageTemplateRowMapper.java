
package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.EmailMessageTemplateEntity;

public class EmailMessageTemplateRowMapper implements RowMapper<EmailMessageTemplateEntity> {
	private static final Logger logger = LoggerFactory.getLogger(EmailMessageTemplateRowMapper.class);

	@Override
	public EmailMessageTemplateEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		EmailMessageTemplateEntity thr = new EmailMessageTemplateEntity();
		try {

			thr.setMecCode(rs.getString("MEC_CODE"));
			thr.setMetCode(rs.getString("MET_CODE"));
			thr.setMsgBoadyFilePath(rs.getString("MSG_BODY_FILE_PATH"));
			thr.setMsgCc(rs.getString("MSG_CC"));
			thr.setMsgFrom(rs.getString("MSG_FROM"));
			thr.setMsgFromPassword(rs.getString("MSG_FROM_PASSWORD"));
			thr.setMsgFromUserName(rs.getString("MSG_FROM_USERNAME"));
			thr.setMsgHost(rs.getString("MSG_FROM_HOST"));
			thr.setMsgPort(rs.getString("MSG_FROM_PORT"));
			thr.setMsgSub(rs.getString("MSG_SUB"));
			thr.setMsgTempId(rs.getString("MSG_TEMP_ID"));
			thr.setMsgTo(rs.getString("MSG_TO"));
			thr.setTenantID(rs.getString("TENANT_ID"));
		} catch (Exception ex) {
			logger.error("EmailMessageTemplateRowMapper exception" + ex);
		}
		return thr;

	}

}
