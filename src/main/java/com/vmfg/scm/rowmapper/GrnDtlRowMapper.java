package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.GrnDtlEntity;

public class GrnDtlRowMapper implements RowMapper<GrnDtlEntity>{
	private static final Logger logger = LoggerFactory.getLogger(GrnDtlRowMapper.class);
	@Override
	public GrnDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GrnDtlEntity grnDtlEntity=new GrnDtlEntity();

		try {
			if (columnExists(rs, "GRN_DTL_ID")) {
				grnDtlEntity.setGrnDtlId(rs.getString("GRN_DTL_ID"));
			}
			grnDtlEntity.setMaterialInwardReceivedQty(rs.getString("MATERIAL_INWARD_RECEIVED_QTY"));
			grnDtlEntity.setGrnReceivedQty(rs.getString("GRN_RECEIVED_QTY"));

			if (columnExists(rs, "MI_ID")) {
				grnDtlEntity.setMiId(rs.getString("MI_ID"));
			}

			if (columnExists(rs, "PO_DTL_ID")) {
				grnDtlEntity.setPoDtlId(rs.getString("PO_DTL_ID"));
			}
			if (columnExists(rs, "MI_DTL_ID")) {
				grnDtlEntity.setMiDtlId(rs.getString("MI_DTL_ID"));
			}
			if (columnExists(rs, "INDENT_DTL_ID")) {
				grnDtlEntity.setIndentDetailId(rs.getString("INDENT_DTL_ID"));
			}
			if (columnExists(rs, "ORDERED_QTY")) {
				grnDtlEntity.setOrderedQty(rs.getString("ORDERED_QTY"));
			}
			if (columnExists(rs, "INSPECTED_QTY")) {
				grnDtlEntity.setInspectedQty(rs.getString("INSPECTED_QTY"));
			}
			if (columnExists(rs, "UOM")) {
				grnDtlEntity.setUom(rs.getString("UOM"));
			}
			if (columnExists(rs, "INDENT_ID")) {
				grnDtlEntity.setIndentId(rs.getString("INDENT_ID"));
			}
			if (columnExists(rs, "PRODUCT_ID")) {
				grnDtlEntity.setProductId(rs.getString("PRODUCT_ID"));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				grnDtlEntity.setProductCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "PRODUCT_DESCRIPTION")) {
				grnDtlEntity.setDescription(rs.getString("PRODUCT_DESCRIPTION"));
			}
			if (columnExists(rs, "SPECIFICATION")) {
				grnDtlEntity.setSpecification(rs.getString("SPECIFICATION"));
			}
			if (columnExists(rs, "MAKE")) {
				grnDtlEntity.setMake(rs.getString("MAKE"));
			}
			if (columnExists(rs, "QTY")) {
				grnDtlEntity.setQty(rs.getString("QTY"));
			}
			if (columnExists(rs, "PRODUCT_UOM_CODE")) {
				grnDtlEntity.setUnit(rs.getString("PRODUCT_UOM_CODE"));
			}
			if (columnExists(rs, "MATERIAL")) {
				grnDtlEntity.setMaterial(rs.getString("MATERIAL"));
			}
			if (columnExists(rs, "REMARKS")) {
				grnDtlEntity.setRemarks(rs.getString("REMARKS"));
			}
			if (columnExists(rs, "PO_CODE")) {
				grnDtlEntity.setPoCode(rs.getString("PO_CODE"));
			}
			if (columnExists(rs, "DC_CODE")) {
				grnDtlEntity.setDcCode(rs.getString("DC_CODE"));
			}
			if (columnExists(rs, "DC_NO")) {
				grnDtlEntity.setDcCode(rs.getString("DC_NO"));
			}
			if (columnExists(rs, "DC_DATE")) {
				grnDtlEntity.setDcDate(rs.getString("DC_DATE"));
			}
			if (columnExists(rs, "MAT_REMARKS")) {
				grnDtlEntity.setMtlRemarks(rs.getString("MAT_REMARKS"));
			}
			if (columnExists(rs, "UOM_LONG_DESCRIPTION")) {
				grnDtlEntity.setUomDesc(rs.getString("UOM_LONG_DESCRIPTION"));
			}
		}catch(Exception ex) {
			logger.error("GrnDtlRowMapper  Method Exception" + ex);

		}
		return grnDtlEntity;
	}



	//column checking purpose (column is there or not)
	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
				return true;
			}
		}

		return false;
	}


}
