package com.vmfg.scm.services.interfaces;

import com.vmfg.design.request.IdAndTenantIdRequest;
import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.sales.request.GetAddressDtlByDcTypeRequest;
import com.vmfg.scm.entity.DcHdrEntity;
import com.vmfg.scm.entity.GetPoDtlsEntity;
import com.vmfg.scm.request.GetDcDtlByDcIdRequest;
import com.vmfg.scm.request.GetpoInstoreDtlByPmIdRequest;
import com.vmfg.scm.request.IndentGrpDtlRequest;
import com.vmfg.scm.request.PoHSNCodeRequest;
import com.vmfg.scm.request.PoTypeUpdateReq;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.scm.request.getDCProductDropDownRequest;

public interface IPoServices {

	ResponseAsList getPoHdrDtlsByIndentId(IdAndTenantIdRequest idAndTenantIdReq);

	ResponseAsList getPoDtlsByPoId(IdAndTenantIdRequest idAndTenantIdReq);

	ResponseAsMessage insertPoHdrDtl(GetPoDtlsEntity insertPoDtlsEntity);

	ResponseAsMessage updatePoSeqAndStatus(UpdateSeqAndStatusRequest updatePoDtlsEntity);

	ResponseAsList getPoDtlsByDateAndPoId(IndentGrpDtlRequest indentGrpDtlReq);

	ResponseAsList getDCTypeDtl();
	
	ResponseAsList getpoInstoreDtlByPmId(GetpoInstoreDtlByPmIdRequest getpoInstoreDtlByPmIdReq);

	ResponseAsList getAddressDtlByDcType(GetAddressDtlByDcTypeRequest getAddressDtlByDcTypeReq);
	
	ResponseAsList getAllDcHdrByPmId(PmHdrIdAndTenantIdRequest pmIdAndTenantId);
	
	ResponseAsList getDcDtlByDcId(GetDcDtlByDcIdRequest getDcDtlByDcIdReq);
	
	ResponseAsMessage insertDcDtl(DcHdrEntity dcHdrReq);
	
	ResponseAsMessage cancelDcHdr(GetDcDtlByDcIdRequest getDcDtlByDcIdReq);

	ResponseAsMessage updatePoType(PoTypeUpdateReq poTypeReq);

	ResponseAsList getDCProductDropDown(getDCProductDropDownRequest getDCProductDropDownReq);
	
	ResponseAsList getHsnCodeByPartNo(PoHSNCodeRequest pohsnCodeReq);
	
	ResponseAsList getdivisionDesc(TenantRequest tenantReq);
	ResponseAsList getTransitInsuranceDesc(TenantRequest tenantReq);
	ResponseAsList getModeOfDispatchDesc(TenantRequest tenantReq);
	ResponseAsList getInspectScopeDesc(TenantRequest tenantReq);
	
}
