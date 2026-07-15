package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.GetBudgetDtlByIndentEntity;

public class GetBudgetDtlByIndentRowMapper implements RowMapper<GetBudgetDtlByIndentEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetBudgetDtlByIndentRowMapper.class);

	@Override
	public GetBudgetDtlByIndentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetBudgetDtlByIndentEntity obj = new GetBudgetDtlByIndentEntity();
		try {

			obj.setPkseId(rs.getString("PKSE_ID"));
	        obj.setPkaId(rs.getString("PKA_ID"));
	        obj.setAllocatedQty(rs.getString("ALLOCATED_QTY"));
	        obj.setAllocatedValue(rs.getString("ALLOCATED_VALUE"));
	        obj.setBalanceQty(rs.getString("BALANCE_QTY"));
	        obj.setBalanceValue(rs.getString("BALANCE_VALUE"));
	        obj.setElementHdr(rs.getString("ELEMENT_HDR"));
	        obj.setElementDtl(rs.getString("ELEMENT_DTL"));
	        obj.setUnitPrice(rs.getString("UNIT_PRICE"));
	        obj.setRequiredQty(rs.getString("REQUIRED_QTY"));
	        obj.setRequiredValue(rs.getString("REQUIRED_VALUE"));
	        obj.setSbExtnId(rs.getString("SB_EXTN_ID"));
	        obj.setElementSpec(rs.getString("SPECIFICATION"));
	        obj.setElementMake(rs.getString("MAKE"));
		} catch (Exception ex) {
			logger.error("GetBudgetDtlByIndentRowMapper  Method Exception" + ex);
		}
		return obj;
	}


}
