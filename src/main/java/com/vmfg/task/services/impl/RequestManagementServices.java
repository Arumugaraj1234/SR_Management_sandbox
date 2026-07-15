package com.vmfg.task.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.StageManagementDAO;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.project.request.ProjectInitiationMstRequest;
import com.vmfg.quality.dao.impl.QualityInspectionDAO;
import com.vmfg.task.dao.interfaces.IRequestManagementDAO;
import com.vmfg.task.entity.GetAllCountByEmpIdEntity;
import com.vmfg.task.entity.GetRemarksDtlEntity;
import com.vmfg.task.entity.GetReqManHdrDtlEntity;
import com.vmfg.task.entity.GetRequestCategoryEntity;
import com.vmfg.task.entity.GetStatusRemarksDtlEntity;
import com.vmfg.task.request.GetAllCountByEmpId;
import com.vmfg.task.request.GetReqManHdrDtlRequest;
import com.vmfg.task.request.GetRequestCategoryRequest;
import com.vmfg.task.request.GetStatusRemarksDtlRequest;
import com.vmfg.task.request.InsertRMRemarksRequest;
import com.vmfg.task.request.ReqManagementHdr;
import com.vmfg.task.request.UpdateReqStatusRequest;
import com.vmfg.task.services.interfaces.IRequestManagementServices;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class RequestManagementServices implements IRequestManagementServices {
	private static final Logger logger = LoggerFactory.getLogger(RequestManagementServices.class);
	@Autowired
	private IRequestManagementDAO iRequestManagementDAO;
	@Autowired
	IndentUploadDAO indentUploadDAO;
	@Autowired
	StageManagementDAO stageManagementDAO;
	
	@Autowired
	QualityInspectionDAO qualityInspectionDAO;

	@Autowired
	CommonNotifyMethod commonNotifyMethod;
	
	@Autowired
	ProjectDAO projectDAO;

	@Override
	public ResponseAsMessage insertReqManagementHdr(ReqManagementHdr reqManHdr) {
		logger.info("insertReqManagementHdr service start");
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		int insertHdr = 0, reqRemarks = 0, reqManStatusDtl = 0;
		String seqNo = "1";
		try {
			// Common docStatus
			String seqStatus = indentUploadDAO.getStatusCodebySeqAndDocType("1", reqManHdr.getTenantId(), "DC069");
			insertHdr = iRequestManagementDAO.insertReqManagementHdr(reqManHdr.getPmHdrId(), reqManHdr.getRemarks(),
					reqManHdr.getReqCategory(), reqManHdr.getRequestedDate(), reqManHdr.getReqDesc(),
					reqManHdr.getReqName(), reqManHdr.getRequestedBy(), reqManHdr.getRequestedByDept(),
					reqManHdr.getRequestedTo(), reqManHdr.getRequestedToDept(), reqManHdr.getTicketReporter(),
					reqManHdr.getTenantId(), seqNo, seqStatus, reqManHdr.getDueDate());

			reqRemarks = iRequestManagementDAO.insertReqRemarks(insertHdr, reqManHdr.getRemarksBy(),
					reqManHdr.getRemarks(), reqManHdr.getTenantId());

			reqManStatusDtl = iRequestManagementDAO.insertReqManStatusDtl(insertHdr, seqNo, seqStatus,
					reqManHdr.getEmpId(), reqManHdr.getRemarks(), reqManHdr.getTenantId());

			if (insertHdr > 0 && reqRemarks > 0 && reqManStatusDtl > 0) {
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				String projCode = indentUploadDAO.getProjectCodeByProjId(reqManHdr.getPmHdrId(),
						reqManHdr.getTenantId());
				otherEmp.add(reqManHdr.getRequestedTo());
				messageList.add(projCode);
				messageList.add(indentUploadDAO.getEmpNameByEmpId(reqManHdr.getRequestedTo()));
				commonNotifyMethod.InvokeNotificationMethod(1, 13, null, reqManHdr.getTenantId(), messageList, otherEmp,
						"0", null, reqManHdr.getPmHdrId(), null);

				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertReqManagementHdr service end");
		} catch (Exception ex) {
			logger.error("insertReqManagementHdr Method Exception --->" + ex);

		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getStatusRemarksDtl(GetStatusRemarksDtlRequest getStatus) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<GetStatusRemarksDtlEntity> statusLists = new ArrayList<GetStatusRemarksDtlEntity>();

			statusLists = iRequestManagementDAO.getStatusRemarksDtl(getStatus.getRqId(), getStatus.getTenantId());

			if (statusLists.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(statusLists);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(statusLists);
			}

		} catch (Exception ex) {
			logger.error("getStatusRemarksDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage insertRMRemarks(InsertRMRemarksRequest insertRMRemarks) {
		logger.info("insertRMRemarks service start");
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		int reqRemarks = 0;
		int rqId = Integer.parseInt(insertRMRemarks.getRqId());
		try {
			reqRemarks = iRequestManagementDAO.insertReqRemarks(rqId, insertRMRemarks.getEmpId(),
					insertRMRemarks.getRemarks(), insertRMRemarks.getTenantId());
			if (reqRemarks > 0) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertRMRemarks service end");
		} catch (Exception ex) {
			logger.error("insertRMRemarks Method Exception --->" + ex);

		}
		return returnMessage;
	}

	@Override
	public ResponseAsMessage insertReqStatus(UpdateReqStatusRequest reqStatus) {
		logger.info("insertReqStatus service start");
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		int insertStatusDtl = 0;
		try {

			List<GetReqManHdrDtlEntity> reqDtl = iRequestManagementDAO
					.getReqManHdrAndStatusAndRemarks(reqStatus.getRqId(), reqStatus.getTenantId());
			// update Closed Date
			if (reqStatus.getIsComplete() == 1 || reqStatus.getIsApproved() == 1) {
				iRequestManagementDAO.updateClosedDateInHdr(reqStatus.getRqId(), CommonMethod.getCurrentDate(),
						reqStatus.getIsComplete(), reqStatus.getIsApproved());
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				String projCode = indentUploadDAO.getProjectCodeByProjId(reqDtl.get(0).getPmHdrId(),
						reqDtl.get(0).getTenantId());
				otherEmp.add(reqDtl.get(0).getRequestedById());
				otherEmp.add(reqDtl.get(0).getRequestedToId());
				messageList.add(projCode);
				commonNotifyMethod.InvokeNotificationMethod(2, 14, null, reqStatus.getTenantId(), messageList, otherEmp,
						"0", null, reqDtl.get(0).getPmHdrId(), null);
			}

//			if (currSeqDocLifeCycleMstList.size() > 0) {
//				if (currSeqDocLifeCycleMstList.get(0).getLastSeq().equalsIgnoreCase("1")) {
//					
//				}
//			}
			insertStatusDtl = iRequestManagementDAO.insertReqStatus(reqStatus.getEmpId(), reqStatus.getRqId(),
					reqStatus.getSeqNo(), reqStatus.getSeqStatus(), reqStatus.getStatusRemarks(),
					reqStatus.getTenantId());

			if (insertStatusDtl > 0) {
				returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMessage.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertReqStatus service end");
		} catch (Exception ex) {
			logger.error("insertReqStatus Method Exception --->" + ex);

		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getRequestCategory(GetRequestCategoryRequest requestCategory) {
		ResponseAsList list = new ResponseAsList();
		try {
			List<GetRequestCategoryEntity> categoryLists = new ArrayList<>();

			categoryLists = iRequestManagementDAO.getRequestCategory(requestCategory.getTenantId());

			if (categoryLists.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(categoryLists);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(categoryLists);
			}

		} catch (Exception ex) {
			logger.error("getRequestCategory Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getReqManHdrDtl(GetReqManHdrDtlRequest getReqManHdr) {
		ResponseAsList list = new ResponseAsList();
		List<GetReqManHdrDtlEntity> reqHdrLists = new ArrayList<>();

		try {

			reqHdrLists = iRequestManagementDAO.getReqManHdrDtl(getReqManHdr.getPmHdrId(), getReqManHdr.getTenantId());

			if (reqHdrLists.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(reqHdrLists);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(reqHdrLists);
			}

		} catch (Exception ex) {
			logger.error("getReqManHdrDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getReqManHdrAndStatusAndRemarks(GetStatusRemarksDtlRequest getStatusRemarksDtlRequest) {
		ResponseAsList list = new ResponseAsList();
		List<GetReqManHdrDtlEntity> reqHdrLists = new ArrayList<>();
		List<GetReqManHdrDtlEntity> allLists = new ArrayList<>();
		List<GetStatusRemarksDtlEntity> statusList = new ArrayList<>();
		List<GetRemarksDtlEntity> remarksList = new ArrayList<>();
		try {

			reqHdrLists = iRequestManagementDAO.getReqManHdrAndStatusAndRemarks(getStatusRemarksDtlRequest.getRqId(),
					getStatusRemarksDtlRequest.getTenantId());
			for (GetReqManHdrDtlEntity obj : reqHdrLists) {
				GetReqManHdrDtlEntity newObg = new GetReqManHdrDtlEntity();
				BeanUtils.copyProperties(obj, newObg);
				statusList = iRequestManagementDAO.getStatusRemarksDtl(obj.getRqId(), obj.getTenantId());
				remarksList = iRequestManagementDAO.getRemarksDtl(obj.getRqId(), obj.getTenantId());
				newObg.setStatusList(statusList);
				newObg.setRemarksList(remarksList);
				allLists.add(newObg);
			}

			if (allLists.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(allLists);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(allLists);
			}

		} catch (Exception ex) {
			logger.error("getReqManHdrAndStatusAndRemarks Error  " + ex);
		}
		return list;
	}

	@Override
	public GetAllCountByEmpIdEntity getAllCounts(GetAllCountByEmpId getAllCountByEmpId) {
		GetAllCountByEmpIdEntity counts = new GetAllCountByEmpIdEntity();

		try {

			counts = iRequestManagementDAO.getAllCounts(getAllCountByEmpId.getEmpId(),
					getAllCountByEmpId.getTenantId());

		} catch (Exception ex) {
			logger.error("getAllCounts Error  " + ex);
		}
		return counts;
	}

	@Override
	public ResponseAsList getRequestedToDtl(GetAllCountByEmpId getAllByEmpId) {
		ResponseAsList list = new ResponseAsList();
		List<GetReqManHdrDtlEntity> reqHdrLists = new ArrayList<>();
		List<GetReqManHdrDtlEntity> fromList = new ArrayList<>();
		try {
			if(getAllByEmpId.getIsDashboard() !=null && getAllByEmpId.getIsDashboard().equalsIgnoreCase("1")) {
				
				ProjectInitiationMstRequest projectInitiation = new ProjectInitiationMstRequest();
				projectInitiation.setEmpId(getAllByEmpId.getEmpId());
				projectInitiation.setPmId(getAllByEmpId.getPmId());
				String mstPocCheck=projectDAO.getProjectInitiationMstResp(projectInitiation,getAllByEmpId.getTenantId());
				String assignedEmpId ="";
				if(mstPocCheck.equalsIgnoreCase("1")) {
					assignedEmpId = "%%";
				}else {
					assignedEmpId = getAllByEmpId.getEmpId(); 
				}
				String dept= qualityInspectionDAO.getEmpDepCode(getAllByEmpId.getEmpId(),getAllByEmpId.getTenantId());
				
				reqHdrLists = iRequestManagementDAO.getRequestAssignedTo(assignedEmpId,
						getAllByEmpId.getTenantId(),dept,getAllByEmpId.getFromDate(),getAllByEmpId.getToDate());
				
				fromList = iRequestManagementDAO.getRequestAssignedFrom(assignedEmpId,
						getAllByEmpId.getTenantId(),dept,getAllByEmpId.getFromDate(),getAllByEmpId.getToDate());
				
				reqHdrLists.addAll(fromList);
				
				reqHdrLists = reqHdrLists.stream()
			            .sorted(Comparator.comparing(GetReqManHdrDtlEntity::getRequestedDate))  // Sort by requested date
			            .collect(Collectors.toList()); 
			}else {
				reqHdrLists = iRequestManagementDAO.getRequestedToDtl(getAllByEmpId.getEmpId(),
						getAllByEmpId.getTenantId());
			}
				
			if (reqHdrLists.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(reqHdrLists);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(reqHdrLists);
			}

		} catch (Exception ex) {
			logger.error("getRequestedToDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getRequestedByDtl(GetAllCountByEmpId getAllByEmpId) {
		ResponseAsList list = new ResponseAsList();
		List<GetReqManHdrDtlEntity> reqHdrLists = new ArrayList<>();

		try {

			reqHdrLists = iRequestManagementDAO.getRequestedByDtl(getAllByEmpId.getEmpId(),
					getAllByEmpId.getTenantId());

			if (reqHdrLists.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(reqHdrLists);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(reqHdrLists);
			}

		} catch (Exception ex) {
			logger.error("getRequestedByDtl Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getRequestedByDtlWithIsComplete(GetAllCountByEmpId getAllByEmpId) {
		ResponseAsList list = new ResponseAsList();
		List<GetReqManHdrDtlEntity> reqHdrLists = new ArrayList<>();

		try {

			reqHdrLists = iRequestManagementDAO.getRequestedByDtlWithIsComplete(getAllByEmpId.getEmpId(),
					getAllByEmpId.getTenantId());

			if (reqHdrLists.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(reqHdrLists);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(reqHdrLists);
			}

		} catch (Exception ex) {
			logger.error("getRequestedByDtlWithIsComplete Error  " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsList getRequestToWithAllDepartment(GetAllCountByEmpId getAllByEmpId) {
		ResponseAsList list = new ResponseAsList();

		List<GetReqManHdrDtlEntity> reqHdrLists = new ArrayList<>();

		try {

			String deptCode = iRequestManagementDAO.getdepartCode(getAllByEmpId.getEmpId(),
					getAllByEmpId.getTenantId());
			if (deptCode != null && !deptCode.isEmpty()) {

				reqHdrLists = iRequestManagementDAO.getRequestToWithAllDepartment(deptCode,
						getAllByEmpId.getTenantId());

			}

			if (reqHdrLists.size() > 0) {

				list.setResponseCode(ResponseMessageMap.responseCodeOk);
				list.setResponseMessage(ResponseMessageMap.success);
				list.setResponseData(reqHdrLists);
			} else {
				list.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				list.setResponseMessage(ResponseMessageMap.noRecord);
				list.setResponseData(reqHdrLists);
			}

		} catch (Exception ex) {
			logger.error("getRequestToWithAllDepartment Error  " + ex);
		}
		return list;
	}

}
