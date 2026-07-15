package com.vmfg.scm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vmfg.design.request.IdAndTenantIdRequest;
import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.sales.request.GetAddressDtlByDcTypeRequest;
import com.vmfg.scm.entity.DcHdrEntity;
import com.vmfg.scm.entity.GetPoDtlsEntity;
import com.vmfg.scm.request.GetDcDtlByDcIdRequest;
import com.vmfg.scm.request.GetpoInstoreDtlByPmIdRequest;
import com.vmfg.scm.request.IndentGrpDtlRequest;
import com.vmfg.scm.request.PoHSNCodeRequest;
import com.vmfg.scm.request.PoTypeUpdateReq;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;
import com.vmfg.scm.request.getDCProductDropDownRequest;
import com.vmfg.scm.services.interfaces.IPoServices;

@Controller
@RequestMapping("/")
public class PoController {
	private static final Logger logger = LoggerFactory.getLogger(PoController.class);
	
	@Autowired
	private IPoServices iPoServices;
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPoHdrDtlsByIndentId")
	public ResponseEntity<ResponseAsList> getPoHdrDtlsByIndentId(@RequestBody IdAndTenantIdRequest IdAndTenantIdReq) {
		logger.info("getPoHdrDtlsByIndentId Controller  method Start");
		ResponseAsList list = null;
		try {
			list = iPoServices.getPoHdrDtlsByIndentId(IdAndTenantIdReq);
		} catch (Exception ex) {
			logger.error("getPoHdrDtlsByIndentId Controller  method  exception:" + ex);
		}
		logger.info("getPoHdrDtlsByIndentId Controller  method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPoDtlsByPoId")
	public ResponseEntity<ResponseAsList> getPoDtlsByPoId(@RequestBody IdAndTenantIdRequest IdAndTenantIdReq) {
		logger.debug("getPoDtlsByPoId   method Start");
		ResponseAsList list=null;
		try {

			list = iPoServices.getPoDtlsByPoId(IdAndTenantIdReq);

		} catch (Exception ex) {
			logger.error("getPoDtlsByPoId  method  exception" + ex);
		}
		logger.debug("getPoDtlsByPoId   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertPoDtlService")
	public ResponseEntity<ResponseAsMessage> insertPoDtlService(@RequestBody GetPoDtlsEntity insertPoDtlsEntity) {
		logger.debug("insertPoDtlsEntity   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iPoServices.insertPoHdrDtl(insertPoDtlsEntity);

		} catch (Exception ex) {
			logger.error("insertPoDtlsEntity  method  exception" + ex);
		}
		logger.debug("insertPoDtlsEntity   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updatePoSeqAndStatus")
	public ResponseEntity<ResponseAsMessage> updatePoSeqAndStatus(@RequestBody UpdateSeqAndStatusRequest updatePoDtlsEntity) {
		logger.debug("updatePoDtlService   method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iPoServices.updatePoSeqAndStatus(updatePoDtlsEntity);

		} catch (Exception ex) {
			logger.error("updatePoDtlService  method  exception" + ex);
		}
		logger.debug("updatePoDtlService   method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("updatePoType")
	public ResponseEntity<ResponseAsMessage> updatePoType(@RequestBody PoTypeUpdateReq poTypeReq) {
		logger.debug("updatePoType method Start");
		ResponseAsMessage respMsg=null;
		try {

			respMsg = iPoServices.updatePoType(poTypeReq);

		} catch (Exception ex) {
			logger.error("updatePoType method  exception" + ex);
		}
		logger.debug("updatePoType  method end");
		return new ResponseEntity<ResponseAsMessage>(respMsg, HttpStatus.OK);
	}
	
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPoDtlsByDateAndPoId")
	public ResponseEntity<ResponseAsList> getPoDtlsByDateAndPoId(@RequestBody IndentGrpDtlRequest IndentGrpDtlReq) {
		logger.debug("getPoDtlsByPoId   method Start");
		ResponseAsList list=null;
		try {

			list = iPoServices.getPoDtlsByDateAndPoId(IndentGrpDtlReq);

		} catch (Exception ex) {
			logger.error("getPoDtlsByDateAndPoId  method  exception" + ex);
		}
		logger.debug("getPoDtlsByDateAndPoId   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@GetMapping("getDCTypeDtl")
	public ResponseEntity<ResponseAsList> getDCTypeDtl() {
		logger.debug("getDCTypeDtl   method Start");
		ResponseAsList list=null;
		try {

			list = iPoServices.getDCTypeDtl();

		} catch (Exception ex) {
			logger.error("getDCTypeDtl  method  exception" + ex);
		}
		logger.debug("getDCTypeDtl   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDCProductDropDown")
	public ResponseEntity<ResponseAsList> getDCProductDropDown(@RequestBody getDCProductDropDownRequest getDCProductDropDownReq) {
		logger.debug("getDCTypeDtl   method Start");
		ResponseAsList list=null;
		try {

			list = iPoServices.getDCProductDropDown(getDCProductDropDownReq);

		} catch (Exception ex) {
			logger.error("getDCProductDropDown  method  exception" + ex);
		}
		logger.debug("getDCProductDropDown   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getpoInstoreDtlByPmId")
	public ResponseEntity<ResponseAsList> getpoInstoreDtlByPmId(@RequestBody GetpoInstoreDtlByPmIdRequest getpoInstoreDtlByPmIdReq) {
		logger.debug("getpoInstoreDtlByPmId   method Start");
		ResponseAsList list=null;
		try {

			list = iPoServices.getpoInstoreDtlByPmId(getpoInstoreDtlByPmIdReq);

		} catch (Exception ex) {
			logger.error("getpoInstoreDtlByPmId  method  exception" + ex);
		}
		logger.debug("getpoInstoreDtlByPmId   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAddressDtlByDcType")
	public ResponseEntity<ResponseAsList> getAddressDtlByDcType(@RequestBody GetAddressDtlByDcTypeRequest getAddressDtlByDcTypeReq) {
		logger.debug("getAddressDtlByDcType   method Start");
		ResponseAsList list=null;
		try {

			list = iPoServices.getAddressDtlByDcType(getAddressDtlByDcTypeReq);

		} catch (Exception ex) {
			logger.error("getAddressDtlByDcType  method  exception" + ex);
		}
		logger.debug("getAddressDtlByDcType   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getAllDcHdrByPmId")
	public ResponseEntity<ResponseAsList> getAllDcHdrByPmId(@RequestBody PmHdrIdAndTenantIdRequest pmHdrIdAndTenantIdReq) {
		logger.debug("getAllDcHdrByPmId   method Start");
		ResponseAsList list=null;
		try {

			list = iPoServices.getAllDcHdrByPmId(pmHdrIdAndTenantIdReq);

		} catch (Exception ex) {
			logger.error("getAllDcHdrByPmId  method  exception" + ex);
		}
		logger.debug("getAllDcHdrByPmId   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getDcDtlByDcId")
	public ResponseEntity<ResponseAsList> getDcDtlByDcId(@RequestBody GetDcDtlByDcIdRequest getDcDtlByDcIdReq) {
		logger.debug("getDcDtlByDcId   method Start");
		ResponseAsList list=null;
		try {

			list = iPoServices.getDcDtlByDcId(getDcDtlByDcIdReq);

		} catch (Exception ex) {
			logger.error("getDcDtlByDcId  method  exception" + ex);
		}
		logger.debug("getDcDtlByDcId   method end");
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertDcDtl")
	public ResponseEntity<ResponseAsMessage> insertDcDtl(@RequestBody DcHdrEntity dcHdrReq) {
		logger.debug("insertDcDtl   method Start");
		ResponseAsMessage list=null;
		try {

			list = iPoServices.insertDcDtl(dcHdrReq);

		} catch (Exception ex) {
			logger.error("insertDcDtl  method  exception" + ex);
		}
		logger.debug("insertDcDtl   method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("cancelDcHdr")
	public ResponseEntity<ResponseAsMessage> cancelDcHdr(@RequestBody GetDcDtlByDcIdRequest getDcDtlByDcIdReq) {
		logger.debug("cancelDcHdr   method Start");
		ResponseAsMessage list=null;
		try {

			list = iPoServices.cancelDcHdr(getDcDtlByDcIdReq);

		} catch (Exception ex) {
			logger.error("cancelDcHdr  method  exception" + ex);
		}
		logger.debug("cancelDcHdr   method end");
		return new ResponseEntity<ResponseAsMessage>(list, HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getHsnCodeByPartNo")
	public ResponseEntity<ResponseAsList> getHsnCodeByPartNo(@RequestBody PoHSNCodeRequest pohsnCodeReq) {
		logger.debug("getHsnCodeByPartNo   method Start");
		ResponseAsList hsnList = null;
		try {

			hsnList = iPoServices.getHsnCodeByPartNo(pohsnCodeReq);

		} catch (Exception ex) {
			logger.error("getHsnCodeByPartNo  method  exception" + ex);
		}
		logger.debug("getHsnCodeByPartNo   method end");
		return new ResponseEntity<ResponseAsList>(hsnList,HttpStatus.OK);
	}
	
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getdivisionDesc")
	public ResponseEntity<ResponseAsList> getdivisionDesc(@RequestBody TenantRequest tenantReq) {
		logger.debug("getHsnCodeByPartNo   method Start");
		ResponseAsList hsnList = null;
		try {

			hsnList = iPoServices.getdivisionDesc(tenantReq);

		} catch (Exception ex) {
			logger.error("getdivisionDesc  method  exception" + ex);
		}
		logger.debug("getdivisionDesc   method end");
		return new ResponseEntity<ResponseAsList>(hsnList,HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTransitInsuranceDesc")
	public ResponseEntity<ResponseAsList> getTransitInsuranceDesc(@RequestBody TenantRequest tenantReq) {
		logger.debug("getransitInsuranceDesc   method Start");
		ResponseAsList hsnList = null;
		try {

			hsnList = iPoServices.getTransitInsuranceDesc(tenantReq);

		} catch (Exception ex) {
			logger.error("getransitInsuranceDesc  method  exception" + ex);
		}
		logger.debug("getransitInsuranceDesc   method end");
		return new ResponseEntity<ResponseAsList>(hsnList,HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getModeOfDispatchDesc")
	public ResponseEntity<ResponseAsList> getModeOfDispatchDesc(@RequestBody TenantRequest tenantReq) {
		logger.debug("getModeOfDispatchDesc   method Start");
		ResponseAsList hsnList = null;
		try {

			hsnList = iPoServices.getModeOfDispatchDesc(tenantReq);

		} catch (Exception ex) {
			logger.error("getModeOfDispatchDesc  method  exception" + ex);
		}
		logger.debug("getModeOfDispatchDesc   method end");
		return new ResponseEntity<ResponseAsList>(hsnList,HttpStatus.OK);
	}
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getInspectScopeDesc")
	public ResponseEntity<ResponseAsList> getInspectScopeDesc(@RequestBody TenantRequest tenantReq) {
		logger.debug("getInspectScopeDesc   method Start");
		ResponseAsList hsnList = null;
		try {

			hsnList = iPoServices.getInspectScopeDesc(tenantReq);

		} catch (Exception ex) {
			logger.error("getInspectScopeDesc  method  exception" + ex);
		}
		logger.debug("getInspectScopeDesc   method end");
		return new ResponseEntity<ResponseAsList>(hsnList,HttpStatus.OK);
	}
	
}
