package com.vmfg.util.entity;

import java.io.Serializable;

public class OEEQualityinfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String availability;
	private String performance;
	private String availabilityDecimal;
	private String performanceDecimal;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAvailabilityDecimal() {
		return availabilityDecimal;
	}

	public void setAvailabilityDecimal(String availabilityDecimal) {
		this.availabilityDecimal = availabilityDecimal;
	}

	public String getPerformanceDecimal() {
		return performanceDecimal;
	}

	public void setPerformanceDecimal(String performanceDecimal) {
		this.performanceDecimal = performanceDecimal;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

}
