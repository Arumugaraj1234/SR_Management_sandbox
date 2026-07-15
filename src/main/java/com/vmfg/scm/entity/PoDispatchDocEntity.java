package com.vmfg.scm.entity;

import java.io.Serializable;

public class PoDispatchDocEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String podId;
	private String poId;
	private String invoiceNo;
	private String pkgList;
	private String awbBl;
	private String testReports;
	private String certificateOfOrigin;
	private String oMManual;
	private String insuranceWarrentyCert;
	private String inspectionReport;
	public String getPodId() {
		return podId;
	}
	public void setPodId(String podId) {
		this.podId = podId;
	}
	public String getPoId() {
		return poId;
	}
	public void setPoId(String poId) {
		this.poId = poId;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPkgList() {
		return pkgList;
	}
	public void setPkgList(String pkgList) {
		this.pkgList = pkgList;
	}
	public String getAwbBl() {
		return awbBl;
	}
	public void setAwbBl(String awbBl) {
		this.awbBl = awbBl;
	}
	public String getTestReports() {
		return testReports;
	}
	public void setTestReports(String testReports) {
		this.testReports = testReports;
	}
	public String getCertificateOfOrigin() {
		return certificateOfOrigin;
	}
	public void setCertificateOfOrigin(String certificateOfOrigin) {
		this.certificateOfOrigin = certificateOfOrigin;
	}
	public String getoMManual() {
		return oMManual;
	}
	public void setoMManual(String oMManual) {
		this.oMManual = oMManual;
	}
	public String getInsuranceWarrentyCert() {
		return insuranceWarrentyCert;
	}
	public void setInsuranceWarrentyCert(String insuranceWarrentyCert) {
		this.insuranceWarrentyCert = insuranceWarrentyCert;
	}
	public String getInspectionReport() {
		return inspectionReport;
	}
	public void setInspectionReport(String inspectionReport) {
		this.inspectionReport = inspectionReport;
	}

}
