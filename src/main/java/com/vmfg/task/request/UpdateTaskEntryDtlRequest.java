package com.vmfg.task.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskEntryDtlRequest {

	private String masterId;
	private String taskTypeCode;
	private String taskCategoryCode;
	private String dependentTeHdrId;
	private String employeID;
	private String tenantId;
	private String ttHdrId;
	private String pmHdrId;
	private List<TaskEntryDtlRequest> taskEntryDtl;
}