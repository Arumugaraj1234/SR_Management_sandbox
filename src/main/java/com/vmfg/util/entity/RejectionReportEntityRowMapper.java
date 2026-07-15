package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class RejectionReportEntityRowMapper implements RowMapper<RejectionReportEntity>{
	private static final Logger logger = LoggerFactory.getLogger(RejectionReportEntityRowMapper.class);
	@Override
	public RejectionReportEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		RejectionReportEntity rre = new RejectionReportEntity();
		try {
			
			rre.setOperation(row.getString("OPERATION_ID"));
			rre.setProduct(row.getString("PRODUCT_CODE"));
			rre.setRejectionQty(row.getString("REJECTED_QUANTITY"));
			rre.setRejectionReason(row.getString("REASON_CODE"));
			rre.setShift(row.getString("SHIFT"));
			rre.setShiftDate(row.getString("SHIFT_DATE"));
			rre.setReportRejectionId(row.getString("REPORT_REJECTION_ID"));
			
		} catch (Exception e) {
			logger.error("PPMDetailsRowMapper RowMapper Exception------>"+e);
		}
		return rre;
	}
}