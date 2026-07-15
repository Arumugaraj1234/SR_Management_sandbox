package com.vmfg.assembly.dao.interfaces;

import java.util.List;

import com.vmfg.assembly.entity.MaterialIssueDtlEntity;
import com.vmfg.assembly.entity.MaterialIssueHdrEntity;
import com.vmfg.assembly.entity.RetriveFromStockIssueEntity;

public interface IAssemblyIssueDAO {

	int insertMaterialIssueHdr(String pmHdrId, String mrHdrId, String issuedBy, String remarks,
			String tenantId);

	int insertMaterialIssueDtl(int responseMiHdrId, String mrDtlId, String productId, String requestedQty,
			String availableQty, String issuedQty, String tenantId,String productCode,String projectId,String miCode,String updateBy, String inventoryLoctionCode);

	List<MaterialIssueHdrEntity> getMaterialIssueHdr(String hdrId, String tenantId, String productId);

	List<MaterialIssueDtlEntity> getMaterialIssueDtl(String hdrId, String tenantId);

	List<RetriveFromStockIssueEntity> retriveFromIssueStock(String hdrId, String tenantId);

	String getMtlIusseCode(String miCode);

	int updateMRDtlIssueQty(String mrDtlId, String issuedQty);

	int getMRCompletedStatus(String mrHdrId);

	int updateMrHdrCompletedStatus(String mrHdrId, String empId);

	String getInventoryLoctionCodeByMrDtlId(String mrDtlId);
}
