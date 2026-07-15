package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ChangeRequestHdrEntity;

public class ChangeRequestHdrRowMapper implements RowMapper<ChangeRequestHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ChangeRequestHdrRowMapper.class);

	@Override
	public ChangeRequestHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ChangeRequestHdrEntity res = new ChangeRequestHdrEntity();
		try {
			res.setApprovedOn(rs.getString("APPROVED_ON"));
			res.setCreatedBy(rs.getString("CREATED_BY"));
			res.setCreatedBydesc(rs.getString("CREATED_BY_DESC"));
			res.setCreatedOn(rs.getString("CREATED_ON"));
			res.setCrId(rs.getString("CR_ID"));
			res.setCrNO(rs.getString("TRANSACTION_NO"));
			res.setDeHdrId(rs.getString("DE_HDR_ID"));
			res.setInitiatedBy(rs.getString("INITIATED_BY"));
			res.setInitiatedByDesc(rs.getString("INITIATED_BY_DESC"));
			res.setIsApproved(rs.getString("IS_APPROVED"));
			res.setIsCompleted(rs.getString("IS_COMPLETED"));
			res.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
			res.setLastUdpdateByDesc(rs.getString("LAST_UPDATED_BY_DESC"));
			res.setLastUpdatedOn(rs.getString("LAST_UPDATE_ON"));
			res.setNextApprovingDesig(rs.getString("NEXT_APPROVING_DESIG"));
		//	res.setNextApprovingDesigDesc(rs.getString("PK_ID"));
			res.setPkdesc(rs.getString("PK_DESC"));
			res.setPkId(rs.getString("PK_ID"));
			res.setPmHdrCode(rs.getString("PROJECT_CODE"));
			res.setPmHdrId(rs.getString("PM_HDR_ID"));
			res.setPmHdrName(rs.getString("PROJECT_NAME"));
			res.setProductCode(rs.getString("PK_ID"));
			res.setProductName(rs.getString("PK_ID"));
			res.setPskDesc(rs.getString("PSK_DESC"));
			res.setPskId(rs.getString("PSK_ID"));
			res.setRequestDetails(rs.getString("REQUEST_DETAILS"));
			res.setTenantId(rs.getString("TENANT_ID"));
			res.setTransactionStatus(rs.getString("TRANSACTION_STATUS"));
			res.setTransactionStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			res.setTransactionStatusSeq(rs.getString("TRANSACTION_STATUS_SEQ"));
			res.setUpdatedDrawingNo(rs.getString("UPDATED_DRAWING_NO"));
			res.setUpdatedDrawingRevNo(rs.getString("UPDATED_DRAWING_REV_NO"));
			res.setCrDate(rs.getString("CR_DATE"));
			res.setProductCode(rs.getString("PRODUCT_CODE"));
			res.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
		} catch (Exception ex) {
			logger.error("ChangeRequestHdrRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
