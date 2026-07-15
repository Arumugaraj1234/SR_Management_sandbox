package com.vmfg.scm.controller;

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

import com.vmfg.design.request.IdAndTenantIdRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.scm.entity.ScpDtlsEntity;
import com.vmfg.scm.request.DeleteIndScpDtlIdRequest;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.IndentGrpDelRequest;
import com.vmfg.scm.request.IndentGrpDtlRequest;
import com.vmfg.scm.request.IndentInsertGrpRequest;
import com.vmfg.scm.request.IndentTemplateNameRequest;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.scm.request.getIndentQtyDtlRequest;
import com.vmfg.scm.services.interfaces.IIndentGroupService;
@Controller
@RequestMapping("/")
public class IndentGroupController {
	private static final Logger logger = LoggerFactory.getLogger(IndentGroupController.class);

	@Autowired
	IIndentGroupService iIndentGroupService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentGroupHdr")
	public ResponseEntity<ResponseAsList> getIndentGroupDetails(@RequestBody IndentGrpDtlRequest indentGrpDtlReq) {
		logger.debug("getIndentGroupDetails   method Start");
		ResponseAsList indentGrpDtls=null;
		try {

			indentGrpDtls = iIndentGroupService.getIndentGroupDetails(indentGrpDtlReq);

		} catch (Exception ex) {
			logger.error("getIndentGroupDetails  method  exception" + ex);
		}
		logger.debug("getIndentGroupDetails   method end");
		return new ResponseEntity<ResponseAsList>(indentGrpDtls, HttpStatus.OK);
	}


	@CrossOrigin(maxAge = 3600)
	@PostMapping("delIndentGrpDtl")
	public ResponseEntity<ResponseAsMessage> delIndentGrpDtl(@RequestBody IndentGrpDelRequest indentGrpDtlReq) {
		logger.debug("delIndentGrpDtl   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iIndentGroupService.delIndentGrpDtl(indentGrpDtlReq);

		} catch (Exception ex) {
			logger.error("delIndentGrpDtl  method  exception" + ex);
		}
		logger.debug("delIndentGrpDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}


	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentGroupHdrAndDtl")
	public ResponseEntity<ResponseAsList> getIndentGroupHdrAndDtl(@RequestBody IndentGrpDelRequest indentGrpDtlReq) {
		logger.debug("getIndentGroupHdrAndDtl   method Start");
		ResponseAsList indentGrpDtls=null;
		try {

			indentGrpDtls = iIndentGroupService.getIndentGroupHdrAndDtl(indentGrpDtlReq);

		} catch (Exception ex) {
			logger.error("getIndentGroupHdrAndDtl  method  exception" + ex);
		}
		logger.debug("getIndentGroupHdrAndDtl   method end");
		return new ResponseEntity<ResponseAsList>(indentGrpDtls, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("checkTemplateName")
	public ResponseEntity<ResponseAsMessage> checkTemplateName(@RequestBody IndentTemplateNameRequest indentTempName) {
		logger.debug("checkTemplateName   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iIndentGroupService.checkTemplateName(indentTempName);

		} catch (Exception ex) {
			logger.error("checkTemplateName  method  exception" + ex);
		}
		logger.debug("checkTemplateName   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}


	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertTempGrup")
	public ResponseEntity<ResponseAsMessage> insertTempGrup(@RequestBody IndentInsertGrpRequest indentTempName) {
		logger.debug("insertTempGrup   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iIndentGroupService.insertTempGrup(indentTempName);

		} catch (Exception ex) {
			logger.error("insertTempGrup  method  exception" + ex);
		}
		logger.debug("insertTempGrup   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentQtyDtl")
	public ResponseEntity<ResponseAsList> getIndentQtyDtl(@RequestBody getIndentQtyDtlRequest getIndentQtyDtlReq) {
		logger.debug("getIndentQtyDtl   method Start");
		ResponseAsList indentGrpDtls=null;
		try {


			indentGrpDtls = iIndentGroupService.getIndentQtyDtl(getIndentQtyDtlReq);

		} catch (Exception ex) {
			logger.error("getIndentQtyDtl  method  exception" + ex);
		}
		logger.debug("getIndentQtyDtl   method end");
		return new ResponseEntity<ResponseAsList>(indentGrpDtls, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getScpDtlsByIgHdrId")
	public ResponseEntity<ResponseAsList> getScpDtlsByIgHdrId(@RequestBody IdAndTenantIdRequest IdAndTenantIdReq) {
		logger.debug("getScpDtlsByIgHdrId   method Start");
		ResponseAsList indentGrpDtls=null;
		try {

			indentGrpDtls = iIndentGroupService.getScpDtlsByIgHdrId(IdAndTenantIdReq);

		} catch (Exception ex) {
			logger.error("getScpDtlsByIgHdrId  method  exception" + ex);
		}
		logger.debug("getScpDtlsByIgHdrId   method end");
		return new ResponseEntity<ResponseAsList>(indentGrpDtls, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertScpDtlsByIgHdrId")
	public ResponseEntity<ResponseAsMessage> insertScpDtlsByIgHdrId(@RequestBody List<ScpDtlsEntity> scpDtlsEntity) {
		logger.debug("insertScpDtlsByIgHdrId   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iIndentGroupService.insertScpDtlsByIgHdrId(scpDtlsEntity);

		} catch (Exception ex) {
			logger.error("insertScpDtlsByIgHdrId  method  exception" + ex);
		}
		logger.debug("insertScpDtlsByIgHdrId   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateScpSeqAndStatus")
	public ResponseEntity<ResponseAsMessage> updateScpSeqAndStatus(@RequestBody UpdateSeqAndStatusRequest UpdateHdrReq) {
		logger.debug("updateScpSeqAndStatus  method Start");
		ResponseAsMessage list = null;
		try {
			list = iIndentGroupService.updateScpSeqAndStatus(UpdateHdrReq);
		} catch (Exception e) {
			logger.debug("updateScpSeqAndStatus methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteIndScpDtlId")
	public ResponseEntity<ResponseAsMessage> deleteIndScpDtlId(@RequestBody DeleteIndScpDtlIdRequest deleteIndScpDtlIdReq) {
		logger.debug("deleteIndScpDtlId   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iIndentGroupService.deleteIndScpDtlId(deleteIndScpDtlIdReq);

		} catch (Exception ex) {
			logger.error("deleteIndScpDtlId  method  exception" + ex);
		}
		logger.debug("deleteIndScpDtlId   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentGroupDtlsForSCS")
	public ResponseEntity<ResponseAsList> getIndentGroupDtlsForSCS(@RequestBody HdrIdandTenantIdRequest hdrIdandTenantIdReq) {
		logger.debug("getIndentGroupDtlsForSCS   method Start");
		ResponseAsList indentGrpDtls=null;
		try {

			indentGrpDtls = iIndentGroupService.getIndentGroupDtlsForSCS(hdrIdandTenantIdReq);

		} catch (Exception ex) {
			logger.error("getIndentGroupDtlsForSCS  method  exception" + ex);
		}
		logger.debug("getIndentGroupDtlsForSCS   method end");
		return new ResponseEntity<ResponseAsList>(indentGrpDtls, HttpStatus.OK);
	}
}
