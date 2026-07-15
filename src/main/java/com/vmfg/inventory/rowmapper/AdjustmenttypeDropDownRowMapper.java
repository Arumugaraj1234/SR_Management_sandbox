package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.AdjustmentTypeDropDownEntity;

public class AdjustmenttypeDropDownRowMapper implements RowMapper<AdjustmentTypeDropDownEntity>{
	private static final Logger logger = LoggerFactory.getLogger(AdjustmenttypeDropDownRowMapper.class);

	@Override
	public AdjustmentTypeDropDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		AdjustmentTypeDropDownEntity res = new AdjustmentTypeDropDownEntity();
		try {
			res.setAdjustmenttypeId(rs.getString("ADJUSTMENT_TYPE_ID"));
			res.setAdjustmenttypeDesc(rs.getString("ADJUSTMENT_TYPE_DESCRIPTION"));
		}catch (Exception e) {
			logger.error("AdjustmenttypeDropDownRowMapper  Method Exception" + e);
		}
		return res;
	}

}
