package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRequestDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String crDtlId;
	private String crhdrId;
	private String designerComments;
	private String tenantId;
	private String empId;
	private String reportedDateTime;
	private String empName;
}
