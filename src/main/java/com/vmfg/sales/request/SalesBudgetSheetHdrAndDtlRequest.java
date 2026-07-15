package com.vmfg.sales.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SalesBudgetSheetHdrAndDtlRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private String sbHdrId;
	private String masterId;
	private String totalBudgetCost;
	private String paymentTerms;
	private String transactionStatus;
	private String transactionStatusSeq;
	private String tenantId;

	private List<SalesBudgetSheetDtlRequest> salesDtlList;
}
