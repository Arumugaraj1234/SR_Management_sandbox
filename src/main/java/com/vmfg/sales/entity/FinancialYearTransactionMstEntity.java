package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinancialYearTransactionMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fytId;
	private String pmID;
	private String prefixCode;
	private String suffixCode;
	private String startId;
	private String isActive;
	private String tenantId;
	
	
}
