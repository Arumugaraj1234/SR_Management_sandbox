package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.SupplierRatingEntity;

public class SupplierRatingRowMapper implements RowMapper<SupplierRatingEntity>{
	private static final Logger logger = LoggerFactory.getLogger(SupplierRatingRowMapper.class);

	@Override
	public SupplierRatingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		SupplierRatingEntity lst = new SupplierRatingEntity();
		try {
			lst.setCa(rs.getString("CA"));
//			lst.setOkQty(rs.getString("OK_QTY"));
			lst.setRejQty(rs.getString("REJECT_QTY"));
			lst.setQtyRate(rs.getString("QUALITY_RATING"));
			lst.setVenName(rs.getString("VENDOR_NAME"));
			lst.setReWorkQty(rs.getString("REWORK_QTY"));
			lst.setInspOk(rs.getString("INSPECTION_OK"));
			lst.setVenCode(rs.getString("VENDOR_CODE"));
			lst.setInwardRate(rs.getString("INWARD_RATING"));
			lst.setRelationshipRate(rs.getString("SUPPLIER_RATING"));
		}catch(Exception ex) {
			logger.error("SupplierRatingRowMapper  Method Exception" + ex);
		}
		return lst;
	}

	

	

}
