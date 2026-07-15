package com.vmfg.scm.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.ChangeRequestDAO;
import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.entity.IndentHdrDtlsEntity;
import com.vmfg.design.request.IndentDtlRequest;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.scm.dao.interfaces.IIndentManagementDAO;
import com.vmfg.scm.entity.IndentGroupHdrAndDtlEntity;
import com.vmfg.scm.entity.IndentHdrDropDownEntity;
import com.vmfg.scm.entity.ProjectDtlsEntity;
import com.vmfg.scm.entity.ScmHdrBasedDtlEntity;
import com.vmfg.scm.request.IndentGrpRetRequest;
import com.vmfg.scm.request.ProjectAssignEmpReq;
import com.vmfg.scm.request.ProjectDtlRequest;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;
import com.vmfg.scm.request.getIndentHdrRequest;
import com.vmfg.scm.services.interfaces.IIndentManagementService;
import com.vmfg.task.dao.interfaces.IDesignTaskDAO;
@Service
public class IndentManagementService implements IIndentManagementService {

	private static final Logger logger = LoggerFactory.getLogger(IndentManagementService.class);
	@Autowired
	private IIndentManagementDAO iIndentManagementDAO;
	@Autowired
	private IndentUploadDAO indentUploadDAO;
	@Autowired
	private IDesignTaskDAO idesignTaskDAO;
	@Autowired
	private ChangeRequestDAO changeRequestDAO;

