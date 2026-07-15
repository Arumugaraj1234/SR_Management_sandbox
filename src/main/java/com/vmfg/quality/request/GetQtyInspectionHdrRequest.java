package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetQtyInspectionHdrRequest {

	private String fromDate;
	private String toDate;
	private String vendor;
	private String tenantId;	
	private String empId;
}
