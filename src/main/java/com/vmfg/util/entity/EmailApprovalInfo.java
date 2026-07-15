package com.vmfg.util.entity;

import java.io.Serializable;

public class EmailApprovalInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sequence;
	private String documentStatusDesc;
	private String docMstId;
	private String emplName;
	private String toEmail;
	private String ccEmail;
	private int noOfAppr;
	private String isCompleted;
	private String forMsg;
	
	
	public String getForMsg() {
		return forMsg;
	}

	public void setForMsg(String forMsg) {
		this.forMsg = forMsg;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public String getEmplName() {
		return emplName;
	}

	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}

	public int getNoOfAppr() {
		return noOfAppr;
	}

	public void setNoOfAppr(int noOfAppr) {
		this.noOfAppr = noOfAppr;
	}

	public String getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getDocMstId() {
		return docMstId;
	}

	public void setDocMstId(String docMstId) {
		this.docMstId = docMstId;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getDocumentStatusDesc() {
		return documentStatusDesc;
	}

	public void setDocumentStatusDesc(String documentStatusDesc) {
		this.documentStatusDesc = documentStatusDesc;
	}

}
