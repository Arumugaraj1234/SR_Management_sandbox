package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.getLinkStatusByPMIdRespEntity;

public class getLinkStatusByPMIdRespRowMapper  implements RowMapper<getLinkStatusByPMIdRespEntity> {
	private static final Logger logger = LoggerFactory.getLogger(getLinkStatusByPMIdRespRowMapper.class);

	@Override
	public getLinkStatusByPMIdRespEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		getLinkStatusByPMIdRespEntity ph = new getLinkStatusByPMIdRespEntity();
		try {
			ph.setCustomerName(row.getString("CUSTOMER_NAME"));
			ph.setPmHdrId(row.getString("PM_HDR_ID"));
			ph.setPkId(row.getString("PK_ID"));
			ph.setPkDesc(row.getString("PK_DESC"));
			ph.setTotalCount(row.getString("TOTAL_COUNT"));
			ph.setAllocatedVal(row.getString("ALLOCATED"));
			ph.setPkaId(row.getString("PKA_ID"));
		} catch (Exception e) {
			logger.error("getLinkStatusByPMIdRespRowMapper Exception--->" + e);
		}
		return ph;
	}

}
