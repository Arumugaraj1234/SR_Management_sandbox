package com.vmfg.export.dao.interfaces;

import java.sql.Connection;
import java.util.List;

import com.vmfg.export.entity.DcRequestDtlEntity;
import com.vmfg.export.entity.DcRequestHdrEntity;

public interface IDeliveryChallanReportTrackerDAO {

	String getPropValueByTenant(String tenantId, String propertyName);
	String getCurrentDateTime();
	String getProjectTrackerPath(String tenantId, String key,String isDestination);
	Connection getConnection();
	String getDcCodeCode(String tenantId, String dcId);
	
	String insertdcReqHdr(String reqOn,String reqBy,String pmHdrId,String remarks,String isComplested,String tenantId, String mrHdrId);
	String insertdcReqDtl(String dcrId,String productId,String  descofGoods,String Qty,String closedQty,String tenantid);
	
	List<DcRequestHdrEntity> getDcreqHdr(String pmHdrId, String tenantId, String locTypeCode);
	List<DcRequestDtlEntity> getDcreqDtl(String dcrId, String tenantId);
	int updateDcqty(String dtlId, String qty);
	int getdcrId(String res1);
	void updateDcrequest(int dcrid);
	int getcheckqty(int dcrId);
	String getMrHdrId(String dcrId);
	void updateMRIscompletedStatus(String mrHdrId);
	String getLocTypeCode(String name, String tenantId);
	
	

}
