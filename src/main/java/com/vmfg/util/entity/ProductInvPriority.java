package com.vmfg.util.entity;

import java.io.Serializable;

public class ProductInvPriority implements Serializable {
private static final long serialVersionUID = 1L;
	
	private String inventoryloccode;

	public String getInventoryloccode() {
		return inventoryloccode;
	}

	public void setInventoryloccode(String inventoryloccode) {
		this.inventoryloccode = inventoryloccode;
	}

}
