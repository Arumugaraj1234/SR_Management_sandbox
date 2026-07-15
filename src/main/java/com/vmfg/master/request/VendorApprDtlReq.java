package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendorApprDtlReq {
	private String tenantId;
	private String approved;
	private String venRatingBased;

}
