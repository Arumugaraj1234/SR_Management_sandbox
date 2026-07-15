package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.BudgetSheetPaymentEntity;
import com.vmfg.scm.rowmapper.PoHdrRowMapper;

public class BudgetSheetPaymentRowMapper implements RowMapper<BudgetSheetPaymentEntity> {
	    private static final Logger logger = LoggerFactory.getLogger(PoHdrRowMapper.class);

	    @Override
	    public BudgetSheetPaymentEntity mapRow(ResultSet row, int rowNum) throws SQLException {
	    	BudgetSheetPaymentEntity pt = new BudgetSheetPaymentEntity();
	        try {
	        	if (columnExists(row, "BS_HDR_ID")) {
	        		pt.setSbPtId(row.getString("BS_HDR_ID"));
				}
	        	if (columnExists(row, "TERM")) {
	        		pt.setTerm(row.getString("TERM"));
				}
	        	if (columnExists(row, "BS_POT_ID")) {
	        		pt.setSbPtId(row.getString("BS_POT_ID"));
				}
	        	if (columnExists(row, "PERCENTAGE")) {
	        		pt.setPercentage(row.getString("PERCENTAGE"));
				}
	        	if (columnExists(row, "PLANNED_DATE")) {
	        		pt.setPlannedDate(row.getString("PLANNED_DATE"));
				}
	        	if (columnExists(row, "ACTUAL_DATE")) {
	        		pt.setActualDate(row.getString("ACTUAL_DATE"));
				}
	        	if (columnExists(row, "REMARKS")) {
	        		pt.setRemarks(row.getString("REMARKS"));
				}
	        	
	        }catch(Exception ex) {
	        	logger.error("BudgetSheetPaymentRowMapper Method Exception" + ex);
	        }
			return pt;
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
