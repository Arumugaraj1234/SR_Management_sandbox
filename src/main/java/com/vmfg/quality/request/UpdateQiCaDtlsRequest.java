package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UpdateQiCaDtlsRequest {
private String caQty;
private String reworkInternal;
private String reworkVendor;
private String rejectedInternal;
private String rejectedExternal;
private String caVendor;
private String caInternal;
private String qiCaDtlId;
private String pmId;
private String mstId;
private String docTypeCode;
private String pmHdrId;
private String tenantId;
private String enquiryId;
private String remarks;

}
