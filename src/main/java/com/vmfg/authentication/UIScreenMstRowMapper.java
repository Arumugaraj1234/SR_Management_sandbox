package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class UIScreenMstRowMapper implements RowMapper<UIScreenMst>{
	private static final Logger logger = LoggerFactory.getLogger(UIScreenMstRowMapper.class);
	@Override
	public UIScreenMst mapRow(ResultSet row, int rowNum) throws SQLException {
		UIScreenMst uism = new UIScreenMst();
		try {
			uism.setUiModuleMstID(row.getInt("UI_MODULE_MST_ID"));
			uism.setUiScreenMstID(row.getInt("UI_SCREEN_MST_ID"));
			uism.setDescription(row.getString("DESCRIPTION"));
			
			uism.setDisplayName(row.getString("DISPLAY_NAME"));
			uism.setMaterialIcon(row.getString("MATERIAL_ICON"));
			uism.setLinkUrl(row.getString("LINK_URL"));
			uism.setSeqNO(row.getInt("SEQ_NO"));
			uism.setIsActive(row.getString("IS_ACTIVE"));
			uism.setTenantId(row.getString("TENANT_ID"));
			uism.setSubModule(row.getString("SUB_MODULE"));

			
		} catch (Exception e) {
			logger.error("UIScreenMstRowMapper Method Exception---->"+e);
		}
		return uism;
	}

}
