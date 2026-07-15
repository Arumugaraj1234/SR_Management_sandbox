package com.vmfg.util.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.vmfg.util.dao.IGetPropertyValueDAO;
import com.vmfg.util.entity.DocumentPropertyMst;
import com.vmfg.util.entity.TenantPropertyMst;

@Service
public class GetPropertyValueService implements IGetPropertyValueService{

	@Autowired
	private IGetPropertyValueDAO iGetPropertyValueDAO;
	
	
	@Override
	public List<TenantPropertyMst> getPropValueByTenantByService(String tENANT_ID, String MODULE_NAME, JdbcTemplate jdbcTemplate) {
		return iGetPropertyValueDAO.getPropValueByTenantByService(tENANT_ID,MODULE_NAME,jdbcTemplate);
	}


	@Override
	public List<DocumentPropertyMst> getDocPropValueByService(String tENANT_ID, String MODULE_NAME, String LINE_CODE,String PROGRAM_CODE,
			JdbcTemplate jdbcTemplate) {
		return iGetPropertyValueDAO.getDocPropValueByService(tENANT_ID, MODULE_NAME, LINE_CODE,PROGRAM_CODE, jdbcTemplate);
	}


	@Override
	public String getTenantPropertyVal(String pROPERTY_NAME,JdbcTemplate jdbcTemplate) {
		return iGetPropertyValueDAO.getTenantPropertyVal(pROPERTY_NAME,jdbcTemplate);
	}

}
