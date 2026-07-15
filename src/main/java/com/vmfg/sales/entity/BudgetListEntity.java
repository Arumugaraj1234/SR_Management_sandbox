package com.vmfg.sales.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BudgetListEntity {
	private String masterId;
	private String tenantId;
	private String salePercent;
	private String finalSaleVal;
	private String paymentTerms;
	private String documentTypeCode;
	private String stageCode;
	private String version;
	private String pmId;
	private String isBudget;
	private String remarks;
	private String crSalePercent;
	private String empId;
	private List<BudgetSheetFileEntity> list;
    private List<BudgetSheetPaymentEntity> paymentTermList;
	
}
