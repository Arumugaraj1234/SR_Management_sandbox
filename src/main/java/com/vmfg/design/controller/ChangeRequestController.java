package com.vmfg.design.controller;

import com.vmfg.design.entity.IndentPartDetailsEntity;
import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.entity.ChangeRequestDtlEntity;
import com.vmfg.design.entity.GetKeyIndentDtRequest;
import com.vmfg.design.request.GetChangeRequestDtlByPmIdRequest;
import com.vmfg.design.request.GetKeyAreaDtlsRequest;
import com.vmfg.design.request.GetKeyIndentDtl;
import com.vmfg.design.request.UpdateChangeRequestDtlRequest;
import com.vmfg.design.request.UpdateHdrSeqAndStatusRequest;
import com.vmfg.design.services.interfaces.IChangeRequestService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

import java.util.Map;

@Controller
@RequestMapping("/")
public class ChangeRequestController {

	private static final Logger logger = LoggerFactory.getLogger(IndentUploadController.class);

	@Autowired
	private IChangeRequestService iChangeRequestService;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateChangeRequestDtl")
	public ResponseEntity<ResponseAsMessage> updateChangeRequestDtl(@RequestBody UpdateChangeRequestDtlRequest updateChangeRequestDtlReq) {
		logger.info("updateChangeRequestDtl Controller  method Start");
		ResponseAsMessage list = null;
		try {
			list = iChangeRequestService.updateChangeRequestDtl(updateChangeRequestDtlReq);
		} catch (Exception ex) {
			logger.error("updateChangeRequestDtl Controller  method  exception:" + ex);
		}
		logger.info("updateChangeRequestDtl Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getChangeRequestDtlByPmId")
	public ResponseEntity<ResponseAsList> getChangeRequestDtlByPmId(@RequestBody GetChangeRequestDtlByPmIdRequest getChangeRequestDtlByPmIdReq) {
		logger.debug("getChangeRequestDtlByPmId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iChangeRequestService.getChangeRequestDtlByPmId(getChangeRequestDtlByPmIdReq);

		} catch (Exception ex) {
			logger.error("getChangeRequestDtlByPmId  method  exception" + ex);
		}
		logger.debug("getChangeRequestDtlByPmId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateDesignerComments")
	public ResponseEntity<ResponseAsMessage> updateDesignerComments(@RequestBody ChangeRequestDtlEntity ChangeRequestDtl) {
		logger.info("updateDesignerComments Controller  method Start");
		ResponseAsMessage list = null;
		try {
			list = iChangeRequestService.updateDesignerComments(ChangeRequestDtl);
		} catch (Exception ex) {
			logger.error("updateDesignerComments Controller  method  exception:" + ex);
		}
		logger.info("updateDesignerComments Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateChangeReqHdrSeqAndStatus")
	public ResponseEntity<ResponseAsMessage> updateChangeReqHdrSeqAndStatus(@RequestBody UpdateHdrSeqAndStatusRequest UpdateHdrSeqAndStatusReq) {
		logger.info("updateChangeReqHdrSeqAndStatus Controller  method Start");
		ResponseAsMessage list = null;
		try {
			list = iChangeRequestService.updateChangeReqHdrSeqAndStatus(UpdateHdrSeqAndStatusReq);
		} catch (Exception ex) {
			logger.error("updateChangeReqHdrSeqAndStatus Controller  method  exception:" + ex);
		}
		logger.info("updateChangeReqHdrSeqAndStatus Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertChangeRequestFile")
	public ResponseEntity<ResponseAsMessage> insertChangeRequestFile(@RequestParam("insertDocRequest") String insertDocReq,@RequestParam("file") MultipartFile file ) {
		logger.debug("insertChangeRequestFile  method Start");
	ResponseAsMessage list = null;
		try {
			JSONObject jsonObj = new JSONObject(insertDocReq);
			list = iChangeRequestService.insertChangeRequestFile(jsonObj,file);
		} catch (Exception e) {
			logger.debug("insertChangeRequestFile methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateFileByDmId")
	public ResponseEntity<ResponseAsMessage> updateFileByDmId(@RequestParam("updateDocRequest") String updateDocReq,@RequestParam("file") MultipartFile file ) {
		logger.debug("updateFileByDmId  method Start");
	ResponseAsMessage list = null;
		try {
			JSONObject jsonObj = new JSONObject(updateDocReq);
			list = iChangeRequestService.updateFileByDmId(jsonObj,file);
		} catch (Exception e) {
			logger.debug("updateFileByDmId methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getChangeReqHdrDtlsByProdCode")
	public ResponseEntity<ResponseAsList> getChangeReqHdrDtlsByProdCode(@RequestBody GetKeyAreaDtlsRequest GetKeyAreaDtlsReq) {
		logger.debug("getChangeReqHdrDtlsByProdCode method Start");
		ResponseAsList list = null;
		try {
			list = iChangeRequestService.getChangeReqHdrDtlsByProdCode(GetKeyAreaDtlsReq);
		} catch (Exception ex) {
			logger.error("getChangeReqHdrDtlsByProdCode  method  exception" + ex);
		}
		logger.debug("getChangeReqHdrDtlsByProdCode   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge=3600) 
	@PostMapping("getChangeReqIndentHdrByProdId") 
	public ResponseEntity<ResponseAsList> getChangeReqIndentHdrByProdId (@RequestBody GetKeyIndentDtRequest GetKeyIndentDtlReq ) { 
		logger.debug("getChangeReqIndentHdrByProdId method Start"); 
		ResponseAsList list = null; 
		try { 
			list = iChangeRequestService.getChangeReqIndentHdrByProdId(GetKeyIndentDtlReq); 
		} catch (Exception ex) { 
			logger.error("getChangeReqIndentHdrByProdId  method  exception" + ex); 
		} 
		logger.debug("getChangeReqIndentHdrByProdId   method end"); 
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK); 
	} 
	
	@CrossOrigin(maxAge=3600) 
	@PostMapping("getChangeReqIndentDtlByIndentId") 
	public ResponseEntity<ResponseAsList> getChangeReqIndentDtlByIndentId (@RequestBody GetKeyIndentDtl GetKeyIndentDtl ) { 
		logger.debug("getChangeReqIndentDtlByIndentId method Start"); 
		ResponseAsList list = null; 
		try { 
			list = iChangeRequestService.getChangeReqIndentDtlByIndentId(GetKeyIndentDtl); 
		} catch (Exception ex) { 
			logger.error("getChangeReqIndentDtlByIndentId  method  exception" + ex); 
		} 
		logger.debug("getChangeReqIndentDtlByIndentId   method end"); 
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK); 
	}

	@CrossOrigin(maxAge=3600)
	@PostMapping("getIndentDetailsByCode")
	public ResponseEntity<IndentPartDetailsEntity> getIndentDetailsByCode(@RequestBody Map<String, Object> request) {
		try {
			Integer indentId = (Integer) request.get("indentId");
			IndentPartDetailsEntity result = iChangeRequestService.getIndentDetailsByCode(indentId);
			if (result != null) {
				logger.info("Returning Indent Details: " + result.getIndentId());
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				logger.warn("No data found for indentId: " + indentId);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception ex) {
			logger.error("Exception in getIndentDetailsByCode: ", ex);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
