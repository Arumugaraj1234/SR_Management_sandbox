package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGroupHdrAndDtlEntity;

public class IndentGroupHdrAndDtlRowMapper implements RowMapper<IndentGroupHdrAndDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGroupHdrAndDtlRowMapper.class);

	@Override
	public IndentGroupHdrAndDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		IndentGroupHdrAndDtlEntity result = new IndentGroupHdrAndDtlEntity();
		try {
			result.setDescription(rs.getString("DESCRIPTION"));
			result.setMake(rs.getString("MAKE"));
			result.setMaterial(rs.getString("MATERIAL"));
			result.setProductCode(rs.getString("PRODUCT_CODE"));
			result.setIndentQty(rs.getString("INDENT_QTY"));
			result.setIndentGrpQty(rs.getString("INDENT_GRP_QTY"));
			if(columnExists(rs, "DIFFERENCE_QTY")){
			result.setDifferenceQty(rs.getString("DIFFERENCE_QTY"));
			}
			result.setRemarks(rs.getString("REMARKS"));
			result.setSpecification(rs.getString("SPECIFICATION"));
			result.setWeight(rs.getString("WEIGHT"));
			
			if (columnExists(rs, "RESP_QTY")) {
				result.setDifferenceQty(rs.getString("RESP_QTY"));
			}
			
			if (columnExists(rs, "UOM")) {
				result.setUom(rs.getString("UOM"));
			}

			if (columnExists(rs, "INDENT_DTL_ID")) {
				result.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			}
			if (columnExists(rs, "IG_DTL_ID")) {
				result.setIndentGrpDtlId(rs.getString("IG_DTL_ID"));
			}

		} catch (Exception ex) {
			logger.error("IndentGroupHdrAndDtlRowMapper error " + ex);
		}
		return result;
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
