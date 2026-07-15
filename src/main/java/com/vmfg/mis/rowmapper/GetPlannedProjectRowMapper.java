package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.GetPlannedProjectEntity;

public class GetPlannedProjectRowMapper implements RowMapper<GetPlannedProjectEntity>{
	private static final Logger logger = LoggerFactory.getLogger(GetPlannedProjectRowMapper.class);

	@Override
	public GetPlannedProjectEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetPlannedProjectEntity res = new GetPlannedProjectEntity();
		try {
			
			if (columnExists(rs, "CompleteCount")) {
				res.setCompletedDrawing(rs.getString("CompleteCount"));
			}
			
			if (columnExists(rs, "DAP_PLANNED_COMPLETED_DATE")) {
				res.setDapPlannedDate(rs.getString("DAP_PLANNED_COMPLETED_DATE"));
			}
			if (columnExists(rs, "MANUAL_PLANNED_COMPLETED_DATE")) {
				res.setManualPlannedDate(rs.getString("MANUAL_PLANNED_COMPLETED_DATE"));
			}
			if (columnExists(rs, "PROJECT_CODE")) {
				res.setProjectCode(rs.getString("PROJECT_CODE"));
			}
			if (columnExists(rs, "PM_HDR_ID")) {
				res.setProjectId(rs.getString("PM_HDR_ID"));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
				res.setProjectName(rs.getString("PROJECT_NAME"));
			}
			if (columnExists(rs, "TotalCount")) {
				res.setTotalDrawing(rs.getString("TotalCount"));
			}
			if (columnExists(rs, "TENANT_ID")) {
				res.setTenantId(rs.getString("TENANT_ID"));
			}
			if (columnExists(rs, "CUSTOMER_NAME")) {
				res.setCustomerName(rs.getString("CUSTOMER_NAME"));
			}
			if (columnExists(rs, "DAP_ACTUAL_DATE")) {
				res.setDapActualDate(rs.getString("CUSTOMER_NAME"));
			}
			if (columnExists(rs, "MANUAL_ACTUAL_DATE")) {
				res.setManualActualDate(rs.getString("CUSTOMER_NAME"));;
			}
			
		}catch (Exception e) {
			logger.error("GetPlannedProjectRowMapper  Method Exception" + e);
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
