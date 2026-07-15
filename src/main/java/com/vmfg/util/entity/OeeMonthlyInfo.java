package com.vmfg.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class OeeMonthlyInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String qrQcMonthId;
	private String planQuantity;
	private String actualQuantity;
	private String gapQUantity;
	private String gapTime;
	private BigDecimal bdTime;
	private String performanceTime;
	private String ngQuantity;
	private String reworkQuantity;
	private String availablity;
	private String performance;
	private String quality;
	private String oee;
	private String jphPlan;
	private String jphActual;
	private String jpmhPlan;
	private String jpmhActual;
	private String plannedProductionTime;
	
	public String getPlannedProductionTime() {
		return plannedProductionTime;
	}
	public void setPlannedProductionTime(String plannedProductionTime) {
		this.plannedProductionTime = plannedProductionTime;
	}
	public String getQrQcMonthId() {
		return qrQcMonthId;
	}
	public void setQrQcMonthId(String qrQcMonthId) {
		this.qrQcMonthId = qrQcMonthId;
	}
	public String getPlanQuantity() {
		return planQuantity;
	}
	public void setPlanQuantity(String planQuantity) {
		this.planQuantity = planQuantity;
	}
	public String getActualQuantity() {
		return actualQuantity;
	}
	public void setActualQuantity(String actualQuantity) {
		this.actualQuantity = actualQuantity;
	}
	public String getGapQUantity() {
		return gapQUantity;
	}
	public void setGapQUantity(String gapQUantity) {
		this.gapQUantity = gapQUantity;
	}
	public String getGapTime() {
		return gapTime;
	}
	public void setGapTime(String gapTime) {
		this.gapTime = gapTime;
	}
	
	public BigDecimal getBdTime() {
		return bdTime;
	}
	public void setBdTime(BigDecimal bdTime) {
		this.bdTime = bdTime;
	}
	public String getPerformanceTime() {
		return performanceTime;
	}
	public void setPerformanceTime(String performanceTime) {
		this.performanceTime = performanceTime;
	}
	public String getNgQuantity() {
		return ngQuantity;
	}
	public void setNgQuantity(String ngQuantity) {
		this.ngQuantity = ngQuantity;
	}
	public String getReworkQuantity() {
		return reworkQuantity;
	}
	public void setReworkQuantity(String reworkQuantity) {
		this.reworkQuantity = reworkQuantity;
	}
	public String getAvailablity() {
		return availablity;
	}
	public void setAvailablity(String availablity) {
		this.availablity = availablity;
	}
	public String getPerformance() {
		return performance;
	}
	public void setPerformance(String performance) {
		this.performance = performance;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getOee() {
		return oee;
	}
	public void setOee(String oee) {
		this.oee = oee;
	}
	public String getJphPlan() {
		return jphPlan;
	}
	public void setJphPlan(String jphPlan) {
		this.jphPlan = jphPlan;
	}
	public String getJphActual() {
		return jphActual;
	}
	public void setJphActual(String jphActual) {
		this.jphActual = jphActual;
	}
	public String getJpmhPlan() {
		return jpmhPlan;
	}
	public void setJpmhPlan(String jpmhPlan) {
		this.jpmhPlan = jpmhPlan;
	}
	public String getJpmhActual() {
		return jpmhActual;
	}
	public void setJpmhActual(String jpmhActual) {
		this.jpmhActual = jpmhActual;
	}
	
}
