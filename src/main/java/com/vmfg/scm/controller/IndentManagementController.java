package com.vmfg.scm.controller;

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

import com.vmfg.design.request.IndentDtlRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.scm.request.IndentGrpRetRequest;
import com.vmfg.scm.request.ProjectAssignEmpReq;
import com.vmfg.scm.request.ProjectDtlRequest;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;
import com.vmfg.scm.request.getIndentHdrRequest;
import com.vmfg.scm.services.interfaces.IIndentManagementService;

@Controller
@RequestMapping("/")
public class IndentManagementController {
	private static final Logger logger = LoggerFactory.getLogger(IndentManagementController.class);

	@Autowired
	private IIndentManagementService iIndentManagementService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentProjectDtlsByDate")
	public ResponseEntity<ResponseAsList> getIndentProjectDtlsByDate(@RequestBody ProjectDtlRequest ProjectDtlReq) {
		logger.info("getIndentProjectDtlsByDate Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentManagementService.getIndentProjectDtlsByDate(ProjectDtlReq);
		} catch (Exception ex) {
			logger.error("getIndentProjectDtlsByDate Controller  method  exception:" + ex);
		}
		logger.info("getIndentProjectDtlsByDate Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentProjectDtlsByEmployee")
	public ResponseEntity<ResponseAsList> getIndentProjectDtlsByEmployee(@RequestBody ProjectAssignEmpReq ProjectDtlReq) {
		logger.info("getIndentProjectDtlsByEmployee Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentManagementService.getIndentProjectDtlsByEmployee(ProjectDtlReq);
		} catch (Exception ex) {
			logger.error("getIndentProjectDtlsByEmployee Controller  method  exception:" + ex);
		}
		logger.info("getIndentProjectDtlsByEmployee Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("indentHdrDropDownByProjectCode")
	public ResponseEntity<ResponseAsList> indentHdrDropDownByProjectCode(@RequestBody getIndentHdrRequest projectIdRequest) {
		logger.info("indentHdrDropDownByProjectCode Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentManagementService.indentHdrDropDownByProjectCode(projectIdRequest);
		} catch (Exception ex) {
			logger.error("indentHdrDropDownByProjectCode Controller  method  exception:" + ex);
		}
		logger.info("indentHdrDropDownByProjectCode Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentHdrDtlsByIndentId")
	public ResponseEntity<ResponseAsList> getIndentHdrDtlsByIndentId(@RequestBody IndentDtlRequest IndentDtlReq) {
		logger.info("getIndentHdrDtlsByIndentId Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentManagementService.getIndentHdrDtlsByIndentId(IndentDtlReq);
		} catch (Exception ex) {
			logger.error("getIndentHdrDtlsByIndentId Controller  method  exception:" + ex);
		}
		logger.info("getIndentHdrDtlsByIndentId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}



	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentGrpNewProd")
	public ResponseEntity<ResponseAsList> getIndentGrpNewProd(@RequestBody IndentGrpRetRequest indentGrpReq) {
		logger.info("getIndentGrpNewProd Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentManagementService.getIndentGrpNewProd(indentGrpReq);
		} catch (Exception ex) {
			logger.error("getIndentGrpNewProd Controller  method  exception:" + ex);
		}
		logger.info("getIndentGrpNewProd Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getScmHdrBasedDtl")
	public ResponseEntity<ResponseAsList> getScmHdrBasedDtl(@RequestBody ScmHdrBasedDtlRequest scmHdrBasedDtl) {
		logger.info("getScmHdrBasedDtl   method Start");
		ResponseAsList list = null;
		try {
			list = iIndentManagementService.getScmHdrBasedDtl(scmHdrBasedDtl);
		} catch (Exception ex) {
			logger.error("getScmHdrBasedDtl   method  exception:" + ex);
		}
		logger.info("getScmHdrBasedDtl  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentProjectDtlsByDateAndIndent")
	public ResponseEntity<ResponseAsList> getIndentProjectDtlsByDateAndIndent(@RequestBody ProjectDtlRequest ProjectDtlReq) {
		logger.info("getIndentProjectDtlsByDateAndIndent Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentManagementService.getIndentProjectDtlsByDateAndIndent(ProjectDtlReq);
		} catch (Exception ex) {
			logger.error("getIndentProjectDtlsByDateAndIndent Controller  method  exception:" + ex);
		}
		logger.info("getIndentProjectDtlsByDateAndIndent Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
}
