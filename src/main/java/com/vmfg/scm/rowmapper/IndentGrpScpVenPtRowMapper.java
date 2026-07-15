package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGrpScpVenPtEntity;

public class IndentGrpScpVenPtRowMapper implements RowMapper<IndentGrpScpVenPtEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGrpScpVenPtRowMapper.class);

	@Override
	public IndentGrpScpVenPtEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentGrpScpVenPtEntity igs = new IndentGrpScpVenPtEntity();
		try {
			igs.setIgScpId(rs.getString("IG_SCS_ID"));
			igs.setIgScpVpt(rs.getString("IG_SCS_VPT"));
			igs.setLevel(rs.getString("LEVEL"));
			igs.setPercentage(rs.getString("PERCENTAGE"));
			igs.setTerm(rs.getString("TERM"));
			igs.setRemarks(rs.getString("REMARKS"));
		} catch (Exception ex) {
			logger.error("IndentGrpScpVenPtRowMapper error " + ex);
		}
		return igs;
	}

}





