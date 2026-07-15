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
import com.vmfg.master.dao.interfaces.ITaskCategoryDAO;
import com.vmfg.master.entity.TaskCategoryEntity;
import com.vmfg.master.entity.TaskTypeDropDownEntity;
import com.vmfg.master.request.TaskCategoryInsertUpdateRequest;
import com.vmfg.master.request.TaskCategoryRequest;
import com.vmfg.master.request.TenantIdRequest;
import com.vmfg.master.services.interfaces.ITaskCategoryService;

@Service
public class TaskCategoryService implements ITaskCategoryService {
	private static final Logger logger = LoggerFactory.getLogger(TaskCategoryService.class);
	
	@Autowired
	private ITaskCategoryDAO  iTaskCategoryDAO;

	@Override
	public ResponseAsList getTaskCategory(TaskCategoryRequest taskCategoryrequest) {
		ResponseAsList list = new ResponseAsList();
		
		try {
			List<TaskCategoryEntity> taskCatList = iTaskCategoryDAO.getTaskCategory(taskCategoryrequest.getTtCode(),taskCategoryrequest.getTenantId());
			if(taskCatList.size()>0) {
				list.setResponseData(taskCatList);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
			}else {
				list.setResponseData(taskCatList);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
		}catch (Exception e) {
			logger.error("getTaskCategory Service error"+e);
		}
		return list;
	}

	@Override
	public ResponseAsMessage insertandUpdateTaskCat(TaskCategoryInsertUpdateRequest taskCategoryInsertUpdateRequest) {
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		String tcCode = taskCategoryInsertUpdateRequest.getTcCode();
		int res = 0;
		try {
			if(tcCode.equalsIgnoreCase("")) {
				res = iTaskCategoryDAO.insertTaskCategory(taskCategoryInsertUpdateRequest);
			}else {
				res = iTaskCategoryDAO.updateTaskCategory(taskCategoryInsertUpdateRequest);
			}
			
			
			if(res==1) {
				returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMsg.setResponseMessage(ResponseMessageMap.successUpdated);
			}else {
				returnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMsg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		}catch (Exception e) {
			logger.error("insertandUpdateTaskCat Service Method Error"+e);
		}
		
		return returnMsg;
	}

	@Override
	public ResponseAsList getTaskTypeDropDownIsActive(TenantIdRequest taskCategoryrequest) {
		ResponseAsList list = new ResponseAsList();
		List<TaskTypeDropDownEntity> ddlist = new ArrayList<>();
		try {
			ddlist = iTaskCategoryDAO.getTaskTypeDropDownIsActive(taskCategoryrequest);
			
			if(ddlist.size()>0) {
				list.setResponseData(ddlist);
				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
			}else {
				list.setResponseData(ddlist);
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch (Exception e) {
			logger.error("getTaskTypeDropDownIsActive service method error"+e);
		}
		return list;
	}
	
	

}
