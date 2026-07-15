package com.vmfg.util.entity;

import java.io.Serializable;

public class PickListHdrIdentity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int picklisthdrid;

	public int getPicklisthdrid() {
		return picklisthdrid;
	}

	public void setPicklisthdrid(int picklisthdrid) {
		this.picklisthdrid = picklisthdrid;
	}
}
