package com.vmfg.quality.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QIStatusEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    private String qsdId;
    private String referenceId;
    private String referenceDoc;
    private String sequenceNo;
    private String sequenceStatus;
    private String seqStatusDesc;
    private String remarks;
    private String updatedBy;
    private String updatedOn;
    private String tenantId;
}
