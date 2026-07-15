package com.vmfg.util.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DocLifecycleVersionEntity {
	 private String version;
	 private String versiondatetime;
	 private String updatedBy;
	 private List<DocLifeCycleMstLogEntity >data= new ArrayList<DocLifeCycleMstLogEntity>();
}
