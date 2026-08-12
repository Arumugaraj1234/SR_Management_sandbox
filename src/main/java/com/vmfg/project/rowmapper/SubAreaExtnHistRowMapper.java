package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.SubAreaExtnHistEntity;

public class SubAreaExtnHistRowMapper implements RowMapper<SubAreaExtnHistEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SubAreaExtnHistRowMapper.class);

	@Override
	public SubAreaExtnHistEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		SubAreaExtnHistEntity ph = new SubAreaExtnHistEntity();
		try {
			ph.setPkseHistId(row.getString("PKSE_HIST_ID"));
			ph.setElementHdr(row.getString("ELEMENT_HDR"));
			ph.setElementDtl(row.getString("ELEMENT_DTL"));
			ph.setSpecification(row.getString("SPECIFICATION"));
			ph.setMake(row.getString("MAKE"));
			ph.setAllocatedQty(row.getString("ALLOCATED_QTY"));
			ph.setAllocatedvalue(row.getString("ALLOCATED_VALUE"));
			ph.setSource(row.getString("SOURCE"));
			ph.setEmpName(row.getString("EMPLOYEE_FIRSTNAME"));
			ph.setCreatedOn(row.getString("CREATED_ON"));
		} catch (Exception e) {
			logger.error("SubAreaExtnHistRowMapper Exception--->" + e);
		}
		return ph;
	}

}
