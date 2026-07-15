package com.vmfg.util.entity;

import java.io.Serializable;

public class EmailApprovalEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String issuedbyname;
	private String approvedbyname;
	private String checkedbyname;
	private String issuedby;
	private String approvedby;
	private String checkedby;
	public String getIssuedbyname() {
		return issuedbyname;
	}
	public void setIssuedbyname(String issuedbyname) {
		this.issuedbyname = issuedbyname;
	}
	public String getApprovedbyname() {
		return approvedbyname;
	}
	public void setApprovedbyname(String approvedbyname) {
		this.approvedbyname = approvedbyname;
	}
	public String getCheckedbyname() {
		return checkedbyname;
	}
	public void setCheckedbyname(String checkedbyname) {
		this.checkedbyname = checkedbyname;
	}
	public String getIssuedby() {
		return issuedby;
	}
	public void setIssuedby(String issuedby) {
		this.issuedby = issuedby;
	}
	public String getApprovedby() {
		return approvedby;
	}
	public void setApprovedby(String approvedby) {
		this.approvedby = approvedby;
	}
	public String getCheckedby() {
		return checkedby;
	}
	public void setCheckedby(String checkedby) {
		this.checkedby = checkedby;
	}
	
			
}
