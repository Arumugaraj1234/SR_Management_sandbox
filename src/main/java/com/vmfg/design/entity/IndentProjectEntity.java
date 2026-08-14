package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentProjectEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String projectCode;
	private String projectName;
	private String indentCode;
	private String createdDate;
	private String expDeliveryDate;
	private	String totalBudgetConsumed;
	private String costFlowType;
	private String allocatedValue;
	private String actualConsumedValue;
	private String canAllocateFromSalesBudget;
	private String isShortfall;
	private String hasBudgetExcess;
	private String pkaId;
	// Utilized-value breakdown for the info icon next to Available Value (NEW-flow only) -
	// approvedPoAmount + committedPjsAmount + reservedPendingExcessAmount sums to actualConsumedValue.
	private String approvedPoAmount;
	private String committedPjsAmount;
	private String reservedPendingExcessAmount;
	// Universal PJS No. minted once at PJS creation (indent_grp_scs.PJS_REF_NO) - see
	// IndentGroupService.insertScpDtlsByIgHdrId. Blank for indents with no PJS yet, or for
	// pre-existing PJS the V5 backfill migration couldn't resolve.
	private String pjsRefNo;

}
