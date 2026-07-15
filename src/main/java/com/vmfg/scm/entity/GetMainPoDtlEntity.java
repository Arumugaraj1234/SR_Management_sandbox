package com.vmfg.scm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GetMainPoDtlEntity implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private List<GetPoDtlsEntity> mainEntity = new ArrayList<GetPoDtlsEntity>();
	
	private List<GetPoDtlsEntity> preRevisionEntity = new ArrayList<GetPoDtlsEntity>();

	public List<GetPoDtlsEntity> getPreRevisionEntity() {
		return preRevisionEntity;
	}

	public void setPreRevisionEntity(List<GetPoDtlsEntity> preRevisionEntity) {
		this.preRevisionEntity = preRevisionEntity;
	}

	public List<GetPoDtlsEntity> getMainEntity() {
		return mainEntity;
	}

	public void setMainEntity(List<GetPoDtlsEntity> mainEntity) {
		this.mainEntity = mainEntity;
	}


}
