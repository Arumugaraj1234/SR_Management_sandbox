package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class LinkUrlRowMapper implements RowMapper<LinkUrlEntity>{
	private static final Logger logger = LoggerFactory.getLogger(LinkUrlRowMapper.class);

	@Override
	public LinkUrlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		LinkUrlEntity result=new LinkUrlEntity();
		try {
			
			result.setLinkurl(rs.getString("LINK_URL"));
		}catch(Exception ex) {
			logger.error("LinkUrlRowMapper error "+ex);
		}
		return result;
	}

}
