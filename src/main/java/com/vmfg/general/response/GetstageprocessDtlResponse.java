package com.vmfg.general.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetstageprocessDtlResponse {

//	private List<ProcessConfigEntity> processConfig;
//	private String dtlId;
	
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
}
