package com.vmfg.general.dao.interfaces;

import java.util.List;

import com.vmfg.util.entity.MessageLogEntity;
import com.vmfg.util.entity.MessageTemplateEntity;

public interface IOutllookNotificationDAO {

	String getEmpEmailIdDtl(String desigCode, String tenantId);

	String getProjName(String pmHdrId, String tenantId);

	MessageTemplateEntity getMsgTemplateDtls(String type, String tenantId);

	int insertInMessageLog(String type, String msgSub, String path, String toEmp, String tenantId);

	List<MessageLogEntity> getMsgLogDtl(String type, String tenantId);

	void updateSentStatus(String msgLogId);

	String getDocTypeDesc(String docTypeCode, String tenantId);

	String getProjCode(String pmHdrId, String tenantId);

	String getOrganizationInfo(String tenantId);

	String getUniqStgCode(String docTypeCode, String tenantId);

}
