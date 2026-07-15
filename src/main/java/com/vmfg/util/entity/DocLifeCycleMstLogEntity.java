package com.vmfg.util.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocLifeCycleMstLogEntity {
    private String dsmLogId;
    private String docType;
    private String docTypeDesc;
    private String processCode;
    private String docStatus;
    private String docStatusDesc;
    private String docGroup;
    private String curSeq;
    private String apprDesiCode;
    private String apprDesi;
    private String lastSeq;
    private String cancelSeq;
    private String nextSeq;
    private String seqBatch;
    private String isEditable;
    private String version ;
    private String versionDate;
    private String updatedBy;
    private String tenantId;
    private String empId;
    private int isActive;
}
