package com.vmfg.scm.dao.interfaces;

import java.util.List;

import com.vmfg.finance.entity.GrnDtlsEntity;
import com.vmfg.scm.entity.*;
import com.vmfg.scm.request.GrnCancelReq;

public interface IGrnDAO {

	List<GrnHdrEntity> getGrnHdrDetails(String projectId, String tenantId, String poId, String fromDate, String toDate);

	List<PoCostTypeEntity> getPoCostType(String isActive, String tenantId);

	List<GrnDtlEntity> getGrnDtlwithMaterialInwardDtl(String grnHdrId, String tenantId);

	int insertQualityInspecRequest(String poId, String poCode, String poDtlId, String qtyToBeInspected,
			String qtyInspected, String tenantId, String indentDtlId, String pmHdrId, String requestFrom, String MiID,String isRework, String empId);

	String getPoCodeByPoId(String poId);

	String getindentDtlBypoDtlId(String poDtlId);

	String getreceivedqtyBypoDtlId(String poDtlId);

	String getPmHdrIdByPOId(String poHdrId);

	EnquiryCodeWithId insertGrnHdr(String createdBy, String grnDate, String miId, String tenantId, String invLocation,String poId);

	int updateDcDtlReceivedQty(String dcDtlId, String recivedQty);

	String getNotifyEmp(String tenantId);

	String getpmHdrId(String poId);

	String getenquiryId(String pmHdrId);

	List<ProductDtlsEntity> getProdDtlsByProductCode(String productCode, String pmHdrId, String tenantId);

	void updatePoCodeInGrnHdr(int grnHdrId, String poCode);

	int insertGrnDtl(int grnHdrId, String miDtlId, String recivedQty, String tenantId, String poDtlId,
					 String indentDtlId, String pmHdrId, String productCode, String invLocation, String createdBy,
					 String enquiryCode, String productId);

	String getProductIdByProductCode(String productCode, String pmHdrId, String tenantId);

	String getQualityHdrId(String pmHdrId, String tenantId);

	int updateDcDtlBin(String dcDtlId, String bin, String productId);

	String getMIIndentDtlBypoDtlId(String poDtlId, String indentDtlId, String tenantId);

	String grnCancel(GrnCancelReq grnCancelReq);

	boolean checkCountInInvProdDtl(String productId, String inventoryLoc, String tenantId, String prodQty);

	String removeQtyFromInvPrdDtl(GrnDtlEntity grnDtl, String invLocCode, String tenantId);

	List<String> getGrnHdrIdByPoId(String poId, String tenantId);

	List<GrnDtlsEntity> getgrnlistForDebitNote(List<String> poDtlIds, String tenantId);

}
