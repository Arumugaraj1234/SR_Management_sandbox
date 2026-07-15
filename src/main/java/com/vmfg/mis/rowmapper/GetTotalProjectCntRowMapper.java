package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.ProjectCntlEntity;

public class GetTotalProjectCntRowMapper implements RowMapper<ProjectCntlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetTotalProjectCntRowMapper.class);

	@Override
	public ProjectCntlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjectCntlEntity lst = new ProjectCntlEntity();
		try {
			lst.setProjCnt(rs.getString("CNT"));
			lst.setProjContri(rs.getString("CONTRIBUTION_VALUE"));
			lst.setProjValue(rs.getString("PROJECT_VALUE"));
		}catch(Exception ex) {
			logger.error("GetTotalProjectCntRowMapper  Method Exception" + ex);
		}
		return lst;
	}

}
