package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.ProcessConfigEntity;

public class ProcessConfigRowMapper  implements RowMapper<ProcessConfigEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProcessConfigRowMapper.class);

	@Override
	public ProcessConfigEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		ProcessConfigEntity tm = new ProcessConfigEntity();
		try {
			tm.setAlwaysVisible(row.getString("ALWAYS_VISIBLE"));
			tm.setComponent(row.getString("COMPONENT"));
			tm.setMasterDocStatus(row.getString("MASTER_DOC_STATUS"));
			tm.setPcId(row.getString("PC_ID"));
			tm.setPreviousStgEdit(row.getString("PREVIOUS_STG_EDIT"));
			tm.setProcessCode(row.getString("PM_ID"));
			tm.setProcessDesc(row.getString("PROCESS_NAME"));
			tm.setSeq(row.getInt("SEQ"));
			tm.setStgCode(row.getString("STG_CODE"));
			tm.setStgComDesc(row.getString("STG_COM_DESC"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setStgDesc(row.getString("STG_DESC"));
			tm.setMstDocstsDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
		} catch (Exception e) {
			logger.error("ProcessConfigRowMapper Exception--->" + e);
		}
		return tm;
	}

}
