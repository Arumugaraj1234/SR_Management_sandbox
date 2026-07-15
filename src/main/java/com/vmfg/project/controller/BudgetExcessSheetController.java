package com.vmfg.project.controller;

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

import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.entity.BudgetExcessSheetEntity;
import com.vmfg.project.entity.SalesCategoryDtlEntity;
import com.vmfg.project.request.BudgetExcessSheetRequest;
import com.vmfg.project.request.BudgetExcessStatusDtlReq;
import com.vmfg.project.request.IndentBudgetDtlReq;
import com.vmfg.project.request.updateBudgetExcessSheetRequest;
import com.vmfg.project.service.interfaces.IBudgetExcessSheetService;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;

@Controller
@RequestMapping("/")
public class BudgetExcessSheetController {
	private static final Logger logger = LoggerFactory.getLogger(BudgetExcessSheetController.class);

	@Autowired
	IBudgetExcessSheetService iBudgetExcessSheetService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertBudgetExcessSheetDtl")
	public ResponseEntity<ResponseAsMessage> insertBudgetExcessSheetDtl(@RequestBody BudgetExcessSheetRequest budgetExcessSheetRequest) {

		logger.debug("insertBudgetExcessSheetDtl   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iBudgetExcessSheetService.insertBudgetExcessSheetDtl(budgetExcessSheetRequest);

		} catch (Exception ex) {
			logger.error("insertBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("insertBudgetExcessSheetDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateBudgetExcessSheetDtl")
	public ResponseEntity<ResponseAsMessage> updateBudgetExcessSheetDtl(@RequestBody updateBudgetExcessSheetRequest updateudget) {

		logger.debug("updateBudgetExcessSheetDtl   method Start");
		ResponseAsMessage resp = null;
		try {

			resp=iBudgetExcessSheetService.updateBudgetExcessSheetDtl(updateudget);

		} catch (Exception ex) {
			logger.error("updateBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("updateBudgetExcessSheetDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("retriveBudgetExcessSheetDtl")
	public ResponseEntity<List<BudgetExcessSheetEntity>> retriveBudgetExcessSheetDtl(@RequestBody BudgetExcessStatusDtlReq statusDtlReq) {

		logger.debug("retriveBudgetExcessSheetDtl   method Start");
		List<BudgetExcessSheetEntity> list = null;
		try {

			list=iBudgetExcessSheetService.retriveBudgetExcessSheetDtl(statusDtlReq);

		} catch (Exception ex) {
			logger.error("retriveBudgetExcessSheetDtl  method  exception" + ex);
		}
		logger.debug("retriveBudgetExcessSheetDtl   method end");
		return new ResponseEntity<List<BudgetExcessSheetEntity>>(list, HttpStatus.OK);
	}



	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentBudgetDtl")
	public ResponseEntity<List<SalesCategoryDtlEntity>> getIndentBudgetDtl(@RequestBody IndentBudgetDtlReq indentBudgetDtl) {

		logger.debug("getIndentBudgetDtl   method Start");
		List<SalesCategoryDtlEntity> list = null;
		try {

			list=iBudgetExcessSheetService.getIndentBudgetDtl(indentBudgetDtl);

		} catch (Exception ex) {
			logger.error("getIndentBudgetDtl  method  exception" + ex);
		}
		logger.debug("getIndentBudgetDtl   method end");
		return new ResponseEntity<List<SalesCategoryDtlEntity>>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateBudgetSheetExcessSeqAndStatus")
	public ResponseEntity<ResponseAsMessage> updateBudgetSheetExcessSeqAndStatus(@RequestBody UpdateSeqAndStatusRequest updateDtlsEntity) {
		logger.debug("updateBudgetSheetExcessSeqAndStatus   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iBudgetExcessSheetService.updateBudgetSheetExcessSeqAndStatus(updateDtlsEntity);

		} catch (Exception ex) {
			logger.error("updateBudgetSheetExcessSeqAndStatus  method  exception" + ex);
		}
		logger.debug("updateBudgetSheetExcessSeqAndStatus   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}


}
