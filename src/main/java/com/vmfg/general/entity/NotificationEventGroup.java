package com.vmfg.general.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEventGroup {
	private String event;
	private List<NotificationReqEntity> list;

	public NotificationEventGroup(String event, List<NotificationReqEntity> list) {
		this.event = event;
		this.list = list;
	}
}
