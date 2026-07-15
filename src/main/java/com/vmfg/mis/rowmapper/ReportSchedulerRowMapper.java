package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.ReportSchedulerEntity;

public class ReportSchedulerRowMapper implements RowMapper<ReportSchedulerEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ReportSchedulerRowMapper.class);

	@Override
	public ReportSchedulerEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReportSchedulerEntity lst = new ReportSchedulerEntity();
		try {
			lst.setEmpId(rs.getString("ASSIGNED_TO"));
			lst.setYear(rs.getString("REPORT_YEAR"));
			lst.setMonth(rs.getString("REPORT_MONTH"));
			//lst.setDayStart(rs.getString("WEEK_START"));
			lst.setDeptCode(rs.getString("DEPARTMENT_CODE"));
			lst.setDelay(rs.getString("DELAY_TASK"));
			lst.setNoCompleted(rs.getString("NO_COMPLETED_TASK"));
			lst.setNoPlanned(rs.getString("NO_PLANNED_TASK"));
			lst.setPerCentage(rs.getString("COMPLETION_PERCENTAGE"));
			lst.setProjId(rs.getString("PM_HDR_ID"));
			lst.setTenantID(rs.getString("TENANT_ID"));
			 if (columnExists(rs, "WEEK_START")) {
				 lst.setDayStart((rs.getString("WEEK_START")));
				}
		}catch(Exception ex) {
			logger.error("ReportSchedulerRowMapper  Method Exception" + ex);
		}
		return lst;
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
