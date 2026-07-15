package com.vmfg.project.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.design.response.KeyArea;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.entity.BudgetSheetPaymentEntity;
import com.vmfg.project.entity.GetProjTimePlanDropDownEntity;
import com.vmfg.project.entity.GetindentbudgetDtlEntity;
import com.vmfg.project.entity.IndentBudgetDtlEntity;
import com.vmfg.project.entity.ProjectHdr;
import com.vmfg.project.entity.ProjectSubAreaExtnEntity;
import com.vmfg.project.entity.ProjectTimelineEntity;
import com.vmfg.project.entity.ProjectTimelineResp;
import com.vmfg.project.entity.ProjectWBSTemplate;
import com.vmfg.project.entity.SalesBudgetExtnDtlEntity;
import com.vmfg.project.entity.SalesBudgetExtnListDtlEntity;
import com.vmfg.project.entity.SubAreaPmHdrListEntity;
import com.vmfg.project.entity.SumOfIndentHdrEntity;
import com.vmfg.project.entity.getLinkStatusByPMIdRespEntity;
import com.vmfg.project.request.AssyMstRequest;
import com.vmfg.project.request.DeleteTimeWBSByIDRequest;
import com.vmfg.project.request.KeyAreaDelRequest;
import com.vmfg.project.request.KeyAreaRequest;
import com.vmfg.project.request.ProjectByIDRequest;
import com.vmfg.project.request.ProjectInitiationMstRequest;
import com.vmfg.project.request.ProjectTimelineRequest;
import com.vmfg.project.request.UpdateDesignHdrRequest;
import com.vmfg.project.request.WbsIDRequest;
import com.vmfg.project.response.ProjectInternalResponse;
import com.vmfg.project.response.getelementHdrDistinctResponse;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;

public interface IProjectDAO {

	List<ProjectHdr> getProjectDtl(String tenantId, String custName, String fromDate, String toDate, String projectID,
			String empId, String pmId);

	List<ProjectWBSTemplate> getWbsTemplate(TenantRequest tenantReq);

	ProjectInternalResponse getIsInternalOrNot(String tenantId, String projectCode);

	ProjectInternalResponse getProjectInternal(String tenantId, String projectCode);

	List<ProjectWBSTemplate> getWbsTemplateById(WbsIDRequest wbsReq);

	int insertUpdateProjectMilestone(ProjectTimelineRequest projTimeReq);

	ResponseAsMessage deleteWBSById(DeleteTimeWBSByIDRequest deleteById);

	List<ProjectTimelineResp> getTimeLineByPM(ProjectByIDRequest projHdr);

	int insertKeyAreaByPMId(KeyAreaRequest pt);

	ResponseAsMessage deleteWBSById(KeyAreaDelRequest delReq);

	List<KeyArea> getPKForProj(ProjectByIDRequest projHdr);

	int updateDesignIndentstart(String deHdrId, String tenantId);

	getLinkStatusByPMIdRespEntity linkStatusCount(String pmHdrId, String pkaId, String tenantId);

	List<SumOfIndentHdrEntity> getSumOfIndentHdrEntity(String pkaId);

	List<getelementHdrDistinctResponse> getelementHdrDistinctList(String mstId, String keyCodeString, String tenantId);

	List<SubAreaPmHdrListEntity> getsubAreaPmHdrList(String pmHdrId, String dskId, String tenantId);

	List<SalesBudgetExtnDtlEntity> getsalesBudgetExtnDtl(String mstId, String elementdesc, String tenantId,
			String keyCode);

	List<SalesBudgetExtnListDtlEntity> getbugetextnListbyDSkId(String pmHdrId, String pskId, String tenantId);

	int deleteSubAreaExtn(String pkseId);

	int deleteindentBudGetId(String indentId);

	int insertsubAreaExtn(String dskId, String sbExtnId, String allocatedQty, String allocatedValue);

	int insertAreaExtn(String pkaId, String sbExtnId, String allocatedQty, String allocatedValue);

	int updatesalesBudgetExtnval(String sbextnId, String qty, String value);

	List<IndentBudgetDtlEntity> getindentBudgetDtlList(String pskId, String pkseId, String tenantId);

