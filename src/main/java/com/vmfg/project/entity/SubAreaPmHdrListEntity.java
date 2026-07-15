package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubAreaPmHdrListEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pkseId;
	private String pkaId;
	private String sbExtnId;
	private String allocatedQty;
	private String allovatedValue;
	private String sbDtlId;
	private String elementHdr;
	private String elementDtl;
	private String tenantId;
	private String keyCategory;
	private String keyCategotyDesc;
	private String customerName;
	private String pskDesc;
}
