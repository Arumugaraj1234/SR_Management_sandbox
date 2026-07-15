package com.vmfg.scm.services.interfaces;

import java.util.List;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.scm.entity.MaterialInwardPoDtl;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.MaterialInwardHdrRequest;

public interface IMaterialInwardService {

	ResponseAsList getMaterialInwardHdrDtls(MaterialInwardHdrRequest materialInwardHdrReq);

	ResponseAsList getMaterialInwardDtlList(HdrIdandTenantIdRequest hdrIdandTenantIdReq);

	ResponseAsMessage insertMaterialInwardDtls(List<MaterialInwardPoDtl> materialInwardPoDtl);

	ResponseAsList getPoDtlsForMaterialInward(HdrIdandTenantIdRequest hdrIdandTenantIdReq);

}
