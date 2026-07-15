package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.response.PMWorkLoadResponse;

public class PMWorkLoadRowMapper implements RowMapper<PMWorkLoadResponse> {
	private static final Logger logger = LoggerFactory.getLogger(PMWorkLoadRowMapper.class);

	@Override
	public PMWorkLoadResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		PMWorkLoadResponse lst = new PMWorkLoadResponse();
		try {

			lst.setEmpCode(rs.getString("EMPLOYEE_CODE"));
			lst.setEmpId(rs.getString("ASSIGNED_EMP_ID"));
			lst.setEmployeeName(rs.getString("EMPLOYEE_FIRSTNAME"));
			lst.setInvProject(rs.getString("projCount"));
		} catch (Exception ex) {
			logger.error("PMWorkLoadRowMapper Method Exception" + ex);
		}
		return lst;
	}
}
