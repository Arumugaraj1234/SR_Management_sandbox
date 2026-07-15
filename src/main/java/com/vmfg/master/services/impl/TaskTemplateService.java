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
import com.vmfg.master.dao.interfaces.ITaskTemplateDAO;
import com.vmfg.master.entity.TaskCategoryDrpdwnEntity;
import com.vmfg.master.entity.TemplateDtlMstEntity;
import com.vmfg.master.entity.TemplateTypeMstEntity;
import com.vmfg.master.request.TaskCategoryDrpdwnRequest;
import com.vmfg.master.request.TaskTemplateInsertUpdateRequest;
import com.vmfg.master.request.TaskTemplateTypeRequest;
import com.vmfg.master.request.TaskTemplatedtlRequest;
import com.vmfg.master.request.taskTemplateHdrRequest;
import com.vmfg.master.services.interfaces.ITaskTemplateService;

@Service
public class TaskTemplateService implements ITaskTemplateService {
	private static final Logger logger = LoggerFactory.getLogger(TaskTemplateService.class);

	@Autowired
	ITaskTemplateDAO iTaskTemplateDAO;

	@Override
	public ResponseAsList getTaskTypeTemplatedrpDwn(TaskTemplateTypeRequest req) {

		ResponseAsList returnList = new ResponseAsList();
		logger.info("getTaskTypeTemplatedrpDwn  method start");
		try {
			List<TemplateTypeMstEntity> typeMst = new ArrayList<TemplateTypeMstEntity>();
			String tenantId = req.getTenantId();
			String deptCode = req.getDeptCode();
			String ttCode = req.getTtCode();
			String tcCode = req.getTcCode();
			
				typeMst = iTaskTemplateDAO.getTaskTypeTemplatedrpDwn(deptCode, ttCode, tcCode, tenantId);
			if (typeMst.size() > 0) {
				returnList.setResponseData(typeMst);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(typeMst);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getTaskTypeTemplatedrpDwn  method exception-->" + ex);
		}
		logger.debug("getTaskTypeTemplatedrpDwn  method end");
		return returnList;
	}

	@Override
	public ResponseAsList getTaskTemplatedtl(TaskTemplatedtlRequest req) {

		ResponseAsList returnList = new ResponseAsList();
		logger.info("getTaskTemplatedtl  method start");
		try {
			List<TemplateDtlMstEntity> typeMst = new ArrayList<TemplateDtlMstEntity>();
			String tenantId = req.getTenantId();
			String ttHdrId = req.getTtHdrId();
			String isActive = req.getIsActive();
			
				typeMst = iTaskTemplateDAO.getTaskTemplatedtl(ttHdrId, tenantId, isActive);
			if (typeMst.size() > 0) {
				returnList.setResponseData(typeMst);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(typeMst);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getTaskTemplatedtl  method exception-->" + ex);
		}
		logger.debug("getTaskTemplatedtl  method end");
		return returnList;
	}

	@Override
	public ResponseAsList getTaskCategorydrpDwn(TaskCategoryDrpdwnRequest req) {

		ResponseAsList returnList = new ResponseAsList();
		logger.info("getTaskCategorydrpDwn  method start");
		try {
			List<TaskCategoryDrpdwnEntity> list = new ArrayList<TaskCategoryDrpdwnEntity>();
			String tenantId = req.getTenantId();
			String ttCode = req.getTtCode();
		
				list = iTaskTemplateDAO.getTaskCategorydrpDwn(ttCode, tenantId);
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
			logger.error("getTaskCategorydrpDwn  method exception-->" + ex);
		}
		logger.debug("getTaskCategorydrpDwn  method end");
		return returnList;
	}

	@Override
	public ResponseAsMessage insertUpdateTemplate(TaskTemplateInsertUpdateRequest request) {
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		String ttDtlId = request.getTtDtlId();
		String ttHdrId = request.getTtHdrId();
		String actName = request.getActName();
		String isActive = request.getIsActive();
		String empId = request.getEmpId();
		String tenantId =  request.getTenantId();
		int res = 0;
		try {
			if(ttDtlId.equalsIgnoreCase("")) {
				res = iTaskTemplateDAO.insertTaskTemplate(ttHdrId, actName, isActive, empId, tenantId );
			}else {
				res = iTaskTemplateDAO.updateTaskTemplate(ttDtlId, actName, ttHdrId, isActive, empId, tenantId);
			}
			
			if(res >= 1) {
				returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMsg.setResponseMessage(ResponseMessageMap.successUpdated);
			}else {
				returnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMsg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		}catch (Exception e) {
			logger.error("insertUpdateTemplate Service Method Error"+e);
		}
		
		return returnMsg;
	}

	@Override
	public ResponseAsMessage insertTemplateHdr(taskTemplateHdrRequest request) {
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		int res=0; int dtl =0;
		String tempName = request.getTemplateName();
		String empId = request.getEmpId();
		String deptCode = request.getDeptCode();
		String ttCode = request.getTtCode();
		String tcCode = request.getTcCode();
		String tenantId = request.getTenantId();
		String isActive = request.getIsActive();
		String actName = request.getActName();
		try {
			if(!tempName.isEmpty()) {
			res = iTaskTemplateDAO.insertTemplateHdr(tempName, empId, deptCode, ttCode, tcCode, tenantId, isActive);
			}
			if(res>=1) {
				String ttHdrId = String.valueOf(res); 
				 dtl = iTaskTemplateDAO.insertTaskTemplate(ttHdrId, actName, isActive, empId, tenantId );
			}
           if(dtl >= 1) {
        	   returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMsg.setResponseMessage(ResponseMessageMap.successUpdated);
			}else {
				returnMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMsg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}     
		}catch(Exception ex) {
			logger.error("insertTemplateHdr Service Method Error"+ex);
		}
		return returnMsg;
	}
	
}
