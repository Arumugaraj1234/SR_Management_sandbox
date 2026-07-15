package com.vmfg.assembly.services.interfaces;

import com.vmfg.assembly.request.InsertMaterialIssueRequest;
import com.vmfg.assembly.request.MaterialIssueHdrRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IAssemblyIssueService {

	ResponseAsMessage insertMaterialIssueHdrAndDtl(InsertMaterialIssueRequest insertMrDtlsEntity);

	ResponseAsList getMaterialIssueHdr(MaterialIssueHdrRequest materialHdrReq);

	ResponseAsList getMaterialIssueDtl(MaterialIssueHdrRequest materialHdrReq);

	ResponseAsList retriveFromIssueStock(MaterialIssueHdrRequest retriveFromStock);

}
