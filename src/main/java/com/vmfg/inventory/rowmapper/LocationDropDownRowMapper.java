package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.LocationDropDownEntity;

public class LocationDropDownRowMapper implements RowMapper<LocationDropDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(LocationDropDownRowMapper.class);

	@Override
	public LocationDropDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		LocationDropDownEntity ldt = new LocationDropDownEntity();
		try {
			ldt.setInventoryLocationCode(rs.getString("INVENTORY_LOCATION_CODE"));
			ldt.setInventoryLocationDescription(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("LocationDropDownRowMapper  Method Exception" + e);
		}
		return ldt;
	}

}
