package com.vmfg.assembly.services.interfaces;

import com.vmfg.assembly.request.InsertMrHdrAndDtlReq;
import com.vmfg.assembly.request.MaterialReqDtlRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.assembly.request.MaterialReturnAcceptRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IAssemblyReturnService {

	ResponseAsList mrHdrRetrieve(MaterialReqHdrRequest materialHdrReq);

	ResponseAsList retrieveMreturnDtlByHdr(MaterialReqHdrRequest materialHdrReq);

	ResponseAsList retrieveApprovedGroupReturnsByProject(MaterialReqHdrRequest materialHdrReq);

	ResponseAsMessage cancelMaterialReturnHdr(MaterialReqHdrRequest materialReqHdr);

	ResponseAsMessage materialReturnAccept(MaterialReturnAcceptRequest materialAccept);

	ResponseAsMessage insertMRHAndMRD(InsertMrHdrAndDtlReq insertMsDtls);

	ResponseAsMessage ApproveMreturnDtls(MaterialReqDtlRequest materialDtlReq);

}
