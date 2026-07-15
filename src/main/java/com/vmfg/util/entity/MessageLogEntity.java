package com.vmfg.util.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MessageLogEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msgLogId;

	private String msgTempId;

	private String sendMsgFilePath;

	private String msgTo;

	private String msgCc;
	
	private String msgLogDatetime;

	private String msgLogDate;

	private String msgSentStatus;

	private String lastUpdatedDatetime;

	private String tenantId;

	private String userRefId;
	
    private String isAttachmentAvailable;
	
	private String attachmentPath;
	
	private String msgBody;
	
	private String msgSubject;

}
