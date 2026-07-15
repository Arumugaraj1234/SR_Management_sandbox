package com.vmfg.general.controller;

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

import com.vmfg.assembly.request.GetAssyDtlRequest;
import com.vmfg.design.request.DesignRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.services.interfaces.ITileViewService;
import com.vmfg.project.request.ProjectHdrRequest;
import com.vmfg.quality.request.GetQtyDtlRequest;
import com.vmfg.sales.request.GetEnqDtlbyDateRequest;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;

@Controller
@RequestMapping("/")

public class TileViewController {

	private static final Logger logger = LoggerFactory.getLogger(StageManagementController.class);

	@Autowired
	ITileViewService iTileViewService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSaleTileView")
	public ResponseEntity<ResponseAsList> getSaleTileView(
			@RequestBody GetEnqDtlbyDateRequest getEnqDtlbyDateReq) {
		logger.debug("getSaleTileView  method Start");
		ResponseAsList list = null;
		try {
			list = iTileViewService.getSaleTileView(getEnqDtlbyDateReq);
		} catch (Exception e) {
			logger.debug("getSaleTileView methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDesignTitleView")
	public ResponseEntity<ResponseAsList> getDesignTitleView(@RequestBody DesignRequest designReq) {
		logger.debug("getDesignTitleView   method Start");
		ResponseAsList resp = null;
		try {

			resp = iTileViewService.getDesignTitleView(designReq);

		} catch (Exception ex) {
			logger.error("getDesignTitleView  method  exception" + ex);
		}
		logger.info("getDesignTitleView   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjectTitleView")
	public ResponseEntity<ResponseAsList> getProjectTitleView(@RequestBody ProjectHdrRequest tenReq) {
		logger.debug("getDesignTitleView   method Start");
		ResponseAsList resp = null;
		try {

			resp = iTileViewService.getProjectTitleView(tenReq);

		} catch (Exception ex) {
			logger.error("getProjectTitleView  method  exception" + ex);
		}
		logger.info("getProjectTitleView   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSCMTitleView")
	public ResponseEntity<ResponseAsList> getSCMTitleView(@RequestBody ScmHdrBasedDtlRequest scmHdrBasedDtl) {
		logger.debug("getSCMTitleView   method Start");
		ResponseAsList resp = null;
		try {

			resp = iTileViewService.getSCMTitleView(scmHdrBasedDtl);

		} catch (Exception ex) {
			logger.error("getSCMTitleView  method  exception" + ex);
		}
		logger.info("getSCMTitleView   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getFinanceTitleView")
	public ResponseEntity<ResponseAsList> getFinanceTitleView(@RequestBody DesignRequest designReq) {
		logger.debug("getFinanceTitleView   method Start");
		ResponseAsList resp = null;
		try {

			resp = iTileViewService.getFinanceTitleView(designReq);

		} catch (Exception ex) {
			logger.error("getFinanceTitleView  method  exception" + ex);
		}
		logger.info("getFinanceTitleView   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAssyTitleView")
	public ResponseEntity<ResponseAsList> getAssyTitleView(@RequestBody GetAssyDtlRequest getAssyDtlReq) {
		logger.debug("getAssyTitleView   method Start");
		ResponseAsList resp = null;
		try {

			resp = iTileViewService.getAssyTitleView(getAssyDtlReq);

		} catch (Exception ex) {
			logger.error("getAssyTitleView  method  exception" + ex);
		}
		logger.info("getAssyTitleView   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getQualityView")
	public ResponseEntity<ResponseAsList> getQualityView(@RequestBody GetQtyDtlRequest getQtyDtlReq) {
		logger.debug("getQualityView   method Start");
		ResponseAsList resp = null;
		try {

			resp = iTileViewService.getQualityView(getQtyDtlReq);

		} catch (Exception ex) {
			logger.error("getQualityView  method  exception" + ex);
		}
		logger.info("getQualityView   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
}
