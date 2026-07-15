package com.vmfg.util.entity;

import java.io.Serializable;

public class ShiftOTInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lineDesc;
	private String fromTime;
	private String toTime;

	public String getLineDesc() {
		return lineDesc;
	}

	public String getFromTime() {
		return fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setLineDesc(String lineDesc) {
		this.lineDesc = lineDesc;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

}
