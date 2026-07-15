package com.vmfg.master.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.master.dao.interfaces.IProjectInitiationMasterDAO;
import com.vmfg.master.entity.ProjectInitiationDtlEntity;
import com.vmfg.master.request.ProjectInitiationUpdateRequest;
import com.vmfg.master.services.interfaces.IProjectInitiationMasterService;

@Service
public class ProjectInitiationMasterService implements IProjectInitiationMasterService{
	private static final Logger logger = LoggerFactory.getLogger(ProjectInitiationMasterService.class);
	
	@Autowired
	private IProjectInitiationMasterDAO iProjectInitiationMasterDAO;

	@Override
	public ResponseAsList getProjectInitiationDtl(TenantRequest tenantRequestReq) {
		ResponseAsList list = new ResponseAsList();
		List<ProjectInitiationDtlEntity> response = new ArrayList<ProjectInitiationDtlEntity>();
		try {
			response = iProjectInitiationMasterDAO.getProjectInitiationDtl(tenantRequestReq.getTenantID());
		} catch (Exception ex) {
			logger.error("ProjectInitiationMasterService Method Exception " + ex);
		}

		if (response.size() > 0) {
			list.setResponseCode(ResponseMessageMap.responseCodeOk);
			list.setResponseMessage(ResponseMessageMap.success);

		} else {
			list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			list.setResponseMessage(ResponseMessageMap.noRecord);
		}
		list.setResponseData(response);
		return list;
	}

	@Override
	public ResponseAsMessage updateProjectIntiationMaster(List<ProjectInitiationUpdateRequest> projectInitiationUpdateReq) {
		ResponseAsMessage msg = new ResponseAsMessage();
		int updateQty=0;
		for(int i=0;i<projectInitiationUpdateReq.size();i++) {
			try {
				updateQty=iProjectInitiationMasterDAO.updateProjectIntiationMasterMethod(projectInitiationUpdateReq.get(i).getPiId(),projectInitiationUpdateReq.get(i).getPrimaryPoc(),projectInitiationUpdateReq.get(i).getMasterPoc(),projectInitiationUpdateReq.get(i).getDepartmentAssigned());
			}catch(Exception ex) {
				logger.error("updateProjectIntiationMaster Method Exception " + ex);
			}
		}
		
		
		
		if(updateQty>0) {
			msg.setResponseCode(ResponseMessageMap.responseCodeOk);
			msg.setResponseMessage(ResponseMessageMap.successCreated);
		}else {
			msg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			msg.setResponseMessage(ResponseMessageMap.failToCreateMsg);
		}
		
		return msg;
	}
	
}
