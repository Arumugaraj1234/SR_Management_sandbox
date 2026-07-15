package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.VendorDetailDrillDownEntity;

public class GetVendorDetailDrillDownRowMapper implements RowMapper<VendorDetailDrillDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetVendorDetailDrillDownRowMapper.class);

	@Override
	public VendorDetailDrillDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		VendorDetailDrillDownEntity lst = new VendorDetailDrillDownEntity();
		try {
		        if (columnExists(rs, "PROJECT_CODE")) {
		            lst.setProjCode(rs.getString("PROJECT_CODE"));
		        }
		        if (columnExists(rs, "PM_HDR_ID")) {
		            lst.setPmHdrId(rs.getString("PM_HDR_ID"));
		        }
		        if (columnExists(rs, "PROJECT_NAME")) {
		            lst.setProjName(rs.getString("PROJECT_NAME"));
		        }
		        if(columnExists(rs, "VENDOR_NAME")) {
		        	lst.setVendorName(rs.getString("VENDOR_NAME"));
		        }
		        if(columnExists(rs, "PO_CODE")) {
		        	lst.setPoCode(rs.getString("PO_CODE"));
		        }
		        if(columnExists(rs, "PO_DATE")) {
		        	lst.setPoDate(rs.getString("PO_DATE"));
		        }
		        if(columnExists(rs, "PRA_CODE")) {
		        	lst.setPraCode(rs.getString("PRA_CODE"));
		        }
		        if(columnExists(rs, "PRA_DATE")) {
		        	lst.setPraDate(rs.getString("PRA_DATE"));
		        }
		        if(columnExists(rs, "AMOUNT_PAYABLE")) {
		        	lst.setAmountPayable(rs.getString("AMOUNT_PAYABLE"));
		        }
		        if(columnExists(rs, "AMOUNT_DUE")) {
		        	lst.setAmountDue(rs.getString("AMOUNT_DUE"));
		        }
		        if(columnExists(rs, "DUE_DATE")) {
		        	lst.setDueDate(rs.getString("DUE_DATE"));
		        }
		        if(columnExists(rs, "STATUS")) {
		        	lst.setStatus(rs.getString("STATUS"));
		        }
		        if(columnExists(rs, "DUE_DATE")) {
		        	lst.setDueDate(rs.getString("DUE_DATE"));
		        }
		        if(columnExists(rs,"OVER_DUE")) {
		        	lst.setOverDue(rs.getString("OVER_DUE"));
		        }
		        if(columnExists(rs,"DONE_SO_FAR")) {
		        	lst.setPaidSoFar(rs.getString("DONE_SO_FAR"));
		        }
		        if(columnExists(rs,"INVOICE_NO")) {
		        	lst.setInvoiceNo(rs.getString("INVOICE_NO"));
		        }
		        if(columnExists(rs,"INVOICE_DATE")) {
		        	lst.setInvoiceDate(rs.getString("INVOICE_DATE"));
		        }
		}catch(Exception ex) {
			logger.error("GetVendorDetailDrillDownRowMapper  Method Exception" + ex);
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
