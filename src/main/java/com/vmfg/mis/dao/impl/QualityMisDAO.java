package com.vmfg.mis.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.mis.dao.interfaces.IQualityMisDAO;
import com.vmfg.mis.entity.DrilldownDtlEntity;
import com.vmfg.mis.entity.QualityWidgetDtlEntity;
import com.vmfg.mis.entity.SupplierRatingEntity;
import com.vmfg.mis.entity.TeamMemberLoadEntity;
import com.vmfg.mis.entity.VendorTypeCateCountEntity;
import com.vmfg.mis.rowmapper.DrilldownDtlRespRowMapper;
import com.vmfg.mis.rowmapper.QualityWidgetDtlRowMapper;
import com.vmfg.mis.rowmapper.SupplierRatingRowMapper;
import com.vmfg.mis.rowmapper.TeamMemberLoadRowMapper;
import com.vmfg.mis.rowmapper.VendorTypeCatRowMapper;


@Transactional
@Repository

public class QualityMisDAO implements IQualityMisDAO {
	private static final Logger logger = LoggerFactory.getLogger(QualityMisDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public ResponseAsMessage getQualityProjCnt(String tenantId, String fromDate, String toDate, 
			String empID, String pmId) {
		ResponseAsMessage cnt = new ResponseAsMessage();
	try {
		String Cnt = "SELECT \r\n" + 
				"    case when count(PM_HDR_ID)>0 then COUNT(PM_HDR_ID) else 0 end as count\r\n" + 
				"FROM\r\n" + 
				"    quality_hdr hdr\r\n" + 
				"        INNER JOIN\r\n" + 
				"    process_assigned_team pteam ON hdr.Q_HDR_ID = pteam.MASTER_ID\r\n" + 
				"WHERE\r\n" + 
				"    hdr.TENANT_ID = ?\r\n" + 
				"        AND hdr.IS_COMPLETED = '0'\r\n" + 
				"        AND pteam.ASSIGNED_EMP_ID = ?\r\n" + 
				"        AND PM_ID = ?  AND pteam.IS_ACTIVE=1\r\n" + 
				"        AND DATE(hdr.CREATED_DATETIME) BETWEEN ? AND ?;";
		  Map<String, Object> resultMap = jdbcTemplate.queryForMap(Cnt,  tenantId, empID, pmId, fromDate, toDate);
		  int count = Integer.parseInt(resultMap.get("count").toString());
		  
			  cnt.setResponseCode(ResponseMessageMap.responseCodeOk);
			  cnt.setResponseDataMessage(String.valueOf(count));
			  cnt.setResponseMessage(ResponseMessageMap.success);	 
	}catch (Exception ex) {
		logger.error("getQualityProjCntResp error " + ex.getMessage());
	}
	 return cnt;
	}

	@Override
	public List<QualityWidgetDtlEntity> QualityWidgetDtlResp(String projId, String tenantId, String empID, 
			String pmID,String fromDate, String toDate) {
		List<QualityWidgetDtlEntity> list = new ArrayList<QualityWidgetDtlEntity>();
		int InspCall = 0;
		try {
			String cal ="SELECT \r\n" + 
					"    COUNT(QI_ID) AS INSPECTION_CALL\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_request req\r\n" + 
					"        INNER JOIN\r\n" + 
					"    quality_hdr hdr ON req.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pat ON hdr.Q_HDR_ID = pat.MASTER_ID\r\n" + 
					"WHERE\r\n" + 
					"    pat.PM_ID = ? AND hdr.PM_HDR_ID LIKE ?\r\n" + 
					"        AND hdr.TENANT_ID = ?\r\n" + 
					"        AND pat.ASSIGNED_EMP_ID = ?\r\n" + 
					"        AND req.INSPECTION_REQUESTED_DATE BETWEEN ? AND ?;";
			
			 Map<String, Object> resultMap = jdbcTemplate.queryForMap(cal, pmID, projId, tenantId, empID, fromDate, toDate);
			 InspCall = Integer.parseInt(resultMap.get("INSPECTION_CALL").toString());
			
			String query = "SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN (SUM(ABS(OK_QTY - (CA_INTERNAL + CA_VENDOR)))) > 0 THEN SUM(ABS(OK_QTY - (CA_INTERNAL + CA_VENDOR)))\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS INSPECTION_OK,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN (SUM(REJECTED_EXTERNAL + REJECTED_INTERNAL)) > 0 THEN SUM(REJECTED_EXTERNAL + REJECTED_INTERNAL)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS REJECT_QTY,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN (SUM((CA_INTERNAL + CA_VENDOR))) > 0 THEN SUM((CA_INTERNAL + CA_VENDOR))\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS CA,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN (SUM(REWORK_INTERNAL + REWORK_VENDOR)) > 0 THEN SUM(REWORK_INTERNAL + REWORK_VENDOR)\r\n" + 
					"        ELSE 0\r\n" + 
					"    END AS REWORK_QTY,\r\n" + 
					"    SUM(qhdr.INSPECTION_QTY) AS INSPECTION_QTY\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_request req\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    quality_inspection_hdr qhdr ON req.QI_ID = qhdr.QI_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    quality_hdr hdr ON req.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pat ON hdr.Q_HDR_ID = pat.MASTER_ID\r\n" + 
					"WHERE\r\n" + 
					"    pat.PM_ID = '"+pmID+"'\r\n" + 
					"        AND hdr.PM_HDR_ID LIKE '"+projId+"'\r\n" + 
					"        AND hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND pat.ASSIGNED_EMP_ID = '"+empID+"'\r\n" + 
					"        AND pat.IS_ACTIVE = '1'\r\n" + 
					"        AND qhdr.INSPECTED_ON BETWEEN '"+fromDate+"' AND '"+toDate+"' and qhdr.IS_COMPLETED=1;";
			
			list = this.jdbcTemplate.query(query, new QualityWidgetDtlRowMapper());	
			if(list.size()>0){
				list.get(0).setInspCall(Integer.toString(InspCall));
			}
		}catch(Exception ex) {
			logger.error("QualityWidgetDtlResp DAO Error" + ex);	
		}
		return list;
	}
	
	@Override
	public List<DrilldownDtlEntity> getDrilldownDtlResp(String projId, String tenantId, String typeCode,
			String empID, String pmID, String fromDate, String toDate) {
		List<DrilldownDtlEntity> list = new ArrayList<DrilldownDtlEntity>();
		String typeCodeCol="", query="";
		if(typeCode.equalsIgnoreCase("CA")) {
			typeCodeCol = "(CA_INTERNAL + CA_VENDOR) > 0";
		}else if(typeCode.equalsIgnoreCase("inspOk")) {
			typeCodeCol = "ABS(OK_QTY - (CA_INTERNAL + CA_VENDOR)) > 0";
		}else if (typeCode.equalsIgnoreCase("rej")) {
			typeCodeCol = "(REJECTED_EXTERNAL + REJECTED_INTERNAL) > 0";
		}else if(typeCode.equalsIgnoreCase("reWork")){
			typeCodeCol = "(REWORK_INTERNAL + REWORK_VENDOR) > 0";
		}else if (typeCode.equalsIgnoreCase("inspQty")){
			typeCodeCol = "(req.QTY_TO_BE_INSPECTED) > 0";
		}else {
			typeCodeCol = "qhdr.QI_HDR_ID > 0";
		}

		try {
			
			if(typeCode.equalsIgnoreCase("INSPECTION_QTY")) {
				query = "SELECT  \r\n" + 
						"					    COUNT(qir.QI_ID) as INSP_REQ , COUNT(qi.QI_HDR_ID) as INSP_COMPLETED, \r\n" + 
						"                        SUM(qir.QTY_TO_BE_INSPECTED) as INSPECTION_QTY, SUM(qi.INSPECTION_QTY) as INSPECTION_OK,\r\n" + 
						"                        SUM(CASE WHEN qi.NR_FLAG = 1 THEN qi.INSPECTION_QTY ELSE 0 END) as QC_NOT_REQUIRED_QTY, \r\n" + 
						"                        FORMAT(SUM(qi.INSPECTION_QTY)/SUM(qir.QTY_TO_BE_INSPECTED)*100,0) as UNDER_INSPECTION_SCOPE,\r\n" + 
						"                        format(SUM(CASE WHEN qi.NR_FLAG = 1 THEN qi.INSPECTION_QTY ELSE 0 END)/SUM(qir.QTY_TO_BE_INSPECTED)*100,0) as NOT_UNDER_INSPECTION_SCOPE,\r\n" + 
						"                        hdr.PM_HDR_ID, hdr.PROJECT_CODE, PROJECT_NAME\r\n" + 
						"					FROM \r\n" + 
						"					    quality_inspection_request qir \r\n" + 
						"					LEFT JOIN \r\n" + 
						"					    quality_inspection_hdr qi ON qir.QI_ID = qi.QI_ID AND qi.IS_LATEST=1 \r\n" + 
						"					 INNER JOIN  po_dtl pdl ON qir.PO_DTL_ID = pdl.PO_DTL_ID   \r\n" + 
						"					 INNER JOIN  po_hdr phdr ON phdr.PO_ID = pdl.PO_ID and phdr.SEQUENCE_NO != 3  \r\n" + 
						"                     INNER JOIN project_hdr hdr ON qir.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
						"					WHERE \r\n" + 
						"					    qir.PM_HDR_ID LIKE '"+projId+"' AND qir.TENANT_ID = '"+tenantId+"' AND qir.IS_LATEST=1\r\n" + 
						"                        AND qir.INSPECTION_REQUESTED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' \r\n" + 
						"                        group by qir.PM_HDR_ID";
			}else if(typeCode.equalsIgnoreCase("inspOk")) {
				query = "SELECT  \r\n" + 
						"					    COUNT(qir.QI_ID) as INSP_REQ , COUNT(qi.QI_HDR_ID) as INSP_COMPLETED, \r\n" + 
						"                        SUM(qir.QTY_TO_BE_INSPECTED) as INSPECTION_QTY, SUM(qi.INSPECTION_QTY) as INSPECTION_OK,\r\n" + 
						"                        SUM(CASE WHEN qi.NR_FLAG = 1 THEN qi.INSPECTION_QTY ELSE 0 END) as QC_NOT_REQUIRED_QTY,\r\n" + 
						"                        SUM(qi.OK_QTY)-SUM(qi.CA_INTERNAL + qi.CA_VENDOR) as OK_QTY, SUM(qi.CA_INTERNAL+qi.CA_VENDOR) as CA,\r\n" + 
						"                        SUM(qi.REWORK_INTERNAL + qi.REWORK_VENDOR) as REWORK_QTY, \r\n" + 
						"                        SUM(qi.REJECTED_INTERNAL+qi.REJECTED_EXTERNAL) as REJECT_QTY,\r\n" + 
						"                        hdr.PM_HDR_ID, hdr.PROJECT_CODE, PROJECT_NAME\r\n" + 
						"					FROM \r\n" + 
						"					    quality_inspection_request qir \r\n" + 
						"					LEFT JOIN \r\n" + 
						"					    quality_inspection_hdr qi ON qir.QI_ID = qi.QI_ID AND qi.IS_LATEST=1 \r\n" + 
						"					 INNER JOIN  po_dtl pdl ON qir.PO_DTL_ID = pdl.PO_DTL_ID   \r\n" + 
						"					 INNER JOIN  po_hdr phdr ON phdr.PO_ID = pdl.PO_ID and phdr.SEQUENCE_NO != 3  \r\n" + 
						"                     INNER JOIN project_hdr hdr ON qir.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
						"					WHERE \r\n" + 
						"					    qir.PM_HDR_ID LIKE '"+projId+"' AND qir.TENANT_ID = '"+tenantId+"' AND qir.IS_LATEST=1\r\n" + 
						"                        AND qir.INSPECTION_REQUESTED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' \r\n" + 
						"                        group by qir.PM_HDR_ID;";
			}else {
				 query ="SELECT \r\n" + 
						"    phdr.PROJECT_CODE,\r\n" + 
						"    phdr.PROJECT_NAME,\r\n" + 
						"	vmst.VENDOR_NAME,\r\n" + 
						"    ABS(OK_QTY - (CA_INTERNAL + CA_VENDOR)) AS INSPECTION_OK,\r\n" + 
						"	qhdr.INSPECTION_QTY,\r\n" + 
						"    (CA_INTERNAL + CA_VENDOR) AS CA,\r\n" + 
						"    qhdr.QUALITY_RATING,\r\n" + 
						"    (REWORK_INTERNAL + REWORK_VENDOR) AS REWORK_QTY,\r\n" + 
						"    (REJECTED_EXTERNAL + REJECTED_INTERNAL) AS REJECT_QTY,\r\n" + 
						"	qhdr.INSPECTED_ON,\r\n" + 
						"    REWORK_INTERNAL,\r\n" + 
						"    REWORK_VENDOR,\r\n" + 
						"    CA_INTERNAL,\r\n" + 
						"    CA_VENDOR,\r\n" + 
						"    OK_QTY,\r\n" + 
						"    dtl.DESCRIPTION,\r\n" + 
						"    REJECTED_EXTERNAL,\r\n" + 
						"    REJECTED_INTERNAL,\r\n" + 
						"    qhdr.QI_HDR_ID, prod.PRODUCT_CODE,\r\n" + 
						"    prod.PRODUCT_DESCRIPTION, req.PO_CODE \r\n" + 
						"FROM\r\n" + 
						"    quality_inspection_request req\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    quality_inspection_hdr qhdr ON req.QI_ID = qhdr.QI_ID and req.IS_LATEST=1\r\n" + 
						"        INNER JOIN\r\n" + 
						"    quality_hdr hdr ON req.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    process_assigned_team pat ON hdr.Q_HDR_ID = pat.MASTER_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
						"            INNER JOIN\r\n" + 
						"    vendor_mst vmst ON qhdr.VENDOR_CODE = vmst.VENDOR_CODE\r\n" + 
						"    INNER JOIN\r\n" + 
						"    indent_dtl dtl ON dtl.INDENT_DTL_ID = req.INDENT_DTL_ID\r\n" +
					    "   INNER JOIN\r\n" + 
						"    product_mst prod ON dtl.PRODUCT_CODE = prod.PRODUCT_CODE AND hdr.PM_HDR_ID=prod.PM_HDR_ID \r\n" + 
//					    "      INNER JOIN  po_dtl po ON req.PO_DTL_ID = po.PO_DTL_ID INNER JOIN po_hdr pohdr ON pohdr.PO_ID = req.PO_ID " +
						"WHERE\r\n" + 
						"    pat.PM_ID = '"+pmID+"' and qhdr.IS_COMPLETED=1\r\n" + 
						"        AND hdr.PM_HDR_ID LIKE '"+projId+"'\r\n" + 
						"        AND hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
						"        AND pat.ASSIGNED_EMP_ID = '"+empID+"'\r\n" + 
						"        AND pat.IS_ACTIVE = '1'\r\n" + 
						"        AND req.INSPECTION_REQUESTED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' AND "+typeCodeCol+";";
			}
			list = this.jdbcTemplate.query(query, new DrilldownDtlRespRowMapper());
		}catch(Exception ex) {
			logger.error("getDrilldownDtlResp DAO Error" + ex);	
		}
		return list;
	}
	
	@Override
	public List<SupplierRatingEntity> SupplierRatingDAO(String projectId,String tENANTID,
			String empID, String pmID, String fromDate, String toDate) {
		List<SupplierRatingEntity> list = new ArrayList<SupplierRatingEntity>();
		try {
			String query = "SELECT \r\n" + 
					"    SUM((CA_INTERNAL + CA_VENDOR)) AS CA,\r\n" + 
					"    SUM(ABS(OK_QTY - (CA_INTERNAL + CA_VENDOR))) AS INSPECTION_OK,\r\n" + 
					"    SUM(REJECTED_EXTERNAL + REJECTED_INTERNAL) AS REJECT_QTY,\r\n" + 
					"    AVG(QUALITY_RATING) AS QUALITY_RATING ,\r\n" + 
					"    SUM(REWORK_INTERNAL + REWORK_VENDOR) AS REWORK_QTY,\r\n" + 
					"    vm.VENDOR_NAME,vm.VENDOR_CODE,\r\n" + 
					"    SUM(qhdr.OK_QTY) AS OK_QTY,AVG(INWARD_RATING) AS INWARD_RATING,AVG(SUPPLIER_RATING) AS SUPPLIER_RATING\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_hdr qhdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    vendor_mst vm ON qhdr.VENDOR_CODE = vm.VENDOR_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    quality_hdr hdr ON hdr.PM_HDR_ID = qhdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.Q_HDR_ID = pteam.MASTER_ID\r\n" + 
					"WHERE\r\n" + 
					"    qhdr.PM_HDR_ID LIKE '"+projectId+"'\r\n" + 
					"        AND qhdr.TENANT_ID = '"+tENANTID+"'\r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = '"+empID+"'\r\n" + 
					"        AND PM_ID = '"+pmID+"'  AND pteam.IS_ACTIVE=1\r\n" + 
					"        AND qhdr.INSPECTED_ON BETWEEN '"+fromDate+"' AND '"+toDate+"'AND qhdr.NR_FLAG = 0 \r\n" + 
					"GROUP BY vm.VENDOR_CODE;";
			list = this.jdbcTemplate.query(query, new SupplierRatingRowMapper());
		}catch(Exception ex) {
			logger.error("SupplierRatingDAO Error" + ex);
		}	
		return list;	
	}
	
	@Override
	public String SupplierSCMAndInwardRatingDAO(String projectId,String tENANTID,
			String empID, String pmID, String fromDate, String toDate,String vendorCode,String column) {
		String response ="";
		try {
			String query = "SELECT   \n"
					+ "					case when count(qhdr.QI_HDR_ID)>0 then	avg(mih."+column+") else 0 end AS val \n"
					+ "					FROM  \n"
					+ "					    quality_inspection_hdr qhdr  \n"
					+ "					        INNER JOIN  \n"
					+ "					    vendor_mst vm ON qhdr.VENDOR_CODE = vm.VENDOR_CODE  \n"
					+ "					        INNER JOIN  \n"
					+ "					    quality_hdr hdr ON hdr.PM_HDR_ID = qhdr.PM_HDR_ID  \n"
					+ "					        INNER JOIN  \n"
					+ "					    process_assigned_team pteam ON hdr.Q_HDR_ID = pteam.MASTER_ID\n"
					+ "                         inner JOIN\n"
					+ "						quality_inspection_request qir ON qhdr.QI_ID = qir.QI_ID\n"
					+ "						inner JOIN\n"
					+ "						material_inward_dtl mid ON mid.MI_DTL_ID = qir.MI_DTL_ID\n"
					+ "						inner JOIN\n"
					+ "						material_inward_hdr mih ON mih.MI_ID = mid.MI_ID\n"
					+ "					WHERE  \n"
					+ "					    qhdr.PM_HDR_ID LIKE ? \n"
					+ "					        AND qhdr.TENANT_ID = ?  \n"
					+ "					        AND pteam.ASSIGNED_EMP_ID = ?\n"
					+ "					        AND PM_ID = ?  AND pteam.IS_ACTIVE=1  \n"
					+ "					        AND qhdr.INSPECTED_ON BETWEEN ? AND ? and vm.VENDOR_CODE = ? ";
			 Map<String, Object> resultMap = jdbcTemplate.queryForMap(query, projectId, tENANTID, empID, pmID, fromDate, toDate, vendorCode);
			 response = resultMap.get("val").toString();
		}catch(Exception ex) {
			logger.error("SupplierSCMAndInwardRatingDAO Error" + ex);
		}	
		return response;	
	}

	@Override
	public List<SupplierRatingEntity> SupplierRatingDAO1(String projectId, String tENANTID, String empID, String pmID,
			String fromDate, String toDate) {
		List<SupplierRatingEntity> list = new ArrayList<SupplierRatingEntity>();
		int cnt = 0;
		
		try {
			String ChkQry ="select count(PM_HDR_ID) as PM_HDR_ID from project_hdr where PM_HDR_ID like ? "
				     + " AND TENANT_ID = ?;";
		
		      Map<String,Object> resultMap = jdbcTemplate.queryForMap(ChkQry, projectId, tENANTID);
		      cnt = Integer.parseInt(resultMap.get("PM_HDR_ID").toString());
		
		    if(cnt > 0) {
			String query = "SELECT \r\n" + 
					"    SUM((CA_INTERNAL + CA_VENDOR)) AS CA,\r\n" + 
					"    SUM(ABS(OK_QTY - (CA_INTERNAL + CA_VENDOR))) AS INSPECTION_OK,\r\n" + 
					"    SUM(REJECTED_EXTERNAL + REJECTED_INTERNAL) AS REJECT_QTY,\r\n" + 
					"    AVG(QUALITY_RATING) as QUALITY_RATING,\r\n" + 
					"    SUM(REWORK_INTERNAL + REWORK_VENDOR) AS REWORK_QTY,\r\n" + 
					"    vm.VENDOR_NAME,\r\n" + 
					"    SUM(qhdr.OK_QTY) as OK_QTY\r\n" + 
					"FROM\r\n" + 
					"    quality_inspection_hdr qhdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    vendor_mst vm ON qhdr.VENDOR_CODE = vm.VENDOR_CODE\r\n" + 
					"WHERE\r\n" + 
					"    qhdr.PM_HDR_ID IN (SELECT \r\n" + 
					"            MASTER_ID\r\n" + 
					"        FROM\r\n" + 
					"            process_assigned_team pteam\r\n" + 
					"                INNER JOIN\r\n" + 
					"            quality_hdr hdr ON hdr.Q_HDR_ID = pteam.MASTER_ID\r\n" + 
					"        WHERE\r\n" + 
					"            PM_ID = '"+pmID+"' AND IS_ACTIVE = 1\r\n" + 
					"                AND ASSIGNED_EMP_ID = '"+empID+"')\r\n" + 
					"        AND qhdr.TENANT_ID = '"+tENANTID+"'\r\n" + 
					"        AND qhdr.INSPECTED_ON BETWEEN '"+fromDate+"' AND '"+toDate+"'\r\n" + 
					"GROUP BY vm.VENDOR_CODE;";
			list = this.jdbcTemplate.query(query, new SupplierRatingRowMapper());
		}
		}catch(Exception ex) {
			logger.error("SupplierRatingDAO1 Error" + ex);
		}	
		return list;	
	}
	
	@Override
	public List<TeamMemberLoadEntity> TeamMemberLoadDAO(String projId, String empId, String tenantId,
			String pmID, String fromDate, String toDate) {
		List<TeamMemberLoadEntity> list = new ArrayList<TeamMemberLoadEntity>();
          try { 
        	  String query = "SELECT \r\n" + 
        	  		"    COUNT(QI_ID) AS INSPECTION_CALL, DATE_FORMAT(req.INSPECTION_REQUESTED_DATE, '%Y-%m') AS INSPECTED_ON\r\n" + 
        	  		"FROM\r\n" + 
        	  		"    quality_inspection_request req\r\n" + 
        	  		"        INNER JOIN\r\n" + 
        	  		"    quality_hdr hdr ON req.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
        	  		"        INNER JOIN\r\n" + 
        	  		"    process_assigned_team pat ON hdr.Q_HDR_ID = pat.MASTER_ID\r\n" + 
        	  		"WHERE\r\n" + 
        	  		"    pat.PM_ID = '"+pmID+"' AND hdr.PM_HDR_ID LIKE '"+projId+"'\r\n" + 
        	  		"        AND hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
        	  		"        AND pat.ASSIGNED_EMP_ID LIKE '"+empId+"'\r\n" + 
        	  		"        AND req.INSPECTION_REQUESTED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'\r\n" + 
        	  		"        GROUP BY month(req.INSPECTION_REQUESTED_DATE),year(req.INSPECTION_REQUESTED_DATE);";
        	  
  			list = this.jdbcTemplate.query(query, new TeamMemberLoadRowMapper());
          }catch(Exception ex){
  			logger.error("TeamMemberLoadDAO Error" + ex);
          }
		return list;
	}
	
	@Override
	public String TeamMemberLoadQty(String projId, String empId, String tenantId, String pmID,
			String[] monthYr, String teamMemberId,String fromDate,String toDate) {
		String Qty="";
        try { 
        	String finalFromDate="";
        	String finalTodate ="";
        	if((monthYr[0]+"-"+ monthYr[1]).equalsIgnoreCase((fromDate.split("-")[0] +"-"+(fromDate.split("-")[1])))){
        		finalFromDate = fromDate;	
        	}else {
        		finalFromDate = monthYr[0]+"-"+ monthYr[1]+"-01";
        	}
        	if((monthYr[0]+"-"+ monthYr[1]).equalsIgnoreCase((toDate.split("-")[0] +"-"+(toDate.split("-")[1])))){
        		finalTodate = toDate;
        	}else {
        		finalTodate = monthYr[0]+"-"+ monthYr[1]+"-31";
        	}
        	String queryCount = "SELECT  count(*) AS COUNT \r\n" + 
          	  		"FROM\r\n" + 
          	  		"    quality_inspection_request req\r\n" + 
          	  		"        INNER JOIN\r\n" + 
          	  		"    quality_hdr hdr ON req.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
          	  		"        INNER JOIN\r\n" + 
          	  		"    process_assigned_team pat ON hdr.Q_HDR_ID = pat.MASTER_ID\r\n" + 
          	  		"        INNER JOIN\r\n" + 
          	  		"    quality_inspection_hdr qc ON req.QI_ID = qc.QI_ID\r\n" + 
          	  		"WHERE\r\n" + 
          	  		"    pat.PM_ID = ?\r\n" + 
          	  		"        AND hdr.PM_HDR_ID LIKE ?\r\n" + 
          	  		"        AND hdr.TENANT_ID = ?\r\n" + 
          	  		"        AND pat.ASSIGNED_EMP_ID = ?\r\n" + 
          	  		"        AND qc.INSPECTED_BY LIKE ? AND req.INSPECTION_REQUESTED_DATE BETWEEN  ? AND ? \r\n" + 
          	  		"     #   AND MONTH(req.INSPECTION_REQUESTED_DATE) = ?\r\n" + 
          	  		"      #  AND YEAR(req.INSPECTION_REQUESTED_DATE) = ?\r\n" + 
          	  		"        AND qc.IS_COMPLETED = 1";
          	  Map<String, Object> resultMap = jdbcTemplate.queryForMap(queryCount, pmID, projId, tenantId, empId, teamMemberId, finalFromDate, finalTodate);
          	  int qtyCount = Integer.parseInt(resultMap.get("COUNT").toString());
        	
//      	  String query = "SELECT \r\n" + 
//      	  		"    CASE\r\n" + 
//      	  		"        WHEN SUM(qc.INSPECTION_QTY) > 0 THEN SUM(qc.INSPECTION_QTY)\r\n" + 
//      	  		"        ELSE 0\r\n" + 
//      	  		"    END AS INSPECTION_QTY\r\n" + 
//      	  		"FROM\r\n" + 
//      	  		"    quality_inspection_request req\r\n" + 
//      	  		"        INNER JOIN\r\n" + 
//      	  		"    quality_hdr hdr ON req.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
//      	  		"        INNER JOIN\r\n" + 
//      	  		"    process_assigned_team pat ON hdr.Q_HDR_ID = pat.MASTER_ID\r\n" + 
//      	  		"        INNER JOIN\r\n" + 
//      	  		"    quality_inspection_hdr qc ON req.QI_ID = qc.QI_ID\r\n" + 
//      	  		"WHERE\r\n" + 
//      	  		"    pat.PM_ID = ?\r\n" + 
//      	  		"        AND hdr.PM_HDR_ID LIKE ?\r\n" + 
//      	  		"        AND hdr.TENANT_ID = ?\r\n" + 
//      	  		"        AND pat.ASSIGNED_EMP_ID = ?\r\n" + 
//      	  		"        AND qc.INSPECTED_BY LIKE ?\r\n" + 
//      	  		"        AND MONTH(req.INSPECTION_REQUESTED_DATE) = ?\r\n" + 
//      	  		"        AND YEAR(req.INSPECTION_REQUESTED_DATE) = ?\r\n" + 
//      	  		"        AND qc.IS_COMPLETED = 1\r\n" + 
//      	  		"        GROUP BY month(req.INSPECTION_REQUESTED_DATE),year(req.INSPECTION_REQUESTED_DATE);";
//      	 if(qtyCount>0) {
//      		Map<String, Object> resultData = jdbcTemplate.queryForMap(query, pmID, projId, tenantId, empId, teamMemberId, monthYr[1], monthYr[0]);
//        	  Qty = resultData.get("INSPECTION_QTY").toString();
//      	 }else {
//      		Qty = "0"; 
//      	 }
      	 Qty =Integer.toString(qtyCount);
        }catch(Exception ex){
			logger.error("TeamMemberLoadQty Error" + ex);
        }
		return Qty ;
	}

	@Override
	public List<VendorTypeCateCountEntity> getVendorByType(TenantRequest tenantId) {
		List<VendorTypeCateCountEntity> list = new ArrayList<VendorTypeCateCountEntity>();
		try {
			String qry ="select count(*) as count,VENDOR_TYPE as description from vendor_mst where TENANT_ID=? group by VENDOR_TYPE";
			list = this.jdbcTemplate.query(qry, new VendorTypeCatRowMapper(),tenantId.getTenantID());
		}catch(Exception ex) {
			logger.error("getVendorByType Exception" + ex);
		}
		return list;
	}

	@Override
	public List<VendorTypeCateCountEntity> getVendorByCategory(TenantRequest tenantId) {
		List<VendorTypeCateCountEntity> list = new ArrayList<VendorTypeCateCountEntity>();
		try {
			String qry ="select count(*) as count,VENDOR_CATEGORY as description from vendor_mst where TENANT_ID=? group by VENDOR_CATEGORY";
			list = this.jdbcTemplate.query(qry, new VendorTypeCatRowMapper(),tenantId.getTenantID());
		}catch(Exception ex) {
			logger.error("getVendorByCategory Exception" + ex);
		}
		return list;
	}

}
