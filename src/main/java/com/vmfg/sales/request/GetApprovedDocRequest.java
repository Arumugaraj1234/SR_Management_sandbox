package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetApprovedDocRequest {
private String enquiryId;
private String stageCode;
private String approved;
private String tenantId;
private String docTypeCode;
private String empId;
}
