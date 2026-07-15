package com.vmfg.task.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskTemplateHdrResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String teHdrId;
	private String tempName;
}
