package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.SalesBudgetSheetDtlEntity;

public class SalesBudgetSheetDtlRowMapper implements RowMapper<SalesBudgetSheetDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SalesBudgetSheetDtlRowMapper.class);

	@Override
	public SalesBudgetSheetDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		SalesBudgetSheetDtlEntity entity = new SalesBudgetSheetDtlEntity();
		
		try {
			entity.setSbDtlId(rs.getString("SB_DTL_ID"));
			entity.setSbcDesc(rs.getString("SBC_DESC"));
			entity.setValue(rs.getString("VALUE"));
			entity.setSbcId(rs.getString("SBC_ID"));
			

		} catch (Exception ex) {
			logger.error("SalesBudgetSheetRowMapper  Method Exception" + ex);

		}
		return entity;
	}

	

}
