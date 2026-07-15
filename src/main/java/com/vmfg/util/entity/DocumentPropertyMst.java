package com.vmfg.util.entity;

import java.io.Serializable;

public class DocumentPropertyMst implements Serializable{


	private static final long serialVersionUID = 1L;
	private String sequence;
	private String approvingDepartment;
	private String approverDesignation;
	private String statusTypeCode;
	private String documentStatusDesc;
	private String notificationReq;
	private String docLifeCycMstId;
	private String approvingEmpId;
	
	public String getNotificationReq() {
		return notificationReq;
	}
	public void setNotificationReq(String notificationReq) {
		this.notificationReq = notificationReq;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getApprovingDepartment() {
		return approvingDepartment;
	}
	public void setApprovingDepartment(String approvingDepartment) {
		this.approvingDepartment = approvingDepartment;
	}
	public String getApproverDesignation() {
		return approverDesignation;
	}
	public void setApproverDesignation(String approverDesignation) {
		this.approverDesignation = approverDesignation;
	}
	public String getStatusTypeCode() {
		return statusTypeCode;
	}
	public void setStatusTypeCode(String statusTypeCode) {
		this.statusTypeCode = statusTypeCode;
	}
	public String getDocumentStatusDesc() {
		return documentStatusDesc;
	}
	public void setDocumentStatusDesc(String documentStatusDesc) {
		this.documentStatusDesc = documentStatusDesc;
	}
	public String getDocLifeCycMstId() {
		return docLifeCycMstId;
	}
	public void setDocLifeCycMstId(String docLifeCycMstId) {
		this.docLifeCycMstId = docLifeCycMstId;
	}
	public String getApprovingEmpId() {
		return approvingEmpId;
	}
	public void setApprovingEmpId(String approvingEmpId) {
		this.approvingEmpId = approvingEmpId;
	}
	

}
