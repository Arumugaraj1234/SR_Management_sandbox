package com.vmfg.util.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vmfg.util.entity.DocumentPropertyMst;
import com.vmfg.util.entity.TenantPropertyMst;
import com.vmfg.util.service.IGetPropertyValueService;

@Controller
@RequestMapping("/")
public class GetPropertyValueController {
	private static final Logger logger = LoggerFactory.getLogger(GetPropertyValueController.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IGetPropertyValueService iGetPropertyValueService;
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPropertyByTenantId")
	public ResponseEntity<List<TenantPropertyMst>> getPropertyByTenantIdMethod(@RequestParam("propertyDtl") String propertyDtl) {
		List<TenantPropertyMst> propValueMap = null;
		logger.info("getPropertyByTenantIdMethod method Start");
		try {
			JSONObject jsonObj = new JSONObject(propertyDtl);
			JSONArray getArray = jsonObj.getJSONArray("propertyItem");
			String MODULE_NAME = "";
			String TENANT_ID = "";
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				JSONArray keys = objects.names();

				for (int j = 0; j < keys.length(); ++j) {

					String key = keys.getString(j);
					String value = objects.getString(key);

					if (key.equalsIgnoreCase("MODULE_NAME")) {
						MODULE_NAME = value;
					}else if (key.equalsIgnoreCase("TENANT_ID")) {
						TENANT_ID = value;
					}
				}
			}

			propValueMap = iGetPropertyValueService.getPropValueByTenantByService(TENANT_ID,MODULE_NAME,jdbcTemplate);
			} catch (Exception ex) {
			logger.error(" getPropertyByTenantIdMethod Method Exception" + ex);

		}
		logger.info("getPropertyByTenantIdMethod method end");
		return new ResponseEntity<List<TenantPropertyMst>>(propValueMap, HttpStatus.OK);
	}
	
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDocPropertyByDoc")
	public ResponseEntity<List<DocumentPropertyMst>> getDocPropertyByDocMethod(@RequestParam("propertyDtl") String propertyDtl) {
		List<DocumentPropertyMst> propValueMap = null;
		logger.info("getDocPropertyByDocMethod method Start");
		try {
			JSONObject jsonObj = new JSONObject(propertyDtl);
			JSONArray getArray = jsonObj.getJSONArray("propertyItem");
			String MODULE_NAME = "";
			String TENANT_ID = "";
			String LINE_CODE = "";
			String PROGRAM_CODE = "";
			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				JSONArray keys = objects.names();

				for (int j = 0; j < keys.length(); ++j) {

					String key = keys.getString(j);
					String value = objects.getString(key);

					if (key.equalsIgnoreCase("DOCUMENT_STATUS_CODE")) {
						MODULE_NAME = value;
					}else if (key.equalsIgnoreCase("TENANT_ID")) {
						TENANT_ID = value;
					}else if (key.equalsIgnoreCase("LINE_CODE")) {
						LINE_CODE = value;
					}else if (key.equalsIgnoreCase("PROGRAM_CODE")) {
						PROGRAM_CODE = value;
					}
				}
			}

			propValueMap = iGetPropertyValueService.getDocPropValueByService(TENANT_ID, MODULE_NAME,LINE_CODE,PROGRAM_CODE, jdbcTemplate);
			} catch (Exception ex) {
			logger.error(" getDocPropertyByDocMethod Method Exception" + ex);

		}
		logger.info("getPropertyByTenantIdMethod method end");
		return new ResponseEntity<List<DocumentPropertyMst>>(propValueMap, HttpStatus.OK);
	}
	
	
	
	// TAKE TENANT PROPERTY VALUE 
	public  String getTenantPropertyValue(String PROPERTY_NAME) {
		logger.info("tenantPropertyValue method Start");
		String propertyValue="";
		try {
			
			 propertyValue=iGetPropertyValueService.getTenantPropertyVal(PROPERTY_NAME,jdbcTemplate);
	} catch (Exception ex) {
		logger.error(" getDocPropertyByDocMethod Method Exception" + ex);

	}
	logger.debug("tenantPropertyValue method end");
		return propertyValue;
		
	}
	
	
}
