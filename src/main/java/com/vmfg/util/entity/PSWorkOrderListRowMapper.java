package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class PSWorkOrderListRowMapper implements RowMapper<PSWorkOrderList>{
	private static final Logger logger = LoggerFactory.getLogger(PSWorkOrderListRowMapper.class);
	@Override
	public PSWorkOrderList mapRow(ResultSet row, int rowNum) throws SQLException {
		PSWorkOrderList ps = new PSWorkOrderList();
		try {
			ps.setProductQuantity(row.getString("PRODUCT_QUANTITY"));
			ps.setRouteID(row.getString("ROUTE_ID"));
			ps.setSequence(row.getString("SEQUENCE"));
			ps.setShift(row.getString("SHIFT"));
			ps.setWoCreatedDate(row.getString("WORKORDER_CREATION_DATE"));
			ps.setWoPlannedEndDate(row.getString("WORKORDER_PLANNED_FINISH_DATE"));
			ps.setWoPlannedStartDate(row.getString("WORKORDER_PLANNED_START_DATE"));
			ps.setWorkOrderId(row.getString("WORKORDER_ID"));
			ps.setWoStatus(row.getString("WORKORDER_STATUS"));
			ps.setProductCode(row.getString("PRODUCT_CODE"));
		}catch(Exception ex) {
			logger.error("PSWorkOrderListRowMapper row mapper exception :"+ex);
		}
		return ps;
	}

}
