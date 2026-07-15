package com.vmfg.util.entity;

import java.io.Serializable;

public class ShiftLossDurationList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String totalDuration;
	private String availablityLoss;
	private String performanceLoss;
	public String getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}
	public String getAvailablityLoss() {
		return availablityLoss;
	}
	public void setAvailablityLoss(String availablityLoss) {
		this.availablityLoss = availablityLoss;
	}
	public String getPerformanceLoss() {
		return performanceLoss;
	}
	public void setPerformanceLoss(String performanceLoss) {
		this.performanceLoss = performanceLoss;
	}
	
	
}
