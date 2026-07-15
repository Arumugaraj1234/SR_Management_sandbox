package com.vmfg.util.entity;

import java.io.Serializable;

public class RejectionReportEntity implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String shift;
	private String shiftDate;
	private String rejectionReason;
	private String operation;
	private String product;
	private String rejectionQty;
	private String reportRejectionId;
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getShiftDate() {
		return shiftDate;
	}
	public void setShiftDate(String shiftDate) {
		this.shiftDate = shiftDate;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getRejectionQty() {
		return rejectionQty;
	}
	public void setRejectionQty(String rejectionQty) {
		this.rejectionQty = rejectionQty;
	}
	public String getReportRejectionId() {
		return reportRejectionId;
	}
	public void setReportRejectionId(String reportRejectionId) {
		this.reportRejectionId = reportRejectionId;
	}
	
}
