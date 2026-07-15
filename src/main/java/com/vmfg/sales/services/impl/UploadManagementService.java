package com.vmfg.sales.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.sales.dao.interfaces.IEnquiryDAO;
import com.vmfg.sales.dao.interfaces.IUploadManagementDAO;
import com.vmfg.sales.entity.ApprovedDocEntity;
import com.vmfg.sales.entity.ChangeRequestHdrInfoEntity;
import com.vmfg.sales.entity.DocumentAppStatusDtlEntity;
import com.vmfg.sales.entity.DocumentManagementTblEntity;
import com.vmfg.sales.entity.FileUploadConfigtblEntity;
import com.vmfg.sales.request.ApprovedBtnRequest;
import com.vmfg.sales.request.ChangeRequest;
import com.vmfg.sales.request.GetApprovedDocRequest;
import com.vmfg.sales.request.GetVersionRequest;
import com.vmfg.sales.request.getFileConfigDtlRequest;
import com.vmfg.sales.services.interfaces.IUploadManagementService;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonNotifyMethod;
import com.vmfg.util.NotificationRequest;

@Service
public class UploadManagementService implements IUploadManagementService {
	private static final Logger logger = LoggerFactory.getLogger(UploadManagementService.class);

	@Autowired
	IUploadManagementDAO iUploadManagementDAO;

	@Autowired
	private DesignTaskDAO DesignTaskDAO;

	@Autowired
	private StageManagementDAO stageManagementDAO;
	
	@Autowired
	UploadManagementDAO uploadManagementDAO;
	
	@Autowired
	NotificationRequest notificationReq;
	
	@Autowired
	IEnquiryDAO iEnquiryDAO;

	@Autowired
	IndentUploadDAO indentUploadDAO;
	
	@Autowired
	CommonNotifyMethod  commonNotifyMethod;
	
