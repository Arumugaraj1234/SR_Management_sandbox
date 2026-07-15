package com.vmfg.general.response;

import java.util.List;

import com.vmfg.general.entity.CancelSeqEntity;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.StatusDtlEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetprocessEnbleStatusResponse {

	private List<DocumentStatusMstEntity>documentStatusMstlist;
	private CancelSeqEntity cancelseq;
	private List<StatusDtlEntity> statusDtl;
}
