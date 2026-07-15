package com.vmfg.task.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class GetReqManHdrAndStatusAndRemarksReq implements Serializable{

	private static final long serialVersionUID = 1L;
	private String rqId;
	private String reqCategory;
	private String pmHdrId;
	private String reqName;
	private String projectDesc;
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
	
	

}
