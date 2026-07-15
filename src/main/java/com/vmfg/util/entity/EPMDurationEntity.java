package com.vmfg.util.entity;

import java.io.Serializable;

public class EPMDurationEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String equipId;
	private String equipDes;
	private String oprId;
	private String oprDesc;
	private String sequence;
	private String routeId;
	private String prodCode;
	private String prodDesc;
	private String duration;
	public String getEquipId() {
		return equipId;
	}
	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}
	public String getEquipDes() {
		return equipDes;
	}
	public void setEquipDes(String equipDes) {
		this.equipDes = equipDes;
	}
	public String getOprId() {
		return oprId;
	}
	public void setOprId(String oprId) {
		this.oprId = oprId;
	}
	public String getOprDesc() {
		return oprDesc;
	}
	public void setOprDesc(String oprDesc) {
		this.oprDesc = oprDesc;
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
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getProdDesc() {
		return prodDesc;
	}
	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
}
