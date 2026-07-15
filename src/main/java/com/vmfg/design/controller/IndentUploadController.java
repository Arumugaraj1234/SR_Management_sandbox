package com.vmfg.design.controller;

import java.util.List;

import org.json.JSONArray;
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

import com.vmfg.design.entity.IndentRequestEntity;
import com.vmfg.design.entity.SalesIndentBudgetDtlEntity;
import com.vmfg.design.request.BudgetValueUpdateRequest;
import com.vmfg.design.request.GetIndentDtlProductCostRequest;
import com.vmfg.design.request.IndentDtlRequest;
import com.vmfg.design.request.IndentRemarksRequest;
import com.vmfg.design.request.InsertIndentRequest;
import com.vmfg.design.request.TenantEmpRequest;
import com.vmfg.design.request.UpdateHdrRequest;
import com.vmfg.design.request.UpdateIndentAssignTeamReq;
import com.vmfg.design.request.getIndentHdrDtlRequest;
import com.vmfg.design.request.getStartIndentRequest;
import com.vmfg.design.response.getindentLifecycDtlResponse;
import com.vmfg.design.rowmapper.IndentDtlByPoRequest;
import com.vmfg.design.services.interfaces.IIndentUploadService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.IndentDtlDeleteRequest;

@Controller
@RequestMapping("/")
public class IndentUploadController {
	private static final Logger logger = LoggerFactory.getLogger(IndentUploadController.class);

