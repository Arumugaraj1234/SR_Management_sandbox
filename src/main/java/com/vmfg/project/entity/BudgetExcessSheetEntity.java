package com.vmfg.project.entity;

import java.io.Serializable;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetExcessSheetEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String serialNumber;
	private String beHdrId;
	private String indentId;
	private String pmHdrId;
	private String vendorCode;
	private String budgetCost;
	private String actualCost;
	private String excess;
	private String actualExcess;
	private String reason;
	private String rootCause;
	private String action;
	private String responsible;
	private String sequenceNo;
	private String sequenceStatus;
	private String statusDesc;
	private String updatedBy;
	private String updatedOn;
	private String empName;
	private String tenantId;
	private String dskId;
	private String indentCode;
	private String projectCode;
	private String projectName;
	private String vendorName;
	private String responsibleDesc;
	private String isCompleted;
	private String assemblyValue;
	private String subAssemblyValue;
	private String nextSeq;
	private String nextSeqDesc;
	private String budgetCostlat;
	private String verCheck;
	List<RetriveBudgetExcessStatusDtlEntity> BudgetExcessStatusDtlList;
	List<DocumentStatusMstEntity> DocumentStatusMstList;
}
