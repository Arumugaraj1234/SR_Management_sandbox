package com.vmfg.task.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRequestCategoryEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String cateId;
	private String cateDesc;	
}
