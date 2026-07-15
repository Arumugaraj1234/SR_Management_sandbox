package com.vmfg.util.entity;

import java.io.Serializable;

public class ProductRmMap implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String rmproductcode;
	private int partperstrip;

	public Integer getPartperstrip() {
		return partperstrip;
	}

	public void setPartperstrip(int partperstrip) {
		this.partperstrip = partperstrip;
	}

	public String getRmproductcode() {
		return rmproductcode;
	}

	public void setRmproductcode(String rmproductcode) {
		this.rmproductcode = rmproductcode;
	}

}
