package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSeqAndStatusRequest {
//	private String qHdrId;
//	private String currentseq;
//	private String empId;
//	private String tenantId;
//	private String hdrId;
//	private String remarks;
//	private String pmId;
//	private String mstId;
//	private String docTypeCode;
//	private String pmHdrId;
//	private String enquiryId;
//	private String docGroup;
//	private String nrFlag;
//	private String poId;
	
	private String currentseq;
	private String qualityHdrId;
	private String tenantId;
	private String empId;
	private String hdrId;
	private String nrFlag;
	private String pmId;
	private String qHdrId;
	private String poId;
	private String pmHdrId;
	private String docGroup;
	private String docTypeCode;
	private String mstId;
	private String enquiryId;
	private String remarks;
	private String cancelFlag;
	private String qiId;
	private String scsFinalCost;
    private String processCode;
	
}

//{
//    "currentseq": "2",
//    "tenantId": "bgrn",
//    "empId": "E0036",
//    "hdrId": "87",
//    "nrFlag": "0",
//    "pmId": "6",
//    "qHdrId": "6",
//    "poId": "6",
//    "pmHdrId": "8"
//}
