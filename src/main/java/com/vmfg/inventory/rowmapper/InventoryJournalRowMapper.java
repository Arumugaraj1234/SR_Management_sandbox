package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;


import com.vmfg.inventory.entity.InventoryJournalEntity;

public class InventoryJournalRowMapper implements RowMapper<InventoryJournalEntity> {
		private static final Logger logger = LoggerFactory.getLogger(InventoryJournalRowMapper.class);

		@Override
		public InventoryJournalEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			InventoryJournalEntity lst = new InventoryJournalEntity();
			try {
				
				lst.setSerialNumber(rs.getInt("SERIAL_NUMBER"));
				lst.setInventoryTransactionDate(rs.getString("INVENTORY_TRANSACTION_DATE"));
				lst.setInventoryTransactionReferenceId(rs.getString("INVENTORY_TRANSACTION_REFERENCE_ID"));
				lst.setInventoryTransactionQuantity(rs.getString("INVENTORY_TRANSACTION_QUANTITY"));
				lst.setInventoryTransactionTypeDescription(rs.getString("INVENTORY_TRANSACTION_TYPE_DESCRIPTION"));
				lst.setProductCode(rs.getString("PRODUCT_CODE"));
				lst.setProductDescription(rs.getString("PRODUCT_DESCRIPTION"));
				lst.setProjectId(rs.getString("PROJECT_ID"));
				lst.setInventoryLocDesc(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
				lst.setOpeningBalance(rs.getString("OPENING_BALANCE"));
				lst.setClosingBalance(rs.getString("CLOSING_BALANCE"));
				lst.setProjectCode(rs.getString("PROJECT_CODE"));
				lst.setUomLongDescription(rs.getString("UOM_LONG_DESCRIPTION"));
				lst.setUomShortDescription(rs.getString("UOM_SHORT_DESCRIPTION"));
				lst.setProjectName(rs.getString("PROJECT_NAME"));
				lst.setSpecification(rs.getString("SPECIFICATION"));
				lst.setPoCode(rs.getString("PO_CODE"));
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("InventoryJournalRowMapper  Method Exception" + e);
			}
			
			return lst;
		}

}
