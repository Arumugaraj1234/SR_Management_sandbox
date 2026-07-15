package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.GeneralEntity;

public class GeneralEntityRowMapper implements RowMapper<GeneralEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GeneralEntityRowMapper.class);

	@Override
	public GeneralEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		GeneralEntity ge = new GeneralEntity();
		try {
			if (columnExists(rs, "KEY1")) {
				ge.setKey1(rs.getString("KEY1"));
			}

			if (columnExists(rs, "KEY2")) {
				ge.setKey2(rs.getString("KEY2"));
			}

			if (columnExists(rs, "KEY3")) {
				ge.setKey3(rs.getString("KEY3"));
			}

			if (columnExists(rs, "KEY4")) {
				ge.setKey4(rs.getString("KEY4"));
			}
			
			if (columnExists(rs, "KEY5")) {
				ge.setKey5(rs.getString("KEY5"));
			}
			if (columnExists(rs, "KEY6")) {
				ge.setKey6(rs.getString("KEY6"));
			}

		} catch (Exception ex) {
			logger.error("GeneralEntityRowMapper  Method Exception" + ex);

		}
		return ge;
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
