package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.QualityInspectionHdrEntity;

public class QualityInspectionHdrRowMapper implements RowMapper<QualityInspectionHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(QualityInspectionHdrRowMapper.class);

	@Override
	public QualityInspectionHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		QualityInspectionHdrEntity qi = new QualityInspectionHdrEntity();
		try {
			if (columnExists(rs, "QI_HDR_ID")) {
			    qi.setQiHdrId(rs.getString("QI_HDR_ID"));
			}
			if (columnExists(rs, "QI_ID")) {
			    qi.setQiId(rs.getString("QI_ID"));
			}
			if (columnExists(rs, "PM_HDR_ID")) {
			    qi.setPmHdrId(rs.getString("PM_HDR_ID"));
			}
			if (columnExists(rs, "QUALITY_REF_NO")) {
			    qi.setQualityRefNo(rs.getString("QUALITY_REF_NO"));
			}
			if (columnExists(rs, "INSPECTION_QTY")) {
			    qi.setInspectionQty(rs.getString("INSPECTION_QTY"));
			}
			if (columnExists(rs, "OK_QTY")) {
			    qi.setTotalOkQty(rs.getString("OK_QTY"));
			}
			if (columnExists(rs, "NOK_QTY")) {
			    qi.setTotalNokQty(rs.getString("NOK_QTY"));
			}
//			if (columnExists(rs, "REWORK_QTY")) {
//			    qi.setTotalRejectQty(rs.getString("REWORK_QTY"));
//			}
			if (columnExists(rs, "CA_INTERNAL")) {
			    qi.setCaInternal(rs.getString("CA_INTERNAL"));
			}
			if (columnExists(rs, "CA_VENDOR")) {
			    qi.setCaVendor(rs.getString("CA_VENDOR"));
			}
			if (columnExists(rs, "REVISION_DATE")) {
			    qi.setRevisionDate(rs.getString("REVISION_DATE"));
			}
			if (columnExists(rs, "INSPECTION_TYPE")) {
			    qi.setInspectionType(rs.getString("INSPECTION_TYPE"));
			}
			if (columnExists(rs, "REWORK_INTERNAL")) {
			    qi.setReworkInternal(rs.getString("REWORK_INTERNAL"));
			}
			if (columnExists(rs, "REWORK_VENDOR")) {
			    qi.setReworkVendor(rs.getString("REWORK_VENDOR"));
			}
			if (columnExists(rs, "REJECTED_INTERNAL")) {
			    qi.setRejectedInternal(rs.getString("REJECTED_INTERNAL"));
			}
			if (columnExists(rs, "REJECTED_EXTERNAL")) {
			    qi.setRejectedExternal(rs.getString("REJECTED_EXTERNAL"));
			}
			if (columnExists(rs, "INSPECTED_BY")) {
			    qi.setInspectedBy(rs.getString("INSPECTED_BY"));
			}
			if (columnExists(rs, "INSPECTED_ON")) {
			    qi.setInspectedOn(rs.getString("INSPECTED_ON"));
			}
			if (columnExists(rs, "SEQUENCE_NO")) {
			    qi.setSequenceNo(rs.getString("SEQUENCE_NO"));
			}
			if (columnExists(rs, "SEQUENCE_STATUS")) {
			    qi.setSequenceStatus(rs.getString("SEQUENCE_STATUS"));
			}
			if (columnExists(rs, "IS_COMPLETED")) {
			    qi.setIsCompleted(rs.getString("IS_COMPLETED"));
			}
			if (columnExists(rs, "NR_FLAG")) {
			    qi.setNrFlag(rs.getString("NR_FLAG"));
			}
			if (columnExists(rs, "TENANT_ID")) {
			    qi.setTenantId(rs.getString("TENANT_ID"));
			}
			if (columnExists(rs, "QIC_HDR_ID")) {
			    qi.setQicHdrId(rs.getString("QIC_HDR_ID"));
			}
			if (columnExists(rs, "QIC_NAME")) {
			    qi.setQicName(rs.getString("QIC_NAME"));
			}
			if (columnExists(rs, "QIC_CREATED_ON")) {
			    qi.setQicCreatedOn(rs.getString("QIC_CREATED_ON"));
			}
			if (columnExists(rs, "QIC_CREATED_BY")) {
			    qi.setQicCreatedBy(rs.getString("QIC_CREATED_BY"));
			}
			if (columnExists(rs, "IS_ACTIVE")) {
			    qi.setIsActive(rs.getString("IS_ACTIVE"));
			}
	        if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				qi.setStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			}
	        if (columnExists(rs, "QIC_HDR_ID")) {
	            qi.setQicHdrId(rs.getString("QIC_HDR_ID"));
	        }
	        if (columnExists(rs, "QIC_NAME")) {
	            qi.setQicName(rs.getString("QIC_NAME"));
	        }
	        if (columnExists(rs, "QIC_CREATED_ON")) {
	            qi.setQicCreatedOn(rs.getString("QIC_CREATED_ON"));
	        }
	        if (columnExists(rs, "QIC_CREATED_BY")) {
	            qi.setQicCreatedBy(rs.getString("QIC_CREATED_BY"));
	        }
	        if (columnExists(rs, "IS_ACTIVE")) {
	            qi.setIsActive(rs.getString("IS_ACTIVE"));
	        }
	        if (columnExists(rs, "QUALITY_RATING")) {
	            qi.setQualityRating(rs.getString("QUALITY_RATING"));
	        }
	        if (columnExists(rs, "CONFIG_NAME")) {
	            qi.setConfigName(rs.getString("CONFIG_NAME"));
	        }
	        if (columnExists(rs, "VENDOR_CODE")) {
	            qi.setVendorCode(rs.getString("VENDOR_CODE"));
	        }
	        if (columnExists(rs, "DRAWING_NO")) {
	            qi.setDrawingNo(rs.getString("DRAWING_NO"));
	        }
	        if (columnExists(rs, "OK_QTY_REMARKS")) {
	            qi.setOkRemarks(rs.getString("OK_QTY_REMARKS"));
	        }
	        if (columnExists(rs, "NOK_QTY_REMARKS")) {
	            qi.setNokRemarks(rs.getString("NOK_QTY_REMARKS"));
	        }
	        if (columnExists(rs, "REJECTED_INTERNAL_REMARKS")) {
	            qi.setRejectIntRemarks(rs.getString("REJECTED_INTERNAL_REMARKS"));
	        }
	        if (columnExists(rs, "REJECTED_EXTERNAL_REMARKS")) {
	            qi.setRejectExtRemarks(rs.getString("REJECTED_EXTERNAL_REMARKS"));
	        }
	        if (columnExists(rs, "REWORK_VENDOR_REMARKS")) {
	            qi.setReworkVenRemarks(rs.getString("REWORK_VENDOR_REMARKS"));
	        }
	        if (columnExists(rs, "REWORK_INTERNAL_REMARKS")) {
	            qi.setReworkIntRemarks(rs.getString("REWORK_INTERNAL_REMARKS"));
	        }
		} catch (Exception e) {
			logger.error("QualityInspectionHdrEntity Exception--->" + e);
		}
		return qi;
	}
	
	//column checking purpose (column is there or not)
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
