package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.MaterialInwardDtlEntity;

public class MaterialInwardDtlRowMapper implements RowMapper<MaterialInwardDtlEntity> {
    private static final Logger logger = LoggerFactory.getLogger(MaterialInwardDtlRowMapper.class);

    @Override
    public MaterialInwardDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        MaterialInwardDtlEntity entity = new MaterialInwardDtlEntity();
        try {
        	entity.setMiDtlId(rs.getString("MI_DTL_ID"));
            entity.setMiId(rs.getString("MI_ID"));
            entity.setPoDtlId(rs.getString("PO_DTL_ID"));
            entity.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
            entity.setOrderedQty(rs.getString("ORDERED_QTY"));
            entity.setReceivedQty(rs.getString("RECEIVED_QTY"));
            entity.setInspectedQty(rs.getString("INSPECTED_QTY"));
            entity.setUom(rs.getString("UOM"));
            entity.setTenantId(rs.getString("TENANT_ID"));
            entity.setProductId(rs.getString("PRODUCT_ID"));
            if (columnExists(rs, "UOM_LONG_DESCRIPTION")) {
            	entity.setUom(rs.getString("UOM_LONG_DESCRIPTION"));
			}
            if (columnExists(rs, "NOK_QTY")) {
            	entity.setNokQty(rs.getString("NOK_QTY"));
			}
            if (columnExists(rs, "REWORK_QTY")) {
            	entity.setReworkQty(rs.getString("REWORK_QTY"));
			}
        }catch(Exception ex) {
        	logger.error("MaterialInwardDtlRowMapper error "+ex);
        }
        
        return entity;
    }
    
 // column checking purpose (column is there or not)
 	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
 		ResultSetMetaData metaData = rs.getMetaData();
 		int columns = metaData.getColumnCount();

 		for (int i = 1; i <= columns; i++) {
 			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
 				return true;
 			}
 		}

 		return false;
 	}
}
