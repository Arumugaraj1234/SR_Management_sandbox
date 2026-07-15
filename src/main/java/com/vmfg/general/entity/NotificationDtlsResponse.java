package com.vmfg.general.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDtlsResponse {
private int count;
private List<NotificationEventGroup> list;

}
