package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.RetrieveMSDtlByHdrEntity;

public class RetrieveMSDtlByHdrRowMapper implements RowMapper<RetrieveMSDtlByHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(RetrieveMSDtlByHdrRowMapper.class);

	@Override
	public RetrieveMSDtlByHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		RetrieveMSDtlByHdrEntity result = new RetrieveMSDtlByHdrEntity();
		try {

			if (columnExists(rs, "PRODUCT_ID")) {
				result.setProductId(rs.getString("PRODUCT_ID"));
			}

			if (columnExists(rs, "QTY")) {
				result.setQty(rs.getString("QTY"));
			}

			if (columnExists(rs, "PRODUCT_CODE")) {
				result.setProductCode(rs.getString("PRODUCT_CODE"));
			}

			if (columnExists(rs, "PRODUCT_DESCRIPTION")) {
				result.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			}
			if (columnExists(rs, "UOM_LONG_DESCRIPTION")) {
				result.setUomLongDesc(rs.getString("UOM_LONG_DESCRIPTION"));
			}
			if (columnExists(rs, "STATION")) {
				result.setStation(rs.getString("STATION"));
			}
			if (columnExists(rs, "SUB_ASSY")) {
				result.setSubAssy(rs.getString("SUB_ASSY"));
			}

			result.setDtlId(rs.getString("MS_DTL_ID"));
			
			if (columnExists(rs, "SPECIFICATION")) {
				result.setSpecification(rs.getString("SPECIFICATION"));
			}
			
			if (columnExists(rs, "MAKE")) {
				result.setMake(rs.getString("MAKE"));
			}

		} catch (Exception ex) {
			logger.error("MsHdrRetrieveRowMapper error " + ex);
		}

		return result;
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
