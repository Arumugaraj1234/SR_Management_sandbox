package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrnHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String grnHdrId;
	private String grnCode;
	private String transactionNo;
	private String financialYearMstId;
	private String miId;
	private String miCode;
	private String miDate;
	private String grnDate;
	private String createdOn;
	private String createdBy;
	private String createdEmpName;
	private String lastUpdatedOn;
	private String lastUpdatedBy;
	private String lastUpdatedEmpName;
	private String vendorName;
	private String vendorCode;
	private String invLocation;
	private String invLocCode;
	private String productCode;
	private String productDesc;
	private String poCode;
	private String grnQty;
	private String projectId;
	private String projectName;
	private String dcNo;
	private String dcDate;
}
