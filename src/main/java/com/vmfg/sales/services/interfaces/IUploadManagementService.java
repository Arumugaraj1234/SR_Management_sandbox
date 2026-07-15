package com.vmfg.sales.services.interfaces;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.sales.request.ApprovedBtnRequest;
import com.vmfg.sales.request.ChangeRequest;
import com.vmfg.sales.request.GetApprovedDocRequest;
import com.vmfg.sales.request.GetVersionRequest;
import com.vmfg.sales.request.getFileConfigDtlRequest;

public interface IUploadManagementService {

	ResponseAsList getApprovedDocDtl(GetApprovedDocRequest getApprovedDocReq);

	ResponseAsList getVersionDtls(GetVersionRequest getVersionReq);


	ResponseAsList getFileUploadConfigDtl(getFileConfigDtlRequest getFileConfigDtlReq);

	ResponseAsMessage addDocument(JSONObject obj, MultipartFile file);

	ResponseAsMessage sumbitApprovedDoc(ApprovedBtnRequest approvedBtnReq);

	ResponseAsList getChangeRequestInfo(ChangeRequest getChangeReq);



}
