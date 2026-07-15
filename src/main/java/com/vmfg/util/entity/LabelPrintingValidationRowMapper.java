package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;


public class LabelPrintingValidationRowMapper implements RowMapper<LabelPrintingValidationEntity>{
	private static final Logger logger = LoggerFactory.getLogger(LabelPrintingValidationRowMapper.class);
	@Override
	public LabelPrintingValidationEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		LabelPrintingValidationEntity hdr = new LabelPrintingValidationEntity();
		try {
			hdr.setSerialNumber(row.getString("SERIAL_NUMBER"));
			hdr.setProductCode(row.getString("PRODUCT_CODE"));
			hdr.setWorkOrder(row.getString("WORKORDER_ID"));
			hdr.setCreatedDatetime(row.getString("CREATED_ON"));
			hdr.setTenantId(row.getString("TENANT_ID"));
		} catch (Exception e) {
			logger.error("LabelPrintingValidationRowMapper map row exception -->"+e);
		}
		return hdr;
	}
}