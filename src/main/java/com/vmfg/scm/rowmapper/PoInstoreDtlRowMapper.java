package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.rowmapper.productMstDropDownRowMapper;
import com.vmfg.scm.entity.PoInstoreDtlEntity;

public class PoInstoreDtlRowMapper implements RowMapper<PoInstoreDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(productMstDropDownRowMapper.class);

	@Override
	public PoInstoreDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		PoInstoreDtlEntity res = new PoInstoreDtlEntity();
		try {
			res.setProductCode(rs.getString("PRODUCT_CODE"));
			res.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			res.setProductId(rs.getString("PRODUCT_ID"));
			res.setPmHdrId(rs.getString("PM_HDR_ID"));
			res.setUomCode(rs.getString("PRODUCT_UOM_CODE"));
			res.setPkaId(rs.getString("PKA_ID"));
			res.setPskaId(rs.getString("PSKA_ID"));
			res.setUomLongDescriprtion(rs.getString("UOM_LONG_DESCRIPTION"));
			res.setUomShortDescriprtion(rs.getString("UOM_SHORT_DESCRIPTION"));
			res.setMake(rs.getString("MAKE"));
			res.setQty(rs.getString("QTY"));
			res.setUnit(rs.getString("UNIT"));
			res.setWeight(rs.getString("WEIGHT"));
			res.setMaterial(rs.getString("MATERIAL"));
			res.setLastupdatedUserId(rs.getString("LAST_UPDATED_USER_ID"));
			res.setLastupdatedDateTime(rs.getString("LAST_UPDATED_DATETIME"));
			res.setPkaDesc(rs.getString("PK_DESC"));
			res.setPskaDesc(rs.getString("PSK_DESC"));
			if(columnExists(rs, "PRODUCT_QUANTITY_ON_HAND")) {
			res.setProductQtyOnHand(rs.getString("PRODUCT_QUANTITY_ON_HAND"));
			}
		} catch (Exception ex) {
			logger.error("PoInstoreDtlRowMapper  Method Exception" + ex);
		}
		return res;
	}
	//column checking purpose (column is there or not)
		private boolean columnExists(ResultSet rs, String columnName) throws SQLException {

			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();

			for (int i = 1; i <= columns; i++) {
				if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
					return true;
				}
			}

			return false;
		}

}
