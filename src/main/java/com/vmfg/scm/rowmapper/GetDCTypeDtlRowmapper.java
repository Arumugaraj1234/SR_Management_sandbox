package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.GetDCTypeDtlEntity;

public class GetDCTypeDtlRowmapper implements RowMapper<GetDCTypeDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetDCTypeDtlRowmapper.class);

	@Override
	public GetDCTypeDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetDCTypeDtlEntity igs = new GetDCTypeDtlEntity();
		try {
			igs.setDcCode(rs.getString("DC_TYPE_CODE"));
			igs.setDcDesc(rs.getString("DC_TYPE_DESC"));
			igs.setIsActive(rs.getString("IS_ACTIVE"));
		} catch (Exception ex) {
			logger.error("GetDCTypeDtlRowmapper error " + ex);
		}
		return igs;
	}


}
