package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectWbsInitiationMst implements Serializable {

	private static final long serialVersionUID = 1L;

	private String piId;
	private String deptCode;
	private String pmId;
	private String primaryDoc;
	private String tenantId;
	private String masterPoc;
	private String assignedDept;

}
