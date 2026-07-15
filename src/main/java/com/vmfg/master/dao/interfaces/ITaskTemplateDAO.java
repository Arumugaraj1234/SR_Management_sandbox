package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.TaskCategoryDrpdwnEntity;
import com.vmfg.master.entity.TemplateDtlMstEntity;
import com.vmfg.master.entity.TemplateTypeMstEntity;

public interface ITaskTemplateDAO {

	List<TemplateTypeMstEntity> getTaskTypeTemplatedrpDwn(String deptCode, String ttCode, String tcCode, String tenantId);

	List<TemplateDtlMstEntity> getTaskTemplatedtl(String ttHdrId, String tenantId, String isActive);

	List<TaskCategoryDrpdwnEntity> getTaskCategorydrpDwn(String deptCode, String ttCode);

	int insertTaskTemplate(String ttHdrId, String actName, String isActive, String empId, String tenantId);

	int updateTaskTemplate(String ttDtlId, String ttHdrId, String isActive, String empId, String tenantId, String actName);

	int insertTemplateHdr(String tempName, String empId, String deptCode, String ttCode, String tcCode,
			String tenantId, String isActive);

}
