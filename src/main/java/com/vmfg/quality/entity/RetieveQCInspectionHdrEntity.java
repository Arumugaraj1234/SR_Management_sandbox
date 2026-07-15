package com.vmfg.quality.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetieveQCInspectionHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String vendorName;
	private String vendorCode;
	private String locationRef;
	private String productName;
	private String inspectQty;
	private String qtyToBeInspected;
	private String indentDtlId;
	private String productCode;
	private String okCount;
	private String rejectedCount;
	private String reworkCount;
	private String conditionalCnt;
	private String insReqDate;
	private String drawingNo;
	private String qualityRefNo;
	private String configName;
	private String inspectedDate;
	private String tenantId;
	private String masterPoc;
	private String qtyInspectionCompleted;
	private int dmId;
}
