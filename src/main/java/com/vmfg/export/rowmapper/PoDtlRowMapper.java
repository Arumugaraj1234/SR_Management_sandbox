package com.vmfg.export.rowmapper;

import java.sql.ResultSet;
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
            po.setTotalValue(row.getString("TOTAL_VALUE") != null ? row.getString("TOTAL_VALUE") : "");
            po.setTotalValueFx(row.getString("TOTAL_VALUE_FX") != null ? row.getString("TOTAL_VALUE_FX") : "");
            po.setUnitRate(row.getString("UNITE_RATE") != null ? row.getString("UNITE_RATE") : "");
            po.setUnitRateFx(row.getString("UNITE_RATE_FX") != null ? row.getString("UNITE_RATE_FX") : "");
            po.setUomCode(row.getString("UOM_CODE") != null ? row.getString("UOM_CODE") : "");
            po.setIndentDtlId(row.getString("INDENT_DTL_ID") != null ? row.getString("INDENT_DTL_ID") : "");
            po.setInspectedQty(row.getString("INSPECTED_QTY")!= null ? row.getString("INSPECTED_QTY") : "");
            po.setReceivedQty(row.getString("RECEIVED_QTY")!= null ? row.getString("RECEIVED_QTY") : "");
            po.setNOkCount(row.getString("NOK_QTY")!= null ? row.getString("NOK_QTY") : "");
            po.setReWorkCount(row.getString("REWORK_QTY")!= null ? row.getString("REWORK_QTY") : "");
        } catch (Exception e) {
            logger.error("PoDtlRowMapper Exception--->" + e);
        }
        return po;
    }
}