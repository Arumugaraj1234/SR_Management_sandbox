package com.vmfg.scm.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.master.entity.VendorMstEntity;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.scm.entity.*;
import com.vmfg.scm.request.GetDcDtlByDcIdRequest;
import com.vmfg.scm.request.PoTypeUpdateReq;

public interface IPoDAO {

	List<IndentGrpHdrIdEntity> getIndentGrpHdrIdList(String hdrId, String tenantId);

	List<GetPoDtlsEntity> getPoHdrListByIgHdrId(String igHdrId, String tenantId);

	List<GetPoDtlsEntity> getPoDtlsByPoId(String hdrId, String tenantId);

	List<DebitNoteEntity> getDebitNoteReason(String isActive, String tenantId);

	List<PoDtlEntity> getPoDtlList(String poId);

	List<PoPaymentTermEntity> getPoPaymentTermList(String poId);

	String updatePoPaymentTerm(String potId, String paidAmount, String tenantId);

	String gstSumByPotId(String potId);

	String getPoIdByIgscsId(String igscsId, String tenantId);

	String getPoIdByIgscsIdForCancelledPo(String igscsId, String tenantId);

	List<PoDispatchDocEntity> getPoDispatchDocList(String poId);

	int insertPoHdrDtl(GetPoDtlsEntity insertPoDtlsEntity);

	int insertPoStatusDtl(String poId, String seqNo, String seqStatusCode, String tenantId, String remarks,
			String empId);

	int updatePoHdrDtl(GetPoDtlsEntity insertPoDtlsEntity);

	int insertpoDispatchDocDtl(PoDispatchDocEntity poDispatchDocEntity, String poHdrId);

	int insertpoPaymentTermDtl(PoPaymentTermEntity poPaymentTermEntity, String poHdrId, int flag);

	int insertpoDtl(PoDtlEntity poDtlEntity, String poHdrId);

	String getPoType(String vendorCode);

	String getIndentId(String igScpId);
	
	String getPJSCreatedBY(String igScpId);

	List<BillingDetailEntity> getBillingDetails(String tenantId);

	List<VendorMstEntity> getVendorMstDtls(String vendorCode, String tenantId);

	List<LocationMstEntity> getVendorLocDtls(String locationId, String tenantId);

	int getLatestRevByPoId(String poId);

	List<PoStatusEntity> getPoStatusList(String poId);

	List<IndentGrpHdrIdEntity> getAllPoHdrByIndentId(String projectId, String tenantId);

	List<GetPoDtlsByDate> getPoDtlsByDateAndPoId(String fromDate, String toDate, String projectId, String tenantId);

	String getProdCodeByIndentDtlId(String indentDtlId);

	void removePoHdrIdBased(String poHdrId);

	int getPtDtlCount(String poId);

	List<GetPoDtlsEntity> getPoDtlsByPoDtlId(String poDtlId);

	String getIgScsIdByPoId(String poId);

	int qtyInspectReqCount(String poDtlId);

	void updatePoApproved(String poId, String isApproved);

	int updatePoSeqAndStatus(String poId, String currentseq, String docStatus, String isLatest, String isApproved,String empId);

	int updateIntentHdrSeqAndStatus(String poId, String currentseq, String docStatus, int getIntentId, String isCompleted);

	int retriveIndentId(String poId);
	
	List<GetDCTypeDtlEntity>getDctypeCode();
	
	List<AddressDtlByDcTypeEntity>getCustomerCode(String custCode,String tenantId);
	
	List<AddressDtlByDcTypeEntity>getVendorCode(String custCode,String tenantId);
	
	List<AddressDtlByDcTypeEntity>getOrganCode(String custCode,String tennatId);
	
	List<PoInstoreDtlEntity> getpoInstoreDtlByPmId(String pmHdrId,String tenantId,String isFlag);
	
	List<DcHdrEntity>getAllDcHdrByPmId(String pmHdrId,String tenantId, String getReturnable);
	
	List<DcHdrEntity>getDcHdrDtlById(String dcId,String tenantId);
	
	List<DcDtlEntity>getDtlByDcId(String dcId,String tenantId, String pmHdrId);
	
	int getCountDtlByDcId(String dcId,String tenantId);

