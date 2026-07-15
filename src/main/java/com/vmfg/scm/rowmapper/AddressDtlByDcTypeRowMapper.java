package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.AddressDtlByDcTypeEntity;

public class AddressDtlByDcTypeRowMapper implements RowMapper<AddressDtlByDcTypeEntity> {
	private static final Logger logger = LoggerFactory.getLogger(AddressDtlByDcTypeRowMapper.class);

	@Override
	public AddressDtlByDcTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		AddressDtlByDcTypeEntity bd = new AddressDtlByDcTypeEntity();
		try {
		if (columnExists(rs, "ADDRESS")) {	
			bd.setAddress(rs.getString("ADDRESS"));
		}if (columnExists(rs, "CITY")) {	
			bd.setCity(rs.getString("CITY"));
		}if (columnExists(rs, "GSTNO")) {	
			bd.setGstNo(rs.getString("GSTNO"));
		}if (columnExists(rs, "NAME")) {	
			bd.setName(rs.getString("NAME"));
		}if (columnExists(rs, "PINCODE")) {	
			bd.setPincode(rs.getString("PINCODE"));
		}if (columnExists(rs, "STATE")) {	
			bd.setState(rs.getString("STATE"));
		}if (columnExists(rs, "CODE")) {	
			bd.setCode(rs.getString("CODE"));
		}if (columnExists(rs, "CONTACT_NO")) {	
			bd.setContactNo(rs.getString("CONTACT_NO"));
		}if (columnExists(rs, "LOCATION_ID")) {	
			bd.setLocationId(rs.getString("LOCATION_ID"));
		}		
		} catch (Exception ex) {
			logger.error("AddressDtlByDcTypeRowMapper error " + ex);
		}
		return bd;
	
	

	}
	//column checking purpose (column is there or not)
	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}

}
