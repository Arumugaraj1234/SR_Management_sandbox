package com.vmfg.task.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRemarksByIdResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String teDtlId;
	private String remarks;
	private String status;
	private String employeeId;
	private String transcactionDatetime;
	private String tenantId;
	private String statusDesc;
	private String employeeName;

}
