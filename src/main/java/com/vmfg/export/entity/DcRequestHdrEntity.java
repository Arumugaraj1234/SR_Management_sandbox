package com.vmfg.export.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DcRequestHdrEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String dcrId;
	private String reqOn;
	private String reqBy;
	private String pmHdrId;
	private String remarks;
	private String isCompleted;
	
	private List<DcRequestDtlEntity> dereqdtllist;

}
