package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentGroupDetailsEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sNumber;
	private String groupName;
	private String partCount;
	private String sbcCode;
	private String sbcDesc;
	private String expectedDeliveryDate;
	private String igHdrId;
	private String indentId;
	private String indentCode;
	private String pkId;
	private String pkDesc;
	private String pskId;
	private String pskDesc;
	private String scsStatus;
	private String poStatus;
	private String targetCost;
	private String isInventory;
	private String scsId;
	private String finalCost;
	private String finalCostFx;
	private String type;
	private String nextStatus;
	private String versionCheck;
	private String poId;
	private String pjsCreatedPerson;
	private String empId;
	private String costFlowType;
}
