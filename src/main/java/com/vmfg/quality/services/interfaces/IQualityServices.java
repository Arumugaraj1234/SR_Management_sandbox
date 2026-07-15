package com.vmfg.quality.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.quality.request.GetConfigNameReq;
import com.vmfg.quality.request.GetInspTypeReq;
import com.vmfg.quality.request.GetQtyDtlRequest;
import com.vmfg.quality.request.GetQtyInspectionHdrRequest;
import com.vmfg.quality.request.GetqtyInspecDocDtlRequest;
import com.vmfg.quality.request.RetieveQCInspectionHdrReq;
import com.vmfg.quality.request.RetrieveQualitInspectionReq;

public interface IQualityServices {
	
	ResponseAsList getQtyDtl(GetQtyDtlRequest getQtyDtlReq);

	ResponseAsList retrieveQualitInspectionReq(RetrieveQualitInspectionReq retrieveQualitInspectionReq);

	ResponseAsList retieveQCInspectionHdr(RetieveQCInspectionHdrReq retieveQCInspection);

	ResponseAsList getInspType(GetInspTypeReq inspecType);

	ResponseAsList getConfigName(GetConfigNameReq inspecType);
	
	ResponseAsList getQtyInspectionHdr(GetQtyInspectionHdrRequest getQtyInspectionReq);
	
	ResponseAsList getqtyInspecDocDtl(GetqtyInspecDocDtlRequest getqtyInspecDocDtlReq);

}
