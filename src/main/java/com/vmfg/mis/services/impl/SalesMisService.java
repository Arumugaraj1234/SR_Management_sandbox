package com.vmfg.mis.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.mis.dao.impl.SalesMisDAO;
import com.vmfg.mis.dao.interfaces.ISalesMisDAO;
import com.vmfg.mis.entity.EmployeeEntity;
import com.vmfg.mis.entity.GetCustomerOrderDtlEntity;
import com.vmfg.mis.entity.GetSalesContDtlEntity;
import com.vmfg.mis.entity.GetSalesConvRatioEntity;
import com.vmfg.mis.entity.GetSalesStageDtlEntity;
import com.vmfg.mis.entity.SalesOrderDetailsList;
import com.vmfg.mis.request.MisFromDateToDateRequest;
import com.vmfg.mis.response.GetSalesOrderDetailsResponse;
import com.vmfg.mis.services.interfaces.ISalesMisService;
import com.vmfg.scm.entity.ProjectHdrDtlEntity;

@Service
public class SalesMisService  implements ISalesMisService {
	private static final Logger logger = LoggerFactory.getLogger(SalesMisService.class);
	
	@Autowired
	ISalesMisDAO iSalesMisDAO;
	
	@Autowired
	private IndentUploadDAO indentUploadDao;
	
