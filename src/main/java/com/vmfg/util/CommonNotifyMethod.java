package com.vmfg.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.services.impl.OutlookNotificationService;

@Transactional
@Repository
public class CommonNotifyMethod {
	@Autowired
	JdbcTemplate jdbctemplate;
	
	@Autowired
	OutlookNotificationService OutlookNotificationService;
	

	private static final Logger logger = LoggerFactory.getLogger(CommonNotifyMethod.class);

	public String InvokeNotificationMethod(int notifyType, int notifyMessage, String empId, String tenantId,
			List<String> messageList, List<String> otherEmpId, String assignTeam, String pmId, String masterId,
			String approveDesig) {
		String Notification="";
		try {
			logger.info("InvokeNotificationMethod: {} {} {} {} {} {} {} {} {} {}", 
					notifyType, notifyMessage, empId, tenantId, messageList, otherEmpId, 
					assignTeam, pmId, masterId, approveDesig);

			Notification=NotificationRequest.notificationreq(notifyType,notifyMessage,empId,
					tenantId,messageList,otherEmpId,assignTeam,pmId,masterId,approveDesig,jdbctemplate);
			
		}catch(Exception ex) {
			logger.error("InvokeNotificationMethod error "+ex);
		}
		return Notification;

	}

	public String InvokeApprovalDesigMethod(String pmId, String docTypeCode, String refId, String pmHdrId,
			String tenatId, String employeeId, String designation, String enqId,String refCode) {
		String Approvals="";
		try {
			 Approvals = DocumentApprovalInsert.InsertDocApprovalDtls(pmId, docTypeCode, refId, pmHdrId, tenatId,
					employeeId, designation, enqId,refCode, jdbctemplate);
//			 OutlookNotificationService.OutLookNotify(designation, pmHdrId, pmId, docTypeCode, tenatId, refCode, "");
		}catch(Exception ex) {
			logger.error("InvokeApprovalDesigMethod error "+ex);
		}
		return Approvals;
	}
	
	public String getNxtAppDesc (String docTypeCode, String currSeq,String tenantId) {
		String desc="";
		try {
			String ApprovingDescQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN APPR_DESI\r\n" + 
					"        ELSE ''\r\n" + 
					"    END APPR_DESI\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst\r\n" + 
					"WHERE\r\n" + 
					"    DOC_TYPE = ? AND TENANT_ID=?\r\n" + 
					"        AND CURR_SEQUENCE = (SELECT \r\n" + 
					"            NEXT_SEQ\r\n" + 
					"        FROM\r\n" + 
					"            document_lifecycle_mst\r\n" + 
					"        WHERE\r\n" + 
					"            DOC_TYPE = ?\r\n" + 
					"                AND CURR_SEQUENCE = ? AND TENANT_ID=?);";
			
			Map<String, Object> resultMap = jdbctemplate.queryForMap(ApprovingDescQry,docTypeCode,tenantId,docTypeCode,currSeq,tenantId);
			desc = resultMap.get("APPR_DESI").toString();
		}catch(Exception ex) {
			logger.error("getNxtAppDesc error "+ex);
		}
		return desc;
	}
	public String getNxtAppDescByDocGroup (String docTypeCode, String currSeq, String docGroup,String tenantId) {
		String desc="";
		try {
			String ApprovingDescQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN APPR_DESI\r\n" + 
					"        ELSE ''\r\n" + 
					"    END APPR_DESI\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst\r\n" + 
					"WHERE\r\n" + 
					"    DOC_TYPE = ? AND TENANT_ID=? and DOC_GROUP = ?\r\n" + 
					"        AND CURR_SEQUENCE = (SELECT \r\n" + 
					"            NEXT_SEQ\r\n" + 
					"        FROM\r\n" + 
					"            document_lifecycle_mst\r\n" + 
					"        WHERE\r\n" + 
					"            DOC_TYPE = ?\r\n" + 
					"                AND CURR_SEQUENCE = ? AND DOC_GROUP = ? AND TENANT_ID=?);";
			
			Map<String, Object> resultMap = jdbctemplate.queryForMap(ApprovingDescQry,docTypeCode,tenantId,docGroup, docTypeCode,currSeq,docGroup,tenantId);
			desc = resultMap.get("APPR_DESI").toString();
		}catch(Exception ex) {
			logger.error("getNxtAppDescByDocGroup error "+ex);
		}
		return desc;
	}
	
