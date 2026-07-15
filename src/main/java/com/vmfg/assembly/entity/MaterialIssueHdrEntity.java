package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialIssueHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String miCode;
	private String mrCode;
	private String miDate;
	private String productCount;
	private String issuedOn;
	private String issuedBy;
	private String miHdrId;
	private String mrHdrId;
	private String requestedOn;
	private String isCompleted;
	private String productCode;
	private String productDesc; 
	private String uomLongDesc;
	private String uomShortDesc;
	private String availableQty;
	private String requestedQty;
	private String issuedQty;
    private String projCode; 
    private String requestedBy;
}
