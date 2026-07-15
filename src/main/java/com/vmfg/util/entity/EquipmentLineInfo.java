package com.vmfg.util.entity;

import java.io.Serializable;

public class EquipmentLineInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String equipmentDesc;
	private String lineDesc;

	public String getEquipmentDesc() {
		return equipmentDesc;
	}

	public String getLineDesc() {
		return lineDesc;
	}

	public void setEquipmentDesc(String equipmentDesc) {
		this.equipmentDesc = equipmentDesc;
	}

	public void setLineDesc(String lineDesc) {
		this.lineDesc = lineDesc;
	}

}
