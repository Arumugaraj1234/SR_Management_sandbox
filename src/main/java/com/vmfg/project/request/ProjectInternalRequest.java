package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectInternalRequest {
    private String tenantId;
    private String projectCode;
}
