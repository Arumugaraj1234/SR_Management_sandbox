package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UpdateDueDateRequest {
	private String pmHdrId;
	private String dueDate;
	private String reason;
	private String empId;
	private String tenantId;
}
