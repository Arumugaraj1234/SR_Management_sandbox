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

import com.vmfg.master.dao.interfaces.IReasonCodeMasterDAO;
import com.vmfg.master.entity.ReasonCodeMasterEntity;
import com.vmfg.master.rowmapper.ReasonCodeMasterRowMapper;
@Transactional
@Repository
public class ReasonCodeMasterDAO implements IReasonCodeMasterDAO{
	private static final Logger logger = LoggerFactory.getLogger(ReasonCodeMasterDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ReasonCodeMasterEntity> getReasonCodeInfo(String tENANT_ID, String eMPLOYEE_ID) {
		List<ReasonCodeMasterEntity> list=new ArrayList<>();
		try {
			String query="SELECT \r\n" + 
					"    rmst.REASON_CODE,\r\n" + 
					"    rmst.REASON_DESCRIPTION,\r\n" + 
					"    rmst.REASON_CODE_TYPE,\r\n" + 
					"    rtype.REASON_TYPE_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    employee_mst emst\r\n" + 
					"        INNER JOIN\r\n" + 
					"    reason_code_mst rmst ON emst.DEPARTMENT_CODE = rmst.DEPARTMENT_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    reason_code_type rtype ON rtype.REASON_CODE_TYPE = rmst.REASON_CODE_TYPE\r\n" + 
					"WHERE\r\n" + 
					"    emst.EMPLOYEE_ID = ?\r\n" + 
					"        AND emst.TENANT_ID = ?\r\n" + 
					"        AND rmst.IS_ACTIVE = ?";
			RowMapper<ReasonCodeMasterEntity> rowmapper = new ReasonCodeMasterRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper, eMPLOYEE_ID,tENANT_ID,"1");	 
		} catch (Exception ex) {
			logger.error("getReasonCodeInfo  method exception-->" + ex);
		}
		logger.debug("getReasonCodeInfo  method end");
		return list;
	}

}
