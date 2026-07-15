package com.vmfg.scm.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentInsertGrpDtlRequest {
	private String indentDtlId;
	private String inventory;
	private String qty;
	private String tenantId;
}
