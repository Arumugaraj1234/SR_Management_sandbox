package com.vmfg.project.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class updateBudgetExcessSheetRequest {

	private String beHdrId;
	private String reason;
	private String rootCase;
	private String action;
	private String responseDept;
	private String approvingStatus;
	private String sequenceNo;
	private String empId;
	private String remarks;
	private String tenantId;
	// Sibling BE_HDR_IDs when a multi-indent PJS's Budget Excess Sheet rows are approved as one
	// merged action from the UI (see project_multi_indent_pjs_grouping memory). Null/empty for the
	// existing single-row callers - beHdrId alone is used in that case, unchanged.
	private List<String> hdrIds;
}
