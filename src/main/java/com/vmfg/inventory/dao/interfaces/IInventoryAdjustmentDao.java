package com.vmfg.inventory.dao.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.vmfg.inventory.entity.AdjustmentTypeDropDownEntity;
import com.vmfg.inventory.entity.InventoryAdjustmentEntity;

public interface IInventoryAdjustmentDao {

	List<InventoryAdjustmentEntity> retrieveinventoryAdjustment(String fromDate, String toDate, String tenantId);

	List<AdjustmentTypeDropDownEntity> getadjustmettypedropdown(String tenantId);

	int insertAdjustment(String projectId, String productId, String locationCode, String adjustmentType,
			String qtyonHand, String adjustmentQty, String revisedQty, String adjustmentedBy,
			LocalDateTime adjustmentDateTime, String reason,String tenantId,String productCode);

}
