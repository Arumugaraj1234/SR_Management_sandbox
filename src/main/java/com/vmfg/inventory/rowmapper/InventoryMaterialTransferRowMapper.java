package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.InventoryMaterialTransferEntity;

public class InventoryMaterialTransferRowMapper implements RowMapper<InventoryMaterialTransferEntity>{
	private static final Logger logger = LoggerFactory.getLogger(InventoryMaterialTransferRowMapper.class);

	@Override
	public InventoryMaterialTransferEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		InventoryMaterialTransferEntity transferEntity = new InventoryMaterialTransferEntity();
		try {
			
	        transferEntity.setMtrId(rs.getString("MTR_ID"));
	        transferEntity.setSNo(rs.getString("SERIAL_NUMBER"));
	        transferEntity.setProductId(rs.getString("PRODUCT_ID"));
	        transferEntity.setFromPmHdrId(rs.getString("FROM_PM_HDR_ID"));
	        transferEntity.setToPmHdrId(rs.getString("TO_PM_HDR_ID"));
	        transferEntity.setTransferQuantity(rs.getString("TRANSFER_QUANTITY"));
	        transferEntity.setTransferDate(rs.getString("TRANSFER_DATE"));
	        transferEntity.setCreatedOn(rs.getString("CREATED_ON"));
	        transferEntity.setCreatedBy(rs.getString("CREATED_BY"));
	        transferEntity.setReason(rs.getString("REASON"));
	        transferEntity.setFrmcustomerName(rs.getString("FROM_CUSTOMER_NAME"));
	        transferEntity.setFrmprojectCode(rs.getString("FROM_PROJECT_CODE"));
	        transferEntity.setFrmprojectName(rs.getString("FROM_PROJECT_NAME"));
	        transferEntity.setTocustomerName(rs.getString("TO_CUSTOMER_NAME"));
	        transferEntity.setToprojectCode(rs.getString("TO_PROJECT_CODE"));
	        transferEntity.setToprojectName(rs.getString("TO_PROJECT_NAME"));
	        transferEntity.setFrmLocation(rs.getString("FROM_LOCATION_CODE"));
	        transferEntity.setToLocation(rs.getString("TO_LOCATION_CODE"));
	        transferEntity.setProductCode(rs.getString("PRODUCT_CODE"));
	        transferEntity.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
	        transferEntity.setUom(rs.getString("UOM_LONG_DESCRIPTION"));
	        transferEntity.setFromLocationDesc(rs.getString("FROM_LOCATION_DESC"));
	        transferEntity.setToLocationDesc(rs.getString("TO_LOCATION_DESC"));
	        transferEntity.setReferenceId(rs.getString("MATERIAL_TRANSFER_CODE"));
	        transferEntity.setOverAllCost(rs.getString("OVER_ALL_COST"));
	        transferEntity.setUnitCost(rs.getString("UINT_COST"));
	        transferEntity.setFromBin(rs.getString("FROM_BIN"));
	        transferEntity.setToBin(rs.getString("TO_BIN"));
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("InventoryMaterialTransferRowMapper  Method Exception" + e);
		}
		 return transferEntity;
	}


}
