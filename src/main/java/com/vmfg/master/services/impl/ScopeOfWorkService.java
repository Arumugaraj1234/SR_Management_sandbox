package com.vmfg.master.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.master.dao.interfaces.IScopeOfWorkDAO;
import com.vmfg.master.entity.ScopOfWorkEntity;
import com.vmfg.master.request.ScopeOfWorkRequest;
import com.vmfg.master.services.interfaces.IScopeOfWorkService;

@Service
public class ScopeOfWorkService implements IScopeOfWorkService {
	private static final Logger logger = LoggerFactory.getLogger(ScopeOfWorkService.class);

	@Autowired
	IScopeOfWorkDAO iScopeOfWorkDAO;

	@Override
	public List<ScopOfWorkEntity> getScopeOfWorkInfo(ScopeOfWorkRequest scop) {

		List<ScopOfWorkEntity> departmentInfoEntity = null;
		logger.info("getScopeOfWorkInfoService  method start");
		try {

			String TENANT_ID = scop.getTenantId();
			// SERVICES
			if ((null != TENANT_ID && !TENANT_ID.isEmpty())) {
				departmentInfoEntity = iScopeOfWorkDAO.getScopeOfWorkInfo(TENANT_ID);
			}
		} catch (Exception ex) {
			logger.error("getScopeOfWorkInfoService  method exception-->" + ex);
		}
		logger.debug("getScopeOfWorkInfoService  method end");
		return departmentInfoEntity;
	}
}
