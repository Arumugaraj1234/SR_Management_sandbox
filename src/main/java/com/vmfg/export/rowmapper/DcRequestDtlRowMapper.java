package com.vmfg.export.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.export.entity.DcRequestDtlEntity;

public class DcRequestDtlRowMapper implements RowMapper<DcRequestDtlEntity> {
	
	private static final Logger logger = LoggerFactory.getLogger(DcRequestDtlRowMapper.class);
	@Override
	public DcRequestDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DcRequestDtlEntity ent = new DcRequestDtlEntity();
		try {
			ent.setDcDtlId(rs.getString("DC_DTL_ID"));
			ent.setDcrId(rs.getString("DCR_ID"));
			ent.setProductId(rs.getString("PRODUCT_ID"));
			ent.setDescofGoods(rs.getString("DESC_OF_GOODS"));
			ent.setQty(rs.getString("QTY"));
			ent.setClosedQty(rs.getString("CLOSED_QTY"));
			ent.setPendingQty(rs.getString("PENDING_QTY"));
			ent.setMrHdrId(rs.getString("MR_HDR_ID"));
			ent.setProductCode(rs.getString("PRODUCT_CODE"));
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("DcRequestDtlRowMapper method error"+e);
		}
		return ent;
	}

}
