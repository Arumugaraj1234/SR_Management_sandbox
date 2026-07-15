package com.vmfg.timesheet.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEntryHdrAndDtlEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String activityName;
	private String assignToDesc;
	private String ttDesc;
	private String tcDesc;
	private String qty;
	private String teDtlId;
	private String ttDtlId;
	private String teHdrId;
}
