package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.GetPoDtlsByDate;

public class GetPoDtlsByDateRowMapper  implements RowMapper<GetPoDtlsByDate> {
    private static final Logger logger = LoggerFactory.getLogger(GetPoDtlsByDateRowMapper.class);

    @Override
    public GetPoDtlsByDate mapRow(ResultSet row, int rowNum) throws SQLException {
    	GetPoDtlsByDate po = new GetPoDtlsByDate();
        try {
          po.setPoCode(row.getString("PO_CODE"));
          po.setPoId(row.getString("PO_ID"));
          po.setVendorName(row.getString("VENDOR_NAME"));
          po.setVendorCode(row.getString("VENDOR_CODE"));
        } catch (Exception e) {
            logger.error("GetPoDtlsByDateRowMapper Exception--->" + e);
        }
        return po;
    }
}