package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.GetComponentDtls;

public class GetComponentDtlsRowMapper implements RowMapper<GetComponentDtls> {
	private static final Logger logger = LoggerFactory.getLogger(DocumentStatusMstRowMapper.class);

	@Override
	public GetComponentDtls mapRow(ResultSet row, int rowNum) throws SQLException {
		GetComponentDtls tm = new GetComponentDtls();
		try {
			tm.setComponentName(row.getString("COMPONENT"));
			tm.setStgDesc(row.getString("STG_DESC"));
		} catch (Exception e) {
			logger.error("DocumentStatusMstRowMapper Exception--->" + e);
		}
		return tm;
	}
}
