package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentCostDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String indentId;
	private String indentTypeCode;
	private String pkaId;
	private String pksaId;
	private String createdDate;
    private String budgetValue;
    private String budgetStatus;
    private String dnValue;

    private String targetValue;
    private String scmBudgetAllocated;
    private String indentTypeDesc;
    private String pkDesc;
    private String pskDesc;
    private String indentCode;
    private String sbcCode;
    private String sbcDesc;
    private String reason;
    private String rootCause;
    private String action;
    private String responsibleCode;
    private String responsibleDesc;
    private String closeDate;
}
