package com.vmfg.general.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.assembly.dao.impl.AssemblyDAO;
import com.vmfg.assembly.dao.interfaces.IAssemblyDAO;
import com.vmfg.assembly.entity.GetAssyDtlEntity;
import com.vmfg.assembly.request.GetAssyDtlRequest;
import com.vmfg.design.dao.impl.ChangeRequestDAO;
import com.vmfg.design.dao.impl.DesignDAO;
import com.vmfg.design.dao.interfaces.IDesignDAO;
import com.vmfg.design.entity.GetPlanAndActualEntity;
import com.vmfg.design.request.DesignRequest;
import com.vmfg.design.response.DesignHdr;
import com.vmfg.finance.entity.FinanceHdrEntity;
import com.vmfg.general.dao.interfaces.ITileViewDAO;
import com.vmfg.general.response.GetTileViewResponse;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.services.interfaces.ITileViewService;
import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.project.dao.interfaces.IBudgetExcessSheetDAO;
import com.vmfg.project.dao.interfaces.IProjectDAO;
import com.vmfg.scm.dao.interfaces.IIndentGroupDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.project.entity.ProjectHdr;
import com.vmfg.project.request.ProjectHdrRequest;
import com.vmfg.quality.dao.impl.QualityDAO;
import com.vmfg.quality.entity.GetQtyDtlEntity;
import com.vmfg.quality.entity.RetieveQCInspectionHdrEntity;
import com.vmfg.quality.request.GetQtyDtlRequest;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.sales.entity.SalesEnqDtlEntity;
import com.vmfg.sales.request.GetEnqDtlbyDateRequest;
import com.vmfg.scm.entity.ScmHdrBasedDtlEntity;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;

@Service
public class TileViewService implements ITileViewService {
	private static final Logger logger = LoggerFactory.getLogger(TileViewService.class);

	@Autowired
	ITileViewDAO iTitleViewDAO;
	
	@Autowired
	ChangeRequestDAO ChangeRequestDAO;
	
	@Autowired
	IDesignDAO iDesignDAO;
	
	@Autowired
	DesignDAO designDAO;

	@Autowired
	UploadManagementDAO deptM;
	
	@Autowired
	IProjectDAO iProjectDAO;

	@Autowired
	IPoDAO iPoDAO;

	@Autowired
	IIndentGroupDAO iIndentGroupDAO;

	@Autowired
	IBudgetExcessSheetDAO iBudgetExcessSheetDAO;

	@Autowired
	IndentUploadDAO indentUploadDao;
	
	@Autowired
	IAssemblyDAO iAssyDAO;
	
	@Autowired
	AssemblyDAO assyDAO;
	
	@Autowired
	QualityDAO iQualityDAO;
	
