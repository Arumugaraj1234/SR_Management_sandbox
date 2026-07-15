package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentStatusMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dsmId;
	private String docType;
	private String docTypeDesc;
	private String currSequence;
	private String docStatus;
	private String docStatusDesc;
	private String apprDesi;
	private String apprDept;
	private String isNotify;
	private String lastSeq;
	private String nextSeq;
	private String seqBatch;
	private String tenantId;
	private String isActive;
	private String docGroup;
	private String processCode;
	private String isEditable;
	private String nextSeqStatusCode;
	private String nextSeqStatusDesc;
	private String previousSeq;
	private String previousSeqStatusCode;
	private String previousSeqStatusDesc;
	private String cancelSeq;
	private String cancelStatusCode;
	private String cancelStatusDesc;

}
