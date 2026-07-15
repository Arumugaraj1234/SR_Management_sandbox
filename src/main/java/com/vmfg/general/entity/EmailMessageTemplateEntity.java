package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class EmailMessageTemplateEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String msgTempId;
	private String mecCode;
	private String metCode;
	private String msgSub;
	private String msgBoadyFilePath;
	private String msgFrom;
	private String msgTo;
	private String msgCc;
	private String msgFromUserName;
	private String msgFromPassword;
	private String msgHost;
	private String msgPort;
	private String tenantID;
	
}
