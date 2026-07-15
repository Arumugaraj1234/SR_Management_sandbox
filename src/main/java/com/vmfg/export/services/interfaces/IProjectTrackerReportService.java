package com.vmfg.export.services.interfaces;

import com.vmfg.export.request.IdAndTenantIdRequest;
import com.vmfg.export.request.ProjectTrackerReportRequest;
import com.vmfg.export.response.ResponseAsList;
import com.vmfg.finance.request.getPraDtlRequest;

public interface IProjectTrackerReportService {

	ResponseAsList getProjectTrackerReportPDF(ProjectTrackerReportRequest designReq);
	ResponseAsList getPoDtlsByPoIdReportPDF(IdAndTenantIdRequest idAndTenantIdReq);
	ResponseAsList getPraReportByPraId(getPraDtlRequest praIdAndTenantIdReq);

}
