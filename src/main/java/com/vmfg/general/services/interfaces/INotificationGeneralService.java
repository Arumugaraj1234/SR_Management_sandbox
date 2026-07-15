package com.vmfg.general.services.interfaces;

import com.vmfg.design.request.TenantEmpRequest;
import com.vmfg.general.request.DirectApprovalRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface INotificationGeneralService {

	ResponseAsList getNotificationDetails(TenantEmpRequest tenantEmpRequest);

	ResponseAsMessage updateNotificationDtls(TenantEmpRequest tenantEmpRequest);

	ResponseAsList getApprovalDesigDtls(TenantEmpRequest tenantEmpRequest);

	ResponseAsMessage getDirectApprovalDesignNotify(DirectApprovalRequest directApprovalReq);

}
