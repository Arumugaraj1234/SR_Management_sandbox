package com.vmfg.util.controller;

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

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.scm.entity.DocLifeCycleLogRequest;
import com.vmfg.util.entity.DocLifeCycleMstLogEntity;
import com.vmfg.util.entity.DocumentLifeCycleInsertRequest;
import com.vmfg.util.service.IDocumentLifeCycleService;

@Controller
@RequestMapping("/")
public class DocumentLifeCycleController {
	private static final Logger logger = LoggerFactory.getLogger(DocumentLifeCycleController.class);

	@Autowired
	private IDocumentLifeCycleService iDocumentLifeCycleService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDocTypes")
	public ResponseEntity<ResponseAsList> getDocTypes(@RequestBody DocLifeCycleLogRequest docLifeCycleLogReq) {
		
		logger.info("getDocTypes Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iDocumentLifeCycleService.getDocTypes(docLifeCycleLogReq);
		}catch(Exception e) {
			logger.error("getDocTypes Controller  method  exception:" + e);
		}
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDocGroups")
	public ResponseEntity<ResponseAsList> getDocGroups(@RequestBody DocLifeCycleLogRequest docLifeCycleLogReq) {
		
		logger.info("getDocGroups Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iDocumentLifeCycleService.getDocGroups(docLifeCycleLogReq);
		}catch(Exception e) {
			logger.error("getDocGroups Controller  method  exception:" + e);
		}
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDocStatusTypes")
	public ResponseEntity<ResponseAsList> getDocStatusTypes(@RequestBody PmHdrIdAndTenantIdRequest tenanttreq) {
		
		logger.info("getDocStatusTypes Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iDocumentLifeCycleService.getDocStatusTypes(tenanttreq);
		}catch(Exception e) {
			logger.error("getDocStatusTypes Controller  method  exception:" + e);
		}
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDocTypesDataList")
	public ResponseEntity<ResponseAsList> getDocTypesDataList(@RequestBody DocLifeCycleLogRequest docReq) {
		
		logger.info("getDocTypesDataList Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iDocumentLifeCycleService.getDocTypesDataList(docReq);
		}catch(Exception e) {
			logger.error("getDocTypesDataList Controller  method  exception:" + e);
		}
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertOrUpdateDoclifeCycle")
	public ResponseEntity<ResponseAsMessage> insertOrUpdateDoclifeCycle(@RequestBody DocumentLifeCycleInsertRequest docList) {
		logger.debug("insertOrUpdateDoclifeCycle   method Start");
		ResponseAsMessage respMsg = null;
		try {
			respMsg = iDocumentLifeCycleService.insertOrUpdateDoclifeCycle(docList);
		} catch (Exception ex) {
			logger.error("insertOrUpdateDoclifeCycle  method  exception" + ex);
		}
		logger.debug("insertOrUpdateDoclifeCycle   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteDocList")
	public ResponseEntity<ResponseAsMessage> deleteDocList(@RequestBody PmHdrIdAndTenantIdRequest docList) {
		logger.debug("deleteDocList   method Start");
		ResponseAsMessage respMsg = null;
		try {
			respMsg = iDocumentLifeCycleService.deleteDocList(docList);
		} catch (Exception ex) {
			logger.error("deleteDocList  method  exception" + ex);
		}
		logger.debug("deleteDocList   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getEmpDesignationList")
	public ResponseEntity<ResponseAsList> getEmpDesignationList(@RequestBody TenantRequest tenReq) {
		
		logger.info("getEmpDesignationList Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iDocumentLifeCycleService.getEmpDesignationList(tenReq);
		}catch(Exception e) {
			logger.error("getEmpDesignationList Controller  method  exception:" + e);
		}
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPmIdList")
	public ResponseEntity<ResponseAsList> getPmIdList(@RequestBody TenantRequest tenReq) {
		
		logger.info("getPmIdList Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iDocumentLifeCycleService.getPmIdList(tenReq);
		}catch(Exception e) {
			logger.error("getPmIdList Controller  method  exception:" + e);
		}
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getLifeCylceMstVersionDtls")
	public ResponseEntity<ResponseAsList> getLifeCylceMstVersionDtls(@RequestBody DocLifeCycleLogRequest docLifeCycleLogReq) {
		
		logger.info("getLifeCylceMstVersionDtls Controller  method Start");
		ResponseAsList resp = new ResponseAsList();
		try {
			resp=iDocumentLifeCycleService.getLifeCylceMstVersionDtls(docLifeCycleLogReq);
		}catch(Exception e) {
			logger.error("getLifeCylceMstVersionDtls Controller  method  exception:" + e);
		}
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
		
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateDocLifeCycleVersion")
	public ResponseEntity<ResponseAsMessage> updateDocLifeCycleVersion(@RequestBody List<DocLifeCycleMstLogEntity>  docLifeCycleMstReq) {
		logger.debug("updateDocLifeCycleVersion   method Start");
		ResponseAsMessage respMsg = null;
		try {
			respMsg = iDocumentLifeCycleService.updateDocLifeCycleVersion(docLifeCycleMstReq);
		} catch (Exception ex) {
			logger.error("updateDocLifeCycleVersion  method  exception" + ex);
		}
		logger.debug("updateDocLifeCycleVersion   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("setDefaultDoc")
	public ResponseEntity<ResponseAsMessage> setDefaultDoc(@RequestBody List<DocLifeCycleMstLogEntity>  docLifeCycleMstReq) {
		logger.debug("setDefaultDoc  method Start");
		ResponseAsMessage respMsg = null;
		try {
			respMsg = iDocumentLifeCycleService.setDefaultDoc(docLifeCycleMstReq);
		} catch (Exception ex) {
			logger.error("setDefaultDoc  method  exception" + ex);
		}
		logger.debug("setDefaultDoc method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	
	
	
}
