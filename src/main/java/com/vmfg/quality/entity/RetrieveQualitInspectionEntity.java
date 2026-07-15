package com.vmfg.quality.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RetrieveQualitInspectionEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String poCode;
	private String productCode;
	private String productDesc;
	private String uom;
	private String qtyToBeInspected;
	private String inspectQty;
	private String qiCode;
	private String qiId;
	private String qiHdrId;
	private String transactionNumber;
	private String financialYearMstId;
	private String poId;
	private String pmHdrId;
	private String poDtlId;
	private String indentDtlId;
	private String inspectionReqDate;
	private String inspectionReqBy;
	private String inspectedDate;
	private String okCount;
	private String conditionalCnt;
    private String conditionalNoCnt;
	private String rejectedCount;
	private String reworkCount;
	private String inspectionStatus;
	private String inspectFlag;
	private String nrFlag;
	private int isnrFlag;
	private String qtyRating;
	private String qcRequestedFrom;
	private String inspectedBy;
	private int isRework;
	private	String vendorCode;
	private String vendorName;
	private String cancelFlag;
	private String reqFrom;

}
