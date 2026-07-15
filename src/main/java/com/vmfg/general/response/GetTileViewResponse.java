package com.vmfg.general.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTileViewResponse {

	private String statusDesc;
	private List<?> enqList;
}
