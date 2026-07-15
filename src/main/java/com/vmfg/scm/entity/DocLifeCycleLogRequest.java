package com.vmfg.scm.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DocLifeCycleLogRequest {
private String docGroup;
private String docType;
private String processCode;
private String tenantId;
}
