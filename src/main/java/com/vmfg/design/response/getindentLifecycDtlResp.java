package com.vmfg.design.response;

import java.util.List;

import com.vmfg.design.entity.GetIndentLifecycDtlEntity;
import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getindentLifecycDtlResp {

	private List<DocumentStatusMstEntity> seqList;
	private List<GetIndentLifecycDtlEntity>indentList;
}