	@Override
	public ResponseAsList getSaleTileView(GetEnqDtlbyDateRequest getEnqDtlbyDateReq) {
		ResponseAsList list = new ResponseAsList();
			logger.info("getSaleTileView  method start");
			List<GetTileViewResponse> respList =new ArrayList<GetTileViewResponse>();
		
			try {
				String fromDate=getEnqDtlbyDateReq.getFromDate();
				String toDate  =getEnqDtlbyDateReq.getToDate();
				String customerName=getEnqDtlbyDateReq.getCustomerName();
				String tenantId = getEnqDtlbyDateReq.getTenantId();
				String empId = getEnqDtlbyDateReq.getEmpId();
				
				List<String> statusDesc = 	iTitleViewDAO.getDistinctStatus("sales_enq_hdr");
			if(statusDesc.size()>0) {
			for(int i =0;i<statusDesc.size();i++) {
List<SalesEnqDtlEntity> saleEnqDtlList =iTitleViewDAO.saleEnqList(statusDesc.get(i),fromDate, toDate, customerName,tenantId,empId,getEnqDtlbyDateReq.getTentativePoVal(),getEnqDtlbyDateReq.getIsExpectedPoDate() );
GetTileViewResponse resp =new GetTileViewResponse();
resp.setEnqList(saleEnqDtlList);
resp.setStatusDesc(ChangeRequestDAO.getStatusDescByStatusCode(statusDesc.get(i), tenantId));
respList.add(resp);
			}
			list.setResponseData(respList);
			list.setResponseCode(ResponseMessageMap.success);
			list.setResponseMessage(ResponseMessageMap.responseCodeOk);
			}else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(respList);
			}
			} catch (Exception ex) {
				logger.error("getSaleTileView  method exception-->" + ex);
			}
			logger.debug("getSaleTileView  method end");
			return list;
	}

	@Override
	public ResponseAsList getDesignTitleView(DesignRequest designReq) {
		ResponseAsList list = new ResponseAsList();
		logger.info("getSaleTileView  method start");
		List<GetTileViewResponse> respList =new ArrayList<GetTileViewResponse>();
	
		try {
			String fromDate=designReq.getFromDate();
			String toDate  =designReq.getToDate();
			String customerName=designReq.getCustomer();
			String tenantId = designReq.getTenantID();
			String empId = designReq.getEmpId();
			
			List<String> statusDesc = 	iTitleViewDAO.getDistinctStatus("design_hdr");
		if(statusDesc.size()>0) {
		for(int i =0;i<statusDesc.size();i++) {
		
List<DesignHdr> designDtlList =iTitleViewDAO.designTitleViewList(statusDesc.get(i),fromDate, toDate, customerName,tenantId,designReq.getProjectId(),designReq.getProcessId(),empId);
GetTileViewResponse resp =new GetTileViewResponse();
designDtlList.forEach(designHdr -> {

	String dept = deptM.getDepCodeByEmpId(designReq.getEmpId(), designReq.getTenantID());
List<GetPlanAndActualEntity> taskPlanList = designDAO.getTaskPlannedGroupList(designHdr.getDesignID(), dept, designReq.getTenantID(), "");
if(taskPlanList.size()>0) {
	designHdr.setTaskPlan(taskPlanList.get(0).getPlanCount());
	designHdr.setTaskActual(taskPlanList.get(0).getActualCount());
}else {
	designHdr.setTaskPlan("0");
	designHdr.setTaskActual("0");
}

List<GetPlanAndActualEntity> indentPlanList =designDAO.getIndentPlannedGroupList(designHdr.getDesignID(), dept, designReq.getTenantID(), "");
	
if(taskPlanList.size()>0) {
	designHdr.setIndentPlan(indentPlanList.get(0).getPlanCount());
	designHdr.setIndentActual(indentPlanList.get(0).getActualCount());
}else {
	designHdr.setIndentPlan("0");
	designHdr.setIndentActual("0");
}


});
resp.setEnqList(designDtlList);
resp.setStatusDesc(ChangeRequestDAO.getStatusDescByStatusCode(statusDesc.get(i), tenantId));
respList.add(resp);
		}
		list.setResponseData(respList);
		list.setResponseCode(ResponseMessageMap.success);
		list.setResponseMessage(ResponseMessageMap.responseCodeOk);
		}else {
			list.setResponseCode(ResponseMessageMap.noRecord);
			list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			list.setResponseData(respList);
		}
		} catch (Exception ex) {
			logger.error("getDesignTitleView  method exception-->" + ex);
		}
		logger.debug("getDesignTitleView  method end");
		return list;
	}

	@Override
	public ResponseAsList getProjectTitleView(ProjectHdrRequest tenReq) {
		ResponseAsList list = new ResponseAsList();
		logger.info("getProjectTitleView  method start");
		List<GetTileViewResponse> respList =new ArrayList<GetTileViewResponse>();
	
		try {
			String fromDate=tenReq.getFromDate();
			String toDate  =tenReq.getToDate();
			String customerName=tenReq.getCustName();
			String tenantId = tenReq.getTenantID();
			String empId = tenReq.getEmpId();
			
			List<String> statusDesc = 	iTitleViewDAO.getDistinctStatus("project_hdr");
		if(statusDesc.size()>0) {
		for(int i =0;i<statusDesc.size();i++) {
			List<ProjectHdr> projectTitleViewList=iTitleViewDAO.projectTitleViewList(tenantId, customerName, fromDate, toDate, tenReq.getProjectID(), empId, tenReq.getPmId(), statusDesc.get(i));

			projectTitleViewList.forEach(projHdr -> {

				projHdr.setIndentPlan(iProjectDAO.getBudgetValue(projHdr.getPmHdrId(), tenReq.getTenantID()));
				projHdr.setIndentActual(iProjectDAO.getAllocValue(projHdr.getPmHdrId(), tenReq.getTenantID()));

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
					BigDecimal debitCost = new BigDecimal(iProjectDAO.getDebitVal(projHdr.getPmHdrId(), tenReq.getTenantID()));
					BigDecimal budgetExcessApproved = new BigDecimal(
							iBudgetExcessSheetDAO.getApprovedExcessTotalByPmHdrId(projHdr.getPmHdrId()));

					BigDecimal actualSpent = consumedSoFar.add(matCost).add(employeeCost).add(budgetExcessApproved)
							.subtract(debitCost);

					projHdr.setAllocatedValue(allocatedValue.toString());
					projHdr.setActualSpent(actualSpent.toString());
				}
			});
			
	GetTileViewResponse resp =new GetTileViewResponse();
resp.setEnqList(projectTitleViewList);
resp.setStatusDesc(ChangeRequestDAO.getStatusDescByStatusCode(statusDesc.get(i), tenantId));
respList.add(resp);
		}
		list.setResponseData(respList);
		list.setResponseCode(ResponseMessageMap.success);
		list.setResponseMessage(ResponseMessageMap.responseCodeOk);
		}else {
			list.setResponseCode(ResponseMessageMap.noRecord);
			list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			list.setResponseData(respList);
		}
		} catch (Exception ex) {
			logger.error("getProjectTitleView  method exception-->" + ex);
		}
		logger.debug("getProjectTitleView  method end");
		return list;
	}

	@Override
	public ResponseAsList getSCMTitleView(ScmHdrBasedDtlRequest scmHdrBasedDtl) {
		ResponseAsList list = new ResponseAsList();
		logger.info("getSaleTileView  method start");
		List<GetTileViewResponse> respList =new ArrayList<GetTileViewResponse>();
	
		try {
			
			List<String> statusDesc = 	iTitleViewDAO.getDistinctStatus("scm_hdr");
		if(statusDesc.size()>0) {
		for(int i =0;i<statusDesc.size();i++) {
			List<ScmHdrBasedDtlEntity> scmEnqDtlList =iTitleViewDAO.scmTitleViewList(scmHdrBasedDtl,statusDesc.get(i));
			GetTileViewResponse resp =new GetTileViewResponse();
			resp.setEnqList(scmEnqDtlList);
			resp.setStatusDesc(ChangeRequestDAO.getStatusDescByStatusCode(statusDesc.get(i), scmHdrBasedDtl.getTenantId()));
			respList.add(resp);
		}
		list.setResponseData(respList);
		list.setResponseCode(ResponseMessageMap.success);
		list.setResponseMessage(ResponseMessageMap.responseCodeOk);
		}else {
			list.setResponseCode(ResponseMessageMap.noRecord);
			list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			list.setResponseData(respList);
		}
		} catch (Exception ex) {
			logger.error("getSCMTitleView  method exception-->" + ex);
		}
		logger.debug("getSCMTitleView  method end");
		return list;
	}

	@Override
	public ResponseAsList getFinanceTitleView(DesignRequest designReq) {
		ResponseAsList list = new ResponseAsList();
		logger.info("getFinanceTitleView  method start");
		List<GetTileViewResponse> respList =new ArrayList<GetTileViewResponse>();
	
		try {
			
			List<String> statusDesc= 	iTitleViewDAO.getDistinctStatus("finance_hdr");
		if(statusDesc.size()>0) {
		for(int i =0;i<statusDesc.size();i++) {
			List<FinanceHdrEntity> financeEnqDtlList =iTitleViewDAO.financeTitleViewList(designReq.getFromDate(),designReq.getToDate(),designReq.getCustomer(),designReq.getProcessId(),designReq.getEmpId(),designReq.getTenantID(),designReq.getDesignID(),designReq.getProjectId(),statusDesc.get(i));
			GetTileViewResponse resp =new GetTileViewResponse();
			resp.setEnqList(financeEnqDtlList);
			resp.setStatusDesc(ChangeRequestDAO.getStatusDescByStatusCode(statusDesc.get(i), designReq.getTenantID()));
			respList.add(resp);
		}
		list.setResponseData(respList);
		list.setResponseCode(ResponseMessageMap.success);
		list.setResponseMessage(ResponseMessageMap.responseCodeOk);
		}else {
			list.setResponseCode(ResponseMessageMap.noRecord);
			list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			list.setResponseData(respList);
		}
		} catch (Exception ex) {
			logger.error("getFinanceTitleView  method exception-->" + ex);
		}
		logger.debug("getFinanceTitleView  method end");
		return list;
	}

	@Override
	public ResponseAsList getAssyTitleView(GetAssyDtlRequest getAssyDtlReq) {
		ResponseAsList list = new ResponseAsList();
		logger.info("getAssyTitleView  method start");
		List<GetTileViewResponse> respList =new ArrayList<GetTileViewResponse>();
	
		try {
			
			List<String> statusDesc = 	iTitleViewDAO.getDistinctStatus("assy_hdr");
		if(statusDesc.size()>0) {
		for(int j =0;j<statusDesc.size();j++) {
			List<GetAssyDtlEntity> getAssyDtl =iTitleViewDAO.assyTitleViewList(getAssyDtlReq.getFromDate(),getAssyDtlReq.getToDate(),getAssyDtlReq.getCustName(),getAssyDtlReq.getAssyId(),getAssyDtlReq.getTenantID(),getAssyDtlReq.getPmId(),getAssyDtlReq.getEmpId(),getAssyDtlReq.getProjectId(),statusDesc.get(j));
			for (int i = 0; i < getAssyDtl.size(); i++) {
				
				List<GetPlanAndActualEntity> taskPlanList  = assyDAO.getIndentAssyPlannedGroupList(getAssyDtl.get(i).getPmHdrId(), getAssyDtl.get(i).getTenantId());
						
					if(taskPlanList.size()>0) {
						getAssyDtl.get(i)
						.setIndentCount(taskPlanList.get(0).getPlanCount());
						getAssyDtl.get(i).setIndentIsCompletedCount(taskPlanList.get(0).getActualCount());
						getAssyDtl.get(i).setIsInternal(assyDAO.getIsInternalOrNot(getAssyDtl.get(i).getPmHdrId()));
				}else {
					getAssyDtl.get(i)
					.setIndentCount("0");
					getAssyDtl.get(i).setIndentIsCompletedCount("0");
					getAssyDtl.get(i).setIsInternal(assyDAO.getIsInternalOrNot(getAssyDtl.get(i).getPmHdrId()));
				}
					List<GetPlanAndActualEntity> mrPlanList  = assyDAO.getMaterialReqAssyGroupList(getAssyDtl.get(i).getPmHdrId(), getAssyDtl.get(i).getTenantId());
					
					if(mrPlanList.size()>0) {
						getAssyDtl.get(i)
						.setMaterialRequestHdrCount(mrPlanList.get(0).getPlanCount());
						getAssyDtl.get(i).setMaterialRequestIsCompletedCount(mrPlanList.get(0).getActualCount());
				}else {
					getAssyDtl.get(i)
					.setMaterialRequestHdrCount("0");
					getAssyDtl.get(i).setMaterialRequestIsCompletedCount("0");
				}

	//			getAssyDtl.get(i)
	//					.setIndentCount(Integer.toString(iAssyDAO.getindentcount(getAssyDtl.get(i).getPmHdrId(), "0")));
	//			getAssyDtl.get(i).setIndentIsCompletedCount(
	//					Integer.toString(iAssyDAO.getindentcount(getAssyDtl.get(i).getPmHdrId(), "1")));
	//			getAssyDtl.get(i).setMaterialRequestHdrCount(
	//					Integer.toString(iAssyDAO.getMaterialReqHdrCount(getAssyDtl.get(i).getPmHdrId(), "0")));
	//			getAssyDtl.get(i).setMaterialRequestIsCompletedCount(
	//					Integer.toString(iAssyDAO.getMaterialReqHdrCount(getAssyDtl.get(i).getPmHdrId(), "1")));
			}
			
			GetTileViewResponse resp =new GetTileViewResponse();
			resp.setEnqList(getAssyDtl);
			resp.setStatusDesc(ChangeRequestDAO.getStatusDescByStatusCode(statusDesc.get(j), getAssyDtlReq.getTenantID()));
			respList.add(resp);
		}
		list.setResponseData(respList);
		list.setResponseCode(ResponseMessageMap.success);
		list.setResponseMessage(ResponseMessageMap.responseCodeOk);
		}else {
			list.setResponseCode(ResponseMessageMap.noRecord);
			list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
			list.setResponseData(respList);
		}
		} catch (Exception ex) {
			logger.error("getAssyTitleView  method exception-->" + ex);
		}
		logger.debug("getAssyTitleView  method end");
		return list;
	}

	@Override
	public ResponseAsList getQualityView(GetQtyDtlRequest getQtyDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetQtyDtlEntity> list = new ArrayList<GetQtyDtlEntity>();
		List<GetTileViewResponse> respList =new ArrayList<GetTileViewResponse>();

		try {
			List<String> statusDesc = 	iTitleViewDAO.getDistinctStatus("quality_hdr");
			if(statusDesc.size()>0) {
				for(int j=0;j<statusDesc.size();j++) {
					
					list = iTitleViewDAO.getQtyDtltile(getQtyDtlReq.getQHdrId(), getQtyDtlReq.getEmpId(), getQtyDtlReq.getFromDate(),
							getQtyDtlReq.getToDate(), getQtyDtlReq.getTenantId(), getQtyDtlReq.getCustomerName(),
							getQtyDtlReq.getPmId(),getQtyDtlReq.getProjectId(),statusDesc.get(j));
				
				
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setQtyinspectionCompleted(iQualityDAO.getQtyinspCompleted(list.get(i).getPmHdrId(),getQtyDtlReq.getTenantId()));
					list.get(i).setQtyinspectionTotal(iQualityDAO.getCountofinsp(list.get(i).getPmHdrId(),getQtyDtlReq.getTenantId()));
					List<RetieveQCInspectionHdrEntity> inspectQuality = iQualityDAO
							.getQiCountsByPmHdrId(list.get(i).getPmHdrId(), getQtyDtlReq.getTenantId());
					list.get(i).setOkCount(inspectQuality.get(0).getOkCount());
					list.get(i).setRejectedCount(inspectQuality.get(0).getRejectedCount());
					list.get(i).setConditionalApprovedCnt(inspectQuality.get(0).getConditionalCnt());
					list.get(i).setReworkCount(inspectQuality.get(0).getReworkCount());
					list.get(i).setQtyToBeInspected(inspectQuality.get(0).getQtyToBeInspected());
					list.get(i).setQtyTotalCompleted(inspectQuality.get(0).getQtyInspectionCompleted());
				}
				GetTileViewResponse resp =new GetTileViewResponse();
				resp.setEnqList(list);
				resp.setStatusDesc(ChangeRequestDAO.getStatusDescByStatusCode(statusDesc.get(j), getQtyDtlReq.getTenantId()));
				respList.add(resp);
				
				}
				returnList.setResponseData(respList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			}

			 else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getQualityView service error " + ex);
		}
		return returnList;
	}


}
