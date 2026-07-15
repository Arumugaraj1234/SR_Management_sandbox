package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.NotificationReqEntity;

public class NotificationReqRowMapper implements RowMapper<NotificationReqEntity> {
	private static final Logger logger = LoggerFactory.getLogger(NotificationReqRowMapper.class);

	@Override
	public NotificationReqEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		NotificationReqEntity result=new NotificationReqEntity();
		try {
			result.setDescription(rs.getString("NOTIFICATION_MESSAGE"));
			result.setEmpId(rs.getString("EMPLOYEE_ID"));
			result.setEvent(rs.getString("NOTIFICATION_TYPE"));
			result.setIsNotified(rs.getString("NOTIFIED"));
			result.setLabel(rs.getString("NOTIFY_DATETIME"));
			result.setNotifiyId(rs.getString("NOT_REQ_ID"));
			result.setTenantId(rs.getString("TENANT_ID"));
			result.setViewedDatetime(rs.getString("NOTIFICATION_VIEWED_DATETIME"));
			
		}catch(Exception ex) {
			logger.error("NotificationReqRowMapper "+ex);
		}
		return result;
	}

}
