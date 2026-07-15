package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class NextWoAndEquipRowMapper implements RowMapper<NextWOAndEquipmentInfo>{

	@Override
	public NextWOAndEquipmentInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		NextWOAndEquipmentInfo nw = new NextWOAndEquipmentInfo();
		try {
			nw.setLastPlannedDate(row.getString("prevGenDate"));
			nw.setNextEquipmentId(row.getString("next_equipment"));
			nw.setNextWo(row.getString("next_wo"));
		}catch(Exception ex) {
			
		}
		return nw;
	}

}
