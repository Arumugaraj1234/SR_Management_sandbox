package com.vmfg.master.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.master.dao.interfaces.ITaskTemplateDAO;
import com.vmfg.master.entity.TaskCategoryDrpdwnEntity;
import com.vmfg.master.entity.TemplateDtlMstEntity;
import com.vmfg.master.entity.TemplateTypeMstEntity;
import com.vmfg.master.rowmapper.TaskCategoryDrpdwnRowMapper;
import com.vmfg.master.rowmapper.TemplateTypeDtlRowMapper;
import com.vmfg.master.rowmapper.TemplateTypeRowMapper;

@Transactional
@Repository
public class TaskTemplateDAO implements ITaskTemplateDAO {
	private static final Logger logger = LoggerFactory.getLogger(TaskTemplateDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<TemplateTypeMstEntity> getTaskTypeTemplatedrpDwn(String deptCode, String ttCode, String tcCode,
			String tenantId) {
		List<TemplateTypeMstEntity> list= null;
		try {
			String query="SELECT \r\n" + 
					"    TT_HDR_ID, TT_NAME, IS_ACTIVE\r\n" + 
					"FROM\r\n" + 
					"    task_template_hdr\r\n" + 
					"WHERE\r\n" + 
					"    TT_DEPARTMENT_CODE = ?\r\n" + 
					"        AND TASK_TYPE_CODE = ?\r\n" + 
					"        AND TASK_CATEGORY_CODE = ? \r\n" + 
					"        AND TENANT_ID=? and IS_ACTIVE = 1";
			RowMapper<TemplateTypeMstEntity> rowmapper = new TemplateTypeRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper,deptCode, ttCode, tcCode, tenantId);	 
		} catch (Exception ex) {
			logger.error("getTaskTypeTemplatedrpDwn  method exception-->" + ex);
		}
		logger.debug("getTaskTypeTemplatedrpDwn  method end");
		return list;
	}

	@Override
	public List<TemplateDtlMstEntity> getTaskTemplatedtl(String ttHdrId, String tenantId, String isActive) {
		List<TemplateDtlMstEntity> list= null;
		try {
			String query="SELECT \r\n" + 
					"    TT_DTL_ID, ACTIVITY_NAME,tdtl.IS_ACTIVE, tdtl.LAST_UPDATED_DATETIME, tdtl.LAST_UPDATED_BY, em.EMPLOYEE_FIRSTNAME\r\n" + 
					"FROM\r\n" + 
					"    task_template_dtl tdtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_template_hdr thdr ON tdtl.TT_HDR_ID = thdr.TT_HDR_ID" +
					"   inner join employee_mst em on tdtl.LAST_UPDATED_BY = em.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    tdtl.TENANT_ID = ? AND thdr.TT_HDR_ID = ? and tdtl.IS_ACTIVE = ? ";
			RowMapper<TemplateDtlMstEntity> rowmapper = new TemplateTypeDtlRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper, tenantId, ttHdrId, isActive);	 
		} catch (Exception ex) {
			logger.error("getTaskTemplatedtl  method exception-->" + ex);
		}
		logger.debug("getTaskTemplatedtl  method end");
		return list;
	}

	@Override
	public List<TaskCategoryDrpdwnEntity> getTaskCategorydrpDwn(String ttCode, String tenantId) {
		List<TaskCategoryDrpdwnEntity> list= null;
		try {
			String query="SELECT \r\n" + 
					"    TC_CODE, TC_DESC\r\n" + 
					"FROM\r\n" + 
					"    task_category_mst\r\n" + 
					"WHERE\r\n" + 
					"    TT_CODE = ? AND TENANT_ID = ?\r\n" + 
					"        AND IS_ACTIVE = 1;";
			RowMapper<TaskCategoryDrpdwnEntity> rowmapper = new TaskCategoryDrpdwnRowMapper();	
			list=this.jdbcTemplate.query(query,rowmapper, ttCode, tenantId);	 
		} catch (Exception ex) {
			logger.error("getTaskCategorydrpDwn  method exception-->" + ex);
		}
		logger.debug("getTaskCategorydrpDwn  method end");
		return list;
	}

	@Override
	public int insertTaskTemplate(String ttHdrId, String actName, String isActive, String empId, String tenantId) {
		logger.debug("insertTaskTemplate   method Start");
		String finalActName = actName.equalsIgnoreCase("") ? "Enter Details" : actName;
		int insertRes = 0;
		try {
			String insertQ = "INSERT INTO task_template_dtl ( TT_HDR_ID, ACTIVITY_NAME, IS_ACTIVE, LAST_UPDATED_DATETIME, LAST_UPDATED_BY, TENANT_ID) \r\n" + 
					"					VALUES (?, ?, ?, NOW(), ?, ?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, ttHdrId);
					ps.setString(2, finalActName);
					ps.setString(3, isActive);
					ps.setString(4, empId);
					ps.setString(5, tenantId);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertTaskTemplate  method  exception" + ex);
		}
		logger.debug("insertTaskTemplate   method end");
		return insertRes;
	}

	@Override
	public int updateTaskTemplate(String ttDtlId, String actName, String ttHdrId, String isActive, String empId, String tenantId) {
		int update =0;
		try {
			String cnt = "select COUNT(TT_DTL_ID)as count FROM task_template_dtl WHERE TT_DTL_ID=? AND TENANT_ID=? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(cnt, ttDtlId, tenantId);
           int count = Integer.parseInt(resultMap.get("count").toString());
			if(count > 0) {
			String updtQry = "update task_template_dtl set ACTIVITY_NAME =?,  IS_ACTIVE=?,  LAST_UPDATED_DATETIME = NOW(), LAST_UPDATED_BY=?  where TT_DTL_ID=? AND TENANT_ID=?;";
			update = this.jdbcTemplate.update(updtQry, actName, isActive, empId, ttDtlId, tenantId );
			}
		}catch (Exception e) {
			logger.error("updateTaskTemplate DAO method error"+e);	
		}
		return update;
	}

	@Override
	public int insertTemplateHdr(String tempName, String empId, String deptCode, String ttCode, String tcCode,
			String tenantId, String isActive) {
		logger.debug("insertTemplateHdr   method Start");
//		String isActive = "1";
		int insertRes = 0;
		try {
			String insertQ = "  INSERT INTO task_template_hdr (TT_NAME, TT_CREATED_BY, TT_CREATED_ON, TT_DEPARTMENT_CODE, TASK_TYPE_CODE, TASK_CATEGORY_CODE, IS_ACTIVE, LAST_UPDATED_DATETIME, LAST_UPDATED_BY, TENANT_ID) "
					+ "values (?, ?, NOW(), ?, ?, ?, ?, NOW(), ?, ?); ";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, tempName);
					ps.setString(2, empId);
					ps.setString(3, deptCode);
					ps.setString(4, ttCode);
					ps.setString(5, tcCode);
					ps.setString(6, isActive);
					ps.setString(7, empId);
					ps.setString(8, tenantId);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertTemplateHdr method  exception" + ex);
		}
		logger.debug("insertTemplateHdr method end");
		return insertRes;
	}
}
