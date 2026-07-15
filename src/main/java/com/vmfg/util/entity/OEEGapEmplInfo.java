package com.vmfg.util.entity;

import java.io.Serializable;

public class OEEGapEmplInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String preparedbyname;
	private String approvedbyname;
	private String preparedby;
	private String approvedby;
	public String getPreparedbyname() {
		return preparedbyname;
	}
	public void setPreparedbyname(String preparedbyname) {
		this.preparedbyname = preparedbyname;
	}
	public String getApprovedbyname() {
		return approvedbyname;
	}
	public void setApprovedbyname(String approvedbyname) {
		this.approvedbyname = approvedbyname;
	}
	public String getPreparedby() {
		return preparedby;
	}
	public void setPreparedby(String preparedby) {
		this.preparedby = preparedby;
	}
	public String getApprovedby() {
		return approvedby;
	}
	public void setApprovedby(String approvedby) {
		this.approvedby = approvedby;
	}
	
	
	
	
		
}
