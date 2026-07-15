package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetExcessStatusDtlReq {
private String pmHdrId;
private String tenantId;
private String sequenceNo;
private String empId;
private String enqId;
private String processCode;
}
