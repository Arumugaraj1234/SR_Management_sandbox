package com.vmfg.util.entity;

import java.io.Serializable;

public class EmailCreateTaskInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String description;
	private String freqCode;
	private String freqDesc;
	private String priority;
	private String createdBy;
	private String createdByName;
	private String createdOn;
	private String activityName;
	private String dueDate;
	private String transcId;
	private String assginTo;
	private String assignToEmp;
	private String dept;
	
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFreqCode() {
		return freqCode;
	}
	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
	}
	public String getFreqDesc() {
		return freqDesc;
	}
	public void setFreqDesc(String freqDesc) {
		this.freqDesc = freqDesc;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedByName() {
		return createdByName;
	}
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getTranscId() {
		return transcId;
	}
	public void setTranscId(String transcId) {
		this.transcId = transcId;
	}
	public String getAssginTo() {
		return assginTo;
	}
	public void setAssginTo(String assginTo) {
		this.assginTo = assginTo;
	}
	public String getAssignToEmp() {
		return assignToEmp;
	}
	public void setAssignToEmp(String assignToEmp) {
		this.assignToEmp = assignToEmp;
	}
	
	

}
