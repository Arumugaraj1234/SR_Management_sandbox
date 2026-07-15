package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.EmployeeForUserDtl;

public class EmployeeUserRowMapper implements RowMapper<EmployeeForUserDtl> {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeUserRowMapper.class);

	@Override
	public EmployeeForUserDtl mapRow(ResultSet rs, int rowNum) throws SQLException {

		EmployeeForUserDtl departmentInfoEntity = new EmployeeForUserDtl();
		try {
			departmentInfoEntity.setDesignation(rs.getString("DESIGNATION_NAME"));
			departmentInfoEntity.setEmployeeId(rs.getString("EMPLOYEE_ID"));
			departmentInfoEntity.setEmployeeName(rs.getString("EMPLOYEE_FIRSTNAME"));
			departmentInfoEntity.setEmployeeStatus(rs.getString("EMPLOYEE_STATUS_NAME"));
			departmentInfoEntity.setUserName(rs.getString("userName"));
			departmentInfoEntity.setUserRole(rs.getString("ROLE_DESCRIPTION"));
			departmentInfoEntity.setDeptCode(rs.getString("DEPARTMENT_CODE"));
			departmentInfoEntity.setDesignation(rs.getString("DESIGNATION_NAME"));
			departmentInfoEntity.setUserRoleId(rs.getString("USER_ROLE_ID"));
			departmentInfoEntity.setEmplStatus(rs.getString("EMPLOYMENT_STATUS_CODE"));
			departmentInfoEntity.setEmailId(rs.getString("EMAIL_ID"));
			departmentInfoEntity.setDepartment(rs.getString("DEPARTMENT_NAME"));
			departmentInfoEntity.setEmpCode(rs.getString("EMPLOYEE_CODE"));
			departmentInfoEntity.setTenantId(rs.getString("TENANT_ID"));
			departmentInfoEntity.setClientDesign(rs.getString("CLIENT_DESIGNATION"));
		} catch (Exception ex) {
			logger.error("DepartmentInfoRowMapper  Method Exception" + ex);

		}
		return departmentInfoEntity;
	}

}
