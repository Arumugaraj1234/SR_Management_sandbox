package com.vmfg.util.entity;

import java.io.Serializable;

public class PPMDetails implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String producedQty;
	private String rejectedQty;
	private String rpmId;
	public String getProducedQty() {
		return producedQty;
	}
	public void setProducedQty(String producedQty) {
		this.producedQty = producedQty;
	}
	public String getRejectedQty() {
		return rejectedQty;
	}
	public void setRejectedQty(String rejectedQty) {
		this.rejectedQty = rejectedQty;
	}
	public String getRpmId() {
		return rpmId;
	}
	public void setRpmId(String rpmId) {
		this.rpmId = rpmId;
	}
	

}
