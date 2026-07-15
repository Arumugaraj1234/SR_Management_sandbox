package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllCountByEmpId {
	private String empId;
	private String tenantId;
	private String pmId;
	private String isDashboard;
	private String fromDate;
	private String toDate;
}
