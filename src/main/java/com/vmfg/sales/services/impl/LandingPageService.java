package com.vmfg.sales.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.sales.dao.interfaces.ILandingPageDAO;
import com.vmfg.sales.entity.CustomerMstEntity;
import com.vmfg.sales.entity.SalesEnqContactEntity;
import com.vmfg.sales.entity.SalesEnqDtlEntity;
import com.vmfg.sales.request.GetEnqDtlbyDateRequest;
import com.vmfg.sales.request.GetEnqDtlbySlaveIdRequest;
import com.vmfg.sales.services.interfaces.ILandingPageService;
@Service
public class LandingPageService implements ILandingPageService{
	private static final Logger logger = LoggerFactory.getLogger(LandingPageService.class);
	
	@Autowired
	ILandingPageDAO iLandingPageDAO;
	@Override
	public ResponseAsList getEnqDtlbyDate(GetEnqDtlbyDateRequest getEnqDtlbyDateRequest) {
		ResponseAsList list =new ResponseAsList();
		try {
			String fromDate=getEnqDtlbyDateRequest.getFromDate();
			String toDate  =getEnqDtlbyDateRequest.getToDate();
			String customerName=getEnqDtlbyDateRequest.getCustomerName();
			String tenantId = getEnqDtlbyDateRequest.getTenantId();
			String empId = getEnqDtlbyDateRequest.getEmpId();
			List<SalesEnqDtlEntity> saleEnqDtl = iLandingPageDAO.saleEnqDtlBydate(fromDate, toDate, customerName,tenantId,empId,getEnqDtlbyDateRequest.getTentativePoVal(),getEnqDtlbyDateRequest.getIsExpectedPoDate());
			if(saleEnqDtl.size()>0) {
			
				for(int i =0;i<saleEnqDtl.size();i++) {
					Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(saleEnqDtl.get(i).getExpectedPoDate());
					DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");  
					saleEnqDtl.get(i).setTentativePoMonth(dateFormat.format(date1));
					List<SalesEnqContactEntity> getSalesContactDtl = iLandingPageDAO.getSalesContactDtl(saleEnqDtl.get(i).getSeId());
					String empNames=iLandingPageDAO.getEmloyeeNames(saleEnqDtl.get(i).getSeId(),tenantId);
					saleEnqDtl.get(i).setEmployeeNames(empNames);
					saleEnqDtl.get(i).setSalesContact(getSalesContactDtl);
					
					List<CustomerMstEntity> customerMstList  = iLandingPageDAO.customerMstList(saleEnqDtl.get(i).getCustomerName(), saleEnqDtl.get(i).getTenantId());
					saleEnqDtl.get(i).setCustMstEntity(customerMstList);
				}
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(saleEnqDtl);
			}else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(saleEnqDtl);
			}
		}catch(Exception ex) {
			logger.error("getEnqDtlbyDate Error " + ex);
		}
		return list;
	}
	@Override
	public ResponseAsList getEnqDtlbySlaveId(GetEnqDtlbySlaveIdRequest getEnqDtlbySlaveIdReq) {
		ResponseAsList list =new ResponseAsList();
		try {
			String slaveId=getEnqDtlbySlaveIdReq.getSlaveId();
			List<SalesEnqDtlEntity> saleEnqDtl = iLandingPageDAO.saleEnqDtlByslaveId(slaveId);
			if(saleEnqDtl.size()>0) {
				for(int i =0;i<saleEnqDtl.size();i++) {
					List<SalesEnqContactEntity> getSalesContactDtl = iLandingPageDAO.getSalesContactDtl(saleEnqDtl.get(i).getSeId());
					saleEnqDtl.get(i).setSalesContact(getSalesContactDtl);
					CustomerMstEntity obj = new CustomerMstEntity();
					List<CustomerMstEntity> customerMstList  = iLandingPageDAO.customerMstList(saleEnqDtl.get(i).getCustomerName(), saleEnqDtl.get(i).getTenantId());
					if(getSalesContactDtl.get(i).isPrimary() == true) {
				   String customerMstContactList  = iLandingPageDAO.customerMstContactList(getSalesContactDtl.get(i).getSecId());
                    obj.setContactNo(customerMstContactList);
					}
					if(customerMstList != null && customerMstList.size()>0) {
					customerMstList.get(0).setContactNo(obj.getContactNo());
					}else {
					 CustomerMstEntity customerNewMstList = new CustomerMstEntity();
						customerNewMstList.setContactNo(obj.getContactNo());
						customerMstList.add(customerNewMstList);
					}
					saleEnqDtl.get(i).setCustMstEntity(customerMstList);		
				}
				list.setResponseCode(ResponseMessageMap.success);
				list.setResponseMessage(ResponseMessageMap.responseCodeOk);
				list.setResponseData(saleEnqDtl);
			}else {
				list.setResponseCode(ResponseMessageMap.noRecord);
				list.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				list.setResponseData(saleEnqDtl);
			}
		}catch(Exception ex) {
			logger.error("getEnqDtlbySlaveId Error " + ex);
		}
		return list;
	}


}
