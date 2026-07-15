package com.vmfg.mis.dao.interfaces;

import java.util.List;

import com.vmfg.mis.entity.EmployeeEntity;
import com.vmfg.mis.entity.GetCustomerOrderDtlEntity;
import com.vmfg.mis.entity.GetSalesContDtlEntity;
import com.vmfg.mis.entity.GetSalesConvRatioEntity;
import com.vmfg.mis.entity.GetSalesStageDtlEntity;
import com.vmfg.mis.entity.SalesOrderDetailsList;
import com.vmfg.scm.entity.ProjectHdrDtlEntity;

public interface ISalesMisDAO {

	List<SalesOrderDetailsList>leadList(String startDate ,String endDate,String tenantId,String pmId,String empId);
	
	List<SalesOrderDetailsList>enqList(String startDate ,String endDate,String tenantId,String pmId,String empId);
	
	List<SalesOrderDetailsList>wonList(String startDate ,String endDate,String tenantId,String pmId,String empId);
	
	List<SalesOrderDetailsList>lostList(String startDate ,String endDate,String tenantId,String pmId,String empId);
	
	List<SalesOrderDetailsList>holdList(String startDate ,String endDate,String tenantId,String pmId,String empId);
	
	List<GetSalesStageDtlEntity>getSalesStageDtl(String startDate ,String endDate,String tenantId,String pmId,String empId);
	
	List<GetCustomerOrderDtlEntity>getCustomerOrderDtl(String startDate ,String endDate,String tenantId,String pmId,String empId);
	
	List<GetSalesConvRatioEntity>getSalesConvRatio(String startDate ,String endDate,String tenantId,String pmId,String empId);
	
	List<GetSalesContDtlEntity>getSalesContDtl(String startDate ,String endDate,String tenantId ,String projectId,String pmId,String empId);
	
	List<SalesOrderDetailsList> saleMISEnqDtlList(String startDate ,String endDate,String tenantId,String empId);

	List<ProjectHdrDtlEntity> getSalesContProjects(String fromDate, String toDate, String tenantId, String projectId,
			String pmId, String empId);
	

	List<EmployeeEntity> getSalesDeptEmployees(String tenantId, String depCode,String empId,String isHod);

}
