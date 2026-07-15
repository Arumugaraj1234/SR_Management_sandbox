package com.vmfg.master.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.master.dao.interfaces.IIndustryTypeDAO;
import com.vmfg.master.entity.IndustryTypeEntity;
import com.vmfg.master.rowmapper.IndustryTypeRowMapper;
@Transactional
@Repository
public class IndustryTypeDAO implements IIndustryTypeDAO{
	private static final Logger logger = LoggerFactory.getLogger(IndustryTypeDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<IndustryTypeEntity> getIndustryTypeInfo(String tENANT_ID) {
		List<IndustryTypeEntity> list=new ArrayList<>();
		try {
			String query="select IT_CODE,IT_DESC from industry_type_code where TENANT_ID= ?";
			RowMapper<IndustryTypeEntity> rowmapper = new IndustryTypeRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper, tENANT_ID);	 
		} catch (Exception ex) {
			logger.error("getScopeOfWorkInfo  method exception-->" + ex);
		}
		logger.debug("getScopeOfWorkInfo  method end");
		return list;
	}

}
