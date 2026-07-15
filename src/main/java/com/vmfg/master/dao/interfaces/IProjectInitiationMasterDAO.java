package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.ProjectInitiationDtlEntity;

public interface IProjectInitiationMasterDAO {

	List<ProjectInitiationDtlEntity> getProjectInitiationDtl(String tenantId);

	int updateProjectIntiationMasterMethod(String piId, String primaryPoc, String masterPoc, String depAssignment);
	

}
