package com.vmfg.mis.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.mis.dao.interfaces.IManagementMisDAO;
import com.vmfg.mis.entity.OverAllProjSpentDrillDownEntity;
import com.vmfg.mis.entity.ProjConsumedValEntity;
import com.vmfg.mis.entity.ProjDetailsDrillDownEntity;
import com.vmfg.mis.entity.ProjSpentDrillDownEntity;
import com.vmfg.mis.entity.ProjectCntlEntity;
import com.vmfg.mis.entity.VendorDetailDrillDownEntity;
import com.vmfg.mis.request.ManagementProjRequest;
import com.vmfg.mis.services.interfaces.IManagementMisService;

@Service
public class ManagementMisService implements IManagementMisService {
	
	@Autowired
	IManagementMisDAO IManagementMisDAO;

	private static final Logger logger = LoggerFactory.getLogger(ManagementMisService.class);

	@Override
	public ResponseAsList getTotalProjCnt(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<ProjectCntlEntity> list = new ArrayList<ProjectCntlEntity>();
		
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		logger.debug("getTotalProjCnt method Start");
		try {
			list = IManagementMisDAO.getTotalProjCnt(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId);
			
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
			logger.error("getTotalProjCnt service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getProjConsumedValue(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<ProjConsumedValEntity> list = new ArrayList<ProjConsumedValEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		
		try {
			list = IManagementMisDAO.getProjConsumedValue(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId);
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
			logger.error("getProjConsumedValue service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getProjSpentDrillDown(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<ProjSpentDrillDownEntity> list = new ArrayList<ProjSpentDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		
		try {
			list = IManagementMisDAO.getProjSpentDrillDown(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId);
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
			logger.error("getProjSpentDrillDown service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getOverAllProjSpentDrillDown(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<OverAllProjSpentDrillDownEntity> list = new ArrayList<OverAllProjSpentDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		
		try {
			list = IManagementMisDAO.getOverAllProjSpentDrillDown(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId);
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
			logger.error("getOverAllProjSpentDrillDown service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getProjActualValDrillDown(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<OverAllProjSpentDrillDownEntity> list = new ArrayList<OverAllProjSpentDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		
		try {
			list = IManagementMisDAO.getProjActualValDrillDown(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId);
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
			logger.error("getProjActualValDrillDown service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getProjDetailsDrillDown(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<ProjDetailsDrillDownEntity> list = new ArrayList<ProjDetailsDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		
		try {
			list = IManagementMisDAO.getProjDetailsDrillDown(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId);
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
			logger.error("getProjDetailsDrillDown service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getVendorDetailDrillDown(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		String vendorId = manageProjCnt.getVendorCode();
		
		try {
			list = IManagementMisDAO.getVendorDetailDrillDown(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId,vendorId);
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
			logger.error("getVendorDetailDrillDown service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getVendorDetailHdrView(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		String vendorId = manageProjCnt.getVendorCode();
		
		try {
			list = IManagementMisDAO.getVendorDetailHdrView(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId,vendorId);
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
			logger.error("getVendorDetailHdrView service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getVendorPaymentDetails(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		String vendorId = manageProjCnt.getVendorCode();
		
		try {
			list = IManagementMisDAO.getVendorPaymentDetails(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId,vendorId);
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
			logger.error("getVendorPaymentDetails service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getProjSpentDetailByPmId(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<OverAllProjSpentDrillDownEntity> list = new ArrayList<OverAllProjSpentDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		
		try {
			list = IManagementMisDAO.getProjSpentDetailByPmId(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId);
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
			logger.error("getProjSpentDetailByPmId service  exception" + ex);
		}
		return returnList;
	}
}
