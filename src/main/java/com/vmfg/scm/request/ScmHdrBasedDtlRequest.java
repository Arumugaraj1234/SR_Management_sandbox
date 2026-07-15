package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmHdrBasedDtlRequest {
private String fromDate;
private String toDate;
private String projectId;
private String tenantId;
private String empId;
private String customerName;
private String pmId;
}
