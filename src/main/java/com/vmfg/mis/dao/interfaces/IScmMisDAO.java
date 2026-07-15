package com.vmfg.mis.dao.interfaces;

import java.util.List;

import com.vmfg.inventory.entity.InvProdEntity;
import com.vmfg.mis.entity.ScmEmployeeIndentDtlsEntity;
import com.vmfg.mis.entity.VendorDetailDrillDownEntity;

public interface IScmMisDAO {

	String indentCount(String projectId,String pmId,String empId, String month, String year,String lifeSpan, String tenantId);
	String indentCompleted(String projectId,String pmId,String empId, String month, String year,String lifeSpan, String tenantId);
	String indentAvgTime(String projectId,String pmId,String empId, String month, String year,String lifeSpan, String tenantId);
	String getcostnegotiated(String pmHdrId, String tenantId, String month, String year,String lifeSpan);
	List<InvProdEntity> getQtyInHand(String pmHdrId, String tenantId);
	String getInventoryAgeing(String pmHdrId, String tenantId);
	List<ScmEmployeeIndentDtlsEntity> getScmEmployeeIndentDtls(String empId, String pmHdrId, String tenantId, String month, String year, String lifeSpan);
	String getCompletedIndentCount(String pmHdrId, String empId, String tenantId);
	String getTotalAssignedIndents(String pmHdrId, String empId, String tenantId);
	String noOfPoApproved(String pmHdrId, String tenantId, String assignedTo, String month, String year, String lifeSpan);
	String getPendingIndentsCnt(String pmHdrId, String tenantId, String empId, String month, String year,
			String lifeSpan, String pmId);
	String getItemsDelayedCnt(String pmHdrId, String tenantId, String empId, String month, String year, String lifeSpan,
			String pmId);
	String getIndentDtlCount(String pmHdrId, String tenantId, String assignedTo, String month, String year,
			String lifeSpan, String pmId);
	String getIndentHdrCount(String pmHdrId, String tenantId, String empId, String month, String year, String lifeSpan,
			String pmId);
	String getInventoryStockCnt(String pmHdrId, String tenantId, String empId, String month, String year,
			String lifeSpan, String pmId);
	List<VendorDetailDrillDownEntity> getVendorPaymentCount(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId, String pmId, String empId);
	List<VendorDetailDrillDownEntity> getVendorDetailView(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId, String empId, String pmId);
	List<VendorDetailDrillDownEntity> getVendorDetailDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId, String empId, String pmId);
}
