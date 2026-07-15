package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class flashReportMailConfigRowMapper implements RowMapper<FlashReportEmailConfigHdr>{

	@Override
	public FlashReportEmailConfigHdr mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		FlashReportEmailConfigHdr hdr=new FlashReportEmailConfigHdr();
		try {
			hdr.setMaintenanceApprovedByName(rs.getString("MAINTENANCE_APPROVED_BYname"));
			hdr.setPeApprovedByName(rs.getString("PE_APPROVED_BYname"));
			hdr.setProductionApprovedByName(rs.getString("PRODUCTION_APPROVED_BYname"));
			hdr.setQualityApprovedByName(rs.getString("QUALITY_APPROVED_BYname"));
			
			hdr.setMaintenanceApprovedBy(rs.getString("MAINTENANCE_APPROVED_BY"));
			hdr.setPeApprovedBy(rs.getString("PE_APPROVED_BY"));
			hdr.setProductionApprovedBy("PRODUCTION_APPROVED_BY");
			hdr.setQualityApprovedBy(rs.getString("QUALITY_APPROVED_BY"));
		}catch (Exception e) {
			
		}
		return hdr;
	}

}
