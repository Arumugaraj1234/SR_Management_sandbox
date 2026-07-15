package com.vmfg.scm.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.design.request.IdAndTenantIdRequest;
import com.vmfg.design.request.TenantRequest;
import com.vmfg.finance.dao.impl.PraDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.master.entity.VendorMstEntity;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.sales.request.GetAddressDtlByDcTypeRequest;
import com.vmfg.scm.dao.impl.GrnDAO;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.scm.dao.impl.MaterialInwardDAO;
import com.vmfg.scm.dao.impl.PoDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.entity.AddressDtlByDcTypeEntity;
import com.vmfg.scm.entity.BillingDetailEntity;
import com.vmfg.scm.entity.DcDtlEntity;
import com.vmfg.scm.entity.DcHdrEntity;
import com.vmfg.scm.entity.DebitNoteEntity;
import com.vmfg.scm.entity.GetDCTypeDtlEntity;
import com.vmfg.scm.entity.GetPoDtlsByDate;
import com.vmfg.scm.entity.GetPoDtlsEntity;
import com.vmfg.scm.entity.GetProductUnitCostEntity;
import com.vmfg.scm.entity.IndentGrpHdrIdEntity;
import com.vmfg.scm.entity.LocationMstEntity;
import com.vmfg.scm.entity.PoDescMstEntity;
import com.vmfg.scm.entity.PoDispatchDocEntity;
import com.vmfg.scm.entity.PoDtlEntity;
import com.vmfg.scm.entity.PoHsnEntity;
import com.vmfg.scm.entity.PoInstoreDtlEntity;
import com.vmfg.scm.entity.PoPaymentTermEntity;
import com.vmfg.scm.entity.PoStatusEntity;
import com.vmfg.scm.request.GetDcDtlByDcIdRequest;
import com.vmfg.scm.request.GetpoInstoreDtlByPmIdRequest;
import com.vmfg.scm.request.IndentGrpDtlRequest;
import com.vmfg.scm.request.PoHSNCodeRequest;
import com.vmfg.scm.request.PoTypeUpdateReq;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.scm.request.getDCProductDropDownRequest;
import com.vmfg.scm.services.interfaces.IPoServices;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class PoService implements IPoServices {
	private static final Logger logger = LoggerFactory.getLogger(PoService.class);
	@Autowired
	private IPoDAO iPoDAO;

	@Autowired
	private PoDAO poDAO;

	@Autowired
	private PraDAO praDAO;

	@Autowired
	IndentUploadDAO indentUploadDAO;

	@Autowired
	DesignTaskDAO designTaskDAO;

	@Autowired
	StageManagementDAO stageManagementDAO;

	@Autowired
	UploadManagementDAO uploadManagementDAO;

	@Autowired
	MaterialInwardDAO materialInwardDAO;

	@Autowired
	IndentGroupDAO indentGroupDAO;

	@Autowired
	CommonNotifyMethod commonNotifyMethod;

	@Autowired
	GrnDAO grnDAO;

	@Override
	public ResponseAsList getPoHdrDtlsByIndentId(IdAndTenantIdRequest idAndTenantIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetPoDtlsEntity> list;
		List<GetPoDtlsEntity> mainList = new ArrayList<GetPoDtlsEntity>();
		List<IndentGrpHdrIdEntity> ighdrIdList = new ArrayList<IndentGrpHdrIdEntity>();

		try {
			if (idAndTenantIdReq.getHdrId().equalsIgnoreCase("getAll")) {
				ighdrIdList = iPoDAO.getAllPoHdrByIndentId(idAndTenantIdReq.getProjectId(),
						idAndTenantIdReq.getTenantId());
			} else {
				ighdrIdList = iPoDAO.getIndentGrpHdrIdList(idAndTenantIdReq.getHdrId(), idAndTenantIdReq.getTenantId());
			}
			if (ighdrIdList.size() > 0) {
				for (int i = 0; i < ighdrIdList.size(); i++) {

					list = iPoDAO.getPoHdrListByIgHdrId(ighdrIdList.get(i).getIgHdrId(),
							idAndTenantIdReq.getTenantId());
					mainList.addAll(list);
				}
			}
			for (int j = 0; j < mainList.size(); j++) {
				String partCount = poDAO.getPartCount(mainList.get(j).getPoId());
				mainList.get(j).setPartCount(partCount);
				if(!mainList.get(j).getPoType().equalsIgnoreCase("0") && mainList.get(j).getIsApproved().equalsIgnoreCase("1")){
				String inspectionStatus = poDAO.getInspectionStatusByPoHdrId(mainList.get(j).getPoId(), idAndTenantIdReq.getTenantId());
				mainList.get(j).setInspectionStatus(inspectionStatus); //Inspection Status
				
				String pendingAmtForPra = poDAO.getPendingAmtForPraStatus(mainList.get(j).getPoId());
				if (pendingAmtForPra != null && Double.parseDouble(pendingAmtForPra) > 0) {
				    mainList.get(j).setPraStatus("Pending");
				} else {
				    mainList.get(j).setPraStatus("Completed");
				}
				}
//				else mainList.get(j).setInspectionStatus(null);
			}
			if (mainList.size() > 0) {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getPoHdrDtlsByIndentId service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPoDtlsByPoId(IdAndTenantIdRequest idAndTenantIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetPoDtlsEntity> mainList = null;
		List<GetPoDtlsEntity> preRevisionList = null;
		List<PoDtlEntity> poDtl = null;
		List<PoPaymentTermEntity> poPaymentTerm = null;
		List<PoDispatchDocEntity> poDispatchDoc = null;
		List<IndentDtlTblEntity> indentDtlList = new ArrayList<IndentDtlTblEntity>();
		DocumentStatusMstEntity docList = new DocumentStatusMstEntity();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<PoStatusEntity> poStatusList = new ArrayList<PoStatusEntity>();
		List<DebitNoteEntity> debitNoteEntity = new ArrayList<>();
		int approveBtnEnable = 0;
		try {
			mainList = iPoDAO.getPoDtlsByPoId(idAndTenantIdReq.getHdrId(), idAndTenantIdReq.getTenantId());
			if (mainList.size() > 0) {
				preRevisionList = poDAO.getPreRevisionPoDtls(mainList.get(0).getIgScpId(),
						mainList.get(0).getRevision(), mainList.get(0).getTenantId());

				for (int i = 0; i < mainList.size(); i++) {
					List<PoDescMstEntity> divisionDesc = poDAO.getdivisionDescById(idAndTenantIdReq.getTenantId(),
							mainList.get(i).getDivision());
					List<PoDescMstEntity> transitInsuranceDesc = poDAO.getransitInsuranceDescById(
							idAndTenantIdReq.getTenantId(), mainList.get(i).getTransitInsurance());
					List<PoDescMstEntity> dispatchModeDesc = poDAO.getModeOfDispatchDescById(
							idAndTenantIdReq.getTenantId(), mainList.get(i).getDispatchMode());
					String gstType = poDAO.getGstType(idAndTenantIdReq.getTenantId(), mainList.get(i).getVendorCode());
					mainList.get(i).setGstType(gstType);
					String partCount = poDAO.getPartCount(mainList.get(i).getPoId());
					mainList.get(i).setPartCount(partCount);
					List<PoDescMstEntity> inspectionScopeDesc = poDAO.getInspectScopeDescById(
							idAndTenantIdReq.getTenantId(), mainList.get(i).getInspectionScope());
					if (divisionDesc.size() > 0) {
						mainList.get(i).setDivisionDesc(divisionDesc.get(0).getDesc());

					}
					if (transitInsuranceDesc.size() > 0) {
						mainList.get(i).setTransitInsuranceDesc(transitInsuranceDesc.get(0).getDesc());

					}
					if (dispatchModeDesc.size() > 0) {
						mainList.get(i).setDispatchModeDesc(dispatchModeDesc.get(0).getDesc());

					}
					if (inspectionScopeDesc.size() > 0) {
						mainList.get(i).setInspectionScopeDesc(inspectionScopeDesc.get(0).getDesc());

					}
					poDtl = iPoDAO.getPoDtlList(mainList.get(i).getPoId());
					if(poDtl.size()>0) {
						for(int p = 0; p < poDtl.size(); p++) {
							String otherLocQty = iPoDAO.getMiDtlList(poDtl.get(p).getPoDtlId(),mainList.get(i).getTenantId());
							poDtl.get(p).setInwardQty(otherLocQty);
						}
					}
					int praCheck = iPoDAO.praCheck(mainList.get(i).getPoId());
					if (praCheck > 0) {
						// --- Transport Charge Calculation ---
						String pendingTransportChrg = getPendingTransportChrg(mainList.get(i).getPoId());
						BigDecimal transportCharge = new BigDecimal(mainList.get(i).getTransportCharges());
						BigDecimal pendingTransPort = new BigDecimal(pendingTransportChrg);
						String pendingTransport = transportCharge.subtract(pendingTransPort).toPlainString();
						System.out.println(pendingTransport + " Pending Transport");
						mainList.get(i).setPendingTransportChrg(pendingTransport);

						// --- Insurance Charge Calculation ---
						String pendingInsuranceChrg = iPoDAO.getPendingInsuranceChrg(mainList.get(i).getPoId());
						BigDecimal insurance = new BigDecimal(mainList.get(i).getInsuranceValue());
						BigDecimal praInsurance = new BigDecimal(pendingInsuranceChrg);
						String pendingInsurance = insurance.subtract(praInsurance).toPlainString();
						mainList.get(i).setPendingInsuranceChrg(pendingInsurance);

						// --- Other Charges Calculation ---
						String pendingOtherChrg = iPoDAO.getPendingOtherChrg(mainList.get(i).getPoId());
						BigDecimal otherCharge = new BigDecimal(mainList.get(i).getOthers());
						BigDecimal praOther = new BigDecimal(pendingOtherChrg);
						String pendingOther = otherCharge.subtract(praOther).toPlainString();
						mainList.get(i).setPendingOtherChrg(pendingOther);

						// --- PF Charges Calculation ---
						String pendingPfChrg = iPoDAO.getPendingPfChrg(mainList.get(i).getPoId());
						BigDecimal pfCharge = new BigDecimal(mainList.get(i).getPF());
						BigDecimal praPf = new BigDecimal(pendingPfChrg);
						String pendingPf = pfCharge.subtract(praPf).toPlainString();
						mainList.get(i).setPendingPfChrg(pendingPf);
					}
					debitNoteEntity = poDAO.getDebitNoteReason("1",mainList.get(0).getTenantId());
					mainList.get(i).setDebitNoteReasonList(debitNoteEntity);
					for (int j = 0; j < poDtl.size(); j++) {
						poDtl.get(j).setQtyInspectReqCount(
								Integer.toString(iPoDAO.qtyInspectReqCount(poDtl.get(j).getPoDtlId())));
						indentDtlList = indentUploadDAO.getIndentDtlsByIndentDtlId(poDtl.get(j).getIndentDtlId(),
								idAndTenantIdReq.getTenantId());
						for (int k = 0; k < indentDtlList.size(); k++) {
							int dmId = indentUploadDAO.getDmIdByLatestVerion(indentDtlList.get(k).getIndentDtlId(),
									idAndTenantIdReq.getTenantId());
							indentDtlList.get(k).setDmId(dmId);
						}
						String productCode = iPoDAO.getProdCodeByIndentDtlId(poDtl.get(j).getIndentDtlId());

//						String qcRequestedQty=iPoDAO.getQcRequestyQty(poDtl.get(j).getPoDtlId());
						BigDecimal qcRequestedQty = BigDecimal.ZERO;

						// get pending qty
						String pendingQty = getQcPendingQty(poDtl.get(j).getPoDtlId(), poDtl.get(j).getQty());

						poDtl.get(j).setPendingQty(String.valueOf(pendingQty));
						poDtl.get(j).setProductCode(productCode);
						poDtl.get(j).setIndentDtlList(indentDtlList);
						poDtl.get(j).setQcRequestedQty(String.valueOf(qcRequestedQty));

					}
					poPaymentTerm = iPoDAO.getPoPaymentTermList(mainList.get(i).getPoId());
					AtomicReference<String> lastPaymentTerm = new AtomicReference<>("");

					poPaymentTerm.stream()
							.filter(p -> "1".equalsIgnoreCase(p.getIsLast()))
							.findFirst()
							.ifPresent(p -> {
								lastPaymentTerm.set(p.getPotId());
							});
					String lastPotId = lastPaymentTerm.get();
					int praCheckByPotId = iPoDAO.praCheckByPotId(lastPotId);
					if(praCheckByPotId > 0){

						String gstSumByPotId = iPoDAO.gstSumByPotId(lastPotId);
						BigDecimal gstCharge = new BigDecimal(mainList.get(i).getGst());
						String igstStr = mainList.get(i).getIgst();
						BigDecimal iGstCharge = (igstStr != null
								&& !igstStr.trim().isEmpty()
								&& !"null".equalsIgnoreCase(igstStr))
								? new BigDecimal(igstStr.trim())
								: BigDecimal.ZERO;
						BigDecimal addIGstAndGstCharge = gstCharge.add(iGstCharge);
						BigDecimal pendingGstByPotId = new BigDecimal(gstSumByPotId);
						String pendingGst = addIGstAndGstCharge.subtract(pendingGstByPotId).toPlainString();
						mainList.get(i).setPendingGst(pendingGst);
					}
					// Adding PRA Status
					if (poPaymentTerm != null && poPaymentTerm.size() > 0) {
						for (PoPaymentTermEntity payment : poPaymentTerm) {
							String status = iPoDAO.getPaymentTermStatus(payment.getPotId());
							String[] cnt = (status != null) ? status.split("\\|") : null;
							payment.setIsCreated(cnt[0]);
							payment.setPraDate(cnt[1]);
							payment.setPraCode(cnt[2]);
							payment.setDocumentStatus(cnt[3]);
						}
					}
					poDispatchDoc = iPoDAO.getPoDispatchDocList(mainList.get(i).getPoId());
					poStatusList = iPoDAO.getPoStatusList(mainList.get(i).getPoId());
					String currSeq = mainList.get(i).getSequenceNo();
					mainList.get(i).setSeqStatusDesc(designTaskDAO.getStatusByDesc(mainList.get(i).getSequenceStatus(),
							idAndTenantIdReq.getTenantId()));

					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC044", currSeq,
							idAndTenantIdReq.getTenantId());
					docLifeCycleMstList = designTaskDAO.getNextSeqandStatus(Integer.parseInt(currSeq), "DC044",
							idAndTenantIdReq.getTenantId());

					if (docLifeCycleMstList.size() > 0) {

						String designCode = uploadManagementDAO.getDesigCodeByEmpId(idAndTenantIdReq.getEmpId(),
								idAndTenantIdReq.getTenantId());

						approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, "default",
								idAndTenantIdReq.getTenantId(), "DC044", docLifeCycleMstList.get(0).getCurrSequence());

						if (approveBtnEnable == 1) {
							// curr seq
							docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO.getDocTypeDescByDocType(
									docLifeCycleMstList.get(0).getDocType(), idAndTenantIdReq.getTenantId()));
							docLifeCycleMstList.get(0)
									.setDocStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													docLifeCycleMstList.get(0).getCurrSequence(),
													idAndTenantIdReq.getTenantId(), "DC044"),
											idAndTenantIdReq.getTenantId()));// Current seq docStatus

							// Previous Seq
							docLifeCycleMstList.get(0).setPreviousSeq(currSeq);
							docLifeCycleMstList.get(0).setPreviousSeqStatusCode(indentUploadDAO
									.getStatusCodebySeqAndDocType(currSeq, idAndTenantIdReq.getTenantId(), "DC044"));

							docLifeCycleMstList.get(0)
									.setPreviousSeqStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
													idAndTenantIdReq.getTenantId(), "DC044"),
											idAndTenantIdReq.getTenantId()));
							// cancel seq
							if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
								docLifeCycleMstList.get(0)
										.setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
								docLifeCycleMstList.get(0)
										.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
												idAndTenantIdReq.getTenantId(), "DC044"));

								docLifeCycleMstList.get(0)
										.setCancelStatusDesc(
												designTaskDAO
														.getStatusByDesc(
																indentUploadDAO.getStatusCodebySeqAndDocType(
																		currSeqDocLifeCycleMstList.get(0)
																				.getCancelSeq(),
																		idAndTenantIdReq.getTenantId(), "DC044"),
																idAndTenantIdReq.getTenantId()));
							}

							mainList.get(i).setDocLifeCycleMstList(docLifeCycleMstList);
						}
					} else {
						// Previous Seq
						docList.setCurrSequence(currSeq);
						docList.setDocStatus(indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
								idAndTenantIdReq.getTenantId(), "DC044"));

						docList.setDocStatusDesc(
								designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
												idAndTenantIdReq.getTenantId(), "DC044"),
										idAndTenantIdReq.getTenantId()));
						// cancel seq
						if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
							docList.setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
							docList.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
									currSeqDocLifeCycleMstList.get(0).getCancelSeq(), idAndTenantIdReq.getTenantId(),
									"DC044"));

							docList.setCancelStatusDesc(
									designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
													idAndTenantIdReq.getTenantId(), "DC044"),
											idAndTenantIdReq.getTenantId()));
						}
						docLifeCycleMstList.add(docList);
						mainList.get(i).setDocLifeCycleMstList(docLifeCycleMstList);
					}

					mainList.get(i).setPoDtl(poDtl);
					mainList.get(i).setPoDispatchDoc(poDispatchDoc);
					mainList.get(i).setPoPaymentTerm(poPaymentTerm);
					mainList.get(i).setPoStatusList(poStatusList);
					mainList.get(i)
							.setIsEditable(currSeqDocLifeCycleMstList.get(0).getIsEditable() != null
									&& currSeqDocLifeCycleMstList.get(0).getIsEditable() != ""
											? currSeqDocLifeCycleMstList.get(0).getIsEditable()
											: "0");
				}

			}
			if (preRevisionList.size() > 0) {

//				preRevisionList = poDAO.getPreRevisionPoDtls(mainList.get(0).getIgScpId(), mainList.get(0).getRevision(), mainList.get(0).getTenantId());
				for (int i = 0; i < preRevisionList.size(); i++) {
					List<PoDescMstEntity> divisionDesc = poDAO.getdivisionDescById(idAndTenantIdReq.getTenantId(),
							preRevisionList.get(i).getDivision());
					List<PoDescMstEntity> transitInsuranceDesc = poDAO.getransitInsuranceDescById(
							idAndTenantIdReq.getTenantId(), preRevisionList.get(i).getTransitInsurance());
					List<PoDescMstEntity> dispatchModeDesc = poDAO.getModeOfDispatchDescById(
							idAndTenantIdReq.getTenantId(), preRevisionList.get(i).getDispatchMode());
					List<PoDescMstEntity> inspectionScopeDesc = poDAO.getInspectScopeDescById(
							idAndTenantIdReq.getTenantId(), preRevisionList.get(i).getInspectionScope());
					if (divisionDesc.size() > 0) {
						preRevisionList.get(i).setDivisionDesc(divisionDesc.get(0).getDesc());

					}
					if (transitInsuranceDesc.size() > 0) {
						preRevisionList.get(i).setTransitInsuranceDesc(transitInsuranceDesc.get(0).getDesc());

					}
					if (dispatchModeDesc.size() > 0) {
						preRevisionList.get(i).setDispatchModeDesc(dispatchModeDesc.get(0).getDesc());

					}
					if (inspectionScopeDesc.size() > 0) {
						preRevisionList.get(i).setInspectionScopeDesc(inspectionScopeDesc.get(0).getDesc());

					}
					poDtl = iPoDAO.getPoDtlList(preRevisionList.get(i).getPoId());
					for (int j = 0; j < poDtl.size(); j++) {
						poDtl.get(j).setQtyInspectReqCount(
								Integer.toString(iPoDAO.qtyInspectReqCount(poDtl.get(j).getPoDtlId())));
						indentDtlList = indentUploadDAO.getIndentDtlsByIndentDtlId(poDtl.get(j).getIndentDtlId(),
								idAndTenantIdReq.getTenantId());
						for (int k = 0; k < indentDtlList.size(); k++) {
							int dmId = indentUploadDAO.getDmIdByLatestVerion(indentDtlList.get(k).getIndentDtlId(),
									idAndTenantIdReq.getTenantId());
							indentDtlList.get(k).setDmId(dmId);
						}
						String productCode = iPoDAO.getProdCodeByIndentDtlId(poDtl.get(j).getIndentDtlId());

//						String qcRequestedQty=iPoDAO.getQcRequestyQty(poDtl.get(j).getPoDtlId());
						BigDecimal qcRequestedQty = BigDecimal.ZERO;

						// get pending qty
						String pendingQty = getQcPendingQty(poDtl.get(j).getPoDtlId(), poDtl.get(j).getQty());

						poDtl.get(j).setPendingQty(String.valueOf(pendingQty));
						poDtl.get(j).setProductCode(productCode);
						poDtl.get(j).setIndentDtlList(indentDtlList);
						poDtl.get(j).setQcRequestedQty(String.valueOf(qcRequestedQty));

					}
					poPaymentTerm = iPoDAO.getPoPaymentTermList(preRevisionList.get(i).getPoId());
					poDispatchDoc = iPoDAO.getPoDispatchDocList(preRevisionList.get(i).getPoId());
					poStatusList = iPoDAO.getPoStatusList(preRevisionList.get(i).getPoId());
					String currSeq = preRevisionList.get(i).getSequenceNo();
					preRevisionList.get(i).setSeqStatusDesc(designTaskDAO.getStatusByDesc(
							preRevisionList.get(i).getSequenceStatus(), idAndTenantIdReq.getTenantId()));

					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq("DC044", currSeq,
							idAndTenantIdReq.getTenantId());
					docLifeCycleMstList = designTaskDAO.getNextSeqandStatus(Integer.parseInt(currSeq), "DC044",
							idAndTenantIdReq.getTenantId());

					if (docLifeCycleMstList.size() > 0) {

						String designCode = uploadManagementDAO.getDesigCodeByEmpId(idAndTenantIdReq.getEmpId(),
								idAndTenantIdReq.getTenantId());

						approveBtnEnable = indentUploadDAO.getApprovebtnEnable(designCode, "default",
								idAndTenantIdReq.getTenantId(), "DC044", docLifeCycleMstList.get(0).getCurrSequence());

						if (approveBtnEnable == 1) {
							// curr seq
							docLifeCycleMstList.get(0).setDocTypeDesc(indentUploadDAO.getDocTypeDescByDocType(
									docLifeCycleMstList.get(0).getDocType(), idAndTenantIdReq.getTenantId()));
							docLifeCycleMstList.get(0)
									.setDocStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													docLifeCycleMstList.get(0).getCurrSequence(),
													idAndTenantIdReq.getTenantId(), "DC044"),
											idAndTenantIdReq.getTenantId()));// Current seq docStatus

							// Previous Seq
							docLifeCycleMstList.get(0).setPreviousSeq(currSeq);
							docLifeCycleMstList.get(0).setPreviousSeqStatusCode(indentUploadDAO
									.getStatusCodebySeqAndDocType(currSeq, idAndTenantIdReq.getTenantId(), "DC044"));

							docLifeCycleMstList.get(0)
									.setPreviousSeqStatusDesc(designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
													idAndTenantIdReq.getTenantId(), "DC044"),
											idAndTenantIdReq.getTenantId()));
							// cancel seq
							if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
								docLifeCycleMstList.get(0)
										.setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
								docLifeCycleMstList.get(0)
										.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
												currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
												idAndTenantIdReq.getTenantId(), "DC044"));

								docLifeCycleMstList.get(0)
										.setCancelStatusDesc(
												designTaskDAO
														.getStatusByDesc(
																indentUploadDAO.getStatusCodebySeqAndDocType(
																		currSeqDocLifeCycleMstList.get(0)
																				.getCancelSeq(),
																		idAndTenantIdReq.getTenantId(), "DC044"),
																idAndTenantIdReq.getTenantId()));
							}

							preRevisionList.get(i).setDocLifeCycleMstList(docLifeCycleMstList);
						}
					} else {
						// Previous Seq
						docList.setCurrSequence(currSeq);
						docList.setDocStatus(indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
								idAndTenantIdReq.getTenantId(), "DC044"));

						docList.setDocStatusDesc(
								designTaskDAO.getStatusByDesc(
										indentUploadDAO.getStatusCodebySeqAndDocType(currSeq,
												idAndTenantIdReq.getTenantId(), "DC044"),
										idAndTenantIdReq.getTenantId()));
						// cancel seq
						if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
							docList.setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
							docList.setCancelStatusCode(indentUploadDAO.getStatusCodebySeqAndDocType(
									currSeqDocLifeCycleMstList.get(0).getCancelSeq(), idAndTenantIdReq.getTenantId(),
									"DC044"));

							docList.setCancelStatusDesc(
									designTaskDAO.getStatusByDesc(
											indentUploadDAO.getStatusCodebySeqAndDocType(
													currSeqDocLifeCycleMstList.get(0).getCancelSeq(),
													idAndTenantIdReq.getTenantId(), "DC044"),
											idAndTenantIdReq.getTenantId()));
						}
						docLifeCycleMstList.add(docList);
						preRevisionList.get(i).setDocLifeCycleMstList(docLifeCycleMstList);
					}

					preRevisionList.get(i).setPoDtl(poDtl);
					preRevisionList.get(i).setPoDispatchDoc(poDispatchDoc);
					preRevisionList.get(i).setPoPaymentTerm(poPaymentTerm);
					preRevisionList.get(i).setPoStatusList(poStatusList);
					preRevisionList.get(i)
							.setIsEditable(currSeqDocLifeCycleMstList.get(0).getIsEditable() != null
									&& currSeqDocLifeCycleMstList.get(0).getIsEditable() != ""
											? currSeqDocLifeCycleMstList.get(0).getIsEditable()
											: "0");
				}
			}

			mainList.addAll(preRevisionList);

			if (mainList.size() > 0) {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getPoDtlsByPoId error---> " + ex);
		}
		return returnList;
	}

	private String getPendingTransportChrg(String poId){
		return iPoDAO.getPendingTransportChrg(poId);
	}

	private String getQcPendingQty(String poDtlId, String poQty) {

		BigDecimal qcRequestedQty = BigDecimal.ZERO;
		BigDecimal qcOkQty = BigDecimal.ZERO;
		BigDecimal qcRaisedRequestedQty = BigDecimal.ZERO;
		BigDecimal qcRaisedRequestQty = BigDecimal.ZERO;
		BigDecimal waitingForQc = BigDecimal.ZERO;
		BigDecimal pendingQty = BigDecimal.ZERO;
		List<RetrieveQualitInspectionEntity> qcReqList = new ArrayList<RetrieveQualitInspectionEntity>();

		qcReqList = iPoDAO.getQcReqDetails(poDtlId, "%%");
		
		if (qcReqList.size() > 0) {
			int qiHdrId = 0; String MiOkQty="0";
			int caApprovedStatus = 0;
			for (int h = 0; h < qcReqList.size(); h++) {
				String waitingQcQty = iPoDAO.getQcForwaitingStatus(qcReqList.get(h).getPoDtlId(),qcReqList.get(h).getQiId());
				// check whether qc Inspection is Raised or not
				int qcRaised = iPoDAO.checkQcIsRaised(qcReqList.get(h).getQiId());
				if (qcRaised > 0) {

					// check whether qc Inspection is completed
					qiHdrId = iPoDAO.getQcIsCompletedStatus(qcReqList.get(h).getQiId());
					if (qiHdrId > 0) {
						 MiOkQty = iPoDAO.getMiOkDetails(qcReqList.get(h).getPoDtlId());
						// check whether CA raised or not
						int caRaisedCnt = iPoDAO.getCARaisedCount(String.valueOf(qiHdrId));
						if (caRaisedCnt > 0) {

							// check whether CA approved or not
							caApprovedStatus = iPoDAO.getCaApprovedStatus(String.valueOf(qiHdrId));
							if (caApprovedStatus == 0) {
								qcRequestedQty = qcRequestedQty
										.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
							} else {
								qcOkQty = qcOkQty.add(new BigDecimal(iPoDAO.getQcOkQty(String.valueOf(qiHdrId))));
							}
						} else {
							qcOkQty = qcOkQty.add(new BigDecimal(iPoDAO.getQcOkQty(String.valueOf(qiHdrId))));
						}
					} else {
						qcRequestedQty = qcRequestedQty.add(new BigDecimal(waitingQcQty));
					}
				} else {
					qcRaisedRequestedQty = qcRaisedRequestedQty
							.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));
					if (qcReqList.get(h).getCancelFlag() == null) {
						qcRaisedRequestQty = qcRaisedRequestQty.add(new BigDecimal(qcReqList.get(h).getQtyToBeInspected()));	
					}else {
					    qcRaisedRequestedQty = qcRaisedRequestedQty.add(new BigDecimal(waitingQcQty));
					}
				}
				if (qcReqList.get(h).getCancelFlag() != null
						&& !qcReqList.get(h).getCancelFlag().equalsIgnoreCase("1")) {
					waitingForQc = new BigDecimal(waitingQcQty)
							.subtract(new BigDecimal(qcReqList.get(h).getInspectQty()));
					if (qiHdrId > 0) {
						if (caApprovedStatus == 0) {

//							if(!qcReqList.get(h).getReworkCount().equalsIgnoreCase("0.00") || qcReqList.get(h).getIsRework() > 0) {
//								pendingQty = new BigDecimal(poQty).subtract(qcOkQty).subtract(new BigDecimal(MiOkQty));
//							}else {
//								pendingQty = new BigDecimal(poQty).subtract(qcOkQty).subtract(waitingForQc).subtract(new BigDecimal(MiOkQty));
//							}
						
							pendingQty = new BigDecimal(poQty).subtract(qcOkQty).subtract(qcRequestedQty).subtract(new BigDecimal(MiOkQty));
//									.subtract(new BigDecimal(qcReqList.get(h).getInspectQty())).subtract(new BigDecimal(MiOkQty));
						} else {
							pendingQty = new BigDecimal(poQty).subtract(qcOkQty).subtract(new BigDecimal(MiOkQty));
						}
					} else {
						pendingQty = new BigDecimal(poQty).subtract(qcOkQty).subtract(waitingForQc).subtract(new BigDecimal(MiOkQty));
					}
				} else if (qcReqList.get(h).getCancelFlag() == null) {
					pendingQty = new BigDecimal(poQty).subtract(qcRaisedRequestQty).subtract(qcOkQty).subtract(new BigDecimal(MiOkQty));
				} else {
					pendingQty = qcRequestedQty.add(new BigDecimal(poQty).subtract(qcRequestedQty)).subtract(qcOkQty).subtract(qcRaisedRequestQty).subtract(new BigDecimal(MiOkQty));
				}
			}
		} else {
			pendingQty = new BigDecimal(poQty).subtract(qcRequestedQty).subtract(qcOkQty);
		}
