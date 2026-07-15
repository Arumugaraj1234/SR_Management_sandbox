package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.GetIndentDtlProductCostEntity;

public class GetIndentDtlProductCostRowMapper implements RowMapper<GetIndentDtlProductCostEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetIndentDtlProductCostRowMapper.class);

	@Override
	public GetIndentDtlProductCostEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetIndentDtlProductCostEntity obj = new GetIndentDtlProductCostEntity();
		try {

		  obj.setPoCode(rs.getString("PO_CODE"));
		  obj.setPoDate(rs.getString("DATE"));
		  obj.setUnitRate(rs.getString("UNITE_RATE"));
		  obj.setVendorName(rs.getString("VENDOR_NAME"));
		  } catch (Exception ex) {
			logger.error("GetIndentDtlProductCostRowMapper  Method Exception" + ex);
		}
		return obj;
	}

}
