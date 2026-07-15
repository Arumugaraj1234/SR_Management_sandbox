package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialReturnDtlAcceptEntity implements Serializable {

	private static final long serialVersionUID = 1L;
			
	private String projectId;
	private String productCode;
	private String qty;
	private String mrHdrId;
	private String tenantId;
    private String productId;
}
