package com.vmfg.util.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentLifeCycleInsertRequest {

	List<DocLifeCycleListEntity> insertArr=null; 
	List<DocLifeCycleListEntity> deletetArr=null;
	
}
