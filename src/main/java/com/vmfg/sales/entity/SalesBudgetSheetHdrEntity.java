package com.vmfg.sales.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesBudgetSheetHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sbHdrId;
	private String totalBudgetCost;
	private String paymentTerms;
	private String transactionStatus;
	private String transactionStatusSeq;
	private String salePercent;
	private String saleValue;
	private String crCost;
	private String crsalePercent;
	private String crfinalCost;
	private List<SalesBudgetSheetDtlEntity> salesDtlList;
	private List<BudgetSheetPaymentEntity> paymentTermList;

}
