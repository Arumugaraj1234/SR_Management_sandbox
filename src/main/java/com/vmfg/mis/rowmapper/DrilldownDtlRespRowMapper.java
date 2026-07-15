package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.DrilldownDtlEntity;

public class DrilldownDtlRespRowMapper implements RowMapper<DrilldownDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DrilldownDtlRespRowMapper.class);

	@Override
	public DrilldownDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DrilldownDtlEntity lst = new DrilldownDtlEntity();
		try {
			if (columnExists(rs, "INSPECTION_OK")) {
			lst.setInspOk(rs.getString("INSPECTION_OK"));
			}
			if (columnExists(rs, "INSPECTION_QTY")) {
			lst.setInspQty(rs.getString("INSPECTION_QTY"));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
			lst.setProjName(rs.getString("PROJECT_NAME"));
			}
			if (columnExists(rs, "QUALITY_RATING")) {
			lst.setQualityRate(rs.getString("QUALITY_RATING"));
			}
			if (columnExists(rs, "REWORK_QTY")) {
			lst.setReWorkQty(rs.getString("REWORK_QTY"));
			}
			if (columnExists(rs, "REJECT_QTY")) {
			lst.setRejQty(rs.getString("REJECT_QTY"));
			}
			if (columnExists(rs, "VENDOR_NAME")) {
			lst.setVendorName(rs.getString("VENDOR_NAME")); 
			}
			if (columnExists(rs, "CA")) {
	        lst.setCa(rs.getString("CA"));
			}
	        if (columnExists(rs, "DESCRIPTION")) {
	        lst.setProductDesc(rs.getString("DESCRIPTION"));
	        }
	        if (columnExists(rs, "INSPECTED_ON")) {
	        lst.setInspOn(rs.getString("INSPECTED_ON"));
	        }
	        if (columnExists(rs, "PROJECT_CODE")) {
	        lst.setProjCode(rs.getString("PROJECT_CODE"));
	        }
	        if (columnExists(rs, "OK_QTY")) {
	        lst.setOkQty(rs.getString("OK_QTY"));
	        }
	        if (columnExists(rs, "CA_INTERNAL")) {
	        lst.setCaInternal(rs.getString("CA_INTERNAL"));
	        }
	        if (columnExists(rs, "CA_VENDOR")) {
	        lst.setCaVendor(rs.getString("CA_VENDOR"));
	        }
	        if (columnExists(rs, "REJECTED_EXTERNAL")) {
	        lst.setRejectExternal(rs.getString("REJECTED_EXTERNAL"));
	        }
	        if (columnExists(rs, "REJECTED_INTERNAL")) {
	        lst.setRejectInternal(rs.getString("REJECTED_INTERNAL"));
	        }
	        if (columnExists(rs, "REWORK_INTERNAL")) {
	        lst.setReWorkInternal(rs.getString("REWORK_INTERNAL"));
	        }
	        if (columnExists(rs, "REWORK_VENDOR")) {
	        lst.setReWorkVendor(rs.getString("REWORK_VENDOR"));
	        }
	        if (columnExists(rs, "INSP_COMPLETED")) {
		        lst.setInspCompleteCnt(rs.getString("INSP_COMPLETED"));
		    }
	        if (columnExists(rs, "INSP_REQ")) {
		        lst.setInspReqCnt(rs.getString("INSP_REQ"));
		    }
	        if (columnExists(rs, "QC_NOT_REQUIRED_QTY")) {
		        lst.setInspNotReq(rs.getString("QC_NOT_REQUIRED_QTY"));
		    }
	        if (columnExists(rs, "UNDER_INSPECTION_SCOPE")) {
		        lst.setUnderScope(rs.getString("UNDER_INSPECTION_SCOPE"));
		    }
	        if (columnExists(rs, "NOT_UNDER_INSPECTION_SCOPE")) {
		        lst.setNotUnderScope(rs.getString("NOT_UNDER_INSPECTION_SCOPE"));
		    }
	        if (columnExists(rs, "PO_CODE")) {
		        lst.setPoCode(rs.getString("PO_CODE"));
		    }
	        if (columnExists(rs, "PRODUCT_CODE")) {
		        lst.setProdCode(rs.getString("PRODUCT_CODE"));
		    }
	        
		}catch(Exception ex) {
			logger.error("DrilldownDtlRespRowMapper  Method Exception" + ex);
		}
		return lst;
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
