package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.request.SalesBudgetSheetExntDtlEntity;

public class SalesBudgetSheetExntDtlRowMapper implements RowMapper<SalesBudgetSheetExntDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SalesBudgetSheetExntDtlRowMapper.class);

	@Override
	public SalesBudgetSheetExntDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		SalesBudgetSheetExntDtlEntity tm = new SalesBudgetSheetExntDtlEntity();
		try {
			tm.setAllocatedQty(row.getString("ALLOCATED_QTY"));
			tm.setAllocatedvalue(row.getString("ALLOCATED_VALUE"));
			tm.setElementDtl(row.getString("ELEMENT_DTL"));
			tm.setElementHdr(row.getString("ELEMENT_HDR"));
			tm.setMake(row.getString("MAKE"));
			tm.setQty(row.getString("QTY"));
			tm.setSbDtld(row.getString("SB_DTL_ID"));
			tm.setSbExtnId(row.getString("SB_EXTN_ID"));
			tm.setSpecification(row.getString("SPECIFICATION"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTotalValue(row.getString("TOTAL_VALUE"));
			tm.setValue(row.getString("VALUE"));
			tm.setCritical(row.getString("CRITICAL"));
			tm.setTimelineInWeeks(row.getString("TIMELINE_IN_WEEKS"));
			tm.setContingency(row.getString("CONTIGENCY"));
			tm.setSubtotal(row.getString("SUB_TOTAL_VALUE"));

		} catch (Exception e) {
			logger.error("SalesBudgetSheetExntDtlRowMapper Exception--->" + e);
		}
		return tm;
	}

}
