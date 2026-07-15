package com.vmfg.mis.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.inventory.entity.InvProdDrillEntity;
import com.vmfg.inventory.entity.NoOfPoDrillEntity;
import com.vmfg.mis.dao.interfaces.IDrillDownDAO;
import com.vmfg.mis.entity.DelayedEntity;
import com.vmfg.mis.entity.DrilldownEntity;
import com.vmfg.mis.entity.InitialScopValuePOEntity;
import com.vmfg.mis.rowmapper.DelayedEntityRowMapper;
import com.vmfg.mis.rowmapper.DrilldownEntityRowMapper;
import com.vmfg.mis.rowmapper.InitialScopePoValueEntityRowMapper;
import com.vmfg.mis.rowmapper.InvProdDrillEntityRowMapper;
import com.vmfg.mis.rowmapper.NoOfPoDrillEntityRowMapper;


@Transactional
@Repository
public class DrillDownDAO implements IDrillDownDAO {
	private static final Logger logger = LoggerFactory.getLogger(DrillDownDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<DrilldownEntity> getIndentByNotAvailablePOList(String tenantId, String projectId, String pmId, String empId,String concatQry) {
		List<DrilldownEntity> list = new ArrayList<DrilldownEntity>();
		
		try {
		
			String qry = "SELECT \r\n" + 
				"    phdr.PROJECT_NAME, phdr.PM_HDR_ID,\r\n" + 
				"    phdr.PROJECT_DESCRIPTION, phdr.PROJECT_CODE,\r\n" + 
				"    phdr.CUSTOMER_NAME, sbc.SBC_DESC as INDENT_TYPE_DESC, inhdr.INDENT_TYPE_CODE, \r\n" + 
				"    inhdr.INDENT_CODE, #mst.EMPLOYEE_FIRSTNAME,\r\n" + 
				"    inhdr.EXPECTED_DELIVERY_DATE, indtl.INDENT_DTL_ID,\r\n" + 
				"    indtl.PRODUCT_CODE,\r\n" + 
				"    indtl.DESCRIPTION,\r\n" + 
				"    indtl.SPECIFICATION,\r\n" + 
				"    indtl.MAKE, indtl.QTY,  PSK_DESC, PK_DESC\r\n" + 
				"FROM\r\n" + 
				"    indent_dtl indtl\r\n" + 
				"        INNER JOIN\r\n" + 
				"    indent_hdr inhdr ON indtl.INDENT_ID = inhdr.INDENT_ID\r\n" + 
				"        INNER JOIN\r\n" + 
				"	indent_type_mst itm ON inhdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" +
				"        INNER JOIN\r\n" + 
//				"   indent_assign_team iat ON indtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
//				"		 INNER JOIN  \r\n" + 
//				"	employee_mst mst ON mst.EMPLOYEE_ID = iat.EMPLOYEE_ID\r\n" + 
//				"        INNER JOIN\r\n" +
				"    scm_hdr sc ON sc.PM_HDR_ID = inhdr.PROJECT_ID\r\n" + 
				"        INNER JOIN\r\n" + 
				"    project_hdr phdr ON phdr.PM_HDR_ID = sc.PM_HDR_ID\r\n" + 
				"        INNER JOIN\r\n" + 
				"    process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID\r\n" + 
				"        INNER JOIN \r\n" + 
				"	 sales_budget_category sbc ON sbc.SBC_CODE = inhdr.SBC_CODE\r\n" +
				"        INNER JOIN \r\n" +
			    "    project_key_area pka ON inhdr.PKA_ID = pka.PKA_ID\r\n" +
				"        INNER JOIN\r\n" + "    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" +
			    "        INNER JOIN\r\n" + "    project_key_sub_area pksa ON pksa.PKSA_ID = inhdr.PKSA_ID\r\n" +
				"        INNER JOIN\r\n" + "    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n" +
				"WHERE\r\n" + 
				"    inhdr.IS_COMPLETED = 0\r\n" + 
				"        AND team.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
//				"        AND iat.EMPLOYEE_ID = '"+empId+"' " +
				"        AND team.IS_ACTIVE = '1'\r\n" + 
				"        AND team.PM_ID = '"+pmId+"'\r\n" + 
				"        AND inhdr.PROJECT_ID LIKE ('"+projectId+"')\r\n" + 
				"        AND inhdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077') \r\n" +
				"\r\n "+concatQry+"\r\n" + 
				"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
				"            podtl.INDENT_DTL_ID\r\n" + 
				"        FROM\r\n" + 
				"            po_hdr pohdr\r\n" + 
				"                INNER JOIN\r\n" + 
				"            po_dtl podtl ON podtl.PO_ID = pohdr.PO_ID\r\n" + 
				"        WHERE\r\n" + 
				"            pohdr.IS_APPROVED = 1\r\n" + 
				"                AND pohdr.IS_LATEST = 1)\r\n" + 
				"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
				"            indtl.INDENT_DTL_ID\r\n" + 
				"        FROM\r\n" + 
				"            indent_grp_dtl indtl\r\n" + 
				"                INNER JOIN\r\n" + 
				"            indent_grp_hdr inhdr ON indtl.IG_HDR_ID = inhdr.IG_HDR_ID\r\n" + 
				"        WHERE\r\n" + 
				"            inhdr.IS_INVENTORY = '1') GROUP BY indtl.INDENT_DTL_ID";
		list = this.jdbcTemplate.query(qry, new DrilldownEntityRowMapper());
//		ADD
		}catch(Exception ex) {
			logger.error("getIndentByNotAvailablePOList Method Exception "+ex);
		}
		return list;
	}

	@Override
	public List<DrilldownEntity> getIndentByInventoryStock(String tenantId, String projectId, String pmId, String empId,
			String monthYear) {
		List<DrilldownEntity> list = new ArrayList<DrilldownEntity>();
		
		try {
		
			String qry = "SELECT DISTINCT\r\n" + 
					"    phdr.PROJECT_NAME,\r\n" + 
					"    phdr.PM_HDR_ID,\r\n" + 
					"    phdr.PROJECT_DESCRIPTION,\r\n" + 
					"    phdr.PROJECT_CODE,\r\n" + 
					"    phdr.CUSTOMER_NAME,\r\n" + 
					"    sbc.SBC_DESC AS INDENT_TYPE_DESC,\r\n" + 
					"    inhdr.INDENT_TYPE_CODE,\r\n" + 
					"    inhdr.INDENT_CODE,\r\n" + 
					"    mst.EMPLOYEE_FIRSTNAME,\r\n" + 
					"    inhdr.EXPECTED_DELIVERY_DATE,\r\n" + 
					"    indtl.INDENT_DTL_ID,\r\n" + 
					"    indtl.PRODUCT_CODE,\r\n" + 
					"    indtl.DESCRIPTION,\r\n" + 
					"    indtl.SPECIFICATION,\r\n" + 
					"    indtl.MAKE,\r\n" + 
					"    indtl.QTY,\r\n" + 
					"    PSK_DESC,\r\n" + 
					"    PK_DESC, TYPE\r\n" + 
					"FROM\r\n" + 
					"    indent_dtl indtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr inhdr ON indtl.INDENT_ID = inhdr.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_type_mst itm ON inhdr.INDENT_TYPE_CODE = itm.INDENT_TYPE_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_assign_team iat ON indtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst mst ON mst.EMPLOYEE_ID = iat.EMPLOYEE_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    scm_hdr sc ON sc.PM_HDR_ID = inhdr.PROJECT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr phdr ON phdr.PM_HDR_ID = sc.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    sales_budget_category sbc ON sbc.SBC_CODE = inhdr.SBC_CODE\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area pka ON inhdr.PKA_ID = pka.PKA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area pksa ON pksa.PKSA_ID = inhdr.PKSA_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_dtl dtl ON indtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_hdr hdr ON dtl.IG_HDR_ID = hdr.IG_HDR_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"	indent_grp_scs scs ON hdr.IG_HDR_ID = scs.IG_HDR_ID \r\n" +
					"WHERE\r\n" + 
					"    hdr.IS_INVENTORY = '1' #AND inhdr.IS_COMPLETED=0 \r\n" + 
					"        AND hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND inhdr.PROJECT_ID LIKE '"+projectId+"'\r\n" + 
					"        AND team.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
					"        AND iat.EMPLOYEE_ID = '"+empId+"'\r\n" + 
					"        AND team.IS_ACTIVE = '1' "+monthYear+"\r\n" + 
					"        AND team.PM_ID = '"+pmId+"';";
		list = this.jdbcTemplate.query(qry, new DrilldownEntityRowMapper());
		}catch(Exception ex) {
			logger.error("getIndentByInventoryStock Method Exception "+ex);
		}
		return list;
	}

	@Override
	public List<DelayedEntity> getDelayedIndentList(String tenantId, String projectId, String pmId, String empId,String concatQry) {
		List<DelayedEntity> list = new ArrayList<DelayedEntity>();
		try {

			String qry = "SELECT \r\n" + 
					"    indtl.INDENT_DTL_ID,\r\n" + 
					"    indtl.DESCRIPTION,\r\n" + 
					"    indtl.MAKE,\r\n" + 
					"    indtl.MATERIAL,\r\n" + 
					"    indtl.PRODUCT_CODE,\r\n" + 
					"    indtl.QTY,\r\n" + 
					"    indtl.REMARKS,\r\n" + 
					"    indtl.SPECIFICATION,\r\n" + 
					"    indtl.UNIT,\r\n" + 
					"    indtl.WEIGHT,\r\n" + 
					"    REVISION_NO,\r\n" + 
					"    REVISION_DATE,\r\n" + 
					"    INDENT_CODE,\r\n" + 
					"    inhdr.INDENT_TYPE_CODE,\r\n" + 
					"    inhdr.CREATED_DATETIME,\r\n" + 
					"    CREATED_BY,\r\n" + 
					"    SEQUENCE_N0,\r\n" + 
					"    EXPECTED_DELIVERY_DATE,\r\n" + 
					"    phdr.CUSTOMER_NAME,\r\n" + 
					"    phdr.PROJECT_NAME,phdr.PROJECT_CODE,\r\n" + 
					"    phdr.PROJECT_DESCRIPTION,\r\n" + 
					"    intype.INDENT_TYPE_DESC,\r\n" + 
//					"    mst.EMPLOYEE_FIRSTNAME, " +
					"  PSK_DESC, PK_DESC\r\n" + 
					"FROM\r\n" + 
					"    indent_dtl indtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr inhdr ON indtl.INDENT_ID = inhdr.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    scm_hdr sc ON sc.PM_HDR_ID = inhdr.PROJECT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr phdr ON phdr.PM_HDR_ID = sc.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_type_mst intype ON intype.INDENT_TYPE_CODE = inhdr.INDENT_TYPE_CODE\r\n" + 
//					"        INNER JOIN \r\n" +
//					"    indent_assign_team iat ON iat.INDENT_DTL_ID = indtl.INDENT_DTL_ID \r\n" +
//					"        INNER JOIN\r\n" + 
//					"    employee_mst mst ON iat.EMPLOYEE_ID = mst.EMPLOYEE_ID\r\n" + 
					"        INNER JOIN \r\n" +
				    "    project_key_area pka ON inhdr.PKA_ID = pka.PKA_ID\r\n" +
					"        INNER JOIN\r\n" + "    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" +
				    "        INNER JOIN\r\n" + "    project_key_sub_area pksa ON pksa.PKSA_ID = inhdr.PKSA_ID\r\n" +
					"        INNER JOIN\r\n" + "    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n" +
					"WHERE\r\n" + 
					"    inhdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND CURRENT_DATE() > EXPECTED_DELIVERY_DATE\r\n" + 
					"        AND inhdr.IS_COMPLETED = 0\r\n" + 
					"        AND team.ASSIGNED_EMP_ID = '"+empId+"' #and iat.EMPLOYEE_ID = '"+empId+"' \r\n" + 
					"        AND team.IS_ACTIVE = '1'\r\n" + 
					"        AND team.PM_ID = '"+pmId+"'\r\n" + 
					"        AND inhdr.PROJECT_ID LIKE ('"+projectId+"')\r\n" + 
					"        AND inhdr.SEQUENCE_N0 > 6\r\n "+concatQry+" " + 
					"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
					"            podtl.INDENT_DTL_ID\r\n" + 
					"        FROM\r\n" + 
					"            po_hdr pohdr\r\n" + 
					"                INNER JOIN\r\n" + 
					"            po_dtl podtl ON podtl.PO_ID = pohdr.PO_ID\r\n" + 
					"        WHERE\r\n" + 
					"            pohdr.IS_APPROVED = 1\r\n" + 
					"                AND pohdr.IS_LATEST = 1)\r\n" + 
					"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
					"            indtl.INDENT_DTL_ID\r\n" + 
					"        FROM\r\n" + 
					"            indent_grp_dtl indtl\r\n" + 
					"                INNER JOIN\r\n" + 
					"            indent_grp_hdr inhdr ON indtl.IG_HDR_ID = inhdr.IG_HDR_ID\r\n" + 
					"        WHERE\r\n" + 
					"            inhdr.IS_INVENTORY = '1') GROUP BY indtl.INDENT_DTL_ID";
			list = this.jdbcTemplate.query(qry, new DelayedEntityRowMapper());
			
			
		}catch(Exception ex) {
			logger.error("getDelayedIndentList Method ex"+ex);
		}
		return list;
	}

	@Override
	public List<InitialScopValuePOEntity> getPoInitalValueList(String pmId, String tenantId,String concatQry) {
		List<InitialScopValuePOEntity> list = new ArrayList<InitialScopValuePOEntity>();
		try {
			String qry = "SELECT \r\n" + 
					"    PROJECT_CODE,INDENT_CODE,PO_CODE, PO_TYPE,DELIVERY_NAME, VENDOR_NAME, TOTAL_VALUE, UNIT_INI_BASIC_TOTAL, EXTN_INI_BASIC_TOTAL,UNIT_INI_BASIC_TOTAL-EXTN_INI_BASIC_TOTAL as diffrence,hdr.DATE, \r\n" +
				    "    PSK_DESC, PK_DESC, sbc.SBC_DESC as INDENT_TYPE_DESC\r\n" + 
					"FROM\r\n" + 
					"    po_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr ind ON ind.INDENT_ID = hdr.INDENT_ID\r\n" + 
					"    inner join project_hdr phdr on ind.PROJECT_ID = phdr.PM_HDR_ID \r\n" +
					"        INNER JOIN \r\n" +
				    "    project_key_area pka ON ind.PKA_ID = pka.PKA_ID\r\n" +
					"        INNER JOIN\r\n" + "    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" +
				    "        INNER JOIN\r\n" + "    project_key_sub_area pksa ON pksa.PKSA_ID = ind.PKSA_ID\r\n" +
					"        INNER JOIN\r\n" + "    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n" +
					"        INNER JOIN  \r\n" + 
					"	sales_budget_category sbc ON sbc.SBC_CODE = ind.SBC_CODE \r\n" +
					"WHERE\r\n" + 
					"    ind.PROJECT_ID LIKE ('"+pmId+"')\r\n" + 
					"        AND hdr.IS_APPROVED = '1'\r\n" + 
					"        AND hdr.IS_LATEST = '1'\r\n" + 
					"        AND hdr.TENANT_ID = '"+tenantId+"' "+concatQry+"";
			list = this.jdbcTemplate.query(qry, new InitialScopePoValueEntityRowMapper());

		} catch (Exception ex) {
			logger.error("getPoInitalValueList Method Exception " + ex);
		}
		return list;
	}

	@Override
	public List<InvProdDrillEntity> getInventoryValueDrillList(String pmId, String tenantId,String projectId) {
		List<InvProdDrillEntity> list = new ArrayList<InvProdDrillEntity>();
		try {
			String qry = "SELECT " +
					"    pm.PM_HDR_ID, " +
					"    pj.PROJECT_CODE, " +
					"    pm.PRODUCT_CODE, " +
					"    pm.PRODUCT_DESCRIPTION, " +
					"    pm.SPECIFICATION, pm.INWARD_DATETIME, " +
					"    pm.MAKE, " +
					"    um.UOM_SHORT_DESCRIPTION, " +
					"    pm.WEIGHT, " +
					"    ilm.INVENTORY_LOCATION_DESCRIPTION, " +
					"    CASE " +
					"        WHEN ilm.INVENTORY_LOCATION_CODE IN ('ILC0002','ILC0004') " +
					"        THEN pm.BIN ELSE '' END AS BIN, " +
					"    SUM(pdtl.PRODUCT_QUANTITY_ON_HAND) AS PRODUCT_QUANTITY_ON_HAND, " +
					"    pm.PRODUCT_COST_PER_UNIT, " +
					"    (SUM(pdtl.PRODUCT_QUANTITY_ON_HAND) * pm.PRODUCT_COST_PER_UNIT) AS INVENTORY_VALUE " +
					"FROM " +
					"    product_mst pm " +
					"    INNER JOIN inventory_product_dtl pdtl ON pm.PRODUCT_ID = pdtl.PRODUCT_ID " +
					"    INNER JOIN inventory_location_mst ilm ON ilm.INVENTORY_LOCATION_CODE = pdtl.INVENTORY_LOCATION_CODE " +
					"    left JOIN uom_mst um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE " +
					"    INNER JOIN project_hdr pj ON pm.PM_HDR_ID = pj.PM_HDR_ID " +
					"WHERE " +
					"    PRODUCT_QUANTITY_ON_HAND > 0 " +
					"    AND pm.TENANT_ID = '" + tenantId + "' " +
					"    AND pj.PM_HDR_ID LIKE ('" + projectId + "') AND ilm.INVENTORY_LOCATION_CODE IN ('ILC0002','ILC0004')" +
					"GROUP BY " +
					"    pm.PRODUCT_ID,"+
					"     pm.PM_HDR_ID " +
					"ORDER BY pj.PM_HDR_ID";
			list = this.jdbcTemplate.query(qry, new InvProdDrillEntityRowMapper());
		}catch(Exception ex) {
			logger.error("getInventoryValueDrillList Method Exception "+ex);
		}
		return list;
	}

	@Override
	public List<NoOfPoDrillEntity> getNumberOfPoDrillList(String projId, String tenantId, String empId, String pmId,
			String monthYear) {
		List<NoOfPoDrillEntity> list = new ArrayList<NoOfPoDrillEntity>();
		try {
			String qry = "SELECT \r\n" + 
					"    dtl.INDENT_DTL_ID,\r\n" + 
					"    dtl.DESCRIPTION,\r\n" + 
					"    dtl.INDENT_ID,\r\n" + 
					"    dtl.MAKE,\r\n" + 
					"    dtl.MATERIAL,\r\n" + 
					"    dtl.PRODUCT_CODE, hdr.PROJECT_ID,\r\n" +
				    "    poh.DATE, hdr.PROJECT_ID,\r\n" + 
				    "     (SELECT PROJECT_CODE FROM project_hdr WHERE \r\n" + 
				    "                        PM_HDR_ID = hdr.PROJECT_ID ) AS PROJECT_CODE,\r\n" +
					"    pod.QTY,\r\n" +
					"    dtl.REMARKS,\r\n" + 
					"    dtl.SPECIFICATION, sbc.SBC_DESC as INDENT_TYPE_DESC, PSK_DESC, PK_DESC,\r\n" + 
					"    dtl.UNIT,\r\n" + 
					"    dtl.WEIGHT,\r\n" + 
					"    pod.UNITE_RATE,\r\n" + 
					"    pod.TOTAL_VALUE,\r\n" + 
					"    hdr.INDENT_CODE, sbc.SBC_DESC as INDENT_TYPE_DESC,\r\n" + 
					"    pod.DELIVERY_DATE AS EXPECTED_DELIVERY_DATE,poh.PO_CODE,poh.VENDOR_NAME, mst.VENDOR_CATEGORY,\r\n" +
					"    (SELECT TRIM(CONCAT_WS(' ', em.EMPLOYEE_FIRSTNAME, NULLIF(TRIM(em.EMPLOYEE_LASTNAME),''))) \r\n" +
					"     FROM indent_grp_scs_status igss \r\n" +
					"     INNER JOIN document_status_type_code doc ON doc.DOCUMENT_STATUS_TYPE_CODE = igss.SEQUENCE_STATUS \r\n" +
					"     INNER JOIN employee_mst em ON em.EMPLOYEE_ID = igss.UPDATED_BY \r\n" +
					"     WHERE igss.IG_SCS_ID = poh.IG_SCS_ID \r\n" +
					"       AND doc.DOCUMENT_STATUS_TYPE_DESCRIPTION = 'SCM Verified' \r\n" +
					"     ORDER BY igss.UPDATED_ON DESC LIMIT 1) AS PJS_PO_CREATOR_NAME \r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
//					"    project_hdr proj ON proj.PM_HDR_ID = hdr.PROJECT_ID\r\n" + 
//					" Inner join \r\n"+
					"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_assign_team iat ON dtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_hdr poh ON hdr.INDENT_ID = poh.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_dtl pod ON dtl.INDENT_DTL_ID = pod.INDENT_DTL_ID\r\n" +
					"        AND poh.PO_ID = pod.PO_ID\r\n" + 
					"     left join vendor_mst mst on poh.VENDOR_CODE = mst.VENDOR_CODE \r\n" +
					"        INNER JOIN\r\n" + 
					"   scm_hdr AS sh ON sh.PM_HDR_ID = hdr.PROJECT_ID\r\n" + 
					"        INNER JOIN \r\n" + 
					"	process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID \r\n" +
					"        INNER JOIN  \r\n" + 
					"	sales_budget_category sbc ON sbc.SBC_CODE = hdr.SBC_CODE \r\n" +
					"        INNER JOIN \r\n" +
				    "    project_key_area pka ON hdr.PKA_ID = pka.PKA_ID\r\n" +
					"        INNER JOIN\r\n" + "    project_key_area_mst pk ON pka.PK_ID = pk.PK_ID\r\n" +
				    "        INNER JOIN\r\n" + "    project_key_sub_area pksa ON pksa.PKSA_ID = hdr.PKSA_ID\r\n" +
					"        INNER JOIN\r\n" + "    project_key_sub_area_mst psk ON pksa.PSK_ID = psk.PSK_ID\r\n" +
					"WHERE\r\n" + 
					"    poh.IS_LATEST = 1\r\n" + 
					"        AND poh.IS_APPROVED = 1\r\n" + 
					"       and  hdr.PROJECT_ID like '"+projId+"'\r\n"+
					"          AND team.ASSIGNED_EMP_ID = '"+empId+"' \r\n" + 
					"						        AND team.IS_ACTIVE = '1' \r\n" + 
					"						        AND team.PM_ID = '"+pmId+"'\r\n" +
					"        AND iat.EMPLOYEE_ID = '"+empId+"'# and IS_PRIMARY =1\r\n" + 
					"       # and hdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')\r\n" +
					"        AND hdr.TENANT_ID = '"+tenantId+"' "+monthYear+"  group by  pod.PO_DTL_ID";
			list = this.jdbcTemplate.query(qry, new NoOfPoDrillEntityRowMapper());
		}catch(Exception ex) {
			logger.error("getNumberOfPoDrillList Method Exception "+ex);
		}
		return list;
	}
/*
	@Override
	public String getPjsCheck(String indentDtlId, String tenantId) {
		String val="";	
		try {
			String Qry = "SELECT \r\n" + 
					"    COUNT(INDENT_DTL_ID) > 0 as COUNT\r\n" + 
					"FROM\r\n" + 
					"    indent_grp_scs scs\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_grp_scs_dtl dtl ON scs.IG_SCS_ID = dtl.IG_SCS_ID\r\n" + 
					"WHERE\r\n" + 
					"    INDENT_DTL_ID = ?\r\n" + 
					"        AND scs.TENANT_ID = ?";
			Map<String,Object> result = jdbcTemplate.queryForMap(Qry,indentDtlId,tenantId);
			int cnt = Integer.valueOf(result.get("COUNT").toString());
			if(cnt>0) {
			String qry = "select case when INDENT_DTL_ID is not null\r\n" + 
					"     then IS_APPROVED ELSE '' END as PJS_STAGE\r\n" + 
					"from indent_grp_scs scs \r\n" + 
					"     inner join indent_grp_scs_dtl dtl\r\n" + 
					"     on scs.IG_SCS_ID = dtl.IG_SCS_ID\r\n" + 
					"where INDENT_DTL_ID =? AND scs.TENANT_ID =?;";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
			val = resultMap.get("PJS_STAGE").toString();
			}
		}catch(Exception e) {
			logger.error("getPjsCheck Method Exception ");	
		}
		return val;
	}
*/
	
	
	public Map<String, String> getPjsCheck(List<String> indentDtlIds, String tenantId) {
	    Map<String, String> result = new HashMap<>();
	    if (indentDtlIds.isEmpty()) {
	        return result;
	    }
	    try {
	    String countSql = "SELECT COUNT(*) AS count " +
	                      "FROM indent_grp_scs scs " +
	                      "INNER JOIN indent_grp_scs_dtl dtl ON scs.IG_SCS_ID = dtl.IG_SCS_ID " +
	                      "WHERE dtl.INDENT_DTL_ID IN (" + String.join(",", Collections.nCopies(indentDtlIds.size(), "?")) + ") " +
	                      "AND scs.TENANT_ID = ?";
	    
	    List<Object> countParams = new ArrayList<>(indentDtlIds);
	    countParams.add(tenantId);
	    
	    int count = jdbcTemplate.queryForObject(countSql, Integer.class, countParams.toArray());

	    if (count > 0) {
	        String sql = "SELECT dtl.INDENT_DTL_ID, " +
	                     "       CASE WHEN dtl.INDENT_DTL_ID IS NOT NULL THEN scs.IS_APPROVED ELSE '' END AS PJS_STAGE " +
	                     "FROM indent_grp_scs scs " +
	                     "INNER JOIN indent_grp_scs_dtl dtl ON scs.IG_SCS_ID = dtl.IG_SCS_ID " +
	                     "WHERE dtl.INDENT_DTL_ID IN (" + String.join(",", Collections.nCopies(indentDtlIds.size(), "?")) + ") " +
	                     "AND scs.TENANT_ID = ?";

	        List<Object> params = new ArrayList<>(indentDtlIds);
	        params.add(tenantId);

	        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params.toArray());

	        for (Map<String, Object> row : rows) {
	            result.put(row.get("INDENT_DTL_ID").toString(), row.get("PJS_STAGE").toString());
	        }
	    } else {
	    	logger.error("No matching records found for the given indentDtlIds and tenantId.");
	    }
	}catch(Exception e) {
		logger.error("getPjsCheck Method Exception ");	
	}
	    return result;
	}



/*
	@Override
	public String getIndentCheck(String indentDtlId, String tenantId) {
		String val="";	
	try {
		String qry = "select case when INDENT_DTL_ID is not null \r\n" + 
				" then IS_COMPLETED ELSE '' END as INDENT_STAGE \r\n" + 
				" from indent_hdr hdr\r\n" + 
				" inner join indent_dtl dtl\r\n" + 
				" on hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
				" where INDENT_DTL_ID = ? AND hdr.TENANT_ID =?";
		Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
		val = resultMap.get("INDENT_STAGE").toString();
	 }catch(Exception ex) {
		   logger.error("getIndentCheck Method Exception "+ex);
	  }
	    return val;
	}
*/
	
