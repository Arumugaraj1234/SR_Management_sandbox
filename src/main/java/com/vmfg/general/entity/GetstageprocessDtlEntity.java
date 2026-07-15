package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetstageprocessDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String isNotification;
	private String processId;
	private String processCode;
	private String stgCode;
	private int seq;
	private String previousStgEdit;
	private String alwaysVisible;
	private String masterDocStatus;
	private String StgComDesc;
	private String component;
	private String tenantId;
	private String processDesc;
	private String isdefault;
	private String stgDesc;
	private String masterDocstsDesc;
	private String mstId;
	private String docTypeCode;
	private String slaveId;
	private String isEditable;
}
