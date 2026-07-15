package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class PrHdrEmailRowMapper implements RowMapper<PrHdrEmail> {
	private static final Logger logger = LoggerFactory.getLogger(PrHdrEmailRowMapper.class);

	@Override
	public PrHdrEmail mapRow(ResultSet row, int rowNum) throws SQLException {
		PrHdrEmail pr = new PrHdrEmail();
		try {

			pr.setCurrentSeq(row.getString("CURRENT_APPROVAL_SEQUENCE"));
			pr.setDeptHead(row.getString("DEPARTMENT_HEAD_APPROVAL"));
			pr.setFinanceHead(row.getString("FINANCE_HEAD_APPROVAL"));
			pr.setGmApproval(row.getString("GM_APPROVAL"));
			pr.setPrhdrId(row.getString("PR_HDR_ID"));
			pr.setProcurementHead(row.getString("PROCUREMENT_HEAD_APPROVAL"));
			pr.setRequestedBy(row.getString("REQUESTED_BY"));
			pr.setTransactionUI(row.getString("TRANSACTION_UI_ID"));
			pr.setReqName(row.getString("REQ_NAME"));
			pr.setReqDep(row.getString("REQ_DEP"));
			pr.setDeptHeadEmail(row.getString("DEPT_EMAIL"));
			pr.setFinanceHeadEmail(row.getString("FINANCE_EMAIL"));
			pr.setGmApprovalEmail(row.getString("GM_EMAIL"));
			pr.setProcurementHeadEmail(row.getString("PROCURE_EMAIL"));
			pr.setRequestedByEmail(row.getString("REQ_EMAIL"));
			pr.setLastUpdatedBy(row.getString("LAST_UPDATED_USER_ID"));
			pr.setAppGmId(row.getString("APPROVING_GM"));
			pr.setAppGmEmail(row.getString("APPROVING_GMAIL"));
		} catch (Exception e) {
			logger.error("PrHdrEmail RowMapper Exception------>" + e);
		}
		return pr;
	}
}
