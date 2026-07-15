package com.vmfg.finance.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PraStatusEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String praSID;
    private String praID;
    private String sequenceNo;
    private String seqStatus;
    private String seqStatusDesc;
    private String remarks;
    private String updatedBy;
    private String empName;
    private String updatedOn;
    private String tenantId;
}
