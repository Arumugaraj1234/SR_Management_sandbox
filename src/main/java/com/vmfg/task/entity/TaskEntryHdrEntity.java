package com.vmfg.task.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEntryHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String teHdrId;
	private String ttHdrId;
	private String masterId;
	private String TaskName;
	private String departmentCode;
	private String departmentDesc;
	private String taskTypeCode;
	private String taskTypeDesc;
	private String taskCategoryCode;
	private String taskCategoryDesc;
	private String dependentTeHdrId;
	private String lastUpdatedDatatime;
	private String lastUpdatedBy;
	private String tenantId;
	private List<TaskEntryDtlEntity> taskEntryDtl;
}
