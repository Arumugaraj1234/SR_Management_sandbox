package com.vmfg.util.entity;

import java.io.Serializable;

public class InventrylocCodesentity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String locationcode;
	private int inventorylochdrid;
	private int productquntyonhand;

	public String getLocationcode() {
		return locationcode;
	}

	public void setLocationcode(String locationcode) {
		this.locationcode = locationcode;
	}

	public int getInventorylochdrid() {
		return inventorylochdrid;
	}

	public void setInventorylochdrid(int inventorylochdrid) {
		this.inventorylochdrid = inventorylochdrid;
	}

	public int getProductquntyonhand() {
		return productquntyonhand;
	}

	public void setProductquntyonhand(int productquntyonhand) {
		this.productquntyonhand = productquntyonhand;
	}
}
