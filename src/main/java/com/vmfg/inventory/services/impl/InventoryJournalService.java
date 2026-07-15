package com.vmfg.inventory.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.inventory.dao.interfaces.IInventoryJournalDao;
import com.vmfg.inventory.entity.InventoryJournalEntity;
import com.vmfg.inventory.entity.LocationDropDownEntity;
import com.vmfg.inventory.request.InventoryJournalRequest;
import com.vmfg.inventory.request.InventoryTenantRequest;
import com.vmfg.inventory.servisec.interfaces.IInventoryJournalService;

@Service
public class InventoryJournalService implements IInventoryJournalService{
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryJournalService.class);
	
	@Autowired
	IInventoryJournalDao iInventoryJournalDao;

	public ResponseAsList retrieveinventoryJournal(InventoryJournalRequest projectdtlreq) {
		ResponseAsList returnList= new ResponseAsList();
		String frmDate = projectdtlreq.getFromDate();
		String toDate = projectdtlreq.getToDate();
		String projectId = projectdtlreq.getProjectId();
		String tenantId = projectdtlreq.getTenantId();
		String projId = "";
		if(projectId.equalsIgnoreCase("getall")) {
			projId = "%%";
		}else {
			projId = projectId;
		}
		List<InventoryJournalEntity> list = new ArrayList<InventoryJournalEntity>();
		try {
			list = iInventoryJournalDao.retriveJournal(frmDate,toDate,projId,tenantId);
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
			logger.error("retrieveinventoryJournal service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getInvLocationForInward(InventoryTenantRequest tenantReq) {
		ResponseAsList returnList= new ResponseAsList();
		List<LocationDropDownEntity> list = new ArrayList<LocationDropDownEntity>();
		try {
			list = iInventoryJournalDao.getInvLocationForInward(tenantReq);
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
			logger.error("retrieveinventoryJournal service error " + e);
		}
		return returnList;
	}

}
