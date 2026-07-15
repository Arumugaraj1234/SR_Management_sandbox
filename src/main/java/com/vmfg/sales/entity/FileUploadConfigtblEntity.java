package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadConfigtblEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String documentTypeCode;
	private String tenantId;
	private String fuCode;
	private String description;

}
