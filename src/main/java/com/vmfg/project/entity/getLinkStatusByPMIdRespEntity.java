package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getLinkStatusByPMIdRespEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pkId;
	private String pmHdrId;
	private String customerName;
	private String totalCount;
	private String allocatedVal;
	private String pkDesc;
	private String pkaId;
	private String budgetCost;
	private String actualCost;
	private String targetCost;

	private String costFlowType;
	private String consumedSoFar;
}
