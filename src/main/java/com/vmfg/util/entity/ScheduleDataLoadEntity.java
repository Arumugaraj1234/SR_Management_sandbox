package com.vmfg.util.entity;

import java.io.Serializable;

public class ScheduleDataLoadEntity implements Serializable{


	private static final long serialVersionUID = 1L;
	private String referenceId;
	private String referenceCode;
	
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getReferenceCode() {
		return referenceCode;
	}
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}
	
	

}
