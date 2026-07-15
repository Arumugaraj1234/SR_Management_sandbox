package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.QiCaEntity;

public class QiCaRowMapper implements RowMapper<QiCaEntity> {
    @Override
    public QiCaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        QiCaEntity qiCa = new QiCaEntity();
        qiCa.setQiCaDtlId(rs.getString("QI_CA_DTL_ID"));
        qiCa.setQiHdrId(rs.getString("QI_HDR_ID"));
        qiCa.setPmHdrId(rs.getString("PM_HDR_ID"));
        qiCa.setPoId(rs.getString("PO_ID"));
        qiCa.setPoCode(rs.getString("PO_CODE"));
        qiCa.setPoDtlId(rs.getString("PO_DTL_ID"));
        qiCa.setCaType(rs.getString("CA_TYPE"));
        qiCa.setQty(rs.getInt("QTY"));
        qiCa.setSequenceNo(rs.getString("SEQUENCE_NO"));
        qiCa.setSequenceStatus(rs.getString("SEQUENCE_STATUS"));
        qiCa.setIsApproved(rs.getString("IS_APPROVED"));
        qiCa.setTenantId(rs.getString("TENANT_ID"));
        qiCa.setReqReceivedDatetime(rs.getString("REQUEST_RECEIEVED_DATETIME"));
        qiCa.setReworkInternal(rs.getInt("REWORK_INTERNAL"));
        qiCa.setReworkVendor(rs.getInt("REWORK_VENDOR"));
        qiCa.setRejectedInternal(rs.getInt("REJECTED_INTERNAL"));
        qiCa.setRejectedExternal(rs.getInt("REJECTED_EXTERNAL"));
        qiCa.setCaQty(rs.getInt("CA_QTY"));
        qiCa.setCaInternal(rs.getInt("CA_INTERNAL"));
        qiCa.setCaVendor(rs.getInt("CA_VENDOR"));
        if (columnExists(rs, "INSPECTION_QTY")) {
            qiCa.setInspectionQty((rs.getString("INSPECTION_QTY")));
        }
        if (columnExists(rs, "REMARKS")) {
        	qiCa.setRemarks((rs.getString("REMARKS")));
		}
        if (columnExists(rs, "PRODUCT_CODE")) {
        	qiCa.setProductCode((rs.getString("PRODUCT_CODE")));
		}
        if (columnExists(rs, "INDENT_DTL_ID")) {
        	qiCa.setIndentDtlId((rs.getString("INDENT_DTL_ID")));
		}
        if (columnExists(rs, "PRODUCT_DESCRIPTION")) {
        	qiCa.setProductDescription((rs.getString("PRODUCT_DESCRIPTION")));
		}
        if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
        	qiCa.setDocumentStatusTypeDescription((rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION")));
		}
        if (columnExists(rs, "UOM_SHORT_DESCRIPTION")) {
        	qiCa.setUomShortDescription((rs.getString("UOM_SHORT_DESCRIPTION")));
		}
        
        if (columnExists(rs, "UOM")) {
        	qiCa.setUom((rs.getString("UOM")));
		}
        if (columnExists(rs, "INDENT_QTY")) {
        	qiCa.setIndentQty((rs.getString("INDENT_QTY")));
		}
        if (columnExists(rs, "VENDOR_NAME")) {
        	qiCa.setVendorName((rs.getString("VENDOR_NAME")));
		}
        if (columnExists(rs, "QI_ID")) {
        	qiCa.setQiId((rs.getString("QI_ID")));
		}
        if (columnExists(rs, "DM_ID")) {
        	qiCa.setDmId((rs.getString("DM_ID")));
		}
        if (columnExists(rs, "PROJECT_CODE")) {
        	qiCa.setProjCode((rs.getString("PROJECT_CODE")));
		}
        if (columnExists(rs, "PROJECT_NAME")) {
        	qiCa.setProjName((rs.getString("PROJECT_NAME")));
		}
        if (columnExists(rs, "APPROVED_BY")) {
        	qiCa.setCaApprovedBy((rs.getString("APPROVED_BY")));
		}
        if (columnExists(rs, "CA_APPROVED_DATE")) {
        	qiCa.setCaApprovedOn((rs.getString("CA_APPROVED_DATE")));
		}
        if (columnExists(rs, "REQUESTED_BY")) {
        	qiCa.setCaRaisedBy((rs.getString("REQUESTED_BY")));
		}
        if (columnExists(rs, "CA_DURATION")) {
        	qiCa.setDurationTime((rs.getString("CA_DURATION")));
		}
        return qiCa;
    }
    
    private boolean columnExists(ResultSet row, String columnName) throws SQLException {
		ResultSetMetaData metaData = row.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}
}