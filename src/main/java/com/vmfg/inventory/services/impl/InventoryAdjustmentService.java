package com.vmfg.inventory.services.impl;

import java.time.LocalDateTime;
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
import com.vmfg.inventory.dao.interfaces.IInventoryAdjustmentDao;
import com.vmfg.inventory.entity.AdjustmentTypeDropDownEntity;
import com.vmfg.inventory.entity.InventoryAdjustmentEntity;
import com.vmfg.inventory.request.InsertAdjustmentRequest;
import com.vmfg.inventory.servisec.interfaces.IInventoryAdjustmentService;
import com.vmfg.scm.request.ProjectDtlRequest;

@Service
public class InventoryAdjustmentService implements IInventoryAdjustmentService{
	
private static final Logger logger = LoggerFactory.getLogger(InventoryAdjustmentService.class);
	
	@Autowired
	IInventoryAdjustmentDao iInventoryAdjustmentDao;

	@Override
	public ResponseAsList retrieveinventoryAdjustment(ProjectDtlRequest projectdtlreq) {
		// TODO Auto-generated method stub
		ResponseAsList returnList = new ResponseAsList();
		String fromDate = projectdtlreq.getFromDate();
		String toDate = projectdtlreq.getToDate();
		String tenantId = projectdtlreq.getTenantId();
		List<InventoryAdjustmentEntity> list = new ArrayList<>(); 
		try {
			
			list = iInventoryAdjustmentDao.retrieveinventoryAdjustment(fromDate,toDate,tenantId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("InventoryAdjustmentService service error " + e);
		}
		
		return returnList;
	}

	@Override
	public ResponseAsList getadjustmettypedropdown(TenantRequest tenanttreq) {
		ResponseAsList returnList = new ResponseAsList();
	
		String tenantId = tenanttreq.getTenantID();
		List<AdjustmentTypeDropDownEntity> list = new ArrayList<>(); 
		try {
			
			list = iInventoryAdjustmentDao.getadjustmettypedropdown(tenantId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("getadjustmettypedropdown service error " + e);
		}
		
		return returnList;
	}

	@Override
	public ResponseAsMessage insertAdjustment(InsertAdjustmentRequest insertadjustreq) {
		// TODO Auto-generated method stub
		LocalDateTime   adjustmentDateTime = LocalDateTime.now();
		
		String projectId= insertadjustreq.getProjectId();
		String productId = insertadjustreq.getProductId();
		String locationCode = insertadjustreq.getLocationCode();
		String adjustmentType = insertadjustreq.getAdjustmentType();
		String qtyonHand = insertadjustreq.getQtyonHand();
		String adjustmentQty = insertadjustreq.getAdjustedQty();
		String revisedQty = insertadjustreq.getRevisedQty();
		String adjustmentedBy = insertadjustreq.getAdjustmentedBy();
		String reason = insertadjustreq.getReason();
		String tenantId =insertadjustreq.getTenantId();
		String productCode = insertadjustreq.getProductCode();
		
		ResponseAsMessage responseMsg = new ResponseAsMessage();
		int insert = 0;
		try {
			insert = iInventoryAdjustmentDao.insertAdjustment(projectId,productId,locationCode,adjustmentType,qtyonHand,
																	adjustmentQty,revisedQty,adjustmentedBy,adjustmentDateTime,reason,tenantId,productCode);
			String res =  String.valueOf(insert);
			if (insert > 0) {

				responseMsg.setResponseCode(ResponseMessageMap.success);
				responseMsg.setResponseMessage(ResponseMessageMap.successUpdated);
				responseMsg.setResponseDataMessage(res);
			} else {
				responseMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
				responseMsg.setResponseDataMessage("Fail to update");
				responseMsg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("insertAdjustment service error " + e);
		}
		
		
		return responseMsg;
		}

}
