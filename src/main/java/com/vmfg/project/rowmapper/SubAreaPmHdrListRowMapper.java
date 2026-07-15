package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.SubAreaPmHdrListEntity;

public class SubAreaPmHdrListRowMapper implements RowMapper<SubAreaPmHdrListEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SubAreaPmHdrListRowMapper.class);

	@Override
	public SubAreaPmHdrListEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		SubAreaPmHdrListEntity ph = new SubAreaPmHdrListEntity();
		try {
		
		//	ph.setElementhdr(row.getString("ELEMENT_DESC"));
			ph.setAllocatedQty(row.getString("ALLOCATED_QTY"));
			ph.setAllovatedValue(row.getString("ALLOCATED_VALUE"));
			ph.setPkaId(row.getString("PKA_ID"));
			ph.setElementDtl(row.getString("ELEMENT_DTL"));
			ph.setElementHdr(row.getString("ELEMENT_HDR"));
			ph.setKeyCategory(row.getString("SBC_CODE"));
			ph.setKeyCategotyDesc(row.getString("SBC_DESC"));
			ph.setPkseId(row.getString("PKSE_ID"));
			ph.setSbDtlId(row.getString("SB_DTL_ID"));
			ph.setSbExtnId(row.getString("SB_EXTN_ID"));
			ph.setTenantId(row.getString("TENANT_ID"));
			ph.setCustomerName(row.getString("CUSTOMER_NAME"));
			ph.setPskDesc(row.getString("PK_DESC"));
		} catch (Exception e) {
			logger.error("SubAreaPmHdrListRowMapper Exception--->" + e);
		}
		return ph;
	}


}
