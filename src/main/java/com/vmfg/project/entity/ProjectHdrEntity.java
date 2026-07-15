package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectHdrEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String pmHdrId;
	private String projectName;
	private String enquiryId;
	private String createdDate;
	private String isCompleted;
	private String completedDateTime;
	private String lastUpdatedDateTime;
	private String tenantId;

}
