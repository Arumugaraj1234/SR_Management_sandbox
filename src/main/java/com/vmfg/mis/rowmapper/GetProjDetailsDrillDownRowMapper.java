package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.ProjDetailsDrillDownEntity;

public class GetProjDetailsDrillDownRowMapper implements RowMapper<ProjDetailsDrillDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetProjDetailsDrillDownRowMapper.class);

	@Override
	public ProjDetailsDrillDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjDetailsDrillDownEntity lst = new ProjDetailsDrillDownEntity();
		try {
			    if (columnExists(rs, "ORDER_VALUE")) {
	                lst.setOrderValue(rs.getString("ORDER_VALUE"));
	            }
		        if (columnExists(rs, "CUST_NAME")) {
		            lst.setCustName(rs.getString("CUST_NAME"));
		        }
		        if (columnExists(rs, "PROJECT_CODE")) {
		            lst.setProjCode(rs.getString("PROJECT_CODE"));
		        }
		        if (columnExists(rs, "PROJECT_NAME")) {
		            lst.setProjName(rs.getString("PROJECT_NAME"));
		        }
		        if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
		            lst.setStage(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
		        }
		        if (columnExists(rs, "SALE_BUDGET_VALUE")) {
		            lst.setBudgetCost(rs.getString("SALE_BUDGET_VALUE"));
		        }
		        if (columnExists(rs, "CONTRIBUTION")) {
		            lst.setContrib(rs.getString("CONTRIBUTION"));
		        }
		        if (columnExists(rs, "RECEIVED_BALANCE")) {
		            lst.setReceived(rs.getString("RECEIVED_BALANCE"));
		        }
		        if (columnExists(rs, "RECEIVABLE")) {
		            lst.setReceivable(rs.getString("RECEIVABLE"));
		        }
		        if (columnExists(rs, "PM_HDR_ID")) {
		            lst.setPmHdrId(rs.getString("PM_HDR_ID"));
		        }
		}catch(Exception ex) {
			logger.error("GetProjDetailsDrillDownRowMapper  Method Exception" + ex);
		}
		return lst;
	}

	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
				return true;
			}
		}

		return false;
	}

}
