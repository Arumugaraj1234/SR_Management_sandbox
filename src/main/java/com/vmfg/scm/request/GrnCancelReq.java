package com.vmfg.scm.request;

import com.vmfg.scm.entity.GrnDtlEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GrnCancelReq {
    private String grnHdrId;
    private String poCode;
    private String miId;
    private String inventoryLoc;
    private String remarks;
    private String tenantId;

    private List<GrnDtlEntity> grnDtlEntityList;
}
