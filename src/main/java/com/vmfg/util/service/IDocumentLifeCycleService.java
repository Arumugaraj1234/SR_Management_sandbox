package com.vmfg.util.service;

import java.util.List;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.scm.entity.DocLifeCycleLogRequest;
import com.vmfg.util.entity.DocLifeCycleMstLogEntity;
import com.vmfg.util.entity.DocumentLifeCycleInsertRequest;

public interface IDocumentLifeCycleService {

	ResponseAsList getDocTypes(DocLifeCycleLogRequest docLifeCycleLogReq);

	ResponseAsList getDocTypesDataList(DocLifeCycleLogRequest docReq);

	ResponseAsList getDocStatusTypes(PmHdrIdAndTenantIdRequest tenanttreq);

	ResponseAsMessage insertOrUpdateDoclifeCycle(DocumentLifeCycleInsertRequest docList);

	ResponseAsList getEmpDesignationList(TenantRequest tenReq);

	ResponseAsList getPmIdList(TenantRequest tenReq);

	ResponseAsMessage deleteDocList(PmHdrIdAndTenantIdRequest docList);

	ResponseAsList getLifeCylceMstVersionDtls(DocLifeCycleLogRequest docLifeCycleLogReq);

	ResponseAsMessage updateDocLifeCycleVersion(List<DocLifeCycleMstLogEntity> docLifeCycleMstReq);

	ResponseAsMessage setDefaultDoc(List<DocLifeCycleMstLogEntity> docLifeCycleMstReq);

	ResponseAsList getDocGroups(DocLifeCycleLogRequest docLifeCycleLogReq);

}
