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
		
}
