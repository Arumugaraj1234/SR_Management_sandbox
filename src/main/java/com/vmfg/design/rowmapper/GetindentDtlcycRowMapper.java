package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.GetindentDtlcycEntity;

public class GetindentDtlcycRowMapper implements RowMapper<GetindentDtlcycEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetindentDtlcycRowMapper.class);

	@Override
	public GetindentDtlcycEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetindentDtlcycEntity obj = new GetindentDtlcycEntity();
		try {

		  obj.setIndentDtlQty(rs.getString("QTY"));
		  obj.setProductCode(rs.getString("PRODUCT_CODE"));
		  obj.setProductDesc(rs.getString("DESCRIPTION"));
		  obj.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
		  obj.setSno(rs.getInt("SERIAL_NO"));
		} catch (Exception ex) {
			logger.error("GetindentDtlcycRowMapper  Method Exception" + ex);
		}
		return obj;
	}


}
