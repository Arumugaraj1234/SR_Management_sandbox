package com.vmfg.util.entity;

import java.io.Serializable;

public class CalibrationEntryMailHdr implements Serializable{

	private static final long serialVersionUID = 1L;

	private String verifiedbyname;
	private String approvedbyname;
	private String preparedbyname;
	private String verifiedby;
	private String approvedby;
	private String preparedby;


	public String getPreparedbyname() {
		return preparedbyname;
	}
	public void setPreparedbyname(String preparedbyname) {
		this.preparedbyname = preparedbyname;
	}
	public String getPreparedby() {
		return preparedby;
	}
	public void setPreparedby(String preparedby) {
		this.preparedby = preparedby;
	}
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
