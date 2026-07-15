package com.vmfg.mis.response;

import java.util.List;

import com.vmfg.mis.entity.SalesOrderDetailsList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSalesOrderDetailsResponse {

	List<SalesOrderDetailsList> leadList;
	List<SalesOrderDetailsList> wonList;
	List<SalesOrderDetailsList> lostList;
	List<SalesOrderDetailsList> holdList;
	
}
