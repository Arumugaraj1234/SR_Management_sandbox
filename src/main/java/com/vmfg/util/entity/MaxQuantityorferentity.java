package com.vmfg.util.entity;

import java.io.Serializable;

public class MaxQuantityorferentity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int maximumorderquantity;

	public int getMaximumorderquantity() {
		return maximumorderquantity;
	}

	public void setMaximumorderquantity(int maximumorderquantity) {
		this.maximumorderquantity = maximumorderquantity;
	}
	
}
