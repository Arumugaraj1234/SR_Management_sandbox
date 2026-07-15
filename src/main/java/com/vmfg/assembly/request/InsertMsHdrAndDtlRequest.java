package com.vmfg.assembly.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMsHdrAndDtlRequest {
	private String pmHdrId;
	private String msName;
	private String stageQty;
	private String createdBy;
	private String tenantId;
	private String uom;
	List<InsertMsDtlRequest> msDtlList;
}
