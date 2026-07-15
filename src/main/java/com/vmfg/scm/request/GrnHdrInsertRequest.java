package com.vmfg.scm.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrnHdrInsertRequest {

	private String grnDate;
	private String createdBy;
	private String tenantId;
	private String poId;
	private String poCode;
	private String invLocation;
	List<GrnDtlInsertReq> grnDtlList;
	
}
