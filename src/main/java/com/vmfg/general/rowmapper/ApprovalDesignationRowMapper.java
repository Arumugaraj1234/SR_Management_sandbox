package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.ApprovalDesignationEntity;

public class ApprovalDesignationRowMapper implements RowMapper<ApprovalDesignationEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ApprovalDesignationRowMapper.class);

	@Override
	public ApprovalDesignationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ApprovalDesignationEntity result=new ApprovalDesignationEntity();
		try {
			result.setDnAppId(rs.getString("DN_APP_ID"));
			result.setDnDtlId(rs.getString("DN_DTL_ID"));
			result.setDocTypeCode(rs.getString("DOC_TYPE_CODE"));
			result.setRefId(rs.getString("REFERENCE_ID"));
			result.setRefCode(rs.getString("REFERENCE_CODE"));
			result.setProjectName(rs.getString("PROJECT_NAME"));
			result.setCustomerName(rs.getString("CUSTOMER_NAME"));
			result.setProjectCode(rs.getString("PROJECT_CODE"));
			result.setDocTypeDesc(rs.getString("dst.DOCUMENT_TYPE_DESCRIPTION"));
			result.setInsertedDate(rs.getString("DATE"));
			result.setProjectId(rs.getString("PM_HDR_ID"));
			result.setPmId(rs.getString("PM_ID"));
			result.setIsInternal(rs.getString("IS_INTERNAL"));
			result.setEnquiryCode(rs.getString("ENQUIRY_CODE"));
//			result.setPreviousSeq(rs.getString("PRE_SEQ"));
//			result.setIsEditing(rs.getString("IS_EDIT"));
//			result.setTargetCost(rs.getString("TARGET_VALUE"));
//			result.setActualCost(rs.getString("ACTUAL_VALUE"));
			result.setEnquiryId(rs.getString("ENQUIRY_ID"));
			
		}catch(Exception ex) {
			logger.error("ApprovalDesignationRowMapper error "+ex);
		}
		return result;
	}

}
