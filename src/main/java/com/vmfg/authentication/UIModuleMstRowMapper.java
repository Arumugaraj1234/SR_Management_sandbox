package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class UIModuleMstRowMapper implements RowMapper<UIModuleMst>{
	private static final Logger logger = LoggerFactory.getLogger(UIModuleMstRowMapper.class);

	@Override
	public UIModuleMst mapRow(ResultSet row, int rowNum) throws SQLException {
		UIModuleMst uimm = new UIModuleMst();
		try {
			uimm.setUiModuleMstID(row.getInt("UI_MODULE_MST_ID"));
			uimm.setDescription(row.getString("DESCRIPTION"));
			uimm.setDisplayName(row.getString("DISPLAY_NAME"));
			uimm.setMaterialIcon(row.getString("MATERIAL_ICON"));
		//	uimm.setLinkUrl(row.getString("LINK_URL"));
			uimm.setSeqNO(row.getInt("SEQ_NO"));
			uimm.setIsActive(row.getString("IS_ACTIVE"));
			uimm.setTenantId(row.getString("TENANT_ID"));
			
		} catch (Exception e) {
			logger.error("UIModuleMstRowMapper Method Exception---->"+e);
		}
		return uimm;
	}

}
