package com.vmfg.util.entity;

import java.io.Serializable;

public class ScheduleConfigInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String verifiedbyname;
	private String approvedbyname;
	private String preparedbyname;
	private String verifiedby;
	private String approvedby;
	private String preparedby;

	public String getVerifiedbyname() {
		return verifiedbyname;
	}

	public String getApprovedbyname() {
		return approvedbyname;
	}

	public String getPreparedbyname() {
		return preparedbyname;
	}

	public String getVerifiedby() {
		return verifiedby;
	}

	public String getApprovedby() {
		return approvedby;
	}

	public String getPreparedby() {
		return preparedby;
	}

	public void setVerifiedbyname(String verifiedbyname) {
		this.verifiedbyname = verifiedbyname;
	}

	public void setApprovedbyname(String approvedbyname) {
		this.approvedbyname = approvedbyname;
	}

	public void setPreparedbyname(String preparedbyname) {
		this.preparedbyname = preparedbyname;
	}

	public void setVerifiedby(String verifiedby) {
		this.verifiedby = verifiedby;
	}

	public void setApprovedby(String approvedby) {
		this.approvedby = approvedby;
	}

	public void setPreparedby(String preparedby) {
		this.preparedby = preparedby;
	}

}
