package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialInwardHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    private String miId;
    private String miCode;
    private String transactionNo;
    private String financialYearMstId;
    private String poId;
    private String poCode;
    private String dcId;
    private String dcCode;
    private String inwardDate;
    private String vendorCode;
    private String vendorName;
    private String dcNo;
    private String dcDate;
    private String noOfParts;
    private String status;
    private String isLatest;
    private String statusDesc;
    private String createdOn;
    private String createdBy;
    private String empName;
    private String lastUpdatedOn;
    private String lastUpdatedBy;
    private String tenantId;
    private String materialLocation;
    private String invFlag;
    private String bin;
    private String inwardRating;
    private String relationshipRating;
    private String projectCode;
    private String projectName;
    private String seqFlag;
    private String grnCode;
    private String grnDate;
}
