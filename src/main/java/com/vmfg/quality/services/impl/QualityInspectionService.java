package com.vmfg.quality.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.DepartmentAndEmployeeDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.quality.dao.interfaces.IQualityInspectionDAO;
import com.vmfg.quality.entity.QIStatusEntity;
import com.vmfg.quality.entity.QiCaEntity;
import com.vmfg.quality.entity.QualityInsReqEntity;
import com.vmfg.quality.entity.QualityInspectionDtlEntity;
import com.vmfg.quality.entity.QualityInspectionHdrEntity;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.quality.request.QiCaDtlsRequest;
import com.vmfg.quality.request.QiStatusRequest;
import com.vmfg.quality.request.UpdateQiCaDtlsRequest;
import com.vmfg.quality.request.UpdateVendorRatingRequest;
import com.vmfg.quality.request.getQIDtlsRequest;
import com.vmfg.quality.services.interfaces.IQualityInspectionService;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.dao.impl.GrnDAO;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.scm.dao.interfaces.IMaterialInwardDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.entity.EnquiryCodeWithId;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;
@Service
public class QualityInspectionService implements IQualityInspectionService {
	private static final Logger logger = LoggerFactory.getLogger(QualityInspectionService.class);
	@Autowired
	IQualityInspectionDAO iQualityInspectionDAO;
	
	@Autowired
	StageManagementDAO stageManagementDAO;
	
	@Autowired
	DesignTaskDAO designTaskDAO;
	
	@Autowired
	UploadManagementDAO uploadManagementDAO;
	
	@Autowired
	IndentUploadDAO indentUploadDAO;
	
	@Autowired
	private CommonNotifyMethod commonNotifyMethod;

	@Autowired
	IMaterialInwardDAO iMaterialInwarDAO;
	
	@Autowired
	IPoDAO iPoDAO;
	
	
	@Autowired
	GrnDAO grnDAO;
	
	@Autowired
	IndentGroupDAO indentGroupDAO;
	
	@Autowired
	DepartmentAndEmployeeDAO departmentAndEmployeeDAO;

