package com.vmfg.master.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.master.dao.interfaces.ITaskTypeDAO;
import com.vmfg.master.entity.TaskTypeEntity;
import com.vmfg.master.request.TaskTypeRequest;
import com.vmfg.master.request.insertUpdateTaskTypeRequest;
import com.vmfg.master.services.interfaces.ITaskTypeService;

@Service
public class TaskTypeService implements ITaskTypeService {
	private static final Logger logger = LoggerFactory.getLogger(IndustryTypeService.class);

	@Autowired ITaskTypeDAO iTaskTypeDAO;
	
	@Override
	public ResponseAsList getTasktypeDtls(TaskTypeRequest taskType) {

		ResponseAsList returnList = new ResponseAsList();
		logger.info("getTasktypeDtls  method start");
		String deptCode = taskType.getDeptCode();
		String tenantId = taskType.getTenantId();
		try {
			List<TaskTypeEntity> list = new ArrayList<TaskTypeEntity>();
				list = iTaskTypeDAO.getTasktypeDtls(deptCode,tenantId);
			
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getTasktypeDtlsService  method exception-->" + ex);
		}
		logger.debug("getTasktypeDtlsService  method end");
		return returnList;
	}

	@Override
	public ResponseAsMessage insertUpdateTaskType(insertUpdateTaskTypeRequest insertDtlreq) {
		
		ResponseAsMessage returnList = new ResponseAsMessage();
		String deptCode = insertDtlreq.getDeptCode();
		String ttCode = insertDtlreq.getTtcode();
		String tenantId = insertDtlreq.getTenantId();
		String ttDesc = insertDtlreq.getTtdesc();
		String isActive = insertDtlreq.getIsActive();
		int insert = 0;
		int update = 0;
		try {
			if(ttCode.isEmpty()) {
				insert = iTaskTypeDAO.insertTaskType(deptCode,tenantId,ttDesc, isActive);
			}else {
				update = iTaskTypeDAO.updateTaskType(ttCode,ttDesc,tenantId, isActive);
			}
			if(insert == 1) {
				 returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			     returnList.setResponseMessage(ResponseMessageMap.successInserted);
			}else if(update == 1) {
				 returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			     returnList.setResponseMessage(ResponseMessageMap.successUpdated);
			}else {
				returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnList.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		}catch(Exception e) {
			logger.error("insertUpdateTaskTypeService  method exception-->" + e);
		}
		
		return returnList;
	}

}
