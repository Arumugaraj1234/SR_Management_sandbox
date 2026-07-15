package com.vmfg.general.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.general.dao.interfaces.IOutllookNotificationDAO;
import com.vmfg.general.rowmapper.MessageTemplateRepository;
import com.vmfg.util.entity.MessageLogEntity;
import com.vmfg.util.entity.MessageLogRepository;
import com.vmfg.util.entity.MessageTemplateEntity;

@Transactional
@Repository
public class OutlookNotificationDAO implements IOutllookNotificationDAO {
	private static final Logger logger = LoggerFactory.getLogger(OutlookNotificationDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String getEmpEmailIdDtl(String desigCode, String tenantId) {
		String resp = "";
		try {
			String notifyQry="SELECT GROUP_CONCAT(EMAIL_ID) AS EMAIL_ID\r\n" + 
					"FROM employee_mst\r\n" + 
					"WHERE FIND_IN_SET(DESIGNATION_CODE,'"+desigCode+"' ) > 0\r\n" + 
					"  AND EMPLOYMENT_STATUS_CODE = 'ES001';";
			
			Map<String, Object> resultMap=this.jdbcTemplate.queryForMap(notifyQry);
			resp = resultMap.get("EMAIL_ID").toString();
		}catch(Exception ex) {
			logger.error("getEmpEmailIdDtl error "+ex);
		}
		// TODO Auto-generated method stub
		return resp;
	}

	@Override
	public String getProjName(String pmHdrId, String tenantId) {
		String resp = "";
		try {
			String notifyQry="SELECT PROJECT_NAME\r\n" + 
					"FROM project_hdr\r\n" + 
					"WHERE PM_HDR_ID = ? and TENANT_ID= ?;";
			
			Map<String, Object> resultMap=this.jdbcTemplate.queryForMap(notifyQry, pmHdrId, tenantId);
			resp = resultMap.get("PROJECT_NAME").toString();
		}catch(Exception ex) {
			logger.error("getProjName error "+ex);
		}
		// TODO Auto-generated method stub
		return resp;
	}

	@SuppressWarnings("deprecation")
	@Override
	public MessageTemplateEntity getMsgTemplateDtls(String type, String tenantId) {
		List<MessageTemplateEntity> list=new ArrayList<MessageTemplateEntity>();
		try {
			String qry = "SELECT * FROM message_template WHERE MSG_TEMP_ID = ? AND TENANT_ID = ?";
			list = this.jdbcTemplate.query(qry, new Object[]{type, tenantId}, new MessageTemplateRepository());

		}catch (Exception ex) {
			logger.error("getMsgTemplateDtls Error" + ex);
		}
		return list.get(0);
	}

	@Override
	public int insertInMessageLog(String type, String msgSub, String path, String toEmp, String tenantId) {
		int insertStatus = 0;
		try {

			String qry = "INSERT INTO message_log (MSG_TEMP_ID, MSG_SUBJECT, SENT_MSG_FILE_PATH, MSG_TO, TENANT_ID) VALUES (?, ?, ?, ? , ?);";
			insertStatus = this.jdbcTemplate.update(qry, type, msgSub, path, toEmp,tenantId);

		} catch (Exception ex) {
			logger.error("insertInMessageLog Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public List<MessageLogEntity> getMsgLogDtl(String type, String tenantId) {
		List<MessageLogEntity> list=new ArrayList<MessageLogEntity>();
		try {
			
			String qry="select * from message_log where MSG_TEMP_ID='"+type+"' and MSG_SENT_STATUS='0'; ";
			list=this.jdbcTemplate.query(qry,new MessageLogRepository());
			
		}catch (Exception ex) {
			logger.error("getMsgLogDtl Error" + ex);
		}
		return list;
	}

	@Override
	public void updateSentStatus(String msgLogId) {
//		int updateStatus = 0;
		try {

			String qry = "update message_log set MSG_SENT_STATUS  = '1' where MSG_LOG_ID = ?";
			 this.jdbcTemplate.update(qry, msgLogId);
		} catch (Exception ex) {
			logger.error("updateSentStatus Error" + ex);
		}
		return;
	}

	@Override
	public String getDocTypeDesc(String docTypeCode, String tenantId) {
		String resp = "";
		try {
			String notifyQry="select DOCUMENT_TYPE_DESCRIPTION from document_type_mst where DOCUMENT_TYPE_CODE = ? and TENANT_ID = ?";
			
			Map<String, Object> resultMap=this.jdbcTemplate.queryForMap(notifyQry, docTypeCode, tenantId);
			resp = resultMap.get("DOCUMENT_TYPE_DESCRIPTION").toString();
		}catch(Exception ex) {
			logger.error("getProjCode error "+ex);
		}
		// TODO Auto-generated method stub
		return resp;
	}

	@Override
	public String getProjCode(String pmHdrId, String tenantId) {
		String resp = "";
		try {
			String notifyQry="SELECT PROJECT_CODE\r\n" + 
					"FROM project_hdr\r\n" + 
					"WHERE PM_HDR_ID = ? and TENANT_ID= ?;";
			
			Map<String, Object> resultMap=this.jdbcTemplate.queryForMap(notifyQry, pmHdrId, tenantId);
			resp = resultMap.get("PROJECT_CODE").toString();
		}catch(Exception ex) {
			logger.error("getProjCode error "+ex);
		}
		return resp;
	}

	@Override
	public String getOrganizationInfo(String tenantId) {
		String orgNameQry = "select APP_URL from organization_info where TENANT_ID = ? limit 1";
		Map<String, Object> resultMap = jdbcTemplate.queryForMap(orgNameQry,tenantId);
		String orgName  = resultMap.get("APP_URL").toString();
		return orgName;
	}

	@Override
	public String getUniqStgCode(String docTypeCode, String tenantId) {
		String resp = "";
		try {
			String notifyQry="select STG_CODE from document_type_mst where DOCUMENT_TYPE_CODE = ? and TENANT_ID = ?";
			
			Map<String, Object> resultMap=this.jdbcTemplate.queryForMap(notifyQry, docTypeCode, tenantId);
			resp = resultMap.get("STG_CODE").toString();
		}catch(Exception ex) {
			logger.error("getUniqStgCode error "+ex);
		}
		// TODO Auto-generated method stub
		return resp;
	}

}
