package com.vmfg.project.dao.interfaces;

import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.project.entity.BudgetExcessSheetEntity;
import com.vmfg.project.entity.GetIndentBudgetDtlsEntity;
import com.vmfg.project.entity.RetriveBudgetExcessStatusDtlEntity;
import com.vmfg.project.entity.SalesCategoryDtlEntity;
import com.vmfg.project.request.BudgetExcessStatusDtlReq;
import com.vmfg.project.request.IndentBudgetDtlReq;
import com.vmfg.project.request.updateBudgetExcessSheetRequest;
import com.vmfg.project.response.BudgetExcessBasedIndentHdrDtl;

public interface IBudgetExcessSheetDAO {

	int updateBudgetExcessSheetDtl(updateBudgetExcessSheetRequest updateudget);

	List<BudgetExcessSheetEntity> retriveBudgetExcessSheetDtl(BudgetExcessStatusDtlReq statusDtlReq);

	List<RetriveBudgetExcessStatusDtlEntity> getBudegetStatusDtl(String brHdrId, String tenantId);

	BudgetExcessBasedIndentHdrDtl getIndentHdrDetail(String indentId);

	List<DocumentStatusMstEntity> getDocCurrentSeqDtl(String docType, String seq, String tenantID, String processDoc);

	int insertBudgetExcessSheetDtl(String indentId, String pmHdrId, String budgetValue, String scmBudAllocatedValue,
			String excess, String vendor, String reason, String rootCase, String action, String responsible,
			String seqNo, String docStatus, String updatedBy, String tenantID, String dskId,String igScsId, String scsActualCost,
			String allocatedValue, String actualSpentSoFar, String pjsRefNo);

	int insertBudgetExcessStatus(int beHdrId, String seqNo, String docStatus, String remarks, String updatedBy,
			String tenantID);

	List<SalesCategoryDtlEntity> getSalesCatagoryDtl(IndentBudgetDtlReq indentBudgetDtl);

	List<GetIndentBudgetDtlsEntity> getIndentBudgetDtl(String sbcCode, String pmHdrId);

	int insertBudgetSheetExcessStatusStatusDtl(String budegtSheetDtlId, String currentseq, String docStatus,
			String tenantId, String remarks, String empId);

	void updateBudgetSheetExcessApproved(String budegtSheetDtlId);

	List<String> getDistinctDocGroup(String docType, String tenantId, String processCode);

	List<DocumentStatusMstEntity> getDocDtlcurrentSeqByDocGrp(String referenceDoc, String seq, String tenantId,
			String docGroup);

	List<DocumentStatusMstEntity> getNextSeqandStatusByDocGrp(int currentSeq, String docType, String tenantId,
			String docGroup);

	String getBudgetExcessValueByHdrId(String budegtSheetDtlId);

	int updateBudgetSheetExcessSeqAndStatus(String budegtSheetDtlId, String currentseq, String docStatus,
			String tenantId, String empId);

	String getIndentIdByBehdrID(String hdrId);

	int UpdateIndentAndScsStatus(String currSeq, String beHdrId, String tenantId, String empId, String docGrp, String processCode);

	String getAssemblyDes(String tenantId, String indentId);

	String getSubAssemblyDes(String tenantId, String indentId);

	String getBudgetCodeByIndentId(String hdrId);

	String getExcessValue(String refCode);

	int getCurrSeqForBudgetExcess(String refCode);

	String getActualValue(String refCode);

	String getTargetValue(String refCode);

	String getVerChechForBudgetExcessByBeHdrId(String indentId, String tenantId);

	String checkIndentExcessCount(String indentId, String tenantID);

	String getOverallExcessCostByPkaId(String pkaId, String tenantId);

	// Project-wide (not per-discipline) running count, matching the existing INDENT_CODE
	// RUNNING_NO convention - see project_budget_target_cost_removal memory.
	int getNextPjsRefSeqByProjectId(String projectId, String tenantId);

}
