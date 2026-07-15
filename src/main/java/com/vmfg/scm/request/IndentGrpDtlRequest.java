package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentGrpDtlRequest {
private String fromDate;
private String toDate;
private String projectId;
private String indentId;
private String tenantId;
private String empId;
private String processCode;
}
