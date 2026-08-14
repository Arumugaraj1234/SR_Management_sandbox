package com.vmfg.design.services.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.dao.impl.ChangeRequestDAO;
import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.dao.interfaces.IIndentUploadDAO;
import com.vmfg.design.entity.CatbaseIndentCostDtlEntity;
import com.vmfg.design.entity.GetBudgetDtlByIndentEntity;
import com.vmfg.design.entity.GetIndentDtlEntity;
import com.vmfg.design.entity.GetIndentDtlProductCostEntity;
import com.vmfg.design.entity.GetIndentLifecycDtlEntity;
import com.vmfg.design.entity.GetindentDtlcycEntity;
import com.vmfg.design.entity.IndentBudgetDtlByIndentIdEntity;
import com.vmfg.design.entity.IndentCostDtlEntity;
import com.vmfg.design.entity.IndentDtlTblEntity;
import com.vmfg.design.entity.IndentHdrDtlsEntity;
import com.vmfg.design.entity.IndentProjectEntity;
import com.vmfg.design.entity.IndentRemarksEntity;
import com.vmfg.design.entity.IndentRequestEntity;
import com.vmfg.design.entity.IndentTypeMstTblEntity;
import com.vmfg.design.entity.SalesIndentBudgetDtlEntity;
import com.vmfg.design.entity.UploadIndentTemplateEntity;
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
import com.vmfg.design.response.BudgetSummaryResponse;
import com.vmfg.design.response.getindentLifecycDtlResp;
import com.vmfg.design.response.getindentLifecycDtlResponse;
import com.vmfg.design.rowmapper.IndentDtlByPoRequest;
import com.vmfg.design.services.interfaces.IIndentUploadService;
import com.vmfg.general.dao.impl.DepartmentAndEmployeeDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.EmployeeForDepartmentEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.inventory.entity.InventoryMaterialTransferEntity;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.project.dao.interfaces.IBudgetExcessSheetDAO;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.project.request.ProjectInitiationMstRequest;
import com.vmfg.project.response.ProjectInternalResponse;
import com.vmfg.sales.dao.impl.EnquiryDAO;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.sales.entity.BudgetKeyCategory;
import com.vmfg.scm.dao.impl.IndentGroupDAO;
import com.vmfg.scm.dao.impl.IndentManagementDAO;
import com.vmfg.scm.dao.interfaces.IIndentGroupDAO;
import com.vmfg.scm.dao.interfaces.IPoDAO;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.scm.request.IndentDtlDeleteRequest;
import com.vmfg.scm.request.PodtlsForProductEntity;
import com.vmfg.task.dao.impl.DesignTaskDAO;
import com.vmfg.util.CommonNotifyMethod;
import com.vmfg.util.entity.Inventrytolocentity;

@Service
public class IndentUploadService implements IIndentUploadService {
	private static final Logger logger = LoggerFactory.getLogger(IndentUploadService.class);
	@Autowired
	private IIndentUploadDAO iIndentUploadDAO;

	@Autowired
	private StageManagementDAO stageManagementDAO;

	@Autowired
	private UploadManagementDAO uploadManagementDAO;
	

	@Autowired
	private DepartmentAndEmployeeDAO departmentAndEmployeeDAO;

	@Autowired
	private DesignTaskDAO DesignTaskDAO;

	@Autowired
	UploadManagementDAO deptM;
	
	@Autowired
	EnquiryDAO enquiryDAO;
	
	@Autowired
	IndentUploadDAO indentUploadDAO;
	
	@Autowired
	CommonNotifyMethod commonNotifyMethod;
	
	@Autowired
	IndentManagementDAO indentManagementDAO;
	
	@Autowired
	ProjectDAO projectDAO;

	@Autowired
	ChangeRequestDAO changeRequestDAO;

	@Autowired
	IPoDAO iPoDAO;

	@Autowired
	IIndentGroupDAO iIndentGroupDAO;

	@Autowired
	IBudgetExcessSheetDAO iBudgetExcessSheetDAO;

	@Autowired
	IndentGroupDAO indentGroupDAO;

