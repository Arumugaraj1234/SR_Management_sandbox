package com.vmfg.finance.dao.interfaces;

import java.util.List;

import com.vmfg.finance.entity.FinanceHdrEntity;

public interface IFinanceDAO {

	List<FinanceHdrEntity> getFinanceDtl(String fromDate, String toDate, String customer, String processId, String empId,
			String tenantID, String financeId,String projectId);

}
