package com.vmfg.design.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentRequestEntity {

	private String indentId;
	private String tenantId;
	private String processCode;
	// PJS group (indent_grp_hdr.IG_HDR_ID) of the sheet being viewed - an indent can have more
	// than one PJS, so the indent alone does not say which one. Optional; falls back to the
	// indent's latest PJS when not sent.
	private String igHdrId;
}
