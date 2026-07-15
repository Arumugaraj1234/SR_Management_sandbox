package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ChangeRequestDtlEntity;

public class ChangeRequestDtlRowMapper implements RowMapper<ChangeRequestDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ChangeRequestDtlRowMapper.class);

	@Override
	public ChangeRequestDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ChangeRequestDtlEntity res = new ChangeRequestDtlEntity();
		try {
			res.setCrDtlId(rs.getString("CR_DTL_ID"));
			res.setCrhdrId(rs.getString("CR_ID"));
			res.setDesignerComments(rs.getString("DESIGNER_COMMENTS"));
			res.setTenantId(rs.getString("TENANT_ID"));
			res.setEmpId(rs.getString("EMPLOYEE_ID"));
			res.setReportedDateTime(rs.getString("REPORTED_DATETIME"));
			res.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
		} catch (Exception ex) {
			logger.error("ChangeRequestDtlRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
