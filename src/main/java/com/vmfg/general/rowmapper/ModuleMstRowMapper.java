package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.ModuleMstEntity;

public class ModuleMstRowMapper implements RowMapper<ModuleMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ModuleMstRowMapper.class);

	@Override
	public ModuleMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModuleMstEntity result=new ModuleMstEntity();
		try {
			result.setDesc(rs.getString("DESCRIPTION"));
			result.setModuleId(rs.getString("UI_MODULE_MST_ID"));
		}catch(Exception ex) {
			logger.error("ModuleMstRowMapper error "+ex);
		}
		// TODO Auto-generated method stub
		return result;
	}

}
