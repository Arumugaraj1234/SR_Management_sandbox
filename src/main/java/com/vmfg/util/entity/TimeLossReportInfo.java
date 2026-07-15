package com.vmfg.util.entity;

import java.io.Serializable;

public class TimeLossReportInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String timeLossDtlid;
	private String timeLossDuration;
	private String productCode;
	private String reasonCode;
	private String operationCode;
	private String maximumTimeLossProduct;
	private String maximumLossDuration;
	public String getTimeLossDtlid() {
		return timeLossDtlid;
	}
	public void setTimeLossDtlid(String timeLossDtlid) {
		this.timeLossDtlid = timeLossDtlid;
	}
	public String getTimeLossDuration() {
		return timeLossDuration;
	}
	public void setTimeLossDuration(String timeLossDuration) {
		this.timeLossDuration = timeLossDuration;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public String getMaximumTimeLossProduct() {
		return maximumTimeLossProduct;
	}
	public void setMaximumTimeLossProduct(String maximumTimeLossProduct) {
		this.maximumTimeLossProduct = maximumTimeLossProduct;
	}
	public String getMaximumLossDuration() {
		return maximumLossDuration;
	}
	public void setMaximumLossDuration(String maximumLossDuration) {
		this.maximumLossDuration = maximumLossDuration;
	}
	
}
