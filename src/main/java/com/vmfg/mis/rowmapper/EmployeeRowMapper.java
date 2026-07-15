package com.vmfg.mis.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.EmployeeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<EmployeeEntity> {
    @Override
    public EmployeeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmpId(rs.getString("EMPLOYEE_ID"));
        employee.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
        return employee;
    }
}
