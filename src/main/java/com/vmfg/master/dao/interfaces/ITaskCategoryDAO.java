package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.TaskCategoryEntity;
import com.vmfg.master.entity.TaskTypeDropDownEntity;
import com.vmfg.master.request.TaskCategoryInsertUpdateRequest;
import com.vmfg.master.request.TenantIdRequest;

public interface ITaskCategoryDAO {

	List<TaskCategoryEntity> getTaskCategory(String depCode, String tenantId);

	int insertTaskCategory(TaskCategoryInsertUpdateRequest taskCategoryInsertUpdateRequest);

	int updateTaskCategory(TaskCategoryInsertUpdateRequest taskCategoryInsertUpdateRequest);

	List<TaskTypeDropDownEntity> getTaskTypeDropDownIsActive(TenantIdRequest taskCategoryrequest);

}
