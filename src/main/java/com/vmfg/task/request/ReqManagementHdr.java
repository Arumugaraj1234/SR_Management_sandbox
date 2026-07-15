package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqManagementHdr {
private String reqCategory;
private String pmHdrId;
private String reqName;
private String reqDesc;
private String requestedBy;
private String requestedByDept;
private String requestedTo;
private String requestedToDept;
private String ticketReporter;
private String tenantId;
private String remarksBy;
private String remarks;
private String empId;
private String requestedDate;
private String dueDate;

}
