package com.vmfg.design.entity;

import java.util.List;

import com.vmfg.design.response.KeyAreaIndentId;
import com.vmfg.design.response.KeySubArea;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ChangeReqHdrDtls {
	private List<KeyAreaIndentId> keyArea;
	private List<KeySubArea> subKeyArea;
	private int revisionNo;

}
