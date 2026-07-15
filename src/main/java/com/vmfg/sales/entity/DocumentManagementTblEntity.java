package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentManagementTblEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String dmId;
	private String enquiryId;
	private String projectId;
	private String documentName;
	private String refId;
	private String stageCode;
	private String uploadDocType;
	private String version;
	private String latestVersion;
	private String approved;
	private String tenantdId;
	private String remarks;
	private String docApprSeq;
}