	@Autowired
	private IIndentUploadService iIndentUploadService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("uploadIndentTemplate")
	public ResponseEntity<ResponseAsList> uploadIndentTemplate(@RequestParam("UploadIndentReq") String uploadIndentReq,
			@RequestParam("file") MultipartFile file) {
		logger.info("uploadIndentTemplate Controller  method Start");
		ResponseAsList list = null;
		try {
			JSONObject jsonObj = new JSONObject(uploadIndentReq);
			JSONArray getArray = jsonObj.getJSONArray("reqObj");
			list = iIndentUploadService.uploadIndentTemplate(getArray, file);
		} catch (Exception ex) {
			logger.error("uploadIndentTemplate Controller  method  exception:" + ex);
		}
		logger.info("uploadIndentTemplate Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertIndentDtls")
	public ResponseEntity<ResponseAsMessage> insertIndentDtls(@RequestBody InsertIndentRequest InsertIndentReq) {
		logger.info("insertIndentDtls Controller  method Start");
		ResponseAsMessage list = null;
		try {

			list = iIndentUploadService.insertIndentDtls(InsertIndentReq);
		} catch (Exception ex) {
			logger.error("insertIndentDtls Controller  method  exception:" + ex);
		}
		logger.info("insertIndentDtls Controller  method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentDtlsByIndentId")
	public ResponseEntity<ResponseAsList> getIndentDtlsByIndentId(@RequestBody IndentDtlRequest IndentDtlReq) {
		logger.info("getIndentDtlsByIndentId Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getIndentDtlsByIndentId(IndentDtlReq);
		} catch (Exception ex) {
			logger.error("getIndentDtlsByIndentId Controller  method  exception:" + ex);
		}
		logger.info("getIndentDtlsByIndentId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentDtlsPoIndentId")
	public ResponseEntity<ResponseAsList> getIndentDtlsPoIndentId(@RequestBody IndentDtlByPoRequest IndentDtlReq) {
		logger.info("getIndentDtlsPoIndentId Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getIndentDtlsByPoIndentId(IndentDtlReq);
		} catch (Exception ex) {
			logger.error("getIndentDtlsPoIndentId Controller  method  exception:" + ex);
		}
		logger.info("getIndentDtlsPoIndentId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentHdrDtlsByProjectId")
	public ResponseEntity<ResponseAsList> getIndentHdrDtlsByProjectId(
			@RequestBody getIndentHdrDtlRequest getIndentHdrDtlReq) {
		logger.info("getIndentHdrDtlsByProjectId Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getIndentHdrDtlsByProjectId(getIndentHdrDtlReq);
		} catch (Exception ex) {
			logger.error("getIndentHdrDtlsByProjectId Controller  method  exception:" + ex);
		}
		logger.info("getIndentHdrDtlsByProjectId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentTypeMstDropDown")
	public ResponseEntity<ResponseAsList> getIndentTypeMstDropDown(@RequestBody TenantEmpRequest TenantReq) {
		logger.info("getIndentTypeMstDropDown Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getIndentTypeMstDropDown(TenantReq);
		} catch (Exception ex) {
			logger.error("getIndentTypeMstDropDown Controller  method  exception:" + ex);
		}
		logger.info("getIndentTypeMstDropDown Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertIndentFile")
	public ResponseEntity<ResponseAsMessage> insertIndentFileByIndentID(
			@RequestParam("insertDocRequest") String insertDocReq, @RequestParam("file") MultipartFile file) {
		logger.debug("insertIndentFileByIndentID  method Start");
		ResponseAsMessage list = null;
		try {
			JSONObject jsonObj = new JSONObject(insertDocReq);
			list = iIndentUploadService.insertIndentFileByIndentID(jsonObj, file);
		} catch (Exception e) {
			logger.debug("insertIndentFileByIndentID methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateIndentHdrStatus")
	public ResponseEntity<ResponseAsMessage> updateIndentHdrStatus(@RequestBody UpdateHdrRequest UpdateHdrReq) {
		logger.debug("updateIndentHdrStatus  method Start");
		ResponseAsMessage list = null;
		try {
			list = iIndentUploadService.updateIndentHdrStatus(UpdateHdrReq);
		} catch (Exception e) {
			logger.debug("updateIndentHdrStatus methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getStartIndentReqStatus")
	public ResponseEntity<ResponseAsMessage> getStartIndentReqStatus(
			@RequestBody getStartIndentRequest getStartIndentReq) {
		logger.debug("getStartIndentReqStatus  method Start");
		ResponseAsMessage list = null;
		try {
			list = iIndentUploadService.getStartIndentReqStatus(getStartIndentReq);
		} catch (Exception e) {
			logger.debug("getStartIndentReqStatus methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentRemarks")
	public ResponseEntity<ResponseAsList> getIndentRemarks(@RequestBody IndentRemarksRequest indentRemReq) {
		logger.debug("getIndentRemarks  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getIndentRemarks(indentRemReq);
		} catch (Exception e) {
			logger.debug("getIndentRemarks method exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateBudgetDtl")
	public ResponseEntity<ResponseAsMessage> updateBudgetDtl(@RequestBody BudgetValueUpdateRequest BudgetValueUpdateReq) {
		logger.debug("updateBudgetDtl method Start");
		ResponseAsMessage resp = null;
		try {
			resp = iIndentUploadService.updateBudgetDtl(BudgetValueUpdateReq);
		} catch (Exception ex) {
			logger.error("updateBudgetDtl  method  exception" + ex);
		}
		logger.debug("updateBudgetDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentByIndentID")
	public ResponseEntity<ResponseAsList> getIndentByIndentID(@RequestBody IndentRequestEntity indentReq) {
		logger.debug("getIndentByIndentID  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getIndentByIndentID(indentReq);
		} catch (Exception e) {
			logger.debug("getIndentByIndentID method exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getCostAnlysDtls")
	public ResponseEntity<ResponseAsList> getCostAnlysDtls(@RequestBody HdrIdandTenantIdRequest cstDtl) {
		logger.debug("getCostAnlysDtls  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getCostAnlysDtls(cstDtl);
		} catch (Exception e) {
			logger.debug("getCostAnlysDtls method exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getMaterialTransferDtls")
	public ResponseEntity<ResponseAsList> getMaterialTransferDtls(@RequestBody PmHdrIdAndTenantIdRequest matDtls) {
		logger.debug("getMaterialTransferDtls  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getMaterialTransferDtls(matDtls);
		} catch (Exception e) {
			logger.debug("getMaterialTransferDtls method exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateEmpInIndentAssignTeam")
	public ResponseEntity<ResponseAsMessage> updateEmpInIndentAssignTeam(@RequestBody UpdateIndentAssignTeamReq indentAssignTeamReq) {
		logger.debug("updateEmpInIndentAssignTeam  method Start");
		ResponseAsMessage list = null;
		try {
			list = iIndentUploadService.updateEmpInIndentAssignTeam(indentAssignTeamReq);
			logger.debug("updateEmpInIndentAssignTeam  method End");
		} catch (Exception e) {
			logger.debug("updateEmpInIndentAssignTeam methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getBudgetDtlByIndent")
	public ResponseEntity<ResponseAsList> getBudgetDtlByIndent(@RequestBody IndentRequestEntity indentReq) {
		logger.debug("getBudgetDtlByIndent  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getBudgetDtlByIndent(indentReq);
		} catch (Exception e) {
			logger.debug("getBudgetDtlByIndent method exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateIndentBudgetDtl")
	public ResponseEntity<ResponseAsMessage> updateIndentBudgetDtl(@RequestBody List<SalesIndentBudgetDtlEntity> salesIndentBudgetDtlReq) {
		logger.debug("updateIndentBudgetDtl  method Start");
		ResponseAsMessage list = null;
		try {
			list = iIndentUploadService.updateIndentBudgetDtl(salesIndentBudgetDtlReq);
			logger.debug("updateIndentBudgetDtl  method End");
		} catch (Exception e) {
			logger.debug("updateIndentBudgetDtl methode exception " + e);
		}
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getindentLifecycDtl")
	public ResponseEntity<ResponseAsList> getindentLifecycDtl(@RequestBody getindentLifecycDtlResponse getindentLifecycDtlReq) {
		logger.debug("getindentLifecycDtl  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getindentLifecycDtl(getindentLifecycDtlReq);
			logger.debug("getindentLifecycDtl  method End");
		} catch (Exception e) {
			logger.debug("getindentLifecycDtl methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getindentDtlcyc")
	public ResponseEntity<ResponseAsList> getindentDtlcyc(@RequestBody IndentRequestEntity indentReq) {
		logger.debug("getindentDtlcyc  method Start");
		ResponseAsList list = null;
		try {
			list = iIndentUploadService.getindentDtlcyc(indentReq);
			logger.debug("getindentDtlcyc  method End");
		} catch (Exception e) {
			logger.debug("getindentDtlcyc methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteIndentByIndentDtlId")
	public ResponseEntity<ResponseAsMessage> deleteIndentByIndentDtlId(@RequestBody IndentDtlDeleteRequest indentDtlDeleteReq) {
		logger.debug("deleteIndentByIndentDtlId   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iIndentUploadService.deleteIndentByIndentDtlId(indentDtlDeleteReq);

		} catch (Exception ex) {
			logger.error("deleteIndentByIndentDtlId  method  exception" + ex);
		}
		logger.debug("deleteIndentByIndentDtlId   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteTaskDoc")
	public ResponseEntity<ResponseAsMessage> deleteTaskDoc(@RequestBody IndentDtlDeleteRequest indentDtlDeleteReq) {
		logger.debug("deleteTaskDoc   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iIndentUploadService.deleteTaskDoc(indentDtlDeleteReq);

		} catch (Exception ex) {
			logger.error("deleteTaskDoc  method  exception" + ex);
		}
		logger.debug("deleteTaskDoc   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIndentDtlProductCost")
	public ResponseEntity<ResponseAsList> getIndentDtlProductCost(@RequestBody GetIndentDtlProductCostRequest getIndentDtlProductCostReq) {
		logger.debug("getIndentDtlProductCost   method Start");
		ResponseAsList respMsg=null;
		try {

			respMsg = iIndentUploadService.getIndentDtlProductCost(getIndentDtlProductCostReq);

		} catch (Exception ex) {
			logger.error("getIndentDtlProductCost  method  exception" + ex);
		}
		logger.debug("getIndentDtlProductCost   method end");
		return new ResponseEntity<ResponseAsList>(respMsg, HttpStatus.OK);
	}
}
