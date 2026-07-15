package com.vmfg.assembly.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.vmfg.assembly.entity.GetAssyDtlEntity;
import com.vmfg.assembly.entity.MaterialReqDtlEntity;
import com.vmfg.assembly.entity.MaterialReqHdrEntity;
import com.vmfg.assembly.entity.RetriveFromStockEntity;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.general.response.ResponseAsMessage;

public interface IAssemblyDAO {

	int getindentcount(String pmId, String isComplete);

	int getMaterialReqHdrCount(String pmId, String isComplete);

	List<MaterialReqHdrEntity> getMaterialReqHdr(String hdrId, String tenantId, String requestType);

	List<MaterialReqDtlEntity> getMaterialReqDtl(String hdrId, String tenantId);

	ResponseAsMessage cancelMiRequestHdr(String hdrId, String tenantId);

	List<RetriveFromStockEntity> retriveFromStock(String pmHdrId, String pkaId, String pskaId, String tenantId);

	int insertMrHdr(String pmHdrId, String requestedBy, String requestedFor, String tenantId, String requestType );

	ResponseAsMessage retriveAssyResp(MaterialReqHdrRequest assyMstRequest);

	List<GetAssyDtlEntity> getAssyDtl(String fromDate, String toDate, String custName, String assyId, String tenantID,
			String pmId, String empId,String projectId);

	int insertMrDtl(int responseMrHdrId, String poductId, String requestedQty, String availableQty, String tenantId,
			String inventoryLocationCode);

	BigDecimal getActualAvailableQty(String pmHdrId,String tenantId,String productCode, String InventoryCode);

	ResponseAsMessage IsStagingStatusForQuality(String hdrId, String tenantId);

	ResponseAsMessage IsStagingStatusForAssy(String hdrId, String tenantId);

	int updateIsStagingStatus(String hdrId, int isStatus, String tenantId);

	int checkIsStagingStatus(String hdrId, String tenantId);

	BigDecimal getGrnQty(String pmHdrId, String tenantId, String productCode, String productId, String InventoryCode, String desc, String spec);

	String getIsInternalOrNot(String pmHdrId);

	
}
