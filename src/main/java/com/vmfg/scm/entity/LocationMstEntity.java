package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    private String locationId;
    private String locationRefName;
    private String locAddressLine;
    private String locCity;
    private String locState;
    private String locCountryCode;
    private String locPinCode;
    private String tenantId;

}
