package com.vmfg.util.entity;

import java.io.Serializable;

public class Rmproductqntyentity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String rmproductcode;
	private int productquantity;
	private int workorderid;
	private int workorderqunty;

	public int getProductquantity() {
		return productquantity;
	}

	public void setProductquantity(int productquantity) {
		this.productquantity = productquantity;
	}

	public String getRmproductcode() {
		return rmproductcode;
	}

	public void setRmproductcode(String rmproductcode) {
		this.rmproductcode = rmproductcode;
	}

	public int getWorkorderid() {
		return workorderid;
	}

	public void setWorkorderid(int workorderid) {
		this.workorderid = workorderid;
	}

	public int getWorkorderqunty() {
		return workorderqunty;
	}

	public void setWorkorderqunty(int workorderqunty) {
		this.workorderqunty = workorderqunty;
	}
	
}