	@Override
	public ResponseAsList getIndentProjectDtlsByDate(ProjectDtlRequest projectDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();

		try {
			list = iIndentManagementDAO.getIndentProjectDtlsByDate(projectDtlReq.getFromDate(),projectDtlReq.getToDate(),projectDtlReq.getTenantId());
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
			logger.error("getIndentProjectDtlsByDate service error " + ex);
		}
		return returnList;
	}
	@Override
	public ResponseAsList indentHdrDropDownByProjectCode(getIndentHdrRequest getIndentHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<IndentHdrDropDownEntity> list = new ArrayList<IndentHdrDropDownEntity>();

		try {
			String indent = getIndentHdrReq.getGetIndent();
			if (indent == null || indent.equalsIgnoreCase("null") || indent.trim().isEmpty()) {
				list = iIndentManagementDAO.indentHdrDropDownByProjectCode(
						getIndentHdrReq.getEmpId(),
						getIndentHdrReq.getPmId(),
						getIndentHdrReq.getProjectId(),
						getIndentHdrReq.getTenantId()
				);
			} else if (getIndentHdrReq.getGetIndent().equalsIgnoreCase("1")) {
				list = iIndentManagementDAO.getIndentsExceptPmVerified(getIndentHdrReq.getEmpId(),getIndentHdrReq.getPmId(), getIndentHdrReq.getProjectId(), getIndentHdrReq.getTenantId());
			} else if (getIndentHdrReq.getGetIndent().equalsIgnoreCase("2")) {
				list = iIndentManagementDAO.getOnlyScmVerifiedAndClosedIndents(getIndentHdrReq.getEmpId(),getIndentHdrReq.getPmId(), getIndentHdrReq.getProjectId(), getIndentHdrReq.getTenantId());
			} else if (getIndentHdrReq.getGetIndent().equalsIgnoreCase("3")) {
				list = iIndentManagementDAO.getIndentsBasedOnEmployee(getIndentHdrReq.getEmpId(),getIndentHdrReq.getPmId(), getIndentHdrReq.getProjectId(), getIndentHdrReq.getTenantId());
			}else if (getIndentHdrReq.getGetIndent().equalsIgnoreCase("4")) {
	         	//no needed this method --> not used because getIndent 4 and 5 same
				list = iIndentManagementDAO.getCapexIndentsBasedOnEmployee(getIndentHdrReq.getEmpId(), getIndentHdrReq.getPmId(), getIndentHdrReq.getProjectId(), getIndentHdrReq.getTenantId());
			}else if (getIndentHdrReq.getGetIndent().equalsIgnoreCase("5")){
				list = iIndentManagementDAO.getIsInternalOneIndents(getIndentHdrReq.getEmpId(),getIndentHdrReq.getPmId(), getIndentHdrReq.getProjectId(), getIndentHdrReq.getTenantId());
			}else if (getIndentHdrReq.getGetIndent().equalsIgnoreCase("6")){
				list = iIndentManagementDAO.getOnlyScmVerifiedIndents(getIndentHdrReq.getEmpId(),getIndentHdrReq.getPmId(), getIndentHdrReq.getProjectId(), getIndentHdrReq.getTenantId());
			}else {
				list = iIndentManagementDAO.getOnlyScmAcceptedIndents(getIndentHdrReq.getEmpId(),getIndentHdrReq.getPmId(), getIndentHdrReq.getProjectId(), getIndentHdrReq.getTenantId());
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
			logger.error("indentHdrDropDownByProjectCode service error " + ex);
		}
		return returnList;
	}
	@Override
	public ResponseAsList getIndentHdrDtlsByIndentId(IndentDtlRequest indentDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<IndentHdrDtlsEntity> list = new ArrayList<IndentHdrDtlsEntity>();

		try {
			if(indentDtlReq.getIndentId().equalsIgnoreCase("getAll")) {
					list = iIndentManagementDAO.getAllIndentHdrDtls(indentDtlReq.getEmpId(),indentDtlReq.getPmId(),indentDtlReq.getProjectId(),indentDtlReq.getTenantId(),indentDtlReq.getByProjectId());
			}else {
				list = iIndentManagementDAO.getIndentHdrDtlsByIndentId(indentDtlReq.getIndentId(),indentDtlReq.getTenantId());
			}
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					int getNoOfProducts = indentUploadDAO.getNoOfProductsByIndentID(list.get(i).getIndentId(),
							indentDtlReq.getTenantId());
					list.get(i).setNoOfProductsCount(getNoOfProducts);
					list.get(i).setIndentClosed(iIndentManagementDAO.getIndentClosedStatus(list.get(i).getIndentId()));

					//Get Assigned Status for Indent Management
					boolean assignedStatus = indentUploadDAO.getAssignedStatus(list.get(i).getIndentId(), indentDtlReq.getTenantId());
					list.get(i).setAssigned(assignedStatus);

					String indentTypeCode = indentUploadDAO.getIndentTypeCodeByIndentId(list.get(i).getIndentId(),
							indentDtlReq.getTenantId());
					String docGroup = indentTypeCode;
                 
					int currentSeq = indentUploadDAO.getCurrentSeqByIndentId(list.get(i).getIndentId(),
							indentDtlReq.getTenantId());
					List<DocumentStatusMstEntity> docLifeCycleMstList;
						docLifeCycleMstList = idesignTaskDAO.getNextSeqandStatusByDoc(currentSeq, indentDtlReq.getDocType(), indentDtlReq.getTenantId(),docGroup);
					if(docLifeCycleMstList.size()>0) {
						String docStatDesc =changeRequestDAO.getStatusDescByStatusCode(docLifeCycleMstList.get(0).getDocStatus(), indentDtlReq.getTenantId());
						list.get(i).setNextstatusDesc(docStatDesc);
					}
				}
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getIndentHdrDtlsByIndentId service error " + ex);
		}
		return returnList;
	}
	@Override
	public ResponseAsList getIndentGrpNewProd(IndentGrpRetRequest indentGrpReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<IndentGroupHdrAndDtlEntity> list = new ArrayList<IndentGroupHdrAndDtlEntity>();

		try {
			list= iIndentManagementDAO.getIndentGrpNewProd(indentGrpReq);
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
			logger.error("getIndentHdrDtlsByIndentId service error " + ex);
		}
		return returnList;
	}
	@Override
	public ResponseAsList getScmHdrBasedDtl(ScmHdrBasedDtlRequest scmHdrBasedDtl) {
		ResponseAsList returnList = new ResponseAsList();
		List<ScmHdrBasedDtlEntity> list = new ArrayList<ScmHdrBasedDtlEntity>();

		try {
			list= iIndentManagementDAO.getScmHdrBasedDtl(scmHdrBasedDtl);
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
			logger.error("getScmHdrBasedDtl service error " + ex);
		}
		return returnList;
	}
	@Override
	public ResponseAsList getIndentProjectDtlsByDateAndIndent(ProjectDtlRequest projectDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();

		try {
			list = iIndentManagementDAO.getIndentProjectDtlsByDateAndIndent(projectDtlReq.getFromDate(),projectDtlReq.getToDate(),projectDtlReq.getTenantId());
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
			logger.error("getIndentProjectDtlsByDateAndIndent service error " + ex);
		}
		return returnList;
	}
	@Override
	public ResponseAsList getIndentProjectDtlsByEmployee(ProjectAssignEmpReq projectDtlReq) {
		// TODO Auto-generated method stub

		ResponseAsList returnList = new ResponseAsList();
		List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();

		try {
			list = iIndentManagementDAO.getIndentProjectDtlsByEmployee(projectDtlReq.getFromDate(),projectDtlReq.getToDate(),projectDtlReq.getTenantId(),projectDtlReq.getEmpId());
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
			logger.error("getIndentProjectDtlsByDate service error " + ex);
		}
		return returnList;
	
	}
	
}
