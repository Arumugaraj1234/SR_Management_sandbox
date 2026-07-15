package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ThreePointCheckSheetRowMapper implements RowMapper<ThreePointCheckSheetEmailConfigHdr>{
	private static final Logger logger = LoggerFactory.getLogger(ThreePointCheckSheetRowMapper.class);
	@Override
	public ThreePointCheckSheetEmailConfigHdr mapRow(ResultSet rs, int rowNum) throws SQLException {
		ThreePointCheckSheetEmailConfigHdr hdr=new ThreePointCheckSheetEmailConfigHdr();
		try {
			hdr.setApprovedByName(rs.getString("APPROVED_BYname"));
			hdr.setCheckedByName(rs.getString("CHECKED_BYname"));
			hdr.setPreparedByName(rs.getString("PREPARED_BYname"));
			hdr.setApprovedBy(rs.getString("APPROVED_BY"));
			hdr.setCheckedBy(rs.getString("CHECKED_BY"));
			hdr.setPreparedBy(rs.getString("PREPARED_BY"));
		}catch (Exception e) {
			logger.error("ThreePointCheckSheetRowMapper Exception--->"+e);
		}
		return hdr;
	}

}
