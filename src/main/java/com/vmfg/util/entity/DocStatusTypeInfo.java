package com.vmfg.util.entity;

import java.io.Serializable;

public class DocStatusTypeInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String docStatusCode;
	private String docSequence;
	private String approvingDeptCode;
	private String approvingDesCode;
	private String docStatusDesc;
	public String getDocStatusCode() {
		return docStatusCode;
	}
	public void setDocStatusCode(String docStatusCode) {
		this.docStatusCode = docStatusCode;
	}
	public String getDocSequence() {
		return docSequence;
	}
	public void setDocSequence(String docSequence) {
		this.docSequence = docSequence;
	}
	public String getApprovingDeptCode() {
		return approvingDeptCode;
	}
	public void setApprovingDeptCode(String approvingDeptCode) {
		this.approvingDeptCode = approvingDeptCode;
	}
	public String getApprovingDesCode() {
		return approvingDesCode;
	}
	public void setApprovingDesCode(String approvingDesCode) {
		this.approvingDesCode = approvingDesCode;
	}
	public String getDocStatusDesc() {
		return docStatusDesc;
	}
	public void setDocStatusDesc(String docStatusDesc) {
		this.docStatusDesc = docStatusDesc;
	}
	
}
