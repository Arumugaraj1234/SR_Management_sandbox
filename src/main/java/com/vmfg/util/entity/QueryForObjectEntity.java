package com.vmfg.util.entity;

import java.io.Serializable;

public class QueryForObjectEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private int numberValue;
	private String strValue;
	public int getNumberValue() {
		return numberValue;
	}
	public void setNumberValue(int numberValue) {
		this.numberValue = numberValue;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

}
