package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.IndentHdrDtlsEntity;

public class IndentHdrDtlsRowMapper implements RowMapper<IndentHdrDtlsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentHdrDtlsRowMapper.class);

	@Override
	public IndentHdrDtlsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentHdrDtlsEntity res = new IndentHdrDtlsEntity();
		try {
			res.setIndentId(rs.getString("INDENT_ID"));
			res.setCreatedBy(rs.getString("CREATED_BY"));
			res.setCreatedOn(rs.getString("CREATED_DATE"));
			res.setIndentCode(rs.getString("INDENT_CODE"));
			res.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
			res.setKeyAreaDesc(rs.getString("PK_DESC"));
			res.setProjectName(rs.getString("PROJECT_NAME"));
			res.setSbcDesc(rs.getString("SBC_DESC"));
			res.setStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			res.setSubKeyAreaDesc(rs.getString("PSK_DESC"));
			res.setExpectedDeliveryDate(rs.getString("EXPECTED_DELIVERY_DATE"));
			//res.setIndentClosedDate(rs.getString("CLOSED_DATE"));
			if (columnExists(rs, "CLOSED_DATE")) {
				res.setIndentClosedDate(rs.getString("CLOSED_DATE"));
			}
			if (columnExists(rs, "PKSA_ID")) {
				res.setKeyAreaId(rs.getString("PKSA_ID"));
			}
			if (columnExists(rs, "TARGET_VALUE")) {
				res.setTargetCost(rs.getString("TARGET_VALUE"));
			}
			if (columnExists(rs, "SEQUENCE_N0")) {
				res.setStatusSeq(rs.getString("SEQUENCE_N0"));
			}
			if (columnExists(rs, "REVISION_NO")) {
				res.setRevisionNo(rs.getString("REVISION_NO"));
			}
			if (columnExists(rs, "REVISION_DATE")) {
				res.setRevisionOn(rs.getString("REVISION_DATE"));
			}
			
			if (columnExists(rs, "EMPLOYEE_ID")) {
				res.setCreatedUserId(rs.getString("EMPLOYEE_ID"));
			}
			
		} catch (Exception ex) {
			logger.error("IndentHdrDtlsRowMapper  Method Exception" + ex);
		}
		return res;
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
