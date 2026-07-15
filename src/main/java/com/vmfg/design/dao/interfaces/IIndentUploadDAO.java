package com.vmfg.design.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.entity.GetBudgetDtlByIndentEntity;
import com.vmfg.design.entity.GetIndentDtlProductCostEntity;
import com.vmfg.design.entity.GetIndentLifecycDtlEntity;
import com.vmfg.design.entity.GetindentDtlcycEntity;
import com.vmfg.design.entity.IndentBudgetDtlByIndentIdEntity;
import com.vmfg.design.entity.IndentCostDtlEntity;
import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.design.entity.IndentHdrDtlsEntity;
import com.vmfg.design.entity.IndentProjectEntity;
import com.vmfg.design.entity.IndentRemarksEntity;
import com.vmfg.design.entity.IndentRequestEntity;
import com.vmfg.design.entity.IndentTypeMstTblEntity;
import com.vmfg.design.entity.SalesIndentBudgetDtlEntity;
import com.vmfg.design.entity.UploadIndentTemplateEntity;
import com.vmfg.inventory.entity.InventoryMaterialTransferEntity;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.sales.entity.BudgetKeyCategory;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.PodtlsForProductEntity;
import com.vmfg.util.entity.Inventrytolocentity;

public interface IIndentUploadDAO {

	List<UploadIndentTemplateEntity> uploadIndentTemplate(JSONArray getArray, MultipartFile file);

	String getUomCodeByUnit(String unit, String tenantId);

	String getProjectCodeByProjId(String projectId, String tenantId);

	String getSbcShortDescBySbcCode(String sbcCode, String tenantId);

	String getSubKeyAreaCodeByPskId(String pskId, String tenantId);

	int insertIndentDtl(int indentId, String prodCode, String desc, String specification, String make, String qty,
			String unit, String weight, String material, String tenantId, String remarks);

	int createNewUomAndInsert(String unit, String tenantId);

	int getLastestRunningNo(String tenantId,String pmHdrId);

	String getKeyAreaCodeByPkId(String pkId, String tenantId);

	List<IndentDtlTblEntity> getIndentDtlsByIndentId(String projId, String indentId, String tenantId);

	List<IndentHdrDtlsEntity> getIndentHdrDtlsByProjectId(String projectId, String tenantId, String deptCode);

	int getNoOfProductsByIndentID(String indentId, String tenantId);

	String getSbcCodeByIndentId(String indentId, String tenantId);

	String getIndentTypeCodeByIndentId(String indentId, String tenantId);

	int getCurrentSeqByIndentId(String indentId, String tenantId);

	int getApprovebtnEnable(String designCode, String docGroup, String tenantId, String docTypeCode, String seq);

	String getIndentDtlIdByProductCode(String productCode, String tenantId, String indentId);

	int getDmIdByLatestVerion(String indentDtlId, String string);

	int insertIndentStatusDtl(String indentId, String seq, String seqStatus, String remarks, String tenantId,
			String empId, String docType);

	String getStatusCodebySeqAndDocType(String seq, String tenantId, String docType);

	int updateIndentHdrStatusAndDesig(String indentId, String seq, String seqStatus, String nextApprdesig, String empId, String tenantId);

	String getStartIndentReqStatus(String hdrId, String tenantId);

	String getPmHdrIdByDesignId(String hdrId, String tenantId);

	int getPmSeqByPmHdrId(String pmHdrId, String tenantId);

	int checkLastSeqByPmId(String pmId, int pmSeq, String tenantId);

	int updateStartIndentReq(String designId);

	int getApprovebtnEnableByCurr(String designCode, String tenantId, String docTypeCode, String seq);

	int insertIndentHdrDtls(String indentCode, String expectedDeliveryDate, String runningNo, String sbcCode,
			String pkId, String pskId, String indentTypeCode, String indentDate, String projectId, String masterId,
			String empId, String seqStatus, String tenantId, String design, String deptCode);

	String getDocTypeDescByDocType(String docType, String tenantId);

	List<IndentRemarksEntity> getIndentRemarks(String indentHdrId, String tenantID);

	String getEmpIdByProjectCode(String tenantId);

	List<IndentProjectEntity> getIndentByIndentID(IndentRequestEntity indentReq);

	String totalbudgetConsumed(String indentId);

	List<IndentDtlTblEntity> getIndentDtlsByIndentDtlId(String indentDtlId, String tenantId);

	BigDecimal getTotalQty(String indentDtlId);

	BigDecimal getTotalValue(String indentDtlId);

	BigDecimal getindentBudgetval(String indentId);

	int updateScmBudget(String scmBudgetValue, String indentId);

	String getAvailableValue(String indentId);

	String getPkaIdByIndentId(String indentId);

	String getTargetValue(String indentId);

	String getBudgetValues(String indentId,String pkaId);

	int updateBudgetValInPKA(String budgetValue, String pkaId, String operation);

	int updateIndentCostDtl(String budgetValue, String targetValue, String expectedDate, String indentId);

	List<IndentCostDtlEntity> getCostAnlysDtlslist(String hdrId, String tenantId, String sbcCode, String poCheck);

	List<BudgetKeyCategory> getbudgetKeyCategoryList(String tenantId);

	BigDecimal totalsalesBudget(String pmId);

	int insertnewProductInProdMst(String productCode, String pmHdrId, String prodDesc, String uomCode, String tenantId,
			String empId, String specification, String make, String qty, String unit, String weight, String material,
			String pkaId, String pskaId, String prodCategory, String unitRate);

	void updateProductCostPerUnit(int productId, String costPerUnit);

	int insertIndentAssignTeam(String indentDtlId, String empId, String primary, String tenantId);

