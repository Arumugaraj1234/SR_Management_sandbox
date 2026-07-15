package com.vmfg.util.entity;

import java.io.Serializable;

public class PrHdrEmail implements Serializable {

	private static final long serialVersionUID = 1L;
	private String prhdrId;
	private String transactionUI;
	private String currentSeq;
	private String requestedBy;
	private String deptHead;
	private String procurementHead;
	private String financeHead;
	private String gmApproval;
	private String requestedByEmail;
	private String deptHeadEmail;
	private String procurementHeadEmail;
	private String financeHeadEmail;
	private String gmApprovalEmail;
	private String lastUpdatedBy;
	private String reqDep;
	private String reqName;
	private String appGmId;
	private String appGmmailId;
	public String getAppGmId() {
		return appGmId;
	}

	public void setAppGmId(String appGmId) {
		this.appGmId = appGmId;
	}

	public String getAppGmEmail() {
		return appGmEmail;
	}

	public void setAppGmEmail(String appGmEmail) {
		this.appGmEmail = appGmEmail;
	}

	private String appGmEmail;
	

	public String getReqDep() {
		return reqDep;
	}

	public void setReqDep(String reqDep) {
		this.reqDep = reqDep;
	}

	public String getReqName() {
		return reqName;
	}

	public void setReqName(String reqName) {
		this.reqName = reqName;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRequestedByEmail() {
		return requestedByEmail;
	}

	public void setRequestedByEmail(String requestedByEmail) {
		this.requestedByEmail = requestedByEmail;
	}

	public String getDeptHeadEmail() {
		return deptHeadEmail;
	}

	public void setDeptHeadEmail(String deptHeadEmail) {
		this.deptHeadEmail = deptHeadEmail;
	}

	public String getProcurementHeadEmail() {
		return procurementHeadEmail;
	}

	public void setProcurementHeadEmail(String procurementHeadEmail) {
		this.procurementHeadEmail = procurementHeadEmail;
	}

	public String getFinanceHeadEmail() {
		return financeHeadEmail;
	}

	public void setFinanceHeadEmail(String financeHeadEmail) {
		this.financeHeadEmail = financeHeadEmail;
	}

	public String getGmApprovalEmail() {
		return gmApprovalEmail;
	}

	public void setGmApprovalEmail(String gmApprovalEmail) {
		this.gmApprovalEmail = gmApprovalEmail;
	}

	public String getPrhdrId() {
		return prhdrId;
	}

	public void setPrhdrId(String prhdrId) {
		this.prhdrId = prhdrId;
	}

	public String getTransactionUI() {
		return transactionUI;
	}

	public void setTransactionUI(String transactionUI) {
		this.transactionUI = transactionUI;
	}

	public String getCurrentSeq() {
		return currentSeq;
	}

	public void setCurrentSeq(String currentSeq) {
		this.currentSeq = currentSeq;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getDeptHead() {
		return deptHead;
	}

	public void setDeptHead(String deptHead) {
		this.deptHead = deptHead;
	}

	public String getProcurementHead() {
		return procurementHead;
	}

	public void setProcurementHead(String procurementHead) {
		this.procurementHead = procurementHead;
	}

	public String getFinanceHead() {
		return financeHead;
	}

	public void setFinanceHead(String financeHead) {
		this.financeHead = financeHead;
	}

	public String getGmApproval() {
		return gmApproval;
	}

	public void setGmApproval(String gmApproval) {
		this.gmApproval = gmApproval;
	}

	public String getAppGmmailId() {
		return appGmmailId;
	}

	public void setAppGmmailId(String appGmmailId) {
		this.appGmmailId = appGmmailId;
	}

}
