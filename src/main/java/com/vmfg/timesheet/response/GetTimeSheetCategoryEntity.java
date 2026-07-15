package com.vmfg.timesheet.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeSheetCategoryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tcId;
	private String tcName;
	private String updatedBy;
	private String createdBy;
	private String updatedEmpName;
	private String createdEmpName;
	private String updatedOn;
	private String createdOn;

}
