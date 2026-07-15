package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class getIndentHdrRequest {
private String projectId;
private String tenantId;
private String empId;
private String pmId;
private String getIndent;
}
