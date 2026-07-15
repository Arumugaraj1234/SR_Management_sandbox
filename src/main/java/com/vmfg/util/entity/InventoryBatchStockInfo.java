package com.vmfg.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class InventoryBatchStockInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String inventoryProductDtlId;
	private String batchNumber;
	private BigDecimal lotQuantity;
	public String getInventoryProductDtlId() {
		return inventoryProductDtlId;
	}
	public void setInventoryProductDtlId(String inventoryProductDtlId) {
		this.inventoryProductDtlId = inventoryProductDtlId;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public BigDecimal getLotQuantity() {
		return lotQuantity;
	}
	public void setLotQuantity(BigDecimal lotQuantity) {
		this.lotQuantity = lotQuantity;
	}
	
}
