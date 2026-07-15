package com.vmfg.scm.request;

import java.util.ArrayList;
import java.util.List;

import com.vmfg.scm.entity.MaterialInwardDtlEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMaterialInwardReq {
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
    private String dcNo;
    private String dcDate;
    private String noOfParts;
    private String status;
    private String empId;
    private String tenantId;
	private List<MaterialInwardDtlEntity> dtlList= new ArrayList<MaterialInwardDtlEntity>();

}
