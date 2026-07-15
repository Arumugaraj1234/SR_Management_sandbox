package com.vmfg.mis.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.inventory.entity.InvProdEntity;
import com.vmfg.mis.dao.interfaces.IScmMisDAO;
import com.vmfg.mis.entity.GetIndentToPOEntity;
import com.vmfg.mis.entity.GetSCMWidgetDtlEntity;
import com.vmfg.mis.entity.ScmEmployeeIndentDtlsEntity;
import com.vmfg.mis.entity.VendorDetailDrillDownEntity;
import com.vmfg.mis.request.ManagementProjRequest;
import com.vmfg.mis.request.ScmMisRequest;
import com.vmfg.mis.services.interfaces.IScmMisService;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.project.request.ProjectInitiationMstRequest;

@Service
public class ScmMisService implements IScmMisService {
	private static final Logger logger = LoggerFactory.getLogger(ScmMisService.class);
	
	@Autowired
	IScmMisDAO iScmMisDAO;

	@Autowired
	ProjectDAO projectDAO;
	
	@Override
	public ResponseAsList getSCMWidgetDtl(ScmMisRequest scmMisReq) {
		GetSCMWidgetDtlEntity obj =new GetSCMWidgetDtlEntity();
		List<GetSCMWidgetDtlEntity> list = new ArrayList<GetSCMWidgetDtlEntity>();
		ResponseAsList returnList = new ResponseAsList();
//		ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
		logger.debug("getPlannedActivity method Start");
		try {
//			projectInitiation.setEmpId(scmMisReq.getEmpId());
//			projectInitiation.setPmId(scmMisReq.getPmId());
//			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation);
//			String assignedTo ="";
//			if(mstPocCheck.equalsIgnoreCase("1")) {
//				assignedTo = "getall";
//			}else {
//				assignedTo = scmMisReq.getEmpId();
//			}
			String month=scmMisReq.getMonthYear().split("-")[0];
			String year=scmMisReq.getMonthYear().split("-")[1];
			String indentHdrCnt=iScmMisDAO.getIndentHdrCount(scmMisReq.getPmHdrId(),scmMisReq.getTenantId(),scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan(),scmMisReq.getPmId());
			String indentDtlCnt=iScmMisDAO.getIndentDtlCount(scmMisReq.getPmHdrId(),scmMisReq.getTenantId(),scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan(),scmMisReq.getPmId());
			String totalPoApproved=iScmMisDAO.noOfPoApproved(scmMisReq.getPmHdrId(),scmMisReq.getTenantId(),scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan());
			String pendingIndents=iScmMisDAO.getPendingIndentsCnt(scmMisReq.getPmHdrId(),scmMisReq.getTenantId(),scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan(),scmMisReq.getPmId());
			String itemsDelayed=iScmMisDAO.getItemsDelayedCnt(scmMisReq.getPmHdrId(),scmMisReq.getTenantId(),scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan(),scmMisReq.getPmId());
			String inventoryStock=iScmMisDAO.getInventoryStockCnt(scmMisReq.getPmHdrId(),scmMisReq.getTenantId(),scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan(),scmMisReq.getPmId());
			
			obj.setIndentHdrCnt(indentHdrCnt);
			obj.setIndentDtlCnt(indentDtlCnt);
			obj.setNoOfPo(totalPoApproved);
			obj.setPendingIndents(pendingIndents);
			obj.setItemsDelayed(itemsDelayed);
			obj.setInventoryStock(inventoryStock);
			list.add(obj);
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
			logger.error("getSCMWidgetDtl method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getIndentToPO(ScmMisRequest scmMisReq) {
		List<GetIndentToPOEntity> list = new ArrayList<GetIndentToPOEntity>();
		ResponseAsList returnList = new ResponseAsList();
	//	ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
		logger.debug("getPlannedActivity method Start");
		try {
			GetIndentToPOEntity resp = new GetIndentToPOEntity();
			String pmHdrId="";
			String month=scmMisReq.getMonthYear().split("-")[0];
			String year=scmMisReq.getMonthYear().split("-")[1];
			if(scmMisReq.getPmHdrId().equalsIgnoreCase("getAll")) {
				pmHdrId = "%%";
			}else {
				pmHdrId = scmMisReq.getPmHdrId();
			}
			String indentcount = iScmMisDAO.indentCount(pmHdrId, scmMisReq.getPmId(), scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan(),scmMisReq.getTenantId());
			String indentCompleted = iScmMisDAO.indentCompleted(pmHdrId, scmMisReq.getPmId(), scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan(),scmMisReq.getTenantId());
			String avgdays = iScmMisDAO.indentAvgTime(pmHdrId, scmMisReq.getPmId(), scmMisReq.getEmpId(),month,year,scmMisReq.getLifeSpan(),scmMisReq.getTenantId());
			resp.setAvgDayToClose(avgdays);
			resp.setIndentclosed(indentCompleted);
			resp.setIndentCount(indentcount);
			list.add(resp);
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
			logger.error("getIndentToPO method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage getcostnegotiated(ScmMisRequest scmMisReq) {// minth year
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			String cost="";
			String month=scmMisReq.getMonthYear().split("-")[0];
			String year=scmMisReq.getMonthYear().split("-")[1];
			cost=iScmMisDAO.getcostnegotiated(scmMisReq.getPmHdrId(),scmMisReq.getTenantId(),month,year,scmMisReq.getLifeSpan());
	
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.success);
			rm.setResponseDataMessage(cost);

		}catch(Exception ex) {
			logger.error("getcostnegotiated method  exception" + ex);
		}
		return rm;
	}

	@Override
	public ResponseAsMessage getInventoryValue(ScmMisRequest scmMisReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		List<InvProdEntity> list = new ArrayList<>();
		try {
			BigDecimal invVal=BigDecimal.ZERO;
			list=iScmMisDAO.getQtyInHand(scmMisReq.getPmHdrId(),scmMisReq.getTenantId());
			for(int i=0;i<list.size();i++) {
//				BigDecimal calculatedValue=new BigDecimal(list.get(i).getQtyOnHand()).multiply(new BigDecimal(list.get(i).getUnitRate()));
//				invVal=invVal.add(calculatedValue);
				if(list.get(i).getInvValue() != null) {
				   invVal=invVal.add(new BigDecimal(list.get(i).getInvValue()));
				}
			}
			String val = String.valueOf(invVal);
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.success);
			rm.setResponseDataMessage(val);

		}catch(Exception ex) {
			logger.error("getInventoryValue method  exception" + ex);
		}
		return rm;
	}

	@Override
	public ResponseAsMessage getInventoryAgeing(ScmMisRequest scmMisReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			String avgDays="";
			avgDays=iScmMisDAO.getInventoryAgeing(scmMisReq.getPmHdrId(),scmMisReq.getTenantId());
	
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.success);
			rm.setResponseDataMessage(avgDays);

		}catch(Exception ex) {
			logger.error("getInventoryAgeing method  exception" + ex);
		}
		return rm;
	}

	@Override
	public ResponseAsList getScmEmployeeIndentDtls(ScmMisRequest scmMisReq) {
		ResponseAsList returnList = new ResponseAsList();	
		List<ScmEmployeeIndentDtlsEntity> list= new ArrayList<ScmEmployeeIndentDtlsEntity>();
		ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
		try {
			String month=scmMisReq.getMonthYear().split("-")[0];
			String year=scmMisReq.getMonthYear().split("-")[1];
			projectInitiation.setEmpId(scmMisReq.getEmpId());
			projectInitiation.setPmId(scmMisReq.getPmId());
			String tenantId = scmMisReq.getTenantId();
			String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,tenantId);
			String assignedTo ="";
			if(mstPocCheck.equalsIgnoreCase("1")) {
				assignedTo = "getall";
			}else {
				assignedTo = scmMisReq.getEmpId();
			}
			list=iScmMisDAO.getScmEmployeeIndentDtls(assignedTo,scmMisReq.getPmHdrId(),scmMisReq.getTenantId(),month,year,scmMisReq.getLifeSpan());
			for(int i=0;i<list.size();i++) {
				list.get(i).setTotalIndentsAssigned(iScmMisDAO.getTotalAssignedIndents(list.get(i).getPmHdrId(), list.get(i).getEmployeeId(),tenantId));
				list.get(i).setCompletedIndents(iScmMisDAO.getCompletedIndentCount(list.get(i).getPmHdrId(), list.get(i).getEmployeeId(),tenantId));
			}
			returnList.setResponseData(list);
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.success);
		}catch(Exception ex) {
			logger.error("getScmEmployeeIndentDtls method  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getVendorPaymentCount(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		String vendorId = manageProjCnt.getVendorCode();
		String pmId = manageProjCnt.getPmId();
		String empId = manageProjCnt.getEmpId();
		try {
			list = iScmMisDAO.getVendorPaymentCount(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId,vendorId,pmId, empId);
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
			logger.error("getVendorPaymentCount service  exception" + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getVendorDetailView(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		String vendorId = manageProjCnt.getVendorCode();
		String empId = manageProjCnt.getEmpId();
		String pmId = manageProjCnt.getPmId();
		
		try {
			list = iScmMisDAO.getVendorDetailView(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId,vendorId, empId, pmId);
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
	public ResponseAsList getVendorDtlDrillDown(ManagementProjRequest manageProjCnt) {
		ResponseAsList returnList= new ResponseAsList();
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String tenantId = manageProjCnt.getTenantId();
		String fromDate = manageProjCnt.getFromDate();
		String toDate = manageProjCnt.getToDate();
		String pmHdrId =  manageProjCnt.getPmHdrId();
		String custCode = manageProjCnt.getCustomerId();
		String stageCode = manageProjCnt.getStageCode();
		String vendorId = manageProjCnt.getVendorCode();
		String empId = manageProjCnt.getEmpId();
		String pmId = manageProjCnt.getPmId();
		
		try {
			list = iScmMisDAO.getVendorDetailDrillDown(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId,vendorId, empId, pmId);
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
}
