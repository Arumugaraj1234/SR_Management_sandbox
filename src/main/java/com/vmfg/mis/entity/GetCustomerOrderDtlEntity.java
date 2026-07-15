package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCustomerOrderDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String customerName;
	private String noOfOrder;
	private String tenantId;
}
