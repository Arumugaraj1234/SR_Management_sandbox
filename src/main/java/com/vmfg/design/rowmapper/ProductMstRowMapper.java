package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ProductMstEntity;

public class ProductMstRowMapper implements RowMapper<ProductMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProductMstRowMapper.class);

	@Override
	public ProductMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductMstEntity res = new ProductMstEntity();
		try {
			res.setProductCode(rs.getString("PRODUCT_CODE"));
			res.setProductDesc(rs.getString("UPDATED_DESCRIPTION"));
			res.setProductId(rs.getString("PRODUCT_ID"));
			res.setPmHdrId(rs.getString("PM_HDR_ID"));
			res.setUomCode(rs.getString("PRODUCT_UOM_CODE"));
			res.setPkaId(rs.getString("PKA_ID"));
			res.setPskaId(rs.getString("PSKA_ID"));
			res.setUomLongDescriprtion(rs.getString("UOM_LONG_DESCRIPTION"));
			res.setUomShortDescriprtion(rs.getString("UOM_SHORT_DESCRIPTION"));
			res.setMake(rs.getString("UPDATED_MAKE"));
			res.setQty(rs.getString("UPDATED_QTY"));
			res.setUnit(rs.getString("UNIT"));
			res.setWeight(rs.getString("UPDATED_WEIGHT"));
			res.setMaterial(rs.getString("UPDATED_MATERIAL"));
			res.setLastupdatedUserId(rs.getString("LAST_UPDATED_USER_ID"));
			res.setLastupdatedDateTime(rs.getString("LAST_UPDATED_DATETIME"));
			res.setPkDesc(rs.getString("PK_DESC"));
			res.setPskDesc(rs.getString("PSK_DESC"));
			res.setSerialNumber(rs.getString("SERIAL_NUMBER"));
			res.setSpecification(rs.getString("UPDATED_SPECIFICATION"));
			res.setIndentCode(rs.getString("INDENT_CODE"));
			res.setSbcDesc(rs.getString("SBC_DESC"));
			res.setProjectCode(rs.getString("PROJECT_CODE"));
			res.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			res.setRemarks(rs.getString("REMARKS"));
//			res.setDmId(rs.getInt("DM_ID"));
			res.setPoCode(rs.getString("PO_CODE"));
			res.setVendorName(rs.getString("VENDOR_NAME"));
			res.setUnitRate(rs.getString("UNITE_RATE"));
			res.setTotalValue(rs.getString("TOTAL_VALUE"));
			res.setFileNameExtn(rs.getString("FILE_NAME_EXTN"));
			res.setIsPdf(rs.getString("IS_PDF"));
		} catch (Exception ex) {
			logger.error("ProductMstRowMapper  Method Exception" + ex);
		}
		return res;
	}
}
