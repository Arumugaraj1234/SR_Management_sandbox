package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.RetieveQCInspectionHdrEntity;

public class RetieveQCInspectionHdrRowMapper implements RowMapper<RetieveQCInspectionHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(RetieveQCInspectionHdrRowMapper.class);

	@Override
	public RetieveQCInspectionHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		RetieveQCInspectionHdrEntity result = new RetieveQCInspectionHdrEntity();
		try {

			if (columnExists(rs, "VENDOR_NAME")) {
				result.setVendorName((rs.getString("VENDOR_NAME")));
			}
			if (columnExists(rs, "LOCATION_REFERENCENAME")) {
				result.setLocationRef((rs.getString("LOCATION_REFERENCENAME")));
			}
			if (columnExists(rs, "DESCRIPTION")) {
				result.setProductName((rs.getString("DESCRIPTION")));
			}
			if (columnExists(rs, "INDENT_DTL_ID")) {
				result.setIndentDtlId((rs.getString("INDENT_DTL_ID")));
			}
			if (columnExists(rs, "QTY_INSPECTED")) {
				result.setInspectQty((rs.getString("QTY_INSPECTED"))!= null ? rs.getString("QTY_INSPECTED") : "0");
			}
			if (columnExists(rs, "QTY_TO_BE_INSPECTED")) {
				result.setQtyToBeInspected((rs.getString("QTY_TO_BE_INSPECTED"))!= null ? rs.getString("QTY_TO_BE_INSPECTED") : "0");
			}
			if (columnExists(rs, "TENANT_ID")) {
				result.setTenantId((rs.getString("TENANT_ID")));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				result.setProductCode((rs.getString("PRODUCT_CODE")));
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
			if (columnExists(rs, "REWORK_CNT")) {
				result.setReworkCount((rs.getString("REWORK_CNT"))!= null ? rs.getString("REWORK_CNT") : "0");
			}
			if (columnExists(rs, "INSPECTION_REQUESTED_DATE")) {
				result.setInsReqDate((rs.getString("INSPECTION_REQUESTED_DATE")));
			}
			if (columnExists(rs, "VENDOR_CODE")) {
				result.setVendorCode((rs.getString("VENDOR_CODE")));
			}
			if (columnExists(rs, "DRAWING_NO")) {
				result.setDrawingNo(rs.getString("DRAWING_NO"));
	        }
			if (columnExists(rs, "INSPECTED_ON")) {
				result.setInspectedDate(rs.getString("INSPECTED_ON"));
			}
			if (columnExists(rs, "CONFIG_NAME")) {
				result.setConfigName(rs.getString("CONFIG_NAME"));
			}
			if (columnExists(rs, "QUALITY_REF_NO")) {
				result.setQualityRefNo(rs.getString("QUALITY_REF_NO"));
			}
			if (columnExists(rs, "INSPECTION_QTY")) {
				result.setQtyInspectionCompleted(rs.getString("INSPECTION_QTY"));
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