	List<ProjectSubAreaExtnEntity> getProjectSubAreaExtnRowMapper(String pkseId);

	int insertindentBudgetDtl(String indentDtl, String pkseId, String qty, String value);

	BigDecimal getTotalSubExtnVal(String pskId, String pmHdrId, String tenantId);

	String getmstIdByPmHdrId(String pmHdrId, String tenantId);

	int updateBudgetExtn(String sbExtnId, String qty, String value);

	int countIndentBudgetCount(String pkseId);

	List<GetindentbudgetDtlEntity> getindentbudgetDtl(String indentDtlId);

	List<IndentBudgetDtlEntity> getindentBudgetDtlById(String indentBudId);

	int updateBudgetQtyAndval(String pkseId, String qty, String val);

	int projectKeyAreaCount(String pmHdrId, String pkId, String tenantId);

	int projectKeySubAreaCount(String pmHdrId, String pskId, String tenantId, String pkaId);

	String getBudgetValue(String projectID, String tenantID);

	String getAllocValue(String projectID, String tenantID);

	int getCountProjectKeyMst(String pkDesc, String tenantId, String pmHdrId);

	int getCountProjectKeySubMst(String pskDesc, String tenantId, String pmHdrId);

	String getLastKeyCode(String tenantId, String pmHdrId);

	String getLastKeySubCode(String tenantId, String pmHdrId);

	int insertProjectKeyArea(String Code, String pkDesc, String isActive, String tenantId);

	int insertProjectKeySubArea(String Code, String pskDesc, String isActive, String tenantId);

	String getindentHdrId(String indentDtlId);

	int updateAllocatedAndBudgetVal(String allocatedVal, String budgetVal, String pkaId);

	String getAllocatedValSum(String pkaId);

	String getBudgetValSum(String pkaId);

	List<GetProjTimePlanDropDownEntity> getProjTimePlanDropDown(String tenantId);

	List<ProjectTimelineResp> getTimeLineOrdByDate(String pmHdrId, String tenantId);

	BigDecimal getTotalallocatedVal(String pkaId);

	int updateProjPlanDate(String pmHdrId, String plannedDate, String endDate, String tenantId, String priority);
//	List<KeyArea_ID> getProjectExtnByProj(String dskId);

	int updateDesignHdr(UpdateDesignHdrRequest updateDesignHdrRequest, List<String> messageList, List<String> otherEmpId);

	int updateAssyMstResp(AssyMstRequest assyMstRequest, List<String> otherEmpId, List<String> messageList);

	String getMinMaxDate(String funcName, String pmHdrId, String fieldName);

	void updatePlanStartAndEndDate(String maxValue, String minValue, String pmHdrId);

	int updateQCbuyoff(AssyMstRequest assyMstRequest);

	void UpdateQCStatus(String pmHdrId, String tenantId);

	List<ProjectTimelineEntity> getTimeTrackerByProjectId(ProjectByIDRequest projHdr);

	String getStartDateByProjId(String projectID, String tenantID, String departmentCode);

	String getEndDateByProjId(String projectID, String tenantID, String departmentCode);

	List<GetTaskEntryDtlEntity> getdesignTaskDtlByProjectId(String typeCode, String categoryCode, String projectID,
			String tenantId, String dependentId, String departmentCode);
	
	int getIndentBudgetCheck(String pkaId,String sbExtnId);
	
	String getpmHdrIdByPkaId(String pkaId);
	
	String getProjCodeByProjId(String projectId, String tenantId);
	
	String getprojKeyMstDesc(String pkaId);

	List<String> getAssignedMembersForProject(String pmHdrId, String tenantId);
	
	String projectDueDate(String pmId,String tenantId);

	String getCompletionPercent(String pmHdrId, String tenantID);

	String getTargetCost(String pmHdrId, String tenantID);

	String getProjectInitiationMstResp(ProjectInitiationMstRequest projectInitiation, String tenantId);

	List<BudgetSheetPaymentEntity> getBudgetSheetPaymentTerms(String sbHdrId);

	int updateBudgetSheetPaymentTerms(String sbPtId, String actualDate, String remarks);

	String getDebitVal(String pmHdrId, String tenantID);
}
