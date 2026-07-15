package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.GrnHdrEntity;

public class GrnHdrRowMapper implements RowMapper<GrnHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GrnHdrRowMapper.class);

	@Override
	public GrnHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GrnHdrEntity grnHdrEntity = new GrnHdrEntity();
		try {
			if (columnExists(rs, "VENDOR_CODE")) {
				grnHdrEntity.setVendorCode(rs.getString("VENDOR_CODE"));
			}
			if (columnExists(rs, "VENDOR_NAME")) {
				grnHdrEntity.setVendorName(rs.getString("VENDOR_NAME"));
			}

			if (columnExists(rs, "MI_ID")) {
				grnHdrEntity.setMiId(rs.getString("MI_ID"));
			}

			if (columnExists(rs, "MI_CODE")) {
				grnHdrEntity.setMiCode(rs.getString("MI_CODE"));
			}
			if (columnExists(rs, "GRN_HDR_ID")) {
				grnHdrEntity.setGrnHdrId(rs.getString("GRN_HDR_ID"));
			}
			if (columnExists(rs, "GRN_CODE")) {
				grnHdrEntity.setGrnCode(rs.getString("GRN_CODE"));
			}

			if (columnExists(rs, "TRANSACTION_NO")) {
				grnHdrEntity.setTransactionNo(rs.getString("TRANSACTION_NO"));
			}
			if (columnExists(rs, "FINANCIAL_YEAR_MST_ID")) {
				grnHdrEntity.setFinancialYearMstId(rs.getString("FINANCIAL_YEAR_MST_ID"));
			}

			if (columnExists(rs, "GRN_DATE")) {
				grnHdrEntity.setGrnDate(rs.getString("GRN_DATE"));
			}
			if (columnExists(rs, "CREATED_ON")) {
				grnHdrEntity.setCreatedOn(rs.getString("CREATED_ON"));
			}
			if (columnExists(rs, "CREATED_BY")) {
				grnHdrEntity.setCreatedBy(rs.getString("CREATED_BY"));
			}
			if (columnExists(rs, "LAST_UPDATED_ON")) {
				grnHdrEntity.setLastUpdatedOn(rs.getString("LAST_UPDATED_ON"));
			}
			if (columnExists(rs, "LAST_UPDATED_BY")) {
				grnHdrEntity.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
			}
			if (columnExists(rs, "CREATED_EMP_NAME")) {
				grnHdrEntity.setCreatedEmpName(rs.getString("CREATED_EMP_NAME"));
			}
			if (columnExists(rs, "LAST_UPDATED_EMP_NAME")) {
				grnHdrEntity.setLastUpdatedEmpName(rs.getString("LAST_UPDATED_EMP_NAME"));
			}
			if (columnExists(rs, "INVENTORY_LOCATION_DESCRIPTION")) {
				grnHdrEntity.setInvLocation(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
			}
			if (columnExists(rs, "INVENTORY_LOCATION_CODE")) {
				grnHdrEntity.setInvLocCode(rs.getString("INVENTORY_LOCATION_CODE"));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				grnHdrEntity.setProductCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "PRODUCT_DESC")) {
				grnHdrEntity.setProductDesc(rs.getString("PRODUCT_DESC"));
			}
			if (columnExists(rs, "INWARD_DATE")) {
				grnHdrEntity.setMiDate(rs.getString("INWARD_DATE"));
			}
			if (columnExists(rs, "PO_CODE")) {
				grnHdrEntity.setPoCode(rs.getString("PO_CODE"));
				String poCode = rs.getString("PO_CODE");
				if (poCode == null || poCode.trim().isEmpty()) {
					grnHdrEntity.setPoCode(rs.getString("GRN_PO"));
				}
			}

//			if (columnExists(rs, "GRN_PO")) {
//				String grnPo = rs.getString("GRN_PO");
//				if (grnPo == null || grnPo.trim().isEmpty()) {
//					grnHdrEntity.setPoCode("PO_CODE"); // fallback default
//				} else {
//					grnHdrEntity.setPoCode(grnPo);
//				}
//			}


			if (columnExists(rs, "GRN_QTY")) {
				grnHdrEntity.setGrnQty(rs.getString("GRN_QTY"));
			}
			if (columnExists(rs, "PROJECT_CODE")) {
				grnHdrEntity.setProjectId(rs.getString("PROJECT_CODE"));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
				grnHdrEntity.setProjectName(rs.getString("PROJECT_NAME"));
			}
			if (columnExists(rs, "DC_NO")) {
				grnHdrEntity.setDcNo(rs.getString("DC_NO"));
			}
			if (columnExists(rs, "DC_DATE")) {
				grnHdrEntity.setDcDate(rs.getString("DC_DATE"));
			}

		} catch (Exception ex) {
			logger.error("GrnHdrRowMapper  Method Exception" + ex);

		}
		return grnHdrEntity;
	}

	// column checking purpose (column is there or not)
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
