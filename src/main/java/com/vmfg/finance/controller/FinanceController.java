package com.vmfg.finance.controller;

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

import com.vmfg.design.request.DesignRequest;
import com.vmfg.finance.services.interfaces.IFinanceService;
import com.vmfg.general.response.ResponseAsList;

@Controller
@RequestMapping("/")
public class FinanceController {
	private static final Logger logger = LoggerFactory.getLogger(FinanceController.class);

	@Autowired
	private IFinanceService iFinanceService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getFinanceDtl")
	public ResponseEntity<ResponseAsList> getFinanceDtl(@RequestBody DesignRequest designReq) {
		logger.info("getFinanceDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iFinanceService.getFinanceDtl(designReq);

		} catch (Exception ex) {
			logger.error("getFinanceDtl  method  exception" + ex);
		}
		logger.info("getFinanceDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
}
