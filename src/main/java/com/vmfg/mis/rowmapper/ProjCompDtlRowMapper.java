package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.getPojCompDtlEntity;

public class ProjCompDtlRowMapper implements RowMapper<getPojCompDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjCompDtlRowMapper.class);
	
	@Override
	public getPojCompDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		getPojCompDtlEntity lst = new getPojCompDtlEntity();
		try {
			lst.setProjName(rs.getString("PROJECT_NAME"));
			lst.setProjCode(rs.getString("PROJECT_CODE"));
			lst.setPlanStart(rs.getString("PLANNED_START_DATE"));
			lst.setPlanEnd(rs.getString("PLANNED_COMPLETED_DATE"));
			lst.setActName(rs.getString("ACTIVITY_NAME"));
			lst.setCustomerName(rs.getString("CUSTOMER_NAME"));
			lst.setActualStart(rs.getString("ACTUAL__START_DATE"));
//			lst.setActualEnd(rs.getString("ACTUAL_END_DATE"));
			lst.setCompletedDate(rs.getString("COMPLETED_DATE"));
			lst.setDelay(rs.getString("DELAY"));
		}catch(Exception ex){
			logger.error("ProjCompDtlRowMapper  Method Exception" + ex);
		}
		return lst;
	}

}
