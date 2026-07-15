package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.LocationMstEntity;

public class LocationMstRowMapper implements RowMapper<LocationMstEntity> {
    private static final Logger logger = LoggerFactory.getLogger(LocationMstRowMapper.class);

    @Override
    public LocationMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocationMstEntity location = new LocationMstEntity();
        try {
            location.setLocationId(rs.getString("LOCATION_ID"));
            location.setLocAddressLine(rs.getString("LOCATION_ADDRESSLINE"));
            location.setLocCity(rs.getString("LOCATION_CITY"));
            location.setLocState(rs.getString("LOCATION_STATE"));
            location.setLocCountryCode(rs.getString("LOCATION_COUNTRY_CODE"));
            location.setLocPinCode(rs.getString("LOCATION_PINCODE"));
            location.setTenantId(rs.getString("TENANT_ID"));
            location.setLocationRefName(rs.getString("LOCATION_REFERENCENAME"));
        } catch (Exception ex) {
            logger.error("LocationMstRowMapper error: " + ex);
        }
        return location;
    }
}