package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentGrpScsStatusEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    private String igScsSId;
    private String igScsId;
    private String sequenceNo;
    private String seqStatus;
    private String seqStatusDesc;
    private String remarks;
    private String updatedBy;
    private String empName;
    private String updatedOn;
    private String tenantId;
}
