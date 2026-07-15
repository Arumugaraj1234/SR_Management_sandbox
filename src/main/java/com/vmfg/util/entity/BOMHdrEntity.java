package com.vmfg.util.entity;

import java.io.Serializable;

public class BOMHdrEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int bomhdrid;
	private String bomhdrpartcode;
	private String tenantid;
	public int getBomhdrid() {
		return bomhdrid;
	}
	public void setBomhdrid(int bomhdrid) {
		this.bomhdrid = bomhdrid;
	}
	public String getBomhdrpartcode() {
		return bomhdrpartcode;
	}
	public void setBomhdrpartcode(String bomhdrpartcode) {
		this.bomhdrpartcode = bomhdrpartcode;
	}
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	
	

}
