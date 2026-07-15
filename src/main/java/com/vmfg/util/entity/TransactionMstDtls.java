package com.vmfg.util.entity;

import java.io.Serializable;

public class TransactionMstDtls implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String prefix;
	private String suffix;
	private String idStart;
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getIdStart() {
		return idStart;
	}
	public void setIdStart(String idStart) {
		this.idStart = idStart;
	}
	

}
