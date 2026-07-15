package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.ProjectWbsInitiationMst;

public class WBSInitiationMstRowMapper implements RowMapper<ProjectWbsInitiationMst> {
	private static final Logger logger = LoggerFactory.getLogger(WBSInitiationMstRowMapper.class);

	@Override
	public ProjectWbsInitiationMst mapRow(ResultSet rs, int rowNum) throws SQLException {

		ProjectWbsInitiationMst wbsMst = new ProjectWbsInitiationMst();
		try {

			wbsMst.setDeptCode(rs.getString("DEPARTMENT_CODE"));
			wbsMst.setPiId(rs.getString("PI_ID"));
			wbsMst.setPmId(rs.getString("PM_ID"));
			wbsMst.setPrimaryDoc(rs.getString("PRIMARY_POC"));
			wbsMst.setTenantId(rs.getString("TENANT_ID"));
			wbsMst.setMasterPoc(rs.getString("MASTER_POC"));
			wbsMst.setAssignedDept(rs.getString("DEPARTMENT_ASSIGNED"));
		} catch (Exception ex) {
			logger.error("ProjectWbsInitiationMst  Method Exception" + ex);

		}
		return wbsMst;
	}

}
