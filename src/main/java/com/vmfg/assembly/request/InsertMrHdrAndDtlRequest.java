package com.vmfg.assembly.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMrHdrAndDtlRequest {
	private String pmHdrId;
	private String requestedBy;
	private String requestedFor;
	private String tenantId;
	private String requestType ;
	private List<InsertMrDtlRequest>mrDtlList;
}
