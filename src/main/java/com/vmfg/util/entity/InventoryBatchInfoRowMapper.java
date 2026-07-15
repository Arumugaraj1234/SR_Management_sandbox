package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class InventoryBatchInfoRowMapper implements RowMapper<InventoryBatchStockInfo>{
	private static final Logger logger = LoggerFactory.getLogger(InventoryBatchInfoRowMapper.class);
	@Override
	public InventoryBatchStockInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		InventoryBatchStockInfo is = new InventoryBatchStockInfo();
		try{
			is.setBatchNumber(row.getString("BATCH_NUMBER"));
			is.setInventoryProductDtlId(row.getString("INVENTORY_PRODUCT_LOT_DTL_ID"));
			is.setLotQuantity(row.getBigDecimal("LOT_QUANTITY"));
		}catch(Exception ex) {
			logger.error("InventoryBatchInfoRowMapper map row exception -->"+ex);
		}
		return is;
	}

}