	@Override
	public ResponseAsList getSalesOrderDetails(MisFromDateToDateRequest misFromDateToDateReq) {
		logger.debug("getSalesOrderDetails   method Start");
		ResponseAsList list = new ResponseAsList();
		List<GetSalesOrderDetailsResponse>respList = new ArrayList<GetSalesOrderDetailsResponse>();
		GetSalesOrderDetailsResponse resp  =new GetSalesOrderDetailsResponse();
		try {
			List<SalesOrderDetailsList> leadlist = iSalesMisDAO.leadList(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
		//	List<SalesOrderDetailsList> wonlist = iSalesMisDAO.wonList(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
			List<SalesOrderDetailsList> lostlist = iSalesMisDAO.lostList(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
			List<SalesOrderDetailsList> holdlist = iSalesMisDAO.holdList(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
			
			for(int i =0;i<leadlist.size();i++) {
				String startDate = "";
				String endDate ="";
				if(i==0) {
					startDate = misFromDateToDateReq.getFromDate();
				}else {
					startDate =leadlist.get(i).getMonthYr() +"-01";
				}
				if(leadlist.size() -1 == i) {
					endDate = misFromDateToDateReq.getToDate();
				}else {
					endDate = leadlist.get(i).getMonthYr() +"-31";
				}
				List<SalesOrderDetailsList> enqlist = iSalesMisDAO.enqList(startDate, endDate, misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
				if(enqlist.size()>0) {
					leadlist.get(i).setEnqCount(enqlist.get(0).getSeCount());
					leadlist.get(i).setEnqValue(enqlist.get(0).getVal());
				}
				
			}
		
			
		List<SalesOrderDetailsList> saleEnqDtlList = iSalesMisDAO.saleMISEnqDtlList(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(), misFromDateToDateReq.getEmpId());	

		Map<String, List<SalesOrderDetailsList>> groupByPriceMap = 
				saleEnqDtlList.stream().collect(Collectors.groupingBy(SalesOrderDetailsList::getMonthYr,  TreeMap::new, Collectors.toList()));
    	List<SalesOrderDetailsList> wonlist = new ArrayList<SalesOrderDetailsList>();
		for (String name: groupByPriceMap.keySet()) {
			SalesOrderDetailsList eachList = new SalesOrderDetailsList();
		    String key = name.toString();
		    List<SalesOrderDetailsList> value = groupByPriceMap.get(name);
		    eachList.setVal(value.stream().map(x -> new BigDecimal(x.getVal()))
			.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
		    eachList.setMonthYr(key);
		    eachList.setSeCount(Integer.toString(value.size()) );
		    wonlist.add(eachList);
		}
			
			resp.setHoldList(holdlist);
			resp.setLeadList(leadlist);
			resp.setLostList(lostlist);
			resp.setWonList(wonlist);
			respList.add(resp);
			list.setResponseCode(ResponseMessageMap.responseCodeOk);
			list.setResponseData(respList);
			list.setResponseMessage(ResponseMessageMap.success);
		}catch(Exception ex) {
			logger.error("getSalesOrderDetails  method  exception" + ex);
		}
		return list;
	
	}

	@Override
	public ResponseAsList getSalesStageDtl(MisFromDateToDateRequest misFromDateToDateReq) {
		logger.debug("getSalesStageDtl   method Start");
		ResponseAsList list = new ResponseAsList();
		List<GetSalesStageDtlEntity>respList = new ArrayList<GetSalesStageDtlEntity>();
			try {
				respList = iSalesMisDAO.getSalesStageDtl(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
			list.setResponseCode(ResponseMessageMap.responseCodeOk);
			list.setResponseData(respList);
			list.setResponseMessage(ResponseMessageMap.success);
		}catch(Exception ex) {
			logger.error("getSalesStageDtl  method  exception" + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getCustomerOrderDtl(MisFromDateToDateRequest misFromDateToDateReq) {
		logger.debug("getCustomerOrderDtl   method Start");
	ResponseAsList list = new ResponseAsList();
	List<GetCustomerOrderDtlEntity>respList = new ArrayList<GetCustomerOrderDtlEntity>();
		try {
			respList = iSalesMisDAO.getCustomerOrderDtl(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
		list.setResponseCode(ResponseMessageMap.responseCodeOk);
		list.setResponseData(respList);
		list.setResponseMessage(ResponseMessageMap.success);
	}catch(Exception ex) {
		logger.error("getCustomerOrderDtl  method  exception" + ex);
	}
	return list;
	}

	@Override
	public ResponseAsList getSalesConvRatio(MisFromDateToDateRequest misFromDateToDateReq) {
		logger.debug("getSalesConvRatio   method Start");
		ResponseAsList list = new ResponseAsList();
		List<GetSalesConvRatioEntity>respList = new ArrayList<GetSalesConvRatioEntity>();
			try {
				respList = iSalesMisDAO.getSalesConvRatio(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
			list.setResponseCode(ResponseMessageMap.responseCodeOk);
			list.setResponseData(respList);
			list.setResponseMessage(ResponseMessageMap.success);
		}catch(Exception ex) {
			logger.error("getSalesConvRatio  method  exception" + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getSalesContDtl(MisFromDateToDateRequest misFromDateToDateReq) {
		logger.debug("getSalesContDtl   method Start");
		ResponseAsList list = new ResponseAsList();
		List<GetSalesContDtlEntity>respList = new ArrayList<GetSalesContDtlEntity>();
			try {
				respList = iSalesMisDAO.getSalesContDtl(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getProjectId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
			list.setResponseCode(ResponseMessageMap.responseCodeOk);
			list.setResponseData(respList);
			list.setResponseMessage(ResponseMessageMap.success);
		}catch(Exception ex) {
			logger.error("getSalesContDtl  method  exception" + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getSalesContProjects(MisFromDateToDateRequest misFromDateToDateReq) {
		logger.debug("getSalesContProjects   method Start");
		ResponseAsList list = new ResponseAsList();
		List<ProjectHdrDtlEntity>respList = new ArrayList<ProjectHdrDtlEntity>();
			try {
				respList = iSalesMisDAO.getSalesContProjects(misFromDateToDateReq.getFromDate(), misFromDateToDateReq.getToDate(), misFromDateToDateReq.getTenantId(),misFromDateToDateReq.getProjectId(),misFromDateToDateReq.getPmId(),misFromDateToDateReq.getEmpId());
			list.setResponseCode(ResponseMessageMap.responseCodeOk);
			list.setResponseData(respList);
			list.setResponseMessage(ResponseMessageMap.success);
		}catch(Exception ex) {
			logger.error("getSalesContProjects  method  exception" + ex);
		}
		return list;
	}
	@Override
	public ResponseAsList getSalesDeptEmployees(MisFromDateToDateRequest misFromDateToDateReq) {
	    logger.debug("getSalesDeptEmployees method Start");

	    ResponseAsList list = new ResponseAsList();
	    List<EmployeeEntity> employeeList = new ArrayList<>();

	    try {
	    	String isHod = indentUploadDao.getProjectInitiationMstResp(
	                misFromDateToDateReq.getPmId(), 
	                misFromDateToDateReq.getEmpId(), 
	                misFromDateToDateReq.getTenantId()
	            );

	            logger.debug("HOD Status Retrieved: " + isHod);

	    	employeeList = iSalesMisDAO.getSalesDeptEmployees(misFromDateToDateReq.getTenantId(), misFromDateToDateReq.getDepCode(),misFromDateToDateReq.getEmpId(),isHod);
	        list.setResponseCode(ResponseMessageMap.responseCodeOk);
	        list.setResponseData(employeeList);
	        list.setResponseMessage(ResponseMessageMap.success);
	    } catch (Exception ex) {
	        logger.error("getSalesDeptEmployees method exception " + ex);
	    }
	    return list;
	}
	
}
