package com.vmfg.assembly.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.vmfg.assembly.dao.interfaces.IAssemblyReturnDAO;
import com.vmfg.assembly.entity.MaterialReturnDtlAcceptEntity;
import com.vmfg.assembly.entity.MrHdrRetrieveEntity;
import com.vmfg.assembly.entity.RetrieveMReturnDtlByHdrEntity;
import com.vmfg.assembly.request.InsertMrDtlReq;
import com.vmfg.assembly.request.InsertMrHdrAndDtlReq;
import com.vmfg.assembly.request.MaterialDtlRequest;
import com.vmfg.assembly.request.MaterialReqDtlRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.assembly.request.MaterialReturnAcceptRequest;
import com.vmfg.assembly.services.interfaces.IAssemblyReturnService;
import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonMethod;

@Service
public class AssemblyReturnService implements IAssemblyReturnService {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyReturnService.class);
	@Autowired
	private IAssemblyReturnDAO iAssemblyReturnDAO;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private IndentUploadDAO indentUploadDAO;

	@Autowired
	private StageManagementDAO stageManagementDAO;
	@Autowired
	private DesignTaskDAO designTaskDAO;
	@Autowired
	private UploadManagementDAO uploadManagementDAO;

	@Override
	public ResponseAsList mrHdrRetrieve(MaterialReqHdrRequest statusDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MrHdrRetrieveEntity> list = new ArrayList<MrHdrRetrieveEntity>();
		List<MrHdrRetrieveEntity> newTotalList = new ArrayList<MrHdrRetrieveEntity>();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		logger.info("mrHdrRetrieve Service start ");
		try {
			list = iAssemblyReturnDAO.mrHdrRetrieve(statusDtlReq.getHdrId(), statusDtlReq.getTenantId());
			for (MrHdrRetrieveEntity listObj : list) {
				int approveBtnEnable = 0;
				// DocList Start
				String currSeq = listObj.getSeqNo();
				currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC071", currSeq,
						statusDtlReq.getTenantId());
//				listObj.setDocumentCurrentMstList(currSeqDocLifeCycleMstList);
				
				docLifeCycleMstList = designTaskDAO.getNextSeqandStatus(Integer.parseInt(currSeq), "DC071",
						statusDtlReq.getTenantId());

				if (docLifeCycleMstList.size() > 0) {

					String designCode = uploadManagementDAO.getDesigCodeByEmpId(statusDtlReq.getEmpId(),
							statusDtlReq.getTenantId());

					approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, "default",
							statusDtlReq.getTenantId(), "DC071", docLifeCycleMstList.get(0).getCurrSequence());
					if (approveBtnEnable == 1) {
						// curr seq
						docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO.getDocTypeDescByDocType(
								docLifeCycleMstList.get(0).getDocType(), statusDtlReq.getTenantId()));
						docLifeCycleMstList.get(0)
								.setDocStatusDesc(designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(
												docLifeCycleMstList.get(0).getCurrSequence(),
												statusDtlReq.getTenantId(), "DC071"),
										statusDtlReq.getTenantId()));// Current seq docStatus

						// Previous Seq
						docLifeCycleMstList.get(0).setPreviousSeq(currSeq);
						docLifeCycleMstList.get(0).setPreviousSeqStatusCode(indentUploadDAO
								.getStatusCodebySeqAndDocType(currSeq, statusDtlReq.getTenantId(), "DC071"));

						docLifeCycleMstList.get(0)
								.setPreviousSeqStatusDesc(designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
												statusDtlReq.getTenantId(), "DC071"),
										statusDtlReq.getTenantId()));
						// cancel seq
						if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
							docLifeCycleMstList.get(0)
									.setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
							docLifeCycleMstList.get(0)
									.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
											currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
											statusDtlReq.getTenantId(), "DC071"));

							docLifeCycleMstList.get(0)
									.setCancelStatusDesc(
											designTaskDAO
													.getStatusByDesc(
															indentUploadDAO.getStatusCodebySeqAndDocType(
																	currSeqDocLifeCycleMstList.get(0)
																			.getCancelSeq(),
																	statusDtlReq.getTenantId(), "DC071"),
															statusDtlReq.getTenantId()));
						}

						listObj.setDocumentStatusMstList(docLifeCycleMstList);
					}
					else {
						// curr seq
						docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO.getDocTypeDescByDocType(
								docLifeCycleMstList.get(0).getDocType(), statusDtlReq.getTenantId()));
						docLifeCycleMstList.get(0)
								.setDocStatusDesc(designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(
												docLifeCycleMstList.get(0).getCurrSequence(),
												statusDtlReq.getTenantId(), "DC071"),
										statusDtlReq.getTenantId()));// Current seq docStatus
						// Previous Seq
						docLifeCycleMstList.get(0).setCurrSequence(currSeq);
						docLifeCycleMstList.get(0).setDocStatus(indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
								statusDtlReq.getTenantId(), "DC071"));

						docLifeCycleMstList.get(0).setDocStatusDesc(
								designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
												statusDtlReq.getTenantId(), "DC071"),
										statusDtlReq.getTenantId()));
						// cancel seq
						if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
							docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
							docLifeCycleMstList.get(0).setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
									currSeqDocLifeCycleMstList.get(0).getCancelSeq(), statusDtlReq.getTenantId(),
									"DC071"));

							docLifeCycleMstList.get(0).setCancelStatusDesc(
									designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
													statusDtlReq.getTenantId(), "DC071"),
											statusDtlReq.getTenantId()));
						}
						listObj.setDocumentStatusMstList(docLifeCycleMstList);					
					}
				}
				listObj.setIsApprovee(String.valueOf(approveBtnEnable));
				newTotalList.add(listObj);

			}

			if (list.size() > 0) {
				returnList.setResponseData(newTotalList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(newTotalList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("mrHdrRetrieve Service end ");
		} catch (Exception ex) {
			logger.error("mrHdrRetrieve error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage insertMRHAndMRD(InsertMrHdrAndDtlReq insertMrDtls) {
		ResponseAsMessage responseMsg = new ResponseAsMessage();
		logger.info("insertMRHAndMRD Service start ");
		int responseMrHdrId = 0, insertRemarks = 0;
		String seqNo = "1";
		try {
			// Common docStatus
			String seqStatus = indentUploadDAO.getStatusCodebySeqAndDocType(seqNo, insertMrDtls.getTenantId(), "DC071");
			// Material Return Hdr Insert
			responseMrHdrId = iAssemblyReturnDAO.insertMaterialReturnHdr(insertMrDtls.getPmHdrId(),
					insertMrDtls.getHdrRemark(), insertMrDtls.getTenantId(), insertMrDtls.getCreatedBy(), seqNo,
					seqStatus);

			// RemarkStatus Insert
			insertRemarks = insertReturnRemarks(String.valueOf(responseMrHdrId), "DC071", seqNo, seqStatus,
					insertMrDtls.getRemarks(), insertMrDtls.getCreatedBy(), insertMrDtls.getTenantId());

			if (responseMrHdrId > 0 && insertRemarks > 0) {
				// Material Return Dtl Insert
				for (InsertMrDtlReq dtlObj : insertMrDtls.getMrDtlList()) {
					 iAssemblyReturnDAO.insertMaterialReturnDtl(responseMrHdrId, dtlObj.getProductId(),
							dtlObj.getQty(), dtlObj.getTenantId());
				}
				responseMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				responseMsg.setResponseMessage(ResponseMessageMap.successCreated);

			}
			else {
			responseMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
			responseMsg.setResponseMessage(ResponseMessageMap.failToCreateMsg);
		}
			logger.info("insertMRHAndMRD Service end ");
		} catch (Exception ex) {
			logger.error("insertMRHAndMRD error " + ex);
		}
		return responseMsg;
	}

	@Override
	public ResponseAsList retrieveMreturnDtlByHdr(MaterialReqHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<RetrieveMReturnDtlByHdrEntity> list = new ArrayList<>();
		logger.info("retrieveMreturnDtlByHdr Service start ");
		try {
			list = iAssemblyReturnDAO.retrieveMreturnDtlByHdr(materialHdrReq.getHdrId(), materialHdrReq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("retrieveMreturnDtlByHdr Service end ");
		} catch (Exception ex) {
			logger.error("retrieveMreturnDtlByHdr error " + ex);
		}
		return returnList;
	}
	
	@Override
	public  ResponseAsMessage ApproveMreturnDtls(MaterialReqDtlRequest materialDtlReq) {
		ResponseAsMessage responseMsg = new ResponseAsMessage();
		String createdOn = CommonMethod.getCurrentDateTime();
		List<MaterialReturnDtlAcceptEntity> list = new ArrayList<>();
		logger.info("ApproveMreturnDtls Service start ");	

		try {
			
		//	String docLifeCycleDocStatus = iAssemblyReturnDAO.getSeqandStatus(materialDtlReq.getLastSeq(),
		//			"DC071", materialDtlReq.getTenantId());
			String docLifeCycleDocStatus = indentUploadDAO.getStatusCodebySeqAndDocType(materialDtlReq.getSeqNo(),
					materialDtlReq.getTenantId(), "DC071");
			if(!docLifeCycleDocStatus.isEmpty()) {
				 iAssemblyReturnDAO.approveMaterialReturnHdr(materialDtlReq.getHdrId(),
						materialDtlReq.getTenantId(), docLifeCycleDocStatus, 
						materialDtlReq.getSeqNo(), materialDtlReq.getEmpId());
				}
			
	   //     if(materialDtlReq.getLastSeq().isEmpty() || !materialDtlReq.getLastSeq().isEmpty() ) {
			for (MaterialDtlRequest dtlObj : materialDtlReq.getMrDtlReqList() ) {
				
				iAssemblyReturnDAO.ApproveDtls(dtlObj.getMrDtlId(), materialDtlReq.getTenantId());
				
			list = iAssemblyReturnDAO.ApproveMreturnDtls(dtlObj.getMrDtlId(),
					materialDtlReq.getTenantId());
		
			if(list.size() > 0) {	
				for (MaterialReturnDtlAcceptEntity obj : list) {
					BigDecimal transQty = new BigDecimal(obj.getQty());
					int decreasing = CommonMethod.updateProductInvDtl(obj.getProjectId(), obj.getProductId(),
							"ILC0003", transQty, "Subraction", "ITTC0017", obj.getMrHdrId(),
							materialDtlReq.getEmpId(), createdOn, obj.getTenantId(), jdbcTemplate);

					if (decreasing > 0) {
						CommonMethod.updateProductInvDtl(obj.getProjectId(), obj.getProductId(), "ILC0002", transQty,
								"", "ITTC0017", obj.getMrHdrId(), materialDtlReq.getEmpId(), createdOn,
								obj.getTenantId(), jdbcTemplate);
				    }	
				}
		    }
		}
			responseMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
			responseMsg.setResponseMessage(ResponseMessageMap.successCreated);
	//    } else {
	//		responseMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
	//		responseMsg.setResponseMessage(ResponseMessageMap.failToCreateMsg);
	//	  }
		}
        catch(Exception ex) {
			logger.info("ApproveMreturnDtls Service error " +ex);
		}
		return responseMsg;
	}

	@Override
	public ResponseAsMessage cancelMaterialReturnHdr(MaterialReqHdrRequest materialReqHdr) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("cancelMaterialReturnHdr Service start ");
//		List<MaterialReturnAcceptEntity> list = new ArrayList<>();
//		String createdOn = CommonMethod.getCurrentDateTime();
		int responseMrHdrId = 0;
		try {
			// Common docStatus
			String seqStatus = indentUploadDAO.getStatusCodebySeqAndDocType(materialReqHdr.getSeqNo(),
					materialReqHdr.getTenantId(), "DC071");
			responseMrHdrId = iAssemblyReturnDAO.cancelMaterialReturnHdr(materialReqHdr.getHdrId(),
					materialReqHdr.getTenantId(), seqStatus, materialReqHdr.getSeqNo());
			
                for (MaterialDtlRequest dtlObj : materialReqHdr.getMrDtlReqList()) {
				
				    iAssemblyReturnDAO.ApproveDtls(dtlObj.getMrDtlId(), materialReqHdr.getTenantId());
                }

//			list = iAssemblyReturnDAO.materialReturnAccept(String.valueOf(materialReqHdr.getHdrId()),
//					materialReqHdr.getTenantId());

//			for (MaterialReturnAcceptEntity obj : list) {
//				BigDecimal transQty = new BigDecimal(obj.getQty());
//				// inCreasing Shop Floor
//				int inCreasing = CommonMethod.updateProductInvDtl(obj.getProjectId(), obj.getProductCode(), "ILC0003",
//						transQty, "", "ITTC0017", String.valueOf(responseMrHdrId), obj.getEmpId(), createdOn,
//						materialReqHdr.getTenantId(), jdbcTemplate);
//
//				// deCreasing Store
//				if (inCreasing > 0) {
//					CommonMethod.updateProductInvDtl(obj.getProjectId(), obj.getProductCode(), "ILC0002", transQty,
//							"Subraction", "ITTC0017", String.valueOf(materialReqHdr.getHdrId()), obj.getEmpId(),
//							createdOn, materialReqHdr.getTenantId(), jdbcTemplate);
//				}
//
//			}

			if (responseMrHdrId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successfulDeleted);
				returnres.setResponseDataMessage(String.valueOf(responseMrHdrId));
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
				returnres.setResponseDataMessage(String.valueOf(responseMrHdrId));
			}
			logger.info("cancelMaterialReturnHdr Service end ");
		} catch (Exception ex) {
			logger.error("cancelMaterialReturnHdr error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsMessage materialReturnAccept(MaterialReturnAcceptRequest material) {
		ResponseAsMessage responseMsg = new ResponseAsMessage();
		int updateStatus = 0, insertRemarks = 0;
		String isCompleted = "";
		try {
			// Common docStatus
			String seqStatus = indentUploadDAO.getStatusCodebySeqAndDocType(material.getCurrentSeq(),
					material.getTenantId(), "DC071");
			if (material.getIsFinal().equals("1")) {
				isCompleted = "1";
				updateStatus = iAssemblyReturnDAO.updateStatusInMaterialReturnHdr(material.getMrhId(),
						material.getCurrentSeq(), seqStatus, isCompleted);
				if (updateStatus > 0) {
					insertRemarks = insertReturnRemarks(material.getMrhId(), "DC071", material.getCurrentSeq(),
							seqStatus, material.getRemarks(), material.getEmpId(), material.getTenantId());
				}

			} else {
				isCompleted = "0";
				updateStatus = iAssemblyReturnDAO.updateStatusInMaterialReturnHdr(material.getMrhId(),
						material.getCurrentSeq(), seqStatus, isCompleted);
				if (updateStatus > 0) {
					insertRemarks = insertReturnRemarks(material.getMrhId(), "DC071", material.getCurrentSeq(),
							seqStatus, material.getRemarks(), material.getEmpId(), material.getTenantId());
				}
			}
			if (updateStatus > 0 && insertRemarks > 0) {

				responseMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				responseMsg.setResponseMessage(ResponseMessageMap.successUpdated);

			} else {
				responseMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
				responseMsg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		} catch (Exception e) {
			logger.error("materialReturnAccept service error " + e);
		}
		return responseMsg;
	}

//common RemarkStatusInsert
	public int insertReturnRemarks(String mrhId, String referenceDoc, String currentSeq, String seqStatus,
			String remarks, String empId, String tenantId) {
		int insertRemarks = 0;
		try {
			insertRemarks = iAssemblyReturnDAO.insertReturnRemarks(mrhId, referenceDoc, currentSeq, seqStatus, remarks,
					empId, tenantId);
		} catch (Exception e) {
			logger.error("insertReturnRemarks service error " + e);
		}
		return insertRemarks;
	}


}
