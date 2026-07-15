package com.vmfg.general.response;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.EmployeemstdetailsEntity;

public class EmployeemstdetailsRowMapper implements RowMapper<EmployeemstdetailsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(EmployeemstdetailsRowMapper.class);

	@Override
	public EmployeemstdetailsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		EmployeemstdetailsEntity employee = new EmployeemstdetailsEntity();
		try {
			employee.setEmployeeId(rs.getString("EMPLOYEE_ID"));
			employee.setEmployeeFirstName(rs.getString("EMPLOYEE_FIRSTNAME"));
			employee.setDesignationCode(rs.getString("DESIGNATION_CODE"));
			employee.setDesignationName(rs.getString("DESIGNATION_NAME"));
			employee.setEmailId(rs.getString("EMAIL_ID"));
			employee.setPhoneNumber(rs.getString("PHONE_NUMBER"));
			employee.setEmploymentStatusCode(rs.getString("EMPLOYMENT_STATUS_CODE"));
			employee.setEmployeeStatusName(rs.getString("EMPLOYEE_STATUS_NAME"));
			employee.setDepartmentCode(rs.getString("DEPARTMENT_CODE"));
			employee.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
			employee.setDateOfJoining(rs.getString("DATE_OF_JOINING"));
			employee.setLastWorkingDay(rs.getString("LAST_WORKING_DAY"));
			employee.setLastUpdatedUserId(rs.getString("LAST_UPDATED_USER_ID"));
			employee.setLastUpdatedDateTime(rs.getString("LAST_UPDATED_DATETIME"));
			employee.setApproxSalary(rs.getString("APPROX_SALARY"));
			employee.setEmployeeCode(rs.getString("EMPLOYEE_CODE"));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("EmployeemstdetailsRowMapper method exception"+e);
		}

		return employee;
	}

}
