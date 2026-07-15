package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoStatusEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String poSId;
	private String poId;
	private String seqNo;
	private String seqStatus;
	private String seqStatusDesc;
    private String remarks;
    private String updatedBy;
    private String updatedOn;
    private String empName;
	private String tenantId;
}
