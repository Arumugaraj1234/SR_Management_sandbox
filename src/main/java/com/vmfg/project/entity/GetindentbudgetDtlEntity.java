package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetindentbudgetDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String indentBudId;
	private String allocatedVal;
	private String allocatedQty;
	private String indentDtlId;
	private String elementHdr;
	private String elementDtl;
	private String pskDesc;
}
