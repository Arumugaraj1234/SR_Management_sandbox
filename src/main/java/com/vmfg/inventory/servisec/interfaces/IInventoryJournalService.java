package com.vmfg.inventory.servisec.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.inventory.request.InventoryJournalRequest;
import com.vmfg.inventory.request.InventoryTenantRequest;


public interface IInventoryJournalService {
	
	ResponseAsList retrieveinventoryJournal(InventoryJournalRequest projectdtlreq) ;

	ResponseAsList getInvLocationForInward(InventoryTenantRequest tenantReq);

}
