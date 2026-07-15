package com.vmfg.master.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.master.dao.interfaces.ITaskCategoryDAO;
import com.vmfg.master.entity.TaskCategoryEntity;
import com.vmfg.master.entity.TaskTypeDropDownEntity;
import com.vmfg.master.request.TaskCategoryInsertUpdateRequest;
import com.vmfg.master.request.TenantIdRequest;
import com.vmfg.master.rowmapper.TaskCategoryRowMapper;
import com.vmfg.master.rowmapper.TaskTypeDropDownRowMapper;
import com.vmfg.util.CommonMethod;

@Transactional
@Repository
public class TaskCategoryDAO implements ITaskCategoryDAO {
	private static final Logger logger= LoggerFactory.getLogger(TaskCategoryDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<TaskCategoryEntity> getTaskCategory(String ttCode, String tenantId) {
		// TODO Auto-generated method stub
		List<TaskCategoryEntity> list = new ArrayList<>();
		try {
		String query = "select TC_DESC,TC_CODE,IS_ACTIVE from task_category_mst where TT_CODE =? and TENANT_ID=?;";
		list = this.jdbcTemplate.query(query, new TaskCategoryRowMapper(),ttCode,tenantId);
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("getTaskCategory DAO method error"+e);
		}
		return list;
	}

	@Override
	public int insertTaskCategory(TaskCategoryInsertUpdateRequest taskCategoryInsertUpdateRequest) {
		int insert=0;
		String TCCODE="";
		try {
			String tcCode = "select TC_CODE from task_category_mst order by TC_CODE desc limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(tcCode);
			String tcCodeVal = resultMap.get("TC_CODE").toString();
			tcCode = CommonMethod.getUomNewCode("TC",tcCodeVal, "TC001");
			
			String split = tcCode.substring(2,tcCode.length());
			if((Integer.parseInt(split)+"").length() <=2) {
				TCCODE = tcCode.substring(0,3) + String.valueOf(Integer.parseInt(tcCode.substring(2, tcCode.length())));
			}else {
				TCCODE = tcCode.substring(0,2) + String.valueOf(Integer.parseInt(tcCode.substring(2, tcCode.length())));
			}
			String inQry = " INSERT INTO task_category_mst (TC_CODE,TC_DESC,TT_CODE,IS_ACTIVE,TENANT_ID) VALUES(?,?,?,?,?);";
			insert = this.jdbcTemplate.update(inQry, TCCODE, taskCategoryInsertUpdateRequest.getTcDesc(), taskCategoryInsertUpdateRequest.getTtCode(), taskCategoryInsertUpdateRequest.getIsActive(),taskCategoryInsertUpdateRequest.getTenantId());
		}catch (Exception e) {
			logger.error("insertTaskCategory DAO method error"+e);
		}
		return insert;
	}

	@Override
	public int updateTaskCategory(TaskCategoryInsertUpdateRequest taskCategoryInsertUpdateRequest) {
		int update =0;
		try {
			String updtQry = "update task_category_mst set TC_DESC =?,IS_ACTIVE=? where TC_CODE =? and TENANT_ID = ?";
			update = this.jdbcTemplate.update(updtQry,taskCategoryInsertUpdateRequest.getTcDesc(),taskCategoryInsertUpdateRequest.getIsActive(),taskCategoryInsertUpdateRequest.getTcCode(),taskCategoryInsertUpdateRequest.getTenantId());
		}catch (Exception e) {
			logger.error("updateTaskCategory DAO method error"+e);	
		}
		return update;
	}

	@Override
	public List<TaskTypeDropDownEntity> getTaskTypeDropDownIsActive(TenantIdRequest taskCategoryrequest) {
		List<TaskTypeDropDownEntity> list = new ArrayList<TaskTypeDropDownEntity>();
		String deptCode = taskCategoryrequest.getDeptCode();
		if (deptCode.equalsIgnoreCase("getall")) {
			deptCode = "%%";
		}
		try {
			String qry = "select TT_CODE,TT_DESC from task_type_mst where TENANT_ID=? and IS_ACTIVE = ? and DEPT_CODE like ?;";
			list = this.jdbcTemplate.query(qry, new TaskTypeDropDownRowMapper(),taskCategoryrequest.getTenantId(), "1", deptCode);
		}catch (Exception e) {
			logger.error("getTaskTypeDropDownIsActive DAO method error"+e);
		}
		return list;
	}

}
