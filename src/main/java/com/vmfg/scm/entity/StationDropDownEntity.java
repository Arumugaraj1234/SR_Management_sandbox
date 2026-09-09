package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StationDropDownEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String pkaId;
	private String stationDesc;

}
