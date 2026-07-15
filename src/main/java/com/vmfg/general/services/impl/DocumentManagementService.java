package com.vmfg.general.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.dao.interfaces.IDocumentManagementDAO;
import com.vmfg.general.dao.interfaces.IStageManagementDAO;
import com.vmfg.general.entity.DocumentManagementAccessEntity;
import com.vmfg.general.entity.DocumentManagementEntity;
import com.vmfg.general.entity.FileManagerDownloadEntity;
import com.vmfg.general.entity.ProjectWbsInitiationMst;
import com.vmfg.general.request.DeleteDocumentManagementAccessRequest;
import com.vmfg.general.request.DocumentManagementAccessRequest;
import com.vmfg.general.request.DocumentManagementByIdRequest;
import com.vmfg.general.request.DocumentManagementRequest;
import com.vmfg.general.request.FileManagerDownloadRequest;
import com.vmfg.general.request.InitiateProcessRequest;
import com.vmfg.general.request.SaveDocumentManagementAccessRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.interfaces.IDocumentManagementService;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.sales.dao.interfaces.IEnquiryDAO;
import com.vmfg.sales.dao.interfaces.IUploadManagementDAO;
import com.vmfg.sales.entity.DocumentManagementTblEntity;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class DocumentManagementService implements IDocumentManagementService {
	private static final Logger logger = LoggerFactory.getLogger(DocumentManagementService.class);

	@Autowired
	private IDocumentManagementDAO iDocumentManagementDAO;

	@Autowired
	UploadManagementDAO deptM;
	
	@Autowired
	IUploadManagementDAO UploadManagementDAO;
	
	@Autowired
	CommonNotifyMethod commonNotifyMethod;
	
	@Autowired
	IStageManagementDAO iStageManagementDAO;
	
	@Autowired
	StageManagementDAO stageManagementDAO;
	
	@Autowired
	IEnquiryDAO iEnquiryDAO;
	
	@Autowired
	IndentUploadDAO indentUploadDAO;
	
	@Autowired
	IndentGroupDAO iIndentGroupDAO;
	
	@Override
	public List<DocumentManagementEntity> getDocumentManagementDetails(
			DocumentManagementRequest documentManagementRequest) {
		List<DocumentManagementEntity> documentManagementEntity = null;
		logger.info("getDocumentManagementDetails  method start");
		try {

			String TENANT_ID = documentManagementRequest.getTenantId();
			String ENQUIRY_ID = documentManagementRequest.getEnquiryId();
			String PROJECT_D = documentManagementRequest.getProjectId();
			String Emp_ID = documentManagementRequest.getEmpId();

			// SERVICES

			if ((null != TENANT_ID && !TENANT_ID.isEmpty()) && !ENQUIRY_ID.isEmpty() || !PROJECT_D.isEmpty()) {
				documentManagementEntity = iDocumentManagementDAO.getDocumentManagementDetails(TENANT_ID, ENQUIRY_ID,
						PROJECT_D, Emp_ID);
				for(int i=0;i<documentManagementEntity.size();i++) {
				String accessDesc = iDocumentManagementDAO.getAccessDeptByDmId(documentManagementEntity.get(i).getDmId(),TENANT_ID);
				documentManagementEntity.get(i).setAccessDesc(accessDesc);
				if(!documentManagementEntity.get(i).getDmId().equalsIgnoreCase("")) {
					String isPdf = iIndentGroupDAO.getIsPdfOrNot(documentManagementEntity.get(i).getDmId());
					documentManagementEntity.get(i).setIsPdf(isPdf);
				}
				
				}
			}
			
		} catch (Exception ex) {
			logger.error("getDocumentManagementDetails  method exception-->" + ex);
		}
		logger.debug("getDocumentManagementDetails  method end");
		return documentManagementEntity;
	}

	@Override
	public FileManagerDownloadEntity documentDownloadDocFile(FileManagerDownloadRequest fileManagerDownloadRequest) {
		FileManagerDownloadEntity fdEntity = null;
		logger.info("documentDownloadDocFile  method start");
		try {

			String TENANT_ID = fileManagerDownloadRequest.getTenantId();
			String REFERNCE_ID = fileManagerDownloadRequest.getReferenceId();

			// SERVICES

			if ((null != TENANT_ID && !TENANT_ID.isEmpty()) && !REFERNCE_ID.isEmpty()) {
				fdEntity = iDocumentManagementDAO.documentDownloadDocFile(TENANT_ID,  REFERNCE_ID);
			}

		} catch (Exception ex) {
			logger.error("documentDownloadDocFile  method exception-->" + ex);
		}
		logger.debug("documentDownloadDocFile  method end");
		return fdEntity;
	}

	@Override
	public List<DocumentManagementAccessEntity> getdocumentManagementAccessDtl(
			DocumentManagementAccessRequest documentManagementAccessRequest) {

		List<DocumentManagementAccessEntity> fdEntity = null;
		logger.info("getdocumentManagementAccessDtl  method start");
		try {

			String TENANT_ID = documentManagementAccessRequest.getTenantId();
			String DM_ID = documentManagementAccessRequest.getDmId();

			// SERVICES

			if ((null != TENANT_ID && !TENANT_ID.isEmpty()) && !DM_ID.isEmpty()) {
				fdEntity = iDocumentManagementDAO.getdocumentManagementAccessDtl(TENANT_ID, DM_ID);
			}

		} catch (Exception ex) {
			logger.error("getdocumentManagementAccessDtl  method exception-->" + ex);
		}
		logger.debug("getdocumentManagementAccessDtl  method end");
		return fdEntity;
	}

	@Override
	public ResponseAsMessage insertDocumentManagementAccessDtl(
			List<SaveDocumentManagementAccessRequest> saveDocumentManagementAccessRequest) {
		logger.info("insertDocumentManagementAccessDtl  method start");
		ResponseAsMessage returnnMsg = null;
		try {

			// SERVICES
			for (SaveDocumentManagementAccessRequest obj : saveDocumentManagementAccessRequest) {
				String TENANT_ID = obj.getTenantId();
				String DM_ID = obj.getDmId();
				String DEPT_CODE = obj.getDeptCode();
				
				if ((!TENANT_ID.isEmpty()) && !DM_ID.isEmpty() && !DEPT_CODE.isEmpty()) {
					int checkRecord =iDocumentManagementDAO.getDeptCountByDmId(DM_ID, DEPT_CODE);
					if(checkRecord ==0) {
					returnnMsg = iDocumentManagementDAO.insertDocumentManagementAccessDtl(TENANT_ID, DM_ID, DEPT_CODE);
					
					List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
					
					List<DocumentManagementTblEntity> docmanagement=	UploadManagementDAO.getDocDlsByDmId(obj.getDmId(), obj.getTenantId(),"");
					InitiateProcessRequest req = new InitiateProcessRequest();
					req.setDeptCode(DEPT_CODE);
					req.setTenantId(TENANT_ID);
			
					List<ProjectWbsInitiationMst> 	PMdetails = iStageManagementDAO.getPMFromDept(req);
					String mstId="";
					if(docmanagement.get(0).getProjectId() ==null) {
						 mstId=	stageManagementDAO.checkMasterInfo(PMdetails.get(0).getPmId(), docmanagement.get(0).getProjectId(), TENANT_ID,1);
							String enqCode =iEnquiryDAO.getsaleEnquiryCode(mstId);
						 messageList.add("Enquiry "+enqCode); 
					}else {
						 mstId=	stageManagementDAO.checkMasterInfo(PMdetails.get(0).getPmId(), docmanagement.get(0).getEnquiryId(), TENANT_ID,0);
						 String projCode = indentUploadDAO.getProjectCodeByProjId(docmanagement.get(0).getProjectId(), TENANT_ID);
						 messageList.add("Project "+projCode); 
					}
					messageList.add(iDocumentManagementDAO.getDepartmentDescByDepartmentCode(DEPT_CODE));
					messageList.add(iDocumentManagementDAO.getEmpIdByDeptCode(DEPT_CODE));
					messageList.add("Document");
					otherEmp.add(iDocumentManagementDAO.getEmpIdByDeptCode(DEPT_CODE));
					commonNotifyMethod.InvokeNotificationMethod(2, 15, null, obj.getTenantId(), messageList, otherEmp, "0", PMdetails.get(0).getPmId(), mstId,PMdetails.get(0).getPrimaryDoc());
				
					}
					}
			}
		} catch (Exception ex) {
			logger.error("insertDocumentManagementAccessDtl  method exception-->" + ex);
		}
		logger.debug("insertDocumentManagementAccessDtl  method end");
		return returnnMsg;
	}

	@Override
	public ResponseAsMessage deleteDocumentManagementAccessDtl(
			List<DeleteDocumentManagementAccessRequest> documentManagementAccessRequest) {
		logger.info("deleteDocumentManagementAccessDtl  method start");
		ResponseAsMessage returnnMsg = new ResponseAsMessage();
		try {

			// SERVICES
			for (DeleteDocumentManagementAccessRequest obj : documentManagementAccessRequest) {

				String DMA_ID = obj.getDmaId();
			
				if (!DMA_ID.isEmpty()) {
					String getDeptByDmaId = iDocumentManagementDAO.getDeptByDmaId(DMA_ID);
					String empDept = deptM.getDepCodeByEmpId(obj.getEmpId(), obj.getTenantId());
					if(!empDept.equalsIgnoreCase(getDeptByDmaId)) {
					returnnMsg = iDocumentManagementDAO.deleteDocumentManagementAccessDtl(DMA_ID);
				
					}else {
						returnnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
						returnnMsg.setResponseDataMessage("Employee can not remove their own department's access.");
					}
				}
			}
		} catch (Exception ex) {
			logger.error("deleteDocumentManagementAccessDtl  method exception-->" + ex);
		}
		logger.debug("deleteDocumentManagementAccessDtl  method end");
		return returnnMsg;
	}

	@Override
	public ResponseAsList getDocumentManagementDetailsById(DocumentManagementByIdRequest documentManagementRequest) {
		ResponseAsList resp = new ResponseAsList();
		List<DocumentManagementEntity> documentManagementEntity = null;
		logger.info("getDocumentManagementDetailsById  method start");
		try {

			String TENANT_ID = documentManagementRequest.getTenantId();
			String refId = documentManagementRequest.getRefId();
			String stgCode = documentManagementRequest.getStgCode();

			// SERVICES
			documentManagementEntity = iDocumentManagementDAO.getDocumentManagementDetailsById(TENANT_ID, refId,
					stgCode);

			if (documentManagementEntity.size() > 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.success);
				resp.setResponseData(documentManagementEntity);
			} else {
				resp.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				resp.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getDocumentManagementDetails  method exception-->" + ex);
		}
		logger.debug("getDocumentManagementDetails  method end");
		return resp;
	}

	@Override
	public ResponseAsMessage deleteUploadDocument(
			List<DeleteDocumentManagementAccessRequest> documentManagementAccessRequest) {
		logger.info("deleteUploadDocument  method start");
		ResponseAsMessage returnnMsg = new ResponseAsMessage();
		try {
				String DMA_ID = documentManagementAccessRequest.get(0).getDmaId();
				if (!DMA_ID.isEmpty()) {
					returnnMsg = iDocumentManagementDAO.deleteDocumentManagementandAccessDtl(DMA_ID);
				}
		} catch (Exception ex) {
			logger.error("deleteUploadDocument  method exception-->" + ex);
		}
		logger.debug("deleteUploadDocument  method end");
		return returnnMsg;
	}

}
