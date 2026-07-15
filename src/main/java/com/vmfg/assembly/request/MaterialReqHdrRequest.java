package com.vmfg.assembly.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialReqHdrRequest {
	private String hdrId;
	private String tenantId;
	private String empId;
	private String seqNo;
	private String requestType ;
	List<MaterialDtlRequest> mrDtlReqList;
}
