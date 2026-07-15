package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesBudgetFullEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sbHdrId;
	private String masterId;
	private String totalBudgetCost;
	private String paymentTerms;
	private String transactionStatus;
	private String transactionSeq;
	private String salePercent;
	private String finalSalesVal;
	private String tenantId;
}
