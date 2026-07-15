package com.vmfg.util.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MessageTemplateEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msgTempId;

	private String mecCode;

	private String metCode;

	private String msgSub;

	private String msgBodyFilePath;

	private String msgFrom;

	private String msgTo;

	private String msgCc;

	private String msgBcc;

	private String msgPriority;

	private String msgEngineType;

	private String msgFromUsername;

	private String msgFromPassword;

	private String msgFromHost;

	private String msgFromPort;

	private String lastUpdatedUserId;

	private String lastUpdatedDatetime;

	private String isActive;

	private String tenantId;

}
