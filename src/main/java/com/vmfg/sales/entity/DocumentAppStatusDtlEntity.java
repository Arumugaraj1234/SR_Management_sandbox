package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentAppStatusDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String dasId;
	private String dmId;
	private String sequenceNo;
	private String sequenceStatus;
	private String updatedby;
	private String updatedOn;
	private String tenantId;
	private String seqStatusDesc;
	private String version;

}

