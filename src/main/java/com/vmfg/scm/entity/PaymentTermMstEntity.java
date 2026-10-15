package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentTermMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private int paymentTermId;
	private String termDesc;
	private int seqNo;
}
