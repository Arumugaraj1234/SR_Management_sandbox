package com.vmfg.quality.dao.interfaces;

import java.util.List;

import com.vmfg.quality.entity.GetInspTypeEntity;
import com.vmfg.quality.entity.GetQtyDtlEntity;
import com.vmfg.quality.entity.GetQtyInspectionHdrEntity;
import com.vmfg.quality.entity.RetieveQCInspectionHdrEntity;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.quality.request.GetqtyInspecDocDtlRequest;
import com.vmfg.sales.entity.ApprovedDocEntity;

public interface IQualityDAO {

	List<GetQtyDtlEntity> getQtyDtl(String qHdrId,String empId,String fromDate,String toDate,String tenantId,String customerName,String pmId,String projectId);
	
	String getCountofinsp(String pmHdrId,String tenantId);
	
	String getQtyinspCompleted(String pmHdrId,String tenantId);

	List<RetrieveQualitInspectionEntity> retrieveQualitInspectionReq(String projectId, String tenantId);

	List<RetieveQCInspectionHdrEntity> retieveQCInspectionHdr(String qiId, String tenantId);

	List<GetInspTypeEntity> getInspType(String tenantId);

	List<String> getConfigName(String tenantId);

	String getConfigNameByQiId(String qiId, String tenantId);

	List<RetieveQCInspectionHdrEntity> getQiCountsByPmHdrId(String pmHdrId, String tenantId);
	
	List<GetQtyInspectionHdrEntity> getInspectionDtlList(String fromDate,String toDate,String pmHdrId,String tenantId);

	String checkInsCount(String qiId);
	
	List<ApprovedDocEntity> getqtyInspecDocDtl(GetqtyInspecDocDtlRequest getqtyInspecDocDtlReq);

	int checkScmEmp(String empId, String tenantId,String Dept);

}
