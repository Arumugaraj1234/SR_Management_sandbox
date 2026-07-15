package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.ScpDtlsEntity;

public class IndentGrpScpRowMapper implements RowMapper<ScpDtlsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGrpScpRowMapper.class);

	@Override
	public ScpDtlsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ScpDtlsEntity igs = new ScpDtlsEntity();
		try {
			igs.setCreatedBy(rs.getString("CREATED_BY"));
			igs.setCreatedDate(rs.getString("CREATED_DATETIME"));
			igs.setCustomerApproval(rs.getString("CUSTOMER_APPROVAL"));
			igs.setIgHdrId(rs.getString("IG_HDR_ID"));
			igs.setIgScpId(rs.getString("IG_SCS_ID"));
			igs.setIsApproved(rs.getString("IS_APPROVED"));
			igs.setJustification(rs.getString("JUSTIFICATION"));
			igs.setSeqNo(rs.getString("SEQUENCE_NO"));
			igs.setSeqStatus(rs.getString("SEQUENCE_STATUS"));
			igs.setTechnicalCompassion(rs.getString("TECHNICAL_COMPASSION"));
			igs.setTechnicalRecommendation(rs.getString("TECHNICAL_RECOMMENDATION"));
			igs.setVendorEvaluated(rs.getString("VENDOR_EVALUATED"));
			igs.setVendorQualified(rs.getString("VENDOR_QUALIFIED"));
			igs.setVendorShortListed(rs.getString("VENDOR_SHORTLISTED"));
			igs.setIndentId(rs.getString("INDENT_ID"));
			igs.setType(rs.getString("TYPE"));
		} catch (Exception ex) {
			logger.error("IndentGrpScmRowMapper error " + ex);
		}
		return igs;
	}

}


