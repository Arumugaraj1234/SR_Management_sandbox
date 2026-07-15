package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentManagementEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String documentName;
	private String createdBy;
	private String version;
	private String dmId;
	private String documentTypeCode;
	private String documentTypeDescription;
	private String stdCode;
	private String stgDescription;
	private String fileCreatedDate;
	private String empName;
	private String fuCode;
	private String fileName;
	private String remarks;
	private String accessDesc;
	private String fileNameExtn;
	private String isPdf;

}
