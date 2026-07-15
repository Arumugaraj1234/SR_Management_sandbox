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

import com.vmfg.master.entity.ScopOfWorkEntity;
import com.vmfg.master.request.ScopeOfWorkRequest;
import com.vmfg.master.services.interfaces.IScopeOfWorkService;

@Controller
@RequestMapping("/")
public class ScopeOfWorkController {
	private static final Logger logger = LoggerFactory.getLogger(ScopeOfWorkController.class);

	@Autowired
	IScopeOfWorkService iScopeOfWorkService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getScopeOfWorkInfo")
	public ResponseEntity<List<ScopOfWorkEntity>> getScopeOfWorkInfo(@RequestBody ScopeOfWorkRequest scop) {
		logger.info("getScopeOfWorkInfoController   method Start");
		List<ScopOfWorkEntity> departmentInfoEntity=null;
		try {

			departmentInfoEntity = iScopeOfWorkService.getScopeOfWorkInfo(scop);

		} catch (Exception ex) {
			logger.error("getScopeOfWorkInfoController  method  exception" + ex);
		}
		logger.debug("getScopeOfWorkInfoController   method end");
		return new ResponseEntity<List<ScopOfWorkEntity>>(departmentInfoEntity, HttpStatus.OK);
	}

}
