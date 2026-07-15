package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.QualityInspectionDtlEntity;

public class QualityInspectionDtlRowMapper implements RowMapper<QualityInspectionDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(QualityInspectionDtlRowMapper.class);

    @Override
    public QualityInspectionDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        QualityInspectionDtlEntity qiDtl = new QualityInspectionDtlEntity();
        try {
        
        if (columnExists(rs, "QI_DTL_ID")) {
            qiDtl.setQiDtlId(rs.getString("QI_DTL_ID"));
        }
        if (columnExists(rs, "QI_HDR_ID")) {
            qiDtl.setQiHdrId(rs.getString("QI_HDR_ID"));
        }
        if (columnExists(rs, "DESCRIPTION")) {
            qiDtl.setDescription(rs.getString("DESCRIPTION"));
        }
        if (columnExists(rs, "SPECIFICATION")) {
            qiDtl.setSpecification(rs.getString("SPECIFICATION"));
        }
        if (columnExists(rs, "INSPECTION_METHOD")) {
            qiDtl.setInspectionMethod(rs.getString("INSPECTION_METHOD"));
        }
        if (columnExists(rs, "MINIMUM")) {
            qiDtl.setMinimum(rs.getString("MINIMUM"));
        }
        if (columnExists(rs, "MAXIMUM")) {
            qiDtl.setMaximum(rs.getString("MAXIMUM"));
        }
        if (columnExists(rs, "AVERAGE")) {
            qiDtl.setAverage(rs.getString("AVERAGE"));
        }
        if (columnExists(rs, "INSPECTION_RESULT")) {
            qiDtl.setInspectionResult(rs.getString("INSPECTION_RESULT"));
        }
        if (columnExists(rs, "TENANT_ID")) {
            qiDtl.setTenantId(rs.getString("TENANT_ID"));
        }
        if (columnExists(rs, "QIC_DTL_ID")) {
        	qiDtl.setQicDtlId(rs.getString("QIC_DTL_ID"));
        }
        if (columnExists(rs, "QIC_HDR_ID")) {
        	qiDtl.setQicHdrId(rs.getString("QIC_HDR_ID"));
        }
        if (columnExists(rs, "IS_ACTIVE")) {
        	qiDtl.setIsActive(rs.getString("IS_ACTIVE"));
        }
        if (columnExists(rs, "INSPECTION_TYPE")) {
		    qiDtl.setInspectionType(rs.getString("INSPECTION_TYPE"));
		}
        if (columnExists(rs, "SERIAL_NO")) {
        	qiDtl.setSerialNumber(rs.getString("SERIAL_NO"));
        }
        if (columnExists(rs, "S_NO")) {
        	qiDtl.setSNo(rs.getInt("S_NO"));
        }
        }catch (Exception e) {
        	logger.error("QualityInspectionDtlRowMapper  Method Exception" + e);
		}
        return qiDtl;
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