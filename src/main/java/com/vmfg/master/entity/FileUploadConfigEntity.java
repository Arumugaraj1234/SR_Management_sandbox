package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileUploadConfigEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fuCode;
	private String docCode;
	private String desc;
}
