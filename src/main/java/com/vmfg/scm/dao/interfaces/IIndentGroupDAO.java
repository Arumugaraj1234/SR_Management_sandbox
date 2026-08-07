package com.vmfg.scm.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.scm.entity.IndentGroupDetailsEntity;
import com.vmfg.scm.entity.IndentGroupHdrAndDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpVenDtlEntity;
import com.vmfg.scm.entity.IndentGrpScpVenEntity;
import com.vmfg.scm.entity.IndentGrpScpVenPtEntity;
import com.vmfg.scm.entity.IndentGrpScsStatusEntity;
import com.vmfg.scm.entity.IndentInsertGrpDtlRequest;
import com.vmfg.scm.entity.ScpDtlsEntity;
import com.vmfg.scm.request.IndentGrpDelRequest;
import com.vmfg.scm.request.IndentInsertGrpRequest;
import com.vmfg.scm.request.IndentTemplateNameRequest;

public interface IIndentGroupDAO {

	int delIndentGrpDtl(IndentGrpDelRequest indentGrpDtlReq);
	
	int lastIndentGrpDtlCheck(String indentDtlId);

	int delIndentGrp(IndentGrpDelRequest indentGrpDtlReq);

	List<IndentGroupHdrAndDtlEntity> getIndentGroupHdrAndDtl(IndentGrpDelRequest indentGrpDtlReq);

	int checkTemplateName(IndentTemplateNameRequest indentTempName);

	int insertTempGrup(IndentInsertGrpRequest indentTempName);

	void insertTempGrpDtl(int hdrId, IndentInsertGrpDtlRequest grpDtl);

	BigDecimal getsumOfGrpQty(String indentDtl, String tenantId);

	List<ScpDtlsEntity> getScpDtlsByIgHdrId(String hdrId, String tenantId);

	List<IndentGrpScpDtlEntity> getScpDtlList(String igScpId);

	List<IndentGrpScpVenEntity> getScpVendorList(String igScpId);

	List<IndentGrpScpVenDtlEntity> getScpVendorDtlList(String igScpId);

	List<IndentGrpScpVenPtEntity> getScpvendorPtList(String igScpId);

	int insertInScp(String igHdrId, String TechnicalCompassion, String technicalRecommendation, String vendorEvaluated,
			String vendorShortListed, String justification, String vendorQualified, String customerApproval,
			String createdDate, String createdBy, String seqNo, String seqStatus, String tenantId, String indentId,
			String type);

	int updateInScp(String igScpId, String TechnicalCompassion, String technicalRecommendation, String vendorEvaluated,
			String vendorShortListed, String justification, String vendorQualified, String customerApproval,
			String createdBy, String type);

	int insertInVen(String igScpVid, String igScpId, String l1VendorCode, String l2VendorCode, String l3VendorCode,
			String level, String createdBy, String l1Gst, String l2Gst, String l3Gst);

	int insertInVenDtl(IndentGrpScpVenDtlEntity indentGrpScpVenDtlEntity, String createdBy, String scpId);

	int insertInScpStatus(String igScpId, String seqNo, String SeqStatus, String remarks, String CreatedBy,
			String tenantId);

	String getIndentIdByIndentDtlId(String indentDtlId);

	String getIndentDtlIdByIgDtlId(String igDtlId);

	int getIndentgrpDtlCount(String igDtlId);
	
	String getIndentIdByIgDtlId(String igDtlId);
	
	String getIndentIdByIgHdrId(String igDtlId);

	int getIndentgrpScsCountByIgHdrId(String igHdrId);

	int insertInScpDtl(String igScpDItld, String igScpId, String igDtlId, String l1CurrencyType, String l1ExchangeRate, String l1UnitPrice, String l1ExtendedPrice, String l1UnitPriceFx, String l1ExtendedPriceFx,
			 String l2CurrencyType, String l2ExchangeRate, String l2UnitPrice, String l2ExtendedPrice, String l2UnitPriceFx, String l2ExtendedPriceFx, String l3CurrencyType, String l3ExchangeRate, String l3UnitPrice, String l3ExtendedPrice, String l3UnitPriceFx, String l3ExtendedPriceFx,
			String finalL1UnitPrice, String finall1ExtnPrice, String finalL1UnitPriceFx, String finalL1ExtnPricefx, String finalL2UnitPrice, String finall2ExtnPrice, String finalL2UnitPriceFx, String finalL2ExtnPricefx,
			String finalL3UnitPrice, String finall3ExtnPrice, String finalL3UnitPriceFx, String finalL3ExtnPricefx,  String tenantId, String indentDtlId);

