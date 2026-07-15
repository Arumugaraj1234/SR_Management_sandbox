package com.vmfg.util.entity;

import java.io.Serializable;

public class PokaYokeEmplInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String auditorName;
	private String auditorBy;
	private String auditeeName;
	private String auditeeBy;
	private String qaVerifiedName;
	private String qaVerifiedBy;
	private String qaApprovedName;
	private String qaApprovedBy;
	private String qaProductionApprovedBy;
	private String qaProductionApprovedName;

	public String getAuditorName() {
		return auditorName;
	}

	public String getAuditorBy() {
		return auditorBy;
	}

	public String getAuditeeName() {
		return auditeeName;
	}

	public String getAuditeeBy() {
		return auditeeBy;
	}

	public String getQaVerifiedName() {
		return qaVerifiedName;
	}

	public String getQaVerifiedBy() {
		return qaVerifiedBy;
	}

	public String getQaApprovedName() {
		return qaApprovedName;
	}

	public String getQaApprovedBy() {
		return qaApprovedBy;
	}

	public String getQaProductionApprovedBy() {
		return qaProductionApprovedBy;
	}

	public String getQaProductionApprovedName() {
		return qaProductionApprovedName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public void setAuditorBy(String auditorBy) {
		this.auditorBy = auditorBy;
	}

	public void setAuditeeName(String auditeeName) {
		this.auditeeName = auditeeName;
	}

	public void setAuditeeBy(String auditeeBy) {
		this.auditeeBy = auditeeBy;
	}

	public void setQaVerifiedName(String qaVerifiedName) {
		this.qaVerifiedName = qaVerifiedName;
	}

	public void setQaVerifiedBy(String qaVerifiedBy) {
		this.qaVerifiedBy = qaVerifiedBy;
	}

	public void setQaApprovedName(String qaApprovedName) {
		this.qaApprovedName = qaApprovedName;
	}

	public void setQaApprovedBy(String qaApprovedBy) {
		this.qaApprovedBy = qaApprovedBy;
	}

	public void setQaProductionApprovedBy(String qaProductionApprovedBy) {
		this.qaProductionApprovedBy = qaProductionApprovedBy;
	}

	public void setQaProductionApprovedName(String qaProductionApprovedName) {
		this.qaProductionApprovedName = qaProductionApprovedName;
	}

}
