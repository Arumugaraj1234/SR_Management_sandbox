package com.vmfg.sales.controller;

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

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.sales.entity.BudgetListEntity;
import com.vmfg.sales.request.DeleteBudgetSheetRequest;
import com.vmfg.sales.request.DeleteBudgetValue;
import com.vmfg.sales.request.SalesBudgetSheetHdrAndDtlRequest;
import com.vmfg.sales.request.SalesBudgetSheetRequest;
import com.vmfg.sales.request.getFileConfigDtlRequest;
import com.vmfg.sales.services.interfaces.ISalesBudgetSheetService;

@Controller
@RequestMapping("/")
public class SalesBudgetSheetController {
	private static final Logger logger = LoggerFactory.getLogger(SalesBudgetSheetController.class);

	@Autowired
	private ISalesBudgetSheetService iSalesBudgetSheetService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSalesBudgetSheetHdrAndDtl")
	public ResponseEntity<ResponseAsList> getSalesBudgetSheetHdrAndDtl(
			@RequestBody SalesBudgetSheetRequest salesBudgetSheetRequest) {
		logger.info("getSalesBudgetSheetHdrAndDtl   method Start");
		ResponseAsList list = null;
		try {

			list = iSalesBudgetSheetService.getSalesBudgetSheetHdrAndDtl(salesBudgetSheetRequest);
			logger.info("getSalesBudgetSheetHdrAndDtl method End");

		} catch (Exception ex) {
			logger.error("getSalesBudgetSheetHdrAndDtl Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertOrUpdateSalesBudgetSheetHdrAndDtl")
	public ResponseEntity<ResponseAsMessage> insertOrUpdateSalesBudgetSheetHdrAndDtl(
			@RequestBody List<SalesBudgetSheetHdrAndDtlRequest> salesBudgetSheetRequest) {
		logger.info("insertOrUpdateSalesBudgetSheetHdrAndDtl   method Start");
		ResponseAsMessage returnMsg = null;
		try {

			returnMsg = iSalesBudgetSheetService.insertOrUpdateSalesBudgetSheetHdrAndDtl(salesBudgetSheetRequest);
			logger.info("insertOrUpdateSalesBudgetSheetHdrAndDtl method End");

		} catch (Exception ex) {
			logger.error("insertOrUpdateSalesBudgetSheetHdrAndDtl Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(returnMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getKeyCategory")
	public ResponseEntity<ResponseAsList> getKeyCategory(@RequestBody getFileConfigDtlRequest request) {
		logger.info("getKeyCategory   method Start");
		ResponseAsList list = null;
		try {

			list = iSalesBudgetSheetService.getKeyCategory(request);
			logger.info("getKeyCategory method End");

		} catch (Exception ex) {
			logger.error("getKeyCategory Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteKeyCategory")
	public ResponseEntity<ResponseAsMessage> deleteKeyCategory(@RequestBody DeleteBudgetValue request) {
		logger.info("deleteKeyCategory   method Start");
		ResponseAsMessage list = null;
		try {

			list = iSalesBudgetSheetService.deleteKeyCategory(request);
			logger.info("deleteKeyCategory method End");

		} catch (Exception ex) {
			logger.error("deleteKeyCategory Method Exception" + ex);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("uploadBudgetSheetTemplate")
	public ResponseEntity<ResponseAsMessage> uploadBudgetSheetTemplate(@RequestBody BudgetListEntity uploadIndentReq)  {
		logger.info("uploadBudgetSheetTemplate Controller  method Start");
		ResponseAsMessage list = null;
		try {
			list = iSalesBudgetSheetService.uploadBudgetSheetTemplate(uploadIndentReq);
		} catch (Exception ex) {
			logger.error("uploadBudgetSheetTemplate Controller  method  exception:" + ex);
		}
		logger.info("uploadBudgetSheetTemplate Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getcriticalListByPmHdrId")
	public ResponseEntity<ResponseAsList> getcriticalListByPmHdrId(@RequestBody SalesBudgetSheetRequest salesBudgetSheetReq)  {
		logger.info("getcriticalListByPmHdrId Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iSalesBudgetSheetService.getcriticalListByPmHdrId(salesBudgetSheetReq);
		} catch (Exception ex) {
			logger.error("getcriticalListByPmHdrId Controller  method  exception:" + ex);
		}
		logger.info("getcriticalListByPmHdrId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteBudgetSheetCR")
	public ResponseEntity<ResponseAsMessage> deleteBudgetSheetCR(@RequestBody DeleteBudgetSheetRequest deleteBudgetSheetRequest)  {
		logger.info("deleteBudgetSheetCR Controller  method Start");
		ResponseAsMessage list = null;
		try {
			list = iSalesBudgetSheetService.deleteBudgetSheetCR(deleteBudgetSheetRequest);
		} catch (Exception ex) {
			logger.error("deleteBudgetSheetCR Controller  method  exception:" + ex);
		}
		logger.info("deleteBudgetSheetCR Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
}
