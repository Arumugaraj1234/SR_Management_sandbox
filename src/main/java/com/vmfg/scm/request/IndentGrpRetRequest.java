package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentGrpRetRequest {

	private String indentId;
	private String tenantId;
	private String empId;
	// Station-scoped PJS grouping (NEW-flow): pull groupable items from every eligible
	// indent under this station (indent_hdr.PKA_ID) instead of a single indentId.
	private String pkaId;
	// Mirrors the "getIndent" flag the indent dropdown uses ("5" = internal -> DS070/DS077,
	// anything else -> DS020) so the station item list matches what the dropdown would have shown.
	private String getIndent;

}
