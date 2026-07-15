package com.vmfg.design.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.vmfg.design.dao.impl.ChangeRequestDAO;
import com.vmfg.design.entity.*;
import com.vmfg.scm.dao.impl.IndentManagementDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.dao.interfaces.IChangeRequestDAO;
import com.vmfg.design.request.GetChangeRequestDtlByPmIdRequest;
import com.vmfg.design.request.GetKeyAreaDtlsRequest;
import com.vmfg.design.request.GetKeyIndentDtl;
import com.vmfg.design.request.UpdateChangeRequestDtlRequest;
import com.vmfg.design.request.UpdateHdrSeqAndStatusRequest;
import com.vmfg.design.response.KeyAreaIndentId;
import com.vmfg.design.response.KeySubArea;
import com.vmfg.design.services.interfaces.IChangeRequestService;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.sales.dao.impl.EnquiryDAO;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonMethod;

@Service
public class ChangeRequestService implements IChangeRequestService {
	private static final Logger logger = LoggerFactory.getLogger(ChangeRequestService.class);

	@Autowired
	private IChangeRequestDAO iChangeRequestDAO;

	@Autowired
	private EnquiryDAO enquiryDAO;

	@Autowired
	private UploadManagementDAO uploadManagementDAO;

	@Autowired
	private IndentUploadDAO indentUploadDAO;

	@Autowired
	DesignTaskDAO designTaskDAO;

	@Autowired
	private StageManagementDAO stageManagementDAO;

	@Autowired
	private ChangeRequestDAO changeRequestDAO;

