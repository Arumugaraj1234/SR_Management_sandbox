package com.vmfg.util.entity;

import java.io.Serializable;

public class EmailMec implements Serializable{

	private static final long serialVersionUID = 1L;
	private String mesDescription;
	private String mesShort;
	public String getMesDescription() {
		return mesDescription;
	}
	public void setMesDescription(String mesDescription) {
		this.mesDescription = mesDescription;
	}
	public String getMesShort() {
		return mesShort;
	}
	public void setMesShort(String mesShort) {
		this.mesShort = mesShort;
	}
	
	
	
}
