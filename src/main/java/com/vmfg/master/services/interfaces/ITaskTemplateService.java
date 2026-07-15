package com.vmfg.master.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.request.TaskCategoryDrpdwnRequest;
import com.vmfg.master.request.TaskTemplateInsertUpdateRequest;
import com.vmfg.master.request.TaskTemplateTypeRequest;
import com.vmfg.master.request.TaskTemplatedtlRequest;
import com.vmfg.master.request.taskTemplateHdrRequest;

public interface ITaskTemplateService {

	ResponseAsList getTaskTypeTemplatedrpDwn(TaskTemplateTypeRequest req);

	ResponseAsList getTaskTemplatedtl(TaskTemplatedtlRequest req);

	ResponseAsList getTaskCategorydrpDwn(TaskCategoryDrpdwnRequest req);

	ResponseAsMessage insertUpdateTemplate(TaskTemplateInsertUpdateRequest request);

	ResponseAsMessage insertTemplateHdr(taskTemplateHdrRequest request);

}
