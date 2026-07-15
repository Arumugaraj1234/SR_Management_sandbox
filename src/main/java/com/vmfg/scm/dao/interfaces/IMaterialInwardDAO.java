package com.vmfg.scm.dao.interfaces;

import java.util.List;

import com.vmfg.scm.entity.MaterialInwardDtlEntity;
import com.vmfg.scm.entity.MaterialInwardHdrEntity;

public interface IMaterialInwardDAO {

	List<MaterialInwardHdrEntity> getMaterialInwardHdrDtls(String poId, String tenantId, String pmHdrId, String fromDate, String toDate);

	List<MaterialInwardDtlEntity> getMaterialInwardDtlList(String hdrId, String tenantId);

	String getPoQty(String poDtlId);

	String getGrnDtlReceivedQty(String poDtlId);

	String getPoInspectedQty(String poDtlId);

	int updateReceivedQty(String inwardQty, String poDtlId);

	String getMICodeById(String MiId);

	int insertMaterialInwardHdr(String tenantId, String poId, String poCode, String currentDate, String vendorCode,
			String dcDate, String noOfParts, String string, String empId, String dcNo, String invLocation, String projectCode,String inwardRating ,String relationshipRating, String isCompleted);

	int insertInMiDtl(String miId, String poDtlId, String indentDtlId, String orderedQty, String receivedQty,
			String inspectedQty, String uom, String tenantId, String projectId, String productCode, String miCode,
			String updatedBy, String invLocation, String remarks, String productId);

	String getInvType(String invLocation, String tenantId);

	String getInvLocationByType(String getInvType, String tenantId, String invLocation);

	String getqtyreqcountByMiDtlId(String miDtlId, String tenantId);

	int updateMiDtlId(String miDtlId, String poDtlId, String inspectionqty);

	void updateBinInprodMst(String productId, String bin);

	List<MaterialInwardDtlEntity> getMiDtlByPoDtlId(String poDtlId);

	int checkMiQtyStatus(String miDtlId);

	String getBinValue(String prodId);

	String getgrnHdrId(String miId, String invLocation, String poId, String poCode);

	String getGrnEnqCode(String grnHdrId);
	
	int getGrnHdr(String miId,String poId,String tenantId);

	String getPoCodeByPoId(String tenantId, String poId);

	int getIndentIdPoId(String tenantId, String poId);

	int getPmHdrIdByIndentId(String tenantId, int indentId);

	int getscmHdrIdByPmHdrId(String tenantId, int pmHdrId);


}
