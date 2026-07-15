package com.vmfg.master.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.master.request.DocTypeMstRequest;
import com.vmfg.master.request.FileUploadConfigRequest;
import com.vmfg.master.request.InsertFileUploadConfigRequest;

public interface IFileUploadConfigService {

	ResponseAsList docTypeMstDropDwn(DocTypeMstRequest req);

	ResponseAsList getFileUploadConfig(FileUploadConfigRequest fileUpload);

	ResponseAsMessage insertUpdateFileUploadConfig(InsertFileUploadConfigRequest insertDtlreq);
	
}