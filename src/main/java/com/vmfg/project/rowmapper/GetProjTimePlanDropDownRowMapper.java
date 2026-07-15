package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.GetProjTimePlanDropDownEntity;

public class GetProjTimePlanDropDownRowMapper  implements RowMapper<GetProjTimePlanDropDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetProjTimePlanDropDownRowMapper.class);

	@Override
	public GetProjTimePlanDropDownEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetProjTimePlanDropDownEntity ph = new GetProjTimePlanDropDownEntity();
		try {
			ph.setPmHdrDesc(row.getString("PM_DESC"));
			ph.setPmHdrId(row.getString("PM_HDR_ID"));
			ph.setTenantId(row.getString("TENANT_ID"));
		} catch (Exception e) {
			logger.error("GetProjTimePlanDropDownRowMapper Exception--->" + e);
		}
		return ph;
	}

}
