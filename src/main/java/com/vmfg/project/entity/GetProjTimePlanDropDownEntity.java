package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProjTimePlanDropDownEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pmHdrId;
	private String pmHdrDesc;
	private String tenantId;
}
