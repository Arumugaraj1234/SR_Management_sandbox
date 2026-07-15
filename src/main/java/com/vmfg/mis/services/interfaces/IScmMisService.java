package com.vmfg.mis.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.request.ManagementProjRequest;
import com.vmfg.mis.request.ScmMisRequest;

public interface IScmMisService {

	ResponseAsList getSCMWidgetDtl(ScmMisRequest scmMisReq);

	ResponseAsList getIndentToPO(ScmMisRequest scmMisReq);

	ResponseAsMessage getcostnegotiated(ScmMisRequest scmMisReq);

	ResponseAsMessage getInventoryValue(ScmMisRequest scmMisReq);

	ResponseAsMessage getInventoryAgeing(ScmMisRequest scmMisReq);

	ResponseAsList getScmEmployeeIndentDtls(ScmMisRequest scmMisReq);

	ResponseAsList getVendorPaymentCount(ManagementProjRequest manageProjCnt);

	ResponseAsList getVendorDetailView(ManagementProjRequest manageProjCnt);

	ResponseAsList getVendorDtlDrillDown(ManagementProjRequest manageProjCnt);

}
