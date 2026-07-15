package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PSEquipListRowMapper implements RowMapper<PSEquipProcessList>{

	@Override
	public PSEquipProcessList mapRow(ResultSet row, int rowNum) throws SQLException {
		PSEquipProcessList eq = new PSEquipProcessList();
		try {
			eq.setDuration(row.getString("DURATION"));
			eq.setEquipmentID(row.getString("EQUIPMENT_ID"));
			eq.setLineCode(row.getString("LINE_CODE"));
			eq.setOperationID(row.getString("OPERATION_ID"));
			eq.setProductCode(row.getString("PRODUCT_CODE"));
			eq.setProgramCode(row.getString("PROGRAM_CODE"));
			eq.setRouteID(row.getString("ROUTE_ID"));
			eq.setSequence(row.getString("SEQUENCE"));
			eq.setOffsetValue(row.getString("OFFSET"));
		}catch(Exception ex) {
			
		}
		return eq;
	}

}
