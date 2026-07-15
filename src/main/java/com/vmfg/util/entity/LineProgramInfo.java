package com.vmfg.util.entity;

import java.io.Serializable;

public class LineProgramInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lineDesc;
	private String prgmDesc;
	private String Process;
	private String nameDesc;
	private String shift;
	private String refDate;
	
	
	
	public String getRefDate() {
		return refDate;
	}
	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getLineDesc() {
		return lineDesc;
	}
	public void setLineDesc(String lineDesc) {
		this.lineDesc = lineDesc;
	}
	public String getPrgmDesc() {
		return prgmDesc;
	}
	public void setPrgmDesc(String prgmDesc) {
		this.prgmDesc = prgmDesc;
	}
	public String getProcess() {
		return Process;
	}
	public void setProcess(String process) {
		Process = process;
	}
	public String getNameDesc() {
		return nameDesc;
	}
	public void setNameDesc(String nameDesc) {
		this.nameDesc = nameDesc;
	}
	
	
			
}