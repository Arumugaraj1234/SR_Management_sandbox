package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.SalesBudgetExtnDtlEntity;

public class SalesBudgetExtnDtlRowMapper  implements RowMapper<SalesBudgetExtnDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SalesBudgetExtnDtlRowMapper.class);

	@Override
	public SalesBudgetExtnDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		SalesBudgetExtnDtlEntity ph = new SalesBudgetExtnDtlEntity();
		try {
		
		//	ph.setElementhdr(row.getString("ELEMENT_DESC"));
			ph.setTotalQty(row.getString("ALLOCATED_QTY"));
			String totalValue=row.getString("ALLOCATED_VALUE");
			ph.setTotalValue(totalValue != null ? totalValue : "0");
	//		ph.setDskId(row.getString("DSK_ID"));
			ph.setElementDtl(row.getString("ELEMENT_DTL"));
			ph.setElementHdr(row.getString("ELEMENT_HDR"));
			ph.setKeyCategory(row.getString("SBC_CODE"));
			ph.setKeyCategotyDesc(row.getString("SBC_DESC"));
		//	ph.setPkseId(row.getString("PKSE_ID"));
			ph.setSbDtlId(row.getString("SB_DTL_ID"));
			ph.setSbExtnId(row.getString("SB_EXTN_ID"));
			ph.setTenantId(row.getString("TENANT_ID"));
	//		ph.setCustomerName(row.getString("CUSTOMER_NAME"));
	//		ph.setPskDesc(row.getString("PSK_DESC"));
			ph.setSpecification(row.getString("SPECIFICATION"));
			ph.setMake(row.getString("MAKE"));
			ph.setPerPartVal(row.getString("PER_PART_VAUE"));
			ph.setTotalAmount(row.getString("TOTAL_AMOUNT"));
			ph.setUtilizedAmount(row.getString("UTILIZED_AMOUNT"));
			ph.setAvailableAmount(row.getString("AVAILABLE_AMOUNT"));
		} catch (Exception e) {
			logger.error("SalesBudgetExtnDtlRowMapper Exception--->" + e);
		}
		return ph;
	}

}


