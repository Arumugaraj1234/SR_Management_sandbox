package com.vmfg.mis.dao.interfaces;

import java.util.List;

import com.vmfg.mis.entity.OverAllProjSpentDrillDownEntity;
import com.vmfg.mis.entity.ProjConsumedValEntity;
import com.vmfg.mis.entity.ProjDetailsDrillDownEntity;
import com.vmfg.mis.entity.ProjSpentDrillDownEntity;
import com.vmfg.mis.entity.ProjectCntlEntity;
import com.vmfg.mis.entity.VendorDetailDrillDownEntity;

public interface IManagementMisDAO {

	List<ProjectCntlEntity> getTotalProjCnt(String tenantId, String fromDate, String toDate, String stageCode,
			String custCode, String pmHdrId);

	List<ProjConsumedValEntity> getProjConsumedValue(String tenantId, String fromDate, String toDate, String stageCode,
			String custCode, String pmHdrId);

	List<ProjSpentDrillDownEntity> getProjSpentDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId);

	List<OverAllProjSpentDrillDownEntity> getOverAllProjSpentDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId);

	List<OverAllProjSpentDrillDownEntity> getProjActualValDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId);

	List<ProjDetailsDrillDownEntity> getProjDetailsDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId);

	List<VendorDetailDrillDownEntity> getVendorDetailDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId);

	List<VendorDetailDrillDownEntity> getVendorDetailHdrView(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId);

	List<VendorDetailDrillDownEntity> getVendorPaymentDetails(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId);

	List<OverAllProjSpentDrillDownEntity> getProjSpentDetailByPmId(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId);

}
