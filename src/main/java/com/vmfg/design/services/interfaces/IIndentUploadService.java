package com.vmfg.design.services.interfaces;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.entity.IndentRequestEntity;
import com.vmfg.design.entity.SalesIndentBudgetDtlEntity;
import com.vmfg.design.request.BudgetValueUpdateRequest;
import com.vmfg.design.request.GetIndentDtlProductCostRequest;
import com.vmfg.design.request.IndentDtlRequest;
import com.vmfg.design.request.IndentRemarksRequest;
import com.vmfg.design.request.InsertIndentRequest;
import com.vmfg.design.request.TenantEmpRequest;
import com.vmfg.design.request.UpdateHdrRequest;
import com.vmfg.design.request.UpdateIndentAssignTeamReq;
import com.vmfg.design.request.getIndentHdrDtlRequest;
import com.vmfg.design.request.getStartIndentRequest;
import com.vmfg.design.response.getindentLifecycDtlResponse;
import com.vmfg.design.rowmapper.IndentDtlByPoRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.IndentDtlDeleteRequest;

public interface IIndentUploadService {

	ResponseAsList uploadIndentTemplate(JSONArray getArray, MultipartFile file);

	ResponseAsMessage insertIndentDtls(InsertIndentRequest insertIndentReq);

	ResponseAsList getIndentDtlsByIndentId(IndentDtlRequest indentDtlReq);

	ResponseAsList getIndentHdrDtlsByProjectId(getIndentHdrDtlRequest getIndentHdrDtlReq);

	ResponseAsMessage insertIndentFileByIndentID(JSONObject jsonObj, MultipartFile file);

	ResponseAsMessage updateIndentHdrStatus(UpdateHdrRequest updateHdrRequ);

	ResponseAsMessage getStartIndentReqStatus(getStartIndentRequest getStartIndentReq);

	ResponseAsList getIndentRemarks(IndentRemarksRequest indentRemReq);

	ResponseAsMessage updateBudgetDtl(BudgetValueUpdateRequest budgetValueUpdateReq);

	ResponseAsList getIndentByIndentID(IndentRequestEntity indentReq);

	ResponseAsList getCostAnlysDtls(HdrIdandTenantIdRequest cstDtl);

	ResponseAsMessage updateEmpInIndentAssignTeam(UpdateIndentAssignTeamReq indentAssignTeamReq);

	ResponseAsList getBudgetDtlByIndent(IndentRequestEntity indentReq);

	ResponseAsMessage updateIndentBudgetDtl(List<SalesIndentBudgetDtlEntity> salesIndentBudgetDtlreq);

	ResponseAsList getIndentTypeMstDropDown(TenantEmpRequest tenantReq);
	
	ResponseAsList getindentLifecycDtl (getindentLifecycDtlResponse getindentLifecycDtlReq);
	
	ResponseAsList getindentDtlcyc(IndentRequestEntity indentReq);

	ResponseAsMessage deleteIndentByIndentDtlId(IndentDtlDeleteRequest indentDtlDeleteReq);
	
	ResponseAsList getIndentDtlProductCost(GetIndentDtlProductCostRequest getIndentDtlProductCostReq);

	ResponseAsList getMaterialTransferDtls(PmHdrIdAndTenantIdRequest matDtls);
	
	ResponseAsMessage deleteTaskDoc(IndentDtlDeleteRequest indentDtlDeleteReq);

	ResponseAsList getIndentDtlsByPoIndentId(IndentDtlByPoRequest indentDtlPoReq);

}
