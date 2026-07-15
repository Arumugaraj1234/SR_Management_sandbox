package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoPaymentTermEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String potId;
	private String poId;
	private String term;
	private String percentage;
	private String paymentAmount;
	private String pendingAmount;
	private String remarks;
	private String isCreated;
	private String praDate;
	private String praCode;
	private String documentStatus;
	private String isLast;
}
