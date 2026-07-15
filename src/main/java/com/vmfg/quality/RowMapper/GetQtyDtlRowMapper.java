package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.GetQtyDtlEntity;

public class GetQtyDtlRowMapper implements RowMapper<GetQtyDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetQtyDtlRowMapper.class);

	@Override
	public GetQtyDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetQtyDtlEntity ph = new GetQtyDtlEntity();
		try {

			ph.setCreatedDatetime(row.getString("CREATED_DATETIME"));
			ph.setCustomerName(row.getString("CUSTOMER_NAME"));
			ph.setInitatedDate(row.getString("INTIATED_DATE"));
			ph.setLastUpdatedDatetime(row.getString("LAST_UPDATED_DATETIME"));
			ph.setPmHdrId(row.getString("PM_HDR_ID"));
			ph.setProjectCode(row.getString("PROJECT_CODE"));
			ph.setProjectDesc(row.getString("PROJECT_DESCRIPTION"));
			ph.setProjectName(row.getString("PROJECT_NAME"));
			ph.setQhdrId(row.getString("Q_HDR_ID"));
			ph.setTenantId(row.getString("TENANT_ID"));
			ph.setTransactionstageCode(row.getString("TRANSACTION_STAGE"));
			ph.setTransactionstageDesc(row.getString("STAGE_DESC"));
			ph.setTransactionstageseq(row.getString("TRANSACTION_STAGE_SEQ"));
			ph.setTransactionstatusCode(row.getString("TRANSACTION_STATUS"));
			ph.setHdrStatusDesc(row.getString("STATUS_DESC"));
			ph.setTransactionstatusseq(row.getString("TRANSACTION_STATUS_SEQ"));

			if (columnExists(row, "ENQUIRY_ID")) {
				ph.setEnquiryId((row.getString("ENQUIRY_ID")));
			}
			if (columnExists(row, "Q_DTL_ID")) {
				ph.setQtyDtlId((row.getString("Q_DTL_ID")));
			}
		} catch (Exception e) {
			logger.error("GetQtyDtlRowMapper Exception--->" + e);
		}
		return ph;
	}

	private boolean columnExists(ResultSet row, String columnName) throws SQLException {
		ResultSetMetaData metaData = row.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}
}
