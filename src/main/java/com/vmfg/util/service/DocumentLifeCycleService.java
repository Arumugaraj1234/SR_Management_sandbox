package com.vmfg.util.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.scm.entity.DocLifeCycleLogRequest;
import com.vmfg.util.dao.IDocumentLifeCycleDAO;
import com.vmfg.util.entity.DocGroupTypeEntity;
import com.vmfg.util.entity.DocLifeCycleListEntity;
import com.vmfg.util.entity.DocLifeCycleMstLogEntity;
import com.vmfg.util.entity.DocLifecycleVersionEntity;
import com.vmfg.util.entity.DocumentLifeCycleInsertRequest;
import com.vmfg.util.entity.DocumentTypeEntity;
import com.vmfg.util.entity.EmployeeDesignationEntity;

@Service
public class DocumentLifeCycleService implements IDocumentLifeCycleService{
	private static final Logger logger = LoggerFactory.getLogger(DocumentLifeCycleService.class);

	@Autowired
	private IDocumentLifeCycleDAO iDocumentLifeCycleDAO;

	@Override
	public ResponseAsList getDocTypes(DocLifeCycleLogRequest req) {
		// TODO Auto-generated method stub
		ResponseAsList Reslist = new ResponseAsList();
		List<DocumentTypeEntity> list=new ArrayList<DocumentTypeEntity>();

		try {
			list=iDocumentLifeCycleDAO.getDocTypes(req.getProcessCode(),req.getTenantId());
			if(list.size()>0) {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeOk);
				Reslist.setResponseMessage(ResponseMessageMap.success);
			}else {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				Reslist.setResponseMessage(ResponseMessageMap.NoData);
			}
		}catch(Exception ex) {
			logger.error("getDocTypes error "+ex);
		}
		return Reslist;
	}

	@Override
	public ResponseAsList getDocTypesDataList(DocLifeCycleLogRequest docReq) {
		ResponseAsList Reslist = new ResponseAsList();
		List<DocLifeCycleListEntity> list=new ArrayList<DocLifeCycleListEntity>();
		try {
			list=iDocumentLifeCycleDAO.getDocTypesDataList(docReq);
			
			if(list.size()>0) {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeOk);
				Reslist.setResponseMessage(ResponseMessageMap.success);
			}else {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				Reslist.setResponseMessage(ResponseMessageMap.NoData);
			}
			
		}catch(Exception ex) {
			logger.error("getDocTypesDataList error "+ex);
		}
		return Reslist;
	}

	@Override
	public ResponseAsList getDocStatusTypes(PmHdrIdAndTenantIdRequest tenanttreq) {
		ResponseAsList Reslist = new ResponseAsList();
		List<DocumentTypeEntity> list=new ArrayList<DocumentTypeEntity>();

		try {
			list=iDocumentLifeCycleDAO.getDocStatusTypes(tenanttreq);
			if(list.size()>0) {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeOk);
				Reslist.setResponseMessage(ResponseMessageMap.success);
			}else {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				Reslist.setResponseMessage(ResponseMessageMap.NoData);
			}
		}catch(Exception ex) {
			logger.error("getDocTypes error "+ex);
		}
		return Reslist;
	}

	@Override
	public ResponseAsMessage insertOrUpdateDoclifeCycle(DocumentLifeCycleInsertRequest docList) {
		ResponseAsMessage Reslist = new ResponseAsMessage();
		List<DocLifeCycleListEntity> insertArr = docList.getInsertArr();
		List<DocLifeCycleListEntity> DeleteArr = docList.getDeletetArr();

		try {
			int insertCount = 0;
//			for (int i = 0; i < insertArr.size(); i++) {
//
//				if (insertArr.get(i).getDsmId() == null || insertArr.get(i).getDsmId().isEmpty() ) {
//					// insert
//					insertCount += iDocumentLifeCycleDAO.insertDocList(insertArr.get(i));
//				} else {
//					insertCount += iDocumentLifeCycleDAO.updateDocList(insertArr.get(i));
//				}
//			}

			if (insertCount == insertArr.size() + DeleteArr.size()) {
				Reslist.setResponseCode(ResponseMessageMap.responseCodeOk);
				Reslist.setResponseDataMessage(ResponseMessageMap.successMsg);
				Reslist.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				Reslist.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				Reslist.setResponseDataMessage(ResponseMessageMap.failMsg);
				Reslist.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}

		} catch (Exception ex) {
			logger.error("insertOrUpdateDoclifeCycle error " + ex);
		}
		return Reslist;
	}

	@Override
	public ResponseAsList getEmpDesignationList(TenantRequest tenReq) {
		ResponseAsList Reslist = new ResponseAsList();
		List<EmployeeDesignationEntity> list=new ArrayList<EmployeeDesignationEntity>();

		try {
			list=iDocumentLifeCycleDAO.getEmpDesigList(tenReq.getTenantID());
			if(list.size()>0) {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeOk);
				Reslist.setResponseMessage(ResponseMessageMap.success);
			}else {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				Reslist.setResponseMessage(ResponseMessageMap.NoData);
			}
		}catch(Exception ex) {
			logger.error("getEmpDesignationList error " + ex);
		}
		return Reslist;
	}

	@Override
	public ResponseAsList getPmIdList(TenantRequest tenReq) {
		ResponseAsList Reslist = new ResponseAsList();
		List<EmployeeDesignationEntity> list=new ArrayList<EmployeeDesignationEntity>();

		try {
			list=iDocumentLifeCycleDAO.getPmIdList(tenReq.getTenantID());
			if(list.size()>0) {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeOk);
				Reslist.setResponseMessage(ResponseMessageMap.success);
			}else {
				Reslist.setResponseData(list);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				Reslist.setResponseMessage(ResponseMessageMap.NoData);
			}
		}catch(Exception ex) {
			logger.error("getEmpDesignationList error " + ex);
		}
		return Reslist;
	}

	@Override
	public ResponseAsMessage deleteDocList(PmHdrIdAndTenantIdRequest docList) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		try {
			
			int deletCheck=iDocumentLifeCycleDAO.deleteDocList(docList);
			if (deletCheck > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successfulDeleted);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
			}
		}catch(Exception ex) {
			logger.error("deleteDocList error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsList getLifeCylceMstVersionDtls(DocLifeCycleLogRequest docLifeCycleLogReq) {
		ResponseAsList Reslist = new ResponseAsList();
		List<DocLifecycleVersionEntity> versionList=new ArrayList<DocLifecycleVersionEntity>();
		try {
			versionList=iDocumentLifeCycleDAO.getVersionList(docLifeCycleLogReq.getDocGroup(),docLifeCycleLogReq.getProcessCode(),docLifeCycleLogReq.getDocType(),docLifeCycleLogReq.getTenantId());
			
			for(int i=0;i<versionList.size();i++) {
				List<DocLifeCycleMstLogEntity> list=iDocumentLifeCycleDAO.getLifeCylceMstVersionDtls(docLifeCycleLogReq.getDocGroup(),docLifeCycleLogReq.getProcessCode(),docLifeCycleLogReq.getDocType(),docLifeCycleLogReq.getTenantId(),versionList.get(i).getVersion());
				for (int j=0;j<list.size();j++) {
					 List<String> desig=new ArrayList<>();
					 desig=iDocumentLifeCycleDAO.getDesignationDesc(list.get(j).getApprDesiCode());
					 String appDesig = String.join(", ", desig);
					 list.get(j).setApprDesi(appDesig);
					 
				}
				versionList.get(i).setData(list);
			}
			
			if(versionList.size()>0) {
				Reslist.setResponseData(versionList);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeOk);
				Reslist.setResponseMessage(ResponseMessageMap.success);
			}else {
				Reslist.setResponseData(versionList);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				Reslist.setResponseMessage(ResponseMessageMap.NoData);
			}
		}catch(Exception ex) {
			logger.error("getLifeCylceMstVersionDtls error " + ex);
		}
		return Reslist;
	}

	@Override
	public ResponseAsMessage updateDocLifeCycleVersion( List<DocLifeCycleMstLogEntity> list) {
		ResponseAsMessage res = new ResponseAsMessage();
		int insertMstCnt=0,insertLogCount=0;
		int deleteStatus=0;
		try {
			int count=iDocumentLifeCycleDAO.getDoclifecycleCount(list.get(0).getDocGroup(), list.get(0).getProcessCode(), list.get(0).getDocType(), list.get(0 ).getTenantId());
			if(count>0) {
				// delete in Doc_lifecycle_mst
				 deleteStatus=iDocumentLifeCycleDAO.deleteDocLifecycleMstList(list.get(0).getDocGroup(), list.get(0).getProcessCode(), list.get(0).getDocType(), list.get(0 ).getTenantId());
			}else {
				deleteStatus=1;
			}
				if(deleteStatus>0) {
				String version =iDocumentLifeCycleDAO.getDocVersion(list.get(0).getDocGroup(), list.get(0).getProcessCode(), list.get(0).getDocType(), list.get(0 ).getTenantId());
					for (int i=0;i<list.size();i++) {
						// insert in Doc_lifecycle_mst
						insertMstCnt += iDocumentLifeCycleDAO.insertDocList(list.get(i));
						
						String statusCode=iDocumentLifeCycleDAO.getStatusCode(list.get(i).getDocStatusDesc(), list.get(i).getTenantId());
						list.get(i).setDocStatus(statusCode);
						// insert in Doc_lifecycle_mst_log 
						insertLogCount += iDocumentLifeCycleDAO.insertDocLifecycleMstLog(list.get(i),version);
					}
					if (insertMstCnt == list.size() && insertLogCount == list.size()) {
						res.setResponseCode(ResponseMessageMap.responseCodeOk);
						res.setResponseDataMessage(ResponseMessageMap.successMsg);
						res.setResponseMessage(ResponseMessageMap.successInserted);
					} else {
						res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						res.setResponseDataMessage(ResponseMessageMap.failMsg);
						res.setResponseMessage(ResponseMessageMap.failtoinsert);
					}
				}else {
					String version =iDocumentLifeCycleDAO.getDocVersion(list.get(0).getDocGroup(), list.get(0).getProcessCode(), list.get(0).getDocType(), list.get(0 ).getTenantId());
					for (int i=0;i<list.size();i++) {
						// insert in Doc_lifecycle_mst
						insertMstCnt += iDocumentLifeCycleDAO.insertDocList(list.get(i));

						String statusCode=iDocumentLifeCycleDAO.getStatusCode(list.get(i).getDocStatusDesc(), list.get(i).getTenantId());
						list.get(i).setDocStatus(statusCode);
						// insert in Doc_lifecycle_mst_log
						insertLogCount += iDocumentLifeCycleDAO.insertDocLifecycleMstLog(list.get(i),version);
					}
					if (insertMstCnt == list.size() && insertLogCount == list.size()) {
						res.setResponseCode(ResponseMessageMap.responseCodeOk);
						res.setResponseDataMessage(ResponseMessageMap.successMsg);
						res.setResponseMessage(ResponseMessageMap.successInserted);
					} else {
						res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
						res.setResponseDataMessage(ResponseMessageMap.failMsg);
						res.setResponseMessage(ResponseMessageMap.failtoinsert);
					}
//					res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
//					res.setResponseDataMessage(ResponseMessageMap.failMsg);
//					res.setResponseMessage(ResponseMessageMap.failTodeleteMsg);
				}
			

		} catch (Exception ex) {
			logger.error("updateDocLifeCycleVersion error " + ex);
		}
		return res;
	}

	@Override
	public ResponseAsMessage setDefaultDoc(List<DocLifeCycleMstLogEntity> list) {
		ResponseAsMessage res = new ResponseAsMessage();
		int insertMstCnt=0,deleteStatus=0;
		try {
			
			int count=iDocumentLifeCycleDAO.getDoclifecycleCount(list.get(0).getDocGroup(), list.get(0).getProcessCode(), list.get(0).getDocType(), list.get(0 ).getTenantId());
			if(count>0) {
				// delete in Doc_lifecycle_mst
				 deleteStatus=iDocumentLifeCycleDAO.deleteDocLifecycleMstList(list.get(0).getDocGroup(), list.get(0).getProcessCode(), list.get(0).getDocType(), list.get(0 ).getTenantId());
			}else {
				deleteStatus=1;
			}
			if(deleteStatus>0) {
				for (int i=0;i<list.size();i++) {
					// insert in Doc_lifecycle_mst
					insertMstCnt += iDocumentLifeCycleDAO.insertDocList(list.get(i));
				}
				if (insertMstCnt == list.size()) {
					res.setResponseCode(ResponseMessageMap.responseCodeOk);
					res.setResponseDataMessage(ResponseMessageMap.successMsg);
					res.setResponseMessage(ResponseMessageMap.successInserted);
				} else {
					res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					res.setResponseDataMessage(ResponseMessageMap.failMsg);
					res.setResponseMessage(ResponseMessageMap.failtoinsert);
				}
			}else {
				res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				res.setResponseDataMessage(ResponseMessageMap.failMsg);
				res.setResponseMessage(ResponseMessageMap.failTodeleteMsg);
			}
			

		} catch (Exception ex) {
			logger.error("setDefaultDoc error " + ex);
		}
		return res;
	}

	@Override
	public ResponseAsList getDocGroups(DocLifeCycleLogRequest req) {
		ResponseAsList Reslist = new ResponseAsList();
		List<DocGroupTypeEntity> docGroups=new ArrayList<DocGroupTypeEntity>();

		try {
			docGroups=iDocumentLifeCycleDAO.getDocGroups(req.getProcessCode(),req.getTenantId(),req.getDocType());
			if(docGroups.size()>0) {
				Reslist.setResponseData(docGroups);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeOk);
				Reslist.setResponseMessage(ResponseMessageMap.success);
			}else {
				Reslist.setResponseData(docGroups);
				Reslist.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				Reslist.setResponseMessage(ResponseMessageMap.NoData);
			}
		}catch(Exception ex) {
			logger.error("getDocTypes error "+ex);
		}
		return Reslist;
	}


	
}
