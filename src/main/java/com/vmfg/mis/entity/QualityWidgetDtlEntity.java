package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QualityWidgetDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ca;
	private String rejQty;
	private String inspOk;
	private String inspCall;
	private String reworkQty;
	private String inspQty;
}
