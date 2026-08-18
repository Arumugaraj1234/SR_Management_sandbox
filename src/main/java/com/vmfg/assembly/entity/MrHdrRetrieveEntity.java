package com.vmfg.assembly.entity;

import java.io.Serializable;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MrHdrRetrieveEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String remarks;
	private String createdOn;
	private String employeeName;
	private String employeeId;
	private String mrhId;
	private String isCanceled;
	private String isCompleted;
	private String statusDesc;
	private String statusCode;
	private String seqNo;
	private String isApprovee;
	private String lastUpdatedBy;
	private String productCount;
	private String returnType;
	private String groupName;
	List<DocumentStatusMstEntity> DocumentStatusMstList;
	List<DocumentStatusMstEntity> DocumentCurrentMstList;
}
