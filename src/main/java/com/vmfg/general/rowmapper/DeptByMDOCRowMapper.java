package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.DocDeptEntity;

public class DeptByMDOCRowMapper implements RowMapper<DocDeptEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DeptByMDOCRowMapper.class);

	@Override
	public DocDeptEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		DocDeptEntity departmentInfoEntity = new DocDeptEntity();
		try {
			departmentInfoEntity.setDeptCode(rs.getString("DEPARTMENT_CODE"));

		} catch (Exception ex) {
			logger.error("DepartmentInfoRowMapper  Method Exception" + ex);

		}
		return departmentInfoEntity;
	}

}
