package com.vmfg.assembly.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMaterialIssueRequest {
private String pmHdrId;
private String mrHdrId;
private String issuedBy;
private String remarks;
private String tenantId;
private String empId;
private List<InsertMaterialIssueDtlRequest>miDtlList;
}
