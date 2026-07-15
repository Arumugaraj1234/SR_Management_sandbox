package com.vmfg.task.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReqManHdrDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String rqId;
	private String dueDate;
	private String reqCategory;
	private String reqCategoryDesc;
	private String pmHdrId;
	private String reqName;
	private String projectDesc;
	private String projectName;
	private String reqDesc;
	private String requestedById;
	private String requestedByName;
	private String requestedToName;
	private String requestedByDept;
	private String requestedByDeptName;
	private String requestedToId;
	private String requestedToDept;
	private String requestedToDeptName;
	private String seqNo;
	private String seqStatusCode;
	private String seqStatusDesc;
	private String ticketReporterName;
	private String ticketReporterId;
	private String requestedDate;
	private String isCompleted;
	private String tenantId;
	private String reqRemarksDateTime;
	private String closedDate;
	private String projectCode;
	List<GetStatusRemarksDtlEntity> statusList;
	List<GetRemarksDtlEntity> remarksList;
}
