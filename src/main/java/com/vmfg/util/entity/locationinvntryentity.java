package com.vmfg.util.entity;

import java.io.Serializable;

public class locationinvntryentity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String inventorylocationcode;
	private int inventoryproducthdrid;

	public String getInventorylocationcode() {
		return inventorylocationcode;
	}

	public void setInventorylocationcode(String inventorylocationcode) {
		this.inventorylocationcode = inventorylocationcode;
	}

	public int getInventoryproducthdrid() {
		return inventoryproducthdrid;
	}

	public void setInventoryproducthdrid(int inventoryproducthdrid) {
		this.inventoryproducthdrid = inventoryproducthdrid;
	}

}
