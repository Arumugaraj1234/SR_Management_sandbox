package com.vmfg.assembly.services.interfaces;

import com.vmfg.assembly.request.InsertMsHdrAndDtlRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IAssemblyStagingService {

	ResponseAsList msHdrRetrieve(MaterialReqHdrRequest materialHdrReq);

	ResponseAsList msHdrRetrieveAll(MaterialReqHdrRequest materialHdrReq);

	ResponseAsList retrieveMSDtlByHdr(MaterialReqHdrRequest materialHdrReq);

	ResponseAsMessage insertMsHdrAndDtl(InsertMsHdrAndDtlRequest insertMsDtls);

	ResponseAsMessage cancelMsHdrReq(MaterialReqHdrRequest materialReqHdr);

	ResponseAsMessage useMsHdrForReturn(MaterialReqHdrRequest materialReqHdr);

	ResponseAsList retrieveForMS(MaterialReqHdrRequest materialHdrReq);

}
