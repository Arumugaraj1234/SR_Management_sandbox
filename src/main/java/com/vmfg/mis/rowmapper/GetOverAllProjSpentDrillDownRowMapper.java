package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.OverAllProjSpentDrillDownEntity;

public class GetOverAllProjSpentDrillDownRowMapper implements RowMapper<OverAllProjSpentDrillDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetOverAllProjSpentDrillDownRowMapper.class);

	@Override
	public OverAllProjSpentDrillDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		OverAllProjSpentDrillDownEntity lst = new OverAllProjSpentDrillDownEntity();
		try {
			    if (columnExists(rs, "PROJECT_CODE")) {
		            lst.setProjCode(rs.getString("PROJECT_CODE"));
		        }
		        if (columnExists(rs, "PROJECT_NAME")) {
		            lst.setProjName(rs.getString("PROJECT_NAME"));
		        }
		        if (columnExists(rs, "PM_HDR_ID")) {
		            lst.setPmHdrId(rs.getString("PM_HDR_ID"));
		        }
			    if (columnExists(rs, "ORDER_VALUE")) {
	                lst.setOrderValue(rs.getString("ORDER_VALUE"));
	            }
			    if (columnExists(rs, "PROJECT_VALUE")) {
	                lst.setProjcBudget(rs.getString("PROJECT_VALUE"));
	            }
		        if (columnExists(rs, "CUSTOMER_NAME")) {
		            lst.setCustName(rs.getString("CUSTOMER_NAME"));
		        }   
		        if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
		            lst.setStage(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
		        }
		        if (columnExists(rs, "MATERIAL_PO_RELEASED")) {
		            lst.setMaterialRelesVal(rs.getString("MATERIAL_PO_RELEASED"));
		        }
		        if (columnExists(rs, "SERVICE_VALUE")) {
		            lst.setServiceBudCons(rs.getString("SERVICE_VALUE"));
		        }
		        if (columnExists(rs, "SERVICE_PO_RELEASED")) {
		            lst.setServiceRelesVal(rs.getString("SERVICE_PO_RELEASED"));
		        }
		        if (columnExists(rs, "MATERIAL_VALUE")) {
		            lst.setMaterialBudCons(rs.getString("MATERIAL_VALUE"));
		        }
		        if (columnExists(rs, "TOTAL_VALUE")) {
		            lst.setTotalBudgetConsum(rs.getString("TOTAL_VALUE"));
		        }
		        if (columnExists(rs, "TOTAL_PO_VALUE")) {
		            lst.setTotalPoreles(rs.getString("TOTAL_PO_VALUE"));
		        }
		        if (columnExists(rs, "ACTUAL_SPENT")) {
		            lst.setActualVal(rs.getString("ACTUAL_SPENT"));
		        }
		        if (columnExists(rs, "SCM_BUDGET_ALLOCATED")) {
		            lst.setScmAllocatedVal(rs.getString("SCM_BUDGET_ALLOCATED"));
		        }
		        if (columnExists(rs, "TIMESHEET_COST")) {
		            lst.setEmpCost(rs.getString("TIMESHEET_COST"));
		        }
		        if (columnExists(rs, "OVER_ALL_COST")) {
		            lst.setMaterialTransferCost(rs.getString("OVER_ALL_COST"));
		        }
		        if (columnExists(rs, "OTHERS_TALLY")) {
		            lst.setOtherInTally(rs.getString("OTHERS_TALLY"));
		        }
		        if (columnExists(rs, "DN_VALUE")) {
		            lst.setDebitValue(rs.getString("DN_VALUE"));
		        }
		}catch(Exception ex) {
			logger.error("GetOverAllProjSpentDrillDownRowMapper  Method Exception" + ex);
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
