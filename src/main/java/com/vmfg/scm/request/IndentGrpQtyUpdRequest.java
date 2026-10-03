package com.vmfg.scm.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

// Details popup: change the qty of lines already in a group (NEW flow).
@Getter
@Setter
public class IndentGrpQtyUpdRequest {
	private String igHdrId;
	private String empId;
	private String tenantId;
	private List<IndentGrpQtyUpdDtlRequest> updGrpDtl;
}
