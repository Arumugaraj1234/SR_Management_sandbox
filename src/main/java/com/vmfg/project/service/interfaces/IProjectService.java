package com.vmfg.project.service.interfaces;

import java.util.List;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.controller.UpdateProjectPlanDateRequest;
import com.vmfg.project.entity.IndentBudgetDtlEntity;
import com.vmfg.project.request.*;
import com.vmfg.project.response.ProjectInternalResponse;

public interface IProjectService {

	ResponseAsList getProjectDtl(ProjectHdrRequest tenReq);

	ResponseAsList getWbsTemplate(TenantRequest tenantReq);

	ResponseAsList getWbsTemplateById(WbsIDRequest wbsReq);

	ResponseAsMessage insertUpdateProjectMilestone(List<ProjectTimelineRequest> projTimeReq);

	ResponseAsMessage deleteWBSById(DeleteTimeWBSByIDRequest deleteById);

	ResponseAsList getTimeLineByPM(ProjectByIDRequest projHdr);

	ProjectInternalResponse getIsInternalOrNot(ProjectInternalRequest request);

	ResponseAsMessage insertKeyAreaByPMId(List<KeyAreaRequest> keyAre);

	ResponseAsMessage delKeyAreaByPKId(KeyAreaDelRequest delReq);

	ResponseAsList getPKForProj(ProjectByIDRequest projHdr);
	
	ResponseAsMessage updatedesignindentReq(ProjectByIDRequest projHdr);

	ResponseAsList getLinkStatusByPMId(ProjectByIDRequest projHdr);

	ResponseAsMessage getCostFlowTypeByPmHdrId(ProjectByIDRequest projHdr);
	
	ResponseAsList getelementHdrDistinct(ProjectByIDRequest projHdr);

	ResponseAsList getSubAreaPmHdrList(GetSubAreaPmHdrListRequest getSubAreaPmHdrListreq);
	
	ResponseAsList getsalesBudgetExtnDtl(getsalesBudgetExtnDtlRequest getsalesBudgetExtnDtlReq);
	
	ResponseAsMessage deleteSubAreaExtn(DeleteSubAreaExtnRequest  deleteSubAreaExtReq);
	
	ResponseAsMessage insertSubAreaExtn(List<InsertSubAreaExtnRequest> insertSubAreaExtnreq);
	
	ResponseAsList getbugetextnListbyDSkId(GetbugetextnListbyDSkIdRequest getbugetextnListbyDSkIdReq);
	
	ResponseAsList getbugetextnListbyDSkId(GetindentbudgetextValbyPSkIdRequest getindentbudgetextValbyPSkIdReq);

	ResponseAsMessage insertindentBudget(List<IndentBudgetDtlEntity> indentBudgetReq);
	
	ResponseAsMessage totalSubAreaValueByPskId(GetSubAreaPmHdrListRequest getSubAreaPmHdrListreq);
	
	ResponseAsList getindentbudgetDtlbyindentDtlId(GetindentbudgetDtlbyindentDtlIdRequest getindentbudgetDtlbyindentDtlIdReq);

	ResponseAsMessage deleteIndentBudgetId(DeleteIndentBudgetIdRequest deleteIndentBudgetIdReq);
	
	ResponseAsList getProjTimePlanDropDown(TenantRequest tenantReq);
	
	ResponseAsList getExistingPMtemplateByPmHdrId(PmHdrIdAndTenantIdRequest pmHdrIdandTenantIdReq);
	
	ResponseAsMessage updateProjectPlanDate(UpdateProjectPlanDateRequest updateProjectPlanDateReq);

	ResponseAsMessage updateDesignHdr(UpdateDesignHdrRequest updateDesignHdrRequest);

	ResponseAsMessage getProjectInitiationMstResp(ProjectInitiationMstRequest projectInitiation);

	ResponseAsMessage updateAssyMstResp(AssyMstRequest assyMstRequest);

	ResponseAsMessage updateQCbuyoff(AssyMstRequest assyMstRequest);

	ResponseAsList getTimeTrackerByProjectId(ProjectByIDRequest projHdr);

	ResponseAsMessage updateBudgetSheetPaymentTerms(updateBsPaymentTermsRequest bsPaymentRequest);
}
