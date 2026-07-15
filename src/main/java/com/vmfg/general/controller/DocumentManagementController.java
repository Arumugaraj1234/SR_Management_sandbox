package com.vmfg.general.controller;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
import com.vmfg.general.services.interfaces.IDocumentManagementService;
import com.vmfg.util.CommonBase64Class;

@Controller
@RequestMapping("/")
public class DocumentManagementController {
	private static final Logger logger = LoggerFactory.getLogger(DocumentManagementController.class);

	@Autowired
	private IDocumentManagementService iDocumentManagementService;



	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDocumentManagementDetails")
	public ResponseEntity<List<DocumentManagementEntity>> getDocumentManagementDetails(@RequestBody DocumentManagementRequest documentManagementRequest) {
		logger.info("getDocumentManagementDetails   method Start");
		List<DocumentManagementEntity> documentManagementEntity=null;
		try {

			documentManagementEntity = iDocumentManagementService.getDocumentManagementDetails(documentManagementRequest);

		} catch (Exception ex) {
			logger.error("getDocumentManagementDetails  method  exception" + ex);
		}
		logger.debug("getDocumentManagementDetails   method end");
		return new ResponseEntity<List<DocumentManagementEntity>>(documentManagementEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("documentDownloadDocFile")
	public ResponseEntity<FileManagerDownloadEntity> documentDownloadDocFile(@RequestBody  FileManagerDownloadRequest fileManagerDownloadRequest) {
		logger.info("documentDownloadDocFile   method Start");
		FileManagerDownloadEntity fdEntity = null;
		try {

			fdEntity = iDocumentManagementService.documentDownloadDocFile(fileManagerDownloadRequest);
			logger.info("documentDownloadDocFile method End");
			if(fdEntity != null) {
				
					fdEntity.setFileContent(CommonBase64Class.getBasesBinary(new File(fdEntity.getFilePath())));
				
			}
		} catch (Exception ex) {
			logger.error("documentDownloadDocFile Method Exception" + ex);
		}
		return new ResponseEntity<FileManagerDownloadEntity>(fdEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getdocumentManagementAccessDtl")
	public ResponseEntity<List<DocumentManagementAccessEntity>> getdocumentManagementAccessDtl(@RequestBody  DocumentManagementAccessRequest documentManagementAccessRequest) {
		logger.info("getdocumentManagementAccessDtl   method Start");
		List<DocumentManagementAccessEntity> documentManagementAccessEntity = null;
		try {

			documentManagementAccessEntity = iDocumentManagementService.getdocumentManagementAccessDtl(documentManagementAccessRequest);
			logger.info("getdocumentManagementAccessDtl method End");

		} catch (Exception ex) {
			logger.error("getdocumentManagementAccessDtl Method Exception" + ex);
		}
		return new ResponseEntity<List<DocumentManagementAccessEntity>>(documentManagementAccessEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertDocumentManagementAccessDtl")
	public ResponseEntity<ResponseAsMessage> insertDocumentManagementAccessDtl(@RequestBody  List<SaveDocumentManagementAccessRequest> saveDocumentManagementAccessRequest) {
		logger.info("insertDocumentManagementAccessDtl   method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iDocumentManagementService.insertDocumentManagementAccessDtl(saveDocumentManagementAccessRequest);
			logger.info("insertDocumentManagementAccessDtl method End");

		} catch (Exception ex) {
			logger.error("insertDocumentManagementAccessDtl Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}


	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteDocumentManagementAccessDtl")
	public ResponseEntity<ResponseAsMessage> deleteDocumentManagementAccessDtl(@RequestBody  List<DeleteDocumentManagementAccessRequest> DocumentManagementAccessRequest) {
		logger.info("deleteDocumentManagementAccessDtl   method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iDocumentManagementService.deleteDocumentManagementAccessDtl(DocumentManagementAccessRequest);
			logger.info("deleteDocumentManagementAccessDtl method End");

		} catch (Exception ex) {
			logger.error("deleteDocumentManagementAccessDtl Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
	

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDocumentManagementDetailsById")
	public ResponseEntity<ResponseAsList> getDocumentManagementDetailsById(@RequestBody DocumentManagementByIdRequest documentManagementRequest) {
		logger.info("getDocumentManagementDetailsById   method Start");
		ResponseAsList documentManagementEntity=null;
		try {

			documentManagementEntity = iDocumentManagementService.getDocumentManagementDetailsById(documentManagementRequest);

		} catch (Exception ex) {
			logger.error("getDocumentManagementDetailsById  method  exception" + ex);
		}
		logger.debug("getDocumentManagementDetailsById   method end");
		return new ResponseEntity<ResponseAsList>(documentManagementEntity, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteUploadDocument")
	public ResponseEntity<ResponseAsMessage> deleteUploadDocument(@RequestBody  List<DeleteDocumentManagementAccessRequest> DocumentManagementAccessRequest) {
		logger.info("deleteUploadDocument   method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iDocumentManagementService.deleteUploadDocument(DocumentManagementAccessRequest);
			logger.info("deleteUploadDocument method End");

		} catch (Exception ex) {
			logger.error("deleteUploadDocument Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}
	

}
