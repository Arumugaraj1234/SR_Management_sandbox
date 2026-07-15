package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.getDesignWidgetDtlByCategoryEntity;

public class GetDesignWidgetDtlByCategoryRowMapper implements RowMapper<getDesignWidgetDtlByCategoryEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetDesignWidgetDtlByCategoryRowMapper.class);

	@Override
	public getDesignWidgetDtlByCategoryEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		getDesignWidgetDtlByCategoryEntity ph = new getDesignWidgetDtlByCategoryEntity();
		try {
			ph.setActivityName(row.getString("ACTIVITY_NAME"));
			ph.setAssignedToId(row.getString("ASSIGNED_TO"));
			ph.setAssignedToName(row.getString("EMPLOYEE_FIRSTNAME"));
			ph.setPlannedCompletedDate(row.getString("PLANNED_COMPLETED_DATE"));
			ph.setPlannedStartDate(row.getString("PLANNED_START_DATE"));
			ph.setCompletedDate(row.getString("COMPLETED_DATE"));
			ph.setProjName(row.getString("PROJECT_NAME"));
			ph.setProjNum(row.getString("PROJECT_CODE"));
		} catch (Exception e) {
			logger.error("GetCustomerOrderDtlRowMapper Exception--->" + e);
		}
		return ph;
	}


}
