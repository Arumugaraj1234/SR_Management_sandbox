package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.DesignWidgetDtlListEntity;

public class DesignWidgetDtlRowMapper implements RowMapper<DesignWidgetDtlListEntity>{
	private static final Logger logger = LoggerFactory.getLogger(DesignWidgetDtlRowMapper.class);

	@Override
	public DesignWidgetDtlListEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DesignWidgetDtlListEntity res = new DesignWidgetDtlListEntity();
		try {
			res.setCompletedStatus(rs.getString("IS_COMPLETED"));
			res.setQty(rs.getString("QTY"));
		}catch (Exception e) {
			logger.error("DesignWidgetDtlRowMapper  Method Exception" + e);
		}
		return res;
	}

}
