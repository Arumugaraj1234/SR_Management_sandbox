package com.vmfg.util.entity;

import java.io.Serializable;

public class WOGroupEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String woId;
	private String prodCode;
	private String prodDes;
	private String woPlStartDate;
	private String sequence;
	private String routeId;
	public String getWoId() {
		return woId;
	}
	public void setWoId(String woId) {
		this.woId = woId;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getProdDes() {
		return prodDes;
	}
	public void setProdDes(String prodDes) {
		this.prodDes = prodDes;
	}
	public String getWoPlStartDate() {
		return woPlStartDate;
	}
	public void setWoPlStartDate(String woPlStartDate) {
		this.woPlStartDate = woPlStartDate;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

}
