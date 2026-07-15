package com.vmfg.finance.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PraDtlListEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String praHdrId;
	private String praDtlId;
	private String grnDtlId;
	private String partDesc;
}
