package com.vmfg.util.entity;

import java.io.Serializable;

public class ProductInvQnty implements Serializable {
private static final long serialVersionUID = 1L;
	
	private int productinvdtlid;
	private int productQuantityonhand;

	public int getProductinvdtlid() {
		return productinvdtlid;
	}

	public void setProductinvdtlid(int productinvdtlid) {
		this.productinvdtlid = productinvdtlid;
	}

	public int getProductQuantityonhand() {
		return productQuantityonhand;
	}

	public void setProductQuantityonhand(int productQuantityonhand) {
		this.productQuantityonhand = productQuantityonhand;
	}

}
