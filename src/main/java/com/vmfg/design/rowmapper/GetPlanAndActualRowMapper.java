package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.GetPlanAndActualEntity;

public class GetPlanAndActualRowMapper implements RowMapper<GetPlanAndActualEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetPlanAndActualRowMapper.class);

	@Override
	public GetPlanAndActualEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetPlanAndActualEntity res = new GetPlanAndActualEntity();
		try {
		
		//	res.setPkaId(rs.getString("PKA_ID"));
			res.setActualCount(rs.getString("ACTUAL_COUNT"));
			res.setPlanCount(rs.getString("PLAN_COUNT"));
		} catch (Exception ex) {
			logger.error("GetPlanAndActualRowMapper  Method Exception" + ex);
		}
		return res;
	}


}
