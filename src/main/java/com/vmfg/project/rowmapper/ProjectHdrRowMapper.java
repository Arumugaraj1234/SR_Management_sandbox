package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.ProjectHdr;

public class ProjectHdrRowMapper implements RowMapper<ProjectHdr> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectHdrRowMapper.class);

	@Override
	public ProjectHdr mapRow(ResultSet row, int rowNum) throws SQLException {
		ProjectHdr ph = new ProjectHdr();
		try {
			ph.setCreatedDate(row.getString("CREATED_DATE"));
			ph.setCustomerName(row.getString("CUSTOMER_NAME"));
			ph.setDueDate(row.getString("DUE_DATE"));
			ph.setEnquiryCode(row.getString("ENQUIRY_CODE"));
			ph.setEnquiryId(row.getString("ENQUIRY_ID"));
			ph.setPmHdrId(row.getString("PM_HDR_ID"));
			ph.setStagName(row.getString("STG_DESC"));
			ph.setProductDtl(row.getString("PRODUCT_DETAILS"));
			ph.setProjCode(row.getString("PROJECT_CODE"));
			ph.setProjDec(row.getString("PROJECT_DESCRIPTION"));
			ph.setProjectName(row.getString("PROJECT_NAME"));
			ph.setProjHandOverDate(row.getString("PROJECT_HANDOVER_DATE"));
			ph.setStatusType(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			ph.setHdrStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			ph.setInitiateDate(row.getString("INITIATION_DATE"));
			ph.setPlannedStartDate(row.getString("PLANNED_START_DATE"));
			ph.setPlannedEndDate(row.getString("PLANNED_END_DATE"));
			ph.setIndustrialType(row.getString("INDUSTRIAL_TYPE"));
			ph.setScopeOfWork(row.getString("SCOPE_OF_WORK"));
			ph.setIsInternal(row.getString("IS_INTERNAL"));
			if (columnExists(row, "FINAL_SALE_VALUE")) {
				ph.setSaleValue(row.getString("FINAL_SALE_VALUE"));
			}
			if(columnExists(row, "serial_number")) {
				ph.setSno(row.getInt("serial_number"));
			}
			if(columnExists(row, "SB_HDR_ID")) {
				ph.setSbHdrId(row.getString("SB_HDR_ID"));
			}

			
			if(row.getString("PRIORITY").equalsIgnoreCase("3")) {
				ph.setPriority("High");
			}else if(row.getString("PRIORITY").equalsIgnoreCase("2")) {
				ph.setPriority("Medium");
			}else {
				ph.setPriority("Low");
			}
		} catch (Exception e) {
			logger.error("ProjectHdrRowMapper Exception--->" + e);
		}
		return ph;
	}
	
	// column checking purpose (column is there or not)
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
