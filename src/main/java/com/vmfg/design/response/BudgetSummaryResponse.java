package com.vmfg.design.response;

import java.util.List;

import com.vmfg.design.entity.CatbaseIndentCostDtlEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetSummaryResponse {

	private String indentedMaterial ;
	private String poReleased;
	private String rmcBudgetStatus;
	private String salesTotalBudget;
	private String availablebudgetOnDate;
	private String materialTransfeCost;
	private String debitNoteValue;
	private List<CatbaseIndentCostDtlEntity> catbaseIndentCostDtl;
}
