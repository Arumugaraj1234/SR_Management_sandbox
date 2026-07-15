package com.vmfg.util.dao;

import java.util.List;

import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.scm.entity.DocLifeCycleLogRequest;
import com.vmfg.util.entity.DocGroupTypeEntity;
import com.vmfg.util.entity.DocLifeCycleListEntity;
import com.vmfg.util.entity.DocLifeCycleMstLogEntity;
import com.vmfg.util.entity.DocLifecycleVersionEntity;
import com.vmfg.util.entity.DocumentTypeEntity;
import com.vmfg.util.entity.EmployeeDesignationEntity;

public interface IDocumentLifeCycleDAO {

	List<DocumentTypeEntity> getDocStatusTypes(PmHdrIdAndTenantIdRequest tenanttreq);

	List<DocLifeCycleListEntity> getDocTypesDataList(DocLifeCycleLogRequest docReq);

	int insertDocList(DocLifeCycleMstLogEntity docLifeCycleMstLogEntity);

	int updateDocList(DocLifeCycleListEntity docLifeCycleListEntity);

	List<EmployeeDesignationEntity> getEmpDesigList(String tenantID);

	List<EmployeeDesignationEntity> getPmIdList(String tenantID);

	int deleteDocList(PmHdrIdAndTenantIdRequest docList);

	List<DocLifeCycleMstLogEntity> getLifeCylceMstVersionDtls(String docGroup, String processCode, String docType,
			String tenantId, String version);

	 List<String> getDesignationDesc(String designationCode);

	List<DocLifecycleVersionEntity> getVersionList(String docGroup, String processCode, String docType,
			String tenantId);

	String getDocVersion(String docGroup, String processCode, String docType, String tenantId);

	int insertDocLifecycleMstLog(DocLifeCycleMstLogEntity docLifeCycleMstLogEntity, String version);

	int deleteDocLifecycleMstList(String docGroup, String processCode, String docType, String tenantId);

	int getDoclifecycleCount(String docGroup, String processCode, String docType, String tenantId);

	List<DocumentTypeEntity> getDocTypes(String processCode, String tenantId);

	List<DocGroupTypeEntity> getDocGroups(String processCode, String tenantId, String docType);

	String getStatusCode(String statusDesc, String tenantId);

}
