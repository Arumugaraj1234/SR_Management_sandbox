package com.vmfg.project.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.dao.interfaces.IBudgetExcessSheetDAO;
import com.vmfg.project.entity.BudgetExcessSheetEntity;
import com.vmfg.project.entity.GetIndentBudgetDtlsEntity;
import com.vmfg.project.entity.RetriveBudgetExcessStatusDtlEntity;
import com.vmfg.project.entity.SalesCategoryDtlEntity;
import com.vmfg.project.request.BudgetExcessSheetRequest;
import com.vmfg.project.request.BudgetExcessStatusDtlReq;
import com.vmfg.project.request.IndentBudgetDtlReq;
import com.vmfg.project.request.updateBudgetExcessSheetRequest;
import com.vmfg.project.response.BudgetExcessBasedIndentHdrDtl;
import com.vmfg.project.service.interfaces.IBudgetExcessSheetService;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class BudgetExcessSheetService implements IBudgetExcessSheetService {
	private static final Logger logger = LoggerFactory.getLogger(BudgetExcessSheetService.class);
	@Autowired
	IBudgetExcessSheetDAO iBudgetExcessSheetDAO;
	@Autowired
	private DesignTaskDAO designTaskDAO;
	@Autowired
	private IndentUploadDAO indentUploadDAO;
	@Autowired
	private StageManagementDAO stageManagementDAO;
	@Autowired
	private UploadManagementDAO uploadManagementDAO;
	
	@Autowired
	private CommonNotifyMethod commonNotifyMethod;

	@Override
	public ResponseAsMessage insertBudgetExcessSheetDtl(BudgetExcessSheetRequest budgetExcessSheetRequest) {
		logger.debug("insertBudgetExcessSheetDtl   method Start");
		ResponseAsMessage rmsg = new ResponseAsMessage();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		int insertBudgetStatusDtl = 0;
		try {
			String seqNo = "1";
			// getIndentHdrDetail
			BudgetExcessBasedIndentHdrDtl hdrDtl = iBudgetExcessSheetDAO
					.getIndentHdrDetail(budgetExcessSheetRequest.getIndentId());
			// getDocCurrentSeq
			List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = iBudgetExcessSheetDAO
					.getDocCurrentSeqDtl("DC039", "1", budgetExcessSheetRequest.getTenantID(),
							budgetExcessSheetRequest.getProcessDoc());
			DocumentStatusMstEntity listObj = currSeqDocLifeCycleMstList.get(0);
			BigDecimal actualCost = new BigDecimal(hdrDtl.getScmBudAllocatedValue());
			BigDecimal targetCost = new BigDecimal(hdrDtl.getTargetValue());
			BigDecimal excessValue = actualCost.subtract(targetCost);
			String excess = String.valueOf(excessValue);
			String reason = null;
			String rootCase = null;
			String action = null;
			String responsible = null;
			BigDecimal scsActualCost= BigDecimal.ZERO;
			String allocatedValue = budgetExcessSheetRequest.getAllocatedValue();
			String actualSpentSoFar = budgetExcessSheetRequest.getActualSpentSoFar();
			String pjsRefNo = null;
			if (allocatedValue != null) {
				// NEW-flow (only reachable via IndentGroupService.raiseBudgetExcess, which always
				// supplies the station snapshot): TARGET_VALUE is always 0 under this cost-flow model,
				// so the legacy checkIndentExcessCount/scsActualCost formula below is meaningless here.
				// Compute Actual Excess Cost against the real remaining station budget instead - see
				// project_budget_target_cost_removal memory for the worked-example formula this mirrors.
				BigDecimal allocated = new BigDecimal(allocatedValue);
				BigDecimal spentSoFar = new BigDecimal(actualSpentSoFar);
				BigDecimal remaining = allocated.subtract(spentSoFar);
				scsActualCost = actualCost.subtract(remaining.max(BigDecimal.ZERO));

				// PJS Ref No., e.g. "1096/E/PJS/1" - {PROJECT_CODE}/{discipline}/PJS/{seq}. Minted once
				// here and persisted, never recomputed, same as INDENT_CODE/PO_CODE. Seq is project-wide
				// (not per-discipline), matching INDENT_CODE's own RUNNING_NO convention - confirmed with
				// user rather than assumed.
				String projectCode = indentUploadDAO.getProjectCodeByProjId(hdrDtl.getPmHdrId(), budgetExcessSheetRequest.getTenantID());
				String sbcShortDesc = indentUploadDAO.getSbcShortDescBySbcCode(hdrDtl.getSbcCode(), budgetExcessSheetRequest.getTenantID());
				int nextSeq = iBudgetExcessSheetDAO.getNextPjsRefSeqByProjectId(hdrDtl.getPmHdrId(), budgetExcessSheetRequest.getTenantID());
				pjsRefNo = projectCode + "/" + sbcShortDesc + "/PJS/" + nextSeq;
			} else {
				// insertBudgetSheetDtl
				String bugtExCheck = iBudgetExcessSheetDAO.checkIndentExcessCount(hdrDtl.getIndentId(), budgetExcessSheetRequest.getTenantID());
				if(!bugtExCheck.equalsIgnoreCase("0") ){
					scsActualCost = new BigDecimal(budgetExcessSheetRequest.getScsFinalCost());
				}else {
					scsActualCost = new BigDecimal (hdrDtl.getScmBudAllocatedValue()).subtract(targetCost);
				}
			}
			int beHdrId = iBudgetExcessSheetDAO.insertBudgetExcessSheetDtl(hdrDtl.getIndentId(), hdrDtl.getPmHdrId(),
					hdrDtl.getTargetValue(), hdrDtl.getScmBudAllocatedValue(), excess,
					budgetExcessSheetRequest.getVendor(), reason, rootCase, action, responsible, seqNo,
					listObj.getDocStatus(), budgetExcessSheetRequest.getUpdatedBy(),
					budgetExcessSheetRequest.getTenantID(), hdrDtl.getDskId(), budgetExcessSheetRequest.getIgScsId(),String.valueOf(scsActualCost),
					allocatedValue, actualSpentSoFar, pjsRefNo);
			// insertBudgetSheetStatus
			String remarks = "New Budget Excess";
			if (beHdrId > 0) {
				insertBudgetStatusDtl = iBudgetExcessSheetDAO.insertBudgetExcessStatus(beHdrId, seqNo,
						listObj.getDocStatus(), remarks, budgetExcessSheetRequest.getUpdatedBy(),
						budgetExcessSheetRequest.getTenantID());
				String indentCode=indentUploadDAO.getIndentCodeByIndentId(budgetExcessSheetRequest.getIndentId());
				messageList.add(indentCode);
				
				// get docGroup based on excess value
				String docGrp = getDocGroup(excess,budgetExcessSheetRequest.getTenantID(),budgetExcessSheetRequest.getProcessDoc());
				String nextApprDesig = indentUploadDAO.getIndentNxtAppDesc("DC039", "1",docGrp,budgetExcessSheetRequest.getTenantID());
				
				commonNotifyMethod.InvokeNotificationMethod(1,43, null, budgetExcessSheetRequest.getTenantID(), messageList, otherEmpId, "1", budgetExcessSheetRequest.getPmId(), budgetExcessSheetRequest.getMasterId(), nextApprDesig);
				commonNotifyMethod.InvokeApprovalDesigMethod(budgetExcessSheetRequest.getPmId(), "DC039",String.valueOf(beHdrId),budgetExcessSheetRequest.getProjectId(), budgetExcessSheetRequest.getTenantID(),"" ,nextApprDesig, indentUploadDAO.getEnqIdByProjectId(budgetExcessSheetRequest.getProjectId()),indentCode);
		
			}
			if (beHdrId > 0 && insertBudgetStatusDtl > 0) {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				rmsg.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				rmsg.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
		} catch (Exception ex) {
			logger.error("insertBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("insertBudgetExcessSheetDtl   method end");
		return rmsg;
	}

	@Override
	public ResponseAsMessage updateBudgetExcessSheetDtl(updateBudgetExcessSheetRequest updateudget) {
		logger.debug("updateBudgetExcessSheetDtl   method Start");
		ResponseAsMessage rmsg = new ResponseAsMessage();
		try {
			int qResp = iBudgetExcessSheetDAO.updateBudgetExcessSheetDtl(updateudget);

			if (qResp > 0) {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				rmsg.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				rmsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				rmsg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		} catch (Exception ex) {
			logger.error("updateBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("updateBudgetExcessSheetDtl   method end");
		return rmsg;
	}

	@Override
	public List<BudgetExcessSheetEntity> retriveBudgetExcessSheetDtl(BudgetExcessStatusDtlReq statusDtlReq) {
		logger.debug("retriveBudgetExcessSheetDtl   method Start");
		List<BudgetExcessSheetEntity> list = new ArrayList<>();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<>();
		List<BudgetExcessSheetEntity> newTotalList = new ArrayList<>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		try {
			String docGroup = "";
			list = iBudgetExcessSheetDAO.retriveBudgetExcessSheetDtl(statusDtlReq);
			int serialNumber = 1;

			// The lookups below repeat the same (indentId) / (currSeq, docGroup) / (statusCode) combinations
			// across many rows of the same request - cache them per-request (read-only lookups, see getDocGroup
			// helpers below) instead of re-querying the DB for every row. Cache maps are local to this method call,
			// so there is no cross-request/thread state.
			Map<String, String> assemblyDesCache = new HashMap<>();
			Map<String, String> subAssemblyDesCache = new HashMap<>();
			Map<String, String> budgetCodeCache = new HashMap<>();
			Map<String, List<DocumentStatusMstEntity>> currSeqDocCache = new HashMap<>();
			Map<String, List<DocumentStatusMstEntity>> nextSeqDocCache = new HashMap<>();
			Map<String, String> statusCodeCache = new HashMap<>();
			Map<String, String> statusDescCache = new HashMap<>();
			Map<String, Integer> approveBtnCache = new HashMap<>();
			Map<String, String> docTypeDescCache = new HashMap<>();
			// Overall Excess Cost (Station) is only meaningful for NEW-flow rows (see
			// project_budget_target_cost_removal memory) - cached per PKA_ID so a station with
			// several rows in the same request only fires the live SUM query once.
			Map<String, String> overallExcessCache = new HashMap<>();

			// docGroupList and designCode are the same for every row in this request (they only depend on
			// tenantId/processCode/empId from statusDtlReq), so fetch them once instead of once per row.
			List<String> docGroupList = iBudgetExcessSheetDAO.getDistinctDocGroup("DC039", statusDtlReq.getTenantId(),
					statusDtlReq.getProcessCode());
			String designCode = uploadManagementDAO.getDesigCodeByEmpId(statusDtlReq.getEmpId(),
					statusDtlReq.getTenantId());

			for (BudgetExcessSheetEntity listObj : list) {
				listObj.setSerialNumber(String.valueOf(serialNumber++));

				String indentId = listObj.getIndentId();

				String assemblyValue = assemblyDesCache.get(indentId);
				if (assemblyValue == null) {
					assemblyValue = iBudgetExcessSheetDAO.getAssemblyDes(statusDtlReq.getTenantId(), indentId);
					assemblyDesCache.put(indentId, assemblyValue);
				}
				listObj.setAssemblyValue(assemblyValue);

				String subAssemblyValue = subAssemblyDesCache.get(indentId);
				if (subAssemblyValue == null) {
					subAssemblyValue = iBudgetExcessSheetDAO.getSubAssemblyDes(statusDtlReq.getTenantId(), indentId);
					subAssemblyDesCache.put(indentId, subAssemblyValue);
				}
				listObj.setSubAssemblyValue(subAssemblyValue);

				String budgetCostlat = budgetCodeCache.get(indentId);
				if (budgetCostlat == null) {
					budgetCostlat = iBudgetExcessSheetDAO.getBudgetCodeByIndentId(indentId);
					budgetCodeCache.put(indentId, budgetCostlat);
				}
				listObj.setBudgetCostlat(budgetCostlat);

				if ("NEW".equalsIgnoreCase(listObj.getCostFlowType())) {
					String pkaId = listObj.getPkaId();
					String overallExcess = overallExcessCache.get(pkaId);
					if (overallExcess == null) {
						overallExcess = iBudgetExcessSheetDAO.getOverallExcessCostByPkaId(pkaId, statusDtlReq.getTenantId());
						overallExcessCache.put(pkaId, overallExcess);
					}
					listObj.setOverallExcessCost(overallExcess);
				}

				// beHdrId-specific, genuinely different per row - not cacheable
				listObj.setVerCheck(iBudgetExcessSheetDAO.getVerChechForBudgetExcessByBeHdrId(listObj.getBeHdrId(),listObj.getTenantId()));
				// get docGroup based on excess value (docGroupList fetched once, above)
				docGroup = resolveDocGroup(listObj.getExcess(), docGroupList);

				int approveBtnEnable = 0;
				// DocList Start
				String currSeq = listObj.getSequenceNo();
				String seqGroupKey = currSeq + "|" + docGroup;

				currSeqDocLifeCycleMstList = currSeqDocCache.get(seqGroupKey);
				if (currSeqDocLifeCycleMstList == null) {
					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp("DC039", currSeq,
							statusDtlReq.getTenantId(), docGroup, statusDtlReq.getProcessCode());
					currSeqDocCache.put(seqGroupKey, currSeqDocLifeCycleMstList);
				}

				List<DocumentStatusMstEntity> cachedNextSeqList = nextSeqDocCache.get(seqGroupKey);
				if (cachedNextSeqList == null) {
					cachedNextSeqList = designTaskDAO.getNextSeqandStatusByDoc(Integer.parseInt(currSeq), "DC039",
							statusDtlReq.getTenantId(), docGroup, statusDtlReq.getProcessCode());
					nextSeqDocCache.put(seqGroupKey, cachedNextSeqList);
				}
				// Each row mutates its own doc-status entry below (setDocStatusDesc/setPreviousSeq/etc) -
				// use an independent copy per row rather than the shared cached instance.
				docLifeCycleMstList = cloneStatusList(cachedNextSeqList);

				String nextSeqTypeCode = "NA";
				if(docLifeCycleMstList.size() > 0) {
					String currSequence = docLifeCycleMstList.get(0).getCurrSequence();
					String currSequenceStatusCode = getCachedStatusCode(statusCodeCache, currSequence, docGroup, statusDtlReq, indentUploadDAO);
					nextSeqTypeCode = getCachedStatusDesc(statusDescCache, currSequenceStatusCode, statusDtlReq, designTaskDAO);
				}

				if (nextSeqTypeCode == null || nextSeqTypeCode.isEmpty()) {
				    nextSeqTypeCode = "NA";
				}

				String nextSeq= docLifeCycleMstList.size() > 0 ? docLifeCycleMstList.get(0).getNextSeq() : "NA";

				listObj.setNextSeq(nextSeq);
				listObj.setNextSeqDesc(nextSeqTypeCode);

				if (docLifeCycleMstList.size() > 0) {

					String currSequence = docLifeCycleMstList.get(0).getCurrSequence();
					String approveBtnKey = docGroup + "|" + currSequence;
					Integer cachedApproveBtn = approveBtnCache.get(approveBtnKey);
					if (cachedApproveBtn == null) {
						cachedApproveBtn = indentUploadDAO.getApprovebtnEnable(designCode, docGroup,statusDtlReq.getProcessCode(),
								statusDtlReq.getTenantId(), "DC039", currSequence);
						approveBtnCache.put(approveBtnKey, cachedApproveBtn);
					}
					approveBtnEnable = cachedApproveBtn;

					if (approveBtnEnable == 1) {
						// curr seq
						String docType = docLifeCycleMstList.get(0).getDocType();
						String docTypeDesc = docTypeDescCache.get(docType);
						if (docTypeDesc == null) {
							docTypeDesc = indentUploadDAO.getDocTypeDescByDocType(docType, statusDtlReq.getTenantId());
							docTypeDescCache.put(docType, docTypeDesc);
						}
						docLifeCycleMstList.get(0).setDocTypeDesc(docTypeDesc);

						String currSeqStatusCode = getCachedStatusCode(statusCodeCache, currSequence, docGroup, statusDtlReq, indentUploadDAO);
						docLifeCycleMstList.get(0).setDocStatusDesc(
								getCachedStatusDesc(statusDescCache, currSeqStatusCode, statusDtlReq, designTaskDAO));// Current seq docStatus

						// Previous Seq
						docLifeCycleMstList.get(0).setPreviousSeq(currSeq);
						String prevSeqStatusCode = getCachedStatusCode(statusCodeCache, currSeq, docGroup, statusDtlReq, indentUploadDAO);
						docLifeCycleMstList.get(0).setPreviousSeqStatusCode(prevSeqStatusCode);

						docLifeCycleMstList.get(0).setPreviousSeqStatusDesc(
								getCachedStatusDesc(statusDescCache, prevSeqStatusCode, statusDtlReq, designTaskDAO));
						// cancel seq
						if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
							String cancelSeq = currSeqDocLifeCycleMstList.get(0).getCancelSeq();
							docLifeCycleMstList.get(0).setCancelSeq(cancelSeq);

							String cancelStatusCode = getCachedStatusCode(statusCodeCache, cancelSeq, docGroup, statusDtlReq, indentUploadDAO);
							docLifeCycleMstList.get(0).setCancelStatusCode(cancelStatusCode);

							docLifeCycleMstList.get(0).setCancelStatusDesc(
									getCachedStatusDesc(statusDescCache, cancelStatusCode, statusDtlReq, designTaskDAO));
						}
						listObj.setDocumentStatusMstList(docLifeCycleMstList);
					}
				}
				// DocList End//
				// BudgetSheet StatusList
				List<RetriveBudgetExcessStatusDtlEntity> statusList = iBudgetExcessSheetDAO
						.getBudegetStatusDtl(listObj.getBeHdrId(),statusDtlReq.getTenantId());
				listObj.setBudgetExcessStatusDtlList(statusList);

				newTotalList.add(listObj);

			}

			list.clear();
			list.addAll(newTotalList);
		} catch (Exception ex) {
			logger.error("retriveBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("retriveBudgetExcessSheetDtl   method end");
		return list;
	}

	@Override
	public List<SalesCategoryDtlEntity> getIndentBudgetDtl(IndentBudgetDtlReq indentBudgetDtl) {
		logger.debug("getIndentBudgetDtl   method Start");
		List<SalesCategoryDtlEntity> salesCategoryDtlList = new ArrayList<>();
		List<GetIndentBudgetDtlsEntity> list = new ArrayList<>();
		List<SalesCategoryDtlEntity> catagoryObj = new ArrayList<>();
		try {

			salesCategoryDtlList = iBudgetExcessSheetDAO.getSalesCatagoryDtl(indentBudgetDtl);

			for (SalesCategoryDtlEntity str : salesCategoryDtlList) {
				BigDecimal budegtValue = new BigDecimal(str.getBudgetValue());
				BigDecimal allocattedValue = new BigDecimal(str.getScmBudgetAllocated());
				BigDecimal excessValue = allocattedValue.subtract(budegtValue);
				String excess = String.valueOf(excessValue);
				str.setExcessBudgetValue(excess);
				list = iBudgetExcessSheetDAO.getIndentBudgetDtl(str.getSbcCode(), indentBudgetDtl.getPmHdrId());

				str.setList(list);
				catagoryObj.add(str);

			}
		} catch (Exception ex) {
			logger.error("getIndentBudgetDtl  method  exception" + ex);
		}
		logger.debug("getIndentBudgetDtl   method end");
		return catagoryObj;
	}

	@Override
	public ResponseAsMessage updateBudgetSheetExcessSeqAndStatus(UpdateSeqAndStatusRequest updateDtls) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {
			logger.debug("updateBudgetSheetExcessSeqAndStatus method Start");
			int insertBudegtSheetDtlStatusDtl = 0;
			String docType = "DC039";
			String budegtSheetDtlId = updateDtls.getHdrId();
			String excessValue = iBudgetExcessSheetDAO.getBudgetExcessValueByHdrId(budegtSheetDtlId);

			String docGrp = getDocGroup(excessValue,updateDtls.getTenantId(),updateDtls.getProcessCode());
			// Take Last Sequence from documentLifeCycle
			currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp(docType,
					updateDtls.getCurrentseq(), updateDtls.getTenantId(), docGrp,updateDtls.getProcessCode());
			
				
				iBudgetExcessSheetDAO.UpdateIndentAndScsStatus(updateDtls.getCurrentseq(), updateDtls.getHdrId(), updateDtls.getTenantId(), updateDtls.getEmpId(), docGrp,updateDtls.getProcessCode());	
				
				// Update BudgetExcessDtl
						iBudgetExcessSheetDAO.updateBudgetSheetExcessSeqAndStatus(budegtSheetDtlId,
						updateDtls.getCurrentseq(), currSeqDocLifeCycleMstList.get(0).getDocStatus(),
						updateDtls.getTenantId(),updateDtls.getEmpId());
				
				// BudgetExcessDtlStatus
				insertBudegtSheetDtlStatusDtl = iBudgetExcessSheetDAO.insertBudgetSheetExcessStatusStatusDtl(
						budegtSheetDtlId, updateDtls.getCurrentseq(), currSeqDocLifeCycleMstList.get(0).getDocStatus(),
						updateDtls.getTenantId(), updateDtls.getRemarks(), updateDtls.getEmpId());

				String indentId=iBudgetExcessSheetDAO.getIndentIdByBehdrID(updateDtls.getHdrId());
				messageList.add(indentUploadDAO.getIndentCodeByIndentId(indentId));
				
				String nextApprDesig = commonNotifyMethod.getNxtAppDescByDocGroup("DC039", updateDtls.getCurrentseq(), updateDtls.getDocGroup(),updateDtls.getTenantId(),updateDtls.getProcessCode());
				if (nextApprDesig.equalsIgnoreCase("")) {
					nextApprDesig=null;
				}
				commonNotifyMethod.InvokeNotificationMethod(1,44, "", updateDtls.getTenantId(), messageList, otherEmpId, "1", updateDtls.getPmId(), null, nextApprDesig);
				
				if (currSeqDocLifeCycleMstList.get(0).getLastSeq()!=null && currSeqDocLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
					iBudgetExcessSheetDAO.updateBudgetSheetExcessApproved(budegtSheetDtlId);
				}
					commonNotifyMethod.InvokeApprovalDesigMethod(updateDtls.getPmId(), "DC039",updateDtls.getHdrId(),updateDtls.getPmHdrId(), updateDtls.getTenantId(),"" ,nextApprDesig, indentUploadDAO.getEnqIdByProjectId(updateDtls.getPmHdrId()),indentUploadDAO.getIndentCodeByIndentId(indentId));
				

				if (insertBudegtSheetDtlStatusDtl > 0) {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage("Success");
					returnMessage.setResponseMessage(ResponseMessageMap.approved);
				} else {

				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updateBudgetSheetExcessSeqAndStatus error " + ex);
		}
		logger.debug("updateBudgetSheetExcessSeqAndStatus method end");
		return returnMessage;
	}

	// Same lookup as getDocGroup below, but reuses a docGroupList already fetched once for the whole
	// request instead of re-querying the DB - used by the per-row loop in retriveBudgetExcessSheetDtl.
	private String resolveDocGroup(String excessValue, List<String> docGroupList) {
		String docGroup = "";
		try {
			BigDecimal excess = new BigDecimal(excessValue);
			if (docGroupList.size() > 0) {
				for (String docGrpVal : docGroupList) {
					if (excess.compareTo(new BigDecimal(docGrpVal)) <= 0) {
						docGroup = docGrpVal;
						break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("resolveDocGroup Error" + ex);
		}
		return docGroup;
	}

	private String getCachedStatusCode(Map<String, String> statusCodeCache, String seq, String docGroup,
			BudgetExcessStatusDtlReq statusDtlReq, IndentUploadDAO indentUploadDAO) {
		String key = seq + "|" + docGroup;
		String statusCode = statusCodeCache.get(key);
		if (statusCode == null) {
			statusCode = indentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(seq, statusDtlReq.getTenantId(), "DC039",
					docGroup, statusDtlReq.getProcessCode());
			statusCodeCache.put(key, statusCode);
		}
		return statusCode;
	}

	private String getCachedStatusDesc(Map<String, String> statusDescCache, String statusCode,
			BudgetExcessStatusDtlReq statusDtlReq, DesignTaskDAO designTaskDAO) {
		String statusDesc = statusDescCache.get(statusCode);
		if (statusDesc == null) {
			statusDesc = designTaskDAO.getStatusByDesc(statusCode, statusDtlReq.getTenantId());
			statusDescCache.put(statusCode, statusDesc);
		}
		return statusDesc;
	}

	// Rows share the same cached DocumentStatusMstEntity list (see nextSeqDocCache in retriveBudgetExcessSheetDtl),
	// but each row mutates its own copy (setDocStatusDesc/setPreviousSeq/etc) - clone before handing one out so
	// mutating one row's copy can never leak into another row that happened to share the same cache key.
	private List<DocumentStatusMstEntity> cloneStatusList(List<DocumentStatusMstEntity> source) {
		List<DocumentStatusMstEntity> copy = new ArrayList<>();
		if (source == null) {
			return copy;
		}
		for (DocumentStatusMstEntity e : source) {
			DocumentStatusMstEntity c = new DocumentStatusMstEntity();
			c.setDsmId(e.getDsmId());
			c.setDocType(e.getDocType());
			c.setDocTypeDesc(e.getDocTypeDesc());
			c.setCurrSequence(e.getCurrSequence());
			c.setDocStatus(e.getDocStatus());
			c.setDocStatusDesc(e.getDocStatusDesc());
			c.setApprDesi(e.getApprDesi());
			c.setApprDept(e.getApprDept());
			c.setIsNotify(e.getIsNotify());
			c.setLastSeq(e.getLastSeq());
			c.setNextSeq(e.getNextSeq());
			c.setSeqBatch(e.getSeqBatch());
			c.setTenantId(e.getTenantId());
			c.setIsActive(e.getIsActive());
			c.setDocGroup(e.getDocGroup());
			c.setProcessCode(e.getProcessCode());
			c.setIsEditable(e.getIsEditable());
			c.setNextSeqStatusCode(e.getNextSeqStatusCode());
			c.setNextSeqStatusDesc(e.getNextSeqStatusDesc());
			c.setPreviousSeq(e.getPreviousSeq());
			c.setPreviousSeqStatusCode(e.getPreviousSeqStatusCode());
			c.setPreviousSeqStatusDesc(e.getPreviousSeqStatusDesc());
			c.setCancelSeq(e.getCancelSeq());
			c.setCancelStatusCode(e.getCancelStatusCode());
			c.setCancelStatusDesc(e.getCancelStatusDesc());
			copy.add(c);
		}
		return copy;
	}

	public String getDocGroup(String excessValue, String tenantId, String processCode) {
		String docGroup = "";
		try {
			BigDecimal excess=new BigDecimal(excessValue);
			List<String> docGroupList = iBudgetExcessSheetDAO.getDistinctDocGroup("DC039",tenantId,processCode);
			if (docGroupList.size() > 0) {
				for (String docGrpVal : docGroupList) {
					if (excess.compareTo(new BigDecimal(docGrpVal)) <= 0) {
					    docGroup = docGrpVal;
					    break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("getDocGroup Error" + ex);
		}
		return docGroup;
	}

}
