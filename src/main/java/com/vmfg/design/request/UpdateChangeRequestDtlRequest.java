package com.vmfg.design.request;

import java.util.List;

import com.vmfg.design.entity.ChangeRequestDtlEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChangeRequestDtlRequest {

	private String crId;
	//private String crNo;
	private String deHdrId;
	private String pmHdrId;
	private String initiatedBy;
//	private String crDate;
	private String pkId;
	private String pskId;
	private String requestDetails;
	private String nextApprovingDesig;
	private String updatedDrawingNo;
	private	String updatedDrawingRevNo;
	private String createdBy;
	private String tenantId;
	private	String pmId;
	private String productDesc;
	private String lastUpdatedBy;
	private String empId;
	private List<ChangeRequestDtlEntity> changeReqDtlEntity;
}
