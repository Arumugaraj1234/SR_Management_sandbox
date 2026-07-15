package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetindentDtlcycEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String indentDtlId;
	private String productCode;
	private String productDesc;
	private String indentDtlQty;
	private String poQty;
	private String miQty;
	private String grnQty;
	private String inspectionQty;
	private int sno;
}
