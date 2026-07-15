package com.vmfg.export.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentManagerFileEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageCode;
	private String fileContent;
	private String filePath;
	private String fileName;
	private String fileOriginalName;
	public boolean exists() {
		// TODO Auto-generated method stub
		return true;
	}

}
