package com.vmfg.master.request;

import java.util.ArrayList;
import java.util.List;

import com.vmfg.master.entity.VendorRatingEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorRatingResponse {
	private String inspRaisedBtn;
	private String newRating;
	private String nextInspectionOn;
	private int inspReqRaised;
	private List<VendorRatingEntity> list = new ArrayList<VendorRatingEntity>();
}
