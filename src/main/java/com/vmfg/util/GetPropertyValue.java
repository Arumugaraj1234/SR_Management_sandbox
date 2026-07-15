package com.vmfg.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.util.dao.IGetPropertyValueDAO;
import com.vmfg.util.entity.DocumentPropertyMst;
import com.vmfg.util.entity.DocumentPropertyMstRowMapper;
import com.vmfg.util.entity.TenantPropertyMst;
import com.vmfg.util.entity.TenantPropertyMstRowMapper;

@Transactional
@Repository
public class GetPropertyValue implements IGetPropertyValueDAO {
	private static final Logger logger = LoggerFactory.getLogger(GetPropertyValue.class);

	public static int getPropValueCountCheck(String propertyName,String tenantId, JdbcTemplate jdbcTemplate) {
		int propertyValue = 0;
		try {
			String qty = "select COUNT(*) AS PROPERTY_VALUE from tenant_property_mst where PROPERTY_NAME = ? and IS_ACTIVE = '1' and TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qty,propertyName,tenantId);
			String propValue = resultMap.get("PROPERTY_VALUE").toString();
			propertyValue =Integer.parseInt(propValue);
		} catch (Exception e) {
			logger.error("getPropValueCountCheck Method Exception|||" + e);
		}
		return propertyValue;
	}
	public static String getPropValue(String propertyName,String tenantId, JdbcTemplate jdbcTemplate) {
		String propertyValue = "";
		try {
			String qty = "select PROPERTY_VALUE from tenant_property_mst where PROPERTY_NAME = ? and IS_ACTIVE = '1' and TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qty,propertyName,tenantId);
			String propValue = resultMap.get("PROPERTY_VALUE").toString();
			propertyValue = (propValue != null ? propValue : "");
		} catch (Exception e) {
			logger.error("getPropValueByTenant Method Exception|||" + e);
		}
		return propertyValue;
	}

