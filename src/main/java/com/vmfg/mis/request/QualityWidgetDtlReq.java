package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualityWidgetDtlReq {
  private String projId;
  private String tenantID;
  private String empId;
  private String pmId;
  private String fromDate;
  private String toDate;
}
