package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.TeamMemberLoadEntity;

public class TeamMemberLoadRowMapper implements RowMapper<TeamMemberLoadEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SupplierRatingRowMapper.class);
	
	@Override
	public TeamMemberLoadEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TeamMemberLoadEntity lst = new TeamMemberLoadEntity();
		try {
			lst.setInspCall(rs.getString("INSPECTION_CALL"));
			lst.setInspOn(rs.getString("INSPECTED_ON"));
		}catch(Exception ex) {
			logger.error("TeamMemberLoadRowMapper Method Exception" + ex);
		}
		return lst;
	}
}
