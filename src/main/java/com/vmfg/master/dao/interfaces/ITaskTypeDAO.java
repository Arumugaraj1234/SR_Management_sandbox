package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.TaskTypeEntity;

public interface ITaskTypeDAO {

	List<TaskTypeEntity> getTasktypeDtls(String deptCode, String tenantId);

	int insertTaskType(String deptCode, String tenantId, String ttDesc, String isActive);

	int updateTaskType(String ttCode, String ttDesc, String tenantId, String isActive);

}
