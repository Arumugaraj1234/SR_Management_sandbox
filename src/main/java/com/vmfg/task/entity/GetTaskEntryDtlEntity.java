package com.vmfg.task.entity;

import java.io.Serializable;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskEntryDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String teDtlId;
	private String teHdrId;
	private String ttDtlId;
	private String activityName;
	private String plannedStartDate;
	private String dueDate;
	private String plannedCompletedDate;
	private String completedDate;
	private String approvalSeq;
	private String approvalStatus;
	private String approvalStatusDesc;
	private String isCompleted;
	private String tenantId;
	private String isApproval;
	private String completePtg;
	private String assignTo;
	private String assignToDesc;
	private String actualStartDate;
	private String ttCode;
	private String ttDesc;
	private String tcCode;
	private String tcDesc;
	private String requirementFrom;
	private String qty;
	private int subTaskCount;
	private String projectCode;
	private String projectName;
	private String customerName;
	private List<DocumentStatusMstEntity> docStatusMst;
}
