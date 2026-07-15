package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.GetTaskCompPerEntity;

public class GetTaskCompPerRowMapper implements RowMapper<GetTaskCompPerEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetTaskCompPerRowMapper.class);
	
	@Override
	public GetTaskCompPerEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetTaskCompPerEntity ph = new GetTaskCompPerEntity();
		try {
			ph.setDelayTask(row.getString("DELAY_TASK"));
			ph.setDepartmentCode(row.getString("DEPARTMENT_CODE"));
			ph.setEmployeeId(row.getString("EMPLOYEE_ID"));
			ph.setEmployeeName(row.getString("EMPLOYEE_FIRSTNAME"));
			ph.setNoCompltedTask(row.getString("NO_COMPLETED_TASK"));
			ph.setNoPlannedTask(row.getString("NO_PLANNED_TASK"));
			ph.setPercentageCompleted(row.getString("PERCENTAGE_COMPLETED"));
			ph.setPmHdrId(row.getString("PM_HDR_ID"));
			ph.setReportmonth(row.getString("REPORT_MONTH"));
			ph.setReportyear(row.getString("REPORT_YEAR"));
			ph.setRtId(row.getString("RT_ID"));
			ph.setTenantId(row.getString("TENANT_ID"));
			if (columnExists(row, "WEEK_START")) {
				ph.setWeekStart(row.getString("WEEK_START"));
			}
			if (columnExists(row, "REPORT_DATE")) {
			ph.setReportDate(row.getString("REPORT_DATE"));
			}
		} catch (Exception e) {
			logger.error("GetTaskCompPerRowMapper Exception--->" + e);
		}
		return ph;
	}
	
	private boolean columnExists(ResultSet row, String columnName) throws SQLException {
		ResultSetMetaData metaData = row.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}

}
