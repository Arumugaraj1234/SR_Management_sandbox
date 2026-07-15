package com.vmfg.master.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.request.TaskTypeRequest;
import com.vmfg.master.request.insertUpdateTaskTypeRequest;

public interface ITaskTypeService {

	ResponseAsList getTasktypeDtls(TaskTypeRequest taskType);

	ResponseAsMessage insertUpdateTaskType(insertUpdateTaskTypeRequest insertDtlreq);

}
