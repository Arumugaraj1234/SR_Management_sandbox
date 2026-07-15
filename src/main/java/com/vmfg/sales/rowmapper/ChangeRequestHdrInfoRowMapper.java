package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.ChangeRequestHdrInfoEntity;

public class ChangeRequestHdrInfoRowMapper implements RowMapper<ChangeRequestHdrInfoEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ChangeRequestHdrInfoRowMapper.class);

	@Override
	public ChangeRequestHdrInfoEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		ChangeRequestHdrInfoEntity tm = new ChangeRequestHdrInfoEntity();
		try {
			tm.setBudgetCost(row.getString("BUDGET_COST"));
			tm.setCrCost(row.getString("CR_COST"));
			tm.setCrDateTime(row.getString("CR_DATETIME"));
			tm.setCrValue(row.getString("CR_VALUE"));
			tm.setFinalSaleValue(row.getString("FINAL_SALE_VALUE"));
			tm.setMasterId(row.getString("MASTER_ID"));
			tm.setRemarks(row.getString("REMARKS"));
			tm.setTotalBudgetCost(row.getString("TOTAL_BUDGET_COST"));
			tm.setSbHdrId(row.getString("SB_HDR_ID"));
			tm.setSbcId(row.getString("SBC_ID"));
		} catch (Exception e) {
			logger.error("ChangeRequestHdrInfoRowMapper Exception--->" + e);
		}
		return tm;
	}

}
