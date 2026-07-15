package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileManagerDownloadEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageCode;
	private String fileContent;
	private String filePath;
	private String fileName;
	private String fileOriginalName;

}
