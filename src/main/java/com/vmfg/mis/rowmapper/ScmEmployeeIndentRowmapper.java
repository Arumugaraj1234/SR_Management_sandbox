package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.ScmEmployeeIndentDtlsEntity;

public class ScmEmployeeIndentRowmapper  implements RowMapper<ScmEmployeeIndentDtlsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ScmEmployeeIndentRowmapper.class);

	@Override
	public ScmEmployeeIndentDtlsEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		ScmEmployeeIndentDtlsEntity ph = new ScmEmployeeIndentDtlsEntity();
		try {
			ph.setEmployee(row.getString("EMPLOYEE"));
			ph.setProjectCode(row.getString("PROJECT_CODE"));
			ph.setEmployeeId(row.getString("EMPLOYEE_ID"));
			ph.setPmHdrId(row.getString("PM_HDR_ID"));
		} catch (Exception e) {
			logger.error("ScmEmployeeIndentRowmapper Exception--->" + e);
		}
		return ph;
	}


}
