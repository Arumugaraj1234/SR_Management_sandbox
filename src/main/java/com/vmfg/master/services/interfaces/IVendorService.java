package com.vmfg.master.services.interfaces;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.entity.CustomerMstEntity;
import com.vmfg.master.request.CustomerComplaintCheck;
import com.vmfg.master.request.InsertVendorReq;
import com.vmfg.master.request.VendorAllDtlReq;
import com.vmfg.master.request.VendorApprDtlReq;
import com.vmfg.master.request.VendorInspRatingRequest;

public interface IVendorService {

	ResponseAsList getApprVendorDtls(VendorApprDtlReq vendorApprDtlReq);

	ResponseAsMessage insertVendorDtls(InsertVendorReq vendorInsertDtlReq);

	ResponseAsList getAllVendorDtls(VendorAllDtlReq vendorApprDtlReq);

	ResponseAsList getAllCustomerDtl(TenantRequest tenantRequest);

	ResponseAsList getVendorCategory(TenantRequest tenantRequest);

	ResponseAsList getVendorInspRatingDtls(VendorInspRatingRequest req);

	ResponseAsMessage updateInspectionRaised(VendorInspRatingRequest req);

	ResponseAsMessage vendorDtlInsert(JSONObject jsonObj, MultipartFile file); 

	ResponseAsMessage customerComplaintCheck(CustomerComplaintCheck customerComplaintCheck);

	ResponseAsMessage updateCustomerdtl(CustomerMstEntity updateCus);

}
