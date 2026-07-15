package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.GetTasKTemplateHdrEntity;

public class GetTasKTemplateHdrRowMapper implements RowMapper<GetTasKTemplateHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetTasKTemplateHdrRowMapper.class);

	@Override
	public GetTasKTemplateHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetTasKTemplateHdrEntity res = new GetTasKTemplateHdrEntity();
		try {
		
			res.setDepartMentDesc(rs.getString("DEPARTMENT_NAME"));
			res.setLastUpdateDatetime(rs.getString("LAST_UPDATED_DATETIME"));
			res.setIsActive(rs.getString("IS_ACTIVE"));
			res.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
			res.setTaskCategoryCode(rs.getString("TASK_CATEGORY_CODE"));
			res.setTaskTypeCode(rs.getString("TASK_TYPE_CODE"));
			res.setTcDesc(rs.getString("TC_DESC"));
			res.setTenantId(rs.getString("TENANT_ID"));
			res.setTtCreatedBy(rs.getString("TT_CREATED_BY"));
			res.setTtCreatedOn(rs.getString("TT_CREATED_ON"));
			res.setTtDepartmentCode(rs.getString("TT_DEPARTMENT_CODE"));
			res.setTtDesc(rs.getString("TT_DESC"));
			res.setTtHdrId(rs.getString("TT_HDR_ID"));
			res.setTtName(rs.getString("TT_NAME"));
			res.setTtCreatedByDesc(rs.getString("EMPLOYEE_FIRSTNAME"));
		} catch (Exception ex) {
			logger.error("GetTasKTemplateHdrRowMapper  Method Exception" + ex);
		}
		return res;
	}



}
