package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsHdrRetrieveEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String msName;
	private String status;
	private String stageQty;
	private String createdOn;
	private String createdBy;
	private String msHdrId;
	private String pmHdrId;
}
