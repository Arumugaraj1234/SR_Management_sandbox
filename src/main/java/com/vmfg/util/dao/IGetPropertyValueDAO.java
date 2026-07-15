package com.vmfg.util.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.vmfg.util.entity.DocumentPropertyMst;
import com.vmfg.util.entity.TenantPropertyMst;

public interface IGetPropertyValueDAO {

	List<TenantPropertyMst> getPropValueByTenantByService(String tENANT_ID, String MODULE_NAME, JdbcTemplate jdbcTemplate);
	List<DocumentPropertyMst> getDocPropValueByService(String tENANT_ID, String MODULE_NAME, String LINE_CODE,String PROGRAM_CODE, JdbcTemplate jdbcTemplate);
	String getTenantPropertyVal(String pROPERTY_NAME,JdbcTemplate jdbcTemplate);
	String getTenantPropertyValByTenantId(String pROPERTY_NAME,String tenantId,JdbcTemplate jdbcTemplate);
}
