package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetieveQCInspectionHdrReq {
	private String qiId;
	private String empId;
	private String pmId;
	private String tenantId;
}
