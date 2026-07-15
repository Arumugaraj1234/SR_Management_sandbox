package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.QualityInsReqEntity;

public class QualityInsRequestRowMapper implements RowMapper<QualityInsReqEntity> {
	private static final Logger logger = LoggerFactory.getLogger(QualityInsRequestRowMapper.class);

	@Override
	public QualityInsReqEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		QualityInsReqEntity qi = new QualityInsReqEntity();
		try {
				qi.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
				qi.setMiDtlId(rs.getString("MI_DTL_ID"));
				qi.setMiId(rs.getString("MI_ID"));
				qi.setPoDtlId(rs.getString("PO_DTL_ID"));
				qi.setProductCode(rs.getString("PRODUCT_CODE"));
				qi.setInventoryLocCode(rs.getString("INVENTORY_LOCATION_CODE"));
				qi.setUom(rs.getString("UOM"));
				qi.setQty(rs.getString("QTY"));
				qi.setProdutId(rs.getString("PRODUCT_ID"));
				
		} catch (Exception e) {
			logger.error("QualityInsRequestRowMapper Exception--->" + e);
		}
		return qi;
	}
}