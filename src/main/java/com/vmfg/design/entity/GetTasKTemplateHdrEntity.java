package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTasKTemplateHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ttHdrId;
	private String ttName;
	private String ttCreatedBy;
	private String ttCreatedOn;
	private String ttDepartmentCode;
	private String taskTypeCode;
	private String taskCategoryCode;
	private String isActive;
	private String lastUpdateDatetime;
	private String lastUpdatedBy;
	private String tenantId;
	private String ttDesc;
	private String tcDesc;
	private String departMentDesc;
	private String ttCreatedByDesc;
}
