package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentGroupHdrAndDtlEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productCode;
	private String description;
	private String specification;
	private String make;
	private String weight;
	private String material;
	private String remarks;
	private String indentQty;
	private String indentGrpQty;
	private String differenceQty;
	private String indentDtlId;
	private String indentGrpDtlId;
	private String uom;
	private String dmId;
}
