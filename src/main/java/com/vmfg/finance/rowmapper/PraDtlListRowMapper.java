package com.vmfg.finance.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.finance.entity.PraDtlListEntity;

public class PraDtlListRowMapper implements RowMapper<PraDtlListEntity> {
	private static final Logger logger = LoggerFactory.getLogger(PraDtlListRowMapper.class);
	@Override
	public PraDtlListEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		PraDtlListEntity row = new PraDtlListEntity();
		try {
			row.setGrnDtlId(rs.getString("GRN_DTL_ID"));
			row.setPartDesc(rs.getString("PRODUCT_DESCRIPTION"));
			row.setPraDtlId(rs.getString("PRA_DTL_ID"));
			row.setPraHdrId(rs.getString("PRA_ID"));
		} catch (Exception e) {
			logger.error("Exception in PraDtlListRowMapper" + e);

		}

		return row;
	}

}
