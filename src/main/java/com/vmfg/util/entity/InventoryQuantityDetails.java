package com.vmfg.util.entity;

public class InventoryQuantityDetails {

	private String inventoryLocationMax;
	private String minimumOrderQuantity;
	private String safetyStock;
	private String productQuantityOnHand;
	private String inventoryHdrId;
	private String inventoryDtlId;
	
	
	public String getInventoryHdrId() {
		return inventoryHdrId;
	}
	public void setInventoryHdrId(String inventoryHdrId) {
		this.inventoryHdrId = inventoryHdrId;
	}
	public String getInventoryDtlId() {
		return inventoryDtlId;
	}
	public void setInventoryDtlId(String inventoryDtlId) {
		this.inventoryDtlId = inventoryDtlId;
	}
	public String getInventoryLocationMax() {
		return inventoryLocationMax;
	}
	public void setInventoryLocationMax(String inventoryLocationMax) {
		this.inventoryLocationMax = inventoryLocationMax;
	}
	public String getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}
	public void setMinimumOrderQuantity(String minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}
	public String getSafetyStock() {
		return safetyStock;
	}
	public void setSafetyStock(String safetyStock) {
		this.safetyStock = safetyStock;
	}
	public String getProductQuantityOnHand() {
		return productQuantityOnHand;
	}
	public void setProductQuantityOnHand(String productQuantityOnHand) {
		this.productQuantityOnHand = productQuantityOnHand;
	}
	
}
