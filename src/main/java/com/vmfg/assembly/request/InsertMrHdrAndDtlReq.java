package com.vmfg.assembly.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMrHdrAndDtlReq {
	private String pmHdrId;
	private String hdrRemark;
	private String createdBy;
	private String tenantId;
	private String remarks;
	List<InsertMrDtlReq> mrDtlList;
}
