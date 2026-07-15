package com.vmfg.scm.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PodtlsForProductEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String poCode;
	private String poId;
	private String poDtlId;
	private String vendorName;
	private String qty;
	private String unitRate;
	private String totalValue;
	private String productCode;
	private String description;
	private String specification;
	private String material;
	private String dtlQty;
	private String unit;
}
