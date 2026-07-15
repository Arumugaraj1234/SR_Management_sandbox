package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertRMRemarksRequest {
private String rqId;
private String remarks;
private String empId;
private String tenantId;
}
