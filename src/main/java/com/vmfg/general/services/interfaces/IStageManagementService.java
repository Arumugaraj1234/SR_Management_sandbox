package com.vmfg.general.services.interfaces;

import com.vmfg.general.request.GenerateProjCodeReq;
import com.vmfg.general.request.GetUpdateProcessDtlRequest;
import com.vmfg.general.request.GetprocessEnbleStatusRequest;
import com.vmfg.general.request.GetstageprocessDtlRequest;
import com.vmfg.general.request.InitiateProcessRequest;
import com.vmfg.general.request.UpdateDueDateRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;

public interface IStageManagementService {

	ResponseAsList getstageprocessDtl(GetstageprocessDtlRequest getstageprocessDtlRequest);
	ResponseAsList getprocessEnbleStatus(GetprocessEnbleStatusRequest getprocessEnbleStatusReq);
	ResponseAsMessage getUpdateProcessDtl(GetUpdateProcessDtlRequest getUpdateProcessDtlreq);
	ResponseAsMessage initiateProcess(InitiateProcessRequest initiateProcessReq);
	ResponseAsMessage GenerateAndUpdateProjectCode(GenerateProjCodeReq generateProjCodeReq);
	ResponseAsMessage UpdateProjectDueDate(UpdateDueDateRequest updateDueDateReq);
	ResponseAsList getProjectDueDates(PmHdrIdAndTenantIdRequest pmHdrIdAndTenantIdReq);
	ResponseAsList getDefaultComponentName(GetstageprocessDtlRequest req);
}
