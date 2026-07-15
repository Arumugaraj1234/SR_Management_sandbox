package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryJournalEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	    
        private int serialNumber;
	    private String inventoryTransactionDate;
	    private String inventoryTransactionTypeDescription;
	    private String productCode;
	    private String productDescription;
	    private String specification;
	    private String projectId;
	    private String locationDesc;
	    private String openingBalance;
	    private String closingBalance;
	    private String projectCode;
	    private String uomLongDescription;
	    private String uomShortDescription;
	    private String inventoryLocDesc;
		private String inventoryTransactionReferenceId;
		private String inventoryTransactionQuantity;
		private String projectName;
	    private String poCode;
}