//		 pendingQty=new BigDecimal(poQty).subtract(qcRequestedQty).subtract(qcOkQty);
		return String.valueOf(pendingQty);
	}

	@Override
	public ResponseAsMessage insertPoHdrDtl(GetPoDtlsEntity insertPoDtlsEntity) {
		List<BillingDetailEntity> getBilllingDetails = new ArrayList<BillingDetailEntity>();
		List<LocationMstEntity> getVendorLocDtls = new ArrayList<LocationMstEntity>();
		List<VendorMstEntity> getVendorMstDtls = new ArrayList<VendorMstEntity>();
		ResponseAsMessage rmsg = new ResponseAsMessage();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		List<PoPaymentTermEntity> poPaymentTermEntities = insertPoDtlsEntity.getPoPaymentTerm();
		int updatePoHdrId = 0, poDispatchDocId = 0, poPaymentTermId = 0, poDtlId = 0, poId = 0;
		String poHdrId = "";
		String seqNo = "1";
		try {

			if (null != insertPoDtlsEntity) {
				
				int praCheck = 0;
				if (insertPoDtlsEntity.getPoId() != null && !insertPoDtlsEntity.getPoId().isEmpty()) {
				    praCheck = iPoDAO.praCheckByPoId(insertPoDtlsEntity.getPoId());
				}

				else if (insertPoDtlsEntity.getPoPaymentTerm() != null && !insertPoDtlsEntity.getPoPaymentTerm().isEmpty()) {
				    for (PoPaymentTermEntity paymentTerm : insertPoDtlsEntity.getPoPaymentTerm()) {
				        if ("1".equalsIgnoreCase(paymentTerm.getIsLast())) {
				            praCheck = iPoDAO.praCheckByPotId(paymentTerm.getPotId());
				            break;
				        }
				    }
				}

				if (praCheck > 0 && (insertPoDtlsEntity.getDocLifeCycleMstList().get(0).getCurrSequence().equals("3") ||
						insertPoDtlsEntity.getDocLifeCycleMstList().get(0).getCurrSequence().equals("4"))) {
				    rmsg.setResponseCode(ResponseMessageMap.failToupdateCode);
				    rmsg.setResponseDataMessage("Failure");
				    rmsg.setResponseMessage("PRA already exists for this PO");
				    return rmsg;
				}


				if(insertPoDtlsEntity.getIsGstChanged()!=null && insertPoDtlsEntity.getIsGstChanged().equalsIgnoreCase("true")){
					int indexOfLastPoPaymentTerm = -1;
					PoPaymentTermEntity lastPoPaymentTerm = null;

					for (int i = 0; i < poPaymentTermEntities.size(); i++) {
						PoPaymentTermEntity poPaymentTerm = poPaymentTermEntities.get(i);
						if ("1".equalsIgnoreCase(poPaymentTerm.getIsLast())) {
							lastPoPaymentTerm = poPaymentTerm;
							indexOfLastPoPaymentTerm = i;
							break; // Break the loop after finding the last term
						}
					}
					int pracheck = 0;
					pracheck = iPoDAO.praCheckByPotId(lastPoPaymentTerm.getPotId());
					if(pracheck>0) {
						rmsg.setResponseCode(ResponseMessageMap.failToupdateCode);
						rmsg.setResponseDataMessage("Failure");
						rmsg.setResponseMessage(ResponseMessageMap.poGstCantCreated);

						return rmsg;
					}
					else {
//						BigDecimal oldGst = new BigDecimal(insertPoDtlsEntity.getOldGst());
						BigDecimal newGst = new BigDecimal(insertPoDtlsEntity.getGst());
						BigDecimal potPercentage = new BigDecimal(lastPoPaymentTerm.getPercentage());
						BigDecimal basicTotal = new BigDecimal(insertPoDtlsEntity.getBasicTotal());

						// Calculate subtotal for this term: (percentage / 100) * basicTotal
						BigDecimal percentageDecimal = potPercentage.divide(BigDecimal.valueOf(100));
						BigDecimal termSubtotal = basicTotal.multiply(percentageDecimal);

						// Add new GST to this termSubtotal
						BigDecimal finalTermValue = termSubtotal.add(newGst);

						// Set updated values
						lastPoPaymentTerm.setPaymentAmount(finalTermValue.toString());
						lastPoPaymentTerm.setPendingAmount(finalTermValue.toString());

						insertPoDtlsEntity.getPoPaymentTerm().set(indexOfLastPoPaymentTerm, lastPoPaymentTerm);
					}

				}
				if (insertPoDtlsEntity.getPoId() == null || insertPoDtlsEntity.getPoId().equalsIgnoreCase("")) {

					String seqStatus = indentUploadDAO.getStatusCodebySeqAndDocType(seqNo,
							insertPoDtlsEntity.getTenantId(), "DC044");
					insertPoDtlsEntity.setSequenceStatus(seqStatus);

//					String poType = iPoDAO.getPoType(insertPoDtlsEntity.getVendorCode());
					insertPoDtlsEntity.setPoType("0");

					String indentId = iPoDAO.getIndentId(insertPoDtlsEntity.getIgScpId());
					insertPoDtlsEntity.setIndentID(indentId);

					getBilllingDetails = iPoDAO.getBillingDetails(insertPoDtlsEntity.getTenantId());
					insertPoDtlsEntity.setBillingAddressLine(getBilllingDetails.get(0).getLocAddressLine());
					insertPoDtlsEntity.setBillingCity(getBilllingDetails.get(0).getLocCity());
					insertPoDtlsEntity.setBillingName(getBilllingDetails.get(0).getOrgName());
					insertPoDtlsEntity.setBillingPincode(getBilllingDetails.get(0).getLocPinCode());
					insertPoDtlsEntity.setBillingState(getBilllingDetails.get(0).getLocState());
					insertPoDtlsEntity.setBillingContactNo(getBilllingDetails.get(0).getContactNo());
					insertPoDtlsEntity.setBillingGst(getBilllingDetails.get(0).getGstNo());
					getVendorMstDtls = iPoDAO.getVendorMstDtls(insertPoDtlsEntity.getVendorCode(),
							insertPoDtlsEntity.getTenantId());
					insertPoDtlsEntity.setVendorName(getVendorMstDtls.get(0).getVendorName());
					insertPoDtlsEntity.setVendorGst(getVendorMstDtls.get(0).getGst());

					getVendorLocDtls = iPoDAO.getVendorLocDtls(getVendorMstDtls.get(0).getLocationId(),
							getVendorMstDtls.get(0).getTenantId());
					insertPoDtlsEntity.setVendorCity(getVendorLocDtls.get(0).getLocCity());
					insertPoDtlsEntity.setVendorAddressLine(getVendorLocDtls.get(0).getLocAddressLine());
					insertPoDtlsEntity.setVendorState(getVendorLocDtls.get(0).getLocState());
					insertPoDtlsEntity.setVendorPincode(getVendorLocDtls.get(0).getLocPinCode());
					insertPoDtlsEntity.setVendorContactNo(getVendorMstDtls.get(0).getContactNo());
					poId = iPoDAO.insertPoHdrDtl(insertPoDtlsEntity);
					poHdrId = String.valueOf(poId);
					// PO status table insert
					if (poId > 0) {
						String createdBy = iPoDAO.getPJSCreatedBY(insertPoDtlsEntity.getIgScpId());
						if(createdBy.equalsIgnoreCase("")) {
							createdBy =insertPoDtlsEntity.getEmpId();
						}
						iPoDAO.insertPoStatusDtl(poHdrId, seqNo, seqStatus, insertPoDtlsEntity.getTenantId(),
								insertPoDtlsEntity.getRemarks(), createdBy);
						String projectId=insertPoDtlsEntity.getProjectId();
						messageList.add(grnDAO.getPoCodeByPoId(poHdrId));
						String nextApprDesig = commonNotifyMethod.getNxtAppDesc("DC044", "1",insertPoDtlsEntity.getTenantId());
						commonNotifyMethod.InvokeNotificationMethod(1,46, null, insertPoDtlsEntity.getTenantId(), messageList, otherEmpId, "0", insertPoDtlsEntity.getPmId(), insertPoDtlsEntity.getMasterId(), nextApprDesig);
						commonNotifyMethod.InvokeApprovalDesigMethod(insertPoDtlsEntity.getPmId(), "DC044", poHdrId,projectId, insertPoDtlsEntity.getTenantId(), "",nextApprDesig, indentUploadDAO.getEnqIdByProjectId(projectId),grnDAO.getPoCodeByPoId(poHdrId));
					}
				} else {

//					int revision = iPoDAO.getLatestRevByPoId(insertPoDtlsEntity.getPoId());
//					insertPoDtlsEntity.setRevision(String.valueOf(revision + 1));
					updatePoHdrId = iPoDAO.updatePoHdrDtl(insertPoDtlsEntity);
					poHdrId = insertPoDtlsEntity.getPoId();
				}

				if (poId > 0 || updatePoHdrId > 0) {
					// PoDispatchDoc table insert
					for (int j = 0; j < insertPoDtlsEntity.getPoDispatchDoc().size(); j++) {
						poDispatchDocId = iPoDAO.insertpoDispatchDocDtl(insertPoDtlsEntity.getPoDispatchDoc().get(j),
								poHdrId);
					}

					// deletedDataforPerticularIds
					int getPtCount = iPoDAO.getPtDtlCount(poHdrId);
					if (getPtCount > 0) {
						iPoDAO.removePoHdrIdBased(poHdrId);
					}
					// PoPaymentTerm table insert
					int flag = 0;
					for (int k = 0; k < insertPoDtlsEntity.getPoPaymentTerm().size(); k++) {
						flag = insertPoDtlsEntity.getPoPaymentTerm().size() - 1 == k ? 1 : 0;
						poPaymentTermId = iPoDAO.insertpoPaymentTermDtl(insertPoDtlsEntity.getPoPaymentTerm().get(k),
								poHdrId, flag);
					}
					//if - >
					// PoDtl table insert
					for (int i = 0; i < insertPoDtlsEntity.getPoDtl().size(); i++) {
						poDtlId = iPoDAO.insertpoDtl(insertPoDtlsEntity.getPoDtl().get(i), poHdrId);
					}

					// if(cancelled PO -> ole pt payable - pending
					// check if PO created before is cancelled...
					if (insertPoDtlsEntity.getDocLifeCycleMstList() == null) {
					int cancelledPo = indentGroupDAO.checkForCancelledPo(insertPoDtlsEntity.getIgScpId(), insertPoDtlsEntity.getTenantId());
					List<PoPaymentTermEntity> oldPoPaymentTerm = new ArrayList<>();
					List<PoPaymentTermEntity> newPoPaymentTerm = iPoDAO.getPoPaymentTermList(poHdrId);
					String cancelPoId = "";
					if (cancelledPo == 1) {
						cancelPoId = iPoDAO.getPoIdByIgscsIdForCancelledPo(insertPoDtlsEntity.getIgScpId(), insertPoDtlsEntity.getTenantId());
						oldPoPaymentTerm = iPoDAO.getPoPaymentTermList(cancelPoId);
					}
					List<Double> diffList = oldPoPaymentTerm.stream()
							.map(term -> {
								double payment = Double.parseDouble(term.getPaymentAmount() == null ? "0" : term.getPaymentAmount());
								double pending = Double.parseDouble(term.getPendingAmount() == null ? "0" : term.getPendingAmount());
								return payment - pending;
							})
							.collect(Collectors.toList());

					for (int i = 0; i < newPoPaymentTerm.size(); i++) {
						String paidAmountStr = String.valueOf(diffList.get(i));
						double paidAmount = Double.parseDouble(paidAmountStr);

						String pendingAmountStr = newPoPaymentTerm.get(i).getPendingAmount();
						double pendingAmount = Double.parseDouble(pendingAmountStr == null ? "0" : pendingAmountStr);

						double updatedAmount = pendingAmount - paidAmount;
						String updatedAmountStr = String.valueOf(updatedAmount);

						String tenantId = insertPoDtlsEntity.getTenantId();
						iPoDAO.updatePoPaymentTerm(newPoPaymentTerm.get(i).getPotId(), updatedAmountStr, tenantId);
					}

					int praCount = iPoDAO.praCheckForCancelledPo(cancelPoId);
					if (praCount > 0) {
						praDAO.updPraAfterPoCancel(cancelPoId, poHdrId, insertPoDtlsEntity.getTenantId());
					}
				}

				}

			}
			if(!insertPoDtlsEntity.getPoType().equalsIgnoreCase("2")) {
				if (poDispatchDocId > 0 && poPaymentTermId == 1 && poDtlId == 1) {
					rmsg.setResponseCode(ResponseMessageMap.responseCodeOk);
					rmsg.setResponseDataMessage(ResponseMessageMap.successMsg);
					rmsg.setResponseMessage(ResponseMessageMap.successCreated);
				} else {
					rmsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					rmsg.setResponseDataMessage(ResponseMessageMap.failMsg);
					rmsg.setResponseMessage(ResponseMessageMap.failToCreateMsg);
				}
			}else {
				if (poDispatchDocId > 0 && poDtlId == 1) {
					rmsg.setResponseCode(ResponseMessageMap.responseCodeOk);
					rmsg.setResponseDataMessage(ResponseMessageMap.successMsg);
					rmsg.setResponseMessage(ResponseMessageMap.successCreated);
				} else {
					rmsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					rmsg.setResponseDataMessage(ResponseMessageMap.failMsg);
					rmsg.setResponseMessage(ResponseMessageMap.failToCreateMsg);
				}
			}


		} catch (Exception ex) {
			logger.error("insertPoHdrDtl Method Exception --->" + ex);
			rmsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			rmsg.setResponseDataMessage(ResponseMessageMap.failMsg);
			rmsg.setResponseMessage("PO creation failed: " + ex.getMessage());
		}
		return rmsg;
	}

	@Override
	public ResponseAsMessage updatePoSeqAndStatus(UpdateSeqAndStatusRequest updatePoDtls) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstLists = new ArrayList<DocumentStatusMstEntity>();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		int updatePoHdr = 0;
		String isLatest = "", isApproved = "0";
		try {
			int insertPoStatusDtl = 0;
			String docType = "DC044";
			String poId = updatePoDtls.getHdrId();
			
			int praCheck = iPoDAO.praIsAvilableCheck(poId);
			if (praCheck > 0) {
			    returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
			    returnMessage.setResponseDataMessage("Failure");
			    returnMessage.setResponseMessage("PRA has already been created for this PO");
			    return returnMessage;
			}
			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeq(docType, updatePoDtls.getCurrentseq(),
					updatePoDtls.getTenantId());
			messageList.add(grnDAO.getPoCodeByPoId(updatePoDtls.getHdrId()));
			String design = iPoDAO.getAppDesig(docType, updatePoDtls.getCurrentseq(), updatePoDtls.getTenantId());
			int materialInwardCheck = 0;
			int pracheck = 0;
			String nextApprDesig = commonNotifyMethod.getNxtAppDesc(docType, updatePoDtls.getCurrentseq(),
					updatePoDtls.getTenantId());
			String projectId = iPoDAO.getPmHdrIdByPoId(poId, updatePoDtls.getTenantId());
			// poCancel check
			if (updatePoDtls.getCurrentseq().equalsIgnoreCase("3")) {
				isLatest = "0";
				materialInwardCheck = iPoDAO.materialInwardCheck(poId);
				if (materialInwardCheck > 0) {
					returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
					returnMessage.setResponseDataMessage("Failure");
					returnMessage.setResponseMessage(ResponseMessageMap.poMICreated);

					return returnMessage;
				}
				pracheck = iPoDAO.praCheck(poId);
				if (pracheck > 0) {
					returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
					returnMessage.setResponseDataMessage("Failure");
					returnMessage.setResponseMessage(ResponseMessageMap.poPRACreated);

					return returnMessage;
				}
				// update seq ,status & isApproved in SCS
				indentGroupDAO.updateScpSeqAndStatus(iPoDAO.getIgScsIdByPoId(poId), "1", "DS069",
						updatePoDtls.getEmpId());
				indentGroupDAO.updateScpApproved(iPoDAO.getIgScsIdByPoId(poId), "0");

				commonNotifyMethod.InvokeNotificationMethod(2, 48, "", updatePoDtls.getTenantId(), messageList,
						otherEmpId, "0", updatePoDtls.getPmId(), updatePoDtls.getMstId(), design);
				commonNotifyMethod.InvokeApprovalDesigMethod(updatePoDtls.getPmId(), docType, poId, projectId,
						updatePoDtls.getTenantId(), "", nextApprDesig, indentUploadDAO.getEnqIdByProjectId(projectId),
						grnDAO.getPoCodeByPoId(poId));

			} else {
				isLatest = "1";
				// check for last seq
				if (currSeqDocLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
					isApproved = "1";

					List<GetProductUnitCostEntity> getProductUnitCostList = iPoDAO
							.getProductUnitCostList(updatePoDtls.getHdrId(), updatePoDtls.getTenantId());

					for (int q = 0; q < getProductUnitCostList.size(); q++) {
						iPoDAO.updateproductMstUnitCost(getProductUnitCostList.get(q).getProductId(),
								getProductUnitCostList.get(q).getUnitCost());
					}
					commonNotifyMethod.InvokeNotificationMethod(2, 47, "", updatePoDtls.getTenantId(), messageList,
							otherEmpId, "0", updatePoDtls.getPmId(), updatePoDtls.getMstId(), design);
					commonNotifyMethod.InvokeApprovalDesigMethod(updatePoDtls.getPmId(), docType,
							updatePoDtls.getHdrId(), projectId, updatePoDtls.getTenantId(), "", "",
							indentUploadDAO.getEnqIdByProjectId(projectId),
							grnDAO.getPoCodeByPoId(updatePoDtls.getHdrId()));

				}

			}

			// Update PoHdr'
			if (materialInwardCheck == 0) {
				updatePoHdr = iPoDAO.updatePoSeqAndStatus(poId, updatePoDtls.getCurrentseq(),
						currSeqDocLifeCycleMstList.get(0).getDocStatus(), isLatest, isApproved,
						updatePoDtls.getEmpId());
			}
			if (updatePoHdr > 0) {
				// Update IntentHdr

				int getIntentId = iPoDAO.retriveIndentId(poId);
				int isInventoryCount = iPoDAO.getIsInventoryCount(Integer.toString(getIntentId),
						updatePoDtls.getTenantId());
				int indentCloseCount = iPoDAO.getIndentCloseStatus(Integer.toString(getIntentId),
						updatePoDtls.getTenantId());
				int checkCount = isInventoryCount - indentCloseCount;
				if (checkCount == 0) {
					String currSeq = iPoDAO.getTenantPropertyVal(updatePoDtls.getTenantId(), "INDENT_CLOSE_SEQ");
					currSeqDocLifeCycleMstLists = stageManagementDAO.getDocDtlcurrentSeq("DC018", currSeq,
							updatePoDtls.getTenantId());

					intentDetailsUpdate(poId, currSeqDocLifeCycleMstLists.get(0).getCurrSequence(),
							currSeqDocLifeCycleMstLists.get(0).getDocStatus(), updatePoDtls.getTenantId(),
							updatePoDtls.getPmId(), updatePoDtls.getMstId(), "1");

					iPoDAO.updateindentClose(Integer.toString(getIntentId));
				}
//				else {
////					String currSeq = iPoDAO.getTenantPropertyVal(updatePoDtls.getTenantId(), "INDENT_CLOSE_SEQ");
//					currSeqDocLifeCycleMstLists = stageManagementDAO.getDocDtlcurrentSeq("DC018",
//							updatePoDtls.getCurrentseq(), updatePoDtls.getTenantId());
//					
//					intentDetailsUpdate(poId, currSeqDocLifeCycleMstLists.get(0).getCurrSequence(),
//				currSeqDocLifeCycleMstLists.get(0).getDocStatus(),updatePoDtls.getTenantId(),updatePoDtls.getPmId(),updatePoDtls.getMstId(),"0");
//				}
			}

			if (updatePoHdr == 1) {

				insertPoStatusDtl = iPoDAO.insertPoStatusDtl(poId, updatePoDtls.getCurrentseq(),
						currSeqDocLifeCycleMstList.get(0).getDocStatus(), updatePoDtls.getTenantId(),
						updatePoDtls.getRemarks(), updatePoDtls.getEmpId());

				// Update PO last updated datetime in indent_Dtl table
				List<IndentDtlTblEntity> list = new ArrayList<IndentDtlTblEntity>();
				list = indentGroupDAO.getIndentDtlsByPoId(poId, updatePoDtls.getTenantId());
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String poLastUpdatedDate = indentGroupDAO.getLastUpdatedDateTime(list.get(i).getIndentDtlId(),
								updatePoDtls.getTenantId(), "PO");
						if (!poLastUpdatedDate.equalsIgnoreCase("")) {
							indentGroupDAO.updateLastUpdatedDateTime(list.get(i).getIndentDtlId(),
									"PO_COMPLETED_DATETIME", poLastUpdatedDate, updatePoDtls.getTenantId());
						} else if (updatePoDtls.getCurrentseq().equalsIgnoreCase("3")) {
							indentGroupDAO.reUpdatedDateTimeIndentDtl(list.get(i).getIndentDtlId(),
									"PO_COMPLETED_DATETIME", updatePoDtls.getTenantId());
							indentGroupDAO.reUpdatedDateTimeIndentDtl(list.get(i).getIndentDtlId(),
									"PJS_COMPLETED_DATETIME", updatePoDtls.getTenantId());
						}
					}
				}

				if (insertPoStatusDtl > 0) {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage("Success");
					returnMessage.setResponseMessage(ResponseMessageMap.approved);
				}
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updatePoDtlService error " + ex);
		}
		return returnMessage;
	}

	public int intentDetailsUpdate(String poId, String seq, String status, String tenantId, String pmId,
			String masterId, String isCompleted) {
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		int updateIndentHdr = 0;
		int getIntentId = iPoDAO.retriveIndentId(poId);

		if (getIntentId > 0) {

			updateIndentHdr = iPoDAO.updateIntentHdrSeqAndStatus(poId, seq, status, getIntentId, isCompleted);
			if (updateIndentHdr == 1) {
				otherEmpId = iPoDAO.getEmpListByIndentId(String.valueOf(getIntentId));
				messageList.add(indentUploadDAO.getIndentCodeByIndentId(String.valueOf(getIntentId)));
				commonNotifyMethod.InvokeNotificationMethod(2, 45, null, tenantId, messageList, otherEmpId, "0", pmId,
						masterId, null);
			}
		}
		return updateIndentHdr;
	}

	@Override
	public ResponseAsList getPoDtlsByDateAndPoId(IndentGrpDtlRequest indentGrpDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetPoDtlsByDate> poList = new ArrayList<GetPoDtlsByDate>();
		logger.info("getPoDtlsByDateAndPoId Service start ");
		try {
			poList = iPoDAO.getPoDtlsByDateAndPoId(indentGrpDtlReq.getFromDate(), indentGrpDtlReq.getToDate(),
					indentGrpDtlReq.getProjectId(), indentGrpDtlReq.getTenantId());
			if (poList.size() > 0) {
				returnList.setResponseData(poList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(poList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getPoDtlsByDateAndPoId Service end ");
		} catch (Exception ex) {
			logger.error("getPoDtlsByDateAndPoId error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getDCTypeDtl() {
		ResponseAsList returnList = new ResponseAsList();
		List<GetDCTypeDtlEntity> getDCTypeDtl = new ArrayList<GetDCTypeDtlEntity>();
		logger.info("getDCTypeDtl Service start ");
		try {
			getDCTypeDtl = iPoDAO.getDctypeCode();
			if (getDCTypeDtl.size() > 0) {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getDCTypeDtl Service end ");
		} catch (Exception ex) {
			logger.error("getDCTypeDtl error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getDCProductDropDown(getDCProductDropDownRequest getDCProductDropDownreq) {
		ResponseAsList returnList = new ResponseAsList();
		List<ProductMstDropDownEntity> list = new ArrayList<ProductMstDropDownEntity>();
		logger.info("getDCProductDropDown Service start ");
		try {
			String name = iPoDAO.invLocType(getDCProductDropDownreq.getFromName(),
					getDCProductDropDownreq.getTenantId());
			list = iPoDAO.getDCProductDropDown(name, getDCProductDropDownreq.getPmHdrId(),
					getDCProductDropDownreq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getDCProductDropDown Service end ");
		} catch (Exception ex) {
			logger.error("getDCProductDropDown error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getpoInstoreDtlByPmId(GetpoInstoreDtlByPmIdRequest getpoInstoreDtlByPmIdRequest) {
		ResponseAsList returnList = new ResponseAsList();
		List<PoInstoreDtlEntity> getDCTypeDtl = new ArrayList<PoInstoreDtlEntity>();
		logger.info("getDCTypeDtl Service start ");
		try {
			getDCTypeDtl = iPoDAO.getpoInstoreDtlByPmId(getpoInstoreDtlByPmIdRequest.getPmHdrId(),
					getpoInstoreDtlByPmIdRequest.getTenantId(), getpoInstoreDtlByPmIdRequest.getIsFlag());
			if (getDCTypeDtl.size() > 0) {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getDCTypeDtl Service end ");
		} catch (Exception ex) {
			logger.error("getDCTypeDtl error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getAddressDtlByDcType(GetAddressDtlByDcTypeRequest getAddressDtlByDcTypeReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<AddressDtlByDcTypeEntity> getDCTypeDtl = new ArrayList<AddressDtlByDcTypeEntity>();
		logger.info("getDCTypeDtl Service start ");
		try {
			if (getAddressDtlByDcTypeReq.getDcTypeCode().equalsIgnoreCase("DCT001")) {
				getDCTypeDtl = iPoDAO.getOrganCode(getAddressDtlByDcTypeReq.getDcTypeCode(),
						getAddressDtlByDcTypeReq.getTenantId());
			} else if (getAddressDtlByDcTypeReq.getDcTypeCode().equalsIgnoreCase("DCT002")) {
				getDCTypeDtl = iPoDAO.getVendorCode(getAddressDtlByDcTypeReq.getDcTypeCode(),
						getAddressDtlByDcTypeReq.getTenantId());
			} else if (getAddressDtlByDcTypeReq.getDcTypeCode().equalsIgnoreCase("DCT003")) {
				getDCTypeDtl = iPoDAO.getCustomerCode(getAddressDtlByDcTypeReq.getDcTypeCode(),
						getAddressDtlByDcTypeReq.getTenantId());
			}
			if (getDCTypeDtl.size() > 0) {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getDCTypeDtl Service end ");
		} catch (Exception ex) {
			logger.error("getDCTypeDtl error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getAllDcHdrByPmId(PmHdrIdAndTenantIdRequest pmIdAndTenantId) {
		ResponseAsList returnList = new ResponseAsList();
		List<DcHdrEntity> dcHdrList = new ArrayList<DcHdrEntity>();
		logger.info("getAllDcHdrByPmId Service start ");
		try {
			dcHdrList = iPoDAO.getAllDcHdrByPmId(pmIdAndTenantId.getPmHdrId(), pmIdAndTenantId.getTenantId(),pmIdAndTenantId.getGetRetrunable());

			for (int i = 0; i < dcHdrList.size(); i++) {
				int dcDtlCount = iPoDAO.getCountDtlByDcId(dcHdrList.get(i).getDcID(), dcHdrList.get(i).getTenantId());
				dcHdrList.get(i).setDcDtlCount(Integer.toString(dcDtlCount));
			}
			if (dcHdrList.size() > 0) {
				returnList.setResponseData(dcHdrList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(dcHdrList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getAllDcHdrByPmId Service end ");
		} catch (Exception ex) {
			logger.error("getAllDcHdrByPmId error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getDcDtlByDcId(GetDcDtlByDcIdRequest getDcDtlByDcIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<DcHdrEntity> dcHdrList = new ArrayList<DcHdrEntity>();
		logger.info("getDCTypeDtl Service start ");
		try {
			dcHdrList = iPoDAO.getDcHdrDtlById(getDcDtlByDcIdReq.getDcHdrId(), getDcDtlByDcIdReq.getTenantId());
			for (int i = 0; i < dcHdrList.size(); i++) {

				if (dcHdrList.get(i).getPoDtlId() != null) {
					dcHdrList.get(i).setPoId(iPoDAO.getpoIdByPoDtlId(dcHdrList.get(i).getPoDtlId()));
				} else {
					dcHdrList.get(i).setPoId(null);
				}
				List<DcDtlEntity> dcDtlList = iPoDAO.getDtlByDcId(dcHdrList.get(i).getDcID(),
						dcHdrList.get(i).getTenantId(), dcHdrList.get(i).getPmHdrId());
				for (DcDtlEntity dcDtl : dcDtlList) {
					String binValue = iPoDAO.getBinValue(dcDtl.getDcId()); // use existing DAO method
					dcDtl.setBin(binValue); // set BIN into entity
				}
				dcHdrList.get(i).setDcDtlCount(Integer.toString(dcDtlList.size()));
				dcHdrList.get(i).setDcDtlList(dcDtlList);
			}

			if (dcHdrList.size() > 0) {
				returnList.setResponseData(dcHdrList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(dcHdrList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getDcDtlByDcId Service end ");
		} catch (Exception ex) {
			logger.error("getDcDtlByDcId error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage insertDcDtl(DcHdrEntity dcHdrReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		int val = 0;
		try {
			if (dcHdrReq.getDcID().equalsIgnoreCase("")) {
				val = iPoDAO.insertDcHdr(dcHdrReq);
			}
			if (val > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage(ResponseMessageMap.successCreated);
				message.setResponseMessage(Integer.toString(val));
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
				message.setResponseMessage(Integer.toString(val));
			}

		} catch (Exception ex) {
			logger.error("insertDcDtl Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage cancelDcHdr(GetDcDtlByDcIdRequest getDcDtlByDcIdReq) {
		ResponseAsMessage message = new ResponseAsMessage();
		int val = 0;
		try {

			val = iPoDAO.cancelDcHdr(getDcDtlByDcIdReq);
			if (val > 0) {
				List<DcHdrEntity> dchdrList = iPoDAO.getDcHdrDtlById(getDcDtlByDcIdReq.getDcHdrId(),
						getDcDtlByDcIdReq.getTenantId());
				List<DcDtlEntity> dcDtlList = iPoDAO.getDtlByDcId(getDcDtlByDcIdReq.getDcHdrId(),
						getDcDtlByDcIdReq.getTenantId(),dchdrList.get(0).getPmHdrId());

				for (int i = 0; i < dcDtlList.size(); i++) {
					if (!dcDtlList.get(i).getProductId().equalsIgnoreCase("")) {
						String prodCode = poDAO.getProdCodeByprodId(dcDtlList.get(i).getProductId());
						iPoDAO.increaseIntPrdDtl(dcDtlList.get(i).getProductId(), dcDtlList.get(i).getQty(),
								dchdrList.get(0).getPmHdrId(), dchdrList.get(0).getDcCode(), prodCode,
								getDcDtlByDcIdReq.getEmpId(), getDcDtlByDcIdReq.getTenantId());
					}
				}
			}
			if (val > 0) {
				message.setResponseCode(ResponseMessageMap.responseCodeOk);
				message.setResponseDataMessage(ResponseMessageMap.dcCancelled);
				message.setResponseMessage(Integer.toString(val));
			} else {
				message.setResponseCode(ResponseMessageMap.failToupdateCode);
				message.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
				message.setResponseMessage(Integer.toString(val));
			}

		} catch (Exception ex) {
			logger.error("cancelDcHdr Error " + ex);
		}
		return message;
	}

	@Override
	public ResponseAsMessage updatePoType(PoTypeUpdateReq poTypeReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		int updateStatus = 0;
		try {
			updateStatus = iPoDAO.updatePoType(poTypeReq);

			if (updateStatus == 1) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage(poTypeReq.getPoType());
				returnMessage.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage(poTypeReq.getPoType());
				returnMessage.setResponseMessage(ResponseMessageMap.failMsg);
			}
		} catch (Exception e) {
			logger.error("updatePoTye Error " + e);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getHsnCodeByPartNo(PoHSNCodeRequest pohsnCodeReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<PoHsnEntity> getDCTypeDtl = new ArrayList<PoHsnEntity>();
		logger.info("getHsnCodeByPartNo Service start ");
		try {
			getDCTypeDtl = iPoDAO.getHSNbyParno(pohsnCodeReq.getPartNo(), pohsnCodeReq.getTenantId());
			if (getDCTypeDtl.size() > 0) {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getHsnCodeByPartNo Service end ");
		} catch (Exception ex) {
			logger.error("getHsnCodeByPartNo error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getdivisionDesc(TenantRequest tenantReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<PoDescMstEntity> getDCTypeDtl = new ArrayList<PoDescMstEntity>();
		logger.info("getdivisionDesc Service start ");
		try {
			getDCTypeDtl = iPoDAO.getdivisionDesc(tenantReq.getTenantID());
			if (getDCTypeDtl.size() > 0) {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getdivisionDesc Service end ");
		} catch (Exception ex) {
			logger.error("getdivisionDesc error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getTransitInsuranceDesc(TenantRequest tenantReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<PoDescMstEntity> getDCTypeDtl = new ArrayList<PoDescMstEntity>();
		logger.info("getTransitInsuranceDesc Service start ");
		try {
			getDCTypeDtl = iPoDAO.getransitInsuranceDesc(tenantReq.getTenantID());
			if (getDCTypeDtl.size() > 0) {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getTransitInsuranceDesc Service end ");
		} catch (Exception ex) {
			logger.error("getTransitInsuranceDesc error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getModeOfDispatchDesc(TenantRequest tenantReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<PoDescMstEntity> getDCTypeDtl = new ArrayList<PoDescMstEntity>();
		logger.info("getHsnCodeByPartNo Service start ");
		try {
			getDCTypeDtl = iPoDAO.getModeOfDispatchDesc(tenantReq.getTenantID());
			if (getDCTypeDtl.size() > 0) {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getModeOfDispatchDesc Service end ");
		} catch (Exception ex) {
			logger.error("getModeOfDispatchDesc error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getInspectScopeDesc(TenantRequest tenantReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<PoDescMstEntity> getDCTypeDtl = new ArrayList<PoDescMstEntity>();
		logger.info("getInspectScopeDesc Service start ");
		try {
			getDCTypeDtl = iPoDAO.getInspectScopeDesc(tenantReq.getTenantID());
			if (getDCTypeDtl.size() > 0) {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(getDCTypeDtl);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getInspectScopeDesc Service end ");
		} catch (Exception ex) {
			logger.error("getInspectScopeDesc error " + ex);
		}
		return returnList;
	}

}
