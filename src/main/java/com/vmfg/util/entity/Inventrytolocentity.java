package com.vmfg.util.entity;

import java.io.Serializable;

public class Inventrytolocentity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tolocationcode;

	public String getTolocationcode() {
		return tolocationcode;
	}

	public void setTolocationcode(String tolocationcode) {
		this.tolocationcode = tolocationcode;
	}
}
