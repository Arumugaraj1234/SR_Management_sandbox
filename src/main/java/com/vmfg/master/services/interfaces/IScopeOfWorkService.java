package com.vmfg.master.services.interfaces;

import java.util.List;

import com.vmfg.master.entity.ScopOfWorkEntity;
import com.vmfg.master.request.ScopeOfWorkRequest;

public interface IScopeOfWorkService {

	List<ScopOfWorkEntity> getScopeOfWorkInfo(ScopeOfWorkRequest scop);

}
