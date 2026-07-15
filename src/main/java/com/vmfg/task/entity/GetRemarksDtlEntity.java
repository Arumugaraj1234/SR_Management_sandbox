package com.vmfg.task.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRemarksDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String rqDtlId;
	private String remarks;
	private String rqId;
	private String empName;
	private String requestedDateTime;
}
