package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.IndentDtlTblEntity;

public class IndentDtlTblRowMapper implements RowMapper<IndentDtlTblEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentDtlTblRowMapper.class);

	@Override
	public IndentDtlTblEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentDtlTblEntity res = new IndentDtlTblEntity();
		try {
			
			if (columnExists(rs, "INDENT_DTL_ID")) {
				res.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			}
			if (columnExists(rs, "INDENT_ID")) {
				res.setIndentId(rs.getString("INDENT_ID"));
			}
			if (columnExists(rs, "INDENT_TYPE_DESC")) {
				res.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
			}
			if (columnExists(rs, "INDENT_TYPE_CODE")) {
				res.setIndentType(rs.getString("INDENT_TYPE_CODE"));
			}
			if (columnExists(rs, "MATERIAL")) {
				res.setMaterial(rs.getString("MATERIAL"));
			}
			if (columnExists(rs, "DESCRIPTION")) {
				res.setDescription(rs.getString("DESCRIPTION"));
			}
			if (columnExists(rs, "MAKE")) {
				res.setMake(rs.getString("MAKE"));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				res.setProductCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "QTY")) {
				res.setQty(rs.getString("QTY"));
			}
			if (columnExists(rs, "SPECIFICATION")) {
				res.setSpecification(rs.getString("SPECIFICATION"));
			}
			if (columnExists(rs, "TENANT_ID")) {
				res.setTenantId(rs.getString("TENANT_ID"));
			}
			if (columnExists(rs, "UNIT")) {
				res.setUnit(rs.getString("UNIT"));
			}
			if (columnExists(rs, "UOM_SHORT_DESCRIPTION")) {
				res.setUomDesc(rs.getString("UOM_SHORT_DESCRIPTION"));
			}
			if (columnExists(rs, "WEIGHT")) {
				res.setWeight(rs.getString("WEIGHT"));
			}
			if (columnExists(rs, "S_NO")) {
				res.setSNo(rs.getInt("S_NO"));
			}
			if (columnExists(rs, "REMARKS")) {
				res.setRemarks(rs.getString("REMARKS"));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				res.setAssignTeamEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}
			if (columnExists(rs, "EMP_ID")) {
				res.setAssignTeamEmpId(rs.getString("EMP_ID"));
			}
			if(columnExists(rs, "DM_ID")) {
				res.setDmId(rs.getInt("DM_ID"));
			}
			if(columnExists(rs, "CREATED_BY")) {
				res.setCreatedBy(rs.getString("CREATED_BY"));
			}
			if(columnExists(rs, "FILE_NAME_EXTN")) {
				res.setFileNameExtn(rs.getString("FILE_NAME_EXTN"));
			}
			if(columnExists(rs, "IS_PDF")) {
				res.setIsPdf(rs.getString("IS_PDF"));
			}
//			if(columnExists(rs, "PO_COUNT")) {
				res.setPoCount(1);
//			}
			res.setTotalQty("0");
			res.setTotalVal("0");
		} catch (Exception ex) {
			logger.error("IndentDtlTblRowMapper  Method Exception" + ex);
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
