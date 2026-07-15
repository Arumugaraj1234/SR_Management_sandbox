package com.vmfg.sales.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.general.dao.impl.DepartmentAndEmployeeDAO;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.DocumentTypeMstEntity;
import com.vmfg.general.entity.EmployeeForDepartmentEntity;
import com.vmfg.general.entity.GetstageprocessDtlEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.entity.ProjectCreationStatusEntity;
import com.vmfg.sales.dao.interfaces.IEnquiryDAO;
import com.vmfg.sales.dao.interfaces.IUploadManagementDAO;
import com.vmfg.sales.entity.BudgetExcessUploadResponse;
import com.vmfg.sales.entity.CustomerMstEntity;
import com.vmfg.sales.entity.SalesBudgetFullEntity;
import com.vmfg.sales.entity.SalesEnqContactEntity;
import com.vmfg.sales.request.EnqEnablementRequest;
import com.vmfg.sales.request.GetsalebudgetextDtlBySbDtlIdRequest;
import com.vmfg.sales.request.SalesBudgetSheetExntDtlEntity;
import com.vmfg.sales.request.UpdateEnquiryDtlRequest;
import com.vmfg.sales.request.UpdatesalesBudgetHdrRequest;
import com.vmfg.sales.request.CustomerMstRequest;
import com.vmfg.sales.services.interfaces.IEnquiryService;
import com.vmfg.scm.request.HdrIdandTenantIdRequest;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class EnquiryService implements IEnquiryService {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryService.class);

	@Autowired
	IEnquiryDAO iEnquiryDAO;
	
	@Autowired
	IUploadManagementDAO uploadDao;

	@Autowired
	DepartmentAndEmployeeDAO departmentAndEmployeeDAO;
	
	@Autowired
	 CommonNotifyMethod commonNotifyMethod;
	
	@Override
	public ResponseAsMessage UpdateEnquiryDtl(UpdateEnquiryDtlRequest updateEnquiryDtlReq) {
		ResponseAsMessage resp = new ResponseAsMessage();
		try {
			int updateSeq = 0;
			String seId = updateEnquiryDtlReq.getSeId();
			String tenantId = updateEnquiryDtlReq.getTenantId();
			if (seId != null && seId.equalsIgnoreCase("")) {
				//check for new customer
				if((updateEnquiryDtlReq.getCustomerCode()==null)||updateEnquiryDtlReq.getCustomerCode().isEmpty()) {
					iEnquiryDAO.insertCustomerDtl(updateEnquiryDtlReq.getCustomerName(), null, null, null, null, null, updateEnquiryDtlReq.getTenantId(), null,updateEnquiryDtlReq.getPan(),updateEnquiryDtlReq.getGst());
					updateEnquiryDtlReq.setCustomerCode(iEnquiryDAO.getCusCodeByCusName(updateEnquiryDtlReq.getCustomerName(), updateEnquiryDtlReq.getTenantId()));
				}
				List<GetstageprocessDtlEntity> stageProcessDtl = iEnquiryDAO
						.getFirstStageByPmId(updateEnquiryDtlReq.getPmId(), tenantId);
				if (stageProcessDtl.size() > 0) {
					List<DocumentTypeMstEntity> docTyeMst = iEnquiryDAO.getEnqDocTypeMstDtlByStage(
							stageProcessDtl.get(0).getStgCode(), updateEnquiryDtlReq.getPmId(), tenantId);
					if (docTyeMst.size() > 0) {
						List<DocumentStatusMstEntity> DocStatusDtl = iEnquiryDAO.getfirstSeqBypmIdDocType(
								docTyeMst.get(0).getDocTypeCode(), updateEnquiryDtlReq.getPmId(), tenantId);
						if (DocStatusDtl.size() > 0) {
							String newstatusSeq = iEnquiryDAO.getEnqProcessLifeCycleCurrSeq(
									updateEnquiryDtlReq.getPmId(), stageProcessDtl.get(0).getMasterDocStatus(),
									tenantId);
							
							if (!newstatusSeq.equalsIgnoreCase("")) {
								updateSeq = iEnquiryDAO.insertEnqHdr(updateEnquiryDtlReq.getProjectName(),
										updateEnquiryDtlReq.getCustomerName(), updateEnquiryDtlReq.getIndustrialType(),
										updateEnquiryDtlReq.getScopeOfWork(),
										updateEnquiryDtlReq.getProjectDescription(),
										updateEnquiryDtlReq.getProductDetails(), updateEnquiryDtlReq.getEnquiryType(),
										updateEnquiryDtlReq.getEnquiryCustomerSts(),
										updateEnquiryDtlReq.getEnquiryDate(), updateEnquiryDtlReq.getReason(),
										updateEnquiryDtlReq.getLeadDtl(), updateEnquiryDtlReq.getTentativePoValue(),
										updateEnquiryDtlReq.getTenantId(), stageProcessDtl.get(0).getStgCode(),
										Integer.toString(stageProcessDtl.get(0).getSeq()),
										stageProcessDtl.get(0).getMasterDocStatus(), newstatusSeq,
										updateEnquiryDtlReq.getLocation(), updateEnquiryDtlReq.getExpectedPoDate(),updateEnquiryDtlReq.getIsInternal());
								seId = Integer.toString(updateSeq);
								if (updateSeq > 0) {
								
									String getDef = iEnquiryDAO.setDefaultUser(updateEnquiryDtlReq.getPmId(),tenantId);
									String empArr[] = getDef.split(",");
									for (int m = 0; m < empArr.length; m++) {
										List<EmployeeForDepartmentEntity> empList = departmentAndEmployeeDAO.getEmployeeForDesignation(updateEnquiryDtlReq.getTenantId(), empArr[m]);

										for(int p =0;p<empList.size();p++) {

											if(!updateEnquiryDtlReq.getEmplId().equalsIgnoreCase(empList.get(p).getEmployeeId())) {
										if(!updateEnquiryDtlReq.getEmplId().equalsIgnoreCase(empArr[m])) {
											iEnquiryDAO.insertProcessAssignDtl(seId, empList.get(p).getEmployeeId(), tenantId,
													updateEnquiryDtlReq.getPmId());
										}
											
									}
										}
									}
									iEnquiryDAO.insertProcessAssignDtl(seId, updateEnquiryDtlReq.getEmplId(), tenantId,
											updateEnquiryDtlReq.getPmId());
									updateStgstatusDtl(Integer.toString(updateSeq), updateEnquiryDtlReq.getPmId(),
											tenantId);
									
									String enqCode =iEnquiryDAO.getsaleEnquiryCode(seId);
									List<String> messageList = new ArrayList<>();
									List<String> otherEmp = new ArrayList<>();
									messageList.add("Enquiry - "+enqCode);
									String nextApproveDesig=commonNotifyMethod.getNxtAppDesc(docTyeMst.get(0).getDocTypeCode(), newstatusSeq,tenantId);
									
									commonNotifyMethod.InvokeNotificationMethod(1,1, "", tenantId, messageList, otherEmp, "1",updateEnquiryDtlReq.getPmId() , seId,null);
									commonNotifyMethod.InvokeApprovalDesigMethod(updateEnquiryDtlReq.getPmId() , docTyeMst.get(0).getDocTypeCode(), seId, null, tenantId, null,nextApproveDesig, seId,enqCode);
										
								}
							}
						}
					}
				}
			} else {
				updateSeq = iEnquiryDAO.updateEnqHdr(updateEnquiryDtlReq.getProjectName(),
						updateEnquiryDtlReq.getCustomerName(), updateEnquiryDtlReq.getIndustrialType(),
						updateEnquiryDtlReq.getScopeOfWork(), updateEnquiryDtlReq.getProjectDescription(),
						updateEnquiryDtlReq.getProductDetails(), updateEnquiryDtlReq.getEnquiryType(),
						updateEnquiryDtlReq.getEnquiryCustomerSts(), updateEnquiryDtlReq.getEnquiryDate(),
						updateEnquiryDtlReq.getReason(), updateEnquiryDtlReq.getLeadDtl(),
						updateEnquiryDtlReq.getTentativePoValue(), updateEnquiryDtlReq.getTenantId(),
						updateEnquiryDtlReq.getSeId(), updateEnquiryDtlReq.getLocation(), updateEnquiryDtlReq.getExpectedPoDate());
				iEnquiryDAO.insertCustomerDtl(updateEnquiryDtlReq.getCustomerName(), updateEnquiryDtlReq.getCity(), updateEnquiryDtlReq.getState(), updateEnquiryDtlReq.getCountry(), updateEnquiryDtlReq.getPincode(), updateEnquiryDtlReq.getAddress(), updateEnquiryDtlReq.getTenantId(),updateEnquiryDtlReq.getContactNo(),updateEnquiryDtlReq.getPan(),updateEnquiryDtlReq.getGst());
		
				String enqCode =iEnquiryDAO.getsaleEnquiryCode(seId);
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				messageList.add("Enquiry - "+enqCode);
				
				commonNotifyMethod.InvokeNotificationMethod(1,2, "", tenantId, messageList, otherEmp, "1",updateEnquiryDtlReq.getPmId() , seId,null);

			}
			List<SalesEnqContactEntity> saleEnqContact = updateEnquiryDtlReq.getSalesEntity();

			for (int i = 0; i < saleEnqContact.size(); i++) {
				String isPrimary = "";
				if (saleEnqContact.get(i).isPrimary()) {
					isPrimary = "1";
				} else {
					isPrimary = "0";
				}
				
				updateContactDtl(saleEnqContact.get(i).getSecId(), seId, saleEnqContact.get(i).getContactName(),
						saleEnqContact.get(i).getContactEmail(), saleEnqContact.get(i).getContactNo(), isPrimary,saleEnqContact.get(i).getDepartment());
			}

			if (updateSeq > 0) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				resp.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				resp.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("UpdateEnquiryDtl Error " + ex);
		}
		return resp;
	}

	public int updateContactDtl(String slaveId, String masterId, String contactName, String contactEmail,
			String contactNo, String primary,String department) {
		int updatestatus = 0;
		try {

			if (slaveId.equalsIgnoreCase("")) {
				updatestatus = iEnquiryDAO.insertcontactDtl(contactName, contactNo, contactEmail, slaveId, masterId,
						primary,department);
			} else {
				updatestatus = iEnquiryDAO.updatecontactDtl(contactName, contactNo, contactEmail, slaveId, primary,department);
			}
		} catch (Exception ex) {
			logger.error("updateContactDtl Error " + ex);
		}

		return updatestatus;
	}

	public int updateStgstatusDtl(String masterId, String pmId, String tenantId) {
		int insert = 0;
		try {
			List<DocumentTypeMstEntity> doctypeMst = iEnquiryDAO.getStgDocDtl(pmId, tenantId);
			for (int p = 0; p < doctypeMst.size(); p++) {
				if (doctypeMst.get(p).getRefTableName() != null
						&& !doctypeMst.get(p).getRefTableName().equalsIgnoreCase("")) {
					List<DocumentStatusMstEntity> DocStatusDtl = iEnquiryDAO
							.getfirstSeqBypmIdDocType(doctypeMst.get(p).getDocTypeCode(), pmId, tenantId);
					if (DocStatusDtl.size() > 0) {
						insert = iEnquiryDAO.insertStgDtl(masterId, DocStatusDtl.get(0).getDocStatus(),
								DocStatusDtl.get(0).getCurrSequence(), tenantId, doctypeMst.get(p).getRefTableName());
					}
				}
			}
		} catch (Exception ex) {
			logger.error("updateStgstatusDtl Error " + ex);
		}
		return insert;
	}

	public int insertProcessAssignDtl(String masterId, String empId, String tenantId, String pmId) {
		int insert = 0;
		try {
			insert = iEnquiryDAO.insertProcessAssignDtl(masterId, empId, tenantId, pmId);
		} catch (Exception ex) {
			logger.error("updateStgstatusDtl Error " + ex);
		}
		return insert;
	}

	@Override
	public ResponseAsList uploadBudgetSheetfile(JSONArray getArray, MultipartFile file) {
		ResponseAsList returnList = new ResponseAsList();
		List<BudgetExcessUploadResponse> mainList = new ArrayList<BudgetExcessUploadResponse>();
		String tenantId = "";
		try {

			for (int i = 0; i < getArray.length(); i++) {
				JSONObject objects = getArray.getJSONObject(i);
				JSONArray keys = objects.names();

				for (int j = 0; j < keys.length(); ++j) {

					String key = keys.getString(j);
					String value = objects.getString(key);
					if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					}
				}
			}
			mainList = iEnquiryDAO.uploadBudgetSheetfile(tenantId, file);
			if (mainList.size() > 0) {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("uploadBudgetSheetfile service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage getProjectCreationStatus(ProjectCreationStatusEntity ProjectCreationStatus) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {

			String resp = iEnquiryDAO.getprojectCodeDtl(ProjectCreationStatus.getEnqId(),
					ProjectCreationStatus.getTenantId());
			if (resp !=null && resp.equalsIgnoreCase("0")) {
				rm.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				rm.setResponseMessage(ResponseMessageMap.NoData);
				rm.setResponseDataMessage(resp);
			} else {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseMessage(ResponseMessageMap.success);
				rm.setResponseDataMessage(resp);
			}
		} catch (Exception ex) {
			logger.error("getProjectCreationStatus" + ex);
		}
		return rm;

	}

	@Override
	public ResponseAsList getsalesBudgetHdrDtl(ProjectCreationStatusEntity ProjectCreationStatus) {
		ResponseAsList returnList = new ResponseAsList();
		List<SalesBudgetFullEntity> list = new ArrayList<SalesBudgetFullEntity>();

		try {

			list = iEnquiryDAO.getsalesHdrDtl(ProjectCreationStatus.getEnqId(), ProjectCreationStatus.getTenantId());
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
			logger.error("getsalesBudgetHdrDtl service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage updatesalesBudgetHdr(UpdatesalesBudgetHdrRequest updatesalesBudgetHdrReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {

			int resp = iEnquiryDAO.updateSaleBudgethdr(updatesalesBudgetHdrReq.getSbHdrId(),
					updatesalesBudgetHdrReq.getPaymentTerms(), updatesalesBudgetHdrReq.getSalesPercent(),
					updatesalesBudgetHdrReq.getFinalSaleVal(), updatesalesBudgetHdrReq.getTenantId());
			if (resp > 0) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseMessage(ResponseMessageMap.successUpdated);
				rm.setResponseDataMessage("Success");
			} else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				rm.setResponseDataMessage("Failure");
			}
		} catch (Exception ex) {
			logger.error("updatesalesBudgetHdr" + ex);
		}
		return rm;

	}

	@Override
	public ResponseAsList getsalebudgetextDtlBySbDtlId(
			GetsalebudgetextDtlBySbDtlIdRequest getsalebudgetextDtlBySbDtlIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<SalesBudgetSheetExntDtlEntity> list = new ArrayList<SalesBudgetSheetExntDtlEntity>();

		try {
			list = iEnquiryDAO.getsaleExtDtlList(getsalebudgetextDtlBySbDtlIdReq.getSbDtlId(),
					getsalebudgetextDtlBySbDtlIdReq.getTenantId());
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
			logger.error("getsalebudgetextDtlBySbDtlId service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage getEnqEnablement(EnqEnablementRequest enqEnablementRequest) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			
			String deptCode = uploadDao.getDepCodeByEmpId(enqEnablementRequest.getEmpId(),enqEnablementRequest.getTenantId());
			
			int resp = iEnquiryDAO.getEnqEnablement(enqEnablementRequest,deptCode);
			if (resp > 0) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseMessage(ResponseMessageMap.success);
				rm.setResponseDataMessage(resp + "");
			} else {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseMessage(ResponseMessageMap.success);
				rm.setResponseDataMessage(resp + "");
			}
		} catch (Exception ex) {
			logger.error("getEnqEnablement" + ex);
		}
		return rm;

	}
	
	@Override
	public ResponseAsList getCustomerMst(CustomerMstRequest customerMstreq) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<CustomerMstEntity> list = new ArrayList<CustomerMstEntity>();

			list = iEnquiryDAO.getCustomerMst(customerMstreq.getTenantId());
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
			logger.error("getCustomerMst" + ex);
		}
		return returnList;

	}

	@Override
	public ResponseAsMessage deleteSaleEnqContact(HdrIdandTenantIdRequest hdrIdandTenantIdReq) {
		ResponseAsMessage resp = new ResponseAsMessage();
		try {
		int updateSeq=0;
		updateSeq=iEnquiryDAO.deleteSaleEnqContact(hdrIdandTenantIdReq.getHdrId());
			if (updateSeq ==1) {
				resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				resp.setResponseMessage(ResponseMessageMap.successfulDeleted);
			} else {
				resp.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				resp.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
			}
		} catch (Exception ex) {
			logger.error("deleteSaleEnqContact Error " + ex);
		}
		return resp;
	}

}
