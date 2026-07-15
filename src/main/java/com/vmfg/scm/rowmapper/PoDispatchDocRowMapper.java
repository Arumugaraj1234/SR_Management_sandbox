package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.PoDispatchDocEntity;

public class PoDispatchDocRowMapper implements RowMapper<PoDispatchDocEntity> {
    private static final Logger logger = LoggerFactory.getLogger(PoDispatchDocRowMapper.class);

    @Override
    public PoDispatchDocEntity mapRow(ResultSet row, int rowNum) throws SQLException {
        PoDispatchDocEntity po = new PoDispatchDocEntity();
        try {
            po.setPodId(row.getString("POD_ID") != null ? row.getString("POD_ID") : "");
            po.setPoId(row.getString("PO_ID") != null ? row.getString("PO_ID") : "");
            po.setInvoiceNo(row.getString("INVOICE_NO") != null ? row.getString("INVOICE_NO") : "");
            po.setPkgList(row.getString("PKG_LIST") != null ? row.getString("PKG_LIST") : "");
            po.setAwbBl(row.getString("AWB_BL") != null ? row.getString("AWB_BL") : "");
            po.setTestReports(row.getString("TEST_REPORTS") != null ? row.getString("TEST_REPORTS") : "");
            po.setCertificateOfOrigin(row.getString("CERTIFICATE_OF_ORIGIN") != null ? row.getString("CERTIFICATE_OF_ORIGIN") : "");
            po.setoMManual(row.getString("O_M_MANUAL") != null ? row.getString("O_M_MANUAL") : "");
            po.setInsuranceWarrentyCert(row.getString("INSURANCE_WARRENTY_CERT") != null ? row.getString("INSURANCE_WARRENTY_CERT") : "");
            po.setInspectionReport(row.getString("INSPECTION_REPORT") != null ? row.getString("INSPECTION_REPORT") : "");
        } catch (Exception e) {
            logger.error("PoDispatchDocRowMapper Exception--->" + e);
        }
        return po;
    }
}