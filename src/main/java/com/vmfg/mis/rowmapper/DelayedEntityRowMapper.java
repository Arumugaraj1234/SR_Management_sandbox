package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.DelayedEntity;

public class DelayedEntityRowMapper implements RowMapper<DelayedEntity> {
	private static final Logger logger = LoggerFactory.getLogger(AssyTaskReportRowMapper.class);

	@Override
	public DelayedEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DelayedEntity lst = new DelayedEntity();
		try {
			lst.setCreatedById(rs.getString("CREATED_BY"));
			lst.setCreatedDateTime(rs.getString("CREATED_DATETIME"));
			lst.setCustomerName(rs.getString("CUSTOMER_NAME"));
			lst.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			lst.setExpectedDeliveryDate(rs.getString("EXPECTED_DELIVERY_DATE"));
			lst.setIndentCode(rs.getString("INDENT_CODE"));
			lst.setIndentTypeCode(rs.getString("INDENT_TYPE_CODE"));
			lst.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
			lst.setProjectDescription(rs.getString("PROJECT_DESCRIPTION"));
			lst.setProjectName(rs.getString("PROJECT_NAME"));
			lst.setRevesionNumber(rs.getString("REVISION_NO"));
			lst.setRevisionDate(rs.getString("REVISION_DATE"));
//			lst.setSequenceNumber(rs.getString("SEQUENCE_N0"));
//			lst.setIndentDtlId(rs.getString("SEQUENCE_N0"));
			lst.setProjectCode(rs.getString("PROJECT_CODE"));
			lst.setDescription(rs.getString("DESCRIPTION"));
			lst.setMake(rs.getString("MAKE"));
			lst.setMaterial(rs.getString("MATERIAL"));
			lst.setProductCode(rs.getString("PRODUCT_CODE"));
			lst.setQty(rs.getString("QTY"));
//			lst.setRemarks(rs.getString("SEQUENCE_N0"));
			lst.setSpecification(rs.getString("SPECIFICATION"));
			lst.setUnit(rs.getString("UNIT"));
			lst.setWeight(rs.getString("WEIGHT"));
			lst.setStation(rs.getString("PK_DESC"));
			lst.setSubAssy(rs.getString("PSK_DESC"));
		}catch(Exception ex) {
			logger.error("DelayedEntityRowMapper Method Exception" + ex);
		}
		return lst;
	}

	

}
