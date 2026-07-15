package com.vmfg.master.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.master.dao.interfaces.IFileUploadConfigDAO;
import com.vmfg.master.entity.DocTypeMstEntity;
import com.vmfg.master.entity.FileUploadConfigEntity;
import com.vmfg.master.request.DocTypeMstRequest;
import com.vmfg.master.request.FileUploadConfigRequest;
import com.vmfg.master.request.InsertFileUploadConfigRequest;
import com.vmfg.master.services.interfaces.IFileUploadConfigService;

@Service
public class FileUploadConfigService implements IFileUploadConfigService{
	private static final Logger logger = LoggerFactory.getLogger(FileUploadConfigService.class);
    @Autowired
    IFileUploadConfigDAO iFileUploadConfigDAO;
	
	@Override
	public ResponseAsList docTypeMstDropDwn(DocTypeMstRequest req) {

		ResponseAsList returnList = new ResponseAsList();
		logger.info("docTypeMstDropDwnService  method start");
		try {
			List<DocTypeMstEntity> typeMst = new ArrayList<DocTypeMstEntity>();
			String TENANT_ID = req.getTenantId();
			if ((null != TENANT_ID && !TENANT_ID.isEmpty())) {
				typeMst = iFileUploadConfigDAO.docTypeMstDropDwn(TENANT_ID);
			}
			if (typeMst.size() > 0) {
				returnList.setResponseData(typeMst);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(typeMst);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("docTypeMstDropDwnService  method exception-->" + ex);
		}
		logger.debug("docTypeMstDropDwnService  method end");
		return returnList;
	}

	@Override
	public ResponseAsList getFileUploadConfig(FileUploadConfigRequest fileUpload) {

		ResponseAsList returnList = new ResponseAsList();
		logger.info("getFileUploadConfigService  method start");
		String docCode = fileUpload.getDocCode();
		String tenantId = fileUpload.getTenantId();
		try {
			List<FileUploadConfigEntity> list = new ArrayList<FileUploadConfigEntity>();
				list = iFileUploadConfigDAO.getFileUploadConfig(docCode,tenantId);
			
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getFileUploadConfigService  method exception-->" + ex);
		}
		logger.debug("getFileUploadConfigService  method end");
		return returnList;
	}

	@Override
	public ResponseAsMessage insertUpdateFileUploadConfig(InsertFileUploadConfigRequest insertDtlreq) {
		
		ResponseAsMessage returnList = new ResponseAsMessage();
		String fuCode = insertDtlreq.getFuCode();
		String desc = insertDtlreq.getDesc();
		String tenantId = insertDtlreq.getTenantId();
		String descCode = insertDtlreq.getDescCode();
		int insert = 0;
		int update = 0;
		try {
			if(fuCode.isEmpty()) {
				insert = iFileUploadConfigDAO.insertFileUploadConfig(desc,tenantId,descCode);
			}else {
				update = iFileUploadConfigDAO.updateFileUploadConfig(fuCode,desc,tenantId);
			}
			if(insert == 1 || update == 1) {
				 returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			     returnList.setResponseMessage(ResponseMessageMap.successUpdated);
			}else {
				 returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
				 returnList.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		}catch(Exception e) {
			logger.error("insertUpdateFileUploadConfigService  method exception-->" + e);
		}
		
		return returnList;
	}
	
}