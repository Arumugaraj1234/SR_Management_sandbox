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

import com.vmfg.general.dao.interfaces.INotificationGeneralDAO;
import com.vmfg.general.entity.ApprovalDesignationEntity;
import com.vmfg.general.entity.NotificationReqEntity;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.general.rowmapper.ApprovalDesignationRowMapper;
import com.vmfg.general.rowmapper.NotificationReqRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class NotificationGeneralDAO implements INotificationGeneralDAO {
	private static final Logger logger = LoggerFactory.getLogger(NotificationGeneralDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<NotificationReqEntity> getNotifyDtlList(String tENANT_ID, String empId) {
		List<NotificationReqEntity> NotificationReqEntity = new ArrayList<NotificationReqEntity>();
		try {
			int NotifyDays=Integer.valueOf(GetPropertyValue.getPropValue("NOTIFY_DAYS", tENANT_ID, jdbcTemplate));
			String notifyQry="SELECT \r\n" +
					"    *\r\n" +
					"FROM\r\n" +
					"    notification_req\r\n" +
					"WHERE\r\n" +
					"     TENANT_ID='"+tENANT_ID+"' and FIND_IN_SET('"+empId+"', EMPLOYEE_ID)"
							+ " and DATE(NOTIFY_DATETIME) BETWEEN DATE_SUB(CURDATE(), INTERVAL "+NotifyDays+" DAY) AND CURDATE() \r\n"
							+ " and NOTIFICATION_MESSAGE NOT LIKE '% - Approved'"
							+ " and NOTIFICATION_MESSAGE NOT LIKE '%Approved.' \r\n" +
					"GROUP BY NOTIFICATION_TYPE , NOT_REQ_ID;";
			
			NotificationReqEntity=this.jdbcTemplate.query(notifyQry, new NotificationReqRowMapper());
			
		}catch(Exception ex) {
			logger.error("NotificationGeneralDAO error "+ex);
		}

		return NotificationReqEntity;
	}

	@Override
	public ResponseAsMessage updateNotificationDetails(String tENANT_ID, String eMP_ID) {
		ResponseAsMessage resp = new ResponseAsMessage();
		try {
			String updateQry="update notification_req set NOTIFIED =1 , "
					+ "NOTIFICATION_VIEWED_DATETIME=? where EMPLOYEE_ID=? and TENANT_ID=?;";
			int updateDtl=this.jdbcTemplate.update(updateQry,CommonMethod.getCurrentDateTime(),eMP_ID,tENANT_ID);
			if (updateDtl > 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.successUpdated);
				resp.setResponseDataMessage(ResponseMessageMap.successUpdated); 

			}else {
				resp.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				resp.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				resp.setResponseDataMessage(ResponseMessageMap.failToupdateMsg); 

			}
		}catch(Exception ex) {
			logger.error("updateNotificationDetails error "+ex);
		}

		return resp;
	}

	@Override
	public int getNotifyCount(String tENANT_ID,String empId) {
		int count=0;
		
		try {
			String countQry="select count(*) as VALUE from notification_req where "
					+ "EMPLOYEE_ID= ? and NOTIFIED =0 and TENANT_ID= ? "
					+ "and NOTIFICATION_MESSAGE NOT LIKE '% - Approved' "
					+ "and NOTIFICATION_MESSAGE NOT LIKE '%Approved.' ;\r\n" ;
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(countQry,empId,tENANT_ID);
			count = Integer.parseInt(resultMap.get("VALUE").toString());
		}catch(Exception ex) {
			logger.error("getNotifyCount error "+ex);
		}
		return count;
	}

	@Override
	public String getEmpDesinationCode(String tENANT_ID, String eMP_ID) {
		String getEmpDesinationCode = "";
		try {
			String getEmpDesinationCodeStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN DESIGNATION_CODE\r\n" + "        ELSE ''\r\n"
					+ "    END AS DESIGNATION_CODE\r\n" + "FROM\r\n" + "    employee_mst\r\n" + "WHERE\r\n"
					+ "    EMPLOYEE_ID = ? \r\n" + "        AND TENANT_ID = ? ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getEmpDesinationCodeStr,eMP_ID,tENANT_ID);
			getEmpDesinationCode = resultMap.get("DESIGNATION_CODE").toString();
		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return getEmpDesinationCode;
	}

	@Override
	public List<ApprovalDesignationEntity> getApprovalList(String tenantId, String empDesc) {
		// TODO Auto-generated method stub
		List<ApprovalDesignationEntity> result=null;
		try {
			String ApprovalQry="SELECT \r\n" + 
					"    hdr.DN_APP_ID,\r\n" + 
					"    IFNULL(hdr.PM_HDR_ID, '') AS PM_HDR_ID,\r\n" + 
					"    DN_DTL_ID,\r\n" + 
					"    REFERENCE_ID,\r\n" + 
					"    REFERENCE_CODE,\r\n" + 
					"    DOC_TYPE_CODE,\r\n" + 
					"    pr.CUSTOMER_NAME,\r\n" + 
					"    pr.PROJECT_NAME,\r\n" + 
					"    pr.PROJECT_CODE,\r\n" + 
					"    dst.DOCUMENT_TYPE_DESCRIPTION,\r\n" + 
					"    dtl.DESIGNATION,\r\n" +
					"    hdr.ENQUIRY_ID, enq.ENQUIRY_CODE, \r\n" + 
					"    enq.IS_INTERNAL,\r\n" + 
					"    IFNULL(dtl.PM_ID, '') AS PM_ID,\r\n" + 
					"    DATE_FORMAT(dtl.INSERTED_DATETIME, '%d-%b-%Y') AS DATE\r\n" + 
					"FROM\r\n" + 
					"    document_approve_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_approve_dtl dtl ON hdr.DN_APP_ID = dtl.DN_APP_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    project_hdr pr ON pr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    document_type_mst dst ON dst.DOCUMENT_TYPE_CODE = hdr.DOC_TYPE_CODE\r\n" +
					"        LEFT JOIN \r\n" +
					"    sales_enq_hdr enq ON enq.SE_ID = hdr.ENQUIRY_ID or enq.SE_ID = pr.ENQUIRY_ID \r\n" +
					"WHERE\r\n" +
					"    hdr.TENANT_ID = '"+tenantId+"' AND dtl.TENANT_ID = '"+tenantId+"'\r\n" +
					"        AND dtl.DESIGNATION = '"+empDesc+"'\r\n" +
					"        AND (hdr.DOC_TYPE_CODE != 'DC077' OR NOT EXISTS (\r\n" +
					"            SELECT 1 FROM pra_hdr p\r\n" +
					"            INNER JOIN po_hdr po ON po.PO_ID = p.PO_ID\r\n" +
					"            WHERE p.PRA_ID = hdr.REFERENCE_ID\r\n" +
					"              AND p.TENANT_ID = hdr.TENANT_ID\r\n" +
					"              AND po.IS_APPROVED = '1'))\r\n" +
					"        AND (hdr.DOC_TYPE_CODE != 'DC038' OR NOT EXISTS (\r\n" +
					"            SELECT 1 FROM indent_grp_scs igs\r\n" +
					"            WHERE igs.IG_SCS_ID = hdr.REFERENCE_ID\r\n" +
					"              AND igs.IS_APPROVED = '1'))";
			result=this.jdbcTemplate.query(ApprovalQry, new ApprovalDesignationRowMapper());
			
		}catch(Exception ex) {
			logger.error("getApprovalList Error" + ex);
		}
		return result;
	}
	
	@Override
	public String getTableNameforNotification(String tenantId, String pmId) {
		String result = null;
		try {
			String query = "SELECT DISTINCT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN MASTER_TABLE_NAME\r\n" + 
					"        ELSE 'NA'\r\n" + 
					"    END MASTER_TABLE_NAME\r\n" + 
					"FROM document_type_mst\r\n" + 
					"WHERE TENANT_ID = ? \r\n" + 
					"  AND PM_ID = ? \r\n" + 
					"LIMIT 1";
			result = this.jdbcTemplate.queryForObject(query, String.class, tenantId, pmId);
		}catch(Exception ex) {
			logger.info("getTableNameforNotification"+ ex);
		}
		
		return result;
	}
	
	@Override
	public int getNotificationRec(String table, String pmHdrId, String pmId, String empId, String type){
		int result = 0;
		try {
			String columnName = null;
			switch(table) {
			case "sales_enq_hdr":
				columnName = "SE_ID";
				break;
			case "design_hdr":
				columnName = "DE_HDR_ID";
				break;
			case "project_hdr":
				columnName = "PM_HDR_ID";
				break;
			case "assy_hdr":
				columnName = "ASSY_HDR_ID";
				break;
			case "scm_hdr":
				columnName = "SCM_HDR_ID";
				break;
			case "quality_hdr":
				columnName = "Q_HDR_ID";
				break;
			case "finance_hdr":
				columnName = "FE_HDR_ID";
				break;
			default:
				columnName = "Default";
			}
			if(!columnName.equalsIgnoreCase("Default") && !columnName.equalsIgnoreCase("SE_ID")) {
				if(type.equalsIgnoreCase("1")) {
					String query = "SELECT COUNT(*)  \r\n" + 
							"FROM\r\n" + 
							"    " + table + " AS hdr\r\n" + 
							"    INNER JOIN process_assigned_team AS pat ON pat.MASTER_ID = hdr." + columnName + "\r\n" + 
							"WHERE\r\n" + 
							"    hdr.PM_HDR_ID = ? \r\n" + 
							"    AND pat.PM_ID = ? \r\n" + 
							"    AND pat.ASSIGNED_EMP_ID = ? \r\n" + 
							"    AND pat.IS_ACTIVE = 1;\r\n" + 
							"";
					result = this.jdbcTemplate.queryForObject(query, Integer.class,  pmHdrId, pmId, empId);
				}else {
					String query = "SELECT MASTER_ID  \r\n" + 
							"FROM\r\n" + 
							"    " + table + " AS hdr\r\n" + 
							"    INNER JOIN process_assigned_team AS pat ON pat.MASTER_ID = hdr." + columnName + "\r\n" + 
							"WHERE\r\n" + 
							"    hdr.PM_HDR_ID = ? \r\n" + 
							"    AND pat.PM_ID = ? \r\n" + 
							"    AND pat.ASSIGNED_EMP_ID = ? \r\n" + 
							"    AND pat.IS_ACTIVE = 1;\r\n" + 
							"";
					Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, pmHdrId, pmId, empId);
					result = Integer.valueOf(resultMap.get("MASTER_ID").toString());
				}		
			}
		} catch(Exception ex) {
			logger.info("getNotificationRec"+ ex);
		}		
		return result;
	}

	@Override
	public String getIndentCurrentSeq(String refId, String tenantId) {
		String count="";
		
		try {
			String countQry="select SEQUENCE_N0 from indent_hdr where INDENT_ID = ? and TENANT_ID = ?" ;
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(countQry,refId,tenantId);
			count = resultMap.get("SEQUENCE_N0").toString();
		}catch(Exception ex) {
			logger.error("getIndentCurrentSeq error "+ex);
		}
		return count;
	}
	
}
