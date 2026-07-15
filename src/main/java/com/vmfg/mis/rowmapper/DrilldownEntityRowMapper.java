package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.DrilldownEntity;

public class DrilldownEntityRowMapper implements RowMapper<DrilldownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DrilldownEntityRowMapper.class);

	@Override
	public DrilldownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DrilldownEntity lst = new DrilldownEntity();
		try {
			lst.setCustomerName(rs.getString("CUSTOMER_NAME"));
			lst.setDeliveryDate(rs.getString("EXPECTED_DELIVERY_DATE"));
			lst.setDescription(rs.getString("DESCRIPTION"));
			lst.setIndentCode(rs.getString("INDENT_CODE"));
			lst.setMake(rs.getString("MAKE"));
			lst.setProductCode(rs.getString("PRODUCT_CODE"));
			lst.setProjectDescription(rs.getString("PROJECT_DESCRIPTION"));
	        lst.setProjectName(rs.getString("PROJECT_NAME"));
	        lst.setSpecification(rs.getString("SPECIFICATION"));
//	        lst.setAssignedPerson(rs.getString("EMPLOYEE_FIRSTNAME"));
	        lst.setIndentTypeCode(rs.getString("INDENT_TYPE_CODE"));
	        lst.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
	        lst.setPmHdrId(rs.getString("PM_HDR_ID"));
	        lst.setProjectCode(rs.getString("PROJECT_CODE"));
	        lst.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
	        lst.setQty(rs.getString("QTY"));
	        lst.setStation(rs.getString("PK_DESC"));
	        lst.setSubAssy(rs.getString("PSK_DESC"));
	        if (columnExists(rs, "TYPE")) {
	        	lst.setType(rs.getString("TYPE"));
	        }
	        
	        
		}catch(Exception ex) {
			logger.error("DrilldownEntityRowMapper  Method Exception" + ex);
		}
		return lst;
	}
	
	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}
}
