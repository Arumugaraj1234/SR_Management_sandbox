package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.PoPaymentTermEntity;

public class PoPaymentTermRowMapper implements RowMapper<PoPaymentTermEntity> {
    private static final Logger logger = LoggerFactory.getLogger(PoPaymentTermRowMapper.class);

    @Override
    public PoPaymentTermEntity mapRow(ResultSet row, int rowNum) throws SQLException {
        PoPaymentTermEntity po = new PoPaymentTermEntity();
        try {
            po.setPotId(row.getString("POT_ID") != null ? row.getString("POT_ID") : "");
            po.setPoId(row.getString("PO_ID") != null ? row.getString("PO_ID") : "");
            po.setTerm(row.getString("TERM") != null ? row.getString("TERM") : "");
            po.setPercentage(row.getString("PERCENTAGE") != null ? row.getString("PERCENTAGE") : "");
            po.setPaymentAmount(row.getString("PAYMENT_AMOUNT") != null ? row.getString("PAYMENT_AMOUNT") : "");
            po.setPendingAmount(row.getString("PENDING_AMOUNT") != null ? row.getString("PENDING_AMOUNT") : "");
            po.setRemarks(row.getString("REMARKS") != null ? row.getString("REMARKS") : "");
            po.setIsLast(row.getString("IS_LAST")!= null ? row.getString("IS_LAST") : "");
        } catch (Exception e) {
            logger.error("PoPaymentTermRowMapper Exception--->" + e);
        }
        return po;
    }
}