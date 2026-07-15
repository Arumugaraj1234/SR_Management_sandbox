package com.vmfg.master.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.master.dao.interfaces.ITaskTypeDAO;
import com.vmfg.master.entity.TaskTypeEntity;
import com.vmfg.master.rowmapper.TasktypeDtlsRowMapper;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class TaskTypeDAO implements ITaskTypeDAO{
	private static final Logger logger = LoggerFactory.getLogger(FileUploadConfigDAO.class);

	@Autowired JdbcTemplate jdbcTemplate;
	
	@Override
	public List<TaskTypeEntity> getTasktypeDtls(String deptCode, String tenantId) {
		List<TaskTypeEntity> list= null;
		try {
			String query="select TT_DESC, TT_CODE, IS_ACTIVE from task_type_mst where DEPT_CODE =? and TENANT_ID =?;";
			RowMapper<TaskTypeEntity> rowmapper = new TasktypeDtlsRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper,deptCode, tenantId);	 
		} catch (Exception ex) {
			logger.error("getTasktypeDtls  method exception-->" + ex);
		}
		logger.debug("getTasktypeDtls  method end");
		return list;
	}

	@Override
	public int insertTaskType(String deptCode, String tenantId, String ttDesc, String isActive) {
		int resp = 0;
		String ttCode =""; 
		try {
			String CodeQry = "select TT_CODE from task_type_mst order by TT_CODE desc limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(CodeQry);
			String Codeval = resultMap.get("TT_CODE").toString();
			String tCode = CommonMethod.getUomNewCode("TT", Codeval, "TT001");
			
			String split = tCode.substring(2,tCode.length());
			if((Integer.parseInt(split)+"").length() <=2) {
				ttCode = tCode.substring(0,3) + String.valueOf(Integer.parseInt(tCode.substring(2, tCode.length())));
			}else {
				ttCode = tCode.substring(0,2) + String.valueOf(Integer.parseInt(tCode.substring(2, tCode.length())));
			}

			String insFile = "INSERT INTO task_type_mst (TT_CODE, TT_DESC, DEPT_CODE, IS_ACTIVE, TENANT_ID) VALUES( ?, ?, ?, ?, ?);";
			int insert = this.jdbcTemplate.update(insFile, ttCode, ttDesc, deptCode, isActive, tenantId );
			if (insert == 1) {
				resp = 1;
			}				
		}catch(Exception e) {
			logger.error("insertTaskType  method end" +e);
		}	
		return resp;
	}

	@Override
	public int updateTaskType(String ttCode, String ttDesc, String tenantId, String isActive) {
		int resp = 0;
		try {
			String updateFile = "update task_type_mst set TT_DESC=?, IS_ACTIVE=? where TT_CODE =? and TENANT_ID =? ;";
			int update = this.jdbcTemplate.update(updateFile, ttDesc, isActive, ttCode, tenantId);
			if (update == 1) {
				resp = 1;
			}
		}catch(Exception e) {
		    logger.debug("updateTaskType method end" +e);		
		}
		return resp;
	}

}
