package com.vmfg.project.controller;

import java.util.List;

import com.vmfg.project.request.*;
import com.vmfg.project.response.ProjectInternalResponse;
import com.vmfg.project.service.impl.ProjectService;
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
import com.vmfg.project.entity.IndentBudgetDtlEntity;
import com.vmfg.project.service.interfaces.IProjectService;

@Controller
@RequestMapping("/")
public class ProjectController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	IProjectService iProjectService;

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjectDtl")
	public ResponseEntity<ResponseAsList> getProjectDtl(@RequestBody ProjectHdrRequest tenantReq) {
		logger.info("getProjectDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getProjectDtl(tenantReq);

		} catch (Exception ex) {
			logger.error("getProjectDtl  method  exception" + ex);
		}
		logger.debug("getProjectDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getWbsTemplate")
	public ResponseEntity<ResponseAsList> getWbsTemplate(@RequestBody TenantRequest tenantReq) {
		logger.info("getWbsTemplate   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getWbsTemplate(tenantReq);

		} catch (Exception ex) {
			logger.error("getWbsTemplate  method  exception" + ex);
		}
		logger.debug("getWbsTemplate   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}


	@Autowired
	private ProjectService projectService;
	@CrossOrigin(maxAge = 3600)
	@PostMapping("getIsInternalOrNot")
	public ResponseEntity<?> getIsInternalOrNot(@RequestBody ProjectInternalRequest request) {
		ProjectInternalResponse response = projectService.getIsInternalOrNot(request);
		if (response == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No record found for projectCode: " + request.getProjectCode());
		}
		return ResponseEntity.ok(response);
	}


	@CrossOrigin(maxAge = 3600)
	@PostMapping("getWbsTemplateById")
	public ResponseEntity<ResponseAsList> getWbsTemplateById(@RequestBody WbsIDRequest wbsReq) {
		logger.info("getWbsTemplateById   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getWbsTemplateById(wbsReq);

		} catch (Exception ex) {
			logger.error("getWbsTemplateById  method  exception" + ex);
		}
		logger.debug("getWbsTemplateById   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertUpdateProjectMilestone")
	public ResponseEntity<ResponseAsMessage> insertUpdateProjectMilestone(
			@RequestBody List<ProjectTimelineRequest> projTimeReq) {
		logger.info("insertUpdateProjectMilestone   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.insertUpdateProjectMilestone(projTimeReq);

		} catch (Exception ex) {
			logger.error("getWbsTemplateById  method  exception" + ex);
		}
		logger.debug("getWbsTemplateById   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteWBSById")
	public ResponseEntity<ResponseAsMessage> deleteWBSById(@RequestBody DeleteTimeWBSByIDRequest deleteById) {
		logger.info("deleteWBSById   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.deleteWBSById(deleteById);

		} catch (Exception ex) {
			logger.error("deleteWBSById  method  exception" + ex);
		}
		logger.debug("deleteWBSById   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTimeLineByPM")
	public ResponseEntity<ResponseAsList> getTimeLineByPM(@RequestBody ProjectByIDRequest projHdr) {
		logger.info("getTimeLineByPM   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getTimeLineByPM(projHdr);

		} catch (Exception ex) {
			logger.error("getTimeLineByPM  method  exception" + ex);
		}
		logger.debug("getTimeLineByPM   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getTimeTrackerByProjectId")
	public ResponseEntity<ResponseAsList> getTimeTrackerByProjectId(@RequestBody ProjectByIDRequest projHdr) {
		logger.info("getTimeTrackerByProjectId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getTimeTrackerByProjectId(projHdr);

		} catch (Exception ex) {
			logger.error("getTimeTrackerByProjectId  method  exception" + ex);
		}
		logger.debug("getTimeTrackerByProjectId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertKeyAreaByPMId")
	public ResponseEntity<ResponseAsMessage> insertKeyAreaByPMId(@RequestBody List<KeyAreaRequest> keyAre) {
		logger.info("insertKeyAreaByPMId   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.insertKeyAreaByPMId(keyAre);

		} catch (Exception ex) {
			logger.error("insertKeyAreaByPMId  method  exception" + ex);
		}
		logger.debug("insertKeyAreaByPMId   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deletePKByPKAId")
	public ResponseEntity<ResponseAsMessage> delKeyAreaByPKId(@RequestBody KeyAreaDelRequest delReq) {
		logger.info("delKeyAreaByPKId   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.delKeyAreaByPKId(delReq);

		} catch (Exception ex) {
			logger.error("delKeyAreaByPKId  method  exception" + ex);
		}
		logger.debug("delKeyAreaByPKId   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getPKForProj")
	public ResponseEntity<ResponseAsList> getPKForProj(@RequestBody ProjectByIDRequest projHdr) {
		logger.debug("getPKForProj  method Start");
		ResponseAsList list = null;
		try {
			list = iProjectService.getPKForProj(projHdr);
		} catch (Exception e) {
			logger.debug("getPKForProj methode exception " + e);
		}
		return new ResponseEntity<ResponseAsList>(list, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updatedesignindentReq")
	public ResponseEntity<ResponseAsMessage> updatedesignindentReq(@RequestBody ProjectByIDRequest projectByIdReq) {
		logger.info("updatedesignindentReq   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.updatedesignindentReq(projectByIdReq);

		} catch (Exception ex) {
			logger.error("updatedesignindentReq  method  exception" + ex);
		}
		logger.debug("updatedesignindentReq   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getLinkStatusByPMId")
	public ResponseEntity<ResponseAsList> getLinkStatusByPMId(@RequestBody ProjectByIDRequest projectByIdReq) {
		logger.info("getLinkStatusByPMId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getLinkStatusByPMId(projectByIdReq);

		} catch (Exception ex) {
			logger.error("getLinkStatusByPMId  method  exception" + ex);
		}
		logger.debug("getLinkStatusByPMId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getelementHdrDistinct")
	public ResponseEntity<ResponseAsList> getelementHdrDistinct(@RequestBody ProjectByIDRequest projectByIdReq) {
		logger.info("getelementHdrDistinct   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getelementHdrDistinct(projectByIdReq);

		} catch (Exception ex) {
			logger.error("getelementHdrDistinct  method  exception" + ex);
		}
		logger.debug("getelementHdrDistinct   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getSubAreaPmHdrList")
	public ResponseEntity<ResponseAsList> getSubAreaPmHdrList(
			@RequestBody GetSubAreaPmHdrListRequest getSubAreaPmHdrListReq) {
		logger.info("getSubAreaPmHdrList   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getSubAreaPmHdrList(getSubAreaPmHdrListReq);

		} catch (Exception ex) {
			logger.error("getSubAreaPmHdrList  method  exception" + ex);
		}
		logger.debug("getSubAreaPmHdrList   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getsalesBudgetExtnDtl")
	public ResponseEntity<ResponseAsList> getsalesBudgetExtnDtl(
			@RequestBody getsalesBudgetExtnDtlRequest getsalesBudgetExtnDtlReq) {
		logger.info("getsalesBudgetExtnDtl   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getsalesBudgetExtnDtl(getsalesBudgetExtnDtlReq);

		} catch (Exception ex) {
			logger.error("getsalesBudgetExtnDtl  method  exception" + ex);
		}
		logger.debug("getsalesBudgetExtnDtl   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteSubAreaExtn")
	public ResponseEntity<ResponseAsMessage> deleteSubAreaExtn(
			@RequestBody DeleteSubAreaExtnRequest deleteSubAreaExtnReq) {
		logger.info("deleteSubAreaExtn   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.deleteSubAreaExtn(deleteSubAreaExtnReq);

		} catch (Exception ex) {
			logger.error("deleteSubAreaExtn  method  exception" + ex);
		}
		logger.debug("deleteSubAreaExtn   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertSubAreaExtn")
	public ResponseEntity<ResponseAsMessage> insertSubAreaExtn(
			@RequestBody List<InsertSubAreaExtnRequest> insertSubAreaExtnReq) {
		logger.info("insertSubAreaExtn   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.insertSubAreaExtn(insertSubAreaExtnReq);

		} catch (Exception ex) {
			logger.error("insertSubAreaExtn  method  exception" + ex);
		}
		logger.debug("insertSubAreaExtn   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getbugetextnListbyDSkId")
	public ResponseEntity<ResponseAsList> getbugetextnListbyDSkId(
			@RequestBody GetbugetextnListbyDSkIdRequest getbugetextnListbyDSkIdReq) {
		logger.info("getbugetextnListbyDSkId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getbugetextnListbyDSkId(getbugetextnListbyDSkIdReq);

		} catch (Exception ex) {
			logger.error("getbugetextnListbyDSkId  method  exception" + ex);
		}
		logger.debug("getbugetextnListbyDSkId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getindentbudgetextValbyPSkId")
	public ResponseEntity<ResponseAsList> getindentbugetextValbyPSkId(
			@RequestBody GetindentbudgetextValbyPSkIdRequest getindentbudgetextValbyPSkIdReq) {
		logger.info("getindentbugetextValbyPSkId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getbugetextnListbyDSkId(getindentbudgetextValbyPSkIdReq);

		} catch (Exception ex) {
			logger.error("getindentbugetextValbyPSkId  method  exception" + ex);
		}
		logger.debug("getindentbugetextValbyPSkId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("insertindentBudget")
	public ResponseEntity<ResponseAsMessage> insertindentBudget(
			@RequestBody List<IndentBudgetDtlEntity> indentBudgetDtlre) {
		logger.info("insertindentBudget   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.insertindentBudget(indentBudgetDtlre);

		} catch (Exception ex) {
			logger.error("insertindentBudget  method  exception" + ex);
		}
		logger.debug("insertindentBudget   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("totalSubAreaValueByPskId")
	public ResponseEntity<ResponseAsMessage> totalSubAreaValueByPskId(
			@RequestBody GetSubAreaPmHdrListRequest getSubAreaPmHdrListReq) {
		logger.info("totalSubAreaValueByPskId   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.totalSubAreaValueByPskId(getSubAreaPmHdrListReq);

		} catch (Exception ex) {
			logger.error("totalSubAreaValueByPskId  method  exception" + ex);
		}
		logger.debug("totalSubAreaValueByPskId   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getindentbudgetDtlbyindentDtlId")
	public ResponseEntity<ResponseAsList> getindentbudgetDtlbyindentDtlId(
			@RequestBody GetindentbudgetDtlbyindentDtlIdRequest getindentbudgetDtlbyindentDtlIdReq) {
		logger.info("getindentbudgetDtlbyindentDtlId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getindentbudgetDtlbyindentDtlId(getindentbudgetDtlbyindentDtlIdReq);

		} catch (Exception ex) {
			logger.error("getindentbudgetDtlbyindentDtlId  method  exception" + ex);
		}
		logger.debug("getindentbudgetDtlbyindentDtlId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("deleteIndentBudgetId")
	public ResponseEntity<ResponseAsMessage> deleteIndentBudgetId(
			@RequestBody DeleteIndentBudgetIdRequest deleteIndentBudgetIdReq) {
		logger.info("deleteIndentBudgetId   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.deleteIndentBudgetId(deleteIndentBudgetIdReq);

		} catch (Exception ex) {
			logger.error("deleteIndentBudgetId  method  exception" + ex);
		}
		logger.debug("deleteIndentBudgetId   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjTimePlanDropDown")
	public ResponseEntity<ResponseAsList> getProjTimePlanDropDown(@RequestBody TenantRequest tenantReq) {
		logger.info("getProjTimePlanDropDown   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getProjTimePlanDropDown(tenantReq);

		} catch (Exception ex) {
			logger.error("getProjTimePlanDropDown  method  exception" + ex);
		}
		logger.debug("getProjTimePlanDropDown   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getExistingPMtemplateByPmHdrId")
	public ResponseEntity<ResponseAsList> getExistingPMtemplateByPmHdrId(
			@RequestBody PmHdrIdAndTenantIdRequest pmHdrIdAndTenantIdReq) {
		logger.info("getExistingPMtemplateByPmHdrId   method Start");
		ResponseAsList resp = null;
		try {

			resp = iProjectService.getExistingPMtemplateByPmHdrId(pmHdrIdAndTenantIdReq);

		} catch (Exception ex) {
			logger.error("getExistingPMtemplateByPmHdrId  method  exception" + ex);
		}
		logger.debug("getExistingPMtemplateByPmHdrId   method end");
		return new ResponseEntity<ResponseAsList>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateProjectPlanDate")
	public ResponseEntity<ResponseAsMessage> updateProjectPlanDate(
			@RequestBody UpdateProjectPlanDateRequest updateProjectPlanDateReq) {
		logger.info("updateProjectPlanDate   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.updateProjectPlanDate(updateProjectPlanDateReq);

		} catch (Exception ex) {
			logger.error("updateProjectPlanDate  method  exception" + ex);
		}
		logger.debug("updateProjectPlanDate   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateDesignHdr")
	public ResponseEntity<ResponseAsMessage> updateDesignHdr(
			@RequestBody UpdateDesignHdrRequest updateDesignHdrRequest) {
		logger.info("updateDesignHdr   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.updateDesignHdr(updateDesignHdrRequest);

		} catch (Exception ex) {
			logger.error("updateDesignHdr  method  exception" + ex);
		}
		logger.debug("updateDesignHdr   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("getProjectInitiationMstResp")
	public ResponseEntity<ResponseAsMessage> getProjectInitiationMstResp(
			@RequestBody ProjectInitiationMstRequest projectInitiation) {
		logger.info("getProjectInitiationMstResp   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.getProjectInitiationMstResp(projectInitiation);

		} catch (Exception ex) {
			logger.error("getProjectInitiationMstResp  method  exception" + ex);
		}
		logger.debug("getProjectInitiationMstResp   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateAssyMstResp")
	public ResponseEntity<ResponseAsMessage> updateAssyMstResp(@RequestBody AssyMstRequest assyMstRequest) {
		logger.info("updateAssyMstResp   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.updateAssyMstResp(assyMstRequest);

		} catch (Exception ex) {
			logger.error("updateAssyMstResp  method  exception" + ex);
		}
		logger.debug("updateAssyMstResp   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateQCbuyoff")
	public ResponseEntity<ResponseAsMessage> updateQCbuyoff(@RequestBody AssyMstRequest assyMstRequest) {
		logger.info("updateQCbuyoff   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.updateQCbuyoff(assyMstRequest);

		} catch (Exception ex) {
			logger.error("updateQCbuyoff  method  exception" + ex);
		}
		logger.debug("updateQCbuyoff   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}

	@CrossOrigin(maxAge = 3600)
	@PostMapping("updateBudgetSheetPaymentTerms")
	public ResponseEntity<ResponseAsMessage> updateBudgetSheetPaymentTerms(@RequestBody updateBsPaymentTermsRequest bsPaymentRequest) {
		logger.info("updateBudgetSheetPaymentTerms   method Start");
		ResponseAsMessage resp = null;
		try {

			resp = iProjectService.updateBudgetSheetPaymentTerms(bsPaymentRequest);

		} catch (Exception ex) {
			logger.error("updateBudgetSheetPaymentTerms  method  exception" + ex);
		}
		logger.debug("updateBudgetSheetPaymentTerms   method end");
		return new ResponseEntity<ResponseAsMessage>(resp, HttpStatus.OK);
	}
}
