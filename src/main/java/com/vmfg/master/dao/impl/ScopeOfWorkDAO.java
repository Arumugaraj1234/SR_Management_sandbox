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

import com.vmfg.master.dao.interfaces.IScopeOfWorkDAO;
import com.vmfg.master.entity.ScopOfWorkEntity;
import com.vmfg.master.rowmapper.ScopeOfWorkRowMapper;
@Transactional
@Repository
public class ScopeOfWorkDAO implements IScopeOfWorkDAO{
	private static final Logger logger = LoggerFactory.getLogger(ScopeOfWorkDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ScopOfWorkEntity> getScopeOfWorkInfo(String tENANT_ID) {
		List<ScopOfWorkEntity> list=new ArrayList<>();
		try {
			String query="select SOW_CODE,SOW_DESC from sow_mst where TENANT_ID=?";
			RowMapper<ScopOfWorkEntity> rowmapper = new ScopeOfWorkRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper, tENANT_ID);	 
		} catch (Exception ex) {
			logger.error("getScopeOfWorkInfo  method exception-->" + ex);
		}
		logger.debug("getScopeOfWorkInfo  method end");
		return list;		
	}

}
