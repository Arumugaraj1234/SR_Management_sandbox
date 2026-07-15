package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGrpScpStatusEntity;

public class IndentGrpScpStatusRowMapper implements RowMapper<IndentGrpScpStatusEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGrpScpStatusRowMapper.class);

	@Override
	public IndentGrpScpStatusEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentGrpScpStatusEntity igs = new IndentGrpScpStatusEntity();
		try {
			igs.setIgScpId(rs.getString("IG_SCS_ID"));
			igs.setIgScpStatusId(rs.getString("IG_SCSS_ID"));
			igs.setRemarks(rs.getString("REMARKS"));
			igs.setSeqNo(rs.getString("SEQUENCE_NO"));
			igs.setSeqStatus(rs.getString("SEQUENCE_STATUS"));
		} catch (Exception ex) {
			logger.error("IndentGrpScpStatusRowMapper error " + ex);
		}
		return igs;
	}

}





