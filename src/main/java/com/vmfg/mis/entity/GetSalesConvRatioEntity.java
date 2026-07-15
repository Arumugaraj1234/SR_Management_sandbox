package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSalesConvRatioEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String customerName;
	private String projectName;
	private String createdDateTime;
	private String createdDate;
	private String completedDateTime;
	private String completedDate;
	private String dateDiff;
	private String tenantId;
	private String handoverDate;
	private String enqCode;
	
}
