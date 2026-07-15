package com.vmfg.mis.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberLoadMainEntity {
	private List<TeamMemberLoadEntity> Count = new ArrayList<TeamMemberLoadEntity>(); 
	private List<TeamMemberLoadEntity> Total = new ArrayList<TeamMemberLoadEntity>(); 
}
