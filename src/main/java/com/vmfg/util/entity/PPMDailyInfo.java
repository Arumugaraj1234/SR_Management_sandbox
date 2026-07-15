package com.vmfg.util.entity;

import java.io.Serializable;

public class PPMDailyInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ppmdId;
	private String producedQuantity;
	private String rejectedQuantity;
	public String getPpmdId() {
		return ppmdId;
	}
	public void setPpmdId(String ppmdId) {
		this.ppmdId = ppmdId;
	}
	public String getProducedQuantity() {
		return producedQuantity;
	}
	public void setProducedQuantity(String producedQuantity) {
		this.producedQuantity = producedQuantity;
	}
	public String getRejectedQuantity() {
		return rejectedQuantity;
	}
	public void setRejectedQuantity(String rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}
	
}
