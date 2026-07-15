package com.vmfg.general.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

import com.vmfg.general.dao.interfaces.IDepartmentAndEmployeeDAO;
import com.vmfg.general.entity.DepartmentInfoEntity;
import com.vmfg.general.entity.DesignationEntity;
import com.vmfg.general.entity.DocDeptEntity;
import com.vmfg.general.entity.EmpRoleEntity;
import com.vmfg.general.entity.EmpStatusEntity;
import com.vmfg.general.entity.EmployeeForDepartmentEntity;
import com.vmfg.general.entity.EmployeeForUserDtl;
import com.vmfg.general.entity.EmployeemstdetailsEntity;
import com.vmfg.general.entity.ModuleMstEntity;
import com.vmfg.general.entity.ModuleScreenList;
import com.vmfg.general.entity.ProcessAssignedTeamEntity;
import com.vmfg.general.request.DeptByPMRequest;
import com.vmfg.general.request.EmployeeDetailsRequest;
import com.vmfg.general.response.EmployeemstdetailsRowMapper;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.rowmapper.DepartmentInfoRowMapper;
import com.vmfg.general.rowmapper.DeptByMDOCRowMapper;
import com.vmfg.general.rowmapper.EmpDesigRowMapper;
import com.vmfg.general.rowmapper.EmpRoleRowMapper;
import com.vmfg.general.rowmapper.EmpStatusRowMapper;
import com.vmfg.general.rowmapper.EmployeeForDepartmentRowMapper;
import com.vmfg.general.rowmapper.EmployeeUserRowMapper;
import com.vmfg.general.rowmapper.ModuleMstRowMapper;
import com.vmfg.general.rowmapper.ModuleScreenListRowMapper;
import com.vmfg.general.rowmapper.ProcessAssignedTeamRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class DepartmentAndEmployeeDAO implements IDepartmentAndEmployeeDAO {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentAndEmployeeDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<DepartmentInfoEntity> getDepartmentInfo(String tENANT_ID, String iS_ACTIVE, String EmployeeId) {
		List<DepartmentInfoEntity> list = new ArrayList<>();
		String isActive = "1";
		iS_ACTIVE = isActive;
		try {
			String query = "";
			RowMapper<DepartmentInfoEntity> rowmapper = new DepartmentInfoRowMapper();
			if (EmployeeId.equalsIgnoreCase("")) {
				query = "select DEPARTMENT_CODE,DEPARTMENT_NAME from department where TENANT_ID=?;";
				list = this.jdbcTemplate.query(query, rowmapper, tENANT_ID);
			} else {

				query = "SELECT \r\n" + "    dep.DEPARTMENT_CODE,\r\n" + "    dep.DEPARTMENT_NAME,\r\n"
						+ "    dep.DEPARTMENT_EMAIL,\r\n" + "    dep.DEPARTMENT_PHONE\r\n" + "FROM\r\n"
						+ "    department dep\r\n" + "        INNER JOIN\r\n"
						+ "    employee_mst mst ON mst.DEPARTMENT_CODE = dep.DEPARTMENT_CODE\r\n" + "WHERE\r\n"
						+ "    mst.TENANT_ID = ?\r\n" + "        AND dep.IS_ACTIVE = ?\r\n"
						+ "        AND mst.EMPLOYEE_ID = ?";
				list = this.jdbcTemplate.query(query, rowmapper, tENANT_ID, iS_ACTIVE, EmployeeId);
				if (list.size() > 0) {
					String getQ = "SELECT \r\n" + "    DEPARTMENT_ASSIGNED\r\n" + "FROM\r\n"
							+ "    project_wbs_initiation_mst\r\n" + "WHERE\r\n" + "    FIND_IN_SET(? , DEPARTMENT_ASSIGNED)";
					Map<String, Object> resultMap = this.jdbcTemplate.queryForMap(getQ, list.get(0).getDepartmentCode());
                    String deptCodes = resultMap.get("DEPARTMENT_ASSIGNED").toString();
					getQ = "SELECT * FROM department where  FIND_IN_SET(DEPARTMENT_CODE,'" + deptCodes
							+ "') and TENANT_ID=?";
					list = this.jdbcTemplate.query(getQ, rowmapper, tENANT_ID);

				}

			}

		} catch (Exception ex) {
			logger.error("getDepartmentInfo  method exception-->" + ex);
		}
		logger.debug("getDepartmentInfo  method end");

		return list;
	}

	@Override
	public List<DepartmentInfoEntity> getEmpInfo(String tENANT_ID, String iS_ACTIVE, String eMPLOYEE_ID) {
		List<DepartmentInfoEntity> list = new ArrayList<>();
		String isActive = "1";
		iS_ACTIVE = isActive;
		try {

			String query = "SELECT dept.DEPARTMENT_CODE,dept.DEPARTMENT_NAME FROM employee_mst AS em join department AS dept ON em.DEPARTMENT_CODE=dept.DEPARTMENT_CODE WHERE em.EMPLOYEE_ID=? AND dept.TENANT_ID=? AND dept.IS_ACTIVE=? ";
			RowMapper<DepartmentInfoEntity> rowmapper = new DepartmentInfoRowMapper();
			list = this.jdbcTemplate.query(query, rowmapper, eMPLOYEE_ID, tENANT_ID, iS_ACTIVE);

		} catch (Exception ex) {
			logger.error("getEmpInfo  method exception-->" + ex);
		}
		logger.debug("getEmpInfo  method end");

		return list;
	}

	@Override
	public List<ProcessAssignedTeamEntity> getprocessAssignedTeam(String tENANT_ID, String rEFERENCE_ID,
			String rEFERENCE_DOC) {
		List<ProcessAssignedTeamEntity> list = new ArrayList<>();

		try {

			String query = "SELECT \r\n"
					+ "    em.EMPLOYEE_FIRSTNAME,em.EMPLOYEE_ID, dept.DEPARTMENT_NAME, pst.IS_ACTIVE\r\n" + "FROM\r\n"
					+ "    process_assigned_team AS pst\r\n" + "        JOIN\r\n"
					+ "    employee_mst AS em ON em.EMPLOYEE_ID = pst.ASSIGNED_EMP_ID\r\n" + "        JOIN\r\n"
					+ "    department AS dept ON em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE\r\n" + "WHERE\r\n"
					+ "    pst.MASTER_ID = ?\r\n" + "        AND pst.PM_ID = ?\r\n"
					+ "        AND pst.TENANT_ID = ? and pst.IS_ACTIVE=? and em.EMPLOYMENT_STATUS_CODE='ES001'";
			RowMapper<ProcessAssignedTeamEntity> rowmapper = new ProcessAssignedTeamRowMapper();
			list = this.jdbcTemplate.query(query, rowmapper, rEFERENCE_ID, rEFERENCE_DOC, tENANT_ID, "1");

		} catch (Exception ex) {
			logger.error("getprocessAssignedTeam  method exception-->" + ex);
		}
		logger.debug("getprocessAssignedTeam  method end");

		return list;
	}

	public List<ProcessAssignedTeamEntity> getEmpIdByDesignation(String tENANT_ID, String designation) {
		List<ProcessAssignedTeamEntity> list = new ArrayList<>();

		try {

			String query = "SELECT \r\n" + "    em.EMPLOYEE_FIRSTNAME,em.EMPLOYEE_ID\r\n" + "FROM\r\n"
					+ "    employee_mst AS em \r\n" + "     WHERE\r\n" + "    em.DESIGNATION_CODE = ?\r\n" + "    \r\n"
					+ "        AND em.TENANT_ID = ?";
			RowMapper<ProcessAssignedTeamEntity> rowmapper = new ProcessAssignedTeamRowMapper();
			list = this.jdbcTemplate.query(query, rowmapper, designation, tENANT_ID);

		} catch (Exception ex) {
			logger.error("getEmpIdByDesignation  method exception-->" + ex);
		}
		logger.debug("getEmpIdByDesignation  method end");

		return list;
	}

	@Override
	public String getPrimaryDocFlagVal(String empId, String pmId,String tenantId) {
		String flagVal = "";
		try {
			String qry = "select  case when count(*)>0 then 1 else 0 end as VALUE  from project_wbs_initiation_mst where FIND_IN_SET(? ,MASTER_POC) and PM_ID = ? and TENANT_ID=?";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry, empId, pmId , tenantId);
			flagVal = result.get("VALUE").toString();

		} catch (Exception ex) {
			logger.error("getPrimaryDocFlagVal method Error" + ex);
		}
		return flagVal;

	}
	@Override
	public String getPrimaryVal(String empId, String pmId) {
		String flagVal = "";
		try {
			String qry = "select  case when count(*)>0 then 1 else 0 end as VALUE from project_wbs_initiation_mst where FIND_IN_SET(? ,PRIMARY_POC) and PM_ID = ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry, empId, pmId);
			flagVal = result.get("VALUE").toString();

		} catch (Exception ex) {
			logger.error("getPrimaryDocFlagVal method Error" + ex);
		}
		return flagVal;

	}

	@Override
	public ResponseAsMessage insertProcessAssignedTeam(String tENANT_ID, String rEFERENCE_ID, String rEFERENCE_DOC,
			String aSSIGNED_EMP_ID, String projectId) {
		ResponseAsMessage resp = new ResponseAsMessage();
		String isActive = "1";
		try {

			String recordCheckQuery = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(PA_ID) > 0 THEN PA_ID\r\n"
					+ "        ELSE 0\r\n" + "    END AS PA_ID\r\n" + " FROM\r\n" + "    process_assigned_team\r\n"
					+ "WHERE\r\n" + "    TENANT_ID = ? \r\n" + "        AND ASSIGNED_EMP_ID = ? \r\n" + "        AND PM_ID = ? \r\n"
					+ "        AND MASTER_ID = ? ";

			Map<String, Object> result = this.jdbcTemplate.queryForMap(recordCheckQuery,tENANT_ID,aSSIGNED_EMP_ID,rEFERENCE_DOC,rEFERENCE_ID );
			String paId = result.get("PA_ID").toString();

			if (paId.equalsIgnoreCase("0")) {
				String inquery = "insert into process_assigned_team (MASTER_ID,PM_ID,ASSIGNED_EMP_ID,TENANT_ID,IS_ACTIVE,PROJECT_ID) values(?,?,?,?,?,?)";

				KeyHolder holder = new GeneratedKeyHolder();

				this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

						ps.setString(1, rEFERENCE_ID);
						ps.setString(2, rEFERENCE_DOC);
						ps.setString(3, aSSIGNED_EMP_ID);
						ps.setString(4, tENANT_ID);
						ps.setString(5, isActive);
						ps.setString(6, projectId);

						return ps;
					}

				}, holder);
				int insertRes = holder.getKey().intValue();

				if (insertRes > 0) {
					resp.setResponseCode(ResponseMessageMap.responseCodeOk);
					resp.setResponseMessage(ResponseMessageMap.successInserted);

				}
			} else {
				String checkIsActive = "select CASE WHEN IS_ACTIVE THEN 1 ELSE 0 END AS IS_ACTIVE from process_assigned_team where PA_ID= ? ";
				Map<String, Object> results = this.jdbcTemplate.queryForMap(checkIsActive, paId);
				int getIsActive = Integer.parseInt(results.get("IS_ACTIVE").toString());
				
				if (getIsActive == 0) {
					String updateQry = "UPDATE process_assigned_team SET IS_ACTIVE='1' WHERE PA_ID=?";
					this.jdbcTemplate.update(updateQry,paId);
					resp.setResponseCode(ResponseMessageMap.responseCodeOk);
					resp.setResponseMessage(ResponseMessageMap.successInserted);

				} else {
					resp.setResponseCode(ResponseMessageMap.responseAlreadyExists);
					resp.setResponseMessage(ResponseMessageMap.alreadyExist);
				}

			}

		} catch (Exception ex) {
			logger.error("insertProcessAssignedTeam DAO method exception-->" + ex);
		}
		logger.debug("insertProcessAssignedTeam DAO method ends");
		return resp;
	}

	@Override
	public ResponseAsMessage deleteProcessAssignedTeam(String tENANT_ID, String rEFERENCE_ID, String rEFERENCE_DOC,
			String aSSIGNED_EMP_ID) {

		ResponseAsMessage resp = new ResponseAsMessage();
		int rs = 0;
		try {

			if ((null != tENANT_ID && !tENANT_ID.isEmpty()) && !rEFERENCE_DOC.isEmpty() && !rEFERENCE_ID.isEmpty()
					&& !aSSIGNED_EMP_ID.isEmpty()) {
				String query = "Update process_assigned_team set IS_ACTIVE=? WHERE MASTER_ID=? AND PM_ID=? AND ASSIGNED_EMP_ID=? AND TENANT_ID=?";
				rs = jdbcTemplate.update(query, 0, rEFERENCE_ID, rEFERENCE_DOC, aSSIGNED_EMP_ID, tENANT_ID);
			} else {

				resp.setResponseMessage(ResponseMessageMap.NoData);
			}
			if (rs != 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.successfulDeleted);
			} else {
				resp.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
			}
		} catch (Exception ex) {
			logger.error("deleteProcessAssignedTeam  method exception-->" + ex);
		}
		logger.debug("deleteProcessAssignedTeam  method end");

		return resp;
	}

	@Override
	public List<EmployeeForDepartmentEntity> getEmployeeForDepartment(String tENANT_ID, String departmentId,
			String empId ) {
		ResponseAsMessage resp = new ResponseAsMessage();
		List<EmployeeForDepartmentEntity> list = new ArrayList<>();
		try {
			
			if(empId !=null && !empId.isEmpty()) {	
			if (departmentId.isEmpty()) {
				String getQ = "select DEPARTMENT_CODE from employee_mst where EMPLOYEE_ID= ? and TENANT_ID=?";
				Map<String, Object> result = this.jdbcTemplate.queryForMap(getQ,empId,tENANT_ID);
				departmentId = result.get("DEPARTMENT_CODE").toString();
			}
		}

			if ((null != tENANT_ID && !tENANT_ID.isEmpty()) && !departmentId.isEmpty() && !departmentId.isEmpty()) {
				String query = "SELECT\r\n" + 
						"    mst.EMPLOYEE_ID,\r\n" + 
						"    mst.EMPLOYEE_FIRSTNAME,\r\n" + 
						"    em.EMPLOYEE_STATUS_NAME\r\n" + 
						"FROM\r\n" + 
						"    employee_mst mst\r\n" + 
						"    INNER JOIN department dep ON dep.DEPARTMENT_CODE = mst.DEPARTMENT_CODE\r\n" + 
						"    INNER JOIN employment_status_mst em ON em.EMPLOYMENT_STATUS_CODE = mst.EMPLOYMENT_STATUS_CODE\r\n" + 
						"WHERE\r\n" + 
						"    dep.DEPARTMENT_CODE = ?\r\n" + 
						"    AND mst.TENANT_ID = ?\r\n" + 
						"    AND mst.EMPLOYMENT_STATUS_CODE IN ('ACTIVE', 'ES001');";
				RowMapper<EmployeeForDepartmentEntity> rowmapper = new EmployeeForDepartmentRowMapper();
				list = this.jdbcTemplate.query(query, rowmapper, departmentId, tENANT_ID);
			} else {

				resp.setResponseMessage(ResponseMessageMap.NoData);
			}
		} catch (Exception ex) {
			logger.error("getEmployeeForDepartment  method exception-->" + ex);
		}
		logger.debug("getEmployeeForDepartment  method end");

		return list;
	}

	public List<EmployeeForDepartmentEntity> getEmployeeForDesignation(String tENANT_ID, String designation) {
		ResponseAsMessage resp = new ResponseAsMessage();
		List<EmployeeForDepartmentEntity> list = new ArrayList<>();
		try {

			if ((null != tENANT_ID && !tENANT_ID.isEmpty()) && !designation.isEmpty() && !designation.isEmpty()) {
				String query = "SELECT \r\n" + "    mst.EMPLOYEE_ID, mst.EMPLOYEE_FIRSTNAME\r\n" + "FROM\r\n"
						+ "    employee_mst mst\r\n" + "    \r\n" + "    \r\n" + "WHERE\r\n"
						+ "    mst.DESIGNATION_CODE = ?\r\n" + "        AND mst.TENANT_ID = ?";
				RowMapper<EmployeeForDepartmentEntity> rowmapper = new EmployeeForDepartmentRowMapper();
				list = this.jdbcTemplate.query(query, rowmapper, designation, tENANT_ID);
			} else {

				resp.setResponseMessage(ResponseMessageMap.NoData);
			}
		} catch (Exception ex) {
			logger.error("getEmployeeForDesignation  method exception-->" + ex);
		}
		logger.debug("getEmployeeForDesignation  method end");

		return list;
	}

	@Override
	public int getAssignTeamCount(String tENANT_ID, String rEFERENCE_ID, String rEFERENCE_DOC) {
		int count = 0;
		try {
			String qry = "select count(*) as VALUE from process_assigned_team where MASTER_ID= ? and PM_ID= ? and TENANT_ID= ? and IS_ACTIVE='1'";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,rEFERENCE_ID,rEFERENCE_DOC,tENANT_ID);
			count = Integer.parseInt(result.get("VALUE").toString());

		} catch (Exception ex) {
			logger.error("getAssignTeamCount Method Exception --->" + ex);

		}
		return count;
	}

	@Override
	public int getMasterAssignTeam(String ASSIGNED_EMP_ID, String TENANT_ID) {
		int count = 0;
		try {
			String qry = "select count(*) as VALUE from project_wbs_initiation_mst where FIND_IN_SET (?,PRIMARY_POC) and TENANT_ID = ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,ASSIGNED_EMP_ID,TENANT_ID);
			count = Integer.parseInt(result.get("VALUE").toString());

		} catch (Exception ex) {
			logger.error("getMasterAssignTeam Method Exception --->" + ex);

		}
		return count;
	}

	@Override
	public List<DepartmentInfoEntity> getDeptForPM(DeptByPMRequest deptInfoId) {
		List<DepartmentInfoEntity> deptInfo = null;
		try {
			String qry = "SELECT \r\n"
					+ "    GROUP_CONCAT(dm.DEPARTMENT_NAME order by dm.DEPARTMENT_CODE) DEPARTMENT_NAME ,  GROUP_CONCAT(dm.DEPARTMENT_CODE order by dm.DEPARTMENT_CODE) DEPARTMENT_CODE\r\n"
					+ "FROM\r\n" + "    project_wbs_initiation_mst pm\r\n" + "        INNER JOIN\r\n"
					+ "    department dm ON FIND_IN_SET(dm.DEPARTMENT_CODE,\r\n"
					+ "            pm.DEPARTMENT_ASSIGNED) > 0 where  PM_ID = ? and dm.DEPARTMENT_CODE = ? and dm.TENANT_ID = ?\r\n"
					+ "GROUP BY pm.DEPARTMENT_CODE";
			RowMapper<DepartmentInfoEntity> rowmapper = new DepartmentInfoRowMapper();
			deptInfo = this.jdbcTemplate.query(qry, rowmapper, deptInfoId.getPmId(), deptInfoId.getDeptCode(), deptInfoId.getTenantId());

		} catch (Exception ex) {
			logger.error("getDeptForPM Method Exception --->" + ex);

		}
		return deptInfo;
	}

	@Override
	public List<DocDeptEntity> getDeptForDesig(String dept, String tenantId,String designation) {
		List<DocDeptEntity> returnList = null;
		try {
			String getQ = "SELECT DISTINCT\r\n" + 
					"    DEPARTMENT_ASSIGNED AS DEPARTMENT_CODE\r\n" + 
					"FROM\r\n" + 
					"    project_wbs_initiation_mst\r\n" + 
					"WHERE\r\n" + 
					"    DEPARTMENT_CODE = ?\r\n" + 
					"        AND TENANT_ID = ?\r\n" + 
					"        AND FIND_IN_SET('"+designation+"', MASTER_POC)";
			RowMapper<DocDeptEntity> rowmapper = new DeptByMDOCRowMapper();
			returnList = this.jdbcTemplate.query(getQ, rowmapper, dept, tenantId);
		} catch (Exception ex) {
			logger.error("getDeptForDesig Method Exception --->" + ex);

		}
		return returnList;
	}


	@Override
	public List<EmployeeForUserDtl> getEmployeeUserDtlForDeptByDept(String deptCode, String tenantId) {
		List<EmployeeForUserDtl> returnList = null;

		try {
			String getQ = "SELECT \r\n" + "  EMPLOYEE_CODE,  em.EMPLOYEE_ID,\r\n" + "    em.EMPLOYEE_FIRSTNAME,em.CLIENT_DESIGNATION,\r\n"
					+ "    em.EMAIL_ID,\r\n" + "    es.EMPLOYEE_STATUS_NAME,\r\n" + "    ed.DESIGNATION_NAME,\r\n"
					+ "    ul.userName,\r\n" + "    ur.ROLE_DESCRIPTION,\r\n" + "    em.DEPARTMENT_CODE,\r\n"
					+ "    em.DESIGNATION_CODE,\r\n" + "    ur.USER_ROLE_ID,\r\n" + "    em.EMPLOYMENT_STATUS_CODE,\r\n"
					+ "    dept.DEPARTMENT_NAME,em.TENANT_ID AS TENANT_ID \r\n" + "FROM\r\n" + "    employee_mst em,\r\n"
					+ "    user_login ul,\r\n" + "    employment_status_mst es,\r\n"
					+ "    employee_designation ed,\r\n" + "    user_role ur,\r\n" + "    user_role_mapping urm,\r\n"
					+ "    department dept\r\n" + "WHERE\r\n" + "    em.EMPLOYEE_ID = ul.EMPLOYEE_ID\r\n"
					+ "        AND em.DESIGNATION_CODE = ed.DESIGNATION_CODE\r\n"
					+ "        AND em.EMPLOYMENT_STATUS_CODE = es.EMPLOYMENT_STATUS_CODE\r\n"
					+ "        AND urm.USER_LOGINID = ul.USER_LOGIN_ID\r\n"
					+ "        AND ur.USER_ROLE_ID = urm.USER_ROLEID\r\n"
					+ "        AND em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE\r\n"
					+ "        AND em.DEPARTMENT_CODE IN (" + deptCode + ")\r\n" + "        AND em.TENANT_ID = ?";
			RowMapper<EmployeeForUserDtl> rowmapper = new EmployeeUserRowMapper();
			returnList = this.jdbcTemplate.query(getQ, rowmapper, tenantId);
		} catch (Exception ex) {
			logger.error("getEmployeeUserDtlForDeptByEmpId Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public List<EmployeeForUserDtl> getEmployeeUserDtlForDeptByEmpId(String employeeId, String tenantId) {
		List<EmployeeForUserDtl> returnList = null;

		try {
			String getQ = "SELECT \r\n" + "  EMPLOYEE_CODE,  em.EMPLOYEE_ID,\r\n" + "    em.EMPLOYEE_FIRSTNAME,em.CLIENT_DESIGNATION,\r\n"
					+ "    em.EMAIL_ID,\r\n" + "    es.EMPLOYEE_STATUS_NAME,\r\n" + "    ed.DESIGNATION_NAME,\r\n"
					+ "    ul.userName,\r\n" + "    ur.ROLE_DESCRIPTION,\r\n" + "    em.DEPARTMENT_CODE,\r\n"
					+ "    em.DESIGNATION_CODE,\r\n" + "    ur.USER_ROLE_ID,\r\n" + "    em.EMPLOYMENT_STATUS_CODE,\r\n"
					+ "    dept.DEPARTMENT_NAME,em.TENANT_ID AS TENANT_ID \r\n" + "FROM\r\n" + "    employee_mst em,\r\n"
					+ "    user_login ul,\r\n" + "    employment_status_mst es,\r\n"
					+ "    employee_designation ed,\r\n" + "    user_role ur,\r\n" + "    user_role_mapping urm,\r\n"
					+ "    department dept\r\n" + "WHERE\r\n" + "    em.EMPLOYEE_ID = ul.EMPLOYEE_ID\r\n"
					+ "        AND em.DESIGNATION_CODE = ed.DESIGNATION_CODE\r\n"
					+ "        AND em.EMPLOYMENT_STATUS_CODE = es.EMPLOYMENT_STATUS_CODE\r\n"
					+ "        AND urm.USER_LOGINID = ul.USER_LOGIN_ID\r\n"
					+ "        AND ur.USER_ROLE_ID = urm.USER_ROLEID\r\n"
					+ "        AND em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE\r\n" + "        AND em.EMPLOYEE_ID = ?\r\n"
					+ "        AND em.TENANT_ID = ?";
			RowMapper<EmployeeForUserDtl> rowmapper = new EmployeeUserRowMapper();
			returnList = this.jdbcTemplate.query(getQ, rowmapper, employeeId, tenantId);
		} catch (Exception ex) {
			logger.error("getEmployeeUserDtlForDeptByEmpId Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public List<DesignationEntity> getEmpDesignation(String tenantId) {
		List<DesignationEntity> returnList = null;

		try {
			String getQ = " select * from employee_designation where TENANT_ID = ?";
			RowMapper<DesignationEntity> rowmapper = new EmpDesigRowMapper();
			returnList = this.jdbcTemplate.query(getQ, rowmapper, tenantId);
		} catch (Exception ex) {
			logger.error("getEmpDesignation Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public List<EmpStatusEntity> getEmpStatus(String tenantId) {
		List<EmpStatusEntity> returnList = null;

		try {
			String getQ = " select * from employment_status_mst";
			RowMapper<EmpStatusEntity> rowmapper = new EmpStatusRowMapper();
			returnList = this.jdbcTemplate.query(getQ, rowmapper);
		} catch (Exception ex) {
			logger.error("getEmpDesignation Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public List<EmpRoleEntity> getEmpRole(String tenantId) {
		List<EmpRoleEntity> returnList = null;

		try {
			String getQ = " select * from user_role where TENANT_ID = ?";
			RowMapper<EmpRoleEntity> rowmapper = new EmpRoleRowMapper();
			returnList = this.jdbcTemplate.query(getQ, rowmapper, tenantId);
		} catch (Exception ex) {
			logger.error("getEmpDesignation Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public void updateEmployeeDtl(EmployeeDetailsRequest empDtl) {

		try {
			String upQ = "UPDATE employee_mst SET DESIGNATION_CODE= ?, EMPLOYEE_FIRSTNAME= ? , EMPLOYMENT_STATUS_CODE= ?, DEPARTMENT_CODE= ?,EMAIL_ID= ? ,EMPLOYEE_CODE =?,CLIENT_DESIGNATION=? WHERE EMPLOYEE_ID= ? ";
			this.jdbcTemplate.update(upQ,empDtl.getDesignation(),empDtl.getEmpName(),empDtl.getStatus(),empDtl.getDepartment(),empDtl.getEmailId(),empDtl.getEmpCode(),empDtl.getClientDesign(),empDtl.getEmpId());
		} catch (Exception ex) {
			logger.error("updateEmployeeDtl Method Exception --->" + ex);

		}

	}

	@Override
	public void updateEmployeePwd(String empId, String encPassword) {
		try {
			String upQ = "update user_login set userPassword = ? where EMPLOYEE_ID =? and  USER_LOGIN_ID>0";
			this.jdbcTemplate.update(upQ,encPassword,empId);
		} catch (Exception ex) {
			logger.error("updateEmployeePwd Method Exception --->" + ex);

		}

	}

	@Override
	public String getUserLoginId(String empId) {
		String userId = "";
		try {
			String getQ = "select USER_LOGIN_ID from user_login where EMPLOYEE_ID= ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getQ,empId);
			userId = result.get("USER_LOGIN_ID").toString();
		} catch (Exception ex) {
			logger.error("getUserLoginId Method Exception --->" + ex);

		}
		return userId;
	}

	@Override
	public void updateUserLogin(String empId, String role, String userLoginId, String tenantId) {
		try {
			String upQ = "update user_role_mapping set USER_ROLEID = ? where USER_LOGINID = ? and USER_ROLER_MST_ID>=0";
			this.jdbcTemplate.update(upQ,role,userLoginId);
		} catch (Exception ex) {
			logger.error("updateUserLogin Method Exception --->" + ex);

		}

	}

	@Override
	public void updateuserLoginAsInactive(String userLoginId, String state,String mailId) {
		try {
			String upQ = "update user_login set IS_ACTIVE = ?, userName = ? where USER_LOGIN_ID = ?";
			this.jdbcTemplate.update(upQ,state,mailId,userLoginId);
		} catch (Exception ex) {
			logger.error("getEmpDesignation Method Exception --->" + ex);

		}

	}

	@Override
	public String getEmpStatusByEmpId(String empId, int cnt, String tenantId) {
		String empStat = "";
		try {
			String getQ = "SELECT EMPLOYMENT_STATUS_CODE FROM employee_mst where EMPLOYEE_ID= ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getQ,empId);
			empStat = result.get("EMPLOYMENT_STATUS_CODE").toString();
			if (!empStat.equalsIgnoreCase("ES001")) {
				String configUserCount = GetPropertyValue.getPropValueByTenant(tenantId, "MAX_USER_COUNT",
						this.jdbcTemplate);
				if (Integer.parseInt(configUserCount) == cnt) {
					empStat = "NR";
				}

			}

		} catch (Exception ex) {
			logger.error("getEmpStatusByEmpId Method Exception --->" + ex);

		}
		return empStat;

	}

	@Override
	public int getActiveUser(String tenantId) {
		int userCnt = 0;
		try {
			String getQ = "select count(*) as VALUE from user_login where TENANT_ID= ? and IS_ACTIVE=1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(getQ,tenantId);
			userCnt = Integer.parseInt(result.get("VALUE").toString());

		} catch (Exception ex) {
			logger.error("getEmpStatusByEmpId Method Exception --->" + ex);

		}
		return userCnt;
	}

	@Override
	public List<ModuleMstEntity> getModuleMstDtlList(String tenantId) {
		List<ModuleMstEntity> result = new ArrayList<ModuleMstEntity>();
		try {
			String resultQry = "select UI_MODULE_MST_ID,DESCRIPTION from ui_module_mst where IS_ACTIVE='1' and TENANT_ID=?;\r\n";
			result = this.jdbcTemplate.query(resultQry, new ModuleMstRowMapper(), tenantId);
		} catch (Exception ex) {
			logger.error("getModuleMstDtlList error " + ex);
		}
		return result;
	}

	@Override
	public List<ModuleScreenList> getModuleBasedScreenDtlList(String tenantId, String moduleId, String roleId) {
		// TODO Auto-generated method stub
		List<ModuleScreenList> result = null;
		try {
			String ScreenList = "SELECT \r\n" + "    us.UI_SCREEN_MST_ID,\r\n"
					+ "    um.DESCRIPTION AS MODULE_DESCRIPTION,\r\n" + "    us.DESCRIPTION AS SCREEN_DESCRIPTION,\r\n"
					+ "    us.DISPLAY_NAME AS SCREEN_DISPLAY_NAME,\r\n" + "    (SELECT \r\n" + "            CASE\r\n"
					+ "                    WHEN COUNT(*) > 0 THEN 1\r\n" + "                    ELSE 0\r\n"
					+ "                END IS_Active\r\n" + "        FROM\r\n"
					+ "            ui_role_screen_mapping\r\n" + "        WHERE\r\n" + "            USER_ROLE_ID = '"
					+ roleId + "'\r\n" + "                AND UI_SCREEN_MST_ID = us.UI_SCREEN_MST_ID) AS IS_ACTIVE\r\n"
					+ "FROM\r\n" + "    ui_screen_mst us\r\n" + "        INNER JOIN\r\n"
					+ "    ui_module_mst um ON um.UI_MODULE_MST_ID = us.UI_MODULE_MST_ID\r\n" + "WHERE\r\n"
					+ "    us.UI_MODULE_MST_ID = '" + moduleId + "'\r\n" + "        AND us.TENANT_ID = '" + tenantId
					+ "'\r\n" + "        AND us.IS_ACTIVE = '1';";
			result = this.jdbcTemplate.query(ScreenList, new ModuleScreenListRowMapper());
		} catch (Exception ex) {
			logger.error("getModuleBasedScreenDtlList error " + ex);
		}
		return result;
	}

	@Override
	public int getCount(String roleId, String uiScreenMstId, String tenantId) {
		// TODO Auto-generated method stub
		int countCheck = 0;

		try {
			String CountQry = "Select count(*) as VALUE from ui_role_screen_mapping where USER_ROLE_ID= ? and "
					+ "UI_SCREEN_MST_ID= ? and TENANT_ID= ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(CountQry,roleId,uiScreenMstId,tenantId);
			countCheck = Integer.parseInt(result.get("VALUE").toString());
		} catch (Exception ex) {
			logger.error("getCount error " + ex);

		}
		return countCheck;
	}

	@Override
	public int InsertNewRoleEntry(String roleId, String uiScreenMstId, String tenantId) {
		int insert = 0;
		try {
			String InsQry = "INSERT INTO `ui_role_screen_mapping`\r\n" + "(\r\n" + "`USER_ROLE_ID`,\r\n"
					+ "`UI_SCREEN_MST_ID`,\r\n" + "`TENANT_ID`)\r\n" + "VALUES\r\n" + "(?,?,?);";
			insert = this.jdbcTemplate.update(InsQry, roleId, uiScreenMstId, tenantId);

		} catch (Exception ex) {
			logger.error("InsertNewRoleEntry error " + ex);
		}
		return insert;
	}

	@Override
	public int deleteRoleEntry(String roleId, String uiScreenMstId, String tenantId) {
		// TODO Auto-generated method stub
		int deleteCheck = 0;
		try {
			String DeleteQry = "DELETE FROM `ui_role_screen_mapping`\r\n"
					+ "WHERE USER_ROLE_ID=? and UI_SCREEN_MST_ID=? and TENANT_ID=?;";
			deleteCheck = this.jdbcTemplate.update(DeleteQry, roleId, uiScreenMstId, tenantId);

		} catch (Exception ex) {
			logger.error("deleteRoleEntry error " + ex);
		}
		return deleteCheck;
	}

	@Override
	public String getTotalEmpCount(int userCnt, String tenantId) {
		String empCheck = "";
		try {
			String configUserCount = GetPropertyValue.getPropValueByTenant(tenantId, "MAX_USER_COUNT",
					this.jdbcTemplate);
			if (Integer.parseInt(configUserCount) == userCnt) {
				empCheck = "NR";
			}

		} catch (Exception ex) {
			logger.error("getTotalEmpCount error " + ex);
		}
		return empCheck;
	}

	@Override
	public String insertNewEmpId(String department, String designation, String empId, String empName, String status,
			String tenantId, String newEmpId, String emailId,String clientDesign) {
		// TODO Auto-generated method stub
		String insertedEmpId = "";
		try {
			String insQry = "INSERT INTO `employee_mst`\r\n" + "(`EMPLOYEE_ID`,\r\n" + "`DESIGNATION_CODE`,\r\n"
					+ "`EMPLOYEE_FIRSTNAME`,\r\n" + "`EMPLOYMENT_STATUS_CODE`,\r\n" + "`DEPARTMENT_CODE`,\r\n"
					+ "`LAST_UPDATED_DATETIME`,\r\n" + "`TENANT_ID`,`EMAIL_ID`,EMPLOYEE_CODE,CLIENT_DESIGNATION)\r\n" + "VALUES\r\n"
					+ "(?,?,?,?,?,?,?,?,?,?);";
			
			int empIns = this.jdbcTemplate.update(insQry, newEmpId, designation, empName, status, department,
					CommonMethod.getCurrentDateTime(), tenantId, emailId, empId,clientDesign);
			
			if(empIns>0) {
				String fetchQuery  = "SELECT EMPLOYEE_ID \r\n" +
					    "FROM employee_mst \r\n" +
					    "ORDER BY EMPLOYEE_ID DESC \r\n" +
					    "LIMIT 1;";
				
				insertedEmpId = this.jdbcTemplate.queryForObject(fetchQuery , String.class);
			}
		} catch (Exception ex) {
			logger.error("insertNewEmpId error " + ex);
		}
		return insertedEmpId;
	}

	@Override
	public String getNewEmpId(String tenantId) {
		// TODO Auto-generated method stub
		String empId = "";
		try {
			String maxEmpquery = "select EMPLOYEE_ID from employee_mst order by EMPLOYEE_ID desc limit 1";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(maxEmpquery);
			String empid = result.get("EMPLOYEE_ID").toString();
			empId = CommonMethod.getEmployeeNewCode("E", empid);

		} catch (Exception ex) {
			logger.error("getNewEmpId error " + ex);
		}
		return empId;
	}

	@Override
	public int insertNewUserLogin(String empName, String encPassword, String newEmployeeId, String tenantId) {
		// TODO Auto-generated method stub
		int insertRes = 0;
		try {

			String inquery = "INSERT INTO `user_login`\r\n" + "(\r\n" + "`userName`,\r\n" + "`userPassword`,\r\n"
					+ "`EMPLOYEE_ID`,\r\n" + "`IS_ACTIVE`,\r\n" + "`TENANT_ID`)\r\n" + "VALUES\r\n" + "(?,?,?,?,?);";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, empName);
					ps.setString(2, encPassword);
					ps.setString(3, newEmployeeId);
					ps.setString(4, "1");
					ps.setString(5, tenantId);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();
		} catch (Exception ex) {
			logger.error("insertNewUserLogin error " + ex);
		}

		return insertRes;
	}

	@Override
	public String insertUserRoleMapping(int userLoginId, String role, String tenantId) {
		// TODO Auto-generated method stub
		String result = "";
		try {

			String inquery = "INSERT INTO `user_role_mapping`\r\n" + "(\r\n" + "`USER_LOGINID`,\r\n"
					+ "`USER_ROLEID`,\r\n" + "`IS_ACTIVE`,\r\n" + "`TENANT_ID`)\r\n" + "VALUES\r\n" + "(?,?,?,?);";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

					ps.setInt(1, userLoginId);
					ps.setString(2, role);
					ps.setString(3, "1");
					ps.setString(4, tenantId);

					return ps;
				}

			}, holder);
			int insertRes = holder.getKey().intValue();
			if (insertRes > 0) {
				result = "Success";
			}

		} catch (Exception ex) {
			logger.error("insertUserRoleMapping error " + ex);
		}
		return result;
	}
	
	@Override
	public int countProcessAssign(String empId, String tenantId) {
		int val = 0;
		try {
			String updquery = "SELECT CASE \r\n" + 
					"      WHEN COUNT(*) > 0 THEN 1\r\n" + 
					"      ELSE 0\r\n" + 
					"      END AS isVal\r\n" + 
					"FROM process_assigned_team\r\n" + 
					"WHERE ASSIGNED_EMP_ID = ?\r\n" + 
					"  AND TENANT_ID = ?;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(updquery, empId, tenantId);
	        val = Integer.parseInt(resultMap.get("isVal").toString());
	} catch (Exception ex) {
		logger.error("countProcessAssign error " + ex);
	 }
	return val;
  }
			
	@Override
	public int insertUserProcessAssign(String fromEmpId, String toEmpId, String tenantId) {
		int insert = 0;
		try {
			String qry = "INSERT INTO process_assigned_team (\r\n" +
					"  MASTER_ID,\r\n" +
					"  PM_ID,\r\n" +
					"  PROJECT_ID,\r\n" +
					"  ASSIGNED_EMP_ID,\r\n" +
					"  IS_ACTIVE,\r\n" +
					"  TENANT_ID\r\n" +
					")\r\n" +
					"SELECT\r\n" +
					"  MASTER_ID,\r\n" +
					"  PM_ID,\r\n" +
					"  PROJECT_ID,\r\n" +
					"  ? AS ASSIGNED_EMP_ID,\r\n" +
					"  IS_ACTIVE,\r\n" +
					"  TENANT_ID\r\n" +
					"FROM process_assigned_team\r\n" +
					"WHERE ASSIGNED_EMP_ID = ?\r\n" +
					"  AND TENANT_ID = ?;";
			insert = this.jdbcTemplate.update(qry, toEmpId, fromEmpId, tenantId);
		} catch (Exception ex) {
			logger.error("insertUserProcessAssign error " + ex);
		}
		return insert;
	}
	
	@Override
	public int countHodIndentDetail(String empId, String tenantId) {
		int val = 0;
		try {
			String updquery = "SELECT \r\n" +
					"  CASE \r\n" +
					"    WHEN COUNT(*) > 0 THEN 1  \r\n" +
					"    ELSE 0                    \r\n" +
					"  END AS isVal   \r\n" +
					"FROM indent_assign_team iat\r\n" +
					"JOIN employee_mst e \r\n" +
					"  ON iat.EMPLOYEE_ID = e.EMPLOYEE_ID   \r\n" +
					"  AND e.EMPLOYEE_ID = ?  \r\n" +
					"  AND e.TENANT_ID = ?;";

			Map<String, Object> resultMap = jdbcTemplate.queryForMap(updquery, empId, tenantId);
	        val = Integer.parseInt(resultMap.get("isVal").toString());
		} catch (Exception ex) {
			logger.error("insertHodIndentDetail error " + ex);
		}
		return val;
	}
	
	@Override
	public int insertUserIndentAssign(String fromEmpId, String toEmpId, String tenantId) {
		int insert = 0;
		try {
        String qry = "INSERT INTO indent_assign_team ( \r\n" + 
        		"					  INDENT_DTL_ID, \r\n" +
        		"                     EMPLOYEE_ID,    \r\n"+
        		"					  IS_PRIMARY, \r\n" + 
        		"					  TENANT_ID \r\n" + 
        		"					) \r\n" + 
        		"					SELECT \r\n" + 
        		"					  INDENT_DTL_ID, \r\n" + 
        		"					  ? AS EMPLOYEE_ID, \r\n" + 
        		"					  1 AS IS_PRIMARY, \r\n" + 
        		"					  TENANT_ID \r\n" + 
        		"					FROM indent_assign_team \r\n" + 
        		"					WHERE EMPLOYEE_ID = ? \r\n" + 
        		"					  AND TENANT_ID = ?;";
        insert = this.jdbcTemplate.update(qry, toEmpId, fromEmpId, tenantId);
	} catch (Exception ex) {
		logger.error("insertUserIndentAssign error " + ex);
	}
	return insert;
	}
	
	@Override
	public int insertExistUserProcessAssign(String fromEmpId, String toEmpId, String tenantId) {
	    int insert = 0;
	    try {
	        String archiveQuery = "UPDATE process_assigned_team SET TENANT_ID = 'bgrn1' " +
	                              "WHERE ASSIGNED_EMP_ID = ? AND TENANT_ID = ?";
	        jdbcTemplate.update(archiveQuery, toEmpId, tenantId);

	        String insertQuery = "INSERT INTO process_assigned_team (MASTER_ID, PM_ID, PROJECT_ID, ASSIGNED_EMP_ID, IS_ACTIVE, TENANT_ID) " +
	                             "SELECT MASTER_ID, PM_ID, PROJECT_ID, ?, IS_ACTIVE, TENANT_ID " +
	                             "FROM process_assigned_team " +
	                             "WHERE ASSIGNED_EMP_ID = ? AND TENANT_ID = ?";
	        insert = jdbcTemplate.update(insertQuery, toEmpId, fromEmpId, tenantId);
	    } catch (Exception ex) {
	        logger.error("insertUserProcessAssign error " + ex);
	    }
	    return insert;
	}

	@Override
	public int insertExistUserIndentAssign(String fromEmpId, String toEmpId, String tenantId) {
	    int insert = 0;
	    try {
	        String archiveQuery = "UPDATE indent_assign_team SET TENANT_ID = 'bgrn1' " +
	                              "WHERE EMPLOYEE_ID = ? AND TENANT_ID = ?";
	        jdbcTemplate.update(archiveQuery, toEmpId, tenantId);

	        String insertQuery = "INSERT INTO indent_assign_team (INDENT_DTL_ID, EMPLOYEE_ID, IS_PRIMARY, TENANT_ID) " +
	                             "SELECT INDENT_DTL_ID, ?, 1, TENANT_ID " +
	                             "FROM indent_assign_team " +
	                             "WHERE EMPLOYEE_ID = ? AND TENANT_ID = ?";
	        insert = jdbcTemplate.update(insertQuery, toEmpId, fromEmpId, tenantId);
	    } catch (Exception ex) {
	        logger.error("insertUserIndentAssign error " + ex);
	    }
	    return insert;
	}


	@Override
	public int checkUserNameExist(String empName) {
		int count = 0;
		try {
			String qry = "select case when count(*)>0 then 1 else 0 end as count from user_login where userName= ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,empName);
			count = Integer.parseInt(result.get("count").toString());

		} catch (Exception ex) {
			logger.error("checkUserNameExist error " + ex);
		}
		return count;
	}

	@Override
	public String checkUserRoleExist(String tenantid, String roleDesc) {
		String count = "";
		try {
			String qry = "select case when count(*)>0 then USER_ROLE_ID else 'NA' end as count from user_role where ROLE_NAME = ? and TENANT_ID = ? ";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,roleDesc,tenantid);
			count = result.get("count").toString();

		} catch (Exception ex) {
			logger.error("checkUserRoleExist error " + ex);
		}
		return count;
	}

	@Override
	public String checkDesignationExist(String Design, String tenantid,String dept) {
		String count = "";
		try {
			String qry = "SELECT \r\n" + 
					"    CASE WHEN COUNT(*) > 0 THEN MAX(DESIGNATION_CODE) ELSE 'NA' END AS count\r\n" + 
					"FROM \r\n" + 
					"    employee_designation \r\n" + 
					"WHERE \r\n" + 
					"    DESIGNATION_NAME = ? \r\n" + 
					"    AND TENANT_ID = ? AND DEPARTMENT_CODE = ?;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry,Design,tenantid,dept);
			count = result.get("count").toString();

		} catch (Exception ex) {
			logger.error("checkUserRoleExist error " + ex);
		}
		return count;
	}

	@Override
	public int insertUserRole(String role, String tenantId) {
		// TODO Auto-generated method stub
		int insertRes = 0;
		try {

			String inquery = "INSERT INTO `user_role`\r\n" + "(\r\n" + "`ROLE_NAME`,\r\n" + "`ROLE_DESCRIPTION`,\r\n"
					+ "`IS_ACTIVE`,\r\n" + "`TENANT_ID`,`ROLE_CODE`)\r\n" + "VALUES\r\n" + "(?,?,?,?,?);";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, role);
					ps.setString(2, role);
					ps.setString(3, "1");
					ps.setString(4, tenantId);
					ps.setString(5, role.replaceAll("\\s", ""));
					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertUserRole error " + ex);
		}
		return insertRes;
	}

	@Override
	public int insertDesignation(String designDesc, String tenantid,String dept) {
		// TODO Auto-generated method stub
		int insertRes = 0;
		try {
			String designationDesc = "";
			String qry = "select DESIGNATION_CODE from employee_designation order by DESIGNATION_CODE desc limit 1;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry);
			designationDesc = result.get("DESIGNATION_CODE").toString();
			String designationCode = CommonMethod.getDesignationCode(designationDesc);

			String inquery = "INSERT INTO `employee_designation`\r\n" + "(\r\n" + "`DESIGNATION_CODE`,\r\n"
					+ "`DESIGNATION_NAME`,\r\n" + "`IS_ACTIVE`,\r\n" + "`TENANT_ID` ,`DEPARTMENT_CODE`)\r\n" + "VALUES\r\n" + "(?,?,?,?,?);";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(inquery, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, designationCode);
					ps.setString(2, designDesc);
					ps.setString(3, "1");
					ps.setString(4, tenantid);
					ps.setString(5, dept);

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insert employee designation error " + ex);
		}
		return insertRes;
	}

	@Override
	public int getEmpCanCreate(String empDesiCode, String tenantID) {
		// TODO Auto-generated method stub
		int empCheck = 0;
		try {
			String empQry = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN COUNT(*)\r\n"
					+ "        ELSE 0\r\n" + "    END EMP_CHECK\r\n" + "FROM\r\n" + "    tenant_property_mst\r\n"
					+ "WHERE\r\n" + "    PROPERTY_DESCRIPTION = 'ADMIN_USER'\r\n" + "        AND FIND_IN_SET( ?, PROPERTY_VALUE);";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(empQry,empDesiCode);
			empCheck = Integer.parseInt(result.get("EMP_CHECK").toString());

		} catch (Exception ex) {
			logger.error("getEmpCanCreate error " + ex);
		}
		return empCheck;
	}

	@Override
	public List<EmployeeForUserDtl> getAllEmployeeUserDtl(String tenantId) {
		List<EmployeeForUserDtl> returnList = null;

		try {
			String getQ = "SELECT \r\n" + "  em.EMPLOYEE_CODE,  em.EMPLOYEE_ID,\r\n" + "    em.EMPLOYEE_FIRSTNAME,em.CLIENT_DESIGNATION,\r\n"
					+ "    em.EMAIL_ID,\r\n" + "    es.EMPLOYEE_STATUS_NAME,\r\n" + "    ed.DESIGNATION_NAME,\r\n"
					+ "    ul.userName,\r\n" + "    ur.ROLE_DESCRIPTION,\r\n" + "    em.DEPARTMENT_CODE,\r\n"
					+ "    em.DESIGNATION_CODE,\r\n" + "    ur.USER_ROLE_ID,\r\n" + "    em.EMPLOYMENT_STATUS_CODE,\r\n"
					+ "     dept.DEPARTMENT_NAME,em.TENANT_ID AS TENANT_ID \r\n" + "FROM\r\n" + "    employee_mst em,\r\n"
					+ "    user_login ul,\r\n" + "    employment_status_mst es,\r\n"
					+ "    employee_designation ed,\r\n" + "    user_role ur,\r\n" + "    user_role_mapping urm,\r\n"
					+ "     department dept\r\n" + "WHERE\r\n" + "    em.EMPLOYEE_ID = ul.EMPLOYEE_ID\r\n"
					+ "        AND em.DESIGNATION_CODE = ed.DESIGNATION_CODE\r\n"
					+ "        AND em.EMPLOYMENT_STATUS_CODE = es.EMPLOYMENT_STATUS_CODE\r\n"
					+ "        AND urm.USER_LOGINID = ul.USER_LOGIN_ID\r\n"
					+ "        AND ur.USER_ROLE_ID = urm.USER_ROLEID\r\n"
					+ "        AND em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE\r\n" + "        AND em.TENANT_ID = ?";
			RowMapper<EmployeeForUserDtl> rowmapper = new EmployeeUserRowMapper();
			returnList = this.jdbcTemplate.query(getQ, rowmapper, tenantId);
		} catch (Exception ex) {
			logger.error("getEmployeeUserDtlForDeptByEmpId Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public List<EmployeeForDepartmentEntity> getMstPocEmpForDepartment(String tENANT_ID, String designation) {
		List<EmployeeForDepartmentEntity> list = new ArrayList<>();
		try {

			String query = "SELECT \n" + "    mst.EMPLOYEE_ID, mst.EMPLOYEE_FIRSTNAME\n" + "FROM\n"
					+ "    employee_mst mst\n" + "WHERE\n" + "    mst.DESIGNATION_CODE= ? \n"
					+ "        AND mst.TENANT_ID = ? AND mst.EMPLOYMENT_STATUS_CODE='ES001'";
			RowMapper<EmployeeForDepartmentEntity> rowmapper = new EmployeeForDepartmentRowMapper();
			list = this.jdbcTemplate.query(query, rowmapper, designation, tENANT_ID);
		} catch (Exception ex) {
			logger.error("getEmployeeForDepartment  method exception-->" + ex);
		}
		logger.debug("getEmployeeForDepartment  method end");

		return list;
	}

	@Override
	public int getEmpDtl(String empId, String tenantId) {

		String gtQ = "select count(*) as VALUE from employee_mst where EMPLOYEE_CODE = ? and TENANT_ID = ? ";
		Map<String, Object> result = this.jdbcTemplate.queryForMap(gtQ,empId,tenantId);
		return Integer.parseInt(result.get("VALUE").toString());
	}

	@Override
	public List<EmployeemstdetailsEntity> getEmpdetails(String tenantId, String departmentId, String empStsCode) {
		List<EmployeemstdetailsEntity> list = new ArrayList<EmployeemstdetailsEntity>();
		try {
			String empStatus = "";
			if(empStsCode != null && !empStsCode.isEmpty()) {
			    empStatus = " and em.EMPLOYMENT_STATUS_CODE='"+empStsCode+"' ";
			}
			 
		String query  = "SELECT \r\n" + 
				"    EMPLOYEE_ID,EMPLOYEE_CODE,\r\n" + 
				"    EMPLOYEE_FIRSTNAME,\r\n" + 
				"    em.DESIGNATION_CODE,\r\n" + 
				"    ed.DESIGNATION_NAME,\r\n" + 
				"    EMAIL_ID,\r\n" + 
				"    PHONE_NUMBER,\r\n" + 
				"    em.EMPLOYMENT_STATUS_CODE,\r\n" + 
				"    esm.EMPLOYEE_STATUS_NAME,\r\n" + 
				"    em.DEPARTMENT_CODE,\r\n" + 
				"    dep.DEPARTMENT_NAME,\r\n" + 
				"    DATE_OF_JOINING,\r\n" + 
				"    LAST_WORKING_DAY,\r\n" + 
				"    LAST_UPDATED_USER_ID,\r\n" + 
				"    LAST_UPDATED_DATETIME,\r\n" + 
				"    APPROX_SALARY\r\n" + 
				"FROM\r\n" + 
				"    employee_mst em\r\n" + 
				"        INNER JOIN\r\n" + 
				"    employment_status_mst esm ON esm.EMPLOYMENT_STATUS_CODE = em.EMPLOYMENT_STATUS_CODE\r\n" + 
				"        INNER JOIN\r\n" + 
				"    department dep ON dep.DEPARTMENT_CODE = em.DEPARTMENT_CODE\r\n" + 
				"    inner join employee_designation ed  on ed.DESIGNATION_CODE = em.DESIGNATION_CODE\r\n" + 
				"    where em.DEPARTMENT_CODE = '"+departmentId+"' and em.TENANT_ID='"+tenantId+"' "+empStatus+" ;" ;
		
		list = this.jdbcTemplate.query(query, new EmployeemstdetailsRowMapper());	
		}
		catch (Exception e) {
			logger.error("getEmpdetails dao method exception"+e);
		}
		return list;
	}

	@Override
	public int updtempmstdetails(String empId, String tenantId, String salary) {
		int update = 0;
		try {
			String upd = "update employee_mst set  APPROX_SALARY = ? where EMPLOYEE_ID = ? and TENANT_ID=?;";
			update = this.jdbcTemplate.update(upd,salary,empId,tenantId);
		}catch (Exception e) {
			logger.error("updtempmstdetails dao method exception"+e);
		}
		return update;
	}

	@Override
	public int getEmpMailDtl(String empId, String tenantId) {
		String gtQ = "select count(*) as VALUE from employee_mst where EMAIL_ID = ? and TENANT_ID = ? ";
		Map<String, Object> result = this.jdbcTemplate.queryForMap(gtQ,empId,tenantId);
		return Integer.parseInt(result.get("VALUE").toString());
	}

	@Override
	public int getupdateCodeAndMailCheck(String cloumnName, String val, String empId, String tenantId) {
		String gtQ = "";
		if(cloumnName.equals("EMPLOYEE_CODE")) {
		     gtQ = "select count(*) as VALUE from .employee_mst where EMPLOYEE_CODE = ? and EMPLOYEE_ID <> ? and TENANT_ID = ? ";

		} else if(cloumnName.equals("EMAIL_ID")) {
			 gtQ = "select count(*) as VALUE from .employee_mst where EMAIL_ID = ? and EMPLOYEE_ID <> ? and TENANT_ID = ? ";
		}
		Map<String, Object> result = this.jdbcTemplate.queryForMap(gtQ,val,empId,tenantId);
		return Integer.parseInt(result.get("VALUE").toString());
	}


}
