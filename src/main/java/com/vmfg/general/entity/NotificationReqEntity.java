package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationReqEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String NotifiyId;
	private String label;
	private String Description;
	private String empId;
	private String Event;
	private String isNotified;
	private String viewedDatetime;
	private String tenantId;
	
	
}
