package com.vmfg.sales.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class ApprovedDocEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String documentType;
	private String document;
	private String dmId;
	private String uploadDocType;
	private String UploadDocument;
	private String version;
	private String documentName;
	private String createdBy;
	private String remarks;
	private String createdDate;
	private int approveBtnEnabled;
	private String referenceId;
	private String stageCode;
	private String filename;
	private String isPdf;
	List<DocumentAppStatusDtlEntity> approvalDetails;

	

}
