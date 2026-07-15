package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetSCMWidgetDtlEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String indentHdrCnt;
	private String indentDtlCnt;
	private String noOfPo;
	private String pendingIndents;
	private String inventoryStock;
	private String itemsDelayed;
}
