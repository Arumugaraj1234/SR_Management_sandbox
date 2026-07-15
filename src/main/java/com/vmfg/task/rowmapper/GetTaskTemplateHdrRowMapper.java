package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.response.GetTaskTemplateHdrResponse;

public class GetTaskTemplateHdrRowMapper implements RowMapper<GetTaskTemplateHdrResponse> {
	private static final Logger logger = LoggerFactory.getLogger(GetTaskTemplateHdrRowMapper.class);

	@Override
	public GetTaskTemplateHdrResponse mapRow(ResultSet row, int rowNum) throws SQLException {
		GetTaskTemplateHdrResponse tm = new GetTaskTemplateHdrResponse();
		try {
			tm.setTeHdrId(row.getString("TT_HDR_ID"));
			tm.setTempName(row.getString("TT_NAME"));
			
		} catch (Exception e) {
			logger.error("GetTaskTemplateHdrRowMapper Exception--->" + e);
		}
		return tm;
	}


}
