package com.vmfg.mis.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.mis.request.PMMileRequest;
import com.vmfg.mis.request.PMWidgetRequest;

public interface IPMMisService {

	ResponseAsList getPMWidgetDtl(PMWidgetRequest pMWidgetReq);

	ResponseAsList getPMBySBCType(PMWidgetRequest pMWidgetReq);

	ResponseAsList getPMMilestoneByMonthYr(PMMileRequest pMMileReq);

	ResponseAsList getPMReportTracker(PMWidgetRequest pMWidgetReq);

	ResponseAsList getPMWorkLoad(PMWidgetRequest pMWidgetReq);

}
