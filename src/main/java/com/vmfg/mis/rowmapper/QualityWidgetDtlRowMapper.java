package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.QualityWidgetDtlEntity;

public class QualityWidgetDtlRowMapper implements RowMapper<QualityWidgetDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(QualityWidgetDtlRowMapper.class);

	@Override
	public QualityWidgetDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		QualityWidgetDtlEntity lst = new QualityWidgetDtlEntity();
		try {
			lst.setCa(rs.getString("CA"));
			lst.setInspQty(rs.getString("INSPECTION_QTY"));
			lst.setRejQty(rs.getString("REJECT_QTY"));
			lst.setReworkQty(rs.getString("REWORK_QTY"));
			lst.setInspOk(rs.getString("INSPECTION_OK"));
		//	lst.setInspCall(rs.getString("INSPECTION_CALL"));
		}catch(Exception ex) {
			logger.error("QualityWidgetDtlRowMapper  Method Exception" + ex);
		}
		return lst;
	}

}
