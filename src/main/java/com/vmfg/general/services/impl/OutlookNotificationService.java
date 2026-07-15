package com.vmfg.general.services.impl;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.email.Email;
import com.vmfg.export.dao.impl.ProjectReportTrackerDAO;
import com.vmfg.general.dao.interfaces.IOutllookNotificationDAO;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.entity.MessageLogEntity;
import com.vmfg.util.entity.MessageTemplateEntity;

@Service
public class OutlookNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(OutlookNotificationService.class);
	
	@Autowired
	IOutllookNotificationDAO IOutllookNotificationDAO;
	
	@Autowired
	ProjectReportTrackerDAO projectReportTrackerDAO;

	public ResponseAsMessage OutLookNotify(String desigCode, String pmHdrId, String pmId, String docTypeCode,
			String tenantId, String refCode, String stgCode) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		MessageTemplateEntity msgTemplate = new MessageTemplateEntity();
		List<MessageLogEntity> messageLogList=new ArrayList<MessageLogEntity>();
		Boolean mailSentStatus=false;
		try {
			String docTypeDesc = IOutllookNotificationDAO.getDocTypeDesc(docTypeCode,tenantId);
			String uniStgCode = IOutllookNotificationDAO.getUniqStgCode(docTypeCode,tenantId);
			String toEmp = IOutllookNotificationDAO.getEmpEmailIdDtl(desigCode, tenantId);
			String projCode = IOutllookNotificationDAO.getProjCode(pmHdrId,tenantId);
			String projName = IOutllookNotificationDAO.getProjName(pmHdrId,tenantId);
			
			msgTemplate=IOutllookNotificationDAO.getMsgTemplateDtls("3",tenantId);
			msgTemplate.setMsgSub(docTypeDesc.concat("-").concat(refCode));
			String contents = new String(Files.readAllBytes(Paths.get(msgTemplate.getMsgBodyFilePath())));
			String filePath=projectReportTrackerDAO.getPropValueByTenant(tenantId, "OUTLOOK MAIL TEMPLATE");
			
			File UPLOADED_FOLDER = new File(filePath);
			if (!UPLOADED_FOLDER.exists()) {
				UPLOADED_FOLDER.mkdirs();
			}
			
			String orgName = IOutllookNotificationDAO.getOrganizationInfo(tenantId);
			orgName = orgName.replace("/auth/login", "");
			
			String pathUrl = projectReportTrackerDAO.getPropValueByTenant(tenantId, "OUTLOOK_URL");	
			pathUrl = orgName.concat(pathUrl).concat("/").concat("pmId="+pmId+"").concat("/").concat("pmHdrId="+pmHdrId+"").concat("/").concat("docTypeCode="+docTypeCode+"").concat("/")
					.concat("docTypeDesc="+docTypeDesc+"").concat("/").concat("uniStgCode="+uniStgCode+"").concat("/").concat("tenantId="+tenantId+"")
					.concat("/").concat("refCode="+refCode+"");
			
			String path = UPLOADED_FOLDER + File.separator + "OUTLOOK_MAIL"
					+ CommonMethod.getCurrentdateformat() + CommonMethod.getCurrentTimeformat() + ".html";
			File newmodfile = new File(path);
			FileWriter writer = new FileWriter(newmodfile);
			
			contents = contents.replace("projectNumberReplacement", projCode);
			contents = contents.replace("projectNameReplacement", projName);
			contents = contents.replace("docDescriptionReplacement", docTypeDesc);
			contents = contents.replace("refCodeReplacement", refCode);
			contents = contents.replace("createdDateReplacement", CommonMethod.getCurrentDateTime());
			contents = contents.replace("titleReplacement", "BGR NEO");
			contents = contents.replace("viewLinkReplacement", pathUrl);
			
			writer.write(contents);
			writer.close();
			String[] toEmpArray = toEmp.split(",");
			
			for (String empEmail : toEmpArray) {
				int msgLog=IOutllookNotificationDAO.insertInMessageLog("3", msgTemplate.getMsgSub(), path, empEmail, tenantId);
			    messageLogList=IOutllookNotificationDAO.getMsgLogDtl("3",tenantId);
				
			    if(msgLog==1) {
			    	for(MessageLogEntity messageLog : messageLogList ) {
			    		Email emailObj = new Email();
			    		mailSentStatus=emailObj.sendMail(messageLog, msgTemplate);
			    		if(mailSentStatus==true) {
			    			IOutllookNotificationDAO.updateSentStatus(messageLog.getMsgLogId());
							returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
							returnMessage.setResponseDataMessage(ResponseMessageMap.successMsg);
			    		}else {
			    			logger.error("email send status is failed");
			    			returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
							returnMessage.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
			    		}
			    	}
			    }else{
			    	logger.error("Error in message log insert");
			    }
			}		    
		}catch(Exception ex) {
			logger.error("outLookNotify  method  exception" + ex);
		}
		// TODO Auto-generated method stub
		return returnMessage;
	}

}
