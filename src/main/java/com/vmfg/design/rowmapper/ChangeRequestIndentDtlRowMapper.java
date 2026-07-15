package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ChangeRequestIndentDtlEntity;


public class ChangeRequestIndentDtlRowMapper implements RowMapper<ChangeRequestIndentDtlEntity> {

	private static final Logger logger = LoggerFactory.getLogger(ChangeRequestIndentDtlRowMapper.class);

	@Override
	public ChangeRequestIndentDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ChangeRequestIndentDtlEntity res = new ChangeRequestIndentDtlEntity();
		try {
			res.setDesc(rs.getString("DESCRIPTION"));
			res.setProdCode(rs.getString("PRODUCT_CODE"));
			res.setTenantId(rs.getString("TENANT_ID"));
			res.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
		} catch (Exception ex) {
			logger.error("ChangeRequestIndentDtlRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
