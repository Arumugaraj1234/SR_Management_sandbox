package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

// One live PRA raised against a PO payment term - each gets its own row under the term on the PO
// screen, since users raise several PRAs per term (even on a single 100% term).
@Getter
@Setter
public class PoTermPraEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String potId;
	private String praId;
	private String praCode;
	private String praDate;
	private String praValue;
	private String statusDesc;
}
