package com.vmfg.scm.services.interfaces;

import java.util.List;

import com.vmfg.design.request.IdAndTenantIdRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.scm.entity.GetPoDtlsEntity;
import com.vmfg.scm.entity.ScpDtlsEntity;
import com.vmfg.scm.request.DeleteIndScpDtlIdRequest;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.IndentGrpDelRequest;
import com.vmfg.scm.request.IndentGrpDtlRequest;
import com.vmfg.scm.request.IndentInsertGrpRequest;
import com.vmfg.scm.request.IndentTemplateNameRequest;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.scm.request.getIndentQtyDtlRequest;

public interface IIndentGroupService {
	ResponseAsList getIndentGroupDetails(IndentGrpDtlRequest indentGrpDtlReq);

	ResponseAsMessage delIndentGrpDtl(IndentGrpDelRequest indentGrpDtlReq);

	ResponseAsList getIndentGroupHdrAndDtl(IndentGrpDelRequest indentGrpDtlReq);

	ResponseAsMessage checkTemplateName(IndentTemplateNameRequest indentTempName);

	ResponseAsMessage insertTempGrup(IndentInsertGrpRequest indentTempName);
	
	ResponseAsList getIndentQtyDtl(getIndentQtyDtlRequest getIndentQtyDtlReq);

	ResponseAsList getScpDtlsByIgHdrId(IdAndTenantIdRequest idAndTenantIdReq);

	ResponseAsMessage insertScpDtlsByIgHdrId(List<ScpDtlsEntity> scpDtlsEntity);

	ResponseAsMessage updateScpSeqAndStatus(UpdateSeqAndStatusRequest updateHdrReq);
	
	ResponseAsMessage deleteIndScpDtlId(DeleteIndScpDtlIdRequest deleteIndScpDtlIdreq);

	ResponseAsList getIndentGroupDtlsForSCS(HdrIdandTenantIdRequest hdrIdandTenantIdReq);

	GetPoDtlsEntity getPoDtlsByScsId(String igScpId, String tenantId, String empId, String pmHdrId, String pmId,
			String masterId);

}
