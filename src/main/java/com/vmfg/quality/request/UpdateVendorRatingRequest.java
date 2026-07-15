package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVendorRatingRequest {
	private String qiHdrId;
	private String supplierValue;
	private String tenantId;
	private String empId;
}
