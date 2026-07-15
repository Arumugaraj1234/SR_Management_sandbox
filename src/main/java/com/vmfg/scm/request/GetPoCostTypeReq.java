package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPoCostTypeReq {
    private String tenantId;
    private String isActive;
}
