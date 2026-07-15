package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.ProductMasterEntity;

public class ProductMasterEntityRowMapper implements RowMapper<ProductMasterEntity>{
	private static final Logger logger = LoggerFactory.getLogger(ProductMasterEntityRowMapper.class);

	@Override
	public ProductMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductMasterEntity res = new ProductMasterEntity();
		try {
			res.setBin(rs.getString("BIN"));
			res.setCreatedDateTime(rs.getString("CREATED_DATETIME"));
			res.setCreatedUserId(rs.getString("CREATED_USER_ID"));
			res.setInwardDateTime(rs.getString("INWARD_DATETIME"));
			res.setIsActive(rs.getString("IS_ACTIVE"));
			res.setIsInventory(rs.getString("IS_INVENTORY"));
			res.setLastUpdatedDateTime(rs.getString("LAST_UPDATED_DATETIME"));
			res.setLastUpdatedUserId(rs.getString("LAST_UPDATED_USER_ID"));
			res.setMake(rs.getString("MAKE"));
			res.setMaterial(rs.getString("MATERIAL"));
			res.setMinimumOrderLevel(rs.getString("MINIMUM_ORDER_QUANTITY"));
			res.setPkaId(rs.getString("PKA_ID"));
			res.setProductCategory(rs.getString("PRODUCT_CATEGORY"));
			res.setProductCode(rs.getString("PRODUCT_CODE"));
			res.setProductCostPerUnit(rs.getString("PRODUCT_COST_PER_UNIT"));
			res.setProductDescription(rs.getString("PRODUCT_DESCRIPTION"));
			res.setProductGstTaxRate(rs.getString("PRODUCT_GST_TAX_RATE"));
			res.setProductHsnSacCode(rs.getString("PRODUCT_HSN_SAC_CODE"));
			res.setProductReorderLevel(rs.getString("PRODUCT_REORDER_LEVEL"));
			res.setProductUomCode(rs.getString("PRODUCT_UOM_CODE"));
			res.setPsksId(rs.getString("PSKA_ID"));
			res.setQty(rs.getString("QTY"));
			res.setSafetyStock(rs.getString("SAFETY_STOCK"));
			res.setSafetyStockDays(rs.getString("SAFETY_STOCK_DAYS"));
			res.setSpecification(rs.getString("SPECIFICATION"));
			res.setTenantId(rs.getString("TENANT_ID"));
			res.setUnit(rs.getString("UNIT"));
			res.setWeight(rs.getString("WEIGHT"));
		}catch (Exception e) {
			logger.error("ProductMasterEntityRowMapper  Method Exception" + e);
		}
		return res;
	}

}
