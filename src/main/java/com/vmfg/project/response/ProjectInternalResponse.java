package com.vmfg.project.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectInternalResponse {
    private String isInternal;

    public String getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(String isInternal) {
        this.isInternal = isInternal;
    }

    public void setProjectCode(String projectCode) {
    }

    public void setTenantId(String tenantId) {

    }
}

