package com.vmfg.util.entity;

import java.io.Serializable;

public class qntyonHandDtlentity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int invtryprdctdtlid;
	private int invtryprdctquantityonhand;
	private String invtrylocationcode;

	public int getInvtryprdctdtlid() {
		return invtryprdctdtlid;
	}

	public void setInvtryprdctdtlid(int invtryprdctdtlid) {
		this.invtryprdctdtlid = invtryprdctdtlid;
	}

	public int getInvtryprdctquantityonhand() {
		return invtryprdctquantityonhand;
	}

	public void setInvtryprdctquantityonhand(int invtryprdctquantityonhand) {
		this.invtryprdctquantityonhand = invtryprdctquantityonhand;
	}

	public String getInvtrylocationcode() {
		return invtrylocationcode;
	}

	public void setInvtrylocationcode(String invtrylocationcode) {
		this.invtrylocationcode = invtrylocationcode;
	}

}
