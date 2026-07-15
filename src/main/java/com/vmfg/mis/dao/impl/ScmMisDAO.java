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

import com.vmfg.inventory.entity.InvProdEntity;
import com.vmfg.inventory.rowmapper.InvProdRowMapper;
import com.vmfg.mis.dao.interfaces.IScmMisDAO;
import com.vmfg.mis.entity.ScmEmployeeIndentDtlsEntity;
import com.vmfg.mis.entity.VendorDetailDrillDownEntity;
import com.vmfg.mis.rowmapper.GetVendorDetailDrillDownRowMapper;
import com.vmfg.mis.rowmapper.ScmEmployeeIndentRowmapper;
@Transactional
@Repository
public class ScmMisDAO implements IScmMisDAO{

	private static final Logger logger = LoggerFactory.getLogger(ScmMisDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String indentCount(String projectId,String pmId,String empId,String month, String year,String lifeSpan, String tenantId) {
		String list="";
		try {
			if(lifeSpan.equalsIgnoreCase("0")) {
			String qry="SELECT \n"
					+ "    Count(*) as Count\n"
					+ "FROM\n"
					+ "    indent_hdr hdr,\n"
					+ "     scm_hdr shdr,project_hdr phdr,\n"
					+ "    process_assigned_team pat\n"
					+ "WHERE\n"
					+ "    hdr.PROJECT_ID = phdr.PM_HDR_ID\n"
					+ "       AND phdr.PM_HDR_ID = shdr.PM_HDR_ID and shdr.SCM_HDR_ID = pat.MASTER_ID\n"
					+ "        AND pat.ASSIGNED_EMP_ID = ?\n"
					+ "        AND pat.PM_ID = ?\n"
					+ "        AND pat.IS_ACTIVE = 1\n"
					+ "        AND hdr.project_ID like ? "
				    + "        and month(hdr.CREATED_DATE)=? and year(hdr.CREATED_DATE)=? and pat.TENANT_ID=?";
			    Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,projectId,month,year,tenantId);
			    list = resultMap.get("Count").toString();
			
			}else {
				String qry="SELECT \n"
						+ "    Count(*) as Count\n"
						+ "FROM\n"
						+ "    indent_hdr hdr,\n"
						+ "     scm_hdr shdr,project_hdr phdr,\n"
						+ "    process_assigned_team pat\n"
						+ "WHERE\n"
						+ "    hdr.PROJECT_ID = phdr.PM_HDR_ID\n"
						+ "       AND phdr.PM_HDR_ID = shdr.PM_HDR_ID and shdr.SCM_HDR_ID = pat.MASTER_ID\n"
						+ "        AND pat.ASSIGNED_EMP_ID = ?\n"
						+ "        AND pat.PM_ID = ?\n"
						+ "        AND pat.IS_ACTIVE = 1\n"
						+ "        AND hdr.project_ID like ? ";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,projectId);
				list = resultMap.get("Count").toString();
			}
			
			
		} catch (Exception ex) {
			logger.error("indentCount  method exception-->" + ex);
		}
		logger.debug("indentCount  method end");
		return list;
	}

	@Override
	public String indentCompleted(String projectId,String pmId,String empId,String month, String year,String lifeSpan, String tenantId) {
		String indentCompleted="";
		try {
			if(lifeSpan.equalsIgnoreCase("0")) {
			String qry="SELECT \n"
					+ "    Count(*) AS COUNT\n"
					+ "FROM\n"
					+ "    indent_hdr hdr,\n"
					+ "    project_hdr phdr,\n"
					+ "    process_assigned_team pat\n"
					+ "WHERE\n"
					+ "    hdr.PROJECT_ID = phdr.PM_HDR_ID\n"
					+ "        AND phdr.PM_HDR_ID = pat.MASTER_ID\n"
					+ "        AND pat.ASSIGNED_EMP_ID = ?\n"
					+ "        AND pat.PM_ID = ?\n"
					+ "        AND pat.IS_ACTIVE = 1\n"
					+ "        AND hdr.project_ID like ? AND hdr.IS_COMPLETED =1 \n"
					+"         and month(hdr.CREATED_DATE)= ? and year(hdr.CREATED_DATE)=? and pat.TENANT_ID=?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,projectId,month,year,tenantId);
			indentCompleted = resultMap.get("COUNT").toString();
			}else {
				String qry="SELECT \n"
						+ "    Count(*) AS COUNT\n"
						+ "FROM\n"
						+ "    indent_hdr hdr,\n"
						+ "    project_hdr phdr,\n"
						+ "    process_assigned_team pat\n"
						+ "WHERE\n"
						+ "    hdr.PROJECT_ID = phdr.PM_HDR_ID\n"
						+ "        AND phdr.PM_HDR_ID = pat.MASTER_ID\n"
						+ "        AND pat.ASSIGNED_EMP_ID = ?\n"
						+ "        AND pat.PM_ID = ?\n"
						+ "        AND pat.IS_ACTIVE = 1\n"
						+ "        AND hdr.project_ID like ? AND hdr.IS_COMPLETED =1";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,projectId);
				indentCompleted = resultMap.get("COUNT").toString();
			} 
		} catch (Exception ex) {
			logger.error("indentCompleted  method exception-->" + ex);
		}
		logger.debug("indentCompleted  method end");
		return indentCompleted;
	}

	@Override
	public String indentAvgTime(String projectId,String pmId,String empId,String month, String year,String lifeSpan, String tenantId) {
		String indentAvgTime="";
		try {
			if(lifeSpan.equalsIgnoreCase("0")) {
			String qry="SELECT \n"
					+ " case when count(*)>0 then  avg( DATEDIFF(hdr.CLOSED_DATE, hdr.CREATED_DATE))  else 0 end AS AVG_DAYS \n"
					+ "FROM\n"
					+ "    indent_hdr hdr,\n"
					+ "    scm_hdr phdr,\n"
					+ "    process_assigned_team pat\n"
					+ "WHERE\n"
					+ "    hdr.PROJECT_ID = phdr.PM_HDR_ID\n"
					+ "        AND phdr.SCM_HDR_ID = pat.MASTER_ID\n"
					+ "        AND pat.ASSIGNED_EMP_ID = ?\n"
					+ "        AND pat.PM_ID = ?\n"
					+ "        AND pat.IS_ACTIVE = 1\n"
					+ "        AND hdr.project_ID like ? AND hdr.IS_COMPLETED =1 \n"
                    +"         and month(hdr.CREATED_DATE)=? and year(hdr.CREATED_DATE)=? and pat.TENANT_ID=?";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,projectId,month,year,tenantId);
			indentAvgTime = resultMap.get("AVG_DAYS").toString();	
			}else {
				String qry="SELECT \n"
						+ " case when count(*)>0 then  avg( DATEDIFF(hdr.CLOSED_DATE, hdr.CREATED_DATE))  else 0 end AS AVG_DAYS \n"
						+ "FROM\n"
						+ "    indent_hdr hdr,\n"
						+ "    scm_hdr phdr,\n"
						+ "    process_assigned_team pat\n"
						+ "WHERE\n"
						+ "    hdr.PROJECT_ID = phdr.PM_HDR_ID\n"
						+ "        AND phdr.SCM_HDR_ID = pat.MASTER_ID\n"
						+ "        AND pat.ASSIGNED_EMP_ID = ?\n"
						+ "        AND pat.PM_ID = ?\n"
						+ "        AND pat.IS_ACTIVE = 1\n"
						+ "        AND hdr.project_ID like ? AND hdr.IS_COMPLETED =1 and pat.TENANT_ID=?";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,projectId,tenantId);
				indentAvgTime = resultMap.get("AVG_DAYS").toString();
			}
		} catch (Exception ex) {
			logger.error("indentAvgTime  method exception-->" + ex);
		}
		logger.debug("indentAvgTime  method end");
		return indentAvgTime;
	}

	@Override
	public String getcostnegotiated(String pmHdrId, String tenantId,String month, String year,String lifeSpan) {
		String cost="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	="%%";
			}
			if(lifeSpan.equalsIgnoreCase("0")) {
			String qry="SELECT \r\n" + 
					"    SUM(hdr.UNIT_INI_BASIC_TOTAL - hdr.EXTN_INI_BASIC_TOTAL) as AVG_VAL\r\n" + 
					"FROM\r\n" + 
					"    po_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr ih ON hdr.INDENT_ID = ih.INDENT_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.IS_APPROVED = 1\r\n" + 
					"        AND hdr.IS_LATEST = 1\r\n" + 
					"        AND ih.PROJECT_ID like ?\r\n" + 
					"        AND hdr.TENANT_ID = ?\r\n" + 
                    "       and month(hdr.DATE)=? and year(hdr.DATE)=?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,pmHdrId,tenantId,month,year);
			cost = resultMap.get("AVG_VAL").toString();
			}else {
				String qry="SELECT \r\n" + 
						"     SUM(hdr.UNIT_INI_BASIC_TOTAL - hdr.EXTN_INI_BASIC_TOTAL) as AVG_VAL \r\n" + 
						"FROM\r\n" + 
						"    po_hdr hdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_hdr ih ON hdr.INDENT_ID = ih.INDENT_ID\r\n" + 
						"WHERE\r\n" + 
						"    hdr.IS_APPROVED = 1\r\n" + 
						"        AND hdr.IS_LATEST = 1\r\n" + 
						"        AND ih.PROJECT_ID like ?\r\n" + 
						"        AND hdr.TENANT_ID = ?" ;
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,pmHdrId,tenantId);
				cost = resultMap.get("AVG_VAL").toString();
			} 
		} catch (Exception ex) {
			logger.error("getcostnegotiated method exception-->" + ex);
		}
		logger.debug("getcostnegotiated method end");
		return cost;
	}

	@Override
	public List<InvProdEntity> getQtyInHand(String pmHdrId, String tenantId) {
//		String col="";
		List<InvProdEntity> list = new ArrayList<>();
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	="%%";
			}
	
			String qry="SELECT \r\n" + 
					"					  \r\n" + 
					"					     (SUM(pdtl.PRODUCT_QUANTITY_ON_HAND) * pm.PRODUCT_COST_PER_UNIT)  AS INVENTORY_VALUE \r\n" + 
					"					FROM \r\n" + 
					"					    product_mst pm \r\n" + 
					"					    INNER JOIN inventory_product_dtl pdtl ON pm.PRODUCT_ID = pdtl.PRODUCT_ID \r\n" + 
					"					    INNER JOIN inventory_location_mst ilm ON ilm.INVENTORY_LOCATION_CODE = pdtl.INVENTORY_LOCATION_CODE \r\n" + 
					"					    left JOIN uom_mst um ON pm.PRODUCT_UOM_CODE = um.UOM_CODE \r\n" + 
					"					    INNER JOIN project_hdr pj ON pm.PM_HDR_ID = pj.PM_HDR_ID \r\n" + 
					"					WHERE \r\n" + 
					"					    PRODUCT_QUANTITY_ON_HAND > 0 \r\n" + 
					"					    AND pm.TENANT_ID = '"+tenantId+"' \r\n" + 
					"					    AND pj.PM_HDR_ID LIKE '"+pmHdrId+"' AND ilm.INVENTORY_LOCATION_CODE IN ('ILC0002','ILC0004') \r\n" + 
					"					GROUP BY \r\n" + 
					"					    pm.PRODUCT_ID,\r\n" + 
					"					     pm.PM_HDR_ID ;" ;
			list = this.jdbcTemplate.query(qry, new InvProdRowMapper());
			
		} catch (Exception ex) {
			logger.error("getQtyInHand method exception-->" + ex);
		}
		logger.debug("getQtyInHand method end");
		return list;
	}
	
	
		@Override
	public String getInventoryAgeing(String pmHdrId, String tenantId) {
		String avgDays="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	= "%%";
			}
			String qry="SELECT \r\n" + 
					"    AVG(TIMESTAMPDIFF(DAY,\r\n" + 
					"        prod.INWARD_DATETIME,\r\n" + 
					"        NOW())) AS Result\r\n" + 
					"FROM\r\n" + 
					"    inventory_product_dtl inv\r\n" + 
					"        INNER JOIN\r\n" + 
					"    product_mst prod ON inv.PRODUCT_ID = prod.PRODUCT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    inventory_location_mst loc ON inv.INVENTORY_LOCATION_CODE = loc.INVENTORY_LOCATION_CODE\r\n" + 
					"WHERE\r\n" + 
					"    prod.PM_HDR_ID LIKE ?\r\n" + 
					"        AND inv.TENANT_ID = ?\r\n" + 
					"        AND inv.PRODUCT_QUANTITY_ON_HAND > 0 AND loc.INVENTORY_LOCATION_CODE IN ('ILC0002','ILC0004');";
			
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,pmHdrId,tenantId);
			avgDays = resultMap.get("Result").toString();
		} catch (Exception ex) {
			logger.error("getInventoryAgeing  method exception-->" + ex);
		}
		logger.debug("getInventoryAgeing  method end");
		return avgDays;
	}

	@Override
	public List<ScmEmployeeIndentDtlsEntity> getScmEmployeeIndentDtls(String empId, String pmHdrId, String tenantId,String month, String year,String lifeSpan) {
		List<ScmEmployeeIndentDtlsEntity> list=new ArrayList<ScmEmployeeIndentDtlsEntity>();
		String projectIdcol="",empIdCol="",monthYear="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				projectIdcol	="  hdr.PROJECT_ID like '%%' ";
			}else {
				projectIdcol	="  hdr.PROJECT_ID = '"+pmHdrId+"'";
			}
			
			if(empId.equalsIgnoreCase("getall")) {
				empIdCol =" AND iat.EMPLOYEE_ID like '%%' ";
			}else {
				empIdCol =" AND iat.EMPLOYEE_ID = '"+empId+"'";
			}
			
			if(lifeSpan.equalsIgnoreCase("0")) {
				monthYear="and month(hdr.CREATED_DATE)='"+month+"' and year(hdr.CREATED_DATE)='"+year+"'";
			}
	
			String qry="SELECT \r\n" + 
					"    proj.PM_HDR_ID,\r\n" + 
					"    proj.PROJECT_CODE AS PROJECT_CODE,\r\n" + 
					"    emp.EMPLOYEE_FIRSTNAME AS EMPLOYEE,\r\n" + 
					"    iat.EMPLOYEE_ID\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_assign_team iat ON dtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr proj ON hdr.PROJECT_ID = proj.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst emp ON iat.EMPLOYEE_ID = emp.EMPLOYEE_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    po_hdr poh ON hdr.INDENT_ID = poh.INDENT_ID\r\n" + 
					"        LEFT JOIN\r\n" + 
					"    po_dtl pod ON dtl.INDENT_DTL_ID = pod.INDENT_DTL_ID\r\n" + 
					"        AND pod.PO_ID = poh.PO_ID\r\n" + 
					"WHERE\r\n" + 
					projectIdcol+"\r\n" + 
					empIdCol+"\r\n" + 
					" AND iat.IS_PRIMARY=0      AND hdr.TENANT_ID = '"+tenantId+"'\r\n" +  monthYear+
					"GROUP BY proj.PM_HDR_ID , iat.EMPLOYEE_ID ORDER BY PROJECT_CODE";

			list = this.jdbcTemplate.query(qry, new ScmEmployeeIndentRowmapper());
			
		} catch (Exception ex) {
			logger.error("getScmEmployeeIndentDtls method exception-->" + ex);
		}
		logger.debug("getScmEmployeeIndentDtls method end");
		return list;
	}

	@Override
	public String noOfPoApproved(String pmHdrId, String tenantId, String assignedTo,String month,String year, String lifeSpan) {
		String count="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	="%%";
			}
			
			if(lifeSpan.equalsIgnoreCase("0")) {
		
				String qry="SELECT \r\n" + 
					" count(DISTINCT PO_DTL_ID) as count\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_assign_team iat ON dtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_hdr poh ON hdr.INDENT_ID = poh.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_dtl pod ON dtl.INDENT_DTL_ID = pod.INDENT_DTL_ID  AND poh.PO_ID = pod.PO_ID\r\n" + 
					"WHERE\r\n" + 
					"        hdr.PROJECT_ID like ? \r\n" + 
					"        AND iat.EMPLOYEE_ID = ?\r\n" + 
					"        AND hdr.TENANT_ID = ?\r\n" + 
					"        AND poh.IS_LATEST = 1\r\n" + 
					"        AND poh.IS_APPROVED = 1 and poh.SEQUENCE_STATUS !='DS100' #and hdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077') \r\n" +
					" and month(hdr.CREATED_DATE)=? and year(hdr.CREATED_DATE)=?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,pmHdrId,assignedTo,tenantId,month,year);
				count = resultMap.get("count").toString();
			}else {
				String qry="SELECT \r\n" + 
						" count(DISTINCT PO_DTL_ID) as count\r\n" + 
						"FROM\r\n" + 
						"    indent_hdr hdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_dtl dtl ON hdr.INDENT_ID = dtl.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_assign_team iat ON dtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_hdr poh ON hdr.INDENT_ID = poh.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_dtl pod ON dtl.INDENT_DTL_ID = pod.INDENT_DTL_ID  AND poh.PO_ID = pod.PO_ID\r\n" + 
						"WHERE\r\n" + 
						"        hdr.PROJECT_ID like ? \r\n" + 
						"        AND iat.EMPLOYEE_ID = ? #AND IS_PRIMARY =1\r\n" + 
						"        AND hdr.TENANT_ID = ? #AND  hdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077') \r\n" + 
						"        AND poh.IS_LATEST = 1\r\n" + 
						"        AND poh.IS_APPROVED = 1  and poh.SEQUENCE_STATUS !='DS100'" ;
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,pmHdrId,assignedTo,tenantId);
				count = resultMap.get("count").toString();
			}	
		} catch (Exception ex) {
			logger.error("noOfPoApproved method exception-->" + ex);
		}
		logger.debug("noOfPoApproved method end");
		return count;
	}
	
	@Override
	public String getIndentDtlCount(String pmHdrId, String tenantId, String assignedTo,String month,String year, String lifeSpan,String pmId) {
		String count="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	="%%";
			}
			
			if(lifeSpan.equalsIgnoreCase("0")) {
		
				String qry="SELECT \r\n" + 
						"    COUNT(DISTINCT dtl.INDENT_DTL_ID) AS INDENT_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    indent_dtl dtl\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_hdr AS ih ON dtl.INDENT_ID = ih.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_assign_team iat ON dtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    iat.EMPLOYEE_ID = ?\r\n" + 
						"        AND team.ASSIGNED_EMP_ID = ?\r\n" + 
						"        AND team.IS_ACTIVE = '1'\r\n" + 
						"        AND team.PM_ID = ?\r\n" + 
						"        AND ih.PROJECT_ID like ? AND ih.TENANT_ID=?\r\n" + 
						"        AND ih.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')\r\n" + 
						"        and month(ih.CREATED_DATE)=? and year(ih.CREATED_DATE)=?;";
				
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,assignedTo,assignedTo,pmId,pmHdrId,tenantId,month,year);
				count = resultMap.get("INDENT_DTL_ID").toString();
			}else {
				String qry="SELECT \r\n" + 
						"    COUNT(DISTINCT dtl.INDENT_DTL_ID) AS INDENT_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    indent_dtl dtl\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_hdr AS ih ON dtl.INDENT_ID = ih.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_assign_team iat ON dtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    iat.EMPLOYEE_ID = ?\r\n" + 
						"        AND team.ASSIGNED_EMP_ID = ?\r\n" + 
						"        AND team.IS_ACTIVE = '1'\r\n" + 
						"        AND team.PM_ID = ?\r\n" + 
						"        AND ih.PROJECT_ID like ? AND ih.TENANT_ID=?\r\n" + 
						"        AND ih.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')\r\n" ;
				
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,assignedTo,assignedTo,pmId,pmHdrId,tenantId);
				count = resultMap.get("INDENT_DTL_ID").toString();
			}	
		} catch (Exception ex) {
			logger.error("getIndentDtlCount method exception-->" + ex);
		}
		logger.debug("getIndentDtlCount method end");
		return count;
	}
	
	@Override
	public String getIndentHdrCount(String pmHdrId, String tenantId, String assignedTo,String month,String year, String lifeSpan,String pmId) {
		String count="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	="%%";
			}
			
			if(lifeSpan.equalsIgnoreCase("0")) {
		
				String qry="SELECT \r\n" + 
						"    COUNT(DISTINCT ih.INDENT_ID) AS INDENT_ID\r\n" + 
						"FROM\r\n" + 
						"    indent_hdr AS ih\r\n" + 
						"        INNER JOIN\r\n" + 
						"    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    team.ASSIGNED_EMP_ID = ?\r\n" + 
						"        AND team.IS_ACTIVE = '1'\r\n" + 
						"        AND team.PM_ID = ?\r\n" + 
						"        AND ih.PROJECT_ID like ? AND ih.TENANT_ID=?\r\n" + 
						"        AND ih.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')\r\n" + 
						"        AND MONTH(ih.CREATED_DATE) = ?\r\n" + 
						"        AND YEAR(ih.CREATED_DATE) =?;";
				
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,assignedTo,pmId,pmHdrId,tenantId,month,year);
				count = resultMap.get("INDENT_ID").toString();
			}else {
				String qry="SELECT \r\n" + 
						"    COUNT(DISTINCT ih.INDENT_ID) AS INDENT_ID\r\n" + 
						"FROM\r\n" + 
						"    indent_hdr AS ih\r\n" + 
						"        INNER JOIN\r\n" + 
						"    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    team.ASSIGNED_EMP_ID = ?\r\n" + 
						"        AND team.IS_ACTIVE = '1'\r\n" + 
						"        AND team.PM_ID = ?\r\n" + 
						"        AND ih.PROJECT_ID like ?\r\n" + 
						"        AND ih.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077') and ih.TENANT_ID=?\r\n" ;
				
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,assignedTo,pmId,pmHdrId,tenantId);
				count = resultMap.get("INDENT_ID").toString();
			}	
		} catch (Exception ex) {
			logger.error("getIndentHdrCount method exception-->" + ex);
		}
		logger.debug("getIndentHdrCount method end");
		return count;
	}
	
	@Override
	public String getPendingIndentsCnt(String pmHdrId, String tenantId, String empId,String month,String year, String lifeSpan,String pmId) {
		String count="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	="%%";
			}
			
			if(lifeSpan.equalsIgnoreCase("0")) {
			String qry="SELECT \r\n" + 
					"    COUNT(DISTINCT indtl.INDENT_DTL_ID) as COUNT\r\n" + 
					"FROM\r\n" + 
					"    indent_dtl indtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr inhdr ON indtl.INDENT_ID = inhdr.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    scm_hdr sc ON sc.PM_HDR_ID = inhdr.PROJECT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID\r\n" + 
					"        INNER JOIN \r\n" +
					"    indent_assign_team iat ON iat.INDENT_DTL_ID = indtl.INDENT_DTL_ID \r\n" +
					"        INNER JOIN\r\n" + 
					"    employee_mst mst ON iat.EMPLOYEE_ID = mst.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    inhdr.IS_COMPLETED = 0\r\n" + 
					"        AND team.ASSIGNED_EMP_ID = ? and iat.EMPLOYEE_ID = '"+empId+"'\r\n" + 
					"        AND team.IS_ACTIVE = '1'\r\n" + 
					"        AND team.PM_ID = ?\r\n  AND inhdr.PROJECT_ID like ? \r\n" + 
					"        AND inhdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')\r\n and inhdr.TENANT_ID='"+tenantId+"'\r\n" + 
					"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
					"            podtl.INDENT_DTL_ID\r\n" + 
					"        FROM\r\n" + 
					"            po_hdr pohdr\r\n" + 
					"                INNER JOIN\r\n" + 
					"            po_dtl podtl ON podtl.PO_ID = pohdr.PO_ID INNER JOIN indent_hdr hdr  on hdr.INDENT_ID = pohdr.INDENT_ID \r\n" + 
					"        WHERE\r\n" + 
					"            pohdr.IS_APPROVED = 1\r\n" + 
					"                AND pohdr.IS_LATEST = 1 and pohdr.TENANT_ID='"+tenantId+"' and hdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077'))\r\n" + 
					"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
					"            indtl.INDENT_DTL_ID\r\n" + 
					"        FROM\r\n" + 
					"            indent_grp_dtl indtl\r\n" + 
					"                INNER JOIN\r\n" + 
					"            indent_grp_hdr inhdr ON indtl.IG_HDR_ID = inhdr.IG_HDR_ID\r\n" + 
					"        WHERE\r\n" + 
					"            inhdr.IS_INVENTORY = '1' and inhdr.TENANT_ID='"+tenantId+"')\r\n" +
					"and month(inhdr.CREATED_DATE)=? and year(inhdr.CREATED_DATE)=? and inhdr.TENANT_ID=? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,pmHdrId,month,year,tenantId);
			count = resultMap.get("COUNT").toString();
			}else {
				String qry="SELECT \r\n" + 
						"    COUNT(DISTINCT indtl.INDENT_DTL_ID) as COUNT\r\n" + 
						"FROM\r\n" + 
						"    indent_dtl indtl\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_hdr inhdr ON indtl.INDENT_ID = inhdr.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    scm_hdr sc ON sc.PM_HDR_ID = inhdr.PROJECT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID\r\n" + 
						"        INNER JOIN \r\n" +
						"    indent_assign_team iat ON iat.INDENT_DTL_ID = indtl.INDENT_DTL_ID \r\n" +
						"        INNER JOIN\r\n" + 
						"    employee_mst mst ON iat.EMPLOYEE_ID = mst.EMPLOYEE_ID\r\n" + 
						"WHERE\r\n" + 
						"    inhdr.IS_COMPLETED = 0\r\n" + 
						"        AND team.ASSIGNED_EMP_ID = '"+empId+"' and iat.EMPLOYEE_ID = '"+empId+"'\r\n" + 
						"        AND team.IS_ACTIVE = '1'\r\n" + 
						"        AND team.PM_ID = '"+pmId+"'\r\n  AND inhdr.PROJECT_ID like '"+pmHdrId+"' \r\n" + 
						"        AND inhdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')\r\n and inhdr.TENANT_ID='"+tenantId+"'\r\n " + 
						"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
						"            podtl.INDENT_DTL_ID\r\n" + 
						"        FROM\r\n" + 
						"            po_hdr pohdr\r\n" + 
						"                INNER JOIN\r\n" + 
						"            po_dtl podtl ON podtl.PO_ID = pohdr.PO_ID  INNER JOIN indent_hdr hdr  on hdr.INDENT_ID = pohdr.INDENT_ID  \r\n" + 
						"        WHERE\r\n" + 
						"            pohdr.IS_APPROVED = 1\r\n" + 
						"                AND pohdr.IS_LATEST = 1  and pohdr.TENANT_ID='"+tenantId+"' and hdr.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077') )\r\n" + 
						"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
						"            indtl.INDENT_DTL_ID\r\n" + 
						"        FROM\r\n" + 
						"            indent_grp_dtl indtl\r\n" + 
						"                INNER JOIN\r\n" + 
						"            indent_grp_hdr inhdr ON indtl.IG_HDR_ID = inhdr.IG_HDR_ID\r\n" + 
						"        WHERE\r\n" + 
						"            inhdr.IS_INVENTORY = '1' and inhdr.TENANT_ID='"+tenantId+"')";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry);
				count = resultMap.get("COUNT").toString();
			}
			
			
		} catch (Exception ex) {
			logger.error("getPendingIndentsCnt method exception-->" + ex);
		}
		logger.debug("getPendingIndentsCnt method end");
		return count;
	}
	
	@Override
	public String getInventoryStockCnt(String pmHdrId, String tenantId, String empId, String month, String year,
			String lifeSpan, String pmId) {
		String count="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	="%%";
			}
			
			if(lifeSpan.equalsIgnoreCase("0")) {
			String qry="SELECT  \r\n" + 
					"						            Count(distinct indtl.INDENT_DTL_ID) as COUNT\r\n" + 
					"						        FROM \r\n" + 
					"                                   indent_dtl dtl \r\n" + 
					"						              INNER JOIN \r\n" + 
					"								   indent_hdr hdr  ON dtl.INDENT_ID = hdr.INDENT_ID \r\n" + 
					"                                       inner join \r\n" + 
					"						            indent_grp_dtl indtl ON indtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID \r\n" + 
					"						                INNER JOIN \r\n" + 
					"						            indent_grp_hdr inhdr ON indtl.IG_HDR_ID = inhdr.IG_HDR_ID \r\n" + 
					"                                        INNER JOIN \r\n" + 
					"						            scm_hdr sc ON sc.PM_HDR_ID = hdr.PROJECT_ID \r\n" + 
					"						                INNER JOIN \r\n" + 
					"						            process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID \r\n" + 
					"        INNER JOIN \r\n" +
					"    indent_assign_team iat ON iat.INDENT_DTL_ID = indtl.INDENT_DTL_ID \r\n" +
					"        INNER JOIN\r\n" + 
					"    employee_mst mst ON iat.EMPLOYEE_ID = mst.EMPLOYEE_ID\r\n" + 
					"						        WHERE \r\n" + 
					"						            inhdr.IS_INVENTORY = '1' and hdr.TENANT_ID=? AND hdr.IS_COMPLETED=0 \r\n" + 
					"                                     AND hdr.PROJECT_ID like ? " +
					"and iat.EMPLOYEE_ID = '"+empId+"' \r\n" + 
					"                                     AND team.ASSIGNED_EMP_ID = ?\r\n" + 
					"									 AND team.IS_ACTIVE = '1' \r\n" + 
					"						             AND team.PM_ID = ? and month(hdr.CREATED_DATE)=? and year(hdr.CREATED_DATE)=? ;";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,tenantId,pmHdrId,empId,pmId,month,year);
			count = resultMap.get("COUNT").toString();
			}else {
				String qry="SELECT  \r\n" + 
						"						            Count(distinct indtl.INDENT_DTL_ID) as COUNT\r\n" + 
						"						        FROM \r\n" + 
						"                                   indent_dtl dtl \r\n" + 
						"						              INNER JOIN \r\n" + 
						"								   indent_hdr hdr  ON dtl.INDENT_ID = hdr.INDENT_ID \r\n" + 
						"                                       inner join \r\n" + 
						"						            indent_grp_dtl indtl ON indtl.INDENT_DTL_ID = dtl.INDENT_DTL_ID \r\n" + 
						"						                INNER JOIN \r\n" + 
						"						            indent_grp_hdr inhdr ON indtl.IG_HDR_ID = inhdr.IG_HDR_ID \r\n" + 
						"                                        INNER JOIN \r\n" + 
						"						            scm_hdr sc ON sc.PM_HDR_ID = hdr.PROJECT_ID \r\n" + 
						"						                INNER JOIN \r\n" + 
						"						            process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID \r\n" + 
						"        INNER JOIN \r\n" +
						"    indent_assign_team iat ON iat.INDENT_DTL_ID = indtl.INDENT_DTL_ID \r\n" +
						"        INNER JOIN\r\n" + 
						"    employee_mst mst ON iat.EMPLOYEE_ID = mst.EMPLOYEE_ID\r\n" + 
						"						        WHERE \r\n" + 
						"						            inhdr.IS_INVENTORY = '1' and hdr.TENANT_ID=?\r\n" + 
						"                                     AND hdr.PROJECT_ID like ? " +
						"and iat.EMPLOYEE_ID = '"+empId+"' \r\n" + 
						"                                     AND team.ASSIGNED_EMP_ID = ? \r\n" + 
						"									 AND team.IS_ACTIVE = '1' \r\n" + 
						"						             AND team.PM_ID = ?;";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,tenantId,pmHdrId,empId,pmId);
				count = resultMap.get("COUNT").toString();
			}
			
			
		} catch (Exception ex) {
			logger.error("getInventoryStockCnt method exception-->" + ex);
		}
		logger.debug("getInventoryStockCnt method end");
		return count;
	}
	
	@Override
	public String getItemsDelayedCnt(String pmHdrId, String tenantId, String empId,String month,String year, String lifeSpan,String pmId) {
		String count="";
		try {
			if(pmHdrId.equalsIgnoreCase("getall")) {
				pmHdrId	= "%%";
			}
			
			if(lifeSpan.equalsIgnoreCase("0")) {
		
			String qry="SELECT \r\n" + 
					"    COUNT(distinct indtl.INDENT_DTL_ID) AS COUNT\r\n" + 
					"FROM\r\n" + 
					"    indent_dtl indtl\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_hdr inhdr ON indtl.INDENT_ID = inhdr.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    scm_hdr sc ON sc.PM_HDR_ID = inhdr.PROJECT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID\r\n" + 
					"        INNER JOIN \r\n" +
					"    indent_assign_team iat ON iat.INDENT_DTL_ID = indtl.INDENT_DTL_ID \r\n" +
					"        INNER JOIN\r\n" + 
					"    employee_mst mst ON iat.EMPLOYEE_ID = mst.EMPLOYEE_ID\r\n" + 
					"WHERE\r\n" + 
					"    inhdr.IS_COMPLETED = 0 AND inhdr.SEQUENCE_N0 > 6\r\n" + 
					"        AND team.ASSIGNED_EMP_ID = ? and iat.EMPLOYEE_ID = '"+empId+"'\r\n" + 
					"        AND team.IS_ACTIVE = '1'\r\n" + 
					"        AND team.PM_ID = ? AND inhdr.PROJECT_ID like ?\r\n" + 
					"        and inhdr.TENANT_ID='"+tenantId+"'" + 
					"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
					"            podtl.INDENT_DTL_ID\r\n" + 
					"        FROM\r\n" + 
					"            po_hdr pohdr\r\n" + 
					"                INNER JOIN\r\n" + 
					"            po_dtl podtl ON podtl.PO_ID = pohdr.PO_ID\r\n" + 
					"        WHERE\r\n" + 
					"            pohdr.IS_APPROVED = 1\r\n" + 
					"                AND pohdr.IS_LATEST = 1 and pohdr.TENANT_ID='"+tenantId+"')\r\n" + 
					"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
					"            indtl.INDENT_DTL_ID\r\n" + 
					"        FROM\r\n" + 
					"            indent_grp_dtl indtl\r\n" + 
					"                INNER JOIN\r\n" + 
					"            indent_grp_hdr inhdr ON indtl.IG_HDR_ID = inhdr.IG_HDR_ID\r\n" + 
					"        WHERE\r\n" + 
					"            inhdr.IS_INVENTORY = '1' and inhdr.TENANT_ID='"+tenantId+"')\r\n" +
					"and month(inhdr.CREATED_DATE)=? and year(inhdr.CREATED_DATE)=?" +
					"and inhdr.EXPECTED_DELIVERY_DATE < CURRENT_DATE and inhdr.TENANT_ID='"+tenantId+"'";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,pmHdrId,month,year);
			count = resultMap.get("COUNT").toString();
			}else {
				String qry="SELECT \r\n" + 
						"    COUNT(distinct indtl.INDENT_DTL_ID) as COUNT\r\n" + 
						"FROM\r\n" + 
						"    indent_dtl indtl\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_hdr inhdr ON indtl.INDENT_ID = inhdr.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    scm_hdr sc ON sc.PM_HDR_ID = inhdr.PROJECT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    process_assigned_team team ON team.MASTER_ID = sc.SCM_HDR_ID\r\n" + 
//						"        INNER JOIN \r\n" +
//						"    indent_assign_team iat ON iat.INDENT_DTL_ID = indtl.INDENT_DTL_ID \r\n" +
//						"        INNER JOIN\r\n" + 
//						"    employee_mst mst ON iat.EMPLOYEE_ID = mst.EMPLOYEE_ID\r\n" +
						"WHERE\r\n" + 
						"    inhdr.IS_COMPLETED = 0 AND inhdr.SEQUENCE_N0 > 6\r\n" + 
						"        AND team.ASSIGNED_EMP_ID = ? #and iat.EMPLOYEE_ID = '"+empId+"'\r\n" + 
						"        AND team.IS_ACTIVE = '1'\r\n" + 
						"        AND team.PM_ID = ? AND inhdr.PROJECT_ID like ?\r\n" + 
						"        and inhdr.TENANT_ID='"+tenantId+"' \r\n" + 
						"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
						"            podtl.INDENT_DTL_ID\r\n" + 
						"        FROM\r\n" + 
						"            po_hdr pohdr\r\n" + 
						"                INNER JOIN\r\n" + 
						"            po_dtl podtl ON podtl.PO_ID = pohdr.PO_ID\r\n" + 
						"        WHERE\r\n" + 
						"            pohdr.IS_APPROVED = 1\r\n" + 
						"                AND pohdr.IS_LATEST = 1 and pohdr.TENANT_ID='"+tenantId+"')\r\n" + 
						"        AND indtl.INDENT_DTL_ID NOT IN (SELECT \r\n" + 
						"            indtl.INDENT_DTL_ID\r\n" + 
						"        FROM\r\n" + 
						"            indent_grp_dtl indtl\r\n" + 
						"                INNER JOIN\r\n" + 
						"            indent_grp_hdr inhdr ON indtl.IG_HDR_ID = inhdr.IG_HDR_ID\r\n" + 
						"        WHERE\r\n" + 
						"            inhdr.IS_INVENTORY = '1' and inhdr.TENANT_ID='"+tenantId+"')\r\n" +
						"and inhdr.EXPECTED_DELIVERY_DATE < CURRENT_DATE and inhdr.TENANT_ID='"+tenantId+"'";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmId,pmHdrId);
				count = resultMap.get("COUNT").toString();
			}			
		} catch (Exception ex) {
			logger.error("getItemsDelayedCnt method exception-->" + ex);
		}
		logger.debug("getItemsDelayedCnt method end");
		return count;
	}
	
	
	@Override
	public String getTotalAssignedIndents(String pmHdrId, String empId,String tenantId) {
		String val="";
		try {
			
			String qry="SELECT \r\n" + 
					"    COUNT(*) AS TOTAL_ASSIGNED_INDENTS\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr ih\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl dtl ON ih.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_assign_team iat ON dtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
					"WHERE\r\n" + 
					"    iat.EMPLOYEE_ID = ? \r\n" + 
					"        AND ih.PROJECT_ID = ? AND ih.TENANT_ID = ?;";
			
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmHdrId,tenantId);
			val = resultMap.get("TOTAL_ASSIGNED_INDENTS").toString();	 
		} catch (Exception ex) {
			logger.error("getTotalAssignedIndents  method exception-->" + ex);
		}
		logger.debug("getTotalAssignedIndents  method end");
		return val;
	}
	
	@Override
	public String getCompletedIndentCount(String pmHdrId, String empId,String tenantId) {
		String val="";
		try {
			
			String qry="SELECT \r\n" + 
					"    COUNT(*) AS COMPLETED_INDENTS\r\n" + 
					"FROM\r\n" + 
					"    indent_hdr ih\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_dtl dtl ON ih.INDENT_ID = dtl.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    indent_assign_team iat ON dtl.INDENT_DTL_ID = iat.INDENT_DTL_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_hdr poh ON ih.INDENT_ID = poh.INDENT_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    po_dtl pod ON poh.PO_ID = pod.PO_ID\r\n" + 
					"        AND dtl.INDENT_DTL_ID = pod.INDENT_DTL_ID\r\n" + 
					"WHERE\r\n" + 
					"    iat.EMPLOYEE_ID = ?\r\n" + 
					"        AND ih.PROJECT_ID = ?\r\n" + 
					"        AND poh.IS_APPROVED = 1\r\n" + 
					"        AND poh.IS_LATEST = 1 AND ih.TENANT_ID = ?;";
			
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmHdrId,tenantId);
			val = resultMap.get("COMPLETED_INDENTS").toString();
		} catch (Exception ex) {
			logger.error("getCompletedIndentCount  method exception-->" + ex);
		}
		logger.debug("getCompletedIndentCount  method end");
		return val;
	}

	@Override
	public List<VendorDetailDrillDownEntity> getVendorPaymentCount(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId, String pmId, String empId) {
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "", vendor = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' and '"+toDate+"' ";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		vendor = "getall".equalsIgnoreCase(vendorId) ? "AND vmst.VENDOR_CODE like '%%'" : " AND vmst.VENDOR_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT  \r\n" + 
	    			"                SUM(AMOUNT_DUE) as AMOUNT_DUE \r\n" + 
	    			"	    			FROM \r\n" + 
	    			"	    			    project_hdr hdr \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"                       customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"                           INNER JOIN\r\n" + 
	    			"	    			    indent_hdr ih ON ih.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"	    			    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"	    			    process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"	    			    po_hdr phdr ON ih.INDENT_ID = phdr.INDENT_ID \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"	    			    pra_hdr pra ON pra.PO_ID = phdr.PO_ID \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"	    			    vendor_mst vmst ON vmst.VENDOR_CODE = phdr.VENDOR_CODE \r\n" + 
	    			"	    			        LEFT JOIN \r\n" + 
	    			"	    				document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS \r\n" +  
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			" AND team.ASSIGNED_EMP_ID = '"+empId+"' AND team.IS_ACTIVE = '1'  AND team.PM_ID ='"+pmId+"' \r\n"  +
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+vendor+"\r\n" + 
	    			"       "+date+"\r\n";
	    	list = this.jdbcTemplate.query(Qry, new GetVendorDetailDrillDownRowMapper());
	    	if(list.size()>0) {
	    		String qry = "SELECT  \r\n" + 
		    			"				  SUM(AMOUNT_PAYABLE) as DONE_SO_FAR\r\n" + 
		    			"	    			FROM \r\n" + 
		    			"	    			    project_hdr hdr \r\n" + 
		    			"                           INNER JOIN\r\n" + 
		    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    indent_hdr ih ON ih.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID \r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID \r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    po_hdr phdr ON ih.INDENT_ID = phdr.INDENT_ID \r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    pra_hdr pra ON pra.PO_ID = phdr.PO_ID \r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    vendor_mst vmst ON vmst.VENDOR_CODE = phdr.VENDOR_CODE \r\n" + 
		    			"	    			        LEFT JOIN \r\n" + 
		    			"	    				document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS \r\n" +  
		    			"WHERE hdr.TENANT_ID = '"+tenantId+"' and pra.STATUS = '9' \r\n" + 
		    			" AND team.ASSIGNED_EMP_ID = '"+empId+"' AND team.IS_ACTIVE = '1'  AND team.PM_ID ='"+pmId+"' \r\n" +
		    			"		"+pmHdr+"\r\n" + 
		    			"       "+stage+"\r\n" + 
		    			"       "+cust+"\r\n" + 
		    			"       "+vendor+"\r\n" + 
		    			"       "+date+"\r\n";
	    		Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry);
   			    String paidSoFar = resultMap.get("DONE_SO_FAR") != null ? resultMap.get("DONE_SO_FAR").toString() : "0";
   			    list.get(0).setPaidSoFar(paidSoFar);
	    	}
	    }catch(Exception ex) {
	    	logger.error("getVendorPaymentCount DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<VendorDetailDrillDownEntity> getVendorDetailView(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId, String empId, String pmId) {
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "", vendor="";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' and '"+toDate+"' ";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		vendor = "getall".equalsIgnoreCase(vendorId) ? "AND vmst.VENDOR_CODE like '%%'" : " AND vmst.VENDOR_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \r\n" + 
	    			"    PROJECT_CODE, hdr.PM_HDR_ID, vmst.VENDOR_CODE,\r\n" + 
	    			"    PROJECT_NAME,\r\n" + 
	    			"    vmst.VENDOR_NAME,\r\n" + 
	    			"    SUM(AMOUNT_PAYABLE) AS AMOUNT_PAYABLE, SUM(AMOUNT_DUE) as AMOUNT_DUE\r\n" + 
	    			"FROM\r\n" + 
	    			"    project_hdr hdr\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    indent_hdr ih ON ih.PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID\r\n" +
	    			"        INNER JOIN\r\n" + 
	    			"    po_hdr phdr ON ih.INDENT_ID = phdr.INDENT_ID\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    pra_hdr pra ON pra.PO_ID = phdr.PO_ID\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    vendor_mst vmst ON vmst.VENDOR_CODE = phdr.VENDOR_CODE\r\n" + 
	    			"        LEFT JOIN\r\n" + 
	    			"	document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\r\n" + 
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' AND team.ASSIGNED_EMP_ID = '"+empId+"' AND team.IS_ACTIVE = '1'  AND team.PM_ID ='"+pmId+"'\r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+vendor+"\r\n" + 
	    			"       "+date+"\r\n" +
	    			"group by vmst.VENDOR_NAME ";
	    	list = this.jdbcTemplate.query(Qry, new GetVendorDetailDrillDownRowMapper());
	    }catch(Exception ex) {
	    	logger.error("getVendorDetailView DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<VendorDetailDrillDownEntity> getVendorDetailDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId, String empId, String pmId) {
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "" , vendor = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' and '"+toDate+"' ";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		vendor = "getall".equalsIgnoreCase(vendorId) ? "AND vmst.VENDOR_CODE like '%%'" : " AND vmst.VENDOR_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \r\n" +
	    			"    PROJECT_CODE, hdr.PM_HDR_ID,\r\n" +
	    			"    PROJECT_NAME,\r\n" +
	    			"    vmst.VENDOR_NAME,\r\n" +
	    			"    phdr.PO_CODE,\r\n" +
	    			"    pra.PO_DATE, pra.PRA_CODE, pra.PRA_DATE,\r\n" +
	    			"    pra.AMOUNT_PAYABLE, pra.AMOUNT_DUE,\r\n" +
	    			"    COALESCE(mi.DC_NO, (SELECT DC_NO FROM material_inward_hdr WHERE PO_ID = pra.PO_ID AND TENANT_ID = '"+tenantId+"' ORDER BY MI_ID DESC LIMIT 1)) AS INVOICE_NO,\r\n" +
	    			"    COALESCE(mi.DC_DATE, (SELECT DC_DATE FROM material_inward_hdr WHERE PO_ID = pra.PO_ID AND TENANT_ID = '"+tenantId+"' ORDER BY MI_ID DESC LIMIT 1)) AS INVOICE_DATE,\r\n" +
	    			"    pra.DUE_DATE, (SELECT DOCUMENT_STATUS_TYPE_DESCRIPTION FROM pra_hdr hdr inner join document_status_type_code mst\r\n" +
	    			"    on hdr.STATUS_CODE = mst.DOCUMENT_STATUS_TYPE_CODE where hdr.PRA_ID = pra.PRA_ID) as STATUS,\r\n" +
	    			"    CASE WHEN pra.COMPLETED_DATETIME is not null THEN  DATEDIFF(DATE(pra.COMPLETED_DATETIME),DATE(pra.DUE_DATE))\r\n" +
	    			"	    ELSE (CASE WHEN CURDATE() < DATE(pra.DUE_DATE) THEN 0 ELSE DATEDIFF(CURDATE(), DATE(pra.DUE_DATE)) END)\r\n" +
	    			"	 END AS OVER_DUE \r\n" +
	    			"FROM\r\n" +
	    			"    project_hdr hdr\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    indent_hdr ih ON ih.PROJECT_ID = hdr.PM_HDR_ID\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    process_assigned_team team ON team.MASTER_ID = sh.SCM_HDR_ID\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    po_hdr phdr ON ih.INDENT_ID = phdr.INDENT_ID\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    pra_hdr pra ON pra.PO_ID = phdr.PO_ID and pra.TENANT_ID = '"+tenantId+"'\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    vendor_mst vmst ON vmst.VENDOR_CODE = phdr.VENDOR_CODE\r\n" +
	    			"        LEFT JOIN\r\n" +
	    			"    grn_hdr gh ON gh.GRN_HDR_ID = pra.GRN_HDR_ID\r\n" +
	    			"        LEFT JOIN\r\n" +
	    			"    material_inward_hdr mi ON mi.MI_ID = gh.MI_ID\r\n" +
	    			"        LEFT JOIN\r\n" +
	    			"	document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\r\n" +
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' AND team.ASSIGNED_EMP_ID = '"+empId+"' AND team.IS_ACTIVE = '1'  AND team.PM_ID ='"+pmId+"' \r\n" +
	    			"		"+pmHdr+"\r\n" +
	    			"       "+stage+"\r\n" +
	    			"       "+cust+"\r\n" +
	    			"       "+vendor+"\r\n" +
	    			"       "+date+"\r\n";
	    	list = this.jdbcTemplate.query(Qry, new GetVendorDetailDrillDownRowMapper());
	    }catch(Exception ex) {
	    	logger.error("getVendorDetailDrillDown DAO Error" + ex);	
	    }
		return list;
	}


}
