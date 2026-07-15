package com.vmfg.util.entity;

import java.io.Serializable;

public class OperationCalibrationEntryHdr implements Serializable{


	private static final long serialVersionUID = 1L;

	private String verifiedbyname;
	private String approvedbyname;
	private String verifiedby;
	private String approvedby;
	
	public String getApprovedbyname() {
		return approvedbyname;
	}
	public void setApprovedbyname(String approvedbyname) {
		this.approvedbyname = approvedbyname;
	}
	public String getVerifiedbyname() {
		return verifiedbyname;
	}
	public String getVerifiedby() {
		return verifiedby;
	}
	public void setVerifiedbyname(String verifiedbyname) {
		this.verifiedbyname = verifiedbyname;
	}
	public void setVerifiedby(String verifiedby) {
		this.verifiedby = verifiedby;
	}
	public String getApprovedby() {
		return approvedby;
	}
	public void setApprovedby(String approvedby) {
		this.approvedby = approvedby;
	}

}
