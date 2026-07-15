/**
 * 
 */
package com.vmfg.design.request;

import java.util.List;

import com.vmfg.design.entity.UploadIndentTemplateEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class InsertIndentRequest {
	private String indentDate;
	private String sbcCode;
	private String projectId;
	private String pmId;
	private String masterId;
	private String empId;
	private String docType;
	private String seq;
	private String tenantId;
	private String pkaId;
	private String pksaId;
	private String expectedDeliveryDate;
	private String indentType;
	private String indentId;
	private List<UploadIndentTemplateEntity> dtlList;
	
}
