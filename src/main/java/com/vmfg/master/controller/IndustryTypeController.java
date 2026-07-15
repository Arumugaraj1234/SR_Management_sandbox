package com.vmfg.master.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vmfg.master.entity.IndustryTypeEntity;
import com.vmfg.master.request.IndustryTypeRequest;
import com.vmfg.master.services.interfaces.IIndustryTypeService;

@Controller
@RequestMapping("/")
public class IndustryTypeController {
	private static final Logger logger = LoggerFactory.getLogger(IndustryTypeController.class);

	@Autowired
	IIndustryTypeService iIndustryTypeService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndustryTypeInfo")
	public ResponseEntity<List<IndustryTypeEntity>> getIndustryTypeInfo(@RequestBody IndustryTypeRequest scop) {
		logger.info("getIndustryTypeInfoController   method Start");
		List<IndustryTypeEntity> departmentInfoEntity=null;
		try {

			departmentInfoEntity = iIndustryTypeService.getIndustryTypeInfo(scop);

		} catch (Exception ex) {
			logger.error("getIndustryTypeInfoController  method  exception" + ex);
		}
		logger.debug("getIndustryTypeInfoController   method end");
		return new ResponseEntity<List<IndustryTypeEntity>>(departmentInfoEntity, HttpStatus.OK);
	}

}
