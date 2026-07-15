package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCategoryDrpdwnEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tcCode;
	private String tcDesc;
}
