package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGrpScpDtlEntity;

public class IndentGrpScpDtlRowMapper implements RowMapper<IndentGrpScpDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGrpScpDtlRowMapper.class);

	@Override
	public IndentGrpScpDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentGrpScpDtlEntity igs = new IndentGrpScpDtlEntity();
		try {
			igs.setIgDtlId(rs.getString("IG_DTL_ID"));
			igs.setIgScpDItld(rs.getString("IG_SCSD_ID"));
			igs.setIgScpId(rs.getString("IG_SCS_ID"));
			igs.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			igs.setL1ExtendedPrice(rs.getString("L1_EXTENDED_PRICE"));
			igs.setL1ExtendedPriceFx(rs.getString("L1_FX_EXTENDED_PRICE"));
			igs.setL1UnitPrice(rs.getString("L1_UNIT_PRICE"));
			igs.setL1UnitPriceFx(rs.getString("L1_UNIT_PRICE_Fx"));
			igs.setL1CurrencyType(rs.getString("L1_CURRENCY_TYPE"));
			igs.setL1ExchangeRate(rs.getString("L1_EXCHANGE_RATE"));
			igs.setL2ExtendedPrice(rs.getString("L2_EXTENDED_PRICE"));
			igs.setL2ExtendedPriceFx(rs.getString("L2_FX_EXTENDED_PRICE"));
			igs.setL2UnitPrice(rs.getString("L2_UNIT_PRICE"));
			igs.setL2UnitPriceFx(rs.getString("L2_UNIT_PRICE_FX"));
			igs.setL2CurrencyType(rs.getString("L2_CURRENCY_TYPE"));
			igs.setL2ExchangeRate(rs.getString("L2_EXCHANGE_RATE"));
			igs.setL3ExtendedPrice(rs.getString("L3_EXTENDED_PRICE"));
			igs.setL3ExtendedPriceFx(rs.getString("L3_FX_EXTENDED_PRICE"));
			igs.setL3UnitPrice(rs.getString("L3_UNIT_PRICE"));
			igs.setL3UnitPriceFx(rs.getString("L3_UNIT_PRICE_FX"));
			igs.setL3CurrencyType(rs.getString("L3_CURRENCY_TYPE"));
			igs.setL3ExchangeRate(rs.getString("L3_EXCHANGE_RATE"));
			igs.setFinalL1UnitPrice(rs.getString("FINAL_L1_UNIT_PRICE"));
			igs.setFinalL1UnitPriceFx(rs.getString("FINAL_L1_FX_UNIT_PRICE"));
	        igs.setFinalL1ExtendedPrice(rs.getString("FINAL_L1_EXTENDED_PRICE"));
			igs.setFinalL1ExtendedPriceFx(rs.getString("FINAL_L1_FX_EXTENDED_PRICE"));
	        igs.setFinalL2UnitPrice(rs.getString("FINAL_L2_UNIT_PRICE"));
			igs.setFinalL2UnitPriceFx(rs.getString("FINAL_L2_FX_UNIT_PRICE"));
			igs.setFinalL2ExtendedPrice(rs.getString("FINAL_L2_EXTENDED_PRICE"));
			igs.setFinalL2ExtendedPriceFx(rs.getString("FINAL_L2_FX_EXTENDED_PRICE"));
	        igs.setFinalL3UnitPrice(rs.getString("FINAL_L3_UNIT_PRICE"));
			igs.setFinalL3UnitPriceFx(rs.getString("FINAL_L3_FX_UNIT_PRICE"));
			igs.setFinalL3ExtendedPrice(rs.getString("FINAL_L3_EXTENDED_PRICE"));
			igs.setFinalL3ExtendedPriceFx(rs.getString("FINAL_L3_FX_EXTENDED_PRICE"));
			igs.setTenantId(rs.getString("TENANT_ID"));
			igs.setProdCode(rs.getString("PRODUCT_CODE"));
			igs.setProdDesc(rs.getString("DESCRIPTION"));
			igs.setQty(rs.getString("QTY"));
			igs.setUom(rs.getString("UOM"));
			igs.setSno(rs.getInt("S_NO"));
			igs.setProdSpec(rs.getString("SPECIFICATION"));
			igs.setWeight(rs.getString("WEIGHT"));
			igs.setMaterial(rs.getString("MATERIAL"));
//			igs.setFileNameExtn(rs.getString("FILE_NAME_EXTN"));
//			igs.setIsPdf(rs.getString("IS_PDF"));
		} catch (Exception ex) {
			logger.error("IndentGrpScpDtlRowMapper error " + ex);
		}
		return igs;
	}
}