package com.vmfg.master.services.interfaces;

import java.util.List;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.request.ProjectInitiationUpdateRequest;

public interface IProjectInitiationMasterService {
	ResponseAsList getProjectInitiationDtl(TenantRequest tenantRequestReq);
	ResponseAsMessage updateProjectIntiationMaster(List<ProjectInitiationUpdateRequest> projectInitiationUpdateReq);
}
