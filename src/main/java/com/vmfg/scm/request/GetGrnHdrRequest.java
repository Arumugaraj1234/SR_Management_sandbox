package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetGrnHdrRequest {
private String projectId;
private String tenantId;
private String poId;
private String fromDate;
private String toDate;
}
