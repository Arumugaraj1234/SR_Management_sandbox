package com.vmfg.mis.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReportProjectTrackerResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String saleValue;
	private String saleContribution;
	private String pmBudgetValue;
	private String targetValue;
	private String scmPoValue;
	private String materialTransferValue;
	private String balanceValue;
	private String projectCode;
	private String empCostValue;
}
