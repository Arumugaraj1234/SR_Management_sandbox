package com.vmfg.scm.services.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.impl.StageManagementService;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.quality.services.impl.QualityInspectionService;
import com.vmfg.scm.dao.impl.GrnDAO;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
//import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.scm.dao.impl.PoDAO;
import com.vmfg.scm.dao.interfaces.IMaterialInwardDAO;
import com.vmfg.scm.entity.EnquiryCodeWithId;
import com.vmfg.scm.entity.GetPoDtlsEntity;
import com.vmfg.scm.entity.MaterialInwardDtlEntity;
import com.vmfg.scm.entity.MaterialInwardHdrEntity;
import com.vmfg.scm.entity.MaterialInwardPoDtl;
import com.vmfg.scm.entity.PoDtlEntity;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.MaterialInwardHdrRequest;
import com.vmfg.scm.services.interfaces.IMaterialInwardService;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class MaterialInwardService implements IMaterialInwardService {
	private static final Logger logger = LoggerFactory.getLogger(MaterialInwardService.class);
	@Autowired
	IMaterialInwardDAO iMaterialInwarDAO;

	@Autowired
	IndentUploadDAO indentUploadDAO;

	@Autowired
	private CommonNotifyMethod commonNotifyMethod;

	@Autowired
	GrnDAO grnDAO;

	@Autowired
	PoDAO poDAO;

	@Autowired
	PoService poService;

	@Autowired
	StageManagementService stageManagementService;

	@Autowired
	QualityInspectionService qualityInspectionService;

	@Autowired
	IndentGroupDAO IndentGroupDAO;

	@Override
	public ResponseAsList getMaterialInwardHdrDtls(MaterialInwardHdrRequest materialInwardHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MaterialInwardHdrEntity> list = new ArrayList<MaterialInwardHdrEntity>();
		logger.info("getMaterialInwardHdrDtls Service start ");
		try {
			String poId = materialInwardHdrReq.getPoId().equalsIgnoreCase("getAll") ? ""
					: materialInwardHdrReq.getPoId();

			list = iMaterialInwarDAO.getMaterialInwardHdrDtls(poId, materialInwardHdrReq.getTenantId(),
					materialInwardHdrReq.getPmHdrId(), materialInwardHdrReq.getFromDate(),materialInwardHdrReq.getToDate());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getMaterialInwardHdrDtls Service end ");
		} catch (Exception ex) {
			logger.error("getMaterialInwardHdrDtls error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getMaterialInwardDtlList(HdrIdandTenantIdRequest hdrIdandTenantIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MaterialInwardDtlEntity> list = new ArrayList<MaterialInwardDtlEntity>();
		List<IndentDtlTblEntity> indentDtlList = new ArrayList<IndentDtlTblEntity>();
		logger.info("getMaterialInwardDtlList Service start ");
		try {
			list = iMaterialInwarDAO.getMaterialInwardDtlList(hdrIdandTenantIdReq.getHdrId(),
					hdrIdandTenantIdReq.getTenantId());

			if (list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					list.get(j).setQtyInspectReqCount(iMaterialInwarDAO
							.getqtyreqcountByMiDtlId(list.get(j).getMiDtlId(), list.get(j).getTenantId()));
					if (list.get(j).getIndentDtlId() != null) {
						indentDtlList = indentUploadDAO.getIndentDtlsByIndentDtlId(list.get(j).getIndentDtlId(),
								list.get(j).getTenantId());
					} else if (list.get(j).getProductId() != null) {
						indentDtlList = indentUploadDAO.getProductDtlsByProductId(list.get(j).getProductId(),
								list.get(j).getTenantId(), hdrIdandTenantIdReq.getProjectCode());
					}

					list.get(j).setIndentDtlList(indentDtlList);

					// get Pending Qty for qc
					BigDecimal qcRequestedQty = BigDecimal.ZERO;
					BigDecimal qcOkQty = BigDecimal.ZERO;
					BigDecimal waitingForQc = BigDecimal.ZERO;
					BigDecimal qcRaisedRequestedQty = BigDecimal.ZERO;
					BigDecimal qcRaisedRequestQty = BigDecimal.ZERO;
//					BigDecimal totQty = BigDecimal.ZERO;
//					BigDecimal rejectedQty = BigDecimal.ZERO;
					List<RetrieveQualitInspectionEntity> qcReqList = new ArrayList<RetrieveQualitInspectionEntity>();

					qcReqList = poDAO.getMiQcReqDetails(list.get(j).getMiDtlId(), list.get(j).getPoDtlId());
					BigDecimal pendingQty = BigDecimal.ZERO;
					if (qcReqList.size() > 0) {
//					for (int h = 0; h < qcReqList.size(); h++) {
//						//check whether qc Inspection is Raised or not
//						int qcRaised=poDAO.checkQcIsRaised(qcReqList.get(h).getQiId());
//						
//						if(qcRaised>0) {
//							
//							//check whether qc Inspection is completed 
//							int qiHdrId=poDAO.getQcIsCompletedStatus(qcReqList.get(h).getQiId());
//							if (qiHdrId > 0) {
//								
//								// check whether CA raised or not
//								int caRaisedCnt=poDAO.getCARaisedCount(String.valueOf(qiHdrId));
//								if(caRaisedCnt>0) {
//									
//									// check whether CA approved or not
//									int caApprovedStatus = poDAO.getCaApprovedStatus(String.valueOf(qiHdrId));
//									if (caApprovedStatus == 0) {
//										qcOkQty=qcOkQty.add(new BigDecimal(poDAO.getQcOkQty(String.valueOf(qiHdrId))));
//										rejectedQty=rejectedQty.add(new BigDecimal(list.get(j).getNokQty())).add(new BigDecimal(list.get(j).getReworkQty()));
//										qcRequestedQty = qcRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
//										pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(qcRequestedQty);	
//									}else {
//										qcOkQty=qcOkQty.add(new BigDecimal(poDAO.getQcOkQty(String.valueOf(qiHdrId))));
//										rejectedQty=rejectedQty.add(new BigDecimal(list.get(j).getNokQty())).add(new BigDecimal(list.get(j).getReworkQty()));
//										qcRequestedQty = qcRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
//										totQty = qcRequestedQty.subtract(qcOkQty);
//										pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(qcRequestedQty).add(totQty);
//									}
//								}else {
//									qcOkQty=qcOkQty.add(new BigDecimal(poDAO.getQcOkQty(String.valueOf(qiHdrId))));
//									rejectedQty=rejectedQty.add(new BigDecimal(list.get(j).getNokQty())).add(new BigDecimal(list.get(j).getReworkQty()));
//									qcRequestedQty = qcRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
//									totQty = qcRequestedQty.subtract(qcOkQty);
//									if(qcReqList.get(h).getCancelFlag() != null) {
//										pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(totQty).add(rejectedQty);
//									}else {
//										pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(totQty);
//									}
//								}
//							}else {
//								qcRequestedQty = qcRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
//								rejectedQty=rejectedQty.add(new BigDecimal(list.get(j).getNokQty())).add(new BigDecimal(list.get(j).getReworkQty()));
//								pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(qcRequestedQty);	
//							}
//						}else {
//							if(qcReqList.get(h).getCancelFlag() != null) {
//								pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(new BigDecimal(qcReqList.get(h).getInspectQty()));
//							}else {
//								qcRequestedQty = qcRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
//								pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(qcRequestedQty);
//								if(list.get(j).getInspectedQty() != null) {
//									pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(new BigDecimal(list.get(j).getInspectedQty()))
//											.subtract(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
//								}
//							}
////							 pendingQty=new BigDecimal(list.get(j).getReceivedQty()).subtract(new BigDecimal(qcReqList.get(h).getQtyToBeInspected())).subtract(qcOkQty);
//						}	
//					}

						int qiHdrId = 0;
						int caApprovedStatus = 0;
						for (int h = 0; h < qcReqList.size(); h++) {
							// check whether qc Inspection is Raised or not
							int qcRaised = poDAO.checkQcIsRaised(qcReqList.get(h).getQiId());
							if (qcRaised > 0) {

								// check whether qc Inspection is completed
								qiHdrId = poDAO.getQcIsCompletedStatus(qcReqList.get(h).getQiId());
								if (qiHdrId > 0) {

									// check whether CA raised or not
									int caRaisedCnt = poDAO.getCARaisedCount(String.valueOf(qiHdrId));
									if (caRaisedCnt > 0) {

										// check whether CA approved or not
										caApprovedStatus = poDAO.getCaApprovedStatus(String.valueOf(qiHdrId));
										if (caApprovedStatus == 0) {
											qcRequestedQty = qcRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
										} else {
											qcOkQty = qcOkQty.add(new BigDecimal(poDAO.getQcOkQty(String.valueOf(qiHdrId))));
										}
									} else {
										qcOkQty = qcOkQty.add(new BigDecimal(poDAO.getQcOkQty(String.valueOf(qiHdrId))));
									}
								} else {
									qcRequestedQty = qcRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
								}
							} else {
								if (qcReqList.get(h).getCancelFlag() == null) {
									qcRaisedRequestQty = qcRaisedRequestQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));	
								}else {
								    qcRaisedRequestedQty = qcRaisedRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
								}
							}

							if (qcReqList.get(h).getCancelFlag() != null
									&& !qcReqList.get(h).getCancelFlag().equalsIgnoreCase("1")) {
								waitingForQc = new BigDecimal(qcReqList.get(h).getQtyToBeInspected())
										.subtract(new BigDecimal(qcReqList.get(h).getInspectQty()));
								if (qiHdrId > 0) {
									if (caApprovedStatus == 0) {
										pendingQty =  new BigDecimal(list.get(j).getReceivedQty()).subtract(qcOkQty).subtract(waitingForQc)
												.add(new BigDecimal(qcReqList.get(h).getReworkCount())).add(new BigDecimal(qcReqList.get(h).getRejectedCount()));
									} else {
										pendingQty = new BigDecimal(list.get(j).getReceivedQty()).subtract(qcOkQty);
									}
								} else {
									pendingQty = new BigDecimal(list.get(j).getReceivedQty()).subtract(qcOkQty).subtract(waitingForQc);
								}
							} else if (qcReqList.get(h).getCancelFlag() == null) {
								pendingQty = new BigDecimal(list.get(j).getReceivedQty()).subtract(qcRaisedRequestQty).subtract(qcOkQty).subtract(qcRequestedQty);
							} else {
								pendingQty = new BigDecimal(list.get(j).getReceivedQty()).subtract(qcRequestedQty).subtract(qcOkQty).subtract(qcRaisedRequestQty);
							}
						}
					} else {
						pendingQty = new BigDecimal(list.get(j).getReceivedQty()).subtract(new BigDecimal(list.get(j).getInspectedQty()));
					}
//					pendingQty = pendingQty.subtract(new BigDecimal(list.get(j).getInspectedQty())).add(qcOkQty).subtract(qcRequestedQty);
					list.get(j).setPendingQty(String.valueOf(pendingQty));
				}
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getMaterialInwardDtlList Service end ");
		} catch (Exception ex) {
			logger.error("getMaterialInwardDtlList error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage insertMaterialInwardDtls(List<MaterialInwardPoDtl> materialInwardPoDtl) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<GetPoDtlsEntity> poHdrList = new ArrayList<GetPoDtlsEntity>();
		try {
			logger.info("insertMaterialInwardDtls service start");
			if (materialInwardPoDtl.size() > 0) {
				String noOfParts = String.valueOf(materialInwardPoDtl.size());
//				for (int l = 0; l < materialInwardPoDtl.size(); l++) {
				int miId = 0;
				String poId = null;
				String productCode = "";
				String projectId = "";
				BigDecimal inwardRating = new BigDecimal("20");
				int allQtyReceivedFlag = 1;
				String status = "DS086";

				for (int i = 0; i < materialInwardPoDtl.size(); i++) {
					BigDecimal inwardQty = new BigDecimal(materialInwardPoDtl.get(i).getInwardQty());
					BigDecimal inspectedQty = new BigDecimal(materialInwardPoDtl.get(i).getInspectQty());
//						    .add(new BigDecimal(materialInwardPoDtl.get(i).getInspectedQty()));
					
					if (inwardQty.compareTo(inspectedQty) > 0) {
						allQtyReceivedFlag = 0;
						status = "DS085";
						break;
					}
				}

				if (materialInwardPoDtl.get(0).getPoDtlId().isEmpty()
						|| materialInwardPoDtl.get(0).getPoDtlId() == null) {
					miId = iMaterialInwarDAO.insertMaterialInwardHdr(materialInwardPoDtl.get(0).getTenantId(), null,
							materialInwardPoDtl.get(0).getPoCode(), CommonMethod.getCurrentDate(), materialInwardPoDtl.get(0).getVendorCode(), materialInwardPoDtl.get(0).getDcDate(),
							noOfParts, status, materialInwardPoDtl.get(0).getEmpId(),
							materialInwardPoDtl.get(0).getDcNo(), materialInwardPoDtl.get(0).getInvLocation(),
							materialInwardPoDtl.get(0).getPmHdrId(), inwardRating.toString(),
							materialInwardPoDtl.get(0).getRelationShipRating(), String.valueOf(allQtyReceivedFlag));
				} else {
					poHdrList = poDAO.getPoDtlsByPoDtlId(materialInwardPoDtl.get(0).getPoDtlId());
					if (poHdrList.size() > 0) {
						poId = poHdrList.get(0).getPoId();
//							poDtlId=materialInwardPoDtl.get(0).getPoDtlId();
						String sDate1 = poHdrList.get(0).getDeliveryDate();
						Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
						Date currdate = new SimpleDateFormat("yyyy-MM-dd").parse(CommonMethod.getCurrentDate());
						if (date1.before(currdate)) {
							float dateDiffL = currdate.getTime() - date1.getTime();
							int dateDiff = (int) (dateDiffL / (1000 * 60 * 60 * 24));
							double delayRating = dateDiff * 0.5;
							inwardRating = inwardRating.subtract(new BigDecimal(delayRating));
							if (inwardRating.compareTo(new BigDecimal("0")) <= 0) {
								inwardRating = new BigDecimal("0");
							}
						}
					}

					miId = iMaterialInwarDAO.insertMaterialInwardHdr(materialInwardPoDtl.get(0).getTenantId(),
							poHdrList.get(0).getPoId(), poHdrList.get(0).getPoCode(), CommonMethod.getCurrentDate(),
							poHdrList.get(0).getVendorCode(), materialInwardPoDtl.get(0).getDcDate(), noOfParts, status,
							materialInwardPoDtl.get(0).getEmpId(), materialInwardPoDtl.get(0).getDcNo(),
							materialInwardPoDtl.get(0).getInvLocation(), materialInwardPoDtl.get(0).getPmHdrId(),
							inwardRating.toString(), materialInwardPoDtl.get(0).getRelationShipRating(),
							String.valueOf(allQtyReceivedFlag));

				}
				if (miId > 0) {
					for (int i = 0; i < materialInwardPoDtl.size(); i++) {
						String miCode = iMaterialInwarDAO.getMICodeById(Integer.toString(miId));
						String totalQty = "";
						if (materialInwardPoDtl.get(i).getIndentDtlId() != null) {
							productCode = poDAO.getProdCodeByIndentDtlId(materialInwardPoDtl.get(i).getIndentDtlId());
							projectId = poDAO.getPmHdrIdByIndentDtlId(materialInwardPoDtl.get(i).getIndentDtlId());
							totalQty = materialInwardPoDtl.get(i).getQty();
						} else {
							totalQty = materialInwardPoDtl.get(i).getPoqty();
							productCode = materialInwardPoDtl.get(i).getProductCode();
							projectId = materialInwardPoDtl.get(i).getPmHdrId();

						}
						String uom = materialInwardPoDtl.get(i).getUom();

						String getInvType = iMaterialInwarDAO.getInvType(materialInwardPoDtl.get(0).getInvLocation(),
								materialInwardPoDtl.get(0).getTenantId());

						String inspLoc = iMaterialInwarDAO.getInvLocationByType(getInvType,
								materialInwardPoDtl.get(0).getTenantId(), materialInwardPoDtl.get(0).getInvLocation());

						String productId = "";
						if (materialInwardPoDtl.get(i).getIndentDtlId() != null) {
							productId = grnDAO.getProductIdAndProductCode(productCode,materialInwardPoDtl.get(i).getDescription(),
									materialInwardPoDtl.get(i).getSpecification(),materialInwardPoDtl.get(i).getUnitRate(),projectId,
									materialInwardPoDtl.get(i).getTenantId());
						} else {

							if (!materialInwardPoDtl.get(i).getUom().equalsIgnoreCase("")) {

								uom = indentUploadDAO.getUomCodeByUnit(materialInwardPoDtl.get(i).getUom(),
										materialInwardPoDtl.get(i).getTenantId());
								if (uom.equalsIgnoreCase("0")) {
									indentUploadDAO.createNewUomAndInsert(materialInwardPoDtl.get(i).getUom().trim(),
											materialInwardPoDtl.get(i).getTenantId());
									uom = indentUploadDAO.getUomCodeByUnit(materialInwardPoDtl.get(i).getUom().trim(),
											materialInwardPoDtl.get(i).getTenantId());
								}
							}

						}
                         
						if(productId.equalsIgnoreCase("")) {
							int productId1 = indentUploadDAO.insertnewProductInProdMst(productCode,
									materialInwardPoDtl.get(i).getPmHdrId(),
									materialInwardPoDtl.get(i).getDescription(), uom,
									materialInwardPoDtl.get(i).getTenantId(), materialInwardPoDtl.get(i).getEmpId(),
									materialInwardPoDtl.get(i).getSpecification(), materialInwardPoDtl.get(i).getMake(),
									materialInwardPoDtl.get(i).getQty(), "0", null,
									materialInwardPoDtl.get(i).getMaterial(), null, null, null, null);

							// After insert, update PRODUCT_COST_PER_UNIT
								String unitRate = materialInwardPoDtl.get(i).getUnitRate(); // assuming you have it
								indentUploadDAO.updateProductCostPerUnit(productId1, unitRate);
							productId = String.valueOf(productId1);
						}
						BigDecimal inspectQty = new BigDecimal(materialInwardPoDtl.get(i).getInspectQty());
						// Floor at 0 — a negative Inward Qty must never be persisted or used to decrement po_dtl.RECEIVED_QTY
						BigDecimal inwardQty = new BigDecimal(materialInwardPoDtl.get(i).getInwardQty()).max(BigDecimal.ZERO);

						String result = "0";
						if (!materialInwardPoDtl.get(i).getInspectedQty().equalsIgnoreCase("0.000")) {
							result = String.valueOf(inspectQty.max(BigDecimal.ZERO).min(inwardQty));
						}

						String poDtlId = "0";
						if(!materialInwardPoDtl.get(i).getPoDtlId().isEmpty()) {
							poDtlId=materialInwardPoDtl.get(i).getPoDtlId();
						}
						int miDtlId = iMaterialInwarDAO.insertInMiDtl(String.valueOf(miId),
								poDtlId, materialInwardPoDtl.get(i).getIndentDtlId(),
								totalQty, inwardQty.toString(), result, uom,
								materialInwardPoDtl.get(i).getTenantId(), projectId, productCode, miCode,
								materialInwardPoDtl.get(i).getEmpId(), inspLoc, materialInwardPoDtl.get(i).getRemarks(),
								productId);


						// update receivedQty in po_dtl
						int updateQty = 0;
						if (materialInwardPoDtl.get(i).getPoDtlId() != null) {
							updateQty = iMaterialInwarDAO.updateReceivedQty(inwardQty.toString(),
									materialInwardPoDtl.get(i).getPoDtlId());
						}

						// update bin and inward datetime in prod_mst
						iMaterialInwarDAO.updateBinInprodMst(productId, materialInwardPoDtl.get(i).getBin());

						if (miDtlId > 0 || (materialInwardPoDtl.get(i).getIndentDtlId() != null && updateQty == 1)) {

							String updatedDateTime = "";
							String type = "MI";
							String colName = "MI_COMPLETED_DATETIME";
							if (materialInwardPoDtl.get(i).getIndentDtlId() != null) {
								updatedDateTime = IndentGroupDAO.getLastUpdatedDateTime(
										materialInwardPoDtl.get(i).getIndentDtlId(),
										materialInwardPoDtl.get(i).getTenantId(), type);
								if (!updatedDateTime.equalsIgnoreCase("") && updatedDateTime != null) {
									IndentGroupDAO.updateLastUpdatedDateTime(
											materialInwardPoDtl.get(i).getIndentDtlId(), colName, updatedDateTime,
											materialInwardPoDtl.get(i).getTenantId());
								}else {
					            	IndentGroupDAO.reUpdatedDateTimeIndentDtl(materialInwardPoDtl.get(i).getIndentDtlId(),
					            			colName, materialInwardPoDtl.get(i).getTenantId());
					            }
							}

							String grnQty = "";

							// GRN insert
							String grnHdrId = "", getGrnHdrId = "", grnEnquiryCode = "" ;

							if (inspectQty.compareTo(BigDecimal.ZERO) > 0) {
								if (inwardQty.compareTo(inspectQty) < 0) {
									grnQty = inwardQty.toString();
								} else {
									grnQty = materialInwardPoDtl.get(i).getInspectQty();
								}
								iMaterialInwarDAO.updateMiDtlId(Integer.toString(miDtlId),
										materialInwardPoDtl.get(i).getPoDtlId(), inspectQty.toString());
								getGrnHdrId = iMaterialInwarDAO.getgrnHdrId(String.valueOf(miId),
										materialInwardPoDtl.get(0).getInvLocation(), poId,materialInwardPoDtl.get(0).getPoCode());

								if (getGrnHdrId.equalsIgnoreCase("NA")) {
									EnquiryCodeWithId hdrId = grnDAO.insertGrnHdr(materialInwardPoDtl.get(i).getEmpId(),
											CommonMethod.getCurrentDate(), String.valueOf(miId),
											materialInwardPoDtl.get(i).getTenantId(),
											materialInwardPoDtl.get(0).getInvLocation(), poId);
									grnHdrId = String.valueOf(hdrId.getId());
									grnEnquiryCode = hdrId.getEnquiryCode();

									String generatedPoCode = materialInwardPoDtl.get(0).getPoCode();
									if (generatedPoCode != null && !generatedPoCode.isEmpty()) {
										grnDAO.updatePoCodeInGrnHdr(Integer.parseInt(grnHdrId), generatedPoCode);
									}


								} else {
									grnHdrId = getGrnHdrId;
									grnEnquiryCode = iMaterialInwarDAO.getGrnEnqCode(grnHdrId);
								}

								if (Integer.parseInt(grnHdrId) > 0) {

									grnDAO.insertGrnDtl(Integer.parseInt(grnHdrId), String.valueOf(miDtlId), grnQty,
											materialInwardPoDtl.get(i).getTenantId(),
											materialInwardPoDtl.get(i).getPoDtlId(),
											materialInwardPoDtl.get(i).getIndentDtlId(), projectId, productCode,
											materialInwardPoDtl.get(0).getInvLocation(),
											materialInwardPoDtl.get(i).getEmpId(), grnEnquiryCode, productId);

								}
							} else {
								iMaterialInwarDAO.updateMiDtlId(Integer.toString(miDtlId),
										materialInwardPoDtl.get(i).getPoDtlId(), inspectQty.toString());
							}
							String typeName = "GRN";
							String col = "GRN_COMPLETED_DATETIME";
							String UpdatedDate = IndentGroupDAO.getLastUpdatedDateTime(
									materialInwardPoDtl.get(i).getIndentDtlId(),
									materialInwardPoDtl.get(i).getTenantId(), typeName);

							if (!UpdatedDate.equalsIgnoreCase("") && UpdatedDate != null) {
								IndentGroupDAO.updateLastUpdatedDateTime(materialInwardPoDtl.get(i).getIndentDtlId(),
										col, UpdatedDate, materialInwardPoDtl.get(i).getTenantId());
							}else {
				            	IndentGroupDAO.reUpdatedDateTimeIndentDtl(materialInwardPoDtl.get(i).getIndentDtlId(),
				            			col, materialInwardPoDtl.get(i).getTenantId());
				            }
							returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
							returnMessage.setResponseMessage(ResponseMessageMap.successCreated);
						}
					}
					qualityInspectionService.updateMiCompleteStatus(String.valueOf(miId),
							materialInwardPoDtl.get(0).getEmpId());

					String poCodeInt = iMaterialInwarDAO.getPoCodeByPoId(materialInwardPoDtl.get(0).getTenantId(),
							poId);
					int IndentId = iMaterialInwarDAO.getIndentIdPoId(materialInwardPoDtl.get(0).getTenantId(), poId);
					int pmHdrId = iMaterialInwarDAO.getPmHdrIdByIndentId(materialInwardPoDtl.get(0).getTenantId(),
							IndentId);

//						String enquiryId=grnDAO.getenquiryId(Integer.toString(pmHdrId));
					List<String> messageList = new ArrayList<String>();
					messageList.add(poCodeInt);
					String projectCode = indentUploadDAO.getProjectCodeByProjId(String.valueOf(pmHdrId),
							materialInwardPoDtl.get(0).getTenantId());
					messageList.add(projectCode);
					List<String> otherEmp = new ArrayList<>();
					String NotifyEmp = grnDAO.getNotifyEmp(materialInwardPoDtl.get(0).getTenantId());
					if (!NotifyEmp.isEmpty()) {
						String empArr[] = NotifyEmp.split(",");
						for (int emp = 0; emp < empArr.length; emp++) {
							otherEmp.add(empArr[emp]);
						}
					}

					//
					int scmHdrId = iMaterialInwarDAO.getscmHdrIdByPmHdrId(materialInwardPoDtl.get(0).getTenantId(),
							pmHdrId);

					commonNotifyMethod.InvokeNotificationMethod(1, 24, "", materialInwardPoDtl.get(0).getTenantId(),
							messageList, otherEmp, "1", materialInwardPoDtl.get(0).getPmId(),
							Integer.toString(scmHdrId), null);
					// commonNotifyMethod.InvokeApprovalDesigMethod(materialInwardPoDtl.get(0).getPmId(),
					// "", poId,
					// Integer.toString(pmHdrId), materialInwardPoDtl.get(0).getTenantId(), "",
					// "DS04,DS11",
					// enquiryId, poCodeInt);

				} else {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					returnMessage.setResponseMessage(ResponseMessageMap.failToCreateMsg);
				}
			}
		} catch (Exception e) {
			logger.error("insertMaterialInwardDtls error " + e);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getPoDtlsForMaterialInward(HdrIdandTenantIdRequest hdrIdandTenantIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MaterialInwardPoDtl> mainList = new ArrayList<MaterialInwardPoDtl>();

		List<PoDtlEntity> poDtlList = new ArrayList<PoDtlEntity>();
		BigDecimal inspectQty = BigDecimal.ZERO;
		logger.info("getPoDtlsForMaterialInward Service start ");
		try {
			poDtlList = poDAO.getPoDtlList(hdrIdandTenantIdReq.getHdrId());
			if (poDtlList.size() > 0) {
				for (int j = 0; j < poDtlList.size(); j++) {
					MaterialInwardPoDtl miDtl = new MaterialInwardPoDtl();
//					BigDecimal inspectedQty= new BigDecimal(poDtlList.get(j).getInspectedQty());
//					BigDecimal receivedQty = new BigDecimal(poDtlList.get(j).getReceivedQty());
					miDtl.setInspectedQty(poDtlList.get(j).getInspectedQty());
					miDtl.setReceivedQty(poDtlList.get(j).getReceivedQty());
					miDtl.setPoDtlId(poDtlList.get(j).getPoDtlId());
					miDtl.setQty(poDtlList.get(j).getQty());
					miDtl.setTotalValue(poDtlList.get(j).getTotalValue());
					miDtl.setUnitRate(poDtlList.get(j).getUnitRate());


					List<IndentDtlTblEntity> indentDtlList = indentUploadDAO.getIndentDtlsByIndentDtlId(
							poDtlList.get(j).getIndentDtlId(), hdrIdandTenantIdReq.getTenantId());
					if (indentDtlList.size() > 0) {
						miDtl.setDescription(indentDtlList.get(0).getDescription());
						miDtl.setMaterial(indentDtlList.get(0).getMaterial());
						miDtl.setProductCode(indentDtlList.get(0).getProductCode());
						miDtl.setSpecification(indentDtlList.get(0).getSpecification());
						miDtl.setUom(indentDtlList.get(0).getUnit());
						miDtl.setUomDesc(indentDtlList.get(0).getUomDesc());
						miDtl.setWeight(indentDtlList.get(0).getWeight());
						miDtl.setIndentDtlId(indentDtlList.get(0).getIndentDtlId());
					}

					String poInspectedQty = iMaterialInwarDAO.getPoInspectedQty(poDtlList.get(j).getPoDtlId());

					String grnReceivedQty = iMaterialInwarDAO.getGrnDtlReceivedQty(poDtlList.get(j).getPoDtlId());
					inspectQty = new BigDecimal(poInspectedQty).subtract(new BigDecimal(grnReceivedQty));
					miDtl.setInspectQty(String.valueOf(inspectQty) != null ? String.valueOf(inspectQty) : "0");
					miDtl.setInwardQty("0");

//					get pending qty to inward
					List<MaterialInwardDtlEntity> miList = new ArrayList<MaterialInwardDtlEntity>();
					BigDecimal miReceivedQty = BigDecimal.ZERO;
					BigDecimal miInspectedQty = BigDecimal.ZERO;
					miList = iMaterialInwarDAO.getMiDtlByPoDtlId(poDtlList.get(j).getPoDtlId());
					if (miList.size() > 0) {
						for (int h = 0; h < miList.size(); h++) {
							int miQtyStatus = iMaterialInwarDAO.checkMiQtyStatus(miList.get(h).getMiDtlId());
							if (miQtyStatus == 0) {
								miReceivedQty = miReceivedQty.add(new BigDecimal(miList.get(h).getReceivedQty()));
							} else {
								miInspectedQty = miInspectedQty.add(new BigDecimal(miList.get(h).getInspectedQty()));
							}
						}
						miDtl.setBin(iMaterialInwarDAO.getBinValue(miList.get(0).getProductId()));
					}

					BigDecimal pendingQty = new BigDecimal(poDtlList.get(j).getQty()).subtract(miReceivedQty)
							.subtract(miInspectedQty);
					miDtl.setQtyToInward(String.valueOf(pendingQty));

					BigDecimal rejectedQty = new BigDecimal(poDtlList.get(j).getNOkCount())
							.add(new BigDecimal(poDtlList.get(j).getReWorkCount()));
					miDtl.setRejectedQty(String.valueOf(rejectedQty));

					// get Waiting for Qc qty
					BigDecimal qcRequestedQty = BigDecimal.ZERO;
					BigDecimal qcOkQty = BigDecimal.ZERO;
					List<RetrieveQualitInspectionEntity> qcReqList = new ArrayList<RetrieveQualitInspectionEntity>();

					qcReqList = poDAO.getQcReqDetails(poDtlList.get(j).getPoDtlId(), "PO");
					for (int h = 0; h < qcReqList.size(); h++) {
                        String waitingForQC = poDAO.getQcForwaitingStatus(poDtlList.get(j).getPoDtlId(),qcReqList.get(h).getQiId());
						// check whether qc Inspection is completed
						int qiHdrId = poDAO.getQcIsCompletedStatus(qcReqList.get(h).getQiId());
						if (qiHdrId > 0) {
//							qcRequestedQty = qcRequestedQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
//						} else {
							// check whether CA raised or not
							int caRaisedCnt = poDAO.getCARaisedCount(String.valueOf(qiHdrId));
							if (caRaisedCnt > 0) {
								// check whether CA approved or not
								int caApprovedStatus = poDAO.getCaApprovedStatus(String.valueOf(qiHdrId));
								if (caApprovedStatus == 0) {
									qcRequestedQty = qcRequestedQty
											.add(new BigDecimal(waitingForQC));
								} else {
									qcOkQty = qcOkQty.add(new BigDecimal(poDAO.getQcOkQty(String.valueOf(qiHdrId))));
								}
							} else {
								qcOkQty = qcOkQty.add(new BigDecimal(poDAO.getQcOkQty(String.valueOf(qiHdrId))));
							}
						}else {
							if(qcReqList.get(h).getCancelFlag() == null || !qcReqList.get(h).getCancelFlag().equalsIgnoreCase("1")) {
								qcRequestedQty = qcRequestedQty.add(new BigDecimal(waitingForQC));	
							}
						}
					}
					miDtl.setWaitingForQc(String.valueOf(qcRequestedQty));
					mainList.add(miDtl);
				}
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getPoDtlsForMaterialInward Service end ");
		} catch (Exception ex) {
			logger.error("getPoDtlsForMaterialInward error " + ex);
		}
		return returnList;
	}

}
