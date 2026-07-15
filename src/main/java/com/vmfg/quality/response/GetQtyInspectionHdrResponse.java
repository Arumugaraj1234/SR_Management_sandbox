package com.vmfg.quality.response;

import java.util.List;

import com.vmfg.quality.entity.GetQtyInspectionHdrEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetQtyInspectionHdrResponse {

	private String vendorName;
	private String vendorCode;
	private String totalCaInternal;
	private String totalCaVendor;
	private String totalOkty;
	private String totalReworkInternal;
	private String totalReworkVendor;
	private String totalRejectedInternal;
	private String totalRejectedExternal;
	private String totalInspectionQty;
	private List<GetQtyInspectionHdrEntity> qtyinspectionList;
}