	String getScmBudgetValue(String indentId, String budgetCol);

	String getVendorNameByVendorCode(String vendorCode);
	
	String getVendorUniqueCodeByVendorCode(String vendorCode);

	int getIndentGrpDtlCountByIgHdrId(String igHdrId);

	String getIndentTargetValue(String indentId);

	List<IndentGrpScsStatusEntity> getScsStatusList(String igScpId);

	String getScsStatusDesc(String igHdrId);

	int getBudgetExcessIsCompleted(String scpId);

	int insertInVenPt(IndentGrpScpVenPtEntity indentGrpScpVenPt, String tenantId, String scpId, String createdBy,
			String remarks);

	int getBudgetExcessDtlCount(String scpId);

	String getScsIdByIndentId(String indentId);

	int getScsPtCount(String scpID);

	void deleteScsPt(String scpID);

	int poScsCheck(String indScpId);

	void deleteScpId(String indScpId, String tableName);

	String getScsIdByIgHdrId(String igHdrId);

	String getPoStatusDesc(String igScsId, String tenantId);

	int getPoCountByIgScsId(String igScsId);

	int checkForCancelledPo(String igScsId, String tenantId);

	String getTenantPropertyVal(String propertyName, String tenantId);

	String getVendorCodeByScsId(String scsId,String vendorCode);

	List<IndentGroupDetailsEntity> getIndentGroupDtlsForSCS(String pmHdrId, String tenantId);

	int updateScpApproved(String scpId, String isApproved);

	String indentGroupSCSType(String igHdrId);

	int updateScpSeqAndStatus(String scpId, String currentseq, String docStatus, String empId);

	String getindentCode(String scsId);

	String getVendorQualified(String scsId);
	
	int pjsversioncheck(String igScsId);
	
	List<IndentGrpScpVenPtEntity> getScpPtListForLevel(String igScpId, String level);

	String getPoRevisionNoByIgScsId(String igScsId);

	String getindentIdBygrpScd(String igScsId);

	List<String> getDistinctScsDocGroup(String docGrp, String tenantId, String processDoc);

	String venDtlBasicCost(String igHdrId, String basicTotalCol);

	String venDtlBasicCostFx(String igHdrId, String basicTotalCol);

	String getVenQualifiedByIgHdrId(String igHdrId);

	List<IndentGroupDetailsEntity> getIndentGroupRetrieve(String tenantId, String fromdate, String todate,
			String indentId, String projectId, String empId);

	String getLastUpdatedDateTime(String indentDtlId, String tenantId, String type);

	int updateLastUpdatedDateTime(String indentDtlId, String colName, String datetime, String tenantId);

	List<IndentDtlTblEntity> getIndentDtlsByScsId(String scsId, String tenantId);

	List<IndentDtlTblEntity> getIndentDtlsByPoId(String poId, String tenantId);

	int reUpdatedDateTimeIndentDtl(String indentDtlId, String colName, String tenantId);

	void deletePoScsId(String indScpId, String tenantId);

	String getBudgetExcessValue(String indentId);

	// NEW-flow equivalent of getBudgetExcessValue - reads ACTUAL_EXCESS (the real shortfall
	// amount) instead of EXCESS (always the full quote for NEW-flow, since TARGET_VALUE is never
	// written - see project_budget_target_cost_removal memory). Same shape: most recent completed
	// budget_excess_dtl row for this indent, "0" if none.
	String getApprovedActualExcessByIndentId(String indentId);

	String getIsPdfOrNot(String dmId);

	String getScsGrpType(String scsId);

	void updateInvStockYesOrNo(String type, String igHdrId);

	int getPendingUngroupedItemCount(String indentId, String tenantId);

	String getOtherCommittedScsTotalByPkaId(String pkaId, String excludeIndentId, String minSeqNo);

	String getPendingBudgetExcessReservedTotalByPkaId(String pkaId, String excludeIndentId);

	int getScsCurrentSeq(String scsId);

	Map<String, BigDecimal> getOtherCommittedScsTotalGroupedByPmHdrId(String pmHdrId, String minSeqNo);

	String getCommittedScsTotalByProjectId(String projectId, String minSeqNo);

	String getCommittedScsTotalByProjectIdAndSbcCode(String projectId, String sbcCode, String minSeqNo);

	Map<String, BigDecimal> getCommittedScsTotalGroupedBySbcCode(String projectId, String minSeqNo);

	Map<String, BigDecimal> getCommittedScsTotalGroupedByProjectIds(List<String> projectIds, String minSeqNo);
}
