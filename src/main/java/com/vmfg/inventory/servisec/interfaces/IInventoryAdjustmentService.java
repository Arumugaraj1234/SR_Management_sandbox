package com.vmfg.inventory.servisec.interfaces;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.inventory.request.InsertAdjustmentRequest;
import com.vmfg.scm.request.ProjectDtlRequest;

public interface IInventoryAdjustmentService {

	ResponseAsList retrieveinventoryAdjustment(ProjectDtlRequest projectdtlreq);

	ResponseAsList getadjustmettypedropdown(TenantRequest tenanttreq);

	ResponseAsMessage insertAdjustment(InsertAdjustmentRequest insertadjustreq);

}
