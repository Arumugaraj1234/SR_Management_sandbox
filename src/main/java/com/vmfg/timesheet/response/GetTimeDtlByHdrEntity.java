package com.vmfg.timesheet.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeDtlByHdrEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String teDtlId;
	private String tDtlId;
	private String tHdrId;
	private String tdDtlId;
	private String timeSheetDtl;
	private String timeSheetHrs;
	private String tenantId;
}
