package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReqStatusRequest {
private String rqId;
private String tenantId;
private String seqStatus;
private String empId;
private String statusRemarks;
private String seqNo;
private int isComplete;
private int isApproved;

}
