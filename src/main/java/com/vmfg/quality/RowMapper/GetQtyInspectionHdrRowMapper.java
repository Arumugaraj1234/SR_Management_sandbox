package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.GetQtyInspectionHdrEntity;

public class GetQtyInspectionHdrRowMapper implements RowMapper<GetQtyInspectionHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetQtyInspectionHdrRowMapper.class);

	@Override
	public GetQtyInspectionHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetQtyInspectionHdrEntity entity = new GetQtyInspectionHdrEntity();
		try {

			  entity.setQiHdrId(rs.getString("QI_HDR_ID"));
		        entity.setQiId(rs.getString("QI_ID"));
		        entity.setPmHdrId(rs.getString("PM_HDR_ID"));
		        entity.setQualityRefNo(rs.getString("QUALITY_REF_NO"));
		        entity.setInspectionQty(rs.getString("INSPECTION_QTY"));
		        entity.setOkQty(rs.getString("OK_QTY"));
		        entity.setCaInternal(rs.getString("CA_INTERNAL"));
		        entity.setCaVendor(rs.getString("CA_VENDOR"));
		        entity.setRevisionDate(rs.getString("REVISION_DATE"));
		        entity.setInspectionType(rs.getString("INSPECTION_TYPE"));
		        entity.setConfigName(rs.getString("CONFIG_NAME"));
		        entity.setReworkInternal(rs.getString("REWORK_INTERNAL"));
		        entity.setReworkVendor(rs.getString("REWORK_VENDOR"));
		        entity.setRejectedInternal(rs.getString("REJECTED_INTERNAL"));
		        entity.setRejectedExternal(rs.getString("REJECTED_EXTERNAL"));
		        entity.setQualityRating(rs.getString("QUALITY_RATING"));
		        entity.setInspectedBy(rs.getString("INSPECTED_BY"));
		        entity.setInspectedOn(rs.getString("INSPECTED_ON"));
		        entity.setSequenceNo(rs.getString("SEQUENCE_NO"));
		        entity.setSequenceStatus(rs.getString("SEQUENCE_STATUS"));
		        entity.setIsCompleted(rs.getString("IS_COMPLETED"));
		        entity.setTenantId(rs.getString("TENANT_ID"));
		        entity.setTotalCa(rs.getString("CA_TOTAL"));
		        entity.setTotalrejected(rs.getString("REJECTED_TOTAL"));
		        entity.setTotalRework(rs.getString("REWORK_TOTAL"));
		        entity.setPmHdrCode(rs.getString("PROJECT_CODE"));
		        entity.setPmHdrDesc(rs.getString("PROJECT_DESCRIPTION"));
		        entity.setPmHdrCustomerName(rs.getString("CUSTOMER_NAME"));
		        entity.setPoCode(rs.getString("PO_CODE"));
		        entity.setUomDesc(rs.getString("UOM_LONG_DESCRIPTION"));
		        entity.setProductCode(rs.getString("PRODUCT_CODE"));
		        entity.setVendorName(rs.getString("VENDOR_NAME"));
		        entity.setSNo(rs.getInt("S_NO"));
		        entity.setDescription(rs.getString("DESCRIPTION"));
		        entity.setInwardRating(rs.getString("INWARD_RATING"));
		        entity.setSupplierRating(rs.getString("SUPPLIER_RATING"));
		        entity.setEmpDesc(rs.getString("EMPLOYEE_FIRSTNAME"));
		        entity.setEmpId(rs.getString("LAST_UPDATED_BY"));
		        entity.setOldsupplierRating(rs.getString("OLD_SUPPLIER_RATING"));
		        entity.setCustomerComplaint(rs.getString("CUSTOMER_COMPLAINT"));
		} catch (Exception e) {
			logger.error("GetQtyInspectionHdrRowMapper Exception--->" + e);
		}
		return entity;
	}

}
