package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentTypeMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String docTypeCode;
	private String docTypeDesc;
	private String pmId;
	private String pmDesc;
	private String mstTableName;
	private String refTableName;
	private String statusTableName;
	private String refSlaveId;
	private String isActive;
	private String tenantId;
	private String stgCode;
	private String mstColumnName;
}
