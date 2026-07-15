package com.vmfg.util.entity;

import java.io.Serializable;

public class lastUpdatedValueentity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int picklistid;

	public int getPicklistid() {
		return picklistid;
	}

	public void setPicklistid(int picklistid) {
		this.picklistid = picklistid;
	}
}
