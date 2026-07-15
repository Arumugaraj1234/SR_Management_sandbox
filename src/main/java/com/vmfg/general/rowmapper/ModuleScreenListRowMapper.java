package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.ModuleScreenList;

public class ModuleScreenListRowMapper implements RowMapper<ModuleScreenList> {
	private static final Logger logger = LoggerFactory.getLogger(ModuleScreenListRowMapper.class);


	@Override
	public ModuleScreenList mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		ModuleScreenList result=new ModuleScreenList();
		try {
			result.setIsActive(rs.getInt("IS_ACTIVE"));
			result.setModuleDesc(rs.getString("MODULE_DESCRIPTION"));
			result.setScreenDesc(rs.getString("SCREEN_DESCRIPTION"));
			result.setScreenDisplayName(rs.getString("SCREEN_DISPLAY_NAME"));
			result.setScreenMstId(rs.getString("UI_SCREEN_MST_ID"));
		}catch(Exception ex) {
			logger.error("ModuleScreenListRowMapper error "+ex);
		}
		return result;
	}
	

}
