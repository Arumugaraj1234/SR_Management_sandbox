package com.vmfg.quality.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QIConfigDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String qicDtlId;
	private String qicHdrId;
	private String description;
	private String specification;
	private String inspectionMethod;
	private String isActive;
	private String tenantId;
}
