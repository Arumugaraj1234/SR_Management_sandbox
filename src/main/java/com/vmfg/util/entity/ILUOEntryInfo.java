package com.vmfg.util.entity;

import java.io.Serializable;

public class ILUOEntryInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String operatorName;
	private String process;
	private String trainingType;
	
	
	public String getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public String getProcess() {
		return process;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public void setProcess(String process) {
		this.process = process;
	}
}
