package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.util.CommonMethod;

public class EmailCreateTaskRowMapper implements RowMapper<EmailCreateTaskInfo> {
	private static final Logger logger = LoggerFactory.getLogger(EmailCreateTaskRowMapper.class);

	@Override
	public EmailCreateTaskInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		EmailCreateTaskInfo em = new EmailCreateTaskInfo();
		try {
			em.setActivityName(row.getString("ACTIVITY_NAME"));
			em.setAssginTo(row.getString("ASSIGN_TO"));
			em.setAssignToEmp(row.getString("EMP"));
			em.setCreatedBy(row.getString("TE_CREATED_BY"));
			em.setCreatedByName(row.getString("EMPLOYEE_FIRSTNAME"));
			em.setCreatedOn(CommonMethod.getDBDateToViewByMonthThree(row.getString("CREATED_DATE")));
			em.setDescription(row.getString("TE_DESCRIPTION"));
			em.setDueDate(CommonMethod.getDBDateToViewByMonthThree(row.getString("DUE_DATE")));
			em.setFreqDesc(row.getString("MAINTENANCE_FREQUENCY_DESCRIPTION"));
			em.setPriority(row.getString("TE_PRIORITY"));
			em.setTranscId(row.getString("TRANSACTION_UI_ID"));
			em.setFreqCode(row.getString("TE_FREQUENCY_CODE"));
			em.setDept(row.getString("DEPARTMENT_CODE"));
			
		} catch (Exception e) {
			logger.error("FinancialYearMstRowMapper Exception--->"+e);
		}
		return em;
	}

}
