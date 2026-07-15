package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;

public class RetrieveQualitInspectionRowMapper implements RowMapper<RetrieveQualitInspectionEntity> {
	private static final Logger logger = LoggerFactory.getLogger(RetrieveQualitInspectionRowMapper.class);

	@Override
	public RetrieveQualitInspectionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		RetrieveQualitInspectionEntity result = new RetrieveQualitInspectionEntity();
		try {

			if (columnExists(rs, "PO_CODE")) {
				result.setPoCode((rs.getString("PO_CODE")));
			}
			if (columnExists(rs, "NR_FLAG")) {
				result.setNrFlag((rs.getString("NR_FLAG")));
			}
			if (columnExists(rs, "QI_CODE")) {
				result.setQiCode(rs.getString("QI_CODE"));
			}
			if (columnExists(rs, "FINANCIAL_YEAR_MST_ID")) {
				result.setFinancialYearMstId(rs.getString("FINANCIAL_YEAR_MST_ID"));
			}
			if (columnExists(rs, "TRANSACTION_NO")) {
				result.setTransactionNumber(rs.getString("TRANSACTION_NO"));
			}
			if (columnExists(rs, "INDENT_DTL_ID")) {
				result.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			}
			if (columnExists(rs, "PM_HDR_ID")) {
				result.setPmHdrId(rs.getString("PM_HDR_ID"));
			}
			if (columnExists(rs, "QI_ID")) {
				result.setQiId(rs.getString("QI_ID"));
			}
			if (columnExists(rs, "PO_ID")) {
				result.setPoId(rs.getString("PO_ID"));
			}
			if (columnExists(rs, "QTY_INSPECTED")) {
				result.setInspectQty((rs.getString("QTY_INSPECTED"))!= null ? rs.getString("QTY_INSPECTED") : "0");
			}
			if (columnExists(rs, "QTY_TO_BE_INSPECTED")) {
				result.setQtyToBeInspected((rs.getString("QTY_TO_BE_INSPECTED"))!= null ? rs.getString("QTY_TO_BE_INSPECTED") : "0");
			}
			if (columnExists(rs, "PO_DTL_ID")) {
				result.setPoDtlId(rs.getString("PO_DTL_ID"));
			}
			if (columnExists(rs, "INSPECTION_REQUESTED_DATE")) {
				result.setInspectionReqDate(rs.getString("INSPECTION_REQUESTED_DATE"));
			}
			if (columnExists(rs, "INSPECTION_REQUESTED_BY")) {
				result.setInspectionReqBy(rs.getString("INSPECTION_REQUESTED_BY"));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				result.setProductCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "DESCRIPTION")) {
				result.setProductDesc(rs.getString("DESCRIPTION"));
			}
			if (columnExists(rs, "UOM_LONG_DESCRIPTION")) {
				result.setUom(rs.getString("UOM_LONG_DESCRIPTION"));
			}
			if (columnExists(rs, "OK_QTY")) {
				result.setOkCount((rs.getString("OK_QTY"))!= null ? rs.getString("OK_QTY") : "0");
			}
			if (columnExists(rs, "NOK_CNT")) {
				result.setRejectedCount((rs.getString("NOK_CNT"))!= null ? rs.getString("NOK_CNT") : "0");
			}
			if (columnExists(rs, "CONDITIONAL_APPROVED_CNT")) {
				result.setConditionalCnt((rs.getString("CONDITIONAL_APPROVED_CNT"))!= null ? rs.getString("CONDITIONAL_APPROVED_CNT") : "0");
			}
			if (columnExists(rs, "CONDITIONAL_NOT_APPROVED_CNT")) {
				result.setConditionalNoCnt((rs.getString("CONDITIONAL_NOT_APPROVED_CNT"))!= null ? rs.getString("CONDITIONAL_NOT_APPROVED_CNT") : "0");
			}
			if (columnExists(rs, "REWORK_CNT")) {
				result.setReworkCount((rs.getString("REWORK_CNT"))!= null ? rs.getString("REWORK_CNT") : "0");
			}
			if (columnExists(rs, "REJECT_CNT")) {
				result.setRejectedCount((rs.getString("REJECT_CNT"))!= null ? rs.getString("REJECT_CNT") : "0");
			}
			if (columnExists(rs, "INSPECTED_ON")) {
				result.setInspectedDate((rs.getString("INSPECTED_ON")));
			}
			
			if(columnExists(rs, "QI_HDR_ID")) {
				result.setQiHdrId((rs.getString("QI_HDR_ID")));
			}
			if(columnExists(rs, "QUALITY_RATING")) {
				result.setQtyRating((rs.getString("QUALITY_RATING")));
			}
			if(columnExists(rs, "REQUEST_FROM")) {
				result.setQcRequestedFrom((rs.getString("REQUEST_FROM")));
			}
			if(columnExists(rs, "INSPECTED_BY")) {
				result.setInspectedBy((rs.getString("INSPECTED_BY")));
			}
			if(columnExists(rs, "IS_REWORK")) {
				result.setIsRework((rs.getInt("IS_REWORK")));
			}
			if(columnExists(rs, "VENDOR_CODE")) {
				result.setVendorCode((rs.getString("VENDOR_CODE")));
			}
			if(columnExists(rs, "CANCEL_FLAG")) {
				result.setCancelFlag((rs.getString("CANCEL_FLAG")));
			}
			if(columnExists(rs, "REQUEST_FROM")) {
				result.setReqFrom((rs.getString("REQUEST_FROM")));
			}
			if(columnExists(rs, "VENDOR_NAME")) {
				result.setVendorName((rs.getString("VENDOR_NAME")));
			}
			if(columnExists(rs, "STATUS")) {
				result.setInspectionStatus((rs.getString("STATUS")));
			}
			if(columnExists(rs, "ISFLAG")) {
				result.setInspectFlag((rs.getString("ISFLAG")));
			}
		} catch (Exception ex) {
			logger.error("RetrieveQualitInspectionRowMapper error " + ex);
		}

		return result;
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