	@Override
	public ResponseAsList getChangeRequestDtlByPmId(GetChangeRequestDtlByPmIdRequest getChangeRequestDtlByPmIdReq) {
		ResponseAsList list = new ResponseAsList();
		List<ChangeRequestHdrEntity> crhdrlist = new ArrayList<ChangeRequestHdrEntity>();
		try {
			String pmId = getChangeRequestDtlByPmIdReq.getPmId();
			String tenantId = getChangeRequestDtlByPmIdReq.getTenantId();
			crhdrlist = iChangeRequestDAO.ChangereqHdrList(pmId, tenantId);
			List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
			if (crhdrlist.size() > 0) {
				for (int i = 0; i < crhdrlist.size(); i++) {
					int dmId = iChangeRequestDAO.getDmIdVal(crhdrlist.get(i).getCrId(), crhdrlist.get(i).getTenantId());
					crhdrlist.get(i).setDmId(Integer.toString(dmId));
					String designCode = uploadManagementDAO.getDesigCodeByEmpId(getChangeRequestDtlByPmIdReq.getEmpId(),
							getChangeRequestDtlByPmIdReq.getTenantId());

					List<DocumentStatusMstEntity> docLifeCycleMstList = designTaskDAO.getNextSeqandStatus(
							Integer.parseInt(crhdrlist.get(i).getTransactionStatusSeq()), "DC020",
							crhdrlist.get(i).getTenantId());
					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC020",
							crhdrlist.get(i).getTransactionStatusSeq(), tenantId);
					if (docLifeCycleMstList.size() > 0) {

						int approveBtnEnable = indentUploadDAO.getApprovebtnEnableByCurr(designCode,
								getChangeRequestDtlByPmIdReq.getTenantId(), "DC020",
								docLifeCycleMstList.get(0).getCurrSequence());
						if (approveBtnEnable == 1) {
							// Current seq
							docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO
									.getDocTypeDescByDocType(docLifeCycleMstList.get(0).getDocType(), tenantId));

							docLifeCycleMstList.get(0)
									.setDocStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													docLifeCycleMstList.get(0).getCurrSequence(), tenantId, "DC020"),
											tenantId));

							// Previous Seq
							docLifeCycleMstList.get(0).setPreviousSeq(crhdrlist.get(i).getTransactionStatusSeq());
							docLifeCycleMstList.get(0)
									.setPreviousSeqStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
											crhdrlist.get(i).getTransactionStatusSeq(), tenantId, "DC020"));

							docLifeCycleMstList.get(0)
									.setPreviousSeqStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													crhdrlist.get(i).getTransactionStatusSeq(), tenantId, "DC020"),
											tenantId));
							// Cancel seq
							if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
								docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
								docLifeCycleMstList.get(0)
										.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(), tenantId, "DC020"));

								docLifeCycleMstList.get(0).setCancelStatusDesc(designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(), tenantId, "DC020"),
										tenantId));
							}
							crhdrlist.get(i).setDocLifeCycleMstList(docLifeCycleMstList);
						}
					}
					String finalDesc = "";
					if (!crhdrlist.get(i).getNextApprovingDesig().equalsIgnoreCase("")) {
						String[] desigarry = crhdrlist.get(i).getNextApprovingDesig().split(",");

						for (int j = 0; j < desigarry.length; j++) {
							String desc = iChangeRequestDAO.designationDesc(desigarry[j]);
							if (finalDesc.equalsIgnoreCase("")) {
								finalDesc = desc;
							} else {
								finalDesc = finalDesc + "," + desc;
							}
						}
					}
					crhdrlist.get(i).setNextApprovingDesigDesc(finalDesc);
					List<ChangeRequestDtlEntity> crDtlList = iChangeRequestDAO
							.ChangereqDtlList(crhdrlist.get(i).getCrId(), tenantId);
					crhdrlist.get(i).setCrDtlList(crDtlList);
				}
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(crhdrlist);
			} else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(crhdrlist);
			}
		} catch (Exception ex) {
			logger.error("getChangeRequestDtlByPmId Error " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage updateChangeRequestDtl(UpdateChangeRequestDtlRequest updateChangeRequestDtlReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			int insertCrhdrId = 0;
			int updateCrHdrId = 0;

			String crhdrId = updateChangeRequestDtlReq.getCrId();
			String crDate = CommonMethod.getCurrentDate();
			if (crhdrId.equalsIgnoreCase("")) {
//				String enqCode="";
//				String prefix="";
//				String suffix="";
//				int startId =1;
//				int lastEnqNo = enquiryDAO.getCRLastNo();
//				String financialMstId = finanaceCodeGen.getFinancialMstId(updateChangeRequestDtlReq.getCrDate(), updateChangeRequestDtlReq.getTenantId(),th);
//				int enqCount=0;
//				if(!financialMstId.equalsIgnoreCase("")) {
//					enqCount = enquiryDAO.checkFinancialNewSeq(financialMstId, updateChangeRequestDtlReq.getTenantId(), "change_request_hdr");
//				}
//				List<FinancialYearTransactionMstEntity>finacicalTransDtl = enquiryDAO.getfinacicalTransDtlByDocType(updateChangeRequestDtlReq.getPmId(),  updateChangeRequestDtlReq.getTenantId());
//				if(finacicalTransDtl.size()>0) {
//					if(finacicalTransDtl.get(0).getPrefixCode()!=null) {
//						prefix = finacicalTransDtl.get(0).getPrefixCode();
//					}
//					if(finacicalTransDtl.get(0).getSuffixCode()!=null) {
//						suffix = finacicalTransDtl.get(0).getSuffixCode();
//					}
//					if(finacicalTransDtl.get(0).getStartId() !=null) {
//						startId = Integer.parseInt(finacicalTransDtl.get(0).getStartId());
//					}
//				}
//				if(enqCount>0) {
//					int nextEnqNo =lastEnqNo+1; 
//					startId = lastEnqNo+1;
//				enqCode = prefix+nextEnqNo + suffix;
//				}else {
//					enqCode = prefix+startId + suffix;
//				}
				List<DocumentStatusMstEntity> DocStatusDtl = enquiryDAO.getfirstSeqBypmIdDocType("DC020",
						updateChangeRequestDtlReq.getPmId(), updateChangeRequestDtlReq.getTenantId());

				if (DocStatusDtl.size() > 0) {
					insertCrhdrId = iChangeRequestDAO.insertChangeRequestHdr(updateChangeRequestDtlReq.getDeHdrId(),
							updateChangeRequestDtlReq.getPmHdrId(), updateChangeRequestDtlReq.getInitiatedBy(), crDate,
							updateChangeRequestDtlReq.getProductDesc(), updateChangeRequestDtlReq.getPkId(),
							updateChangeRequestDtlReq.getPskId(), updateChangeRequestDtlReq.getRequestDetails(),
							updateChangeRequestDtlReq.getNextApprovingDesig(), DocStatusDtl.get(0).getDocStatus(),
							DocStatusDtl.get(0).getCurrSequence(), updateChangeRequestDtlReq.getUpdatedDrawingNo(),
							updateChangeRequestDtlReq.getUpdatedDrawingRevNo(),
							updateChangeRequestDtlReq.getCreatedBy(), updateChangeRequestDtlReq.getLastUpdatedBy(),
							updateChangeRequestDtlReq.getTenantId());
					crhdrId = Integer.toString(insertCrhdrId);
				}

			} else {
				insertCrhdrId = iChangeRequestDAO.updateChangeRequestHdr(updateChangeRequestDtlReq.getDeHdrId(),
						updateChangeRequestDtlReq.getPmHdrId(), updateChangeRequestDtlReq.getInitiatedBy(), crDate,
						updateChangeRequestDtlReq.getProductDesc(), updateChangeRequestDtlReq.getPkId(),
						updateChangeRequestDtlReq.getPskId(), updateChangeRequestDtlReq.getRequestDetails(),
						updateChangeRequestDtlReq.getNextApprovingDesig(),
						updateChangeRequestDtlReq.getUpdatedDrawingNo(),
						updateChangeRequestDtlReq.getUpdatedDrawingRevNo(),
						updateChangeRequestDtlReq.getLastUpdatedBy(), updateChangeRequestDtlReq.getTenantId(),
						updateChangeRequestDtlReq.getCrId());
			}

			int rowsAffected = 0; // <- initialize outside loop
			for (int j = 0; j < updateChangeRequestDtlReq.getChangeReqDtlEntity().size(); j++) {
				rowsAffected += updateCRDtl(
						updateChangeRequestDtlReq.getChangeReqDtlEntity().get(j).getCrDtlId(),
						crhdrId,
						updateChangeRequestDtlReq.getChangeReqDtlEntity().get(j).getDesignerComments(),
						updateChangeRequestDtlReq.getChangeReqDtlEntity().get(j).getTenantId(),
						updateChangeRequestDtlReq.getChangeReqDtlEntity().get(j).getEmpId()
				);
			}
			System.out.println("Total rows affected in CRDtl loop: " + rowsAffected);
			System.out.println("CR_HDR_ID used: " + crhdrId);
			String hdrId = String.valueOf(insertCrhdrId);
			if (rowsAffected > 0) {
				message.setResponseCode(ResponseMessageMap.success);
				message.setResponseMessage(ResponseMessageMap.successUpdated);
				message.setResponseDataMessage(hdrId);
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseDataMessage("Fail to update");
				message.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updateChangeRequestDtl Error " + ex);
		}
		return message;
	}

	public int updateCRDtl(String crDtlId, String crId, String designerComments, String tenantId, String empId) {
		int updateCRDtl = 0;
		try {
			if (crDtlId == null || crDtlId.trim().isEmpty()) {
				System.out.println("Inserting new ChangeRequestDtl with CR_ID: " + crId);
				updateCRDtl = iChangeRequestDAO.insertChangeRequestDtl(crId, designerComments, tenantId, empId);
			} else {
				System.out.println("Updating existing ChangeRequestDtl ID: " + crDtlId);
				updateCRDtl = iChangeRequestDAO.updateChangeRequestDtl(crDtlId, crId, designerComments, tenantId, empId);
			}
			System.out.println("Rows affected in CRDtl: " + updateCRDtl);
		} catch (Exception ex) {
			logger.error("updateCRDtl Error: " + ex.getMessage());
		}
		return updateCRDtl;
	}


	@Override
	public ResponseAsMessage updateDesignerComments(ChangeRequestDtlEntity changeRequestDtl) {
		ResponseAsMessage message = new ResponseAsMessage();
		try {
			int updateCRDtl = updateCRDtl(changeRequestDtl.getCrDtlId(), changeRequestDtl.getCrhdrId(),
					changeRequestDtl.getDesignerComments(), changeRequestDtl.getTenantId(),
					changeRequestDtl.getEmpId());
			if (updateCRDtl > 0) {
				message.setResponseCode(ResponseMessageMap.success);
				message.setResponseMessage(ResponseMessageMap.successUpdated);
				message.setResponseDataMessage("Success");
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseDataMessage("Fail to update");
				message.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updateDesignerComments Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage updateChangeReqHdrSeqAndStatus(UpdateHdrSeqAndStatusRequest updateHdrSeqAndStatusReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String docType = "DC020";
			docLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq(docType,
					updateHdrSeqAndStatusReq.getCurrentseq(), updateHdrSeqAndStatusReq.getTenantId());
//			String nextApprDesig=stageManagementDAO.getNextApprDesigByDocType(docType, docLifeCycleMstList.get(0).getCurrSequence(), updateHdrSeqAndStatusReq.getTenantId());
			String dtlId = "";
			String statusDesc = designTaskDAO
					.getStatusByDesc(
							indentUploadDAO.getStatusCodebySeqAndDocType(docLifeCycleMstList.get(0).getCurrSequence(),
									updateHdrSeqAndStatusReq.getTenantId(), "DC020"),
							updateHdrSeqAndStatusReq.getTenantId());

			int updateChangeReqHdr = iChangeRequestDAO.updateChangeReqHdrStatus(updateHdrSeqAndStatusReq.getHdrId(),
					docLifeCycleMstList.get(0).getCurrSequence(), docLifeCycleMstList.get(0).getDocStatus(),
					docLifeCycleMstList.get(0).getApprDesi(), updateHdrSeqAndStatusReq.getEmpId());

			if (updateChangeReqHdr > 0) {
				updateCRDtl(dtlId, updateHdrSeqAndStatusReq.getHdrId(), statusDesc,
						updateHdrSeqAndStatusReq.getTenantId(), updateHdrSeqAndStatusReq.getEmpId());
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage("Success");
				returnMessage.setResponseMessage(ResponseMessageMap.successInserted);

			} else {

				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updateChangeReqHdrSeqAndStatus Error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsMessage insertChangeRequestFile(JSONObject jsonObj, MultipartFile file) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> list = new ArrayList<DocumentStatusMstEntity>();
		int newDmId = 0;
		try {
			JSONArray iluoArray = jsonObj.getJSONArray("reqObj");
			String enquiryId = "";
			String tenantId = "";
			String type = "";
			String empId = "";
			String refId = "";
			String projectId = "";
			String stageCode = "";
			String remarks = "";
			String documentType = "DC020";
			String uploadDocType = "FC016";

			for (int l = 0; l < iluoArray.length(); l++) {
				JSONObject iluoobjects = iluoArray.getJSONObject(l);
				JSONArray iluobodykeys = iluoobjects.names();
				for (int k = 0; k < iluobodykeys.length(); ++k) {
					String key = iluobodykeys.getString(k);
					String value = iluoobjects.getString(key);
					if (key.equalsIgnoreCase("refId")) {
						refId = value;
					} else if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					} else if (key.equalsIgnoreCase("empId")) {
						empId = value;
					} else if (key.equalsIgnoreCase("type")) {
						type = value;
					} else if (key.equalsIgnoreCase("stageCode")) {
						stageCode = value;
					} else if (key.equalsIgnoreCase("enquiryId")) {
						enquiryId = value;
					} else if (key.equalsIgnoreCase("projectId")) {
						projectId = value;
					}

				}
			}
			String getfileName = file.getOriginalFilename();
			String fileName = getfileName.substring(0, getfileName.lastIndexOf('.'));

			int version = 0;
			int checkCount = uploadManagementDAO.getCountByComb(tenantId, uploadDocType, refId, stageCode);
			if (checkCount > 0) {
				version = uploadManagementDAO.getLatestVersionbycomb(tenantId, uploadDocType, refId, stageCode);
				version = version + 1;
			} else {
				version = 1;
			}
			list = uploadManagementDAO.getSeqAndStatusByDocTypeCode(documentType, tenantId);
			if (list.size() > 0) {
				newDmId = uploadManagementDAO.insertDocumentDtls(enquiryId, projectId, fileName, refId, stageCode,
						uploadDocType, version, tenantId, remarks, list.get(0).getCurrSequence(),"0",documentType);
			}

			if (newDmId > 0) {
				int insertFileDtls = uploadManagementDAO.insertNewFileDtl(file, tenantId, newDmId, uploadDocType, empId,
						version, documentType, type, refId);
				if (insertFileDtls == 1) {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage(getfileName);
					returnMessage.setResponseMessage(ResponseMessageMap.successUpload);
				} else {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					returnMessage.setResponseDataMessage(getfileName);
					returnMessage.setResponseMessage(ResponseMessageMap.failTouploadMsg);
				}
			}

		} catch (Exception ex) {
			logger.error("insertChangeRequestFile error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsMessage updateFileByDmId(JSONObject jsonObj, MultipartFile file) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();

		int updateStatus = 0;
		try {
			JSONArray iluoArray = jsonObj.getJSONArray("reqObj");
			String dmId = "";
			String tenantId = "";
			String type = "";
			String documentType = "";
			String uploadDocType = "";
//			String enquiryId,refId ,projectId,stageCode,remarks,empId= "";

			for (int l = 0; l < iluoArray.length(); l++) {
				JSONObject iluoobjects = iluoArray.getJSONObject(l);
				JSONArray iluobodykeys = iluoobjects.names();
				for (int k = 0; k < iluobodykeys.length(); ++k) {
					String key = iluobodykeys.getString(k);
					String value = iluoobjects.getString(key);
					if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					} else if (key.equalsIgnoreCase("type")) {
						type = value;
					} else if (key.equalsIgnoreCase("dmId")) {
						dmId = value;
					} else if (key.equalsIgnoreCase("uploadDocType")) {
						uploadDocType = value;
					} else if (key.equalsIgnoreCase("documentType")) {
						documentType = value;
					}
//					 else if (key.equalsIgnoreCase("stageCode")) {
//						stageCode = value;
//					} else if (key.equalsIgnoreCase("enquiryId")) {
//						enquiryId = value;
//					} else if (key.equalsIgnoreCase("projectId")) {
//						projectId = value;
//					} else if (key.equalsIgnoreCase("empId")) {
//						empId = value;
//					} else if (key.equalsIgnoreCase("refId")) {
//						refId = value;
//					}  

				}
			}
			String getfileName = file.getOriginalFilename();
			String fileName = getfileName.substring(0, getfileName.lastIndexOf('.'));
			int updateDmTbl = iChangeRequestDAO.updateDocManagementByDmId(dmId, fileName);

			if (updateDmTbl > 0) {
				updateStatus = iChangeRequestDAO.updateFileByDmId(file, tenantId, dmId, uploadDocType, documentType,
						type);
				if (updateStatus > 0) {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage(getfileName);
					returnMessage.setResponseMessage(ResponseMessageMap.successUpdated);
				} else {

					returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
					returnMessage.setResponseDataMessage(getfileName);
					returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				}
			}
		} catch (Exception ex) {
			logger.error("updateFileByDmId error " + ex);
		}

		return returnMessage;
	}

	@Override
	public ResponseAsList getChangeReqHdrDtlsByProdCode(GetKeyAreaDtlsRequest getKeyAreaDtlsReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<ChangeReqHdrDtls> mainList = new ArrayList<ChangeReqHdrDtls>();
		ChangeReqHdrDtls getList = new ChangeReqHdrDtls();
		List<KeyAreaIndentId> keyArea = null;
		List<KeySubArea> subKeyArea = null;
		int revisionNo = 0;
		try {
			if(getKeyAreaDtlsReq.getIndentId() ==null) {
			keyArea = iChangeRequestDAO.getKeyAreaDtls(getKeyAreaDtlsReq.getProductCode(),
					getKeyAreaDtlsReq.getMasterId(), getKeyAreaDtlsReq.getTenantId());
			}else {
			subKeyArea = iChangeRequestDAO.getSubKeyAreaDtls(getKeyAreaDtlsReq.getProductCode(),
					getKeyAreaDtlsReq.getMasterId(), getKeyAreaDtlsReq.getTenantId(),getKeyAreaDtlsReq.getIndentId());
			}
			getList.setKeyArea(keyArea);
			getList.setSubKeyArea(subKeyArea);

			revisionNo = iChangeRequestDAO.getRevisionNoCount(getKeyAreaDtlsReq.getProductCode(),
					getKeyAreaDtlsReq.getMasterId(), getKeyAreaDtlsReq.getTenantId());
			if (revisionNo == 0) {
				revisionNo = 1;
			} else {
				revisionNo = revisionNo + 1;
			}
			getList.setRevisionNo(revisionNo);
			mainList.add(getList);
			returnList.setResponseData(mainList);
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.success);

		} catch (Exception ex) {
			logger.error("getChangeReqHdrDtlsByProdCode error " + ex);
		}
		return returnList;
	}
	@Override 
	public ResponseAsList getChangeReqIndentHdrByProdId(GetKeyIndentDtRequest GetKeyIndentDtlReq) { 
		// TODO Auto-generated method stub 
		ResponseAsList returnList = new ResponseAsList(); 
		String projectId = GetKeyIndentDtlReq.getProjectId(); 
		String tenantId = GetKeyIndentDtlReq.getTenantId(); 
		List<ChangeRequestIndentEntity> list = new ArrayList<>(); 

try { 
			list = iChangeRequestDAO.getChangeRequestByIndentId(projectId,tenantId); 
			if (list.size() > 0) { 
				returnList.setResponseData(list); 
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk); 
				returnList.setResponseMessage(ResponseMessageMap.success); 
			} else { 
				returnList.setResponseData(list); 
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk); 
				returnList.setResponseMessage(ResponseMessageMap.noRecord); 
			}
		}catch (Exception e) { 
			// TODO: handle exception 
			logger.error("getChangeRequestByIndentId service error " + e); 
		} 
		return returnList;
	} 

@Override 
public ResponseAsList getChangeReqIndentDtlByIndentId(GetKeyIndentDtl getKeyIndentDtl) { 
	// TODO Auto-generated method stub 
	ResponseAsList returnList = new ResponseAsList(); 
	String indentId = getKeyIndentDtl.getIndentId(); 
	String tenantId = getKeyIndentDtl.getTenantId(); 
	List<ChangeRequestIndentDtlEntity> list = new ArrayList<>(); 
	 
try { 
		list = iChangeRequestDAO.getChangeRequestByIndentDtlId(indentId,tenantId); 
		if (list.size() > 0) { 
			returnList.setResponseData(list); 
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk); 
			returnList.setResponseMessage(ResponseMessageMap.success); 
		} else { 
			returnList.setResponseData(list); 
			returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk); 
			returnList.setResponseMessage(ResponseMessageMap.noRecord); 
		} 
	}catch (Exception e) { 
		// TODO: handle exception 
		logger.error("getChangeRequestByIndentDtlId service error " + e); 
	} 
	return returnList; 
}


	@Override
	public IndentPartDetailsEntity getIndentDetailsByCode(Integer indentId) {
		return changeRequestDAO.getIndentDetailsByIndentCode(indentId);
	}


}
