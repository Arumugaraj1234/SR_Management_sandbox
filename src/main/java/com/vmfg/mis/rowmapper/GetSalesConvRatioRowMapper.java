package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.GetSalesConvRatioEntity;

public class GetSalesConvRatioRowMapper implements RowMapper<GetSalesConvRatioEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetSalesConvRatioRowMapper.class);

	@Override
	public GetSalesConvRatioEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetSalesConvRatioEntity ph = new GetSalesConvRatioEntity();
		try {
			ph.setCustomerName(row.getString("CUSTOMER_NAME"));
			ph.setCompletedDate(row.getString("COMPLETED_DATE"));
			ph.setCompletedDateTime(row.getString("COMPLETED_DATETIME"));
			ph.setCreatedDate(row.getString("CREATED_DATE"));
			ph.setCreatedDateTime(row.getString("CREATED_DATETIME"));
			ph.setProjectName(row.getString("PROJECT_NAME"));
			ph.setTenantId(row.getString("TENANT_ID"));
			ph.setDateDiff(row.getString("DATE_DIFF"));
			ph.setHandoverDate(row.getString("PROJECT_HANDOVER_DATE"));
			ph.setEnqCode(row.getString("ENQUIRY_CODE"));
		} catch (Exception e) {
			logger.error("GetSalesConvRatioRowMapper Exception--->" + e);
		}
		return ph;
	}

}