	String getIaId(String indentDtlId);

	int updateIndentAssignTeam(String empId, String iaId);

	int CheckIsPrimaryZero(String indentDtlId);

	int CheckEmpInIndentAssignTeam(String indentDtlId, String empId);

	String getEmpIdByIndentDtlId(String indentDtlId);

	String getEmpNameByEmpId(String assignTeamEmpId);

	List<Inventrytolocentity> getProdLcnList(String tenantId);

	String insertInvProdDtl(int prodId, String tolocationcode, String tenantId);

	List<GetBudgetDtlByIndentEntity> getBudgetDtlByIndent(IndentRequestEntity indentReq);

	int updateIndentBudgetDtl(List<SalesIndentBudgetDtlEntity> salesIndentBudgetDtlReq);

	List<IndentTypeMstTblEntity> getIndentTypeMstDropDown(String tenantID, String deptCode,String isInternal);

	int checkProductCodeInProdMst(String ProductCode,String desc, String specification, String tenantId, String projectId, String prodCategory);

	String getStatusCodebySeqAndDocTypeByDocGrp(String seq, String tenantId, String docType, String docGrp);

	List<IndentHdrDtlsEntity> getIndentHdrDtlsByProjectIdAndType(String projectId, String tenantId,String indentType);
	
	List<GetIndentLifecycDtlEntity> getIndentSummaryDtlByProjectIdAndType(String projectId, String tenantId,String indentType, String string, String mstPocCheck, String empId, String fromDate, String toDate);

	String getIndentCodeByIndentId(String indentId);

	String getProjectIdByIndentId(String indentId);

	String getEnqIdByProjectId(String projectId);

	int getScsCountByIndentDtlId(String indentDtlId);

	int getUngroupedIndentDtlCount(String indentId, String tenantId);

	int deleteIndentByIndentDtlId(String indentDtlId);

	void deleteDocumentManagement(String dmId);

	void deleteFileManager(String dmId);

	int updateIndentHdrStatusAndSeq(String indentId, String seq, String seqStatus, String empId, String tenantId);

	List<PodtlsForProductEntity> getLatestPodtls(String productCode, String projectId, String tenantId);

	List<IndentRemarksEntity> indentRemarks(String indentId,String tenantId);
	
	int indentisFlagReApproval(String indentId, String tenantId);
	
	List<GetindentDtlcycEntity>getindentDtlcyclist(String indentId,String tenantId);
	
	int indentPoDtlval(String indentDtlId, String tenantId);
	
	int indentMiDtlval(String indentDtlId, String tenantId);
	
	int indentGrnDtlVal(String indentDtlId, String tenantId);
	
	int indentInspectionDtlVal(String indentDtlId, String tenantId);
	
	String indentPoDtlDateTime(String indentDtlId, String tenantId);
	
	String indentMiDtlDateTime(String indentDtlId, String tenantId);
	
	String indentGrnDtlDateTime(String indentDtlId, String tenantId);
	
	String indentInspectionDtlDateTime(String indentDtlId, String tenantId);
	
	String indentInspectionReqDtlDateTime(String indentDtlId, String tenantId);
	
	String indentGroupScsDateTime(String indentDtlId, String tenantId);
	
	String praReqDateTime(String indentDtlId, String tenantId);
	
	String praDateTime(String indentDtlId, String tenantId);
	
	List<GetIndentDtlProductCostEntity> getIndentDtlProductCost(String productCode,String tenantId, String pmHdrId, String productId);

	int getCheckIndentScs(String indentDtlId);
	
	int updateIndentDtl(String productCode, String desc, String spec, String make, String qty, String unit,
			String weight, String material, String remarks, String indentDtlId);

	String getCurIndentSeq(String indentId);

	String getIndentRevNo(int indentId);

	int getIndentRevCheck(int indentId);

	void updateIndentRevNo(String revision, int indentId, String empId);

	int updateProductInProdMst(String desc, String uom, String tenantId, String empId, String specification,
			String make, String qty, String weight, String material, String pkaId, String pksaId, int productId);

	String getIndentHdrId(String indentDtlId);
	
	List<IndentBudgetDtlByIndentIdEntity> getBudgetByIndentId(String indentId,String tenantId);

	void deleteIndentHdr(String indentHdrId, String tenantId);
	
	String getPkseIdBypkaId(String pkaId,String sbExtnId);

	List<InventoryMaterialTransferEntity> getMaterialTransferDtls(PmHdrIdAndTenantIdRequest matDtls);

	String getSumOfTransferValue(HdrIdandTenantIdRequest cstDtl);

	String getIndentNxtAppDesc(String docTypeCode, String currSeq, String docGroup, String tenantId);

	void deleteIndentStatusTbl(String indentHdrId, String tenantId);
	
	void updatenotification(String indentCode, String doctype,String tenantId);
	
	String filePathByDmId(String dmId);
	
	String fileNameByDmId(String dmId);
	
	void deleteBudgetExcessTbl(String indentHdrId, String tenantId);
	 
	void deleteBudgetExcessStatusTbl(String indentHdrId, String tenantId);

	int getCurIndentGrpSeq(String igScsId);
	
	String getProjectInitiationMstResp(String pmId, String empId, String tenantId);

	int updateIndentPkaAndPksaId(String pkaId, String pksaId, int indentId, String tenantId);

	boolean getAssignedStatus(String indentId, String tenantId);

	String cheackPoCreateOrNot(String hdrId, String tenantId);

	String CheckIndentBudgetVal(int indentId);

	String getTypeIndentGrpScs(String igHdrId, String tenantId);

	String getPmIdbyDepCode(String tenantId, String depCode);
}
