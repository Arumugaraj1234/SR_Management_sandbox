package com.vmfg.export.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.export.entity.DcRequestHdrEntity;



public class DcRequestHdrRowMapper implements RowMapper<DcRequestHdrEntity>{
	
	private static final Logger logger = LoggerFactory.getLogger(DcRequestHdrRowMapper.class);

	@Override
	public DcRequestHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DcRequestHdrEntity ent = new DcRequestHdrEntity();
		try {
			ent.setDcrId(rs.getString("DCR_ID"));
			ent.setReqOn(rs.getString("REQUESTED_ON"));
			ent.setReqBy(rs.getString("REQUESTED_BY"));
			ent.setPmHdrId(rs.getString("PM_HDR_ID"));
			ent.setRemarks(rs.getString("MR_CODE"));
			ent.setIsCompleted(rs.getString("IS_COMPLETED"));
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("DcRequestHdrRowMapper method error"+e);
		}
		return ent;
	}

}
