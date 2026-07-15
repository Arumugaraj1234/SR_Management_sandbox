package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class FourMEmailApprovalEntityRowMapper implements RowMapper<FourMEmailApprovalEntity>{
	private static final Logger logger = LoggerFactory.getLogger(FourMEmailApprovalEntityRowMapper.class);
	@Override
	public FourMEmailApprovalEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		FourMEmailApprovalEntity tm = new FourMEmailApprovalEntity();
		try {
			tm.setProdSuprApproved(row.getString("PROD_SUPERVISOR_APPROVAL_BY"));
			tm.setProdEnggHeadApproved(row.getString("PROD_ENGG_HEAD_APPROVAL_BY"));
			tm.setQualityEngg(row.getString("QUALITY_ENGG_APPROVAL_BY"));
			tm.setCkpsHead(row.getString("CKPS_HEAD_APPROVAL_BY"));
			tm.setQualityHead(row.getString("QUALITY_HEAD_APPROVAL_BY"));
			tm.setProdSuprApprovedName(row.getString("PROD_SUPERVISOR_APPROVAL_BYname"));
			tm.setProdEnggHeadApprovedName(row.getString("PROD_ENGG_HEAD_APPROVAL_BYname"));
			tm.setQualityEnggName(row.getString("QUALITY_ENGG_APPROVAL_BYname"));
			tm.setCkpsHeadName(row.getString("CKPS_HEAD_APPROVAL_BYname"));
			tm.setQualityHeadName(row.getString("QUALITY_HEAD_APPROVAL_BYname"));
			
			} catch (Exception e) {
			logger.error("FourMEmailApprovalEntityRowMapper Exception--->"+e);
		}
		return tm;
	}

}