	public String getNxtAppDocDesc (String docTypeCode, String currSeq,String tenantId) {
		String docdesc="";
		try {
			String ApprovingDescQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN dt.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + 
					"        ELSE ''\r\n" + 
					"    END DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst mst\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_status_type_code dt ON dt.DOCUMENT_STATUS_TYPE_CODE = mst.DOC_STATUS\r\n" + 
					"WHERE\r\n" + 
					"    DOC_TYPE = ? AND mst.TENANT_ID=?\r\n" + 
					"        AND CURR_SEQUENCE = (SELECT \r\n" + 
					"            NEXT_SEQ\r\n" + 
					"        FROM\r\n" + 
					"            document_lifecycle_mst\r\n" + 
					"        WHERE\r\n" + 
					"            DOC_TYPE = ?\r\n" + 
					"                AND CURR_SEQUENCE = ? AND TENANT_ID=?);";
			
			Map<String, Object> resultMap = jdbctemplate.queryForMap(ApprovingDescQry,docTypeCode,tenantId,docTypeCode,currSeq,tenantId);
			docdesc = resultMap.get("DOCUMENT_STATUS_TYPE_DESCRIPTION").toString();
					
		}catch(Exception ex) {
			logger.error("getNxtAppDocDesc error "+ex);
		}
		return docdesc;
	}
	
	
	public String getPrimaryPOC (String pmId, String tenantId) {
		String pocDesc="";
		try {
			String pocDesigQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN PRIMARY_POC\r\n" + 
					"        ELSE ''\r\n" + 
					"    END PRIMARY_POC\r\n" + 
					"FROM\r\n" + 
					"    project_wbs_initiation_mst\r\n" + 
					"WHERE\r\n" + 
					"    PM_ID = ? and TENANT_ID=? ";
			
			Map<String, Object> resultMap = jdbctemplate.queryForMap(pocDesigQry,pmId,tenantId);
			pocDesc = resultMap.get("PRIMARY_POC").toString();
		}catch(Exception ex) {
			logger.error("getPrimaryPOC error "+ex);
		}
		return pocDesc;
	}
	public String getApprovDesc(String pmId ,String masterId ,String tenantId) {
		String approvDesc="";
		try {
			String pocDesigQry="SELECT \n"
					+ "   group_concat( distinct(DESIGNATION_CODE)) AS DESIGNATION_CODE \n"
					+ "FROM\n"
					+ "    quality_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    process_assigned_team pa ON hdr.Q_HDR_ID = pa.MASTER_ID\n"
					+ "        INNER JOIN\n"
					+ "    employee_mst em ON em.EMPLOYEE_ID = pa.ASSIGNED_EMP_ID\n"
					+ "WHERE\n"
					+ "    pa.PM_ID = ? AND pa.IS_ACTIVE = 1 and MASTER_ID = ? and hdr.TENANT_ID =? ";
			
			Map<String, Object> resultMap = jdbctemplate.queryForMap(pocDesigQry,pmId,masterId,tenantId);
			approvDesc = resultMap.get("DESIGNATION_CODE").toString();
		}catch(Exception ex) {
			logger.error("getApprovDesc error "+ex);
		}
		return approvDesc;
	}
	/*
	 * public String getPOCode(String poId) { String poCode=""; try { String
	 * poStringDesc="SELECT \r\n" + "    CASE\r\n" +
	 * "        WHEN COUNT(*) > 0 THEN PO_ID\r\n" + "        ELSE ''\r\n" +
	 * "    END PO_ID\r\n" + "FROM\r\n" + "    po_dtl\r\n" + "WHERE\r\n" +
	 * "    PO_ID = ? and TENANT_ID=?;";
	 * poCode=this.jdbctemplate.queryForObject(poStringDesc, String.class);
	 * }catch(Exception ex) { logger.error("getPOCode error "+ex); } return poCode;
	 * }
	 */

	public String getNxtAppDescByDocGroup(String docTypeCode, String currSeq, String docGroup, String tenantId,
			String processDoc) {
		String desc="";
		try {
			String ApprovingDescQry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN APPR_DESI\r\n" + 
					"        ELSE ''\r\n" + 
					"    END APPR_DESI\r\n" + 
					"FROM\r\n" + 
					"    document_lifecycle_mst\r\n" + 
					"WHERE\r\n" + 
					"    DOC_TYPE = ? AND TENANT_ID=? and DOC_GROUP = ? and PROCESS_CODE=? \r\n" + 
					"        AND CURR_SEQUENCE = (SELECT \r\n" + 
					"            NEXT_SEQ\r\n" + 
					"        FROM\r\n" + 
					"            document_lifecycle_mst\r\n" + 
					"        WHERE\r\n" + 
					"            DOC_TYPE = ?\r\n" + 
					"                AND CURR_SEQUENCE = ? AND DOC_GROUP = ? AND PROCESS_CODE=? AND TENANT_ID=?);";
			
			Map<String, Object> resultMap = jdbctemplate.queryForMap(ApprovingDescQry,docTypeCode,tenantId,docGroup,processDoc,docTypeCode,currSeq,docGroup,processDoc,tenantId);
			desc = resultMap.get("APPR_DESI").toString();
		}catch(Exception ex) {
			logger.error("getNxtAppDescByDocGroup error "+ex);
		}
		return desc;
	}
}
