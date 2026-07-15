package com.vmfg.design.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignHdr implements Serializable {

	private static final long serialVersionUID = 1L;
	private String designID;
	private String projectName;
	private String projectID;
	private String statusDesign;
	private String taskPlan;
	private String taskActual;
	private String indentPlan;
	private String indentActual;
	private String dueDate;
	private String customerName;
	private String requestedBy;
	private String requestedByID;
	private String plannedStartDate;
	private String actualStartDate;
	private String actualEndDate;
	private String projectCode;
	private String enquiryId;
	private String hdrStatusDesc;
	private String StagName;
	private String designCode;
	private String checked;
	private String verified;
	private String approved;
	private String designApproved;
	private String designVerified;
	private String isInternal;
}
