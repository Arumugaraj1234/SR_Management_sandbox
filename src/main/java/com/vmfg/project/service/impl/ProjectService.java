package com.vmfg.project.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.project.request.*;
import com.vmfg.project.response.ProjectInternalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.dao.interfaces.IDesignDAO;
import com.vmfg.design.entity.ProjectKeyAreaMstEntity;
import com.vmfg.design.request.ProductDtlDropDownRequest;
import com.vmfg.design.request.TenantRequest;
import com.vmfg.design.response.KeyArea;
import com.vmfg.general.request.InitiateProcessRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.impl.StageManagementService;
import com.vmfg.project.controller.UpdateProjectPlanDateRequest;
import com.vmfg.project.dao.interfaces.IBudgetExcessSheetDAO;
import com.vmfg.project.dao.interfaces.IProjectDAO;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.project.entity.GetProjTimePlanDropDownEntity;
import com.vmfg.project.entity.GetindentbudgetDtlEntity;
import com.vmfg.project.entity.IndentBudgetDtlEntity;
import com.vmfg.project.entity.ProjectHdr;
import com.vmfg.project.entity.ProjectSubAreaExtnEntity;
import com.vmfg.project.entity.ProjectTimelineEntity;
import com.vmfg.project.entity.ProjectTimelineResp;
import com.vmfg.project.entity.ProjectWBSTemplate;
import com.vmfg.project.entity.SalesBudgetExtnDtlEntity;
import com.vmfg.project.entity.BudgetSheetPaymentEntity;
import com.vmfg.project.entity.SalesBudgetExtnListDtlEntity;
import com.vmfg.project.entity.SubAreaPmHdrListEntity;
import com.vmfg.project.entity.SumOfIndentHdrEntity;
import com.vmfg.project.entity.getLinkStatusByPMIdRespEntity;
import com.vmfg.project.response.GetbugetextnListbyDSkIdResponse;
import com.vmfg.project.response.getelementHdrDistinctResponse;
import com.vmfg.project.service.interfaces.IProjectService;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.dao.interfaces.IIndentGroupDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class ProjectService implements IProjectService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
	@Autowired
	IProjectDAO iProjectDAO;

	@Autowired
	IDesignDAO iDesignDAO;
	@Autowired
	UploadManagementDAO deptM;
	@Autowired
	StageManagementService stageManagementService;
	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	IndentUploadDAO indentUploadDao;

	@Autowired
	CommonNotifyMethod commonNotifyMethod;

	@Autowired
	IPoDAO iPoDAO;

	@Autowired
	IIndentGroupDAO iIndentGroupDAO;

	@Autowired
	IBudgetExcessSheetDAO iBudgetExcessSheetDAO;

	@Override
	public ResponseAsList getProjectDtl(ProjectHdrRequest tenReq) {
		List<ProjectHdr> resp = iProjectDAO.getProjectDtl(tenReq.getTenantID(), tenReq.getCustName(),
				tenReq.getFromDate(), tenReq.getToDate(), tenReq.getProjectID(), tenReq.getEmpId(), tenReq.getPmId());
		ResponseAsList respLi = new ResponseAsList();
		if (resp.size() > 0) {

			resp.forEach(projHdr -> {
				 List<BudgetSheetPaymentEntity> paymentTerms = iProjectDAO.getBudgetSheetPaymentTerms(projHdr.getSbHdrId());
                projHdr.setPaymentTerms(paymentTerms);
				projHdr.setIndentPlan(iProjectDAO.getBudgetValue(projHdr.getPmHdrId(), tenReq.getTenantID()));
				projHdr.setIndentActual(iProjectDAO.getAllocValue(projHdr.getPmHdrId(), tenReq.getTenantID()));
				projHdr.setTargetCost(iProjectDAO.getTargetCost(projHdr.getPmHdrId(), tenReq.getTenantID()));
				projHdr.setCompletionPercent(iProjectDAO.getCompletionPercent(projHdr.getPmHdrId(), tenReq.getTenantID()));
				projHdr.setDebitValue(iProjectDAO.getDebitVal(projHdr.getPmHdrId(), tenReq.getTenantID()));

				String costFlowType = iProjectDAO.getCostFlowTypeByPmHdrId(projHdr.getPmHdrId());
				projHdr.setCostFlowType(costFlowType);
				if ("NEW".equalsIgnoreCase(costFlowType)) {
					BigDecimal allocatedValue = new BigDecimal(iProjectDAO.getAllocatedValSumByPmHdrId(projHdr.getPmHdrId()));
					String scsSeq = iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", tenReq.getTenantID());
					String capexScsSeq = iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", tenReq.getTenantID());
					String minSeqNo = new BigDecimal(scsSeq).min(new BigDecimal(capexScsSeq)).toString();
					BigDecimal approvedPoTotal = new BigDecimal(iPoDAO.getApprovedPoTotalByProjectId(projHdr.getPmHdrId()));
					BigDecimal committedScsTotal = new BigDecimal(
							iIndentGroupDAO.getCommittedScsTotalByProjectId(projHdr.getPmHdrId(), minSeqNo));
					BigDecimal consumedSoFar = approvedPoTotal.add(committedScsTotal);

					HdrIdandTenantIdRequest transferReq = new HdrIdandTenantIdRequest();
					transferReq.setHdrId(projHdr.getPmHdrId());
					transferReq.setTenantId(tenReq.getTenantID());
					BigDecimal matCost = new BigDecimal(indentUploadDao.getSumOfTransferValue(transferReq));

					BigDecimal employeeCost = new BigDecimal(
							iProjectDAO.getEmployeeCostByPmHdrId(projHdr.getPmHdrId(), tenReq.getTenantID()));
					BigDecimal debitCost = new BigDecimal(projHdr.getDebitValue());
					BigDecimal budgetExcessApproved = new BigDecimal(
							iBudgetExcessSheetDAO.getApprovedExcessTotalByPmHdrId(projHdr.getPmHdrId()));

					BigDecimal actualSpent = consumedSoFar.add(matCost).add(employeeCost).add(budgetExcessApproved)
							.subtract(debitCost);

					projHdr.setAllocatedValue(allocatedValue.toString());
					projHdr.setActualSpent(actualSpent.toString());
				}
			});

			respLi.setResponseCode(ResponseMessageMap.success);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
			respLi.setResponseData(resp);
		} else {
			respLi.setResponseCode(ResponseMessageMap.noRecord);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		return respLi;
	}

	@Override
	public ResponseAsList getWbsTemplate(TenantRequest tenantReq) {
		// TODO Auto-generated method stub
		List<ProjectWBSTemplate> wbsTemplate = iProjectDAO.getWbsTemplate(tenantReq);

		ResponseAsList respLi = new ResponseAsList();
		if (wbsTemplate.size() > 0) {

			respLi.setResponseCode(ResponseMessageMap.success);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
			respLi.setResponseData(wbsTemplate);
		} else {
			respLi.setResponseCode(ResponseMessageMap.noRecord);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		return respLi;
	}

	@Override
	public ResponseAsList getWbsTemplateById(WbsIDRequest wbsReq) {
		List<ProjectWBSTemplate> wbsTemplate = iProjectDAO.getWbsTemplateById(wbsReq);

		ResponseAsList respLi = new ResponseAsList();
		if (wbsTemplate.size() > 0) {

			respLi.setResponseCode(ResponseMessageMap.success);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
			respLi.setResponseData(wbsTemplate);
		} else {
			respLi.setResponseCode(ResponseMessageMap.noRecord);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		return respLi;
	}

	@Override
	public ResponseAsMessage insertUpdateProjectMilestone(List<ProjectTimelineRequest> projTimeReq) {

		ResponseAsMessage rm = new ResponseAsMessage();

		AtomicInteger count = new AtomicInteger();
	//	String dueDate = iProjectDAO.projectDueDate(projTimeReq.get(0).getPmHdrId(), projTimeReq.get(0).getTenantId());
		projTimeReq.forEach(pt -> {
			InitiateProcessRequest initiateReq = new InitiateProcessRequest();
			initiateReq.setDeptCode(pt.getResponsibleDeptCode());
			initiateReq.setDueDate(pt.getPlannedEndDate());
			initiateReq.setEmpID(pt.getUpdatedBy());
			initiateReq.setRefId(pt.getPmHdrId());
			initiateReq.setTenantId(pt.getTenantId());
			initiateReq.setStartDate(pt.getPlannedStartDate());
			initiateReq.setPmId("");
			stageManagementService.initiateProcess(initiateReq);
			int qResp = iProjectDAO.insertUpdateProjectMilestone(pt);
			if (qResp == 200) {
				count.incrementAndGet();
			}

		});
	//	String maxValue = iProjectDAO.getMinMaxDate("max", projTimeReq.get(0).getPmHdrId(), "PLANNED_END_DATE");
	//	String minValue = iProjectDAO.getMinMaxDate("min", projTimeReq.get(0).getPmHdrId(), "PLANNED_START_DATE");

	//	iProjectDAO.updatePlanStartAndEndDate(maxValue, minValue, projTimeReq.get(0).getPmHdrId());

		if (count.intValue() == projTimeReq.size()) {
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.successUpdated);
		} else {
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.partialSucess);
		}

		return rm;
	}

	@Override
	public ResponseAsMessage deleteWBSById(DeleteTimeWBSByIDRequest deleteById) {
		return iProjectDAO.deleteWBSById(deleteById);
	}

	@Override
	public ResponseAsList getTimeLineByPM(ProjectByIDRequest projHdr) {
		List<ProjectTimelineResp> resp = iProjectDAO.getTimeLineByPM(projHdr);

		ResponseAsList respLi = new ResponseAsList();
		if (resp.size() > 0) {

			respLi.setResponseCode(ResponseMessageMap.success);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
			respLi.setResponseData(resp);
		} else {
			respLi.setResponseCode(ResponseMessageMap.noRecord);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		return respLi;
	}

	@Override
	public ProjectInternalResponse getIsInternalOrNot(ProjectInternalRequest request) {
		logger.info("Service - getIsInternalOrNot called with tenantId={}, projectCode={}",
				request.getTenantId(), request.getProjectCode());

		ProjectInternalResponse response = projectDAO.getProjectInternal(
				request.getTenantId(),
				request.getProjectCode()
		);

		logger.info("Service - DAO returned: {}", response);
		return response;
	}

	@Override
	public ResponseAsMessage insertKeyAreaByPMId(List<KeyAreaRequest> keyAre) {

		ResponseAsMessage rm = new ResponseAsMessage();

		keyAre.forEach(pt -> {
			int pkId = iProjectDAO.getCountProjectKeyMst(pt.getPkId(), pt.getTenantId(),pt.getPmHdrId());
			if (pkId == 0) {
				String lastCode = iProjectDAO.getLastKeyCode(pt.getTenantId(),pt.getPmHdrId());
				int newCode = Integer.parseInt(lastCode) + 10;
				pkId = iProjectDAO.insertProjectKeyArea(Integer.toString(newCode), pt.getPkId(), "1", pt.getTenantId());
			}
			if (pkId > 0) {
				int pskCheck = iProjectDAO.projectKeyAreaCount(pt.getPmHdrId(), Integer.toString(pkId),
						pt.getTenantId());
				if (pskCheck == 0) {
					pt.setPkId(Integer.toString(pkId));
					int qResp = iProjectDAO.insertKeyAreaByPMId(pt);
					if (qResp == 200) {
						List<String> messageList = new ArrayList<>();
						List<String> otherEmp = new ArrayList<>();
						String projCode =indentUploadDao.getProjectCodeByProjId(keyAre.get(0).getPmHdrId(),keyAre.get(0).getTenantId());
						
						messageList.add("Project "+projCode);
						commonNotifyMethod.InvokeNotificationMethod(2, 10, null, keyAre.get(0).getTenantId(), messageList, otherEmp, "1",keyAre.get(0).getPmId(), keyAre.get(0).getPmHdrId(),null);
						
						rm.setResponseCode(ResponseMessageMap.responseCodeOk);
						rm.setResponseMessage(ResponseMessageMap.successUpdated);
					} else {
						rm.setResponseCode(ResponseMessageMap.failToupdateCode);
						rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
					}
				} else {
					rm.setResponseCode(ResponseMessageMap.failToupdateCode);
					rm.setResponseMessage(ResponseMessageMap.responseAlreadyExistMsg);

				}

			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		});

		return rm;
	}

	@Override
	public ResponseAsMessage delKeyAreaByPKId(KeyAreaDelRequest delReq) {
		return iProjectDAO.deleteWBSById(delReq);
	}

	@Override
	public ResponseAsList getPKForProj(ProjectByIDRequest projHdr) {
		List<KeyArea> ka = iProjectDAO.getPKForProj(projHdr);
		ResponseAsList respLi = new ResponseAsList();
		if (ka.size() > 0) {

			respLi.setResponseCode(ResponseMessageMap.success);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
			respLi.setResponseData(ka);
		} else {
			respLi.setResponseCode(ResponseMessageMap.noRecord);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		return respLi;

	}

	@Override
	public ResponseAsMessage updatedesignindentReq(ProjectByIDRequest projHdr) {

		ResponseAsMessage rm = new ResponseAsMessage();

		int qResp = iProjectDAO.updateDesignIndentstart(projHdr.getProjectID(), projHdr.getTenantID());
		if (qResp > 0) {
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.successUpdated);
		} else {
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.partialSucess);
		}

		return rm;
	}

	@Override
	public ResponseAsList getLinkStatusByPMId(ProjectByIDRequest projHdr) {
		ResponseAsList list = new ResponseAsList();
		try {
			ProductDtlDropDownRequest tentReq = new ProductDtlDropDownRequest();
			tentReq.setPmHdrId(projHdr.getProjectID());
			tentReq.setTenantId(projHdr.getTenantID());
			List<getLinkStatusByPMIdRespEntity> finalResp = new ArrayList<getLinkStatusByPMIdRespEntity>();
			List<ProjectKeyAreaMstEntity> keyArea = iDesignDAO.getKeyArea(tentReq);
			if (keyArea.size() > 0) {
				String costFlowType = iProjectDAO.getCostFlowTypeByPmHdrId(projHdr.getProjectID());
				String minSeqNo = null;
				if ("NEW".equalsIgnoreCase(costFlowType)) {
					String scsSeq = iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", projHdr.getTenantID());
					String capexScsSeq = iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", projHdr.getTenantID());
					minSeqNo = new BigDecimal(scsSeq).min(new BigDecimal(capexScsSeq)).toString();
				}
				for (int i = 0; i < keyArea.size(); i++) {
					getLinkStatusByPMIdRespEntity resp = iProjectDAO.linkStatusCount(projHdr.getProjectID(),
							keyArea.get(i).getPkId(), projHdr.getTenantID());
					List<SumOfIndentHdrEntity> sumOfIndentHdr = iProjectDAO.getSumOfIndentHdrEntity(resp.getPkaId());
					if (sumOfIndentHdr.size() > 0) {
						resp.setActualCost(sumOfIndentHdr.get(0).getActualCost());
						resp.setTargetCost(sumOfIndentHdr.get(0).getTargetCost());
						resp.setBudgetCost(sumOfIndentHdr.get(0).getBudgetCost());
					} else {
						resp.setActualCost("0");
						resp.setTargetCost("0");
						resp.setBudgetCost("0");
					}
					resp.setCostFlowType(costFlowType);
					if ("NEW".equalsIgnoreCase(costFlowType)) {
						BigDecimal approvedPoTotal = new BigDecimal(iPoDAO.getApprovedPoTotalByPkaId(resp.getPkaId()));
						BigDecimal otherCommittedPjs = new BigDecimal(
								iIndentGroupDAO.getOtherCommittedScsTotalByPkaId(resp.getPkaId(), "-1", minSeqNo));
						resp.setConsumedSoFar(approvedPoTotal.add(otherCommittedPjs).toString());
					}
					finalResp.add(resp);
				}
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(finalResp);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(finalResp);
			}

		} catch (Exception ex) {
			logger.error("Error getLinkStatusByPMId " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage getCostFlowTypeByPmHdrId(ProjectByIDRequest projHdr) {
		ResponseAsMessage rmsg = new ResponseAsMessage();
		try {
			String costFlowType = iProjectDAO.getCostFlowTypeByPmHdrId(projHdr.getProjectID());
			rmsg.setResponseCode(ResponseMessageMap.responseCodeOk);
			rmsg.setResponseMessage(ResponseMessageMap.success);
			rmsg.setResponseDataMessage(costFlowType);
		} catch (Exception ex) {
			logger.error("Error getCostFlowTypeByPmHdrId " + ex);
		}
		return rmsg;
	}

	@Override
	public ResponseAsList getelementHdrDistinct(ProjectByIDRequest projHdr) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<getelementHdrDistinctResponse> finalResp = new ArrayList<getelementHdrDistinctResponse>();
			String mstId = iProjectDAO.getmstIdByPmHdrId(projHdr.getProjectID(), projHdr.getTenantID());
			if (!mstId.equalsIgnoreCase("")) {

				finalResp = iProjectDAO.getelementHdrDistinctList(mstId, projHdr.getKeyCode(), projHdr.getTenantID());
			}
			if (finalResp.size() > 0) {
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(finalResp);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(finalResp);
			}

		} catch (Exception ex) {
			logger.error("Error getelementHdrDistinct " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getSubAreaPmHdrList(GetSubAreaPmHdrListRequest getSubAreaPmHdrListreq) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<SubAreaPmHdrListEntity> finalResp = iProjectDAO.getsubAreaPmHdrList(
					getSubAreaPmHdrListreq.getPmHdrId(), getSubAreaPmHdrListreq.getPkId(),
					getSubAreaPmHdrListreq.getTenantId());
			if (finalResp.size() > 0) {
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(finalResp);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(finalResp);
			}

		} catch (Exception ex) {
			logger.error("Error getelementHdrDistinct " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getsalesBudgetExtnDtl(getsalesBudgetExtnDtlRequest getsalesBudgetExtnDtlReq) {
		ResponseAsList list = new ResponseAsList();
		List<SalesBudgetExtnDtlEntity> finalResp = new ArrayList<SalesBudgetExtnDtlEntity>();
		try {
			String mstId = iProjectDAO.getmstIdByPmHdrId(getsalesBudgetExtnDtlReq.getPmHdrId(),
					getsalesBudgetExtnDtlReq.getTenantId());
			if (!mstId.equalsIgnoreCase("")) {
				finalResp = iProjectDAO.getsalesBudgetExtnDtl(mstId, getsalesBudgetExtnDtlReq.getElementDesc(),
						getsalesBudgetExtnDtlReq.getTenantId(), getsalesBudgetExtnDtlReq.getKeyCode());
				if (finalResp.size() > 0) {
					list.setResponseCode(ResponseMessageMap.success);
					list.setResponseMessage(ResponseMessageMap.responseCodeOk);
					list.setResponseData(finalResp);
				} else {
					list.setResponseCode(ResponseMessageMap.responseCodeOk);
					list.setResponseMessage(ResponseMessageMap.noRecord);
					list.setResponseData(finalResp);
				}
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(finalResp);
			}
		} catch (Exception ex) {
			logger.error("Error getelementHdrDistinct " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage deleteSubAreaExtn(DeleteSubAreaExtnRequest deleteSubAreaExtReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			// int indentBudgetDtlCount =
			// iProjectDAO.countIndentBudgetCount(deleteSubAreaExtReq.getPkseId());
			// if (indentBudgetDtlCount == 0) {
			List<ProjectSubAreaExtnEntity> subArea = iProjectDAO
					.getProjectSubAreaExtnRowMapper(deleteSubAreaExtReq.getPkseId());
			if (subArea.size() > 0) {
				//BigDecimal getTotalallocatedVal = iProjectDAO.getTotalallocatedVal(subArea.get(0).getPkaId());
				String costFlowType = iProjectDAO.getCostFlowTypeByPkaId(subArea.get(0).getPkaId());
				boolean blockDelete;
				if ("NEW".equalsIgnoreCase(costFlowType)) {
					String pkaId = subArea.get(0).getPkaId();
					BigDecimal stationAllocated = new BigDecimal(iProjectDAO.getAllocatedValSum(pkaId));
					BigDecimal thisRowValue = new BigDecimal(subArea.get(0).getAllocateVal());
					BigDecimal approvedPoTotal = new BigDecimal(iPoDAO.getApprovedPoTotalByPkaId(pkaId));
					String scsSeq = iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", deleteSubAreaExtReq.getTenantId());
					String capexScsSeq = iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", deleteSubAreaExtReq.getTenantId());
					String minSeqNo = new BigDecimal(scsSeq).min(new BigDecimal(capexScsSeq)).toString();
					BigDecimal otherCommittedPjs = new BigDecimal(
							iIndentGroupDAO.getOtherCommittedScsTotalByPkaId(pkaId, "-1", minSeqNo));
					BigDecimal remainingAfterRemoval = stationAllocated.subtract(thisRowValue)
							.subtract(approvedPoTotal).subtract(otherCommittedPjs);
					blockDelete = remainingAfterRemoval.compareTo(BigDecimal.ZERO) < 0;
				} else {
					blockDelete = iProjectDAO.getIndentBudgetCheck(subArea.get(0).getPkaId(), subArea.get(0).getSbExtnId()) != 0;
				}
				if (!blockDelete) {
					iProjectDAO.updateBudgetExtn(subArea.get(0).getSbExtnId(), subArea.get(0).getAllocatedQty(),
							subArea.get(0).getAllocateVal());

					int deleteCount = iProjectDAO.deleteSubAreaExtn(deleteSubAreaExtReq.getPkseId());
					String allocatedValue = iProjectDAO.getAllocatedValSum(subArea.get(0).getPkaId());
					String budgetValue = iProjectDAO.getBudgetValSum(subArea.get(0).getPkaId());
					iProjectDAO.updateAllocatedAndBudgetVal(allocatedValue, budgetValue, subArea.get(0).getPkaId());
					if (deleteCount > 0) {
						String pmHdrId = iProjectDAO.getpmHdrIdByPkaId(subArea.get(0).getPkaId());
						List<String> messageList = new ArrayList<>();
						List<String> otherEmp = new ArrayList<>();
						String projCode =iProjectDAO.getProjCodeByProjId(pmHdrId,deleteSubAreaExtReq.getTenantId());
						String wbsDesc = iProjectDAO.getprojKeyMstDesc(subArea.get(0).getPkaId());
						messageList.add("Project "+projCode);
						messageList.add(wbsDesc);	
						commonNotifyMethod.InvokeNotificationMethod(2, 10, null, deleteSubAreaExtReq.getTenantId(), messageList, otherEmp, "1",deleteSubAreaExtReq.getPmId(), pmHdrId,null);
						
						rm.setResponseCode(ResponseMessageMap.responseCodeOk);
						rm.setResponseMessage(ResponseMessageMap.successfulDeleted);
					} else {
						rm.setResponseCode(ResponseMessageMap.failToupdateCode);
						rm.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
					}
				} else {
					rm.setResponseCode(ResponseMessageMap.failToupdateCode);
					rm.setResponseMessage(ResponseMessageMap.indentIsConsumed);
				}

			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
			}
			// } else {
			// rm.setResponseCode(ResponseMessageMap.failToupdateCode);
			// rm.setResponseMessage(ResponseMessageMap.indentAllocated);
			//
			// }

		} catch (Exception ex) {
			logger.error("Error deleteSubAreaExtn " + ex);
		}

		return rm;
	}

	@Override
	public ResponseAsMessage insertSubAreaExtn(List<InsertSubAreaExtnRequest> insertSubAreaExtnreq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			int insertCount = 0;
			int insertfinalCount = 0;
			for (int i = 0; i < insertSubAreaExtnreq.size(); i++) {
				iProjectDAO.updatesalesBudgetExtnval(insertSubAreaExtnreq.get(i).getSbExtnId(),
						insertSubAreaExtnreq.get(i).getAllocatedQty(), insertSubAreaExtnreq.get(i).getAllocatedvalue());
				insertCount = iProjectDAO.insertsubAreaExtn(insertSubAreaExtnreq.get(i).getPkaId(),
						insertSubAreaExtnreq.get(i).getSbExtnId(), insertSubAreaExtnreq.get(i).getAllocatedQty(),
						insertSubAreaExtnreq.get(i).getAllocatedvalue());
				insertfinalCount = insertfinalCount + insertCount;
				String allocatedValue = iProjectDAO.getAllocatedValSum(insertSubAreaExtnreq.get(i).getPkaId());
				String budgetValue = iProjectDAO.getBudgetValSum(insertSubAreaExtnreq.get(i).getPkaId());
				iProjectDAO.updateAllocatedAndBudgetVal(allocatedValue, budgetValue,
						insertSubAreaExtnreq.get(i).getPkaId());
			}

			logger.debug("insertfinalCount  " + insertfinalCount);
			logger.debug("insertSubAreaExtnreq  " + insertSubAreaExtnreq);
			if (insertfinalCount == insertSubAreaExtnreq.size()) {
				String pmHdrId = iProjectDAO.getpmHdrIdByPkaId(insertSubAreaExtnreq.get(0).getPkaId());
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				String projCode =iProjectDAO.getProjCodeByProjId(pmHdrId,insertSubAreaExtnreq.get(0).getTenantId());
				String wbsDesc = iProjectDAO.getprojKeyMstDesc(insertSubAreaExtnreq.get(0).getPkaId());
				messageList.add("Project "+projCode);
				messageList.add(wbsDesc);	
				commonNotifyMethod.InvokeNotificationMethod(2, 10, null, insertSubAreaExtnreq.get(0).getTenantId(), messageList, otherEmp, "1",insertSubAreaExtnreq.get(0).getPmId(), pmHdrId,null);
				
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseMessage(ResponseMessageMap.sucessExecuted);
			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("Error insertSubAreaExtn " + ex);
		}

		return rm;
	}

	@Override
	public ResponseAsList getbugetextnListbyDSkId(GetbugetextnListbyDSkIdRequest getbugetextnListbyDSkIdReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<SalesBudgetExtnListDtlEntity> finalResp = iProjectDAO.getbugetextnListbyDSkId(
					getbugetextnListbyDSkIdReq.getPmHdrId(), getbugetextnListbyDSkIdReq.getPskId(),
					getbugetextnListbyDSkIdReq.getTenantId());
			if (finalResp.size() > 0) {
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(finalResp);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(finalResp);
			}

		} catch (Exception ex) {
			logger.error("Error getelementHdrDistinct " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getbugetextnListbyDSkId(GetindentbudgetextValbyPSkIdRequest getindentbudgetextValbyPSkIdReq) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<GetbugetextnListbyDSkIdResponse> respList = new ArrayList<GetbugetextnListbyDSkIdResponse>();
			GetbugetextnListbyDSkIdResponse resp = new GetbugetextnListbyDSkIdResponse();

			BigDecimal indentQty = BigDecimal.ZERO;
			BigDecimal subAreaQty = BigDecimal.ZERO;
			BigDecimal indentVal = BigDecimal.ZERO;
			BigDecimal subAreaVal = BigDecimal.ZERO;
			List<IndentBudgetDtlEntity> indentList = iProjectDAO.getindentBudgetDtlList(
					getindentbudgetextValbyPSkIdReq.getPskId(), getindentbudgetextValbyPSkIdReq.getPkseId(),
					getindentbudgetextValbyPSkIdReq.getTenantId());

			for (int i = 0; i < indentList.size(); i++) {
				if (indentList.size() > 0) {
					indentQty = new BigDecimal(indentList.get(i).getAllocatedQty());
					indentVal = new BigDecimal(indentList.get(i).getAllocatedVal());
				}
				List<ProjectSubAreaExtnEntity> prjSubArea = iProjectDAO
						.getProjectSubAreaExtnRowMapper(getindentbudgetextValbyPSkIdReq.getPkseId());
				if (prjSubArea.size() > 0) {
					subAreaQty = new BigDecimal(prjSubArea.get(i).getAllocatedQty());
					subAreaVal = new BigDecimal(prjSubArea.get(i).getAllocateVal());
				}
				resp.setTotalQty((indentQty.subtract(subAreaQty)).toString());
				resp.setTotalVal((indentVal.subtract(subAreaVal)).toString());
				respList.add(resp);
			}
			if (respList.size() > 0) {
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(respList);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(respList);
			}

		} catch (Exception ex) {
			logger.error("Error getbugetextnListbyDSkId " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage insertindentBudget(List<IndentBudgetDtlEntity> indentBudgetReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			// int insertCount = 0;
			// int insertfinalCount = 0;
			// for (int i = 0; i < indentBudgetReq.size(); i++) {
			// insertCount =
			// iProjectDAO.insertindentBudgetDtl(indentBudgetReq.get(i).getIndentDtlId(),
			// indentBudgetReq.get(i).getPkseId(), indentBudgetReq.get(i).getAllocatedQty(),
			// indentBudgetReq.get(i).getAllocatedVal());
			// insertfinalCount = insertfinalCount + insertCount;
			// String
			// indenyId=iProjectDAO.getindentHdrId(indentBudgetReq.get(i).getIndentDtlId());
			// indentUploadDAO.updateExpectedDeliveryDate(indenyId,
			// indentBudgetReq.get(i).getExpectedDeliveryDate());
			// }
			// logger.debug("insertfinalCount " + insertfinalCount);
			// logger.debug("indentBudgetReq " + indentBudgetReq);
			// if (insertfinalCount == indentBudgetReq.size()) {
			// rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			// rm.setResponseMessage(ResponseMessageMap.successUpload);
			// } else {
			// rm.setResponseCode(ResponseMessageMap.failToupdateCode);
			// rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			// }

		} catch (Exception ex) {
			logger.error("Error insertindentBudget " + ex);
		}

		return rm;
	}

	@Override
	public ResponseAsMessage totalSubAreaValueByPskId(GetSubAreaPmHdrListRequest getSubAreaPmHdrListreq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		BigDecimal qResp = iProjectDAO.getTotalSubExtnVal(getSubAreaPmHdrListreq.getPkaId(),
				getSubAreaPmHdrListreq.getPmHdrId(), getSubAreaPmHdrListreq.getTenantId());

		rm.setResponseCode(ResponseMessageMap.responseCodeOk);
		rm.setResponseMessage(ResponseMessageMap.successUpdated);
		rm.setResponseDataMessage(qResp.toString());

		return rm;
	}

	@Override
	public ResponseAsList getindentbudgetDtlbyindentDtlId(
			GetindentbudgetDtlbyindentDtlIdRequest getindentbudgetDtlbyindentDtlIdReq) {
		List<GetindentbudgetDtlEntity> dtlList = iProjectDAO
				.getindentbudgetDtl(getindentbudgetDtlbyindentDtlIdReq.getIndentDtlId());

		ResponseAsList respLi = new ResponseAsList();
		if (dtlList.size() > 0) {

			respLi.setResponseCode(ResponseMessageMap.success);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
			respLi.setResponseData(dtlList);
		} else {
			respLi.setResponseCode(ResponseMessageMap.noRecord);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		return respLi;
	}

	@Override
	public ResponseAsMessage deleteIndentBudgetId(DeleteIndentBudgetIdRequest deleteIndentBudgetIdReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			List<IndentBudgetDtlEntity> indentBudDtl = iProjectDAO
					.getindentBudgetDtlById(deleteIndentBudgetIdReq.getIndentBudId());
			List<ProjectSubAreaExtnEntity> subArea = iProjectDAO
					.getProjectSubAreaExtnRowMapper(indentBudDtl.get(0).getPkseId());
			iProjectDAO.updateBudgetQtyAndval(subArea.get(0).getPkseId(), indentBudDtl.get(0).getAllocatedQty(),
					indentBudDtl.get(0).getAllocatedVal());

			int deleteCount = iProjectDAO.deleteindentBudGetId(deleteIndentBudgetIdReq.getIndentBudId());
			if (subArea.size() > 0) {
				if (deleteCount > 0) {
					rm.setResponseCode(ResponseMessageMap.responseCodeOk);
					rm.setResponseMessage(ResponseMessageMap.successfulDeleted);
				} else {
					rm.setResponseCode(ResponseMessageMap.failToupdateCode);
					rm.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
				}
			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
			}

		} catch (Exception ex) {
			logger.error("Error deleteSubAreaExtn " + ex);
		}

		return rm;

	}

	@Override
	public ResponseAsList getProjTimePlanDropDown(TenantRequest tenantReq) {
		List<GetProjTimePlanDropDownEntity> dtlList = iProjectDAO.getProjTimePlanDropDown(tenantReq.getTenantID());

		ResponseAsList respLi = new ResponseAsList();
		if (dtlList.size() > 0) {

			respLi.setResponseCode(ResponseMessageMap.success);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
			respLi.setResponseData(dtlList);
		} else {
			respLi.setResponseCode(ResponseMessageMap.noRecord);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		return respLi;
	}

	@Override
	public ResponseAsList getExistingPMtemplateByPmHdrId(PmHdrIdAndTenantIdRequest pmHdrIdandTenantIdReq) {
		ResponseAsList respLi = new ResponseAsList();
		try {
			List<ProjectTimelineResp> pmTimeLinelist = iProjectDAO
					.getTimeLineOrdByDate(pmHdrIdandTenantIdReq.getPmHdrId(), pmHdrIdandTenantIdReq.getTenantId());
			// List<ProjectTimelineResp> pmTimeLinefinalList = new
			// ArrayList<ProjectTimelineResp>();
			// ProjectTimelineResp pmtimeLine= new ProjectTimelineResp();
			// pmTimeLinefinalList =pmTimeLinelist;
			for (int i = 0; i < pmTimeLinelist.size(); i++) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(pmTimeLinelist.get(i).getPlannedStartDate());
				Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(pmTimeLinelist.get(i).getPlannedEndDate());
				long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
				int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

				if (i == 0) {
					pmTimeLinelist.get(i).setGeneratedStartDate(CommonMethod.getCurrentDate());
				} else {
					Date prevStartDate = new SimpleDateFormat("yyyy-MM-dd")
							.parse(pmTimeLinelist.get(i - 1).getPlannedStartDate());
					long fromdateDiffInMillies = Math.abs(startDate.getTime() - prevStartDate.getTime());
					int fromdateDiff = (int) TimeUnit.DAYS.convert(fromdateDiffInMillies, TimeUnit.MILLISECONDS);
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);

					cal.add(Calendar.DATE, fromdateDiff);
					pmTimeLinelist.get(i).setGeneratedStartDate(sdf.format(cal.getTime()));
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(pmTimeLinelist.get(i).getGeneratedStartDate()));

				cal.add(Calendar.DATE, diff);
				pmTimeLinelist.get(i).setGeneratedEndDate(sdf.format(cal.getTime()));
				// pmTimeLinefinalList.add(pmtimeLine);
			}

			if (pmTimeLinelist.size() > 0) {

				respLi.setResponseCode(ResponseMessageMap.success);
				respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
				respLi.setResponseData(pmTimeLinelist);
			} else {
				respLi.setResponseCode(ResponseMessageMap.noRecord);
				respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			}
		} catch (Exception e) {
			logger.error("getExistingPMtemplateByPmHdrId Error" + e);
		}
		return respLi;
	}

	@Override
	public ResponseAsMessage updateProjectPlanDate(UpdateProjectPlanDateRequest updateProjectPlanDateReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			if (updateProjectPlanDateReq.getPriority().equalsIgnoreCase("High")) {
				updateProjectPlanDateReq.setPriority("3");
			} else if (updateProjectPlanDateReq.getPriority().equalsIgnoreCase("Medium")) {
				updateProjectPlanDateReq.setPriority("2");
			} else {
				updateProjectPlanDateReq.setPriority("1");
			}
			int updateprojectPlandate = iProjectDAO.updateProjPlanDate(updateProjectPlanDateReq.getPmHdrId(),
					updateProjectPlanDateReq.getPmPlanDate(), updateProjectPlanDateReq.getPmEndDate(),
					updateProjectPlanDateReq.getTenantId(), updateProjectPlanDateReq.getPriority());

			if (updateprojectPlandate > 0) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseDataMessage(ResponseMessageMap.successMsg);
				rm.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseDataMessage(ResponseMessageMap.failMsg);
				rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("Error updateProjectPlanDate " + ex);
		}

		return rm;
	}

	@Override
	public ResponseAsMessage updateDesignHdr(UpdateDesignHdrRequest updateDesignHdrRequest) {
		ResponseAsMessage rm = new ResponseAsMessage();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {

				String projectCode=iProjectDAO.getProjCodeByProjId(updateDesignHdrRequest.getPmHdrId(), updateDesignHdrRequest.getTenantId());
				messageList.add(projectCode);
				otherEmpId=iProjectDAO.getAssignedMembersForProject(updateDesignHdrRequest.getPmHdrId(),updateDesignHdrRequest.getTenantId());
				logger.info("otheremp " + otherEmpId);
			int resp = iProjectDAO.updateDesignHdr(updateDesignHdrRequest,messageList,otherEmpId);

			if (resp == 0 || resp == 1) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseDataMessage(String.valueOf(resp));
				rm.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseDataMessage(ResponseMessageMap.failMsg);
				rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("Error updateDesignHdr " + ex);
		}

		return rm;
	}

	@Override
	public ResponseAsMessage getProjectInitiationMstResp(ProjectInitiationMstRequest projectInitiation) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {

			String resp = iProjectDAO.getProjectInitiationMstResp(projectInitiation,projectInitiation.getTenantId());

			if (resp.equals("1") || resp.equals("0")) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseDataMessage(String.valueOf(resp));
				rm.setResponseMessage(ResponseMessageMap.success);
			} else {
				rm.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				rm.setResponseDataMessage(ResponseMessageMap.failMsg);
				rm.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("Error getProjectInitiationMstResp " + ex);
		}

		return rm;
	}

	@Override
	public ResponseAsMessage updateAssyMstResp(AssyMstRequest assyMstRequest) {
		ResponseAsMessage rm = new ResponseAsMessage();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {

			String projectCode=iProjectDAO.getProjCodeByProjId(assyMstRequest.getPmHdrId(), assyMstRequest.getTenantId());
			messageList.add(projectCode);
			otherEmpId=iProjectDAO.getAssignedMembersForProject(assyMstRequest.getPmHdrId(),assyMstRequest.getTenantId());
			int resp = iProjectDAO.updateAssyMstResp(assyMstRequest,otherEmpId,messageList);

			if (resp == 0 || resp == 1) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseDataMessage(String.valueOf(resp));
				rm.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseDataMessage(ResponseMessageMap.failMsg);
				rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("Error updateAssyMstResp " + ex);
		}

		return rm;
	}

	@Override
	public ResponseAsMessage updateQCbuyoff(AssyMstRequest assyMstRequest) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {

			int resp = iProjectDAO.updateQCbuyoff(assyMstRequest);

			iProjectDAO.UpdateQCStatus(assyMstRequest.getPmHdrId(), assyMstRequest.getTenantId());

			if (resp == 0 || resp == 1) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseDataMessage(String.valueOf(resp));
				rm.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseDataMessage(ResponseMessageMap.failMsg);
				rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("Error updateQCbuyoff " + ex);
		}

		return rm;
	}

	@Override
	public ResponseAsList getTimeTrackerByProjectId(ProjectByIDRequest projHdr) {
		List<ProjectTimelineEntity> newRespList = new ArrayList<>();
		List<GetTaskEntryDtlEntity> taskList = new ArrayList<>();
		String typeCode = "getAll";
		String categoryCode = "getAll";
		String dependentId = "";
		String tenantId = projHdr.getTenantID();

		List<ProjectTimelineEntity> resp = iProjectDAO.getTimeTrackerByProjectId(projHdr);
		if (resp.size() > 0) {
			for (ProjectTimelineEntity projObj : resp) {
				ProjectTimelineEntity projectTimeline = new ProjectTimelineEntity();
				BeanUtils.copyProperties(projObj, projectTimeline);
				// StartDate
				String startDate = iProjectDAO.getStartDateByProjId(projHdr.getProjectID(), projHdr.getTenantID(),
						projObj.getDepartmentCode());
				// EndDate
				String endDate = iProjectDAO.getEndDateByProjId(projHdr.getProjectID(), projHdr.getTenantID(),
						projObj.getDepartmentCode());

				projectTimeline.setActualStartDate(startDate);
				projectTimeline.setActualEndDate(endDate);

				taskList = iProjectDAO.getdesignTaskDtlByProjectId(typeCode, categoryCode, projHdr.getProjectID(),
						tenantId, dependentId, projObj.getDepartmentCode());
				if (taskList.size() > 0) {
					projectTimeline.setTaskEntryDtlEntity(taskList);
				}
				newRespList.add(projectTimeline);
			}
		}
		ResponseAsList respLi = new ResponseAsList();
		if (resp.size() > 0) {

			respLi.setResponseCode(ResponseMessageMap.success);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeOk);
			respLi.setResponseData(newRespList);
		} else {
			respLi.setResponseCode(ResponseMessageMap.noRecord);
			respLi.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
		}
		return respLi;
	}

	@Override
	public ResponseAsMessage updateBudgetSheetPaymentTerms(updateBsPaymentTermsRequest bsPaymentRequest) {
		ResponseAsMessage resp = new ResponseAsMessage();
		try {
			 int totalUpdated = 0;
             for(BsPaymentTermsRequest term : bsPaymentRequest.getPaymentTerms()) {
            	 int update = iProjectDAO.updateBudgetSheetPaymentTerms(term.getSbPtId(), term.getActualDate(), term.getRemarks());
            	 if (update == 1) {
                     totalUpdated++;
                 }
             }
			if (totalUpdated > 1) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseDataMessage(String.valueOf(totalUpdated));
				resp.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				resp.setResponseCode(ResponseMessageMap.failToupdateCode);
				resp.setResponseDataMessage(ResponseMessageMap.failMsg);
				resp.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("Error updateBudgetSheetPaymentTerms " + ex);
		}

		return resp;
	}
}
