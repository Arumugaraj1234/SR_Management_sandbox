package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ImageUrlRowMapper implements RowMapper<ImageUrlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ImageUrlRowMapper.class);

	@Override
	public ImageUrlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		ImageUrlEntity result= new ImageUrlEntity();
		try {
			result.setImageUrl(rs.getString(""));
			result.setTenantId(rs.getString(""));
		}catch(Exception ex) {
		logger.error("ImageUrlRowMapper error "+ex);	
		}
		return null;
	}

}
