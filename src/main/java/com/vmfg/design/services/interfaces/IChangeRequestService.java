package com.vmfg.design.services.interfaces;

import com.vmfg.design.entity.IndentPartDetailsEntity;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.entity.ChangeRequestDtlEntity;
import com.vmfg.design.entity.GetKeyIndentDtRequest;
import com.vmfg.design.request.GetChangeRequestDtlByPmIdRequest;
import com.vmfg.design.request.GetKeyAreaDtlsRequest;
import com.vmfg.design.request.GetKeyIndentDtl;
import com.vmfg.design.request.UpdateChangeRequestDtlRequest;
import com.vmfg.design.request.UpdateHdrSeqAndStatusRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IChangeRequestService {

	ResponseAsMessage updateChangeRequestDtl(UpdateChangeRequestDtlRequest updateChangeRequestDtlReq);
	
	ResponseAsList	getChangeRequestDtlByPmId(GetChangeRequestDtlByPmIdRequest getChangeRequestDtlByPmIdReq);

	ResponseAsMessage updateDesignerComments(ChangeRequestDtlEntity changeRequestDtl);

	ResponseAsMessage updateChangeReqHdrSeqAndStatus(UpdateHdrSeqAndStatusRequest updateHdrSeqAndStatusReq);

	ResponseAsMessage insertChangeRequestFile(JSONObject jsonObj, MultipartFile file);

	ResponseAsMessage updateFileByDmId(JSONObject jsonObj, MultipartFile file);

	ResponseAsList getChangeReqHdrDtlsByProdCode(GetKeyAreaDtlsRequest getKeyAreaDtlsReq);

	ResponseAsList getChangeReqIndentHdrByProdId(GetKeyIndentDtRequest getKeyIndentDtlReq);

	ResponseAsList getChangeReqIndentDtlByIndentId(GetKeyIndentDtl getKeyIndentDtl);

    IndentPartDetailsEntity getIndentDetailsByCode(Integer indentId);
}
