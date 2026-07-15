package com.vmfg.util.entity;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class QueryForIntegerRowMapper implements RowMapper<QueryForObjectEntity>{
	private static final Logger logger = LoggerFactory.getLogger(QueryForIntegerRowMapper.class);

	@Override
	public QueryForObjectEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		QueryForObjectEntity q = new QueryForObjectEntity();
		try {				
			q.setNumberValue(rs.getInt("NUMBER"));			
					
		}catch(Exception ex) {
			logger.error("QueryForIntegerRowMapper Exception --->"+ex);
		}
		return q;
	}
}
