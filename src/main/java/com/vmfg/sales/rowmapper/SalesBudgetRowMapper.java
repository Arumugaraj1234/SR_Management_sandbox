package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.BudgetKeyCategory;

public class SalesBudgetRowMapper implements RowMapper<BudgetKeyCategory> {
	private static final Logger logger = LoggerFactory.getLogger(DocumentAppStatusDtlRowMapper.class);

	@Override
	public BudgetKeyCategory mapRow(ResultSet row, int rowNum) throws SQLException {
		BudgetKeyCategory tm = new BudgetKeyCategory();
		try {
			tm.setKeyCategory(row.getString("SBC_DESC"));
			tm.setKeyCatCode(row.getString("SBC_CODE"));

		} catch (Exception e) {
			logger.error("BudgetKeyCategory Exception--->" + e);
		}
		return tm;
	}

}