	int getGroupDtlCountByDcId(String dcId,String tenantId);
	
	int insertDcHdr(DcHdrEntity dcHdrEntity);
	
	int cancelDcHdr(GetDcDtlByDcIdRequest getDcDtlByDcIdReq);
	
	int increaseIntPrdDtl(String productId,String qty,String pmHdrId,String dcCode,String productCode,String updateBy,String tenantId);

	String getTenantPropertyVal(String tenantId,String propertyName);
	
	int getIndentCloseStatus(String indentId,String tenantId);
	
	int updateindentClose(String indentId);

	String getIndentIdByPraId(String praId, String tenantId);

	int getUncompletedPraCountByIndentId(String indentId, String tenantId);

	int getUncoveredNonInventoryCount(String indentId, String tenantId);

	List<String> getEmpListByIndentId(String string);

	List<String> getEmpListByPoId(String poId);

	String getAppDesig(String docGrp, String currentseq, String tenantId);
	
	int materialInwardCheck(String poId);

	int praCheckByPotId(String potId);

	String getPendingTransportChrg(String poId);
	String getPendingInsuranceChrg(String poId);
	String getPendingOtherChrg(String poId);
	String getPendingPfChrg(String poId);

	int praCheck(String poId);
	int praCheckForPendingValues(String poId);

	int praCheckForCancelledPo(String poId);
	int updatePoType(PoTypeUpdateReq poTypeReq);

	List<ProductMstDropDownEntity> getDCProductDropDown(String name , String pmHdr,String tenantID);
	
	List<PoHsnEntity> getHSNbyParno(String partNo, String tenantId);
	
	String getpoIdByPoDtlId(String poDtlId);
	
	List<PoDescMstEntity> getdivisionDesc(String tenantId);
	List<PoDescMstEntity> getransitInsuranceDesc(String tenantId);
	List<PoDescMstEntity> getModeOfDispatchDesc(String tenantId);
	List<PoDescMstEntity> getInspectScopeDesc(String tenantId);
	
	String invLocType (String name,String tenantId);

	String getQcRequestyQty(String poDtlId);

	List<PoDtlEntity> getPoDtlListByPoDtlId(String poDtlId);

	int getIsInventoryCount(String IndentId, String tenantId);
	
	List<GetProductUnitCostEntity> getProductUnitCostList(String poHdr ,String tenantId);
	
	int updateproductMstUnitCost(String productId,String cost);

	int getQcIsCompletedStatus(String qiId);

	int getCARaisedCount(String qiHdrId);

	int getCaApprovedStatus(String qiHdrId);

	List<RetrieveQualitInspectionEntity> getQcReqDetails(String poDtlId, String type);

	String getQcOkQty(String qiHdrId);

	String getPoDtQty(String poDtlId);

	List<RetrieveQualitInspectionEntity> getMiQcReqDetails(String miDtlId, String poDtlId);

	String getPaymentTermStatus(String potId);

	List<GetPoDtlsEntity> getPreRevisionPoDtls(String igScpId, String revision, String tenantId);
	
	String getGstType(String vendorCode,String tenantId);
	
	String getPartCount(String poId);

	String getPmHdrIdByPoId(String poId, String tenantId);

	String getProdDescByIndentDtlId(String indentDtlId);
	String getProdDescByQiId(String qiId);

	int checkQcIsRaised(String qiId);
	
	String getBinValue(String dcId);

	String getMiDtlList(String poDtlId, String tenantId);

	String getMiOkDetails(String poDtlId);

	String getQcForwaitingStatus(String poDtlId, String qiId);

	int praCheckByPoId(String poId);

	int praIsAvilableCheck(String poId);

	String getPendingAmtForPraStatus(String poId);

	String getApprovedPoTotalByPkaId(String pkaId);

	Map<String, BigDecimal> getApprovedPoTotalGroupedByPmHdrId(String pmHdrId);

	String getApprovedPoTotalByProjectId(String projectId);

	String getApprovedPoTotalByProjectIdAndSbcCode(String projectId, String sbcCode);

	Map<String, BigDecimal> getApprovedPoTotalGroupedBySbcCode(String projectId);

	Map<String, BigDecimal> getApprovedPoTotalGroupedByProjectIds(List<String> projectIds);

}
