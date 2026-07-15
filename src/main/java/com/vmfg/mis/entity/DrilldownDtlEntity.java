package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DrilldownDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
//common use
	private String ProjCode;
	private String projName;
	private String vendorName;
	
	private String qualityRate;
	private String reWorkQty;
	private String ca;
	private String rejQty;
	private String inspOn;
	private String productDesc;
	private String okQty;
	private String reWorkInternal;
	private String reWorkVendor;
	private String caInternal;
	private String caVendor;
	private String rejectInternal;
	private String rejectExternal;
	
// Total Inspection Call
	private String inspReqCnt;
	private String inspCompleteCnt;
	private String inspOk;
	private String inspQty;
	private String inspNotReq;
	private String underScope;
	private String notUnderScope;
	
	private String prodCode;
	private String poCode;
	
}
