package com.vmfg.master.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.master.dao.interfaces.IIndustryTypeDAO;
import com.vmfg.master.entity.IndustryTypeEntity;
import com.vmfg.master.request.IndustryTypeRequest;
import com.vmfg.master.services.interfaces.IIndustryTypeService;

@Service
public class IndustryTypeService implements IIndustryTypeService {
	private static final Logger logger = LoggerFactory.getLogger(IndustryTypeService.class);
	@Autowired
	IIndustryTypeDAO iIndustryTypeDAO;

	@Override
	public List<IndustryTypeEntity> getIndustryTypeInfo(IndustryTypeRequest scop) {

		List<IndustryTypeEntity> departmentInfoEntity = null;
		logger.info("getIndustryTypeInfoService  method start");
		try {

			String TENANT_ID = scop.getTenantId();
			// SERVICES
			if ((null != TENANT_ID && !TENANT_ID.isEmpty())) {
				departmentInfoEntity = iIndustryTypeDAO.getIndustryTypeInfo(TENANT_ID);
			}
		} catch (Exception ex) {
			logger.error("getIndustryTypeInfoService  method exception-->" + ex);
		}
		logger.debug("getIndustryTypeInfoService  method end");
		return departmentInfoEntity;
	}

}
