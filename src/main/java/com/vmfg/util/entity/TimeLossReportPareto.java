package com.vmfg.util.entity;

public class TimeLossReportPareto {

	private String timeLossDuration;
	private String reasonDesc;
	private String percentage;
	private String reasonCode;
	private String timeLossCount;
	
	public String getTimeLossCount() {
		return timeLossCount;
	}
	public void setTimeLossCount(String timeLossCount) {
		this.timeLossCount = timeLossCount;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getTimeLossDuration() {
		return timeLossDuration;
	}
	public void setTimeLossDuration(String timeLossDuration) {
		this.timeLossDuration = timeLossDuration;
	}
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	
}
