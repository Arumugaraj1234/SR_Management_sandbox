package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGroupDetailsEntity;

public class IndentGroupDetailsRowMapper implements RowMapper<IndentGroupDetailsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGroupDetailsRowMapper.class);

	@Override
	public IndentGroupDetailsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		IndentGroupDetailsEntity result = new IndentGroupDetailsEntity();
		try {
			result.setGroupName(rs.getString("GROUP_NAME"));
//			result.setPartCount(rs.getString("PART_COUNT"));
			result.setSbcCode(rs.getString("SBC_CODE"));
			if (columnExists(rs, "CREATED_BY")) {
			result.setEmpId(rs.getString("CREATED_BY"));
			}
			result.setSbcDesc(rs.getString("SBC_DESC"));
			result.setExpectedDeliveryDate(rs.getString("EXPECTED_DELIVERY_DATE"));
			result.setIgHdrId(rs.getString("IG_HDR_ID"));
			result.setIndentCode(rs.getString("INDENT_CODE"));
			result.setIndentId(rs.getString("INDENT_ID"));
			result.setPskDesc(rs.getString("PSK_DESC"));
			result.setPskId(rs.getString("PKSA_ID"));
			result.setPkId(rs.getString("PKA_ID"));
			result.setPkDesc(rs.getString("PK_DESC"));
			result.setTargetCost(rs.getString("TARGET_VALUE"));
			result.setIsInventory(rs.getString("IS_INVENTORY"));
			if (columnExists(rs, "TYPE")) {
				result.setType(rs.getString("TYPE"));
			}
			if (columnExists(rs, "IG_SCS_ID")) {
				result.setScsId(rs.getString("IG_SCS_ID"));
			}
			if(columnExists(rs, "EMPLOYEE_FIRSTNAME")){
				result.setPjsCreatedPerson(rs.getString("EMPLOYEE_FIRSTNAME"));
			}
		} catch (Exception ex) {
			logger.error("IndentGroupDetailsRowMapper error " + ex);
		}
		return result;
	}
	
	//column checking purpose (column is there or not)
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
