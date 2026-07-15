package com.vmfg.mis.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.mis.dao.interfaces.IAssemblyMisDAO;
import com.vmfg.mis.entity.DesignWidgetDtlEntity;
import com.vmfg.mis.entity.ProjectProgressEntity;
import com.vmfg.mis.entity.TaskCompTimeEntity;
import com.vmfg.mis.entity.getAssyDtlTaskReportEntity;
import com.vmfg.mis.entity.getAssyTaskReportEntity;
import com.vmfg.mis.entity.getPojCompDtlEntity;
import com.vmfg.mis.services.interfaces.IAssemblyMisService;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.project.request.DesignWidgetDtlReq;
import com.vmfg.project.request.ProjectInitiationMstRequest;

@Service
public class AssemblyMisService implements IAssemblyMisService {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyMisService.class);

	@Autowired
	IAssemblyMisDAO iAssemblyMisDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Override
	public ResponseAsList getAssyMisWidgetDtl(DesignWidgetDtlReq designWidgetDtl) {
		ResponseAsList returnList= new ResponseAsList();
//		String monthYr = designWidgetDtl.getMonYr();
		String empId = designWidgetDtl.getEmpId();
		String tenantId = designWidgetDtl.getTenantId();
		String deptCode = designWidgetDtl.getDeptCode();
		String pmId = designWidgetDtl.getPmId();
		String projId = designWidgetDtl.getProjId();
		if(projId.equalsIgnoreCase("getall")) {
			projId = "%%";
		}
		List<DesignWidgetDtlEntity> list = new ArrayList<DesignWidgetDtlEntity>();
		try {	
			
			list = iAssemblyMisDAO.assyWidgetDtl(tenantId,empId,deptCode,pmId,projId);
			int cnt = (iAssemblyMisDAO.assyWidgetDtlDAO(tenantId,empId,deptCode,pmId,projId));
			list.get(0).setProjCnt(String.valueOf(cnt));
			int taskCnt = iAssemblyMisDAO.assyWidgetTaskCnt(tenantId,empId,deptCode,pmId,projId);
			list.get(0).setAvgTasktime(String.valueOf(taskCnt));
				
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception e) {
			logger.error("getDesignWidgetDtl service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getTaskCompTimeResp(DesignWidgetDtlReq designWidgetDtl) {
		ResponseAsList returnList= new ResponseAsList();
//		ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
		String monthYr = designWidgetDtl.getMonYr();
		String empId = designWidgetDtl.getEmpId();
		String tenantId = designWidgetDtl.getTenantId();
		String deptCode = designWidgetDtl.getDeptCode();
		String pmId = designWidgetDtl.getPmId();
		String projId = designWidgetDtl.getProjId();
		if(projId.equalsIgnoreCase("getall")) {
			projId = "%%";
		}
		List<TaskCompTimeEntity> list = new ArrayList<TaskCompTimeEntity>();
		try {
//			projectInitiation.setEmpId(designWidgetDtl.getEmpId());
//			projectInitiation.setPmId(designWidgetDtl.getPmId());
//			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation);
//			if(mstPocCheck.equalsIgnoreCase("1")) {
				list = iAssemblyMisDAO.getTaskCompTimeDAO(monthYr,tenantId,empId,deptCode,pmId,projId);
//			}else {
//				list = iAssemblyMisDAO.getTaskCompTimeDAO1(MonthYr,tENANTID,EmpId,DeptCode,pmId,projectId);
//			}
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception e) {
			logger.error("getTaskCompTimeResp service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPojCompDtl(DesignWidgetDtlReq designWidgetDtl) {
		ResponseAsList returnList= new ResponseAsList();
//		ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
//		String monthYr = designWidgetDtl.getMonYr();
		String empId = designWidgetDtl.getEmpId();
		String tenantId = designWidgetDtl.getTenantId();
		String deptCode = designWidgetDtl.getDeptCode();
		String pmId = designWidgetDtl.getPmId();
		String projId = designWidgetDtl.getProjId();
		if(projId.equalsIgnoreCase("getall")) {
			projId = "%%";
		}
		List<getPojCompDtlEntity> list = new ArrayList<getPojCompDtlEntity>();
		try {
//			projectInitiation.setEmpId(designWidgetDtl.getEmpId());
//			projectInitiation.setPmId(designWidgetDtl.getPmId());
//			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation);
//			if(mstPocCheck.equalsIgnoreCase("1")) {
				list = iAssemblyMisDAO.getPojCompDtlDAO(tenantId,empId,deptCode,pmId,projId);
//			}else {
//				list = iAssemblyMisDAO.getPojCompDtlDAO1(MonthYr,tENANTID,EmpId,DeptCode,pmId,projectId);
//			}
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception e) {
			logger.error("getPojCompDtl service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getAssyTaskReport(DesignWidgetDtlReq designWidgetDtl) {
		ResponseAsList returnList= new ResponseAsList();
//		ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
		String monthYr = designWidgetDtl.getMonYr();
		String empId = designWidgetDtl.getEmpId();
		String tenantId = designWidgetDtl.getTenantId();
		String deptCode = designWidgetDtl.getDeptCode();
		String pmId = designWidgetDtl.getPmId();
		String projId = designWidgetDtl.getProjId();
		String lifespan = designWidgetDtl.getLifeSpan();
		if(projId.equalsIgnoreCase("getall")) {
			projId = "%%";
		}
		List<getAssyTaskReportEntity> list = new ArrayList<getAssyTaskReportEntity>();
		try {
//			projectInitiation.setEmpId(designWidgetDtl.getEmpId());
//			projectInitiation.setPmId(designWidgetDtl.getPmId());
//			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation);
//			if(mstPocCheck.equalsIgnoreCase("1")) {
				list = iAssemblyMisDAO.getAssyTaskReportDAO(monthYr,tenantId,empId,deptCode,pmId,projId,lifespan);
//			}else {
//				list = iAssemblyMisDAO.getAssyTaskReportDAO1(MonthYr,tENANTID,EmpId,DeptCode,pmId,projectId);
//			}
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception e) {
			logger.error("getAssyTaskReport service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getAssyDtlTaskReportResp(DesignWidgetDtlReq designWidgetDtl) {
		ResponseAsList returnList= new ResponseAsList();
		ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
		String MonthYr = designWidgetDtl.getMonYr();
		String EmpId = designWidgetDtl.getEmpId();
		String tENANTID = designWidgetDtl.getTenantId();
		String DeptCode = designWidgetDtl.getDeptCode();
		String pmId = designWidgetDtl.getPmId();
		String projId = designWidgetDtl.getProjId();
		String lifeSpan = designWidgetDtl.getLifeSpan();
		String projectId = "";
		if(projId.equalsIgnoreCase("getall")) {
			projectId = "%%";
		}else {
			projectId = projId;
		}
		List<getAssyDtlTaskReportEntity> list = new ArrayList<getAssyDtlTaskReportEntity>();
		try {
			projectInitiation.setEmpId(designWidgetDtl.getEmpId());
			projectInitiation.setPmId(designWidgetDtl.getPmId());
			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,tENANTID);
			if(mstPocCheck.equalsIgnoreCase("1")) {
				list = iAssemblyMisDAO.getAssyDtlTaskReportDAO(MonthYr,tENANTID,EmpId,DeptCode,pmId,projectId,lifeSpan);
			}else {
				list = iAssemblyMisDAO.getAssyDtlTaskReportDAO1(MonthYr,tENANTID,EmpId,DeptCode,pmId,projectId,lifeSpan);
			}
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception e) {
			logger.error("getAssyDtlTaskReportResp service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getProjectProgressDtls(DesignWidgetDtlReq designWidgetDtl) {
		// TODO Auto-generated method stub
		ResponseAsList returnList= new ResponseAsList();
//		ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
//		String MonthYr = designWidgetDtl.getMonYr();
		String EmpId = designWidgetDtl.getEmpId();
		String tENANTID = designWidgetDtl.getTenantId();
		String DeptCode = designWidgetDtl.getDeptCode();
		String pmId = designWidgetDtl.getPmId();
		String projId = designWidgetDtl.getProjId();
		String projectId = "";
		if(projId.equalsIgnoreCase("getall")) {
			projectId = "%%";
		}else {
			projectId = projId;
		}
		List<ProjectProgressEntity> list = new ArrayList<ProjectProgressEntity>();

		try {
			
			list=iAssemblyMisDAO.getprojectInProgress(tENANTID,EmpId,DeptCode,pmId,projectId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
		}catch(Exception ex) {
			logger.error("getProjectProgressDtls service error " + ex);

		}
		return returnList;
	}
}
