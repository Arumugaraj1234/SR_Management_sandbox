package com.vmfg.project.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetExcessBasedIndentHdrDtl implements Serializable {

	private static final long serialVersionUID = 1L;

	private String indentId;
	private String pmHdrId;
	private String targetValue;
	private String scmBudAllocatedValue;
	private String dskId;
	private String sbcCode;

}
