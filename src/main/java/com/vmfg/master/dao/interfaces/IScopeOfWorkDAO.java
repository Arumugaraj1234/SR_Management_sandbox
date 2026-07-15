package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.ScopOfWorkEntity;

public interface IScopeOfWorkDAO {

	List<ScopOfWorkEntity> getScopeOfWorkInfo(String tENANT_ID);

}
