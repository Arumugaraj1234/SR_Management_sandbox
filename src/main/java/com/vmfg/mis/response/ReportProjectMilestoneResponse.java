package com.vmfg.mis.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReportProjectMilestoneResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String projectCode;
	private String deptCode;
	private String pmHdrId;
	private String mileStone;
	private String department;
	private String plannedStartDate;
	private String plannedEndDate;
	private String actualEndDate;

}
