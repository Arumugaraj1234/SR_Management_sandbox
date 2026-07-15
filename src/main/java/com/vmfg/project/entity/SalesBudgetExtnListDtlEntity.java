package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter	
public class SalesBudgetExtnListDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pkseId;
	private String dskId;
	private String sbExtnId;
	private String totalQty;
	private String TotalValue;
	private String sbDtlId;
	private String elementHdr;
	private String elementDtl;
	private String tenantId;
	private String keyCategory;
	private String keyCategotyDesc;
	private String customerName;
	private String pskDesc;
	private String specification;
	private String make;
	private String perPartVal;
}
