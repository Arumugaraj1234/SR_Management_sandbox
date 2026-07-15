package com.vmfg.scm.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertQtyInspRequest {

	private String tenantId;
	private String poId;
	private String empId;
	private String pmId;
	private String pmHdrId;
	private String mstId;
	private String enquiryId;
	private List<SelectedPODtlsReq> selectedPODtls; 
}
