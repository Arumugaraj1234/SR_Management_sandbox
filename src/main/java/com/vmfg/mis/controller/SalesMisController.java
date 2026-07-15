package com.vmfg.mis.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.mis.request.MisFromDateToDateRequest;
import com.vmfg.mis.services.interfaces.ISalesMisService;

@Controller
@RequestMapping("/")
@RestController
public class SalesMisController {
	private static final Logger logger = LoggerFactory.getLogger(SalesMisController.class);

	@Autowired
	ISalesMisService iSalesMisService;
	

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSalesOrderDetails")
	public ResponseEntity<ResponseAsList> getSalesOrderDetails(@RequestBody MisFromDateToDateRequest misFromDateToDateReq) {

		logger.debug("getSalesOrderDetails   method Start");
		ResponseAsList resp = null;
		try {

			resp = iSalesMisService.getSalesOrderDetails(misFromDateToDateReq);

		} catch (Exception ex) {
			logger.error("getSalesOrderDetails  method  exception" + ex);
		}
		logger.debug("getSalesOrderDetails   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSalesStageDtl")
	public ResponseEntity<ResponseAsList> getSalesStageDtl(@RequestBody MisFromDateToDateRequest misFromDateToDateReq) {

		logger.debug("getSalesStageDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iSalesMisService.getSalesStageDtl(misFromDateToDateReq);

		} catch (Exception ex) {
			logger.error("getSalesStageDtl  method  exception" + ex);
		}
		logger.debug("getSalesStageDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getCustomerOrderDtl")
	public ResponseEntity<ResponseAsList> getCustomerOrderDtl(@RequestBody MisFromDateToDateRequest misFromDateToDateReq) {

		logger.debug("getCustomerOrderDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iSalesMisService.getCustomerOrderDtl(misFromDateToDateReq);

		} catch (Exception ex) {
			logger.error("getCustomerOrderDtl  method  exception" + ex);
		}
		logger.debug("getCustomerOrderDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSalesConvRatio")
	public ResponseEntity<ResponseAsList> getSalesConvRatio(@RequestBody MisFromDateToDateRequest misFromDateToDateReq) {

		logger.debug("getSalesConvRatio   method Start");
		ResponseAsList resp = null;
		try {

			resp = iSalesMisService.getSalesConvRatio(misFromDateToDateReq);

		} catch (Exception ex) {
			logger.error("getSalesConvRatio  method  exception" + ex);
		}
		logger.debug("getSalesConvRatio   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSalesContDtl")
	public ResponseEntity<ResponseAsList> getSalesContDtl(@RequestBody MisFromDateToDateRequest misFromDateToDateReq) {

		logger.debug("getSalesContDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iSalesMisService.getSalesContDtl(misFromDateToDateReq);

		} catch (Exception ex) {
			logger.error("getSalesContDtl  method  exception" + ex);
		}
		logger.debug("getSalesContDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSalesContProjects")
	public ResponseEntity<ResponseAsList> getSalesContProjects(@RequestBody MisFromDateToDateRequest misFromDateToDateRequest ) {
		logger.debug("getSalesContProjects  method Start");
	ResponseAsList list = null;
		try {
			list = iSalesMisService.getSalesContProjects(misFromDateToDateRequest);
		} catch (Exception e) {
			logger.debug("getSalesContProjects methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSalesDeptEmployees")
	public ResponseEntity<ResponseAsList> getSalesDeptEmployees(@RequestBody MisFromDateToDateRequest misFromDateToDateRequest) {
	    logger.debug("getSalesDeptEmployees method Start");
	    ResponseAsList list = null;
	    try {
	        list = iSalesMisService.getSalesDeptEmployees(misFromDateToDateRequest);
	    } catch (Exception e) {
	        logger.error("getSalesDeptEmployees method exception " + e);
	    }
	    return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
}


    
