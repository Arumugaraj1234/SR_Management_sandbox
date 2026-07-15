package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.response.ReportProjectTrackerResponse;

public class PMWidgetRowMapper implements RowMapper<ReportProjectTrackerResponse> {
	private static final Logger logger = LoggerFactory.getLogger(PMWidgetRowMapper.class);

	@Override
	public ReportProjectTrackerResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReportProjectTrackerResponse lst = new ReportProjectTrackerResponse();
		try {

			lst.setBalanceValue(rs.getString("BALANCE_VALUE"));
			lst.setMaterialTransferValue(rs.getString("MATERIAL_TRANSFER_VALUE"));
			lst.setPmBudgetValue(rs.getString("PM_BUDGET_VALUE"));
			lst.setSaleContribution(rs.getString("SALE_CONTRIBUTION"));
			lst.setSaleValue(rs.getString("SALE_VALUE"));
			lst.setScmPoValue(rs.getString("SCM_PO_VALUE"));
			lst.setTargetValue(rs.getString("PM_TARGET_VALUE"));
			lst.setProjectCode(rs.getString("PROJECT_CODE"));
			lst.setEmpCostValue(rs.getString("EMPLOYEE_COST"));
		} catch (Exception ex) {
			logger.error("PMWidgetRowMapper Method Exception" + ex);
		}
		return lst;
	}
}
