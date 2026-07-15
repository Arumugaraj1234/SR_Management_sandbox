package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentRemarksEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String remarks;
	private String updatedOn;
	private String updatedBy;
	private String statusDesc;
}
