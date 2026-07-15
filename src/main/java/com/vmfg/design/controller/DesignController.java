package com.vmfg.design.controller;

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

import com.vmfg.design.request.DeletedesignSubKeyAreaRequest;
import com.vmfg.design.request.DesignRequest;
import com.vmfg.design.request.GetKeySubAreaByPKIdRequest;
import com.vmfg.design.request.GetTasKTemplateHdrRequest;
import com.vmfg.design.request.ProductBasedInventoryDtlRequest;
import com.vmfg.design.request.ProductDtlDropDownRequest;
import com.vmfg.design.request.UpdatedesignSubKeyAreaRequest;
import com.vmfg.design.request.getPoDetailByIndentDtlRequest;
import com.vmfg.design.services.interfaces.IDesignService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

@Controller
@RequestMapping("/")
public class DesignController {
	private static final Logger logger = LoggerFactory.getLogger(DesignController.class);

	@Autowired
	IDesignService iDesignService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDesignDtl")
	public ResponseEntity<ResponseAsList> getDesignHdr(@RequestBody DesignRequest designReq) {
		logger.info("getDesignDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getDesignHdr(designReq);

		} catch (Exception ex) {
			logger.error("getDesignDtl  method  exception" + ex);
		}
		logger.info("getDesignDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getKeyArea")
	public ResponseEntity<ResponseAsList> getKeyArea(@RequestBody ProductDtlDropDownRequest tentReq) {
		logger.debug("getKeyArea   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getKeyArea(tentReq);

		} catch (Exception ex) {
			logger.error("getKeyArea  method  exception" + ex);
		}
		logger.debug("getKeyArea   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getKeySubArea")
	public ResponseEntity<ResponseAsList> getKeySubArea(@RequestBody ProductDtlDropDownRequest tentReq) {
		logger.debug("getKeyArea   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getKeySubArea(tentReq);

		} catch (Exception ex) {
			logger.error("getKeySubArea  method  exception" + ex);
		}
		logger.debug("getKeySubArea   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getKeySubAreaDtl")
	public ResponseEntity<ResponseAsList> getKeySubAreaDtl(@RequestBody ProductDtlDropDownRequest tentReq) {
		logger.debug("getKeySubAreaDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getKeySubAreaDtl(tentReq);

		} catch (Exception ex) {
			logger.error("getKeySubAreaDtl  method  exception" + ex);
		}
		logger.debug("getKeySubAreaDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getKeySubAreaByPKId")
	public ResponseEntity<ResponseAsList> getKeySubAreaByPKId(
			@RequestBody GetKeySubAreaByPKIdRequest getKeySubAreaByPKIdReq) {
		logger.debug("getKeySubAreaByPKId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getKeySubAreaByPKId(getKeySubAreaByPKIdReq);

		} catch (Exception ex) {
			logger.error("getKeySubAreaByPKId  method  exception" + ex);
		}
		logger.debug("getKeySubAreaByPKId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllProductsByPmHdrId")
	public ResponseEntity<ResponseAsList> getAllProductsByPmHdrId(
			@RequestBody ProductDtlDropDownRequest ProductDtlDropDownReq) {
		logger.debug("getAllProductsByPmHdrId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getAllProductsByPmHdrId(ProductDtlDropDownReq);

		} catch (Exception ex) {
			logger.error("getAllProductsByPmHdrId  method  exception" + ex);
		}
		logger.debug("getAllProductsByPmHdrId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllProductsDtl")
	public ResponseEntity<ResponseAsList> getAllProductsDtl(
			@RequestBody ProductDtlDropDownRequest ProductDtlDropDownReq) {
		logger.debug("getAllProductsDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getAllProductsDtl(ProductDtlDropDownReq);

		} catch (Exception ex) {
			logger.error("getAllProductsDtl  method  exception" + ex);
		}
		logger.debug("getAllProductsDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPoDetailByIndentDtl")
	public ResponseEntity<ResponseAsList> getPoDetailByIndentDtl(@RequestBody getPoDetailByIndentDtlRequest IndentDtlIdReq) {
		logger.debug("getPoDetailByIndentDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getPoDetailByIndentDtl(IndentDtlIdReq);

		} catch (Exception ex) {
			logger.error("getPoDetailByIndentDtl  method  exception" + ex);
		}
		logger.debug("getIndentDtlId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updatedesignSubKeyArea")
	public ResponseEntity<ResponseAsMessage> updatedesignSubKeyArea(
			@RequestBody List<UpdatedesignSubKeyAreaRequest> updatedesignSubKeyAreaReq) {
		logger.debug("updatedesignSubKeyArea   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iDesignService.updatedesignSubKeyArea(updatedesignSubKeyAreaReq);

		} catch (Exception ex) {
			logger.error("updatedesignSubKeyArea  method  exception" + ex);
		}
		logger.debug("updatedesignSubKeyArea   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deletedesignSubKeyArea")
	public ResponseEntity<ResponseAsMessage> deletedesignSubKeyArea(
			@RequestBody DeletedesignSubKeyAreaRequest deletedesignSubKeyAreaReq) {
		logger.debug("deletedesignSubKeyArea   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iDesignService.deletedesignSubKeyArea(deletedesignSubKeyAreaReq);

		} catch (Exception ex) {
			logger.error("deletedesignSubKeyArea  method  exception" + ex);
		}
		logger.debug("deletedesignSubKeyArea   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProductBasedInventoryDtl")
	public ResponseEntity<ResponseAsList> getProductBasedInventoryDtl(
			@RequestBody ProductBasedInventoryDtlRequest inventoryDtl) {
		logger.debug("getProductBasedInventoryDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getProductBasedInventoryDtl(inventoryDtl);

		} catch (Exception ex) {
			logger.error("getProductBasedInventoryDtl  method  exception" + ex);
		}
		logger.debug("getProductBasedInventoryDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProductBasedPoDtl")
	public ResponseEntity<ResponseAsList> getProductBasedPoDtl(
			@RequestBody ProductBasedInventoryDtlRequest inventoryDtl) {
		logger.debug("getProductBasedPoDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getProductBasedPoDtl(inventoryDtl);

		} catch (Exception ex) {
			logger.error("getProductBasedPoDtl  method  exception" + ex);
		}
		logger.debug("getProductBasedPoDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTasKTemplateHdr")
	public ResponseEntity<ResponseAsList> getTasKTemplateHdr(
			@RequestBody GetTasKTemplateHdrRequest getTasKTemplateHdrReq) {
		logger.debug("getTasKTemplateHdr   method Start");
		ResponseAsList resp = null;
		try {

			resp = iDesignService.getTasKTemplateHdr(getTasKTemplateHdrReq);

		} catch (Exception ex) {
			logger.error("getTasKTemplateHdr  method  exception" + ex);
		}
		logger.debug("getTasKTemplateHdr   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

}
