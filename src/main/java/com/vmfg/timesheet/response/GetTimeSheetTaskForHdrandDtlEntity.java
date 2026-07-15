package com.vmfg.timesheet.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeSheetTaskForHdrandDtlEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String taskGroupName;
	private String createdBy;
	private String createdEmpName;
	private String updatedBy;
	private String updatedEmpName;
	private String createdOn;
	private String updatedOn;
	private String tdId;
	private String tdDtlId;
	private String defaulTaskDtl;
}
