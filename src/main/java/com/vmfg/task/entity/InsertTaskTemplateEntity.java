package com.vmfg.task.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertTaskTemplateEntity {

	private String ttHdrId;
	private String ttName;
	private String ttCreatedBy;
	private String ttCreatedOn;
	private String ttDepartmentCode;
	private String taskTypeCode;
	private String taskCategoryCode;
	private String isActive;
	private String lastUpdatedBy;
	private String tenantId;
	private List<TaskTemplateDtlEntity> taskTemplateDtlList;
}
