package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRatingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ca;
	private String inspOk;
	private String rejQty;
	private String QtyRate;
	private String venName;
	private String venCode;
	private String reWorkQty;
	private String okQty;
	private String inwardRate;
	private String relationshipRate;
}
