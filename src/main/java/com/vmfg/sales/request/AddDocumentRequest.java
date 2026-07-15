package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AddDocumentRequest {
	private String enquiryId;
	private String tenantId;
	private String documentType;
	private String uploadDocType;
	private String remarks;
	private String empId;
	private String referenceId;
	private String projectId;
	private String stageCode;
	private String documentName;
	
	

}
