package com.vmfg.inventory.dao.interfaces;

import java.util.List;

import com.vmfg.inventory.entity.InventoryJournalEntity;
import com.vmfg.inventory.entity.LocationDropDownEntity;
import com.vmfg.inventory.request.InventoryTenantRequest;

public interface IInventoryJournalDao {

	List<InventoryJournalEntity> retriveJournal(String frmDate, String toDate,String projectId, String tenantId);

	List<LocationDropDownEntity> getInvLocationForInward(InventoryTenantRequest tenantReq);
	
}