	@Override
	public ResponseAsList getApprovedDocDtl(GetApprovedDocRequest getApprovedDocReq) {
		ResponseAsList list = new ResponseAsList();
		List<ApprovedDocEntity> docList = new ArrayList<ApprovedDocEntity>();
	//	List<DocumentAppStatusDtlEntity> appList = new ArrayList<DocumentAppStatusDtlEntity>();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String enquiryId = getApprovedDocReq.getEnquiryId();
			String stageCode = getApprovedDocReq.getStageCode();
			String approved = getApprovedDocReq.getApproved();
			String tenantId = getApprovedDocReq.getTenantId();
			String empId = getApprovedDocReq.getEmpId();
			String docTypeCode = getApprovedDocReq.getDocTypeCode();
			int apprBtnEnableStatus = 0;

			String designCode = iUploadManagementDAO.getDesigCodeByEmpId(empId, tenantId);
			docList = iUploadManagementDAO.getApprovedDocDtl(enquiryId, stageCode, approved, tenantId,docTypeCode);
			if (docList.size() > 0) {
				for (int i = 0; i < docList.size(); i++) {
					List<DocumentAppStatusDtlEntity> appList = new ArrayList<DocumentAppStatusDtlEntity>();
					List<ApprovedDocEntity> alldmId=uploadManagementDAO.getAllDmId(tenantId, docList.get(i).getUploadDocType(), docList.get(i).getReferenceId(), docList.get(i).getStageCode());
					for(int j= 0;j<alldmId.size();j++) {
						List<DocumentAppStatusDtlEntity>appLists = iUploadManagementDAO.getDocStatusList(alldmId.get(j).getDmId(), tenantId);
						appList.addAll(appLists);
					}
				//	appList = iUploadManagementDAO.getDocStatusList(docList.get(i).getDmId(), tenantId);
					docList.get(i).setApprovalDetails(appList);

					if (approved.equalsIgnoreCase("0")) {

						int currentSeq = iUploadManagementDAO.getCurrentSeqbyDmId(docList.get(i).getDmId(), tenantId);
						int checkNextSeqCount = iUploadManagementDAO.checkForNextSeq(currentSeq, docTypeCode, tenantId);
						if (checkNextSeqCount > 0) {
							docLifeCycleMstList = iUploadManagementDAO.getNextSeqandStatus(currentSeq, docTypeCode,
									tenantId);
							apprBtnEnableStatus = iUploadManagementDAO.getApprovebtnEnableStatus(designCode, tenantId,
									docTypeCode, docLifeCycleMstList.get(0).getCurrSequence());
						}

					}
					docList.get(i).setApproveBtnEnabled(apprBtnEnableStatus);
				}
				list.setResponseData(docList);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
			} else {
				list.setResponseData(docList);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);

			}

		} catch (Exception ex) {
			logger.error("getApprovedDocDtl error " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getVersionDtls(GetVersionRequest getVersionReq) {
		ResponseAsList list = new ResponseAsList();
		List<DocumentManagementTblEntity> docList = new ArrayList<DocumentManagementTblEntity>();
		List<DocumentAppStatusDtlEntity> approveList = new ArrayList<DocumentAppStatusDtlEntity>();
		List<ApprovedDocEntity> getApproveDtls = new ArrayList<ApprovedDocEntity>();

		try {
			String dmId = getVersionReq.getDmId();
			String tenantId = getVersionReq.getTenantId();
			docList = iUploadManagementDAO.getDocDlsByDmId(dmId, tenantId,"1");
			if (docList.size() > 0) {
				int version = Integer.parseInt(docList.get(0).getVersion());
				if (version > 1) {
					getApproveDtls = iUploadManagementDAO.getDocDtlsByCombination(docList.get(0).getEnquiryId(),
							docList.get(0).getProjectId(), docList.get(0).getDocumentName(), docList.get(0).getRefId(),
							docList.get(0).getStageCode(), tenantId);

					if (getApproveDtls.size() > 0) {
						for (int i = 0; i < getApproveDtls.size(); i++) {
							approveList = iUploadManagementDAO.getDocStatusList(getApproveDtls.get(i).getDmId(),
									tenantId);
							getApproveDtls.get(i).setApprovalDetails(approveList);
						}
						list.setResponseData(getApproveDtls);
						list.setResponseCode(ResponseMessageMap.responseCodeOk);
						list.setResponseMessage(ResponseMessageMap.success);
					} else {
						list.setResponseData(getApproveDtls);
						list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						list.setResponseMessage(ResponseMessageMap.noRecord);

					}
				}
			}

		} catch (Exception ex) {
			logger.error("getVersionDtls error " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getFileUploadConfigDtl(getFileConfigDtlRequest getFileConfigDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<FileUploadConfigtblEntity> list = new ArrayList<FileUploadConfigtblEntity>();
		try {
			String docTypeCode = getFileConfigDtlReq.getDocumentTypeCode();
			String tenantId = getFileConfigDtlReq.getTenantId();

			int checkCount = iUploadManagementDAO.getFileDtlCountByDocTypeCode(docTypeCode, tenantId);
			if (checkCount > 0) {
				list = iUploadManagementDAO.getFileDtlsByDocTypeCode(docTypeCode, tenantId);
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);

			}

		} catch (Exception ex) {
			logger.error("getFileUploadConfigDtl error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage sumbitApprovedDoc(ApprovedBtnRequest approvedBtnReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
//		List<DocumentManagementTblEntity> docList = new ArrayList<DocumentManagementTblEntity>();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();

		try {
			String dmId = approvedBtnReq.getDmId();
			String tenantId = approvedBtnReq.getTenantId();
			String empId = approvedBtnReq.getEmpID();
			String docTypeCode = approvedBtnReq.getDocTypeCode();
//			docList=iUploadManagementDAO.getDocDlsByDmId(dmId, tenantId);
			int curentSeq = iUploadManagementDAO.getCurrentSeqbyDmId(dmId, tenantId);
			int newdmId = Integer.parseInt(dmId);

			docLifeCycleMstList = DesignTaskDAO.getNextSeqandStatus(curentSeq, docTypeCode, tenantId);
			int updateNextSeq = iUploadManagementDAO.updateNextSeqInDmTbl(dmId, tenantId,
					docLifeCycleMstList.get(0).getCurrSequence());
			int insertStatusDtls = iUploadManagementDAO.insertDocAppStatusDtl(newdmId,
					docLifeCycleMstList.get(0).getCurrSequence(), tenantId, docLifeCycleMstList.get(0).getDocStatus(),
					empId);

//				int checkNextSeqCount = iUploadManagementDAO.checkForNextSeq(nextSeq, docTypeCode, tenantId);
			if (docLifeCycleMstList.get(0).getLastSeq() != null
					&& docLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
				String uploadDocType = iUploadManagementDAO.getUploadDocTypeByDmId(dmId, tenantId);
				iUploadManagementDAO.updateApprAndLatest(tenantId, dmId);
				
				
				List<DocumentManagementTblEntity> docmanagement=	iUploadManagementDAO.getDocDlsByDmId(dmId, tenantId,"1");
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				if(docmanagement.get(0).getEnquiryId() !=null) {
				iUploadManagementDAO.updateLatestVersion(tenantId, dmId, uploadDocType,docmanagement.get(0).getEnquiryId());
				}
				String projCode="";
				if(docmanagement.get(0).getProjectId() ==null) {
					String enqCode =iEnquiryDAO.getsaleEnquiryCode(docmanagement.get(0).getEnquiryId());
					messageList.add("Enquiry "+enqCode);
				}else {
					 projCode = indentUploadDAO.getProjectCodeByProjId(docmanagement.get(0).getProjectId(), tenantId);
					messageList.add("Project "+projCode);
				}
				List<FileUploadConfigtblEntity>fuCodeName =uploadManagementDAO.getFileDtlsByFileUploadCode(uploadDocType,tenantId);
				messageList.add(fuCodeName.get(0).getDescription());
				String nextApproveDesig=commonNotifyMethod.getNxtAppDesc(docTypeCode, docmanagement.get(0).getDocApprSeq(),tenantId);
				
				commonNotifyMethod.InvokeNotificationMethod(1, 6, null, tenantId, messageList, otherEmp, "1",approvedBtnReq.getDmId(), docmanagement.get(0).getRefId(),null);
				commonNotifyMethod.InvokeApprovalDesigMethod(approvedBtnReq.getDmId(), docTypeCode, docmanagement.get(0).getRefId(),docmanagement.get(0).getProjectId(), tenantId, "",nextApproveDesig, docmanagement.get(0).getEnquiryId(),projCode);
				
			}

			if (updateNextSeq == 1 && insertStatusDtls == 1) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage("Success");
				returnMessage.setResponseMessage(ResponseMessageMap.approved);
			} else {

				returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToApprove);
			}

		} catch (Exception ex) {
			logger.error("sumbitApprovedDoc error " + ex);
		}

		return returnMessage;
	}

	@Override
	public ResponseAsMessage addDocument(JSONObject obj, MultipartFile file) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> list = new ArrayList<DocumentStatusMstEntity>();
		int newDmId = 0;
		try {
			JSONArray iluoArray = obj.getJSONArray("reqObj");
			String enquiryId = "";
			String tenantId = "";
			String documentType = "";
			String uploadDocType = "";
			String remarks = "";
			String empId = "";
			String refId = "";
			String projectId = "";
			String stageCode = "";
			String documentName = "";
			String type = "";
			String pmId = "";
			String projCode="";
			for (int l = 0; l < iluoArray.length(); l++) {
				JSONObject iluoobjects = iluoArray.getJSONObject(l);
				JSONArray iluobodykeys = iluoobjects.names();
				for (int k = 0; k < iluobodykeys.length(); ++k) {
					String key = iluobodykeys.getString(k);
					String value = iluoobjects.getString(key);
					if (key.equalsIgnoreCase("enquiryId")) {
						enquiryId = value;
					} else if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					} else if (key.equalsIgnoreCase("documentType")) {
						documentType = value;
					} else if (key.equalsIgnoreCase("uploadDocType")) {
						uploadDocType = value;
					} else if (key.equalsIgnoreCase("remarks")) {
						remarks = value;
					} else if (key.equalsIgnoreCase("empId")) {
						empId = value;
					} else if (key.equalsIgnoreCase("referenceId") || key.equalsIgnoreCase("refId")) {
						refId = value;
					} else if (key.equalsIgnoreCase("projectId")) {
						projectId = value;
					} else if (key.equalsIgnoreCase("stageCode")) {
						stageCode = value;
					} else if (key.equalsIgnoreCase("documentName")) {
						documentName = value;
					} else if (key.equalsIgnoreCase("type")) {
						type = value;
					} else if (key.equalsIgnoreCase("pmId")) {
						pmId = value;
					}

				}
			}
			if (refId.equalsIgnoreCase("")) {
				refId = enquiryId;
			}
			if (projectId.equalsIgnoreCase("")) {
				projectId = null;
			}
			int version = 0;
			int checkCount = iUploadManagementDAO.getCountByComb(tenantId, uploadDocType, refId, stageCode);
			if (checkCount > 0) {
				version = iUploadManagementDAO.getLatestVersionbycomb(tenantId, uploadDocType, refId, stageCode);
				version = version + 1;
			} else {
				version = 1;
			}
//			String docTypeCode=iUploadManagementDAO.getDocTypeCodeByDocDesc(documentType, tenantId);

			list = stageManagementDAO.getDocDtlcurrentSeq(documentType, "1", tenantId);

			if (list.size() > 0) {
				String lastSeq = list.get(0).getLastSeq();
				if (lastSeq == null) {
					lastSeq = "0";
				}

				newDmId = iUploadManagementDAO.insertDocumentDtls(enquiryId, projectId, documentName, refId, stageCode,
						uploadDocType, version, tenantId, remarks, list.get(0).getCurrSequence(), lastSeq,documentType);
			}

			if (newDmId > 0) {
				String depCode = iUploadManagementDAO.getDepCodeByEmpId(empId, tenantId);
				String DeptList[] = iUploadManagementDAO.getMasterDoc(depCode, tenantId,pmId).split(",");

				if (DeptList.length == 1) {
					if (DeptList[0].equalsIgnoreCase("NA")) {
						iUploadManagementDAO.insertDocMagAccessDtl(newDmId, depCode, tenantId);
					} else {
						iUploadManagementDAO.insertDocMagAccessDtl(newDmId, DeptList[0], tenantId);
					}
				} else {
					for (int h = 0; h < DeptList.length; h++) {
						iUploadManagementDAO.insertDocMagAccessDtl(newDmId, DeptList[h], tenantId);
					}
				}

				int insertFileDtls = iUploadManagementDAO.insertNewFileDtl(file, tenantId, newDmId, uploadDocType,
						empId, version, documentType, type, refId);
				int insertDocStatusDtl = iUploadManagementDAO.insertDocAppStatusDtl(newDmId,
						list.get(0).getCurrSequence(), tenantId, list.get(0).getDocStatus(), empId);
//				List<FileUploadConfigtblEntity>fuCodeName =uploadManagementDAO.getFileDtlsByFileUploadCode(uploadDocType,tenantId);
				
				if (insertFileDtls == 1 && insertDocStatusDtl == 1) {
//					String designCode = iUploadManagementDAO.getDesigCodeByEmpId(empId, tenantId);
//					String documentNameByDmId = iUploadManagementDAO.getDocumentNameByDmId(newDmId, tenantId);
					List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
//					int approveEmp = iUploadManagementDAO.getApprovebtnEnableStatus(designCode, tenantId,
//							documentType, list.get(0).getCurrSequence());
					String approveDesig = iEnquiryDAO.setDefaultUser(pmId, tenantId);
					String approveEmp = iEnquiryDAO.getEmpNameDesingCode(approveDesig, tenantId);
					otherEmp.add(String.valueOf(approveEmp));
					if(projectId ==null) {
						String enqCode =iEnquiryDAO.getsaleEnquiryCode(enquiryId);
						messageList.add("Enquiry "+enqCode);
					}else {
						 projCode = indentUploadDAO.getProjectCodeByProjId(projectId, tenantId);
						messageList.add("Project "+projCode);
					}
					List<DocumentManagementTblEntity> docmanagement=	iUploadManagementDAO.getDocDlsByDmId(Integer.toString(newDmId), tenantId,"0");
					
					//messageList.add(fuCodeName.get(0).getDescription());
					messageList.add("Document");
					if(docmanagement.size()>0) {
						String nextApproveDesig=commonNotifyMethod.getNxtAppDesc(documentType, docmanagement.get(0).getDocApprSeq(),tenantId);
						otherEmp.clear();
						commonNotifyMethod.InvokeNotificationMethod(1, 3, null, tenantId, messageList, otherEmp, "1", pmId, refId,nextApproveDesig);
						commonNotifyMethod.InvokeApprovalDesigMethod(pmId, documentType, refId, projectId, tenantId, null, nextApproveDesig, docmanagement.get(0).getEnquiryId(), projCode);
					}
					else {
					    List<DocumentManagementTblEntity> accDocManagement = iUploadManagementDAO.getAccDocDlsByDmId(Integer.toString(newDmId), tenantId, "0");

					    if (accDocManagement.size() > 0) {
					        String nextApproveDesig = commonNotifyMethod.getNxtAppDesc(documentType, accDocManagement.get(0).getDocApprSeq(), tenantId);
							otherEmp.clear();
							commonNotifyMethod.InvokeNotificationMethod(1, 3, null, tenantId, messageList, otherEmp, "1", pmId, refId,nextApproveDesig);
							commonNotifyMethod.InvokeApprovalDesigMethod(pmId, documentType, refId, projectId, tenantId, null, nextApproveDesig, docmanagement.get(0).getEnquiryId(), projCode);
					    }
					}
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage("Success");
					returnMessage.setResponseMessage(ResponseMessageMap.successUpload);
				} else {

					returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					returnMessage.setResponseDataMessage("Failure");
					returnMessage.setResponseMessage(ResponseMessageMap.failTouploadMsg);
				}

			}
		} catch (Exception ex) {
			logger.error("addDocument error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getChangeRequestInfo(ChangeRequest getChangeReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<ChangeRequestHdrInfoEntity> list = new ArrayList<ChangeRequestHdrInfoEntity>();
		try {
			String sbcHdrId = getChangeReq.getSbHdrId();
			String tenantId = getChangeReq.getTenantId();

			int checkCount = iUploadManagementDAO.getChangeRequestInfoCheck(sbcHdrId, tenantId);
			if (checkCount > 0) {
				list = iUploadManagementDAO.getChangeRequestInfo(sbcHdrId, tenantId);
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);

			}

		} catch (Exception ex) {
			logger.error("getChangeRequestInfo error " + ex);
		}
		return returnList;
	}

}
