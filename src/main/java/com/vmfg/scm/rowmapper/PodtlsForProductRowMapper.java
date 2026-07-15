package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.request.PodtlsForProductEntity;

public class PodtlsForProductRowMapper implements RowMapper<PodtlsForProductEntity> {
	  private static final Logger logger = LoggerFactory.getLogger(PodtlsForProductRowMapper.class);
	  
	@Override
	public PodtlsForProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		PodtlsForProductEntity podtls = new PodtlsForProductEntity();
		try {
			podtls.setPoId(rs.getString("PO_ID"));
			podtls.setPoCode(rs.getString("PO_CODE"));
			podtls.setVendorName(rs.getString("VENDOR_NAME"));
			podtls.setPoDtlId(rs.getString("PO_DTL_ID"));
			podtls.setQty(rs.getString("QTY"));
			podtls.setUnitRate(rs.getString("UNITE_RATE"));
			podtls.setTotalValue(rs.getString("TOTAL_VALUE"));
			podtls.setProductCode(rs.getString("PRODUCT_CODE"));
			podtls.setDescription(rs.getString("DESCRIPTION"));
			podtls.setDtlQty(rs.getString("DTL_QTY"));
			podtls.setMaterial(rs.getString("MATERIAL"));
			podtls.setSpecification(rs.getString("SPECIFICATION"));
			podtls.setUnit(rs.getString("UNIT"));
		} catch (Exception e) {
			logger.error("PodtlsForProductRowMapper Exception--->" + e);
		}
		return podtls;
	}
}
