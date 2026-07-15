package com.vmfg.design.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadIndentTemplateEntity {
	private int sNo;
	private String itemNo;
	private String partNumber;
	private String weight;
	private String material;
	private String qty;
	private String desc;
	private String make;
	private String specification;
	private String unit;
	private String duplicateRecord;
	private String remarks;
	private String indentDtlId;

}
