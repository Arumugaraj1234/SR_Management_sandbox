package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.response.DesignHdr;

public class DesignHdrRowMapper implements RowMapper<DesignHdr> {
	private static final Logger logger = LoggerFactory.getLogger(DesignHdrRowMapper.class);

	@Override
	public DesignHdr mapRow(ResultSet rs, int rowNum) throws SQLException {
		DesignHdr res = new DesignHdr();
		try {
			res.setProjectID(rs.getString("PM_HDR_ID"));
			res.setDesignID(rs.getString("DE_HDR_ID"));
			res.setProjectName(rs.getString("PROJECT_NAME"));
			res.setCustomerName(rs.getString("CUSTOMER_NAME"));
			res.setRequestedBy(rs.getString("EMPLOYEE_FIRSTNAME"));
			res.setActualStartDate(rs.getString("ACTUAL_START_DATE"));
			res.setActualEndDate(rs.getString("ACTUAL_END_DATE"));
			res.setPlannedStartDate(rs.getString("PLANNED_START_DATE"));
			res.setDueDate(rs.getString("PLANNED_END_DATE"));
			res.setRequestedByID(rs.getString("REQUESTED_BY"));
			res.setProjectCode(rs.getString("PROJECT_CODE"));
			res.setEnquiryId(rs.getString("ENQUIRY_ID"));
			res.setHdrStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			res.setStagName(rs.getString("STG_DESC"));
			res.setDesignCode(rs.getString("DESIGN_CODE"));
			res.setIsInternal(rs.getString("IS_INTERNAL"));
		} catch (Exception ex) {
			logger.error("DesignHdrRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
