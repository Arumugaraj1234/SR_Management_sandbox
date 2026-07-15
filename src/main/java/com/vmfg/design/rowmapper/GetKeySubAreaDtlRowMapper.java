package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.GetKeySubAreaDtlEntity;

public class GetKeySubAreaDtlRowMapper  implements RowMapper<GetKeySubAreaDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetKeySubAreaDtlRowMapper.class);

	@Override
	public GetKeySubAreaDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetKeySubAreaDtlEntity res = new GetKeySubAreaDtlEntity();
		try {
		
			res.setPkaId(rs.getString("PKA_ID"));
			res.setPkDesc(rs.getString("PK_DESC"));
			res.setPksaId(rs.getString("PKSA_ID"));
			res.setPmHdrId(rs.getString("PM_HDR_ID"));
			res.setPskDesc(rs.getString("PSK_DESC"));
			res.setPskId(rs.getString("PSK_ID"));
			res.setTenantId(rs.getString("TENANT_ID"));
			res.setPkCode(rs.getString("PK_CODE"));
			res.setPskCode(rs.getString("PSK_CODE"));
		} catch (Exception ex) {
			logger.error("GetKeySubAreaDtlRowMapper  Method Exception" + ex);
		}
		return res;
	}


}
