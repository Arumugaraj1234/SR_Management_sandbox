package com.vmfg.scm.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dcID;
    private String dcCode;
    private String dcType;
    private String dcDate;
    private String pmHdrId;
    private String poNO;
    private String poCode;
    private String poDtlId;
    private String indentDtlId;
    private String poDATE;
    private String transportationName;
    private String transportationMode;
    private String lrDateTime;
    private String lrNo;
    private String shippedFrom;
    private String shippedFromAddress;
    private String shippedFromDistrict;
    private String shippedFromState;
    private String shippedFromCountry;
    private String deliveredTo;
    private String deliveredToAddress;
    private String deliveredToDistrict;
    private String deliveredToState;
    private String deliveredToCountry;
    private String amountInWords;
    private String remarks;
    private String totalBasic;
    private String gstValue;
    private String totalValue;
    private String tenantId;
    private String dcDtlCount;
    private String dcTypeDesc;
    private String shippedPinCode;
    private String shippedGstIn;
    private String deliveredPinCode;
    private String deliveredGstIn;
    private String recNo;
    private String division;
    private String empId;
    private String isCancel;
    private String poId;
    private String projectCode;
    private String projectName;
    private List<DcDtlEntity> dcDtlList= null;
}
