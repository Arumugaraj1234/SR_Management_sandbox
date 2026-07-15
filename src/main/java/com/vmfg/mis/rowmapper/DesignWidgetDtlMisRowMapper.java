package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.DesignWidgetDtlEntity;

public class DesignWidgetDtlMisRowMapper implements RowMapper<DesignWidgetDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DesignWidgetDtlMisRowMapper.class);

	@Override
	public DesignWidgetDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DesignWidgetDtlEntity lst = new DesignWidgetDtlEntity();
		try {
//			lst.setProjCnt(rs.getString("PM_HDR_ID"));
//			lst.setAvgTasktime(rs.getString("AVG_TASK_TIME"));
			lst.setAvgProjtime(rs.getString("AVG_PROJ_DATETIME"));
		}catch(Exception ex) {
			logger.error("DesignWidgetDtlMisRowMapper  Method Exception" + ex);
		}
		return lst;
	}
}