	@Override
	public ResponseAsList uploadIndentTemplate(JSONArray getArray, MultipartFile file) {
		ResponseAsList returnList = new ResponseAsList();
		List<UploadIndentTemplateEntity> list = new ArrayList<UploadIndentTemplateEntity>();

		try {
			list = iIndentUploadDAO.uploadIndentTemplate(getArray, file);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("uploadIndentTemplate service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage insertIndentDtls(InsertIndentRequest InsertIndentReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> documentSeqList = new ArrayList<DocumentStatusMstEntity>();
		List<UploadIndentTemplateEntity> dtlList;
		List<Inventrytolocentity> lcnlist = null;
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		String indentCode="", projectCode="", sbcShortDesc="", keyAreaCode="", subKeyAreaCode="", runningNo = "";
		int newIndentId=0,indentId=0;
		try {
			String tenantId = InsertIndentReq.getTenantId();
			if(InsertIndentReq.getIndentId()==null|| InsertIndentReq.getIndentId().isEmpty()) {
				
				projectCode = iIndentUploadDAO.getProjectCodeByProjId(InsertIndentReq.getProjectId(), tenantId);
				sbcShortDesc = iIndentUploadDAO.getSbcShortDescBySbcCode(InsertIndentReq.getSbcCode(), tenantId);
				keyAreaCode = iIndentUploadDAO.getKeyAreaCodeByPkId(InsertIndentReq.getPkaId(), tenantId);
				subKeyAreaCode = iIndentUploadDAO.getSubKeyAreaCodeByPskId(InsertIndentReq.getPksaId(), tenantId);
				runningNo = String.valueOf(iIndentUploadDAO.getLastestRunningNo(tenantId,InsertIndentReq.getProjectId()));

				indentCode = projectCode + "-" + sbcShortDesc + "-" + keyAreaCode + "-" + subKeyAreaCode + "-" + runningNo;

				documentSeqList = stageManagementDAO.getDocDtlcurrentSeq(InsertIndentReq.getDocType(), "1", tenantId);

				String seqStatus = documentSeqList.get(0).getDocStatus();

				String deptCode = uploadManagementDAO.getDepCodeByEmpId(InsertIndentReq.getEmpId(), tenantId);

				newIndentId = iIndentUploadDAO.insertIndentHdrDtls(indentCode,
						InsertIndentReq.getExpectedDeliveryDate(), runningNo, InsertIndentReq.getSbcCode(),
						InsertIndentReq.getPkaId(), InsertIndentReq.getPksaId(), InsertIndentReq.getIndentType(),
						InsertIndentReq.getIndentDate(), InsertIndentReq.getProjectId(), InsertIndentReq.getMasterId(),
						InsertIndentReq.getEmpId(), seqStatus, tenantId, null, deptCode);
				
				// Insert for remarks
				iIndentUploadDAO.insertIndentStatusDtl(String.valueOf(newIndentId), "1", seqStatus, "Created", tenantId,
						InsertIndentReq.getEmpId(),InsertIndentReq.getDocType());

			}else {
				indentId=Integer.parseInt(InsertIndentReq.getIndentId());
				int revisionCheck = iIndentUploadDAO.getIndentRevCheck(indentId);
				if(revisionCheck > 1) {
				int revision=Integer.parseInt(iIndentUploadDAO.getIndentRevNo(indentId))+1;
				iIndentUploadDAO.updateIndentRevNo(String.valueOf(revision),indentId,InsertIndentReq.getEmpId());
				}
			}
			
			
			dtlList = InsertIndentReq.getDtlList();

			if (newIndentId > 0 || indentId > 0) {

				for (int i = 0; i < dtlList.size(); i++) {
					
					String make = dtlList.get(i).getMake() == "" ? null : dtlList.get(i).getMake();
					String weight = dtlList.get(i).getWeight() == "" ? null : dtlList.get(i).getWeight();
					String uom = "";
					
					// check for new uom
					uom = iIndentUploadDAO.getUomCodeByUnit(dtlList.get(i).getUnit(), tenantId);
						if (uom.equalsIgnoreCase("0")) {
							iIndentUploadDAO.createNewUomAndInsert(dtlList.get(i).getUnit().trim(), tenantId);
							uom = iIndentUploadDAO.getUomCodeByUnit(dtlList.get(i).getUnit(), tenantId);
						}
					
					String prodCategory="";
					if(InsertIndentReq.getIndentType().equalsIgnoreCase("IT001")){
						prodCategory="CN";
					}else if (InsertIndentReq.getIndentType().equalsIgnoreCase("IT002")){
						prodCategory="SE";
					}else {
						prodCategory="CN";
//						logger.error("Invalid Product Category");
					}
					
					// check for new product
					int productId = iIndentUploadDAO.checkProductCodeInProdMst(dtlList.get(i).getPartNumber(),dtlList.get(i).getDesc(),dtlList.get(i).getSpecification(),
							tenantId, InsertIndentReq.getProjectId(),prodCategory);
					if (productId == 0) {
						int prodId = iIndentUploadDAO.insertnewProductInProdMst(dtlList.get(i).getPartNumber().trim(),
								InsertIndentReq.getProjectId(), dtlList.get(i).getDesc(), uom, tenantId,
								InsertIndentReq.getEmpId(), dtlList.get(i).getSpecification(), make,
								dtlList.get(i).getQty(), uom, weight, dtlList.get(i).getMaterial(),
								InsertIndentReq.getPkaId(), InsertIndentReq.getPksaId(),prodCategory, "1");
						if (prodId > 0) {
							lcnlist = iIndentUploadDAO.getProdLcnList(tenantId);
							for (int l = 0; l < lcnlist.size(); l++) {
								iIndentUploadDAO.insertInvProdDtl(prodId, lcnlist.get(l).getTolocationcode(), tenantId);
							}
						}
					} else {
						iIndentUploadDAO.updateProductInProdMst( dtlList.get(i).getDesc(), uom,
								InsertIndentReq.getEmpId(), dtlList.get(i).getSpecification(), make,
								dtlList.get(i).getQty(), weight, dtlList.get(i).getMaterial(),
								InsertIndentReq.getPkaId(), InsertIndentReq.getPksaId(),prodCategory,productId);
						
					}
					
					if(dtlList.get(i).getIndentDtlId()==null ||dtlList.get(i).getIndentDtlId().isEmpty() ) {
						if(newIndentId == 0) {
							newIndentId = indentId;
						}
						int indentDtlId = iIndentUploadDAO.insertIndentDtl(newIndentId,
								dtlList.get(i).getPartNumber().trim(), dtlList.get(i).getDesc(),
								dtlList.get(i).getSpecification(), make, dtlList.get(i).getQty(), uom, weight,
								dtlList.get(i).getMaterial(), tenantId, dtlList.get(i).getRemarks());

						// default insert in indent_assign_team
						String empDesig = iIndentUploadDAO.getEmpIdByProjectCode(tenantId);
						String empArr[] = empDesig.split(",");
						for (int m = 0; m < empArr.length; m++) {
							List<EmployeeForDepartmentEntity> empList = departmentAndEmployeeDAO
									.getEmployeeForDesignation(InsertIndentReq.getTenantId(), empArr[m]);
							for (int p = 0; p < empList.size(); p++) {
								iIndentUploadDAO.insertIndentAssignTeam(String.valueOf(indentDtlId),
										empList.get(p).getEmployeeId(), "1", tenantId);
							}
						}	
					}else {
						int checkIndentGrp =iIndentUploadDAO.getCheckIndentScs(dtlList.get(i).getIndentDtlId());
					if(checkIndentGrp ==0) {
						iIndentUploadDAO.updateIndentDtl(dtlList.get(i).getPartNumber().trim(), dtlList.get(i).getDesc(),
								dtlList.get(i).getSpecification(), make, dtlList.get(i).getQty(), uom, weight,
								dtlList.get(i).getMaterial(), dtlList.get(i).getRemarks(),dtlList.get(i).getIndentDtlId());
					   }
					}
		
				}
				
				if(InsertIndentReq.getPkaId() != null &&  InsertIndentReq.getPksaId() != null) {
					if(!InsertIndentReq.getPksaId().isEmpty() && !InsertIndentReq.getPkaId().isEmpty()) {
						String cnt= iIndentUploadDAO.CheckIndentBudgetVal(indentId);
						if(cnt.equalsIgnoreCase("0") || cnt.equalsIgnoreCase("0.00")) {
							iIndentUploadDAO.updateIndentPkaAndPksaId(InsertIndentReq.getPkaId(), InsertIndentReq.getPksaId(),indentId, tenantId); 	
						}
					}
				}
						
				String nextApprDesig = iIndentUploadDAO.getIndentNxtAppDesc(InsertIndentReq.getDocType(), "1", InsertIndentReq.getIndentType(),InsertIndentReq.getTenantId());
				messageList.add(indentCode);
				commonNotifyMethod.InvokeNotificationMethod(1, 42, "", tenantId, messageList, otherEmpId, "1", InsertIndentReq.getPmId(), InsertIndentReq.getMasterId(), nextApprDesig);
				commonNotifyMethod.InvokeApprovalDesigMethod(InsertIndentReq.getPmId(), InsertIndentReq.getDocType(), String.valueOf(newIndentId),InsertIndentReq.getProjectId(), tenantId, InsertIndentReq.getEmpId(),nextApprDesig, iIndentUploadDAO.getEnqIdByProjectId(InsertIndentReq.getProjectId()),indentCode);
				
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage(Integer.toString(newIndentId));
				returnMessage.setResponseMessage(ResponseMessageMap.successUpload);
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage(("0"));
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("insertIndentDtls Method Exception --->" + ex);

		}
		return returnMessage;
	}

	// Picks the correct "committed PJS" threshold for a project: SCS_BUDGET_EXCESS (Project Approved)
	// for normal projects, CAPEX_SCS_BUDGET_EXCESS (Purchase Approved) only for capex projects.
	// Previously this was approximated with min(scsSeq, capexScsSeq), which always resolved to the
	// lower capex threshold and let non-capex PJS count as "committed" one stage too early.
	private String resolveCommittedScsMinSeqNo(String tenantId, String pmHdrId, String scsSeq, String capexScsSeq) {
		ProjectInternalResponse internalResp = projectDAO.getProjectInternal(tenantId, pmHdrId);
		boolean isCapex = internalResp != null && "1".equals(internalResp.getIsInternal());
		return isCapex ? capexScsSeq : scsSeq;
	}

	@Override
	public ResponseAsList getIndentDtlsByIndentId(IndentDtlRequest indentDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetIndentDtlEntity> mainList = new ArrayList<GetIndentDtlEntity>();
		GetIndentDtlEntity getList = new GetIndentDtlEntity();
		List<IndentDtlTblEntity> list = new ArrayList<IndentDtlTblEntity>();
		List<DocumentStatusMstEntity> docLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
		List<DocumentStatusMstEntity> currSeqDocLifeCycleMstList = new ArrayList<DocumentStatusMstEntity>();
//		List<PodtlsForProductEntity> poVendorDtls =new ArrayList<PodtlsForProductEntity>();
		String tenantId = indentDtlReq.getTenantId();
		int approveBtnEnable = 0;
		BigDecimal allocatedValue = BigDecimal.ZERO;
//		int dmId = 0;
		try {
			allocatedValue = iIndentUploadDAO.getindentBudgetval(indentDtlReq.getIndentId());
			list = iIndentUploadDAO.getIndentDtlsByIndentId(indentDtlReq.getProjectId(), indentDtlReq.getIndentId(), indentDtlReq.getTenantId());
			String targetValue = iIndentUploadDAO.getTargetValue(indentDtlReq.getIndentId());
			String costFlowType = iIndentUploadDAO.getCostFlowTypeByIndentId(indentDtlReq.getIndentId());
			String availableValue;
			if ("NEW".equalsIgnoreCase(costFlowType)) {
				// Real station-level Available Budget for NEW-flow: Allocated Value minus real
				// approved PO spend minus committed PJS quotes past "Project Approved" with no PO
				// yet - same formula already proven for the Budget Excess gate / PJS screen's
				// "Available Value" field, just reused here for this indent-detail popup.
				String pkaId = iIndentUploadDAO.getPkaIdByIndentId(indentDtlReq.getIndentId());
				BigDecimal stationAllocated = new BigDecimal(projectDAO.getAllocatedValSum(pkaId));
				BigDecimal approvedPoTotal = new BigDecimal(iPoDAO.getApprovedPoTotalByPkaId(pkaId));
				String scsSeq = iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", indentDtlReq.getTenantId());
				String capexScsSeq = iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", indentDtlReq.getTenantId());
				String minSeqNo = "1".equals(projectDAO.getIsInternalByPkaId(pkaId)) ? capexScsSeq : scsSeq;
				BigDecimal otherCommittedPjs = new BigDecimal(
						iIndentGroupDAO.getOtherCommittedScsTotalByPkaId(pkaId, "-1", minSeqNo));
				availableValue = stationAllocated.subtract(approvedPoTotal).subtract(otherCommittedPjs).toString();
			} else {
				availableValue = iIndentUploadDAO.getAvailableValue(indentDtlReq.getIndentId());
			}
			if (list.size() > 0) {
//				for (int i = 0; i < list.size(); i++) {
				String getPmId = "";
				if(indentDtlReq.getDepCode() != null) {
				    getPmId = iIndentUploadDAO.getPmIdbyDepCode(indentDtlReq.getTenantId(), indentDtlReq.getDepCode());
				}else {
					getPmId = indentDtlReq.getPmId();
				}
				   String isCnt = iIndentUploadDAO.getProjectInitiationMstResp(getPmId, indentDtlReq.getEmpId(),indentDtlReq.getTenantId());
                      if(isCnt.equalsIgnoreCase("0")) {
                    	 getList.setIsFlag(list.get(0).getCreatedBy().equalsIgnoreCase(indentDtlReq.getEmpId()) ? 1 : 0);     
                        }else {
                        	getList.setIsFlag(Integer.valueOf(isCnt));
                      }
//				 }
//				for (int i = 0; i < list.size(); i++) {
//					dmId = iIndentUploadDAO.getDmIdByLatestVerion(list.get(i).getIndentDtlId(),
//							indentDtlReq.getTenantId());
//					list.get(i).setDmId(dmId);
//					//BigDecimal totaQty = iIndentUploadDAO.getTotalQty(list.get(i).getIndentDtlId());
//					//BigDecimal totaValue = iIndentUploadDAO.getTotalValue(list.get(i).getIndentDtlId());
//					list.get(i).setTotalQty("0");
//					list.get(i).setTotalVal("0");
//					
//					String assignTeamEmpId = iIndentUploadDAO.getEmpIdByIndentDtlId(list.get(i).getIndentDtlId());
//					if (!assignTeamEmpId.equalsIgnoreCase("0")) {
//						list.get(i).setAssignTeamEmpId(assignTeamEmpId);
//						list.get(i).setAssignTeamEmpName(iIndentUploadDAO.getEmpNameByEmpId(assignTeamEmpId));
//					}
//					
//					//Po Dtls of latest vendors
//					poVendorDtls=iIndentUploadDAO.getLatestPodtls(list.get(i).getProductCode(),indentDtlReq.getProjectId(),indentDtlReq.getTenantId());
//					list.get(i).setPoDtlList(poVendorDtls);
//				}
				
			
//				list.parallelStream().forEach(item -> {
				
//				    int dmId = iIndentUploadDAO.getDmIdByLatestVerion(item.getIndentDtlId(), indentDtlReq.getTenantId());
//				    item.setDmId(dmId);

//				    item.setTotalQty("0");
//				    item.setTotalVal("0");

//				    String assignTeamEmpId = iIndentUploadDAO.getEmpIdByIndentDtlId(item.getIndentDtlId());
//				    if (!assignTeamEmpId.equalsIgnoreCase("0")) {
//				        item.setAssignTeamEmpId(assignTeamEmpId);
//				        item.setAssignTeamEmpName(iIndentUploadDAO.getEmpNameByEmpId(assignTeamEmpId));
//				    }

				    // Get Po Dtls of latest vendors
//				    List<PodtlsForProductEntity> poVendorDetails = iIndentUploadDAO.getLatestPodtls(item.getProductCode(), indentDtlReq.getProjectId(), indentDtlReq.getTenantId());
//				    item.setPoDtlList(poVendorDetails);
//				});

				
				String indentTypeCode = iIndentUploadDAO.getIndentTypeCodeByIndentId(indentDtlReq.getIndentId(),
						indentDtlReq.getTenantId());
				String docGroup = indentTypeCode;

				int currentSeq = iIndentUploadDAO.getCurrentSeqByIndentId(indentDtlReq.getIndentId(),
						indentDtlReq.getTenantId());
				docLifeCycleMstList = DesignTaskDAO.getNextSeqandStatusByDoc(currentSeq, indentDtlReq.getDocType(), tenantId,docGroup);

				if (docLifeCycleMstList.size() > 0) {
					String designCode = uploadManagementDAO.getDesigCodeByEmpId(indentDtlReq.getEmpId(),
							indentDtlReq.getTenantId());

					currSeqDocLifeCycleMstList = stageManagementDAO.getDocDtlcurrentSeqByDocGrp(indentDtlReq.getDocType(),
							String.valueOf(currentSeq), tenantId,docGroup);

					approveBtnEnable = iIndentUploadDAO.getApprovebtnEnable(designCode, docGroup,
							indentDtlReq.getTenantId(), indentDtlReq.getDocType(), docLifeCycleMstList.get(0).getCurrSequence());
					if (approveBtnEnable == 1) {
						// curr seq
						docLifeCycleMstList.get(0).setDocTypeDesc(iIndentUploadDAO
								.getDocTypeDescByDocType(docLifeCycleMstList.get(0).getDocType(), tenantId));
						docLifeCycleMstList.get(0)
								.setDocStatusDesc(DesignTaskDAO.getStatusByDesc(
										iIndentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
												docLifeCycleMstList.get(0).getCurrSequence(), tenantId, indentDtlReq.getDocType(), docGroup),
										tenantId));// Current seq docStatus

						// Previous Seq
						docLifeCycleMstList.get(0).setPreviousSeq(String.valueOf(currentSeq));
						docLifeCycleMstList.get(0).setPreviousSeqStatusCode(iIndentUploadDAO
								.getStatusCodebySeqAndDocTypeByDocGrp(String.valueOf(currentSeq), tenantId, indentDtlReq.getDocType(), docGroup));

						docLifeCycleMstList.get(0).setPreviousSeqStatusDesc(
								DesignTaskDAO.getStatusByDesc(iIndentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
										String.valueOf(currentSeq), tenantId, indentDtlReq.getDocType(), docGroup), tenantId));

						if (docLifeCycleMstList.get(0).getNextSeq() != null
								&& !docLifeCycleMstList.get(0).getNextSeq().equalsIgnoreCase("")) {
							docLifeCycleMstList.get(0)
									.setNextSeqStatusCode(iIndentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
											docLifeCycleMstList.get(0).getNextSeq(), tenantId, indentDtlReq.getDocType(), docGroup));

							docLifeCycleMstList.get(0).setNextSeqStatusDesc(DesignTaskDAO.getStatusByDesc(
									iIndentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
											String.valueOf(docLifeCycleMstList.get(0).getNextSeq()), tenantId,indentDtlReq.getDocType(), docGroup),
									tenantId));
						}

						// cancel seq
						if (currSeqDocLifeCycleMstList.get(0).getCancelSeq() != null) {
							docLifeCycleMstList.get(0).setCancelSeq(currSeqDocLifeCycleMstList.get(0).getCancelSeq());
							docLifeCycleMstList.get(0)
									.setCancelStatusCode(iIndentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
											currSeqDocLifeCycleMstList.get(0).getCancelSeq(), tenantId, indentDtlReq.getDocType(), docGroup));

							docLifeCycleMstList.get(0).setCancelStatusDesc(DesignTaskDAO.getStatusByDesc(
									iIndentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(
											currSeqDocLifeCycleMstList.get(0).getCancelSeq(), tenantId, indentDtlReq.getDocType(), docGroup),
									tenantId));
						}

						getList.setDocLifeCycleMstList(docLifeCycleMstList);
					}
				}

				getList.setApproveBtnEnable(approveBtnEnable);
				getList.setTargetValue("0");
				;
				getList.setDtlList(list);
				getList.setAllocatedValue(allocatedValue.toString());
				getList.setAvilablevalue(availableValue);
				getList.setTargetValue(targetValue);
				getList.setCostFlowType(costFlowType);
				getList.setSeq(iIndentUploadDAO.getCurIndentSeq(indentDtlReq.getIndentId()));
				mainList.add(getList);
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getIndentDtlsByIndentId Method Exception --->" + ex);

		}
		return returnList;
	}
	

	@Override
	public ResponseAsList getIndentDtlsByPoIndentId(IndentDtlByPoRequest indentDtlPoReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<PodtlsForProductEntity> poVendorDtls =new ArrayList<PodtlsForProductEntity>();
		try {	
				poVendorDtls=iIndentUploadDAO.getLatestPodtls(indentDtlPoReq.getProdCode(),
						indentDtlPoReq.getProjId(),indentDtlPoReq.getTenantId());
			if (poVendorDtls.size() > 0) {
				returnList.setResponseData(poVendorDtls);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			 } else {
				returnList.setResponseData(poVendorDtls);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getIndentDtlsIndentId Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public ResponseAsList getIndentHdrDtlsByProjectId(getIndentHdrDtlRequest getIndentHdrDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<IndentHdrDtlsEntity> list = new ArrayList<IndentHdrDtlsEntity>();

		try {
			String dept = deptM.getDepCodeByEmpId(getIndentHdrDtlReq.getEmpId(), getIndentHdrDtlReq.getTenantId());	
			list = iIndentUploadDAO.getIndentHdrDtlsByProjectId(getIndentHdrDtlReq.getProjectId(),
					getIndentHdrDtlReq.getTenantId(),dept);
			if (list.size() > 0) {		
//				for (int i = 0; i < list.size(); i++) {
				list.parallelStream().forEach(item -> {
					int isFlag = iIndentUploadDAO.indentisFlagReApproval(item.getIndentId(),
							getIndentHdrDtlReq.getTenantId());
					item.setIsFlag(isFlag);
					String indentTypeCode = iIndentUploadDAO.getIndentTypeCodeByIndentId(item.getIndentId(),
							getIndentHdrDtlReq.getTenantId());
					String docGroup = indentTypeCode;

					int currentSeq = iIndentUploadDAO.getCurrentSeqByIndentId(item.getIndentId(),
							getIndentHdrDtlReq.getTenantId());
					
					List<DocumentStatusMstEntity> docLifeCycleMstList = DesignTaskDAO.getNextSeqandStatusByDoc(currentSeq, getIndentHdrDtlReq.getDocType(), getIndentHdrDtlReq.getTenantId(),docGroup);
					if(docLifeCycleMstList.size()>0) {
						String docStatDesc =changeRequestDAO.getStatusDescByStatusCode(docLifeCycleMstList.get(0).getDocStatus(), getIndentHdrDtlReq.getTenantId());
						item.setNextstatusDesc(docStatDesc);
					}
					int getNoOfProducts = iIndentUploadDAO.getNoOfProductsByIndentID(item.getIndentId(),
							getIndentHdrDtlReq.getTenantId());
					item.setNoOfProductsCount(getNoOfProducts);
					item.setIndentClosed(indentManagementDAO.getIndentClosedStatus(item.getIndentId()));
					item.setVerCheck(indentManagementDAO.indentVerCheck(item.getIndentId(),getIndentHdrDtlReq.getTenantId()));
				});
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getIndentHdrDtlsByProjectId Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public ResponseAsList getIndentTypeMstDropDown(TenantEmpRequest tenantReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<IndentTypeMstTblEntity> list = new ArrayList<IndentTypeMstTblEntity>();
		try {
			if(tenantReq.getEmpId() != null) {
			String dept = deptM.getDepCodeByEmpId(tenantReq.getEmpId(), tenantReq.getTenantID());
			list = iIndentUploadDAO.getIndentTypeMstDropDown(tenantReq.getTenantID(), dept,tenantReq.getIsInternal());
			}else {
				list = iIndentUploadDAO.getIndentTypeMstDropDown(tenantReq.getTenantID(), null,tenantReq.getIsInternal());	
			}
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getIndentTypeMstDropDown Method Exception --->" + ex);

		}
		return returnList;
	}

	@Override
	public ResponseAsMessage insertIndentFileByIndentID(JSONObject jsonObj, MultipartFile file) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<DocumentStatusMstEntity> list = new ArrayList<DocumentStatusMstEntity>();
		int newDmId = 0;
		try {
			JSONArray iluoArray = jsonObj.getJSONArray("reqObj");
			String enquiryId = "",docType="",uploadDocType="";
			String tenantId = "";
			String type = "";
			String empId = "";
			String refId = "";
			String projectId = "";
			String stageCode = "";
			String remarks = "";
			String indentHdrId = "";
			String indentDtlId = "";
//			String documentType = "DC018";
//			String uploadDocType = "FC015";

//			String documentName="";
			for (int l = 0; l < iluoArray.length(); l++) {
				JSONObject iluoobjects = iluoArray.getJSONObject(l);
				JSONArray iluobodykeys = iluoobjects.names();
				for (int k = 0; k < iluobodykeys.length(); ++k) {
					String key = iluobodykeys.getString(k);
					String value = iluoobjects.getString(key);
					if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					} else if (key.equalsIgnoreCase("empId")) {
						empId = value;
					} else if (key.equalsIgnoreCase("type")) {
						type = value;
					} else if (key.equalsIgnoreCase("stageCode")) {
						stageCode = value;
					} else if (key.equalsIgnoreCase("enquiryId")) {
						enquiryId = value;
					} else if (key.equalsIgnoreCase("projectId")) {
						projectId = value;
					} else if (key.equalsIgnoreCase("indentHdrId")) {
						indentHdrId = value;
					} else if (key.equalsIgnoreCase("indentDtlId")) {
						indentDtlId = value;
					}else if (key.equalsIgnoreCase("docType")) {
						docType = value;
					} else if (key.equalsIgnoreCase("uploadDocType")) {
						uploadDocType = value;
					}

				}
			}

			String getfileName = file.getOriginalFilename();
			String fileName = getfileName.substring(0, getfileName.lastIndexOf('.'));

			String productCode = fileName;
			if (indentDtlId.equalsIgnoreCase("")) {
				refId = iIndentUploadDAO.getIndentDtlIdByProductCode(productCode, tenantId, indentHdrId);
			} else {
				refId = indentDtlId;
			}
			if (!refId.equalsIgnoreCase("0")) {

				int version = 0;
				int checkCount = uploadManagementDAO.getCountByComb(tenantId, uploadDocType, refId, stageCode);
				if (checkCount > 0) {
					version = uploadManagementDAO.getLatestVersionbycomb(tenantId, uploadDocType, refId, stageCode);
					version = version + 1;
				} else {
					version = 1;
				}
				list = uploadManagementDAO.getSeqAndStatusByDocTypeCode(docType, tenantId);
				if (list.size() > 0) {
					newDmId = uploadManagementDAO.insertDocumentDtls(enquiryId, projectId, productCode, refId,
							stageCode, uploadDocType, version, tenantId, remarks, list.get(0).getCurrSequence(), "0",
							docType);
				}

				if (newDmId > 0) {
					int insertFileDtls = uploadManagementDAO.insertNewFileDtl(file, tenantId, newDmId, uploadDocType,
							empId, version, docType, type, refId);
					if (insertFileDtls == 1) {
						returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnMessage.setResponseDataMessage("Successfully Uploaded");
						returnMessage.setResponseMessage(ResponseMessageMap.successUpload);
					} else {

						returnMessage.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						returnMessage.setResponseDataMessage(getfileName);
						returnMessage.setResponseMessage(ResponseMessageMap.noRecord);
					}
				}

			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Uploaded file doesn't match with the product updated.");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		} catch (Exception ex) {
			logger.error("insertIndentFileByIndentID error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsMessage updateIndentHdrStatus(UpdateHdrRequest updateHdrReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {
			String docType = updateHdrReq.getDocType();
			String indentTypeCode = iIndentUploadDAO.getIndentTypeCodeByIndentId(updateHdrReq.getIndentId(),
					updateHdrReq.getTenantId());
			
			String seqStatus = iIndentUploadDAO.getStatusCodebySeqAndDocTypeByDocGrp(updateHdrReq.getCurrentseq(),
					updateHdrReq.getTenantId(), docType, indentTypeCode);

				if (seqStatus == null || seqStatus.isEmpty()) {
					returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
					returnMessage.setResponseDataMessage("Failure");
					returnMessage.setResponseMessage("Status code not found for sequence " + updateHdrReq.getCurrentseq());
					return returnMessage;
				}

			if ("DS020".equals(seqStatus)) {
				int ungroupedCount = iIndentUploadDAO.getUngroupedIndentDtlCount(
						updateHdrReq.getIndentId(), updateHdrReq.getTenantId());
				if (ungroupedCount > 0) {
					returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
					returnMessage.setResponseDataMessage("Failure");
					returnMessage.setResponseMessage(ungroupedCount + " item(s) not yet added to a PJS indent group. Please complete grouping before SCM Verification.");
					return returnMessage;
				}
			}

			String nextApprDesig = iIndentUploadDAO.getIndentNxtAppDesc(docType, updateHdrReq.getCurrentseq(), indentTypeCode,updateHdrReq.getTenantId());
			
			int updateIdentHdr = iIndentUploadDAO.updateIndentHdrStatusAndDesig(updateHdrReq.getIndentId(),
					updateHdrReq.getCurrentseq(), seqStatus, nextApprDesig, updateHdrReq.getEmpId(), updateHdrReq.getTenantId());
			
			if (updateIdentHdr > 0) {
				int insertIndentStatusDtl = iIndentUploadDAO.insertIndentStatusDtl(updateHdrReq.getIndentId(),
						updateHdrReq.getCurrentseq(), seqStatus, updateHdrReq.getRemarks(), updateHdrReq.getTenantId(),
						updateHdrReq.getEmpId(),updateHdrReq.getDocType());
				if (insertIndentStatusDtl > 0) {
					
					String projectId=iIndentUploadDAO.getProjectIdByIndentId(updateHdrReq.getIndentId());
					messageList.add(iIndentUploadDAO.getIndentCodeByIndentId(updateHdrReq.getIndentId()));
					
					commonNotifyMethod.InvokeNotificationMethod(1, 41, null, updateHdrReq.getTenantId(), messageList, otherEmpId, "1", updateHdrReq.getPmId(), updateHdrReq.getMasterId(), nextApprDesig);
					commonNotifyMethod.InvokeApprovalDesigMethod(updateHdrReq.getPmId(), docType, updateHdrReq.getIndentId(),iIndentUploadDAO.getProjectIdByIndentId(updateHdrReq.getIndentId()), updateHdrReq.getTenantId(), updateHdrReq.getEmpId(),nextApprDesig, iIndentUploadDAO.getEnqIdByProjectId(projectId),iIndentUploadDAO.getIndentCodeByIndentId(updateHdrReq.getIndentId()));

					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage("Success");
					returnMessage.setResponseMessage(ResponseMessageMap.successInserted);
				}
			} else {

				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("updateIndentHdrStatus error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsMessage deleteIndentByIndentDtlId(IndentDtlDeleteRequest indentDtlDeleteReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		int del = 0;
		try {
				int checkScsCount = iIndentUploadDAO.getScsCountByIndentDtlId(indentDtlDeleteReq.getIndentDtlId());
				if (checkScsCount == 0) {
					
					String indentHdrId = iIndentUploadDAO.getIndentHdrId(indentDtlDeleteReq.getIndentDtlId());
					int indentDtlCnt =0;
					if(!indentHdrId.equalsIgnoreCase("0")) {
						indentDtlCnt =iIndentUploadDAO.getNoOfProductsByIndentID(indentHdrId,
								indentDtlDeleteReq.getTenantId());
					}
					
					del = iIndentUploadDAO.deleteIndentByIndentDtlId(indentDtlDeleteReq.getIndentDtlId());
					
					if(indentDtlCnt == 1) {
					List<IndentBudgetDtlByIndentIdEntity> indentBudgetList = iIndentUploadDAO.getBudgetByIndentId(indentHdrId,indentDtlDeleteReq.getTenantId())	;
					for(int q =0;q< indentBudgetList.size();q++) {
						
						String pkseId = iIndentUploadDAO.getPkseIdBypkaId(indentBudgetList.get(q).getPkaId(), indentBudgetList.get(q).getSbExtnId());
						if(!pkseId.equalsIgnoreCase("")) {
						projectDAO.updateBudgetQtyAndval(pkseId, indentBudgetList.get(q).getBudgetQty(), indentBudgetList.get(q).getBudgetValue());
						iIndentUploadDAO.updateBudgetValInPKA(indentBudgetList.get(q).getBudgetValue(), indentBudgetList.get(q).getPkaId(), "-");
						projectDAO.deleteindentBudGetId(indentBudgetList.get(q).getIndentBudId());
						}
					}
					String indentCode=	iIndentUploadDAO.getIndentCodeByIndentId(indentHdrId);
					
					iIndentUploadDAO.deleteBudgetExcessStatusTbl(indentHdrId, indentDtlDeleteReq.getTenantId());
					iIndentUploadDAO.deleteBudgetExcessTbl(indentHdrId, indentDtlDeleteReq.getTenantId());
					
					
						iIndentUploadDAO.deleteIndentHdr(indentHdrId,indentDtlDeleteReq.getTenantId());
						iIndentUploadDAO.deleteIndentStatusTbl(indentHdrId,indentDtlDeleteReq.getTenantId());
						iIndentUploadDAO.updatenotification(indentCode, "DC018", indentDtlDeleteReq.getTenantId());
					}
					if(del==1) {
						if(indentDtlDeleteReq.getDmId()!="") {
							iIndentUploadDAO.deleteDocumentManagement(indentDtlDeleteReq.getDmId());
							iIndentUploadDAO.deleteFileManager(indentDtlDeleteReq.getDmId());
						}
						rm.setResponseCode(ResponseMessageMap.responseCodeOk);
						rm.setResponseMessage(ResponseMessageMap.successfulDeleted);
					}else {
						rm.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						rm.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
					}
				}else {
					rm.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					rm.setResponseMessage("PJS raised for this Indent");
					rm.setResponseDataMessage(ResponseMessageMap.deleteUnSuccessful);
				}
		
	}catch (Exception ex) {
			logger.error("deleteIndentByIndentDtlId error " + ex);
		}
		return rm;
	}
	
	@Override
	public ResponseAsMessage getStartIndentReqStatus(getStartIndentRequest getStartIndentReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		try {

		//	String pmHdrId = iIndentUploadDAO.getPmHdrIdByDesignId(getStartIndentReq.getHdrId(),
		//			getStartIndentReq.getTenantId());
			int pmSeq = iIndentUploadDAO.getPmSeqByPmHdrId(getStartIndentReq.getHdrId(), getStartIndentReq.getTenantId());
			int checkLastSeq = iIndentUploadDAO.checkLastSeqByPmId(getStartIndentReq.getPmId(), pmSeq,
					getStartIndentReq.getTenantId());
			if (checkLastSeq == 1) {
				iIndentUploadDAO.updateStartIndentReq(getStartIndentReq.getHdrId());
			}
			String startIndentReqStatus = iIndentUploadDAO.getStartIndentReqStatus(getStartIndentReq.getHdrId(),
					getStartIndentReq.getTenantId());

			returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnMessage.setResponseDataMessage(startIndentReqStatus);
			returnMessage.setResponseMessage(ResponseMessageMap.success);

		} catch (Exception ex) {
			logger.error("getStartIndentReqStatus error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getIndentRemarks(IndentRemarksRequest indentRemReq) {

		ResponseAsList returnList = new ResponseAsList();
		List<IndentRemarksEntity> indentRemark = null;
		try {
			indentRemark = iIndentUploadDAO.getIndentRemarks(indentRemReq.getIndentHdrId(), indentRemReq.getTenantID());
			if (indentRemark.size() > 0) {
				returnList.setResponseData(indentRemark);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getStartIndentReqStatus error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage updateBudgetDtl(BudgetValueUpdateRequest budgetValueUpdateReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		int updateStatus = 0;
		try {

			String pkaId = iIndentUploadDAO.getPkaIdByIndentId(budgetValueUpdateReq.getIndentId());
			String budgetValue = iIndentUploadDAO.getBudgetValues(budgetValueUpdateReq.getIndentId(),pkaId);
//			iIndentUploadDAO.updateBudgetValInPKA(budgetValue, pkaId, "-");
if(budgetValueUpdateReq.getTargetValue() == null) {
	budgetValueUpdateReq.setTargetValue("0");
}
			updateStatus = iIndentUploadDAO.updateIndentCostDtl(budgetValue,
					budgetValueUpdateReq.getTargetValue(), budgetValueUpdateReq.getDate(),
					budgetValueUpdateReq.getIndentId());

//			iIndentUploadDAO.updateBudgetValInPKA(budgetValueUpdateReq.getBudgetValue(), pkaId, "+");

			if (updateStatus == 1) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseDataMessage(ResponseMessageMap.success);
				returnMessage.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {

				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage(ResponseMessageMap.failToupdateMsg);
				returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
		} catch (Exception ex) {
			logger.error("updateBudgetDtl error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getIndentByIndentID(IndentRequestEntity indentReq) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<IndentProjectEntity> proj = iIndentUploadDAO.getIndentByIndentID(indentReq);

			if (proj.size() > 0) {
			//	proj.get(0).setTotalBudgetConsumed(iIndentUploadDAO.totalbudgetConsumed(indentReq.getIndentId()));
				// PJS No. applies regardless of cost flow type (every PJS gets one at creation,
				// see IndentGroupService.insertScpDtlsByIgHdrId) - resolved here, not inside the
				// NEW-flow branch below, so it shows for legacy indents too.
				String scsIdForPjsRef = iIndentGroupDAO.getScsIdByIndentId(indentReq.getIndentId());
				if (!scsIdForPjsRef.isEmpty()) {
					proj.get(0).setPjsRefNo(
							iIndentGroupDAO.getPjsRefNoByIgScsId(scsIdForPjsRef, indentReq.getTenantId()));
				}
				if ("NEW".equalsIgnoreCase(proj.get(0).getCostFlowType())) {
					// NEW-flow: Budget Cost/Budget Consumed are hidden on this screen (see
					// project_budget_target_cost_removal), replaced with real station numbers -
					// same formula pieces as the Budget Excess Sheet gate in IndentGroupService.
					String pkaId = iIndentUploadDAO.getPkaIdByIndentId(indentReq.getIndentId());
					proj.get(0).setPkaId(pkaId);
					String scsBudgetExcessSeq = "8".equalsIgnoreCase(indentReq.getProcessCode())
							? iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", indentReq.getTenantId())
							: iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", indentReq.getTenantId());
					BigDecimal allocatedValue = new BigDecimal(projectDAO.getAllocatedValSum(pkaId));
					BigDecimal approvedPoTotal = new BigDecimal(iPoDAO.getApprovedPoTotalByPkaId(pkaId));
					BigDecimal otherCommittedPjs = new BigDecimal(iIndentGroupDAO
							.getOtherCommittedScsTotalByPkaId(pkaId, indentReq.getIndentId(), scsBudgetExcessSeq));
					// Reserves the full value of any other indent at this station with a Budget
					// Excess raised but not yet approved - same reservation the "Project Approved"
					// gate now checks (see IndentGroupService.updateScpSeqAndStatus) - folded in here
					// too so this screen's Available Value/isShortfall can't show "fine" right before
					// the gate blocks the same click for a reason this display doesn't know about.
					BigDecimal reservedPendingExcess = new BigDecimal(iIndentGroupDAO
							.getPendingBudgetExcessReservedTotalByPkaId(pkaId, indentReq.getIndentId(), scsBudgetExcessSeq));
					BigDecimal actualConsumedValue = approvedPoTotal.add(otherCommittedPjs).add(reservedPendingExcess);
					proj.get(0).setAllocatedValue(allocatedValue.toString());
					proj.get(0).setActualConsumedValue(actualConsumedValue.toString());
					proj.get(0).setApprovedPoAmount(approvedPoTotal.toString());
					proj.get(0).setCommittedPjsAmount(otherCommittedPjs.toString());
					proj.get(0).setReservedPendingExcessAmount(reservedPendingExcess.toString());

					// Is this station short on this SCS's own quote, and if so, is there any
					// unallocated Sales Budget left anywhere in the project for the PM to pull
					// from? Drives the "Allocate to Station" button on the PM's PJS screen.
					BigDecimal remainingStationBudget = allocatedValue.subtract(actualConsumedValue);
					BigDecimal currentQuoteValue = new BigDecimal(
							indentGroupDAO.getindentScmVal(indentReq.getIndentId()));
					boolean isShortfall = remainingStationBudget.compareTo(currentQuoteValue) < 0;
					boolean canAllocate = false;
					if (isShortfall) {
						String projectId = iIndentUploadDAO.getProjectIdByIndentId(indentReq.getIndentId());
						String mstId = projectDAO.getmstIdByPmHdrId(projectId, indentReq.getTenantId());
						BigDecimal unallocatedProjectBudget = new BigDecimal(
								projectDAO.getUnallocatedSalesBudgetTotalByMstId(mstId, indentReq.getTenantId()));
						canAllocate = unallocatedProjectBudget.compareTo(BigDecimal.ZERO) > 0;
					}
					proj.get(0).setCanAllocateFromSalesBudget(String.valueOf(canAllocate));
					proj.get(0).setIsShortfall(String.valueOf(isShortfall));

					String scsId = iIndentGroupDAO.getScsIdByIndentId(indentReq.getIndentId());
					boolean hasBudgetExcess = !scsId.isEmpty() && iIndentGroupDAO.getBudgetExcessDtlCount(scsId) > 0;
					proj.get(0).setHasBudgetExcess(String.valueOf(hasBudgetExcess));
				}
				returnList.setResponseData(proj);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(proj);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getIndentByIndentID error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getCostAnlysDtls(HdrIdandTenantIdRequest cstDtl) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<BudgetSummaryResponse> respList = new ArrayList<BudgetSummaryResponse>();
			BudgetSummaryResponse resp = new BudgetSummaryResponse();
			BigDecimal indentedMaterial = BigDecimal.ZERO;
			BigDecimal poReleased = BigDecimal.ZERO;
			String dnValue = "";
			BigDecimal rmcBudgetStatus = BigDecimal.ZERO;
			String materialTransferVal=iIndentUploadDAO.getSumOfTransferValue(cstDtl);
			List<BudgetKeyCategory> catLi = iIndentUploadDAO.getbudgetKeyCategoryList(cstDtl.getTenantId());
			List<CatbaseIndentCostDtlEntity> catbaseIndentList = new ArrayList<CatbaseIndentCostDtlEntity>();

			String costFlowTypeForCat = projectDAO.getCostFlowTypeByPmHdrId(cstDtl.getHdrId());
			String minSeqNoForCat = null;
			Map<String, BigDecimal> approvedPoTotalBySbcCode = new HashMap<>();
			Map<String, BigDecimal> committedScsTotalBySbcCode = new HashMap<>();
			Map<String, BigDecimal> budgetExcessApprovedBySbcCode = new HashMap<>();
			if ("NEW".equalsIgnoreCase(costFlowTypeForCat)) {
				String scsSeqForCat = iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", cstDtl.getTenantId());
				String capexScsSeqForCat = iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", cstDtl.getTenantId());
				minSeqNoForCat = resolveCommittedScsMinSeqNo(cstDtl.getTenantId(), cstDtl.getHdrId(), scsSeqForCat, capexScsSeqForCat);
				// Fetch per-category totals once for the whole project instead of once per category in the loop below -
				// same batching approach used for the WBS screen, avoids an O(3N) round-trip pattern per category.
				approvedPoTotalBySbcCode = iPoDAO.getApprovedPoTotalGroupedBySbcCode(cstDtl.getHdrId());
				committedScsTotalBySbcCode = iIndentGroupDAO.getCommittedScsTotalGroupedBySbcCode(cstDtl.getHdrId(), minSeqNoForCat);
				budgetExcessApprovedBySbcCode = iBudgetExcessSheetDAO.getApprovedExcessTotalGroupedBySbcCode(cstDtl.getHdrId(), minSeqNoForCat);
			}

			for (int i = 0; i < catLi.size(); i++) {
				CatbaseIndentCostDtlEntity catbaseIndent = new CatbaseIndentCostDtlEntity();
				String poCheck = iIndentUploadDAO.cheackPoCreateOrNot(cstDtl.getHdrId(),cstDtl.getTenantId());
				List<IndentCostDtlEntity> proj = iIndentUploadDAO.getCostAnlysDtlslist(poCheck,cstDtl.getHdrId(),
						cstDtl.getTenantId(), catLi.get(i).getKeyCatCode());
				
				//call dao for po check
//				if(!poCheck.isEmpty()) {
//					proj.get(i).setBudgetStatus("0");
//				}
				BigDecimal totalBudgetCost = BigDecimal.ZERO;
				BigDecimal totalPoCost = BigDecimal.ZERO;
				BigDecimal finalBudgetStatus = BigDecimal.ZERO;
				BigDecimal totalTargetCode = BigDecimal.ZERO;
				
				if (proj.size() > 0) {
//					for (int j = 0; j < proj.size(); j++) {
//
//					}
					totalBudgetCost = proj.stream().map(x -> new BigDecimal(x.getBudgetValue())).reduce(BigDecimal.ZERO,
							BigDecimal::add);
//					totalBudgetCost=totalBudgetCost.subtract(new BigDecimal(materialTransferVal));
					
					totalPoCost = proj.stream().map(x -> new BigDecimal(x.getScmBudgetAllocated()))
							.reduce(BigDecimal.ZERO, BigDecimal::add);
//					totalPoCost=totalBudgetCost.subtract(new BigDecimal(materialTransferVal));
					totalTargetCode = proj.stream().map(x -> new BigDecimal(x.getTargetValue())).reduce(BigDecimal.ZERO,
							BigDecimal::add);

//					totalTargetCode=totalBudgetCost.subtract(new BigDecimal(materialTransferVal));
					finalBudgetStatus = totalBudgetCost.subtract(totalPoCost);
					catbaseIndent.setFinalBudgetStatus(finalBudgetStatus.toString());
					catbaseIndent.setTotalBudgetCost(totalBudgetCost.toString());
					catbaseIndent.setTotalPoCost(totalPoCost.toString());
					catbaseIndent.setTotalTargetCost(totalTargetCode.toString());
					catbaseIndent.setIndentCostDtl(proj);
					indentedMaterial = indentedMaterial.add(totalBudgetCost);
					poReleased = poReleased.add(totalPoCost);
					dnValue = proj.get(0).getDnValue();

					if ("NEW".equalsIgnoreCase(costFlowTypeForCat)) {
						String sbcCode = catLi.get(i).getKeyCatCode();
						BigDecimal approvedPoTotalForCat = approvedPoTotalBySbcCode.getOrDefault(sbcCode, BigDecimal.ZERO);
						BigDecimal committedScsTotalForCat = committedScsTotalBySbcCode.getOrDefault(sbcCode, BigDecimal.ZERO);
						BigDecimal budgetExcessApprovedForCat = budgetExcessApprovedBySbcCode.getOrDefault(sbcCode, BigDecimal.ZERO);
						catbaseIndent.setConsumedSoFar(approvedPoTotalForCat.add(committedScsTotalForCat).toString());
						catbaseIndent.setBudgetExcessApproved(budgetExcessApprovedForCat.toString());
					}

					catbaseIndentList.add(catbaseIndent);
				}

			}
			BigDecimal totalSalesBudget = iIndentUploadDAO.totalsalesBudget(cstDtl.getHdrId());
			BigDecimal AvailableBudget = totalSalesBudget.subtract(poReleased);
			AvailableBudget=AvailableBudget.subtract(new BigDecimal(materialTransferVal));
			totalSalesBudget=totalSalesBudget.subtract(new BigDecimal(materialTransferVal));
			rmcBudgetStatus = indentedMaterial.subtract(poReleased);
			poReleased=poReleased.add(new BigDecimal(materialTransferVal));
			resp.setCatbaseIndentCostDtl(catbaseIndentList);
			resp.setIndentedMaterial(indentedMaterial.toString());
			resp.setPoReleased(poReleased.toString());
			resp.setSalesTotalBudget(totalSalesBudget.toString());
			resp.setRmcBudgetStatus(rmcBudgetStatus.toString());
			resp.setMaterialTransfeCost(materialTransferVal);
			resp.setAvailablebudgetOnDate(AvailableBudget.toString());
			resp.setDebitNoteValue(dnValue);

			String costFlowType = projectDAO.getCostFlowTypeByPmHdrId(cstDtl.getHdrId());
			resp.setCostFlowType(costFlowType);
			if ("NEW".equalsIgnoreCase(costFlowType)) {
				String allocatedValue = projectDAO.getAllocatedValSumByPmHdrId(cstDtl.getHdrId());
				String scsSeq = iIndentGroupDAO.getTenantPropertyVal("SCS_BUDGET_EXCESS", cstDtl.getTenantId());
				String capexScsSeq = iIndentGroupDAO.getTenantPropertyVal("CAPEX_SCS_BUDGET_EXCESS", cstDtl.getTenantId());
				String minSeqNo = resolveCommittedScsMinSeqNo(cstDtl.getTenantId(), cstDtl.getHdrId(), scsSeq, capexScsSeq);
				BigDecimal approvedPoTotal = new BigDecimal(iPoDAO.getApprovedPoTotalByProjectId(cstDtl.getHdrId()));
				BigDecimal committedScsTotal = new BigDecimal(
						iIndentGroupDAO.getCommittedScsTotalByProjectId(cstDtl.getHdrId(), minSeqNo));
				BigDecimal consumedSoFar = approvedPoTotal.add(committedScsTotal);
				BigDecimal budgetExcessApproved = new BigDecimal(
						iBudgetExcessSheetDAO.getApprovedExcessTotalByPmHdrId(cstDtl.getHdrId(), minSeqNo));
				resp.setAllocatedValue(allocatedValue);
				resp.setConsumedSoFar(consumedSoFar.toString());
				resp.setBudgetExcessApproved(budgetExcessApproved.toString());
			}

			respList.add(resp);
			if (respList.size() > 0) {
				returnList.setResponseData(respList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(respList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getCostAnlysDtls error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage updateEmpInIndentAssignTeam(UpdateIndentAssignTeamReq indentAssignTeamReq) {
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		try {
			String[] indentDtlIdArr = indentAssignTeamReq.getSelectedIndents();
			if (indentDtlIdArr.length > 0) {
				for (int i = 0; i < indentDtlIdArr.length; i++) {
					int checkCount = iIndentUploadDAO.CheckIsPrimaryZero(indentDtlIdArr[i]);
					if (checkCount > 0) {
						String iaId = iIndentUploadDAO.getIaId(indentDtlIdArr[i]);
						iIndentUploadDAO.updateIndentAssignTeam(indentAssignTeamReq.getEmployeeId(), iaId);
					} else {
						int checkEmpCount = iIndentUploadDAO.CheckEmpInIndentAssignTeam(indentDtlIdArr[i],
								indentAssignTeamReq.getEmployeeId());
						if (checkEmpCount == 0) {
							iIndentUploadDAO.insertIndentAssignTeam(indentDtlIdArr[i],
									indentAssignTeamReq.getEmployeeId(), "0", indentAssignTeamReq.getTenantId());
						}
					}
				}
				returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMsg.setResponseDataMessage(ResponseMessageMap.success);
				returnMsg.setResponseMessage(ResponseMessageMap.successUpdated);
			}
		} catch (Exception e) {
			logger.error("updateEmpInIndentAssignTeam error " + e);
		}
		return returnMsg;
	}

	@Override
	public ResponseAsList getBudgetDtlByIndent(IndentRequestEntity indentReq) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<GetBudgetDtlByIndentEntity> proj = iIndentUploadDAO.getBudgetDtlByIndent(indentReq);

			if (proj.size() > 0) {
				returnList.setResponseData(proj);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(proj);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getIndentByIndentID error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage updateIndentBudgetDtl(List<SalesIndentBudgetDtlEntity> salesIndentBudgetDtlreq) {
		ResponseAsMessage returnList = new ResponseAsMessage();
		try {
			int proj = iIndentUploadDAO.updateIndentBudgetDtl(salesIndentBudgetDtlreq);

			if (proj > 0) {
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseDataMessage(ResponseMessageMap.success);
				returnList.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseDataMessage(ResponseMessageMap.success);
				returnList.setResponseMessage(ResponseMessageMap.successUpdated);
			}

		} catch (Exception ex) {
			logger.error("getIndentByIndentID error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getindentLifecycDtl(getindentLifecycDtlResponse getindentLifecycDtlReq) {
		List<getindentLifecycDtlResp> respList = new ArrayList<getindentLifecycDtlResp>();
		ResponseAsList returnList = new ResponseAsList();
		try {
			getindentLifecycDtlResp list = new getindentLifecycDtlResp();
			List<DocumentStatusMstEntity> docLifeCyc = stageManagementDAO.getAlldocLifeCyc("DC018", getindentLifecycDtlReq.getIndentType(),getindentLifecycDtlReq.getTenantId());;;
			
		//	List<IndentHdrDtlsEntity> indentlist =  iIndentUploadDAO.getIndentHdrDtlsByProjectIdAndType(getindentLifecycDtlReq.getProjectId(),
		//			getindentLifecycDtlReq.getTenantId(),getindentLifecycDtlReq.getIndentType());
			String indentQry="",mstPocCheck="0";
			if(getindentLifecycDtlReq.getIndentId()!=null && !getindentLifecycDtlReq.getIndentId().isEmpty() && !getindentLifecycDtlReq.getIndentId().equalsIgnoreCase("")) {
				indentQry="AND hdr.INDENT_ID='"+getindentLifecycDtlReq.getIndentId()+"'";
				
				ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
				projectInitiation.setEmpId(getindentLifecycDtlReq.getEmpId());
				projectInitiation.setPmId(getindentLifecycDtlReq.getProjectId());
				mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,getindentLifecycDtlReq.getTenantId());
			}else {
				mstPocCheck ="1";
			}
			
			List<GetIndentLifecycDtlEntity> indentlist = iIndentUploadDAO.getIndentSummaryDtlByProjectIdAndType(getindentLifecycDtlReq.getProjectId(),
					getindentLifecycDtlReq.getTenantId(),getindentLifecycDtlReq.getIndentType(),indentQry,mstPocCheck,getindentLifecycDtlReq.getEmpId(),
					getindentLifecycDtlReq.getFromDate(),getindentLifecycDtlReq.getToDate());

			Set<String> uniqueIndentIds = indentlist.stream()
			        .map(indent -> indent.getIndentId()) 
			        .collect(Collectors.toSet());
			uniqueIndentIds.forEach(indentId -> {
			    List<IndentRemarksEntity> indentRemark = iIndentUploadDAO.indentRemarks(indentId, getindentLifecycDtlReq.getTenantId());
			    indentlist.stream()
	              .filter(indent -> indent.getIndentId().equals(indentId)) 
	              .forEach(indent -> indent.setRemarksval(indentRemark));
			});
			
			List<GetIndentLifecycDtlEntity> inventoryIndentList = indentlist.stream()
			        .filter(indent -> indent.getIsInventroy() != null && "1".equals(indent.getIsInventroy()))
			        .collect(Collectors.toList());
			inventoryIndentList.forEach(indent ->{
				String type = iIndentUploadDAO.getTypeIndentGrpScs(indent.getIgHdrId(),getindentLifecycDtlReq.getTenantId());
				indent.setType(type);
			});

			list.setIndentList(indentlist);
			list.setSeqList(docLifeCyc);
			respList.add(list);
			if (respList.size() > 0) {
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseData(respList);
				returnList.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseData(respList);
				returnList.setResponseMessage(ResponseMessageMap.successUpdated);
			}

		} catch (Exception ex) {
			logger.error("getindentLifecycDtl error " + ex);
		}
		return returnList;
	}
	public List<GetindentDtlcycEntity> getindentDtlcycByindentId(IndentRequestEntity indentReq) {
		List<GetindentDtlcycEntity> dtl = new ArrayList<GetindentDtlcycEntity>();
		try {
			dtl = iIndentUploadDAO.getindentDtlcyclist(indentReq.getIndentId(), indentReq.getTenantId());
			
			for(int i =0; i<dtl.size();i++) {
				BigDecimal indentqtyVal =new BigDecimal(dtl.get(i).getIndentDtlQty()) ;
				BigDecimal grnQty =new BigDecimal(iIndentUploadDAO.indentGrnDtlVal(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())) ;
				BigDecimal poQty =new BigDecimal(iIndentUploadDAO.indentPoDtlval(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())) ;
				BigDecimal miQty =new BigDecimal(iIndentUploadDAO.indentMiDtlval(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())) ;
				BigDecimal inspecQty =new BigDecimal(iIndentUploadDAO.indentInspectionDtlVal(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())) ;
			if( grnQty.compareTo(indentqtyVal) >= 0) {
				dtl.get(i).setGrnQty(iIndentUploadDAO.indentGrnDtlDateTime(dtl.get(i).getIndentDtlId(), indentReq.getTenantId()));
			}
			if(poQty.compareTo(indentqtyVal) >= 0 ) {
				dtl.get(i).setPoQty(iIndentUploadDAO.indentPoDtlDateTime(dtl.get(i).getIndentDtlId(), indentReq.getTenantId()));
			}
			if(miQty.compareTo(indentqtyVal) >= 0  ) {
				dtl.get(i).setMiQty(iIndentUploadDAO.indentMiDtlDateTime(dtl.get(i).getIndentDtlId(), indentReq.getTenantId()));
			}
			if(inspecQty.compareTo(indentqtyVal) >= 0 ) {
				dtl.get(i).setInspectionQty(iIndentUploadDAO.indentInspectionDtlDateTime(dtl.get(i).getIndentDtlId(), indentReq.getTenantId()));
			}
			}
		} catch (Exception ex) {
			logger.error("getindentDtlcycByindentId error " + ex);
		}
		return dtl;
	}
	@Override
	public ResponseAsList getindentDtlcyc(IndentRequestEntity indentReq) {
		List<GetindentDtlcycEntity> dtl = new ArrayList<GetindentDtlcycEntity>();
		ResponseAsList returnList = new ResponseAsList();
		try {
			dtl = iIndentUploadDAO.getindentDtlcyclist(indentReq.getIndentId(), indentReq.getTenantId());
			
			for(int i =0; i<dtl.size();i++) {
				BigDecimal indentqtyVal =new BigDecimal(dtl.get(i).getIndentDtlQty()) ;
				BigDecimal grnQty =new BigDecimal(iIndentUploadDAO.indentGrnDtlVal(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())) ;
				BigDecimal poQty =new BigDecimal(iIndentUploadDAO.indentPoDtlval(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())) ;
				BigDecimal miQty =new BigDecimal(iIndentUploadDAO.indentMiDtlval(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())) ;
				BigDecimal inspecQty =new BigDecimal(iIndentUploadDAO.indentInspectionDtlVal(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())) ;
			if( grnQty.compareTo(indentqtyVal) >= 0) {
				dtl.get(i).setGrnQty(iIndentUploadDAO.indentGrnDtlDateTime(dtl.get(i).getIndentDtlId(), indentReq.getTenantId()));
			}
			if(poQty.compareTo(indentqtyVal) >= 0 ) {
				dtl.get(i).setPoQty(iIndentUploadDAO.indentPoDtlDateTime(dtl.get(i).getIndentDtlId(), indentReq.getTenantId()));
			}
			if(miQty.compareTo(indentqtyVal) >= 0  ) {
				dtl.get(i).setMiQty(iIndentUploadDAO.indentMiDtlDateTime(dtl.get(i).getIndentDtlId(), indentReq.getTenantId()));
			}
			if(inspecQty.compareTo(indentqtyVal) >= 0 ) {
				dtl.get(i).setInspectionQty(iIndentUploadDAO.indentInspectionDtlDateTime(dtl.get(i).getIndentDtlId(), indentReq.getTenantId()));
			}
	//		dtl.get(i).setGrnQty(Integer.toString(iIndentUploadDAO.indentGrnDtlVal(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())));
	//			dtl.get(i).setPoQty(Integer.toString(iIndentUploadDAO.indentPoDtlval(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())));
	//			dtl.get(i).setMiQty(Integer.toString(iIndentUploadDAO.indentMiDtlval(dtl.get(i).getIndentDtlId(), indentReq.getTenantId())));
			}
			returnList.setResponseData(dtl);
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.successUpdated);
		} catch (Exception ex) {
			logger.error("getindentDtlcyc error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getIndentDtlProductCost(GetIndentDtlProductCostRequest getIndentDtlProductCostReq) {
		List<GetIndentDtlProductCostEntity> dtl = new ArrayList<GetIndentDtlProductCostEntity>();
		ResponseAsList returnList = new ResponseAsList();
		try {
			dtl = iIndentUploadDAO.getIndentDtlProductCost(getIndentDtlProductCostReq.getProductCode(), getIndentDtlProductCostReq.getTenantId(),
					getIndentDtlProductCostReq.getPmHdrId(),getIndentDtlProductCostReq.getProductId());
			returnList.setResponseData(dtl);
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.successUpdated);
		} catch (Exception ex) {
			logger.error("getIndentDtlProductCost error " + ex);
		}
		return returnList;

	}

	@Override
	public ResponseAsList getMaterialTransferDtls(PmHdrIdAndTenantIdRequest matDtls) {
		// TODO Auto-generated method stub
		List<InventoryMaterialTransferEntity> list = new ArrayList<InventoryMaterialTransferEntity>();
		ResponseAsList returnList = new ResponseAsList();
		try {
			list=iIndentUploadDAO.getMaterialTransferDtls(matDtls);
			if (list.size() > 0) {
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseData(list);
				returnList.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseData(list);
				returnList.setResponseMessage(ResponseMessageMap.NoData);
			}
		}catch(Exception ex) {
			logger.error("getMaterialTransferDtls error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage deleteTaskDoc(IndentDtlDeleteRequest indentDtlDeleteReq) {
		ResponseAsMessage returnList = new ResponseAsMessage();
		try {
		String filePath = iIndentUploadDAO.filePathByDmId(indentDtlDeleteReq.getDmId());	
		String fileName = iIndentUploadDAO.fileNameByDmId(indentDtlDeleteReq.getDmId());	
		 File file
         = new File(filePath +"//"+fileName);

     if (file.delete()) {
    	 
     
		iIndentUploadDAO.deleteDocumentManagement(indentDtlDeleteReq.getDmId());
		iIndentUploadDAO.deleteFileManager(indentDtlDeleteReq.getDmId());
		
		returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
		returnList.setResponseDataMessage(ResponseMessageMap.successfulDeleted);
		returnList.setResponseMessage(ResponseMessageMap.successfulDeleted);
	} else {
		returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
		returnList.setResponseDataMessage(ResponseMessageMap.deleteUnSuccessful);
		returnList.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
	}

		}catch(Exception ex) {
			logger.error("deleteTaskDoc error " + ex);
		}
		return returnList;
	}

}