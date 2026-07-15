package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class QueryForObjectRowMapper implements RowMapper<QueryForObjectEntity>{
	private static final Logger logger = LoggerFactory.getLogger(QueryForObjectRowMapper.class);
	@Override
	public QueryForObjectEntity mapRow(ResultSet row, int arg1) throws SQLException {
		QueryForObjectEntity q = new QueryForObjectEntity();
		try {			
			q.setStrValue(row.getString("emp"));
					
		}catch(Exception ex) {
			logger.error("QueryForObjectRowMapper Exception --->"+ex);
		}
		return q;
	}

}
