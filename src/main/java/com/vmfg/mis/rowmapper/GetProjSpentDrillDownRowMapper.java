package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.ProjSpentDrillDownEntity;

public class GetProjSpentDrillDownRowMapper implements RowMapper<ProjSpentDrillDownEntity> {

	private static final Logger logger = LoggerFactory.getLogger(GetProjSpentDrillDownRowMapper.class);

	@Override
	public ProjSpentDrillDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjSpentDrillDownEntity lst = new ProjSpentDrillDownEntity();
		try {
			lst.setPmHdrId(rs.getString("PM_HDR_ID"));
			lst.setProjCode(rs.getString("PROJECT_CODE"));
			lst.setProjName(rs.getString("PROJECT_NAME"));
			lst.setActualVal(rs.getString("SCM_BUDGET_ALLOCATED"));
			lst.setBudgetConsumed(rs.getString("BUDGET_VALUE"));
			lst.setMaterialBudCons(rs.getString("MATERIAL_VALUE"));
			lst.setMaterialRelesVal(rs.getString("MATERIAL_PO_RELEASED"));
			lst.setServiceBudCons(rs.getString("SERVICE_VALUE"));
			lst.setServiceRelesVal(rs.getString("SERVICE_PO_RELEASED"));
		}catch(Exception ex) {
			logger.error("GetProjSpentDrillDownRowMapper  Method Exception" + ex);
		}
		return lst;
	}

}
