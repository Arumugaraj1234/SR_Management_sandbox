package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.NoOfPoDrillEntity;

public class NoOfPoDrillEntityRowMapper implements RowMapper<NoOfPoDrillEntity> {
	private static final Logger logger = LoggerFactory.getLogger(NoOfPoDrillEntityRowMapper.class);

	@Override
	public NoOfPoDrillEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		NoOfPoDrillEntity lst = new NoOfPoDrillEntity();
		try {
			lst.setBasicTotal(rs.getString("UNITE_RATE"));
			lst.setDescription(rs.getString("DESCRIPTION"));
			lst.setExpectedDeliveryDate(rs.getString("EXPECTED_DELIVERY_DATE"));
			lst.setIndentCode(rs.getString("INDENT_CODE"));
			lst.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			lst.setIndentId(rs.getString("INDENT_ID"));
			lst.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
			lst.setMake(rs.getString("MAKE"));
			lst.setMaterial(rs.getString("MATERIAL"));
			lst.setProductCode(rs.getString("PRODUCT_CODE"));
			lst.setQty(rs.getString("QTY"));
			lst.setRemarks(rs.getString("REMARKS"));
			lst.setSpecification(rs.getString("SPECIFICATION"));
			lst.setStation(rs.getString("PK_DESC"));
			lst.setSubAssy(rs.getString("PSK_DESC"));
			lst.setTotalValue(rs.getString("TOTAL_VALUE"));
			lst.setUnit(rs.getString("UNIT"));
			lst.setWeight(rs.getString("WEIGHT"));
			lst.setPoNumber(rs.getString("PO_CODE"));
			lst.setVendor(rs.getString("VENDOR_NAME"));
			lst.setPoDate(rs.getString("DATE"));
			lst.setProjectId(rs.getString("PROJECT_ID"));
			lst.setProjectCode(rs.getString("PROJECT_CODE"));
			lst.setVenCategory(rs.getString("VENDOR_CATEGORY"));
			lst.setPjsPoCreatorName(rs.getString("PJS_PO_CREATOR_NAME"));
		}catch(Exception ex) {
			logger.error("NoOfPoDrillEntityRowMapper Method Exception" + ex);
		}
		return lst;
	}

	

}
