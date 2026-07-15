package com.vmfg.scm.request;

import java.util.List;

import com.vmfg.scm.entity.IndentInsertGrpDtlRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentInsertGrpRequest {
	private String groupName;
	private String igHdrId;
	private String createdBy;
	private String lastUpdatedBy;
	private String tenantId;
	private String isInventory;
	List<IndentInsertGrpDtlRequest> insrtGrpDtl;

}
