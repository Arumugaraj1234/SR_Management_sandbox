package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendorRatingEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String vdtlId;
    private String vendorCode;
    private String vendorName;
    private String inspectionDate;
    private String inspectionRating;
    private String inspectedOn;
    private String inspectedBy;
    private String dmId;
}
