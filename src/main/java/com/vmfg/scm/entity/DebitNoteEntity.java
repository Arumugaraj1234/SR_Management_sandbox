package com.vmfg.scm.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitNoteEntity {

    private String dnrId;
    private String dnrDesc;
    private String isActive;
    private String tenantId;
}
