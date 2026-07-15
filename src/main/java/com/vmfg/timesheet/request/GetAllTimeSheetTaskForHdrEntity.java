package com.vmfg.timesheet.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllTimeSheetTaskForHdrEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String taskGroupName;
	private String createdBy;
	private String createdEmpName;
	private String UpdatedEmpName;
	private String createdOn;
	private String updatedBy;
	private String updatedOn;
	private String tdId;
}
