package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.TemplateDtlMstEntity;

public class TemplateTypeDtlRowMapper implements RowMapper<TemplateDtlMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TemplateTypeDtlRowMapper.class);

	@Override
	public TemplateDtlMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		TemplateDtlMstEntity res =  new TemplateDtlMstEntity();
		try {
			if (columnExists(rs, "ACTIVITY_NAME")) {
				res.setActName(rs.getString("ACTIVITY_NAME"));
			}
			if (columnExists(rs, "IS_ACTIVE")) {
				res.setIsActive(rs.getString("IS_ACTIVE"));
			}
			if (columnExists(rs, "LAST_UPDATED_BY")) {
				res.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
			}
			if (columnExists(rs, "LAST_UPDATED_DATETIME")) {
				res.setLastUpdatedOn(rs.getString("LAST_UPDATED_DATETIME"));
			}
			if (columnExists(rs, "TT_DTL_ID")) {
				res.setTtDtlId(rs.getString("TT_DTL_ID"));
			}
			if (columnExists(rs, "TT_DTL_ID")) {
				res.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}
			
		}catch(Exception e) {
			logger.error("TemplateTypeDtlRowMapper  Method Exception" + e);
		}
		return res;
	}

	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

	for (int i = 1; i <= columns; i++) {
		if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
			return true;
		}
	 }
	return false;
	}

}
