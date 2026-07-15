package com.vmfg.scm.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PoCostTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String pctId;
    private String pctDesc;
    private String isActive;
    private String tenantId;
}
