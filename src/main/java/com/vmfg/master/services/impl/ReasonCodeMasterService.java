package com.vmfg.master.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.master.dao.interfaces.IReasonCodeMasterDAO;
import com.vmfg.master.entity.ReasonCodeMasterEntity;
import com.vmfg.master.request.ReasonCodeMasterRequest;
import com.vmfg.master.services.interfaces.IReasonCodeMasterService;
@Service
public class ReasonCodeMasterService implements IReasonCodeMasterService{
	private static final Logger logger = LoggerFactory.getLogger(ReasonCodeMasterService.class);
	
	@Autowired
	IReasonCodeMasterDAO iReasonCodeMasterDAO;

	@Override
	public List<ReasonCodeMasterEntity> getReasonCodeInfo(ReasonCodeMasterRequest scop) {
		
		List<ReasonCodeMasterEntity> departmentInfoEntity = null;
		logger.info("getReasonCodeInfoService  method start");
		try {

			String TENANT_ID = scop.getTenantId();
			String EMPLOYEE_ID = scop.getEmployeeId();
			// SERVICES
			if ((null != TENANT_ID && !TENANT_ID.isEmpty() && null != EMPLOYEE_ID && !EMPLOYEE_ID.isEmpty() )) {
				departmentInfoEntity = iReasonCodeMasterDAO.getReasonCodeInfo(TENANT_ID,EMPLOYEE_ID);
			}
		} catch (Exception ex) {
			logger.error("getReasonCodeInfoService  method exception-->" + ex);
		}
		logger.debug("getReasonCodeInfoService  method end");
		return departmentInfoEntity;
	}
}
