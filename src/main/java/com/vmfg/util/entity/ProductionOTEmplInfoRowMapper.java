package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ProductionOTEmplInfoRowMapper implements RowMapper<ProductionOTEmplInfo> {
	private static final Logger logger = LoggerFactory.getLogger(ProductionOTEmplInfoRowMapper.class);

	@Override
	public ProductionOTEmplInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		ProductionOTEmplInfo qa = new ProductionOTEmplInfo();
		try {
			qa.setProductionManagerApprovedName(row.getString("PRODUCTION_MANAGER_APPROVED_NAME"));
			qa.setHrManagerApprovedName(row.getString("HR_MANAGER_APPROVED_NAME"));
			qa.setQualityManagerApprovedName(row.getString("QUALITY_MANAGER_APPROVED_NAME"));
			qa.setPlantManagerApprovedName(row.getString("PLANT_MANAGER_APPROVED_NAME"));

			qa.setProductionManagerApprovedBY(row.getString("PRODUCTION_MANAGER_APPROVED_BY"));
			qa.setHrManagerApprovedBY(row.getString("HR_MANAGER_APPROVED_BY"));
			qa.setQualityManagerApprovedBY(row.getString("QUALITY_MANAGER_APPROVED_BY"));
			qa.setPlantManagerApprovedBy(row.getString("PLANT_MANAGER_APPROVED_BY"));

		} catch (Exception ex) {
			logger.error("ProductionOTEmplInfoRowMapper map row exception -->" + ex);
		}
		return qa;
	}

}
