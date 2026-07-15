package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.GetindentbudgetDtlEntity;

public class GetindentbudgetDtlRowMapper implements RowMapper<GetindentbudgetDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetindentbudgetDtlRowMapper.class);

	@Override
	public GetindentbudgetDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetindentbudgetDtlEntity ph = new GetindentbudgetDtlEntity();
		try {
		
			ph.setAllocatedQty(row.getString("ALLOCATED_QTY"));
			ph.setAllocatedVal(row.getString("ALLOCATED_VALUE"));
			ph.setElementDtl(row.getString("ELEMENT_DTL"));
			ph.setIndentBudId(row.getString("INDENT_BUD_ID"));
			ph.setElementHdr(row.getString("ELEMENT_HDR"));
			ph.setIndentDtlId(row.getString("INDENT_DTL_ID"));
			ph.setPskDesc(row.getString("PSK_DESC"));
		} catch (Exception e) {
			logger.error("GetindentbudgetDtlRowMapper Exception--->" + e);
		}
		return ph;
	}

}
