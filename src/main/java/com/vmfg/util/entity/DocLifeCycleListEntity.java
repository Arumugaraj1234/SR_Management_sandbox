package com.vmfg.util.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocLifeCycleListEntity {


	
	private String dsmId;
	private String docType;
	private String docTypeDesc;
	private String processCode;
	private String docStatus;
	private String docStatusDesc;
	private int curSeq;
	private String apprDesiCode;
	private int lastSeq;
	private String nextSeq;
	private String cancelSeq;
	private int isEditable;
	private String seqBatch;
	private String tenantId;
	private String docGroup;
	private String isNotify;
	private int isActive;
	private String empId;
	private int sNo;
	
	
	
	
	
}
