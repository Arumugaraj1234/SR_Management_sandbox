package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendorInspRatingRequest {
	private String vendorCode;
	private String tenantId;
	private String empId;
}
