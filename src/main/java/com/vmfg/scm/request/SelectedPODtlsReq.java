package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectedPODtlsReq {

	String poDtlId;
	String miDtlId;
	String qtyInspectReqCount;
	String reworkQty;
}
