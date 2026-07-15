package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentGrpScpVenEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String igScpVid;
	private String igScpId;
	private String l1VendorCode;
	private String l1VendorName;
	private String l1VendorCountry;
	private String l1VendorCurrency;
	private String l2VendorCode;
	private String l2VendorName;
	private String l2VendorCountry;
	private String l2VendorCurrency;
	private String l3VendorCode;
	private String l3VendorName;
	private String l3VendorCountry;
	private String l3VendorCurrency;
	private String level;
	private String lastUpdatedDate;
	private String lastUpdatedBy;
	private String l1Gst;
	private String l2Gst;
	private String l3Gst;
	private String l1VendorUniqueCode;
	private String l2VendorUniqueCode;
	private String l3VendorUniqueCode;
}


