package com.vmfg.sales.services.interfaces;

import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.entity.ProjectCreationStatusEntity;
import com.vmfg.sales.request.CustomerMstRequest;
import com.vmfg.sales.request.EnqEnablementRequest;
import com.vmfg.sales.request.GetsalebudgetextDtlBySbDtlIdRequest;
import com.vmfg.sales.request.UpdateEnquiryDtlRequest;
import com.vmfg.sales.request.UpdatesalesBudgetHdrRequest;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;

public interface IEnquiryService {

	ResponseAsMessage UpdateEnquiryDtl(UpdateEnquiryDtlRequest updateEnquiryDtlReq);

	ResponseAsList uploadBudgetSheetfile(JSONArray getArray, MultipartFile file);
	

	ResponseAsMessage getProjectCreationStatus(ProjectCreationStatusEntity ProjectCreationStatus);
	
	ResponseAsList getsalesBudgetHdrDtl(ProjectCreationStatusEntity ProjectCreationStatus);

	ResponseAsMessage updatesalesBudgetHdr(UpdatesalesBudgetHdrRequest updatesalesBudgetHdrReq);
	
	ResponseAsList getsalebudgetextDtlBySbDtlId(GetsalebudgetextDtlBySbDtlIdRequest getsalebudgetextDtlBySbDtlIdReq);

	ResponseAsMessage getEnqEnablement(EnqEnablementRequest enqEnablementRequest);

	ResponseAsList getCustomerMst(CustomerMstRequest customerMstreq);

	ResponseAsMessage deleteSaleEnqContact(HdrIdandTenantIdRequest hdrIdandTenantIdReq);
	}
