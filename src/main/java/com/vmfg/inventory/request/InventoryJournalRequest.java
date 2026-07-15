package com.vmfg.inventory.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryJournalRequest {

	private String fromDate;
	private String toDate;
	private String projectId;
	private String tenantId;
}
