package com.vmfg.assembly.services.interfaces;

import com.vmfg.assembly.request.GetAssyDtlRequest;
import com.vmfg.assembly.request.InsertMrHdrAndDtlRequest;
import com.vmfg.assembly.request.IsStagingRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.assembly.request.RetriveFromStockRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IAssemblyService {

	ResponseAsList	getAssyDtl(GetAssyDtlRequest getAssyDtlReq);

	ResponseAsList getMaterialReqHdr(MaterialReqHdrRequest materialHdrReq);

	ResponseAsList getMaterialReqDtl(MaterialReqHdrRequest materialHdrReq);

	ResponseAsMessage cancelMiRequestHdr(MaterialReqHdrRequest materialHdrReq);

	ResponseAsList retriveFromStock(RetriveFromStockRequest retriveFromStock);

	ResponseAsMessage insertMrHdrAndDtl(InsertMrHdrAndDtlRequest insertMrDtlsEntity);

	ResponseAsMessage retriveAssyResp(MaterialReqHdrRequest assyMstRequest);

	ResponseAsMessage retriveIsStagingStatus(IsStagingRequest isStagingReq);

	ResponseAsMessage updateIsStagingStatus(IsStagingRequest isStagingReq);
}
