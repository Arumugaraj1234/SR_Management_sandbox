package com.vmfg.general.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class GetAssignTeamDtl {
	String assignTeam;
	List<ProcessAssignedTeamEntity> processAssignedTeamEntity = null;
}

