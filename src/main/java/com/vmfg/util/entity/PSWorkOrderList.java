package com.vmfg.util.entity;

public class PSWorkOrderList {

	private String workOrderId;
	private String woStatus;
	private String woCreatedDate;
	private String woPlannedStartDate;
	private String woPlannedEndDate;
	private String productQuantity;
	private String shift;
	private String sequence;
	private String routeID;
	private int processStatus;
	private String productCode;
	
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public int getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}
	public String getWorkOrderId() {
		return workOrderId;
	}
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
	public String getWoStatus() {
		return woStatus;
	}
	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}
	public String getWoCreatedDate() {
		return woCreatedDate;
	}
	public void setWoCreatedDate(String woCreatedDate) {
		this.woCreatedDate = woCreatedDate;
	}
	public String getWoPlannedStartDate() {
		return woPlannedStartDate;
	}
	public void setWoPlannedStartDate(String woPlannedStartDate) {
		this.woPlannedStartDate = woPlannedStartDate;
	}
	public String getWoPlannedEndDate() {
		return woPlannedEndDate;
	}
	public void setWoPlannedEndDate(String woPlannedEndDate) {
		this.woPlannedEndDate = woPlannedEndDate;
	}
	public String getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(String productQuantity) {
		this.productQuantity = productQuantity;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getRouteID() {
		return routeID;
	}
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}
	
}