	public Map<String, String> getIndentCheck(List<String> indentDtlIds, String tenantId) {
	    Map<String, String> result = new HashMap<>();
	    if (indentDtlIds.isEmpty()) {
	        return result;
	    }

	    String sql = "SELECT dtl.INDENT_DTL_ID, " +
	                 "       CASE WHEN dtl.INDENT_DTL_ID IS NOT NULL THEN hdr.IS_COMPLETED ELSE '' END AS INDENT_STAGE " +
	                 "FROM indent_hdr hdr " +
	                 "INNER JOIN indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID " +
	                 "WHERE dtl.INDENT_DTL_ID IN (" + String.join(",", Collections.nCopies(indentDtlIds.size(), "?")) + ") " +
	                 "AND hdr.TENANT_ID = ?";

	    List<Object> params = new ArrayList<>(indentDtlIds);
	    params.add(tenantId);

	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params.toArray());
	    for (Map<String, Object> row : rows) {
	        result.put(row.get("INDENT_DTL_ID").toString(), row.get("INDENT_STAGE").toString());
	    }

	    return result;
	}


	/*
	
	@Override
	public String getPoCheck(String indentDtlId, String tenantId) {
		String val="";	
	try {
		String qry = "select case when INDENT_DTL_ID is not null \r\n" + 
				" then IS_APPROVED ELSE '' END as PO_STAGE \r\n" + 
				" from po_hdr hdr \r\n" + 
				" inner join po_dtl dtl\r\n" + 
				" on hdr.PO_ID = dtl.PO_ID\r\n" + 
				" where INDENT_DTL_ID = ? AND hdr.TENANT_ID =?";
		Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,indentDtlId,tenantId);
		val = resultMap.get("PO_STAGE").toString();
	 }catch(Exception ex) {
		   logger.error("getIndentCheck Method Exception "+ex);
	  }
	    return val;
	}
*/
	
	
	public Map<String, String> getPoCheck(List<String> indentDtlIds, String tenantId) {
	    Map<String, String> result = new HashMap<>();
	    if (indentDtlIds.isEmpty()) {
	        return result;
	    }

	    String sql = "SELECT dtl.INDENT_DTL_ID, " +
	                 "       CASE WHEN dtl.INDENT_DTL_ID IS NOT NULL THEN hdr.IS_APPROVED ELSE '' END AS PO_STAGE " +
	                 "FROM po_hdr hdr " +
	                 "INNER JOIN po_dtl dtl ON hdr.PO_ID = dtl.PO_ID " +
	                 "WHERE dtl.INDENT_DTL_ID IN (" + String.join(",", Collections.nCopies(indentDtlIds.size(), "?")) + ") " +
	                 "AND hdr.TENANT_ID = ?";

	    List<Object> params = new ArrayList<>(indentDtlIds);
	    params.add(tenantId);

	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params.toArray());
	    for (Map<String, Object> row : rows) {
	        result.put(row.get("INDENT_DTL_ID").toString(), row.get("PO_STAGE").toString());
	    }

	    return result;
	}
	
	@Override
	public Map<String, String> getAssignedBy(List<String> indentDtlIds, String tenantId) {
	    Map<String, String> result = new HashMap<>();
	    if (indentDtlIds.isEmpty()) {
	        return result;
	    }

	    String sql = "    SELECT dtl.INDENT_DTL_ID AS INDENT_DTL_ID,\n" +
				"    (\n" +
				"        SELECT \n" +
				"            emp.EMPLOYEE_FIRSTNAME\n" +
				"        FROM\n" +
				"            employee_mst emp\n" +
				"        WHERE\n" +
				"            emp.EMPLOYEE_ID = iteam.EMPLOYEE_ID\n" +
				"    ) AS EMPLOYEE_FIRSTNAME\n" +
				"FROM\n" +
				"    indent_hdr hdr\n" +
				"INNER JOIN\n" +
				"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\n" +
				"LEFT JOIN\n" +
				"    indent_assign_team iteam ON dtl.INDENT_DTL_ID = iteam.INDENT_DTL_ID\n" +
				"    AND iteam.IS_PRIMARY = 0\n" +
	                 "WHERE dtl.INDENT_DTL_ID IN (" + String.join(",", Collections.nCopies(indentDtlIds.size(), "?")) + ") " +
	                 "AND hdr.TENANT_ID = ?";

	    List<Object> params = new ArrayList<>(indentDtlIds);
	    params.add(tenantId);

	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params.toArray());
	    for (Map<String, Object> row : rows) {
	    	 Object idObj = row.get("INDENT_DTL_ID");
	         if (idObj == null) {
	             continue;
	         }
	         String id = idObj.toString();

	         Object empObj = row.get("EMPLOYEE_FIRSTNAME");
	         String empName = (empObj == null) ? "" : empObj.toString();

	         result.put(id, empName);
	    }

	    return result;
	}

	@Override
	public String getPrimaryDocFlagVal(String empId, String pmId, String tenantId) {
		String flagVal = "";
		try {
			String qry = "SELECT \r\n" + 
					"    CASE WHEN COUNT(*) > 0 THEN '' ELSE \r\n" +
					"  (SELECT em.EMPLOYEE_FIRSTNAME \r\n" + 
					"              FROM employee_mst em \r\n" + 
					"              WHERE em.EMPLOYEE_ID = ?\r\n" + 
					"             ) \r\n" +
				    " END AS VALUE\r\n" + 
					"FROM project_wbs_initiation_mst pwm\r\n" + 
					"WHERE FIND_IN_SET(\r\n" + 
					"            (SELECT em.DESIGNATION_CODE \r\n" + 
					"             FROM employee_mst em \r\n" + 
					"             WHERE em.EMPLOYEE_ID = ?\r\n" + 
					"            ),\r\n" + 
					"            pwm.MASTER_POC\r\n" + 
					"      )\r\n" + 
					"AND pwm.PM_ID = ?\r\n" + 
					"AND pwm.TENANT_ID = ?;";
			Map<String, Object> result = this.jdbcTemplate.queryForMap(qry, empId, empId, pmId , tenantId);
			flagVal = result.get("VALUE").toString();

		} catch (Exception ex) {
			logger.error("getPrimaryDocFlagVal method Error" + ex);
		}
		return flagVal;
	}
}
