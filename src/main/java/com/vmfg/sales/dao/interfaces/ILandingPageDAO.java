package com.vmfg.sales.dao.interfaces;

import java.util.List;

import com.vmfg.sales.entity.CustomerMstEntity;
import com.vmfg.sales.entity.SalesEnqContactEntity;
import com.vmfg.sales.entity.SalesEnqDtlEntity;

public interface ILandingPageDAO {
		
	List<SalesEnqDtlEntity> saleEnqDtlBydate(String fromDate,String toDate,String customerName,String tenantId,String empId,String tentativePoValue,String isExpectedPoDate);
	List<SalesEnqDtlEntity> saleEnqDtlByslaveId(String slaveId);
	List<SalesEnqContactEntity> getSalesContactDtl(String slaveId);
	List<CustomerMstEntity> customerMstList(String customerName,String tenantId);
	String getEmloyeeNames(String seId, String tenantId);
	String customerMstContactList(String sceId);
}
