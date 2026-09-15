package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetExcessSheetRequest {
	private String tenantID;
	private String indentId;
	private String vendor;
	private String updatedBy;
	private String igScsId;
	private String pmId;
	private String masterId;
	private String projectId;
	private String scsFinalCost;
    private String processDoc;
	private String allocatedValue;
	private String actualSpentSoFar;
	// Multi-indent PJS support: when set, insertBudgetExcessSheetDtl uses this value directly as
	// ACTUAL_EXCESS instead of deriving it from allocatedValue/actualSpentSoFar - the caller has
	// already split the PJS's total shortfall across its distinct indents (proportional to each
	// indent's own share of this PJS) and is inserting one row per indent. See
	// project_multi_indent_pjs_grouping memory, Problem 4.
	private String explicitScsActualCost;

}
