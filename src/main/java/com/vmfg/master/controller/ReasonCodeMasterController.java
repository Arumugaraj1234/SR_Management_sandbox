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

import com.vmfg.master.entity.ReasonCodeMasterEntity;
import com.vmfg.master.request.ReasonCodeMasterRequest;
import com.vmfg.master.services.interfaces.IReasonCodeMasterService;

@Controller
@RequestMapping("/")
public class ReasonCodeMasterController {
	private static final Logger logger = LoggerFactory.getLogger(ReasonCodeMasterController.class);

	@Autowired
	IReasonCodeMasterService iReasonCodeMasterService;
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getReasonCodeInfo")
	public ResponseEntity<List<ReasonCodeMasterEntity>> getReasonCodeInfo(@RequestBody ReasonCodeMasterRequest scop) {
		logger.info("getReasonCodeInfoController   method Start");
		List<ReasonCodeMasterEntity> departmentInfoEntity=null;
		try {

			departmentInfoEntity = iReasonCodeMasterService.getReasonCodeInfo(scop);

		} catch (Exception ex) {
			logger.error("getReasonCodeInfoController  method  exception" + ex);
		}
		logger.debug("getReasonCodeInfoController   method end");
		return new ResponseEntity<List<ReasonCodeMasterEntity>>(departmentInfoEntity, HttpStatus.OK);
	}

}
