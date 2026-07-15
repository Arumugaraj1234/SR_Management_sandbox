package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskHdrByEmpIdReq {
	private String empId;
	private String tenantId;
}
