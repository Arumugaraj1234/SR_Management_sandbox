package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.GetAssyDtlEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAssyDtlRowMapper implements RowMapper<GetAssyDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetAssyDtlRowMapper.class);

	@Override
	public GetAssyDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetAssyDtlEntity tm = new GetAssyDtlEntity();
		try {

			tm.setActualEndDate(row.getString("ACTUAL_END_DATE"));
			tm.setActualStartDate(row.getString("ACTUAL_START_DATE"));
			tm.setAssyHdrId(row.getString("ASSY_HDR_ID"));
			tm.setCreatedDateTime(row.getString("CREATED_DATETIME"));
			tm.setCustomerName(row.getString("CUSTOMER_NAME"));
			tm.setLastUpdatedDateTime(row.getString("LAST_UPDATED_DATETIME"));
			tm.setPlanEndDate(row.getString("PLANNED_END_DATE"));
			tm.setPlanStartDate(row.getString("PLANNED_START_DATE"));
			tm.setPmHdrId(row.getString("PM_HDR_ID"));
			tm.setProductDetails(row.getString("PRODUCT_DETAILS"));
			tm.setProjectDesc(row.getString("PROJECT_DESCRIPTION"));
			tm.setProjectName(row.getString("PROJECT_NAME"));
			tm.setRequestBy(row.getString("REQUESTED_BY"));
			tm.setRequestDate(row.getString("REQUEST_DATE"));
			tm.setStartMaterialReq(row.getString("START_MATERIAL_REQUEST"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTransactionStage(row.getString("TRANSACTION_STAGE"));
			tm.setTransactionStageDesc(row.getString("STAGE_DESC"));
			tm.setTransactionstatus(row.getString("TRANSACTION_STATUS"));
			tm.setTransactionstatusSeq(row.getString("TRANSACTION_STATUS_SEQ"));
			tm.setHdrStatusDesc(row.getString("STATUS_DESC"));
			tm.setTransactionStageSeq(row.getString("TRANSACTION_STAGE_SEQ"));
			tm.setProjectCode(row.getString("PROJECT_CODE"));
			tm.setEnquiryId(row.getString("ENQUIRY_ID"));
		} catch (Exception e) {
			logger.error("GetAssyDtlRowMapper Exception--->" + e);
		}
		return tm;
	}
}
