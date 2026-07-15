package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectInitiationUpdateRequest {
     private String primaryPoc;
     private String masterPoc;
     private String piId;
     private String departmentAssigned;
}
