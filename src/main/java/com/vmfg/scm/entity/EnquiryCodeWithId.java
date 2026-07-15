package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnquiryCodeWithId implements Serializable {
	private static final long serialVersionUID = 1L;
	private String enquiryCode;
	private int id;
}
