package com.vmfg.mis.services.interfaces;


import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.request.DrilldownDtlReq;
import com.vmfg.mis.request.QualityWidgetDtlReq;
import com.vmfg.mis.request.QulyProjCntRequest;
import com.vmfg.mis.request.TeamMemberLoadReq;

public interface IQualityMisService {

	ResponseAsMessage getQualityProjCnt(QulyProjCntRequest qlyProjCnt);
	
	ResponseAsList QualityWidgetDtlResp(QualityWidgetDtlReq widgetDtl);

	ResponseAsList SupplierRatingResp(QualityWidgetDtlReq widgetDtl);

	ResponseAsList TeamMemberLoadResp(TeamMemberLoadReq teamLoad);

	ResponseAsList getDrilldownDtlResp(DrilldownDtlReq drillDownDtl);

	ResponseAsList getVendorByCatAndType(TenantRequest tenantId);

}
