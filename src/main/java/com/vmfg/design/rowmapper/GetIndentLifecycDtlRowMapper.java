package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.GetIndentLifecycDtlEntity;

public class GetIndentLifecycDtlRowMapper implements RowMapper<GetIndentLifecycDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetIndentLifecycDtlRowMapper.class);

	@Override
	public GetIndentLifecycDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetIndentLifecycDtlEntity res = new GetIndentLifecycDtlEntity();
		try {
			res.setIndentId(rs.getString("INDENT_ID"));
			res.setCreatedBy(rs.getString("CREATED_BY"));
			res.setCreatedOn(rs.getString("CREATED_DATE"));
			res.setIndentCode(rs.getString("INDENT_CODE"));
			res.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
			res.setKeyAreaDesc(rs.getString("PK_DESC"));
			res.setProjectName(rs.getString("PROJECT_NAME"));
			res.setSbcDesc(rs.getString("SBC_DESC"));
			res.setStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			res.setSubKeyAreaDesc(rs.getString("PSK_DESC"));
			res.setExpectedDeliveryDate(rs.getString("EXPECTED_DELIVERY_DATE"));
			//res.setIndentClosedDate(rs.getString("CLOSED_DATE"));
			if (columnExists(rs, "CLOSED_DATE")) {
				res.setIndentClosedDate(rs.getString("CLOSED_DATE"));
			}
			if (columnExists(rs, "PRODUCT_ID")) {
				res.setProductId(rs.getString("PRODUCT_ID"));
			}
			if (columnExists(rs, "PKSA_ID")) {
				res.setKeyAreaId(rs.getString("PKSA_ID"));
			}
			if (columnExists(rs, "TARGET_VALUE")) {
				res.setTargetCost(rs.getString("TARGET_VALUE"));
			}
			if (columnExists(rs, "SEQUENCE_N0")) {
				res.setStatusSeq(rs.getString("SEQUENCE_N0"));
			}
			if (columnExists(rs, "IG_HDR_ID")) {
				res.setIgHdrId(rs.getString("IG_HDR_ID"));
			}
			if (columnExists(rs, "IS_INVENTORY")) {
				res.setIsInventroy(rs.getString("IS_INVENTORY"));
			}
			if (columnExists(rs, "TYPE_CREATED_TIME")) {
				res.setTypeCreatedTime(rs.getString("TYPE_CREATED_TIME"));
			}
//			res.setIsFlag(rs.getInt("COUNT"));
			res.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			res.setProductCode(rs.getString("PRODUCT_CODE"));
			res.setProductDesc(rs.getString("DESCRIPTION"));
			res.setIndentQty(rs.getString("INDENT_QTY"));
//			res.setPoCheck(rs.getString("PO_CHECK"));
//			res.setMiCheck(rs.getString("MI_CHECK"));
//			res.setGrnCheck(rs.getString("GRN_CHECK"));
//			res.setInspReqCheck(rs.getString("INSP_CHECK"));
//			res.setGroupCheck(rs.getString("GROUP_CHECK"));
			res.setRevisionNo(rs.getString("REVISION_NO"));
			res.setRevisionDate(rs.getString("REVISION_DATE"));
			if(columnExists(rs,"PO_CHECK")) {
			    res.setPoCheck(rs.getString("PO_CHECK"));
			}
			if(columnExists(rs,"MI_CHECK")) {
			    res.setMiCheck(rs.getString("MI_CHECK"));
			}
			if(columnExists(rs,"GRN_CHECK")) {
			    res.setGrnCheck(rs.getString("GRN_CHECK"));
			}
			if(columnExists(rs,"GROUP_CHECK")) {
			    res.setGroupCheck(rs.getString("GROUP_CHECK"));
			}
			if(columnExists(rs,"INSP_CHECK")) {
			    res.setInspReqCheck(rs.getString("INSP_CHECK"));
			}
			if(columnExists(rs, "PO_COMPLETED_DATETIME")) {
				res.setPoDateTime(rs.getString("PO_COMPLETED_DATETIME"));	
			}
			if(columnExists(rs, "PJS_COMPLETED_DATETIME")) {
				res.setPjsDateTime(rs.getString("PJS_COMPLETED_DATETIME"));	
			}
			if(columnExists(rs, "MI_COMPLETED_DATETIME")) {
				res.setMiDateTime(rs.getString("MI_COMPLETED_DATETIME"));	
			}
			if(columnExists(rs, "PRA_REQUESTED_DATETIME")) {
				res.setPraReqDateTime(rs.getString("PRA_REQUESTED_DATETIME"));	
			}
			if(columnExists(rs, "PRA_COMPLETED_DATETIME")) {
				res.setPraDateTime(rs.getString("PRA_COMPLETED_DATETIME"));	
			}
			if(columnExists(rs, "GRN_COMPLETED_DATETIME")) {
				res.setGrnDateTime(rs.getString("GRN_COMPLETED_DATETIME"));	
			}
			if(columnExists(rs, "QI_REQUESTED_DATETIME")) {
				res.setInspReqDateTime(rs.getString("QI_REQUESTED_DATETIME"));	
			}
			if(columnExists(rs, "QI_COMPLETED_DATETIME")) {
				res.setInspDateTime(rs.getString("QI_COMPLETED_DATETIME"));	
			}
			if (columnExists(rs, "INSP_CHECK_COMPLETE")) {
				res.setInspCheck(rs.getString("INSP_CHECK_COMPLETE"));
			}
			if (columnExists(rs, "PRA_CHECK")) {
				res.setPraReqCheck(rs.getString("PRA_CHECK"));
			}
			if (columnExists(rs, "PRA_CHECK_COMPLETE")) {
				res.setPraCheck(rs.getString("PRA_CHECK_COMPLETE"));
			}
			if (columnExists(rs, "PO_CHECK")) {
				res.setPoCheck(rs.getString("PO_CHECK"));
			}
			if (columnExists(rs, "VENDOR_NAME")) {
				res.setVendorName(rs.getString("VENDOR_NAME"));
			}
			if (columnExists(rs, "DELIVERY_DATE")) {
				res.setDeliveryDate(rs.getString("DELIVERY_DATE"));
			}
		} catch (Exception ex) {
			logger.error("IndentHdrDtlsRowMapper  Method Exception" + ex);
		}
		return res;
	}


	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}

}
