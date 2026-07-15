package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateDtlMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ttDtlId;
	private String actName;
	private String isActive;
	private String lastUpdatedOn;
	private String lastUpdatedBy;
	private String empName;
}
