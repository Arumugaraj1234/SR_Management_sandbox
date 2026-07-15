package com.vmfg.scm.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.scm.request.*;

public interface IGrnService {

	ResponseAsList getGrnHdrDetails(GetGrnHdrRequest getGrnHdrRequest);

	ResponseAsList getPoCostType(GetPoCostTypeReq getPoCostTypeReq);

	ResponseAsList getGrnDtlwithMaterialInwardDtl(GrnDtlRequest grnDtlRequest);

	ResponseAsMessage insertGrnHdrAndDtl(GrnHdrInsertRequest grnHdrAndDtl);
	
	ResponseAsMessage insertQtyInspReq(InsertQtyInspRequest insertQtyInspReq);
	
	ResponseAsMessage insertMIQtyReq(InsertQtyInspRequest insertQtyInspReq);

	ResponseAsMessage grnCancel(GrnCancelReq grnCancelReq);

}
