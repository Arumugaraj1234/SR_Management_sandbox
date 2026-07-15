package com.vmfg.util.entity;

import java.io.Serializable;

public class MessageTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msgBody;
	private String messageTempid;
	private String messTo;
	private String cc;
	private String msgPath;

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public String getMessageTempid() {
		return messageTempid;
	}

	public void setMessageTempid(String messageTempid) {
		this.messageTempid = messageTempid;
	}

	public String getMessTo() {
		return messTo;
	}

	public void setMessTo(String messTo) {
		this.messTo = messTo;
	}

	public String getMsgPath() {
		return msgPath;
	}

	public void setMsgPath(String msgPath) {
		this.msgPath = msgPath;
	}

}
