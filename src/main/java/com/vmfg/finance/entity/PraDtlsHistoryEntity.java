package com.vmfg.finance.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PraDtlsHistoryEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String praNum;
	private String praValue;
	private String praStatusDesc;
	private String praStatusCode;
}
