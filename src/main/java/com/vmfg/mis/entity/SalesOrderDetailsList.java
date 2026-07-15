package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesOrderDetailsList implements Serializable {

	private static final long serialVersionUID = 1L;

	private String monthYr;
	private String val;
	private String seCount;
	private String enqCount;
	private String enqValue;
}
