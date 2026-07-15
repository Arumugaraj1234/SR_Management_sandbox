package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.GetSCMWidgetDtlEntity;

public class SCMWidgetDtlRowMapper implements RowMapper<GetSCMWidgetDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SCMWidgetDtlRowMapper.class);

	@Override
	public GetSCMWidgetDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetSCMWidgetDtlEntity ph = new GetSCMWidgetDtlEntity();
		try {
			ph.setItemsDelayed(row.getString("ITEMS_DELAYED"));
			ph.setNoOfPo(row.getString("TOTAL_PO_RELEASED"));
			ph.setPendingIndents(row.getString("PENDING_INDENTS"));
			
		} catch (Exception e) {
			logger.error("SCMWidgetDtlRowMapper Exception--->" + e);
		}
		return ph;
	}
}