	@Override
	public ResponseAsList getInspHdrAndDtl(getQIDtlsRequest getQIDtlsReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<QualityInspectionHdrEntity> qiList = new ArrayList<QualityInspectionHdrEntity>();
		List<QualityInspectionDtlEntity> qiDtlList=new ArrayList<QualityInspectionDtlEntity>();
		
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		int approveBtnEnable=0;
		try {
				qiList=iQualityInspectionDAO.getQiHdrList(getQIDtlsReq.getQiId(),getQIDtlsReq.getTenantId());
				if(qiList.size()>0) {
					
					for(int j=0;j<qiList.size();j++) {
						qiDtlList=iQualityInspectionDAO.getQIDtlList(qiList.get(j).getQiHdrId());
						qiList.get(j).setDtlList(qiDtlList);
						qiList.get(j).setTotalRejcInt(qiList.get(j).getRejectedInternal());
						qiList.get(j).setTotalRejcExt(qiList.get(j).getRejectedExternal());
						qiList.get(j).setTotalReworkInt(qiList.get(j).getReworkInternal());
						qiList.get(j).setTotalReworkVen(qiList.get(j).getReworkVendor());
						BigDecimal directlyAccepted= new BigDecimal(qiList.get(j).getTotalOkQty()).subtract(new BigDecimal(qiList.get(j).getCaInternal()).add(new BigDecimal(qiList.get(j).getCaVendor()))); 
						qiList.get(j).setDirectlyAccepted(String.valueOf(directlyAccepted));
					
						currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC068",qiList.get(0).getSequenceNo(), getQIDtlsReq.getTenantId());
					
					docLifeCycleMstList= designTaskDAO.getNextSeqandStatus(Integer.parseInt(qiList.get(0).getSequenceNo()), "DC068", getQIDtlsReq.getTenantId());
					if(docLifeCycleMstList.size()>0) {
						
							String designCode = uploadManagementDAO.getDesigCodeByEmpId(getQIDtlsReq.getEmpId(),
									getQIDtlsReq.getTenantId());
	
							approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, "default",
									getQIDtlsReq.getTenantId(), "DC068", docLifeCycleMstList.get(0).getCurrSequence());
							
							if (approveBtnEnable == 1) {
								// curr seq
								docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO
										.getDocTypeDescByDocType(docLifeCycleMstList.get(0).getDocType(), getQIDtlsReq.getTenantId()));
								docLifeCycleMstList.get(0)
								.setDocStatusDesc(designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(
												docLifeCycleMstList.get(0).getCurrSequence(),  getQIDtlsReq.getTenantId(), "DC068"),
										getQIDtlsReq.getTenantId()));// Current seq docStatus

								// Previous Seq
								docLifeCycleMstList.get(0).setPreviousSeq(currSeqDocLifeCycleMstList.get(0).getCurrSequence());
								docLifeCycleMstList.get(0).setPreviousSeqStatusCode(indentUploadDAO
										.getStatusCodebySeqAndDocType(currSeqDocLifeCycleMstList.get(0).getCurrSequence(),  getQIDtlsReq.getTenantId(), "DC068"));
	
								docLifeCycleMstList.get(0).setPreviousSeqStatusDesc(
										designTaskDAO.getStatusByDesc(currSeqDocLifeCycleMstList.get(0).getDocStatus(),  getQIDtlsReq.getTenantId()));
							
								// cancel seq
								if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
									docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
									docLifeCycleMstList.get(0)
									.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
											currSeqDocLifeCycleMstList.get(0).getCancelSeq(), getQIDtlsReq.getTenantId(), "DC068"));
	
									docLifeCycleMstList.get(0).setCancelStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													currSeqDocLifeCycleMstList.get(0).getCancelSeq(), getQIDtlsReq.getTenantId(), "DC068"),
											getQIDtlsReq.getTenantId()));
								}
								
								qiList.get(j).setDocLifeCycleMstList(docLifeCycleMstList);
								qiList.get(j).setIsEditable(docLifeCycleMstList.get(0).getIsEditable()!=null && docLifeCycleMstList.get(0).getIsEditable()!=""? docLifeCycleMstList.get(0).getIsEditable():"0");
							}else {
								qiList.get(j).setIsEditable("0");
							}
						}else {
							qiList.get(j).setIsEditable("0");
						}
					}

				}else {
					qiList=iQualityInspectionDAO.getQIConfigHdrList(getQIDtlsReq.getQicName(),getQIDtlsReq.getTenantId());
					if (qiList.size() > 0) {
						for (int i = 0; i < qiList.size(); i++) {
							qiDtlList = iQualityInspectionDAO.getQIConfigDtls(qiList.get(i).getQicHdrId());
							qiList.get(i).setDtlList(qiDtlList);
						}
						qiList.get(0).setIsEditable("1");
					}
				}
			if(qiList.size()>0) {
				returnList.setResponseData(qiList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			}else {
				returnList.setResponseData(qiList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getInspHdrAndDtl error---> " + ex);
		}
		return returnList;
	}


	@Override
	public ResponseAsList getQIStatusDtls(QiStatusRequest qiStatusReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<QIStatusEntity> list = new ArrayList<QIStatusEntity>();
		try {
			list = iQualityInspectionDAO.getQIStatusDtls(qiStatusReq.getRefId(),qiStatusReq.getRefDoc(),qiStatusReq.getTenantId());

			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getQIStatusDtls error---> " + ex);
		}
		return returnList;
	}


	@Override
	public ResponseAsMessage insertInspHdrAndDtl(QualityInspectionHdrEntity qiHdrDtlReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		int newQiHdrId = 0, updateHdr = 0, dtlStatus = 0;
		BigDecimal internalCal =BigDecimal.ZERO;
		BigDecimal caExternaCal=BigDecimal.ZERO;
		BigDecimal reworkExternalCal=BigDecimal.ZERO;
		BigDecimal qualityRating=BigDecimal.ZERO;
	
		try {
			logger.info("insertInspHdrAndDtl service start");
			if (qiHdrDtlReq != null) {
				if(qiHdrDtlReq.getNrFlag().equalsIgnoreCase("0") && !qiHdrDtlReq.getCancelFlag().equalsIgnoreCase("1")) {
					internalCal = new BigDecimal(qiHdrDtlReq.getTotalOkQty())
			                .add(new BigDecimal(qiHdrDtlReq.getCaInternal()))
			                .add(new BigDecimal(qiHdrDtlReq.getTotalReworkInt()))
			                .add(new BigDecimal(qiHdrDtlReq.getTotalRejcInt()))
			                .divide(new BigDecimal(qiHdrDtlReq.getInspectionQty()), 2, RoundingMode.HALF_DOWN)
			                .multiply(new BigDecimal("100"));
						   
					caExternaCal = new BigDecimal(qiHdrDtlReq.getCaVendor())
			                	.divide(new BigDecimal(qiHdrDtlReq.getInspectionQty()), 2, RoundingMode.HALF_DOWN)
			                    .multiply(new BigDecimal("75"));
	
					reworkExternalCal = new BigDecimal(qiHdrDtlReq.getTotalReworkVen())
			                        .divide(new BigDecimal(qiHdrDtlReq.getInspectionQty()), 2, RoundingMode.HALF_DOWN)
			                        .multiply(new BigDecimal("50"));
				 
					qualityRating=internalCal.add(caExternaCal).add(reworkExternalCal);
//					stageManagementDAO.updatenotification(refId, doctype, tenantId);
				}else {
					qualityRating = BigDecimal.ZERO;
				}
				
			 qiHdrDtlReq.setQualityRating(String.valueOf(qualityRating));
			 
				if (qiHdrDtlReq.getQiHdrId().isEmpty()) {
					 String seqStatus = "";
					if(qiHdrDtlReq.getNrFlag().equalsIgnoreCase("1")) {
					    seqStatus=  indentUploadDAO.getStatusCodebySeqAndDocType("2", qiHdrDtlReq.getTenantId(), "DC068");
					}else {
					    seqStatus=  indentUploadDAO.getStatusCodebySeqAndDocType("1", qiHdrDtlReq.getTenantId(), "DC068");	
					}
					qiHdrDtlReq.setSequenceStatus(seqStatus);
					if(qiHdrDtlReq.getCancelFlag() == null ) {
						qiHdrDtlReq.setCancelFlag("0");
					}
						iQualityInspectionDAO.resetQiHdrIsLatest(qiHdrDtlReq.getQiId(), qiHdrDtlReq.getTenantId());
						newQiHdrId = iQualityInspectionDAO.insertInQiHdr(qiHdrDtlReq);
						qiHdrDtlReq.setQiHdrId(String.valueOf(newQiHdrId));
					if(qiHdrDtlReq.getNrFlag().equalsIgnoreCase("1")) {
						iQualityInspectionDAO.insertQiStatus(qiHdrDtlReq.getQiHdrId(),"INSPECTION","2",seqStatus,"Approved",qiHdrDtlReq.getEmpId(),qiHdrDtlReq.getTenantId());
					}else {
						iQualityInspectionDAO.insertQiStatus(qiHdrDtlReq.getQiHdrId(),"INSPECTION","1",seqStatus,"Created",qiHdrDtlReq.getEmpId(),qiHdrDtlReq.getTenantId());
					}
				
					if(newQiHdrId>0) {
						String enqId = indentUploadDAO.getEnqIdByProjectId(qiHdrDtlReq.getPmHdrId());
						String approDesc = commonNotifyMethod.getNxtAppDesc("DC068", "1",qiHdrDtlReq.getTenantId());
							List<String> messageList = new ArrayList<>();
							List<String> otherEmp = new ArrayList<>();
								String projCode = indentUploadDAO.getProjectCodeByProjId(qiHdrDtlReq.getPmHdrId(),
										qiHdrDtlReq.getTenantId());
								String poCode = grnDAO.getPoCodeByPoId(qiHdrDtlReq.getPoId());
								String prodDesc = iPoDAO.getProdDescByQiId(qiHdrDtlReq.getQiId());
								messageList.add(poCode);
								messageList.add(projCode);
								messageList.add(prodDesc);
							
							commonNotifyMethod.InvokeNotificationMethod(1, 57, "", 
									qiHdrDtlReq.getTenantId(), messageList,
									otherEmp, "",qiHdrDtlReq.getPmId(), qiHdrDtlReq.getQHdrId(), approDesc);
							commonNotifyMethod.InvokeApprovalDesigMethod(qiHdrDtlReq.getPmId(),
									"DC068", qiHdrDtlReq.getQiId(), qiHdrDtlReq.getPmHdrId(), qiHdrDtlReq.getTenantId(),
									"", approDesc,enqId ,poCode+"-"+prodDesc);
						}
				}else {
					updateHdr = iQualityInspectionDAO.updateQIHdr(qiHdrDtlReq);
				}

				if (newQiHdrId > 0 || updateHdr > 0) {
					if(qiHdrDtlReq.getCancelFlag().equalsIgnoreCase("1")) {
					  iQualityInspectionDAO.updateCancelInspQualityStatus(qiHdrDtlReq.getCancelFlag(),qiHdrDtlReq.getEmpId(),qiHdrDtlReq.getQiHdrId(),qiHdrDtlReq.getTenantId());	
					}
					if (qiHdrDtlReq.getDtlList().size() > 0) {
						for (int i = 0; i < qiHdrDtlReq.getDtlList().size(); i++) {
							if (qiHdrDtlReq.getDtlList().get(i).getQiDtlId()==null || qiHdrDtlReq.getDtlList().get(i).getQiDtlId().isEmpty() ) {
								qiHdrDtlReq.getDtlList().get(i).setQiHdrId(String.valueOf(newQiHdrId));
								int insertDtl = iQualityInspectionDAO.insertQiDtl(qiHdrDtlReq.getDtlList().get(i));
								dtlStatus = insertDtl;
							} else {
								int updateDtl = iQualityInspectionDAO.updateQiDtl(qiHdrDtlReq.getDtlList().get(i));
								dtlStatus = updateDtl;
							}
						}
						if (dtlStatus == 1) {
							returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
							returnMessage.setResponseDataMessage(qiHdrDtlReq.getQiHdrId());
							returnMessage.setResponseMessage(ResponseMessageMap.successUpload);
						} else {
							returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
							returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
						}
						
					}else{
						returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnMessage.setResponseDataMessage(qiHdrDtlReq.getQiHdrId());
						returnMessage.setResponseMessage(ResponseMessageMap.successUpload);
					}
					
				}else {
					logger.error("Error in QI hdr insert");
				}
			}
		} catch (Exception ex) {
			logger.error("insertInspHdrAndDtl Method Exception --->" + ex);

		}
		logger.info("insertInspHdrAndDtl service end");
		return returnMessage;
	}


	@Override
	public ResponseAsMessage updateQISeqAndStatus(UpdateSeqAndStatusRequest updateHdrReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();	
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<QualityInspectionHdrEntity> qiHdrList = new ArrayList<QualityInspectionHdrEntity>();
		List<RetrieveQualitInspectionEntity> poDtllist = new ArrayList<>();
		List<QualityInsReqEntity> grnDtls=new ArrayList<QualityInsReqEntity>();
		int  seqUpdated=0,isCompleted=0, qiCaId=0; String type = "GRN"; String colName = "GRN_COMPLETED_DATETIME";
		String currSeq="",nrFlag="0", canFlag="0";
		try {
			logger.info("updateQISeqAndStatus service start");
			
			qiHdrList=iQualityInspectionDAO.getQiHdrListtByQiHdrId(updateHdrReq.getHdrId(),updateHdrReq.getTenantId());
			if((updateHdrReq.getNrFlag() != null && updateHdrReq.getNrFlag().equalsIgnoreCase("1")) && (updateHdrReq.getCancelFlag() != null && updateHdrReq.getCancelFlag().equalsIgnoreCase("0"))) {
				String lastSeq=iQualityInspectionDAO.getLastSeq("DC068",updateHdrReq.getTenantId());
				currSeq=lastSeq;
				nrFlag="1";
			}else if(updateHdrReq.getCancelFlag() != null && updateHdrReq.getCancelFlag().equalsIgnoreCase("1")) {
				currSeq="3";
				canFlag="1";
			}else {
				currSeq=updateHdrReq.getCurrentseq();
			}
			
			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC068", currSeq,updateHdrReq.getTenantId());
			if(currSeqDocLifeCycleMstList.size()>0) {
				if(currSeqDocLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
					isCompleted=1;
					poDtllist=iQualityInspectionDAO.getPoDtlsByQiHdrId(qiHdrList.get(0).getQiId());
					grnDtls=iQualityInspectionDAO.getGrnDtls(updateHdrReq.getHdrId(),qiHdrList.get(0).getTenantId());
					
//					insert in quality_ca_dtl Table
					String caSeqStatus=indentUploadDAO.getStatusCodebySeqAndDocType("1", updateHdrReq.getTenantId(), "DC070");
					
					BigDecimal rejectedQty=new BigDecimal(qiHdrList.get(0).getRejectedInternal()).add(new BigDecimal(qiHdrList.get(0).getRejectedExternal()));
					BigDecimal reworkQty= new BigDecimal(qiHdrList.get(0).getReworkInternal()).add(new BigDecimal(qiHdrList.get(0).getReworkVendor()));
					BigDecimal nOkQty = new BigDecimal(qiHdrList.get(0).getTotalNokQty());
					
					if (!qiHdrList.get(0).getTotalNokQty().isEmpty() && nOkQty.compareTo(BigDecimal.ZERO) != 0) {
						qiCaId=iQualityInspectionDAO.insertInQualityCa(updateHdrReq.getHdrId(), poDtllist.get(0).getPoId(), poDtllist.get(0).getPoCode(), poDtllist.get(0).getPoDtlId(), "CA", nOkQty.toString(), "1", caSeqStatus, "0",qiHdrList.get(0).getPmHdrId(),updateHdrReq.getTenantId());
					    iQualityInspectionDAO.insertQiStatus(String.valueOf(qiCaId), "CA", "1",caSeqStatus, "Created", updateHdrReq.getEmpId(), updateHdrReq.getTenantId());
					  
					}   
						
					// insert in GRN
					if(grnDtls.size()>0) {
						int alreadyCompleted = iQualityInspectionDAO.getCompletedCountByQiId(
								qiHdrList.get(0).getQiId(), updateHdrReq.getHdrId(), currSeq);
						if(alreadyCompleted == 0) {
//					update Inspected,Rework,Rejected qty in po_dtl and Qi_request
						iQualityInspectionDAO.updateInspectedQty(qiHdrList.get(0).getTotalOkQty(), grnDtls.get(0).getPoDtlId(),String.valueOf(rejectedQty),String.valueOf(reworkQty));
						iQualityInspectionDAO.updateQiReqQtyInspected(qiHdrList.get(0).getTotalOkQty(),qiHdrList.get(0).getQiId());
						if(grnDtls.get(0).getMiDtlId()!=null && grnDtls.get(0).getMiId()!= null) {

//							update ok,nok,rework qty in Material_inward_dtl
							iQualityInspectionDAO.updateMiQty(grnDtls.get(0).getMiDtlId(), qiHdrList.get(0).getTotalOkQty(), String.valueOf(rejectedQty),String.valueOf(reworkQty) );
						
							if (new BigDecimal(qiHdrList.get(0).getTotalOkQty()).compareTo(BigDecimal.ZERO) > 0) {
								EnquiryCodeWithId grnHdrRes = grnDAO.insertGrnHdr(updateHdrReq.getEmpId(),
										CommonMethod.getCurrentDateTime(), grnDtls.get(0).getMiId(),
										qiHdrList.get(0).getTenantId(),grnDtls.get(0).getInventoryLocCode(),poDtllist.get(0).getPoId());
								
								if (grnHdrRes.getId() > 0) {
									String getInvType = iMaterialInwarDAO.getInvType(
											grnDtls.get(0).getInventoryLocCode(),
											qiHdrList.get(0).getTenantId());

									String inspLoc = iMaterialInwarDAO.getInvLocationByType(getInvType,
											qiHdrList.get(0).getTenantId(),
											grnDtls.get(0).getInventoryLocCode());
									iQualityInspectionDAO.udpatepreinspectionqty(qiHdrList.get(0).getPmHdrId(), grnDtls.get(0).getProdutId(), inspLoc, new BigDecimal(qiHdrList.get(0).getTotalOkQty()), "",
											"ITTC0014", grnHdrRes.getEnquiryCode(), updateHdrReq.getEmpId(), CommonMethod.getCurrentDateTime(), updateHdrReq.getTenantId());
//									String productId=grnDAO.getProductIdByProductCode(grnDtls.get(0).getProductCode(),qiHdrList.get(0).getPmHdrId(),qiHdrList.get(0).getTenantId());
									grnDAO.insertGrnDtl(grnHdrRes.getId(), grnDtls.get(0).getMiDtlId(),
											qiHdrList.get(0).getTotalOkQty(),
											qiHdrList.get(0).getTenantId(),
											grnDtls.get(0).getPoDtlId(),
											grnDtls.get(0).getIndentDtlId(),qiHdrList.get(0).getPmHdrId(),
											grnDtls.get(0).getProductCode(),grnDtls.get(0).getInventoryLocCode(),
											updateHdrReq.getEmpId(),grnHdrRes.getEnquiryCode(),grnDtls.get(0).getProdutId());									
									
								}
							}
							updateMiCompleteStatus(grnDtls.get(0).getMiId(), updateHdrReq.getEmpId());
						}
						} // end alreadyCompleted == 0
					}else {
						String miDtlId = iQualityInspectionDAO.getMiDtlIdByQiHdrId(updateHdrReq.getHdrId(),updateHdrReq.getTenantId());
						if(miDtlId!=null) {
							String miId= iQualityInspectionDAO.getMiIdByMiDtlId(miDtlId, updateHdrReq.getTenantId());
							updateMiCompleteStatus(miId, updateHdrReq.getEmpId());
						}
					}
					
					if(grnDtls.size()>0 && !grnDtls.get(0).getIndentDtlId().equalsIgnoreCase("")) {
						String UpdatedDateTime = indentGroupDAO.getLastUpdatedDateTime(grnDtls.get(0).getIndentDtlId(), qiHdrList.get(0).getTenantId(), type);
				            if(!UpdatedDateTime.equalsIgnoreCase("")) {
					           indentGroupDAO.updateLastUpdatedDateTime(grnDtls.get(0).getIndentDtlId(), colName, UpdatedDateTime,  qiHdrList.get(0).getTenantId());
				            }else {
				            	indentGroupDAO.reUpdatedDateTimeIndentDtl(grnDtls.get(0).getIndentDtlId(), colName, qiHdrList.get(0).getTenantId());
				            }
					}
					
					//update Inward_rating quality_inspection_hdr
			   
					String poDelDate=iQualityInspectionDAO.getPoDelDate(updateHdrReq.getHdrId(),updateHdrReq.getTenantId());
					String insDate=iQualityInspectionDAO.getInsDate(updateHdrReq.getHdrId(),updateHdrReq.getTenantId());
					
				    iQualityInspectionDAO.updateInwardRating(updateHdrReq.getHdrId(),updateHdrReq.getTenantId(),poDelDate,insDate);
					
				}
				if(updateHdrReq.getCancelFlag() != null && updateHdrReq.getCancelFlag().equalsIgnoreCase("1")) {
					seqUpdated=iQualityInspectionDAO.updateCancelInspQualityStatus(canFlag,updateHdrReq.getEmpId(),updateHdrReq.getHdrId(),updateHdrReq.getTenantId());	
					 String indentDtlId = iQualityInspectionDAO.getIndentHdrIdByQiHdrId(updateHdrReq.getHdrId(), updateHdrReq.getTenantId());
					 if(indentDtlId != null) {
						 indentGroupDAO.reUpdatedDateTimeIndentDtl(indentDtlId, "QI_REQUESTED_DATETIME", qiHdrList.get(0).getTenantId());
					 }     	
				}else {
				seqUpdated=iQualityInspectionDAO.updateQISeqAndStatus(updateHdrReq.getCurrentseq(),currSeqDocLifeCycleMstList.get(0).getDocStatus(),isCompleted,updateHdrReq.getHdrId(),updateHdrReq.getEmpId(),nrFlag);
				}
			}
			if (seqUpdated==1) {
				
	            // Update QI last updated datetime in indent_Dtl table
				if(grnDtls.size()>0 && !grnDtls.get(0).getIndentDtlId().equalsIgnoreCase("")) {
					String qiLastUpdatedDate=indentGroupDAO.getLastUpdatedDateTime(grnDtls.get(0).getIndentDtlId(),qiHdrList.get(0).getTenantId(), "QI");
					if(!qiLastUpdatedDate.equalsIgnoreCase("")){
						indentGroupDAO.updateLastUpdatedDateTime(grnDtls.get(0).getIndentDtlId(), "QI_COMPLETED_DATETIME", qiLastUpdatedDate, qiHdrList.get(0).getTenantId());
					}else {
		            	indentGroupDAO.reUpdatedDateTimeIndentDtl(grnDtls.get(0).getIndentDtlId(), "QI_COMPLETED_DATETIME", qiHdrList.get(0).getTenantId());
		            }
				}
				
				int statusInsert=iQualityInspectionDAO.insertQiStatus(updateHdrReq.getHdrId(), "INSPECTION", updateHdrReq.getCurrentseq(),currSeqDocLifeCycleMstList.get(0).getDocStatus(), updateHdrReq.getRemarks(), updateHdrReq.getEmpId(), updateHdrReq.getTenantId());
				if(statusInsert ==1) {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage(ResponseMessageMap.successMsg);
					returnMessage.setResponseMessage(ResponseMessageMap.approved);
				}
				
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				String poCode = grnDAO.getPoCodeByPoId(updateHdrReq.getPoId());
				messageList.add(poCode);
				String projCode = indentUploadDAO.getProjectCodeByProjId(updateHdrReq.getPmHdrId(),
						updateHdrReq.getTenantId());
				String enqId = indentUploadDAO.getEnqIdByProjectId(updateHdrReq.getPmHdrId()
						);
				messageList.add(projCode);
				String prodDesc = iPoDAO.getProdDescByQiId(qiHdrList.get(0).getQiId());
				messageList.add(prodDesc);
				String approDesc=commonNotifyMethod.getNxtAppDesc("DC068", updateHdrReq.getCurrentseq(),updateHdrReq.getTenantId());
				commonNotifyMethod.InvokeNotificationMethod(2, 61, "",
						updateHdrReq.getTenantId(), messageList,
						otherEmp, "",updateHdrReq.getPmId(), updateHdrReq.getMstId(), approDesc);
				commonNotifyMethod.InvokeApprovalDesigMethod(updateHdrReq.getPmId(),
						"DC068", qiHdrList.get(0).getQiId(), updateHdrReq.getPmHdrId(), updateHdrReq.getTenantId(),
						"", approDesc,enqId ,poCode+"-"+prodDesc);
				
			}
			else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
			if(qiCaId>0) {
				  List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
					String projCode = indentUploadDAO.getProjectCodeByProjId(updateHdrReq.getPmHdrId(),
							updateHdrReq.getTenantId());
					String poCode = grnDAO.getPoCodeByPoId(updateHdrReq.getPoId());
					String enqId = indentUploadDAO.getEnqIdByProjectId(updateHdrReq.getPmHdrId()
							);
					messageList.add(poCode);
					messageList.add(projCode);
					String approDesc=commonNotifyMethod.getNxtAppDesc("DC070", "1",updateHdrReq.getTenantId());
					commonNotifyMethod.InvokeNotificationMethod(1, 58, "", 
							updateHdrReq.getTenantId(), messageList,
							otherEmp, "",updateHdrReq.getPmId(), updateHdrReq.getMstId(), approDesc);
					commonNotifyMethod.InvokeApprovalDesigMethod(updateHdrReq.getPmId(),
							"DC070", String.valueOf(qiCaId) , updateHdrReq.getPmHdrId(), updateHdrReq.getTenantId(),
							"", approDesc,enqId ,poCode);

			}
		} catch (Exception ex) {
			logger.error("updateQISeqAndStatus error " + ex);
		}
		logger.info("updateQISeqAndStatus service end");
		return returnMessage;
	}


	@Override
	public ResponseAsMessage updateQiCaSeqAndStatus(UpdateSeqAndStatusRequest updateHdrReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<QiCaEntity> qiCaList = new ArrayList<QiCaEntity>();
		int  seqUpdated=0,isApproved=0; String type = "GRN";
		String colName = "GRN_COMPLETED_DATETIME";
		BigDecimal internalCal =BigDecimal.ZERO;
		BigDecimal caExternaCal=BigDecimal.ZERO;
		BigDecimal reworkExternalCal=BigDecimal.ZERO;
		BigDecimal qualityRating=BigDecimal.ZERO;
		
		try {
			logger.info("updateQiCaSeqAndStatus service start");
			qiCaList=iQualityInspectionDAO.getQiCaListByQiCaId(updateHdrReq.getHdrId());
			
			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC070",updateHdrReq.getCurrentseq() ,updateHdrReq.getTenantId());
			if(currSeqDocLifeCycleMstList.size()>0) {
				if(currSeqDocLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
					isApproved=1;
					
					// update all the qty in qi_hdr
					BigDecimal okQty=new BigDecimal(qiCaList.get(0).getCaInternal()).add(new BigDecimal(qiCaList.get(0).getCaVendor()));
					BigDecimal nOkQty=new BigDecimal(qiCaList.get(0).getRejectedExternal()).add(new BigDecimal(qiCaList.get(0).getRejectedInternal()));
					BigDecimal reworkQty=new BigDecimal(qiCaList.get(0).getReworkInternal()).add(new BigDecimal(qiCaList.get(0).getReworkVendor()));
					
					  internalCal = 
			                new BigDecimal(qiCaList.get(0).getCaInternal())
			                .add(new BigDecimal(qiCaList.get(0).getReworkInternal()))
			                .add(new BigDecimal(qiCaList.get(0).getRejectedInternal()))
			                .divide(new BigDecimal(qiCaList.get(0).getInspectionQty()), 2, RoundingMode.HALF_DOWN)
			                .multiply(new BigDecimal("100"));
						   
					caExternaCal = new BigDecimal(qiCaList.get(0).getCaVendor())
			                	.divide(new BigDecimal(qiCaList.get(0).getInspectionQty()), 2, RoundingMode.HALF_DOWN)
			                    .multiply(new BigDecimal("75"));
	
					reworkExternalCal = new BigDecimal(qiCaList.get(0).getReworkVendor())
			                        .divide(new BigDecimal(qiCaList.get(0).getInspectionQty()), 2, RoundingMode.HALF_DOWN)
			                        .multiply(new BigDecimal("50"));
				 
					qualityRating=internalCal.add(caExternaCal).add(reworkExternalCal);
						
					iQualityInspectionDAO.updateQiHdrValues(String.valueOf(qiCaList.get(0).getCaInternal()),String.valueOf(qiCaList.get(0).getCaVendor()),
								String.valueOf(qiCaList.get(0).getReworkInternal()), String.valueOf(qiCaList.get(0).getReworkVendor()), String.valueOf(qiCaList.get(0).getRejectedInternal()),
								String.valueOf( qiCaList.get(0).getRejectedExternal()), qiCaList.get(0).getQiHdrId(),String.valueOf(okQty),"0","0",updateHdrReq.getEmpId(),String.valueOf(qualityRating));
					
					// update inspected ,nOk and Rework qty in po_dtl
					
					int poUpdateStatus=iQualityInspectionDAO.updateInspectedQty(String.valueOf(okQty), qiCaList.get(0).getPoDtlId(),String.valueOf(nOkQty),String.valueOf(reworkQty));
					if(poUpdateStatus==1) {
						String getreceivedQty=iQualityInspectionDAO.compareQtyAndInsQty(qiCaList.get(0).getPoDtlId());
						BigDecimal receivedQty=BigDecimal.ZERO;
						receivedQty=new BigDecimal(getreceivedQty);
						
						if(receivedQty.compareTo(BigDecimal.ZERO) > 0) {
							iQualityInspectionDAO.resetPoInspectedQty(String.valueOf(receivedQty), qiCaList.get(0).getPoDtlId());
						}
					}
					
					// insert in GRN
					String miDtlId=iQualityInspectionDAO.getMiDtlIdByCaDtlId(qiCaList.get(0).getQiCaDtlId(),qiCaList.get(0).getTenantId());
					if(miDtlId!=null && !miDtlId.equalsIgnoreCase("")) {
						int alreadyCompletedCa = iQualityInspectionDAO.getCompletedCountByQiId(
								iQualityInspectionDAO.getQiIdByQiHdrId(qiCaList.get(0).getQiHdrId()),
								qiCaList.get(0).getQiHdrId(), updateHdrReq.getCurrentseq());
						if(alreadyCompletedCa == 0) {
						//update qty_inspected in qi_request
						iQualityInspectionDAO.updateQiReqQtyInspected(String.valueOf(okQty),iQualityInspectionDAO.getQiIdByQiHdrId(qiCaList.get(0).getQiHdrId()));
//						update ok,nok,rework qty in Material_inward_dtl
						iQualityInspectionDAO.updateMiQty(miDtlId, String.valueOf(okQty), String.valueOf(nOkQty),String.valueOf(reworkQty) );
						
						String miId=iQualityInspectionDAO.getMiIdByMiDtlId(miDtlId, qiCaList.get(0).getTenantId());
						String inventoryCode=iQualityInspectionDAO.getInventoryCodeByMiId(miId);
						if (okQty.compareTo(BigDecimal.ZERO) > 0) {
							EnquiryCodeWithId grnHdrRes = grnDAO.insertGrnHdr(updateHdrReq.getEmpId(),
									CommonMethod.getCurrentDateTime(), miId,
									updateHdrReq.getTenantId(),inventoryCode,qiCaList.get(0).getPoId());
							if (grnHdrRes.getId() > 0) {
								String getInvType = iMaterialInwarDAO.getInvType(
										inventoryCode,
										updateHdrReq.getTenantId());

								String inspLoc = iMaterialInwarDAO.getInvLocationByType(getInvType,
										updateHdrReq.getTenantId(),
										inventoryCode);
								String productId=grnDAO.getProductIdByProductCode(miDtlId,"",qiCaList.get(0).getTenantId());
								iQualityInspectionDAO.udpatepreinspectionqty(qiCaList.get(0).getPmHdrId(), productId, inspLoc, new BigDecimal(String.valueOf(okQty)), "",
										"ITTC0014", grnHdrRes.getEnquiryCode(), updateHdrReq.getEmpId(), CommonMethod.getCurrentDateTime(), updateHdrReq.getTenantId());
								grnDAO.insertGrnDtl(grnHdrRes.getId(), miDtlId,
										String.valueOf(okQty),
										updateHdrReq.getTenantId(),
										qiCaList.get(0).getPoDtlId(),
										qiCaList.get(0).getIndentDtlId(),qiCaList.get(0).getPmHdrId(), qiCaList.get(0).getProductCode(),inventoryCode,updateHdrReq.getEmpId(),grnHdrRes.getEnquiryCode(),productId);		
							}
						}
						
						updateMiCompleteStatus(miId, updateHdrReq.getEmpId());
						} // end alreadyCompletedCa == 0
					}
					if(qiCaList.get(0).getIndentDtlId()!=null && !qiCaList.get(0).getIndentDtlId().equalsIgnoreCase("")) {
						String UpdatedDateTime = indentGroupDAO.getLastUpdatedDateTime(qiCaList.get(0).getIndentDtlId(), updateHdrReq.getTenantId(), type);
				            if(!UpdatedDateTime.equalsIgnoreCase("")) {
					           indentGroupDAO.updateLastUpdatedDateTime(qiCaList.get(0).getIndentDtlId(), colName, UpdatedDateTime, updateHdrReq.getTenantId());
				            }else {
				            	indentGroupDAO.reUpdatedDateTimeIndentDtl(qiCaList.get(0).getIndentDtlId(), colName, updateHdrReq.getTenantId());
				            }
					}
				}
				seqUpdated=iQualityInspectionDAO.updateQiCaSeqAndStatus(updateHdrReq.getCurrentseq(),currSeqDocLifeCycleMstList.get(0).getDocStatus(),isApproved,updateHdrReq.getHdrId(),updateHdrReq.getEmpId());
				
				// Update QI last updated datetime in indent_Dtl table
				String qiLastUpdatedDate=indentGroupDAO.getLastUpdatedDateTime(qiCaList.get(0).getIndentDtlId(), updateHdrReq.getTenantId(), "QI");
				if(!qiLastUpdatedDate.equalsIgnoreCase("")){
					indentGroupDAO.updateLastUpdatedDateTime(qiCaList.get(0).getIndentDtlId(), "QI_COMPLETED_DATETIME", qiLastUpdatedDate,  updateHdrReq.getTenantId());
				}else {
	            	indentGroupDAO.reUpdatedDateTimeIndentDtl(qiCaList.get(0).getIndentDtlId(),  "QI_COMPLETED_DATETIME", updateHdrReq.getTenantId());
	            }		
			}
			if (seqUpdated==1) {
				int statusInsert=iQualityInspectionDAO.insertQiStatus(updateHdrReq.getHdrId(), "CA", updateHdrReq.getCurrentseq(),currSeqDocLifeCycleMstList.get(0).getDocStatus(), updateHdrReq.getRemarks(), updateHdrReq.getEmpId(), updateHdrReq.getTenantId());
				if(statusInsert ==1) {
					String empDepQry=iQualityInspectionDAO.getEmpDepCode(updateHdrReq.getEmpId(),updateHdrReq.getTenantId());
					List<String> messageList = new ArrayList<>();
					List<String> otherEmp = new ArrayList<>();
					if (empDepQry.equalsIgnoreCase("D04")) {// Design approval
						String projCode = indentUploadDAO.getProjectCodeByProjId(updateHdrReq.getPmHdrId(),
								updateHdrReq.getTenantId());
						messageList.add(projCode);
						String approDesc=commonNotifyMethod.getNxtAppDesc("DC070", updateHdrReq.getCurrentseq(),updateHdrReq.getTenantId());
						commonNotifyMethod.InvokeNotificationMethod(1, 26, "", 
								updateHdrReq.getTenantId(), messageList,
								otherEmp, "",updateHdrReq.getPmId(), updateHdrReq.getMstId(), approDesc);
						commonNotifyMethod.InvokeApprovalDesigMethod(updateHdrReq.getPmId(),
								"DC070", qiCaList.get(0).getQiHdrId(), updateHdrReq.getPmHdrId(), updateHdrReq.getTenantId(),
								"", approDesc,updateHdrReq.getEnquiryId() ,projCode);
					} else if (empDepQry.equalsIgnoreCase("D03")) {// PM approval
						String projCode = indentUploadDAO.getProjectCodeByProjId(updateHdrReq.getPmHdrId(),
								updateHdrReq.getTenantId());
						messageList.add(projCode);
						commonNotifyMethod.InvokeNotificationMethod(1, 26, "", 
								updateHdrReq.getTenantId(), messageList,
								otherEmp, "1",updateHdrReq.getPmId(), updateHdrReq.getMstId(), null);
						commonNotifyMethod.InvokeApprovalDesigMethod(updateHdrReq.getPmId(),
"DC070",qiCaList.get(0).getQiHdrId(), 
								updateHdrReq.getPmHdrId(), updateHdrReq.getTenantId(),
								"", null,updateHdrReq.getEnquiryId(),projCode );
					}
					
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage("Success");
					returnMessage.setResponseMessage(ResponseMessageMap.approved);
				}
			}
			else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		} catch (Exception ex) {
			logger.error("updateQiCaSeqAndStatus error " + ex);
		}
		logger.info("updateQiCaSeqAndStatus service end");
		return returnMessage;
	}


	@Override
	public ResponseAsList getQiCaDtlsByPmHdrId(PmHdrIdAndTenantIdRequest request) {
		ResponseAsList returnList = new ResponseAsList();
		List<QiCaEntity> qiCaList = new ArrayList<QiCaEntity>();
		try {
			logger.info("getQiCaDtlsByPmHdrId service start");
			String pmHdrId = request.getPmHdrId().equalsIgnoreCase("getAll") ? "%%" :  request.getPmHdrId();
				qiCaList=iQualityInspectionDAO.getQiCaList(pmHdrId,request.getTenantId(),request.getFromDate(), request.getToDate());

			if(qiCaList.size()>0) {
				returnList.setResponseData(qiCaList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			}else {
				returnList.setResponseData(qiCaList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getQiCaDtlsByPmHdrId service end");
		} catch (Exception ex) {
			logger.error("getQiCaDtlsByPmHdrId error---> " + ex);
		}
		return returnList;
	}
	
	@Override
	public ResponseAsList getQiCaDtlsByQiCaDtlId(QiCaDtlsRequest request) {
		ResponseAsList returnList = new ResponseAsList();
		List<QiCaEntity> qiCaList = new ArrayList<QiCaEntity>();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		int approveBtnEnable=0;
		try {
			logger.info("getQiCaDtlsByPmHdrId service start");
				qiCaList=iQualityInspectionDAO.getQiCaDtlsByQiCaId(request.getQiCaDtlId(),request.getTenantId());
				if(qiCaList.size()>0) {
					
					String desigCode = uploadManagementDAO.getDesigCodeByEmpId(request.getEmpId(), request.getTenantId());
					String approveSeq = departmentAndEmployeeDAO.getPrimaryDocFlagVal(desigCode,request.getPmId(),request.getTenantId());
					qiCaList.get(0).setMasterPoc(approveSeq);

					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC070",qiCaList.get(0).getSequenceNo(), request.getTenantId());
					docLifeCycleMstList= designTaskDAO.getNextSeqandStatus(Integer.parseInt(qiCaList.get(0).getSequenceNo()), "DC070", request.getTenantId());
					if(docLifeCycleMstList.size()>0) {
						
							String designCode = uploadManagementDAO.getDesigCodeByEmpId(request.getEmpId(),
									request.getTenantId());
	
							approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, "default",
									request.getTenantId(), "DC070", docLifeCycleMstList.get(0).getCurrSequence());
							
							if (approveBtnEnable == 1) {
								// curr seq
								docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO
										.getDocTypeDescByDocType(docLifeCycleMstList.get(0).getDocType(), request.getTenantId()));
								docLifeCycleMstList.get(0)
								.setDocStatusDesc(designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(
												docLifeCycleMstList.get(0).getCurrSequence(),  request.getTenantId(), "DC070"),
										request.getTenantId()));

								// Previous Seq
								docLifeCycleMstList.get(0).setPreviousSeq(currSeqDocLifeCycleMstList.get(0).getCurrSequence());
								docLifeCycleMstList.get(0).setPreviousSeqStatusCode(indentUploadDAO
										.getStatusCodebySeqAndDocType(currSeqDocLifeCycleMstList.get(0).getCurrSequence(),  request.getTenantId(), "DC070"));
	
								docLifeCycleMstList.get(0).setPreviousSeqStatusDesc(
										designTaskDAO.getStatusByDesc(currSeqDocLifeCycleMstList.get(0).getDocStatus(),  request.getTenantId()));
							
								// cancel seq
								if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
									docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
									docLifeCycleMstList.get(0)
									.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
											currSeqDocLifeCycleMstList.get(0).getCancelSeq(), request.getTenantId(), "DC070"));
	
									docLifeCycleMstList.get(0).setCancelStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													currSeqDocLifeCycleMstList.get(0).getCancelSeq(), request.getTenantId(), "DC070"),
											request.getTenantId()));
								}
								
								qiCaList.get(0).setDocLifeCycleMstList(docLifeCycleMstList);
								qiCaList.get(0).setIsEditable(docLifeCycleMstList.get(0).getIsEditable()!=null && docLifeCycleMstList.get(0).getIsEditable()!=""? docLifeCycleMstList.get(0).getIsEditable():"0");
							}else {
								qiCaList.get(0).setIsEditable("0");
							}
						}else {
							qiCaList.get(0).setIsEditable("0");
						}
				}
			if(qiCaList.size()>0) {
				returnList.setResponseData(qiCaList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			}else {
				returnList.setResponseData(qiCaList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getQiCaDtlsByPmHdrId service end");
		} catch (Exception ex) {
			logger.error("getQiCaDtlsByPmHdrId error---> " + ex);
		}
		return returnList;
	}


	@Override
	public ResponseAsMessage updateQiCaDtls(UpdateQiCaDtlsRequest updateQiCaDtlsReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();	
		try {
			int updateCaDtls= iQualityInspectionDAO.updateQiCaDtlValues(updateQiCaDtlsReq.getCaQty(), 
					updateQiCaDtlsReq.getReworkInternal(), updateQiCaDtlsReq.getReworkVendor(), updateQiCaDtlsReq.getRejectedInternal(),
					updateQiCaDtlsReq.getRejectedExternal(), updateQiCaDtlsReq.getQiCaDtlId(),updateQiCaDtlsReq.getCaVendor(),updateQiCaDtlsReq.getCaInternal(),updateQiCaDtlsReq.getRemarks());
			
			if(updateCaDtls ==1) {
				//
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				
				String poCode=iQualityInspectionDAO.getPoCode(updateQiCaDtlsReq.getQiCaDtlId());
				String projCode = indentUploadDAO.getProjectCodeByProjId(updateQiCaDtlsReq.getPmHdrId(),
						updateQiCaDtlsReq.getTenantId());
				messageList.add(poCode);
				messageList.add( projCode);
				commonNotifyMethod.InvokeNotificationMethod(1, 25, "", 
						updateQiCaDtlsReq.getTenantId(), messageList,
						otherEmp, "",updateQiCaDtlsReq.getPmId(), updateQiCaDtlsReq.getMstId(), null);
				commonNotifyMethod.InvokeApprovalDesigMethod(updateQiCaDtlsReq.getPmId(),
						"DC070", updateQiCaDtlsReq.getQiCaDtlId(), updateQiCaDtlsReq.getPmHdrId(), updateQiCaDtlsReq.getTenantId(),
						"", "","",projCode );
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage("Success");
				returnMessage.setResponseMessage(ResponseMessageMap.successUpdated);
			}
		else {
			returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
			returnMessage.setResponseDataMessage("Failure");
			returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
		}
		}catch (Exception e) {
			logger.error("updateQiCaDtls error---> " + e);
		}
		return returnMessage;
	}
	
	public void updateMiCompleteStatus(String miId,String empId) {
		try {
			List<Integer> miQtyStatus=iQualityInspectionDAO.getMiCompletedStatus(miId);
			
	        boolean allQtyReceived = true;
	        for (int status : miQtyStatus) {
	            if (status != 1) {
	            	allQtyReceived = false;
	                break;
	            }
	        }
	        
	        if (allQtyReceived) {
	            iQualityInspectionDAO.updateMiStatus(miId, empId);
	        }
			
		}catch (Exception e) {
			logger.error("updateGrnCompleteStatus error---> " + e);
		}
	}


	@Override
	public ResponseAsMessage updateVendorRating(UpdateVendorRatingRequest updateVendorRatingRequest) {
		// TODO Auto-generated method stub
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		try {
			int updateRating = iQualityInspectionDAO.updateVendorRating(updateVendorRatingRequest);
			if (updateRating > 0) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage("Success");
				returnMessage.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		} catch (Exception ex) {
			logger.error("updateGrnComplupdateVendorRatingeteStatus error---> " + ex);
		}
		return returnMessage;
	}
	
}

				