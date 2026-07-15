package com.vmfg.master.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.master.dao.interfaces.IProjectInitiationMasterDAO;
import com.vmfg.master.entity.ProjectInitiationDtlEntity;
import com.vmfg.master.rowmapper.ProjectInitiationDtlRowMapper;

@Transactional
@Repository
public class ProjectInitiationMasterDAO implements IProjectInitiationMasterDAO {
	private static final Logger logger = LoggerFactory.getLogger(ProjectInitiationMasterDAO.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ProjectInitiationDtlEntity> getProjectInitiationDtl(String tenantId) {
		List<ProjectInitiationDtlEntity> response = new ArrayList<ProjectInitiationDtlEntity>();
		try {
			
			String qry = "SELECT \r\n" + 
					"    PI_ID,\r\n" + 
					"    dep.DEPARTMENT_CODE,\r\n" + 
					"    dep.DEPARTMENT_NAME,\r\n" + 
					"    PRIMARY_POC,\r\n" + 
					"    MASTER_POC,\r\n" + 
					"    mst.DEPARTMENT_ASSIGNED\r\n"+
					"FROM\r\n" + 
					"    project_wbs_initiation_mst mst\r\n" + 
					"        INNER JOIN\r\n" + 
					"    department dep ON dep.DEPARTMENT_CODE = mst.DEPARTMENT_CODE where mst.TENANT_ID= ? ";
			
			response=this.jdbcTemplate.query(qry,new ProjectInitiationDtlRowMapper(), tenantId);	 
			
			
		}catch(Exception ex) {
			logger.error("getProjectInitiationDtl Method Exception "+ex);
		}
		return response;
	}

	@Override
	public int updateProjectIntiationMasterMethod(String piId, String primaryPoc, String masterPoc,String depAssignment) {
		int res=0;
		try {
			String updateQry="update project_wbs_initiation_mst SET PRIMARY_POC= ? ,MASTER_POC= ?,DEPARTMENT_ASSIGNED=?  where PI_ID= ? ";
			res = this.jdbcTemplate.update(updateQry,primaryPoc,masterPoc,depAssignment,piId);
		}catch(Exception ex) {
			logger.error("updateProjectIntiationMasterMethod Method Exception "+ex);
		}
		return res;
	}

	

	
}
