package com.vmfg.mis.dao.interfaces;

import java.util.List;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.mis.entity.DrilldownDtlEntity;
import com.vmfg.mis.entity.QualityWidgetDtlEntity;
import com.vmfg.mis.entity.SupplierRatingEntity;
import com.vmfg.mis.entity.TeamMemberLoadEntity;
import com.vmfg.mis.entity.VendorTypeCateCountEntity;

public interface IQualityMisDAO {

	ResponseAsMessage getQualityProjCnt(String tenantId, String fromDate, String toDate, String empID, String pmId);

	List<SupplierRatingEntity> SupplierRatingDAO(String projectId, String tENANTID, String empID, String pmID, String fromDate, String toDate);

	List<TeamMemberLoadEntity> TeamMemberLoadDAO(String projId, String empId, String tenantId, String pmID, String fromDate, String toDate);

	List<QualityWidgetDtlEntity> QualityWidgetDtlResp(String projId, String tenantId, String empID, String pmID, String fromDate, String toDate);

	List<DrilldownDtlEntity> getDrilldownDtlResp(String projId, String tenantId, String typeCode, String empID, String pmID, String fromDate, String toDate);

	List<SupplierRatingEntity> SupplierRatingDAO1(String projectId, String tENANTID, String empID, String pmID,
			String fromDate, String toDate);

   String TeamMemberLoadQty(String projId, String empId, String tenantId, String pmID,
			String[] monthYr, String teamMemberId,String fromDate,String toDate);
   
  String SupplierSCMAndInwardRatingDAO(String projectId, String tENANTID, String empID, String pmID, String fromDate, String toDate,String vendorCode,String column);

List<VendorTypeCateCountEntity> getVendorByType(TenantRequest tenantId);

List<VendorTypeCateCountEntity> getVendorByCategory(TenantRequest tenantId);

}
