package com.vmfg.util.entity;

import java.io.Serializable;

public class PerformanceReportEntity implements Serializable{


	private static final long serialVersionUID = 1L;
	private String producedQty;
	private String plannedQty;
	private String rpId;
	public String getRpId() {
		return rpId;
	}
	public void setRpId(String rpId) {
		this.rpId = rpId;
	}
	public String getProducedQty() {
		return producedQty;
	}
	public void setProducedQty(String producedQty) {
		this.producedQty = producedQty;
	}
	public String getPlannedQty() {
		return plannedQty;
	}
	public void setPlannedQty(String plannedQty) {
		this.plannedQty = plannedQty;
	}
	
	

}
