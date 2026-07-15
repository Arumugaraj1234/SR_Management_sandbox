package com.vmfg.util.entity;

import java.io.Serializable;

public class ILUOEmplInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String trainingByName;
	private String trainingBy;
	private String checkedByName;
	private String checkedBy;
	private String approvedBy;
	private String approvedByName;
	
	public String getTrainingByName() {
		return trainingByName;
	}
	public String getTrainingBy() {
		return trainingBy;
	}
	public String getCheckedByName() {
		return checkedByName;
	}
	public String getCheckedBy() {
		return checkedBy;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public String getApprovedByName() {
		return approvedByName;
	}
	public void setTrainingByName(String trainingByName) {
		this.trainingByName = trainingByName;
	}
	public void setTrainingBy(String trainingBy) {
		this.trainingBy = trainingBy;
	}
	public void setCheckedByName(String checkedByName) {
		this.checkedByName = checkedByName;
	}
	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public void setApprovedByName(String approvedByName) {
		this.approvedByName = approvedByName;
	}
	

}
