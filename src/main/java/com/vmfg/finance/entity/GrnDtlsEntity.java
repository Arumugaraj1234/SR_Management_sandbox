package com.vmfg.finance.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrnDtlsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String grnNo;
	private String grnDate;
	private String grnQty;
	private String invoiceNo;
	private String invoiceDate;
}
