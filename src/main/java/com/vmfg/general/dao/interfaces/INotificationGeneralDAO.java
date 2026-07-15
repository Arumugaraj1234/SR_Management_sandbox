package com.vmfg.general.dao.interfaces;

import java.util.List;

import com.vmfg.general.entity.ApprovalDesignationEntity;
import com.vmfg.general.entity.NotificationReqEntity;
import com.vmfg.general.response.ResponseAsMessage;

public interface INotificationGeneralDAO {

	List<NotificationReqEntity> getNotifyDtlList(String tENANT_ID, String empId);

	ResponseAsMessage updateNotificationDetails(String tENANT_ID, String eMP_ID);

	int getNotifyCount(String tENANT_ID, String empId);

	String getEmpDesinationCode(String tENANT_ID, String eMP_ID);

	List<ApprovalDesignationEntity> getApprovalList(String tENANT_ID, String empDesc);

	String getTableNameforNotification(String tenantId, String pmId);

	int getNotificationRec(String tableName, String projectId, String pmId, String eMP_ID, String type);

	String getIndentCurrentSeq(String refId, String tenantId);

}
