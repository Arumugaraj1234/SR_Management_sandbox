package com.vmfg.general.services.interfaces;

import java.util.List;

import com.vmfg.general.entity.DocumentManagementAccessEntity;
import com.vmfg.general.entity.DocumentManagementEntity;
import com.vmfg.general.entity.FileManagerDownloadEntity;
import com.vmfg.general.request.DeleteDocumentManagementAccessRequest;
import com.vmfg.general.request.DocumentManagementAccessRequest;
import com.vmfg.general.request.DocumentManagementByIdRequest;
import com.vmfg.general.request.DocumentManagementRequest;
import com.vmfg.general.request.FileManagerDownloadRequest;
import com.vmfg.general.request.SaveDocumentManagementAccessRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IDocumentManagementService {

	List<DocumentManagementEntity> getDocumentManagementDetails(DocumentManagementRequest documentManagementRequest);

	FileManagerDownloadEntity documentDownloadDocFile(FileManagerDownloadRequest fileManagerDownloadRequest);

	List<DocumentManagementAccessEntity> getdocumentManagementAccessDtl(
			DocumentManagementAccessRequest fileManagerDownloadRequest);

	ResponseAsMessage insertDocumentManagementAccessDtl(
			List<SaveDocumentManagementAccessRequest> saveDocumentManagementAccessRequest);

	ResponseAsMessage deleteDocumentManagementAccessDtl(
			List<DeleteDocumentManagementAccessRequest> documentManagementAccessRequest);

	ResponseAsList getDocumentManagementDetailsById(
			DocumentManagementByIdRequest documentManagementRequest);

	ResponseAsMessage deleteUploadDocument(List<DeleteDocumentManagementAccessRequest> documentManagementAccessRequest);

}
