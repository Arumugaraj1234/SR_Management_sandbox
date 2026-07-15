package com.vmfg.master.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.request.TaskCategoryInsertUpdateRequest;
import com.vmfg.master.request.TaskCategoryRequest;
import com.vmfg.master.request.TenantIdRequest;

public interface ITaskCategoryService {

	ResponseAsList getTaskCategory(TaskCategoryRequest taskCategoryrequest);

	ResponseAsMessage insertandUpdateTaskCat(TaskCategoryInsertUpdateRequest taskCategoryInsertUpdateRequest);

	ResponseAsList getTaskTypeDropDownIsActive(TenantIdRequest tenantIdRequest);

}
