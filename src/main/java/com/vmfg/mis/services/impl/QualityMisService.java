package com.vmfg.mis.services.impl;

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
import com.vmfg.mis.dao.interfaces.IQualityMisDAO;
import com.vmfg.mis.entity.DrilldownDtlEntity;
import com.vmfg.mis.entity.QualityWidgetDtlEntity;
import com.vmfg.mis.entity.SupplierRatingEntity;
import com.vmfg.mis.entity.TeamMemberLoadEntity;
import com.vmfg.mis.entity.VendorTypeCateCountEntity;
import com.vmfg.mis.entity.VendorTypeCateCountListEntity;
import com.vmfg.mis.request.DrilldownDtlReq;
import com.vmfg.mis.request.QualityWidgetDtlReq;
import com.vmfg.mis.request.QulyProjCntRequest;
import com.vmfg.mis.request.TeamMemberLoadReq;
import com.vmfg.mis.services.interfaces.IQualityMisService;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.quality.dao.interfaces.IQualityDAO;

@Service
public class QualityMisService implements IQualityMisService{
	private static final Logger logger = LoggerFactory.getLogger(QualityMisService.class);
	
	@Autowired
	IQualityMisDAO iQualityMisDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	IQualityDAO iQualityDAO;
	
	public ResponseAsMessage getQualityProjCnt(QulyProjCntRequest qlyProjCnt) {
		ResponseAsMessage Cnt = new ResponseAsMessage() ;
		logger.debug("getQualityProjCnt method Start");
		String tenantId = qlyProjCnt.getTenantId();
		String fromDate = qlyProjCnt.getFromDate();
		String toDate = qlyProjCnt.getToDate();
		String empID = qlyProjCnt.getEmpId();
		String pmId = qlyProjCnt.getPmId();
		try {
			 Cnt=iQualityMisDAO.getQualityProjCnt(tenantId, fromDate, toDate, empID, pmId);

		}catch(Exception ex) {
			logger.error("getQualityProjCnt service  exception" + ex);
		}
		return Cnt;
	}

	
	@Override
	public ResponseAsList QualityWidgetDtlResp(QualityWidgetDtlReq widgetDtl) {
		ResponseAsList returnList= new ResponseAsList();
		String projId = widgetDtl.getProjId();
		String tenantId = widgetDtl.getTenantID();
		String empID = widgetDtl.getEmpId();
		String pmID = widgetDtl.getPmId();
		String fromDate = widgetDtl.getFromDate();
		String toDate = widgetDtl.getToDate();
		if(projId.equalsIgnoreCase("getall")) {
			projId = "%%";
		}
		List<QualityWidgetDtlEntity> list = new ArrayList<QualityWidgetDtlEntity>();
		try {

			list = iQualityMisDAO.QualityWidgetDtlResp(projId,tenantId,empID,pmID,fromDate,toDate);

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
			logger.error("QualityWidgetDtlResp service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getDrilldownDtlResp(DrilldownDtlReq drillDownDtl) {
		ResponseAsList returnList= new ResponseAsList();
		String projId = drillDownDtl.getProjId();
		String tenantId = drillDownDtl.getTenantId();
		String typeCode = drillDownDtl.getTypeCode();
		String empID = drillDownDtl.getEmpId();
		String pmID = drillDownDtl.getPmId();
		String fromDate = drillDownDtl.getFromDate();
		String toDate = drillDownDtl.getToDate();
		if(projId.equalsIgnoreCase("getall")) {
			projId = "%%";
		}
		List<DrilldownDtlEntity> list = new ArrayList<DrilldownDtlEntity>();
		try {
			list = iQualityMisDAO.getDrilldownDtlResp(projId,tenantId,typeCode,empID,pmID,fromDate,toDate);
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
			logger.error("getDrilldownDtlResp service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList SupplierRatingResp(QualityWidgetDtlReq widgetDtl) {
		ResponseAsList returnList= new ResponseAsList();
		String projId = widgetDtl.getProjId();
		String tENANTID = widgetDtl.getTenantID();
		String empID = widgetDtl.getEmpId();
		String pmID = widgetDtl.getPmId();
		String fromDate = widgetDtl.getFromDate();
		String toDate = widgetDtl.getToDate();
		String projectId = "";
		if(projId.equalsIgnoreCase("getall")) {
			projectId = "%%";
		}else {
			projectId = projId;
		}
		List<SupplierRatingEntity> list = new ArrayList<SupplierRatingEntity>();
		try {
			list = iQualityMisDAO.SupplierRatingDAO(projectId,tENANTID,empID,pmID,fromDate,toDate);
			if (list.size() > 0) {
//				for(int i =0;i<list.size();i++) {
//				String inwardrate = iQualityMisDAO.SupplierSCMAndInwardRatingDAO(projectId,tENANTID,empID,pmID,fromDate,toDate, list.get(i).getVenCode(), "INWARD_RATING");
//				String scmrate = iQualityMisDAO.SupplierSCMAndInwardRatingDAO(projectId,tENANTID,empID,pmID,fromDate,toDate, list.get(i).getVenCode(), "RELATIONSHIP_RATING");
//				list.get(i).setInwardRate(inwardrate);
//				list.get(i).setRelationshipRate(scmrate);
//				}
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception e) {
			logger.error("SupplierRatingResp service error " + e);
		}
		return returnList;
	}


	@Override
	public ResponseAsList TeamMemberLoadResp(TeamMemberLoadReq teamLoad) {
		ResponseAsList returnList= new ResponseAsList();
		String projId = teamLoad.getProjId();
		String empId = teamLoad.getEmpId();
		String tenantId = teamLoad.getTenantID();
		String pmID = teamLoad.getPmId();
		String fromDate = teamLoad.getFromDate();
		String toDate = teamLoad.getToDate();
		String teamMemberId = teamLoad.getTeamMemEmpId();
		if(projId.equalsIgnoreCase("getall")) {
			projId = "%%";
		}
		if(empId.equalsIgnoreCase("getall")) {
			empId = "%%";
		}
		if(teamMemberId.equalsIgnoreCase("getall")) {
			teamMemberId = "%%";
		}
		List<TeamMemberLoadEntity> list = new ArrayList<TeamMemberLoadEntity>();
		try {
			list = iQualityMisDAO.TeamMemberLoadDAO(projId,empId,tenantId,pmID,fromDate,toDate);
			for(int i=0;i<list.size();i++) {
				String[] monthYr = list.get(i).getInspOn().split("-");
				String qty = iQualityMisDAO.TeamMemberLoadQty(projId,empId,tenantId,pmID,monthYr,teamMemberId,fromDate,toDate);
				String inspectionQty = qty.isEmpty() ? "0" : qty;  
				list.get(i).setInspQty(inspectionQty);
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
		}catch(Exception ex) {
			logger.error("TeamMemberLoadResp service error " + ex);
		}
		return returnList;
	}


	@Override
	public ResponseAsList getVendorByCatAndType(TenantRequest tenantId) {
		ResponseAsList returnList= new ResponseAsList();
		List<VendorTypeCateCountListEntity> response = new ArrayList<VendorTypeCateCountListEntity>();
		VendorTypeCateCountListEntity obj = new VendorTypeCateCountListEntity();
		
		List<VendorTypeCateCountEntity> list1 = new ArrayList<VendorTypeCateCountEntity>();
		List<VendorTypeCateCountEntity> list2 = new ArrayList<VendorTypeCateCountEntity>();
		try {
			list1 = iQualityMisDAO.getVendorByType(tenantId);
			list2 = iQualityMisDAO.getVendorByCategory(tenantId);
			obj.setVendorCategory(list2);
			obj.setVendorType(list1);
			response.add(obj);
			
			if (response.size() > 0) {
				returnList.setResponseData(response);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(response);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception ex){
			logger.error("getVendorByCatAndType service error " + ex);
		}
		return returnList;
	}

}
