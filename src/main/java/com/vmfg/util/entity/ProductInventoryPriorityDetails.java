package com.vmfg.util.entity;

public class ProductInventoryPriorityDetails {
	private String inventoryLocationCode;
	private String priority;
	private String pickListType;
	
	
	public String getPickListType() {
		return pickListType;
	}
	public void setPickListType(String pickListType) {
		this.pickListType = pickListType;
	}
	public String getInventoryLocationCode() {
		return inventoryLocationCode;
	}
	public void setInventoryLocationCode(String inventoryLocationCode) {
		this.inventoryLocationCode = inventoryLocationCode;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
}