	public static String getPropValueByTenant(String tenantId, String propertyName, JdbcTemplate jdbcTemplate) {
		String propertyValue = "";
		try {
			String qty = "select PROPERTY_VALUE from tenant_property_mst where TENANT_ID = ? and PROPERTY_NAME = ? and IS_ACTIVE = '1'";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qty,tenantId,propertyName);
			String propValue = resultMap.get("PROPERTY_VALUE").toString();
			propertyValue = (propValue != null ? propValue : "");
		} catch (Exception e) {
			logger.error("getPropValueByTenant Method Exception|||" + e);
		}
		return propertyValue;
	}

	public static String getPropValueByTenant(String tenantId, String branchCode, String propertyName,
			JdbcTemplate jdbcTemplate) {
		String propertyValue = "";
		try {
			String qty = "select PROPERTY_VALUE from tenant_property_mst where TENANT_ID = ? and BRANCH_CODE = ? and  PROPERTY_NAME = ? and IS_ACTIVE = '1'";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qty,tenantId,branchCode,propertyName);
			String propValue = resultMap.get("PROPERTY_VALUE").toString();
			propertyValue = (propValue != null ? propValue : "");

		} catch (Exception e) {
			logger.error("getPropValueByTenant Method Exception|||" + e);
		}
		return propertyValue;
	}

	@Override
	public List<TenantPropertyMst> getPropValueByTenantByService(String tENANT_ID, String MODULE_NAME,
			JdbcTemplate jdbcTemplateparam) {
		List<TenantPropertyMst> propertyValueMap = null;
		try {
			String qty = "select PROPERTY_NAME,PROPERTY_VALUE from tenant_property_mst where TENANT_ID = ? and PROPERTY_CATEGORY = ? and IS_ACTIVE = '1'";
			RowMapper<TenantPropertyMst> rowMapper = new TenantPropertyMstRowMapper();
			propertyValueMap = jdbcTemplateparam.query(qty, rowMapper, tENANT_ID, MODULE_NAME);
		} catch (Exception e) {
			logger.error("getPropValueByTenant Method Exception|||" + e);
		}
		return propertyValueMap;
	}

	@Override
	public List<DocumentPropertyMst> getDocPropValueByService(String tENANT_ID, String MODULE_NAME, String LINE_CODE,
			String PROGRAM_CODE, JdbcTemplate jdbcTemplateparam) {
		List<DocumentPropertyMst> propertyValueMap = null;
		try {
			if(LINE_CODE.equalsIgnoreCase("") || LINE_CODE == null || LINE_CODE.equalsIgnoreCase("getall") || LINE_CODE.equalsIgnoreCase("<--Select-->")) {
				List<TenantPropertyMst> propertyValuelinecode = null;
				String qty = "select PROPERTY_NAME,PROPERTY_VALUE from tenant_property_mst where TENANT_ID = ? and PROPERTY_NAME = ? and IS_ACTIVE = '1'";
				RowMapper<TenantPropertyMst> rowMapper = new TenantPropertyMstRowMapper();
				propertyValuelinecode = jdbcTemplateparam.query(qty, rowMapper, tENANT_ID, "BIOMETRIC_LINE");
				
				//LINE_CODE = VmfgComProperties.defaultLineCode;
				LINE_CODE = propertyValuelinecode.get(0).getPropertyValue();
				
			}
			if(PROGRAM_CODE.equalsIgnoreCase("") || PROGRAM_CODE == null || PROGRAM_CODE.equalsIgnoreCase("getall") || PROGRAM_CODE.equalsIgnoreCase("<--Select-->")) {
				//PROGRAM_CODE = VmfgComProperties.defaultProgramCode;
				
				List<TenantPropertyMst> propertyValueprogracode = null;
				String qty = "select PROPERTY_NAME,PROPERTY_VALUE from tenant_property_mst where TENANT_ID = ? and PROPERTY_NAME = ? and IS_ACTIVE = '1'";
				RowMapper<TenantPropertyMst> rowMapper = new TenantPropertyMstRowMapper();
				propertyValueprogracode = jdbcTemplateparam.query(qty, rowMapper, tENANT_ID, "DEAULT_PROGRAM_CODE");
				
				PROGRAM_CODE = propertyValueprogracode.get(0).getPropertyValue();
			}
			logger.info(LINE_CODE+"-"+PROGRAM_CODE);
			String qty = "SELECT \r\n" + "    dlm.SEQUENCE,\r\n" + "    dlm.APPROVING_DEPT_CODE,\r\n"
					+ "    dlm.APPROVING_DESIGNATION_CODE,\r\n" + "    dlm.DOCUMENT_STATUS_TYPE_CODE,\r\n"
					+ "    dsc.DOCUMENT_STATUS_TYPE_DESCRIPTION,dlm.IS_NOTIFICATION_REQ,dlm.DOCUMENT_LIFECYCLE_MST_ID  FROM\r\n"
					+ "    document_lifecycle_mst dlm\r\n" + "        INNER JOIN\r\n"
					+ "    document_type_mst dtm ON dtm.DOCUMENT_TYPE_CODE = dlm.DOCUMENT_TYPE_CODE\r\n"
					+ "    inner join document_status_type_code dsc on dsc.DOCUMENT_STATUS_TYPE_CODE=dlm.DOCUMENT_STATUS_TYPE_CODE    INNER JOIN\r\n" + 
					"    document_lifecycle_dtl dld ON dld.DOCUMENT_LIFECYCLE_MST_ID = dlm.DOCUMENT_LIFECYCLE_MST_ID\r\n"
					+ "WHERE\r\n" + "    dlm.TENANT_ID = ?\r\n" + "        AND dlm.DOCUMENT_TYPE_CODE = ?\r\n"
					+ "        AND dlm.IS_ACTIVE = ? AND dld.LINE_MST_CODE = ? \r\n" + "        AND dld.PROGRAM_CODE = ?"
					+ "ORDER BY dlm.SEQUENCE; ";
			RowMapper<DocumentPropertyMst> rowMapper = new DocumentPropertyMstRowMapper();
			propertyValueMap = jdbcTemplateparam.query(qty, rowMapper, tENANT_ID, MODULE_NAME, 1, LINE_CODE,
					PROGRAM_CODE);
			if (propertyValueMap != null && propertyValueMap.size() > 0) {
				for (int i = 0; i < propertyValueMap.size(); i++) {

					String getDtlEmpId = "SELECT \r\n" + "   group_concat(EMPLOYEE_ID,',') as EMPLOYEE_ID\r\n"
							+ "FROM\r\n" + "    document_lifecycle_mst dlm\r\n" + "        INNER JOIN\r\n"
							+ "    document_lifecycle_dtl dld ON dld.DOCUMENT_LIFECYCLE_MST_ID = dlm.DOCUMENT_LIFECYCLE_MST_ID\r\n"
							+ "WHERE\r\n" + "   dld.DOCUMENT_LIFECYCLE_MST_ID = ?\r\n" + "   AND dld.LINE_MST_CODE = ?\r\n"
							+ "        AND dld.PROGRAM_CODE =?\r\n" + "        AND dlm.TENANT_ID =? AND dlm.TENANT_ID = dld.TENANT_ID" ;
					
					Map<String, Object> resultMap = jdbcTemplateparam.queryForMap(getDtlEmpId,propertyValueMap.get(i).getDocLifeCycMstId(),LINE_CODE,PROGRAM_CODE,tENANT_ID);
					String approvingEmpId = resultMap.get("EMPLOYEE_ID").toString();
					propertyValueMap.get(i).setApprovingEmpId(approvingEmpId);
				}
			}
		} catch (Exception e) {
			logger.error("getPropValueByTenant Method Exception|||" + e);
		}
		return propertyValueMap;
	}

	@Override
	public String getTenantPropertyVal(String pROPERTY_NAME,JdbcTemplate jdbcTemplate) {
		String path="";
		try {
		String pathQ = "select PROPERTY_VALUE from tenant_property_mst where PROPERTY_NAME=? and IS_ACTIVE=1\r\n";
		Map<String, Object> resultMap = jdbcTemplate.queryForMap(pathQ,pROPERTY_NAME);
		path = resultMap.get("PROPERTY_VALUE").toString();
	} catch (Exception e) {
		logger.error("getTenantPropertyVal Method Exception|||" + e);
	}
		return path;
	}

	@Override
	public String getTenantPropertyValByTenantId(String pROPERTY_NAME, String tenantId, JdbcTemplate jdbcTemplate) {
		String path="";
		try {
		String pathQ = "select PROPERTY_VALUE from tenant_property_mst where PROPERTY_NAME=? and TENANT_ID=? and IS_ACTIVE=1\r\n";
		Map<String, Object> resultMap = jdbcTemplate.queryForMap(pathQ,pROPERTY_NAME,tenantId);
		path = resultMap.get("PROPERTY_VALUE").toString();
	} catch (Exception e) {
		logger.error("getTenantPropertyValByTenantId Method Exception|||" + e);
	}
		return path;
	}
	
	

}
