package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.DcDtlEntity;

public class DcDtlRowMapper implements RowMapper<DcDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(BillingDetailRowMapper.class);

	@Override
	public DcDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DcDtlEntity bd = new DcDtlEntity();
		try {
			bd.setReceivedQty(rs.getString("RECEIVED_QTY"));
			bd.setDcDtlId(rs.getString("DC_DTL_ID"));
			bd.setDcId(rs.getString("DC_ID"));
			bd.setDescOfGoods(rs.getString("DESCRIPTION_OF_GOODS"));
			bd.setHsnNo(rs.getString("HSN_NO"));
			bd.setQty(rs.getString("QTY"));
			bd.setRate(rs.getString("RATE"));
			bd.setTotal(rs.getString("TOTAL"));
			bd.setUom(rs.getString("UOM"));
			bd.setUomDesc(rs.getString("UOM_LONG_DESCRIPTION"));
			bd.setProductId(rs.getString("PM_PRODUCT_ID"));
			bd.setProductCode(rs.getString("PRODUCT_CODE"));
			bd.setHdrId(rs.getString("PRODUCT_ID"));
			bd.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
		} catch (Exception ex) {
			logger.error("DcDtlRowMapper error " + ex);
		}
		return bd;
	}

}
