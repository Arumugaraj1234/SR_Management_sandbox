package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPlannedProjectEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String projectCode;
	private String projectName;
	private String projectId;
	private String customerName;
	private String totalDrawing;
	private String completedDrawing;
	private String dapPlannedDate;
	private String manualPlannedDate;
	private String tenantId;
	private String dapActualDate;
	private String manualActualDate;
	

}
