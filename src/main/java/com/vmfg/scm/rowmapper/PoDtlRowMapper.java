package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.PoDtlEntity;

public class PoDtlRowMapper implements RowMapper<PoDtlEntity> {
    private static final Logger logger = LoggerFactory.getLogger(PoDtlRowMapper.class);

    @Override
    public PoDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
        PoDtlEntity po = new PoDtlEntity();
        try {
            po.setDeliveryDate(row.getString("DELIVERY_DATE") != null ? row.getString("DELIVERY_DATE") : "");
            po.setHsnCode(row.getString("HSN_CODE") != null ? row.getString("HSN_CODE") : "");
            po.setPoGst(row.getString("PO_GST") != null ? row.getString("PO_GST") : "");
            po.setPoDtlId(row.getString("PO_DTL_ID") != null ? row.getString("PO_DTL_ID") : "");
            po.setPoId(row.getString("PO_ID") != null ? row.getString("PO_ID") : "");
            po.setQty(row.getString("QTY") != null ? row.getString("QTY") : "");
            po.setCurrencyType(row.getString("CURRENCY_TYPE") != null ? row.getString("CURRENCY_TYPE") : "");
            po.setTotalValue(row.getString("TOTAL_VALUE") != null ? row.getString("TOTAL_VALUE") : "");
            po.setTotalValueFx(row.getString("TOTAL_VALUE_FX") != null ? row.getString("TOTAL_VALUE_FX") : "");
            po.setUnitRateFx(row.getString("UNIT_RATE_FX") != null ? row.getString("UNIT_RATE_FX") : "");
            po.setUnitRate(row.getString("UNITE_RATE") != null ? row.getString("UNITE_RATE") : "");
            po.setUomCode(row.getString("UOM_CODE") != null ? row.getString("UOM_CODE") : "");
            po.setIndentDtlId(row.getString("INDENT_DTL_ID") != null ? row.getString("INDENT_DTL_ID") : "");
            po.setInspectedQty(row.getString("INSPECTED_QTY")!= null ? row.getString("INSPECTED_QTY") : "");
            po.setReceivedQty(row.getString("RECEIVED_QTY")!= null ? row.getString("RECEIVED_QTY") : "");
            po.setNOkCount(row.getString("NOK_QTY")!= null ? row.getString("NOK_QTY") : "");
            po.setReWorkCount(row.getString("REWORK_QTY")!= null ? row.getString("REWORK_QTY") : "");
            po.setServiceNo(row.getString("SERVICE_NUMBER")!=null ? row.getString("SERVICE_NUMBER") : "");
            po.setMaterialDesc(row.getString("MATERIAL_DESCRIPTION")!=null ? row.getString("MATERIAL_DESCRIPTION") : "");
        } catch (Exception e) {
            logger.error("PoDtlRowMapper Exception--->" + e);
        }
        return po;
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