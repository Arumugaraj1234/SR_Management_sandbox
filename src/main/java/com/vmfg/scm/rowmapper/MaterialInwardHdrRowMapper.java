package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.MaterialInwardHdrEntity;

public class MaterialInwardHdrRowMapper implements RowMapper<MaterialInwardHdrEntity> {
	@Override
	public MaterialInwardHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MaterialInwardHdrEntity entity = new MaterialInwardHdrEntity();
		entity.setMiId(rs.getString("MI_ID"));
		entity.setMiCode(rs.getString("MI_CODE"));
		entity.setTransactionNo(rs.getString("TRANSACTION_NO"));
		entity.setFinancialYearMstId(rs.getString("FINANCIAL_YEAR_MST_ID"));
		entity.setPoId(rs.getString("PO_ID"));
		entity.setPoCode(rs.getString("PO_CODE"));
		entity.setDcId(rs.getString("DC_ID"));
		entity.setDcCode(rs.getString("DC_CODE"));
		entity.setInwardDate(rs.getString("INWARD_DATE"));
		entity.setVendorCode(rs.getString("VENDOR_CODE"));
		entity.setDcNo(rs.getString("DC_NO"));
		entity.setDcDate(rs.getString("DC_DATE"));
		entity.setNoOfParts(rs.getString("NO_OF_PARTS"));
		entity.setStatus(rs.getString("STATUS"));
		entity.setIsLatest(rs.getString("IS_LATEST"));
		entity.setCreatedOn(rs.getString("CREATED_ON"));
		entity.setCreatedBy(rs.getString("CREATED_BY"));
		entity.setLastUpdatedOn(rs.getString("LAST_UPDATED_ON"));
		entity.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
		entity.setTenantId(rs.getString("TENANT_ID"));
		entity.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
		entity.setVendorName(rs.getString("VENDOR_NAME"));
		entity.setStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
		entity.setMaterialLocation(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
		entity.setInvFlag(rs.getString("INV_FLAG"));
		entity.setInwardRating(rs.getString("INWARD_RATING"));
		entity.setRelationshipRating(rs.getString("RELATIONSHIP_RATING"));
		entity.setProjectCode(rs.getString("PROJECT_CODE"));
		entity.setProjectName(rs.getString("PROJECT_NAME"));
		entity.setSeqFlag(rs.getString("SEQ_FLAG"));
		entity.setGrnCode(rs.getString("GRN_CODE"));
		entity.setGrnDate(rs.getString("GRN_DATE"));
		return entity;
	}
}